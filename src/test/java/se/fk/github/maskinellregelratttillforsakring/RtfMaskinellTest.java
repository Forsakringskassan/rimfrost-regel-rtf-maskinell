package se.fk.github.maskinellregelratttillforsakring;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.spi.Connector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import se.fk.github.maskinellregelratttillforsakring.logic.RtfService;
import se.fk.rimfrost.framework.handlaggning.model.FSSAinformation;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableHandlaggning;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableIndividYrkandeRoll;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableUppgift;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableUppgiftSpecifikation;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableYrkande;
import se.fk.rimfrost.framework.handlaggning.model.UppgiftStatus;
import se.fk.rimfrost.framework.handlaggning.model.Yrkandestatus;
import se.fk.rimfrost.framework.regel.*;
import se.fk.rimfrost.framework.regel.maskinell.logic.dto.ImmutableRegelMaskinellRequest;
import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import static com.github.tomakehurst.wiremock.client.WireMock.anyRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@QuarkusTest
@QuarkusTestResource.List(
{
      @QuarkusTestResource(WireMockTestResource.class)
})
public class RtfMaskinellTest
{

   @Inject
   RtfService rtfService;

   private static final String folkbokforingEndpoint = "/folkbokforing/";
   private static final String arbetsgivareEndpoint = "/arbetsgivare/";
   private static WireMockServer wiremockServer;

   @Inject
   @Connector("smallrye-in-memory")
   InMemoryConnector inMemoryConnector;

   @BeforeAll
   static void setup()
   {
      setupRtfMaskinellTest();
      setupWiremock();
   }

   static void setupRtfMaskinellTest()
   {
      Properties props = new Properties();
      try (InputStream in = RtfMaskinellTest.class.getResourceAsStream("/test.properties"))
      {
         if (in == null)
         {
            throw new RuntimeException("Could not find /test.properties in classpath");
         }
         props.load(in);
      }
      catch (IOException e)
      {
         throw new RuntimeException("Failed to load test.properties", e);
      }
   }

   static void setupWiremock()
   {
      wiremockServer = WireMockTestResource.getWireMockServer();
   }

   public static List<LoggedRequest> waitForWireMockRequest(
         WireMockServer server,
         String urlRegex,
         int minRequests)
   {
      List<LoggedRequest> requests = Collections.emptyList();
      int retries = 20;
      long sleepMs = 250;
      for (int i = 0; i < retries; i++)
      {
         requests = server.findAll(anyRequestedFor(urlMatching(urlRegex)));
         if (requests.size() >= minRequests)
         {
            return requests;
         }
         try
         {
            Thread.sleep(sleepMs);
         }
         catch (InterruptedException e)
         {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting for WireMock request", e);
         }
      }
      return requests;
   }

   private List<? extends Message<?>> waitForMessages(String channel)
   {
      await().atMost(5, TimeUnit.SECONDS).until(() -> !inMemoryConnector.sink(channel).received().isEmpty());
      return inMemoryConnector.sink(channel).received();
   }

   @Test
   public void testHealthEndpoint()
   {
      when()
            .get("/q/health/live")
            .then()
            .statusCode(200)
            .body("status", is("UP"));
   }

   @ParameterizedTest
   @CsvSource(
   {
         "5367f6b8-cc4a-11f0-8de9-199901011234, 19990101-1234, Ja",
         "5367f6b8-cc4a-11f0-8de9-199901013333, 19990101-3333, Utredning",
         "5367f6b8-cc4a-11f0-8de9-199901012222, 19990101-2222, Ja",
         "5367f6b8-cc4a-11f0-8de9-199901014444, 19990101-4444, Nej"
   })
   void TestRtfMaskinellSmoke(UUID handlaggningId,
         String persnr,
         String expectedUtfall) throws Exception
   {
      // Clear out any previous requests
      wiremockServer.resetRequests();

      System.out.printf("Starting RtfMaskinellTest. %S%n", handlaggningId);

      var individ = ImmutableIndividYrkandeRoll.builder().individId(handlaggningId).yrkandeRollId(UUID.randomUUID()).build();

      var yrkande = ImmutableYrkande.builder()
            .id(UUID.randomUUID())
            .addIndividYrkandeRoller(individ)
            .erbjudandeId(UUID.randomUUID())
            .version(1)
            .yrkandeDatum(OffsetDateTime.now())
            .yrkandeFrom(OffsetDateTime.now())
            .yrkandeTom(OffsetDateTime.now())
            .yrkandeStatus(Yrkandestatus.UNDER_UTREDNING)
            .avsikt("avsikt")
            .build();

      var handlaggning = ImmutableHandlaggning.builder()
            .id(UUID.randomUUID())
            .version(1)
            .yrkande(yrkande)
            .processInstansId(UUID.randomUUID())
            .skapadTS(OffsetDateTime.now())
            .handlaggningspecifikationId(UUID.randomUUID())
            .build();

      var uppgiftSpecifikation = ImmutableUppgiftSpecifikation.builder()
            .id(UUID.randomUUID())
            .version(1)
            .build();

      var uppgift = ImmutableUppgift.builder()
            .id(UUID.randomUUID())
            .version(1)
            .skapadTs(OffsetDateTime.now())
            .utforarId(UUID.randomUUID())
            .uppgiftStatus(UppgiftStatus.TILLDELAD)
            .aktivitetId(UUID.randomUUID())
            .fSSAinformation(FSSAinformation.HANDLAGGNING_PAGAR)
            .uppgiftSpecifikation(uppgiftSpecifikation)
            .build();

      var request = ImmutableRegelMaskinellRequest.builder()
            .handlaggning(handlaggning)
            .uppgift(uppgift)
            .processInstansId(UUID.randomUUID())
            .build();

      // Send Rtf maskinell request to start workflow
      var result = rtfService.processRegel(request);

      // Verify processInstansId
      assertEquals(request.processInstansId(), result.handlaggningUpdate().processInstansId());

      // Verify folkbokföring requests
      var folkbokforingRequests = waitForWireMockRequest(wiremockServer, folkbokforingEndpoint + persnr, 1);
      assertEquals(1, folkbokforingRequests.size());
      assertEquals(folkbokforingEndpoint + persnr, folkbokforingRequests.getFirst().getUrl());
      assertEquals(RequestMethod.GET, folkbokforingRequests.getFirst().getMethod());

      // Verify arbetsgivare requests
      var arbetsgivareRequests = waitForWireMockRequest(wiremockServer, arbetsgivareEndpoint + persnr, 1);
      assertEquals(1, arbetsgivareRequests.size());
      assertEquals(arbetsgivareEndpoint + persnr, arbetsgivareRequests.getFirst().getUrl());
      assertEquals(RequestMethod.GET, arbetsgivareRequests.getFirst().getMethod());

      assertEquals("Folkbokförd", result.handlaggningUpdate().underlag().getFirst().typ());
      assertEquals("Arbetsgivare", result.handlaggningUpdate().underlag().getLast().typ());

      assertEquals(Utfall.fromValue(expectedUtfall), result.utfall());
   }
}
