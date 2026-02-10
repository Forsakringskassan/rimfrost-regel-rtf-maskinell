package se.fk.github.maskinellregelratttillforsakring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import se.fk.rimfrost.framework.regel.*;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Ersattning;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.PutKundbehovsflodeRequest;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.UppgiftStatus;

import java.io.IOException;
import java.io.InputStream;
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

   private static final String regelRequestsChannel = "regel-requests";
   private static final String regelResponsesChannel = "regel-responses";
   private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
         .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
   private static final String kundbehovsflodeEndpoint = "/kundbehovsflode/";
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

   private void sendRtfMaskinellRequest(String kundbehovsflodeId) throws Exception
   {
      RegelRequestMessagePayload payload = new RegelRequestMessagePayload();
      RegelRequestMessagePayloadData data = new RegelRequestMessagePayloadData();
      data.setKundbehovsflodeId(kundbehovsflodeId);
      payload.setSpecversion(SpecVersion.NUMBER_1_DOT_0);
      payload.setId("99994567-89ab-4cde-9012-3456789abcde");
      payload.setSource("TestSource-001");
      payload.setType(regelRequestsChannel);
      payload.setKogitoprocid("234567");
      payload.setKogitorootprocid("123456");
      payload.setKogitorootprociid("77774567-89ab-4cde-9012-3456789abcde");
      payload.setKogitoparentprociid("88884567-89ab-4cde-9012-3456789abcde");
      payload.setKogitoprocinstanceid("66664567-89ab-4cde-9012-3456789abcde");
      payload.setKogitoprocist("345678");
      payload.setKogitoprocversion("111");
      payload.setKogitoproctype(KogitoProcType.BPMN);
      payload.setKogitoprocrefid("56789");
      payload.setData(data);
      inMemoryConnector.source(regelRequestsChannel).send(payload);
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
         "5367f6b8-cc4a-11f0-8de9-199901011234, 19990101-1234, Ja, JA",
         "5367f6b8-cc4a-11f0-8de9-199901013333, 19990101-3333, Utredning, FU",
         "5367f6b8-cc4a-11f0-8de9-199901012222, 19990101-2222, Ja, JA",
         "5367f6b8-cc4a-11f0-8de9-199901014444, 19990101-4444, Nej, NEJ"
   })
   void TestRtfMaskinellSmoke(String kundbehovsflodeId,
         String persnr,
         String expectedUtfall, Ersattning.BeslutsutfallEnum expectedBeslutsutfall) throws Exception
   {
      // Clear out any previous requests
      wiremockServer.resetRequests();

      // Clear out any previous messages
      inMemoryConnector.sink(regelResponsesChannel).clear();

      System.out.printf("Starting RtfMaskinellTest. %S%n", kundbehovsflodeId);

      // Send Rtf maskinell request to start workflow
      sendRtfMaskinellRequest(kundbehovsflodeId);

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

      // Verify kundbehovsflöde requests
      var kundbehovsflodeRequests = waitForWireMockRequest(wiremockServer,
            kundbehovsflodeEndpoint + kundbehovsflodeId, 3);

      assertEquals(3, kundbehovsflodeRequests.size());

      for (var kundbehovsflodeRequest : kundbehovsflodeRequests)
      {
         assertEquals(kundbehovsflodeEndpoint + kundbehovsflodeId, kundbehovsflodeRequest.getUrl());
      }

      assertEquals(2, kundbehovsflodeRequests.stream().filter(r -> r.getMethod().equals(RequestMethod.GET)).count());
      assertEquals(1, kundbehovsflodeRequests.stream().filter(r -> r.getMethod().equals(RequestMethod.PUT)).count());

      var putRequest = kundbehovsflodeRequests.stream().filter(r -> r.getMethod().equals(RequestMethod.PUT)).findFirst()
            .orElseThrow();
      var sentPutKundbehovsflodeRequest = mapper.readValue(putRequest.getBodyAsString(), PutKundbehovsflodeRequest.class);
      assertEquals(UppgiftStatus.AVSLUTAD, sentPutKundbehovsflodeRequest.getUppgift().getUppgiftStatus());
      assertEquals("TestUppgiftNamn", sentPutKundbehovsflodeRequest.getUppgift().getUppgiftspecifikation().getNamn());
      assertEquals("TestUppgiftBeskrivning",
            sentPutKundbehovsflodeRequest.getUppgift().getUppgiftspecifikation().getUppgiftbeskrivning());

      var sentUnderlag = sentPutKundbehovsflodeRequest.getUppgift().getUnderlag();
      assertEquals(2, sentUnderlag.size());
      assertEquals("FolkbokfördUnderlag", sentUnderlag.getFirst().getTyp());
      assertEquals("ArbetsgivareUnderlag", sentUnderlag.getLast().getTyp());

      var sentKundBehov = sentPutKundbehovsflodeRequest.getUppgift().getKundbehovsflode().getKundbehov();
      for (var ersattning : sentKundBehov.getErsattning())
      {
         assertEquals(expectedBeslutsutfall, ersattning.getBeslutsutfall());
      }

      // Verify rule response
      var messages = waitForMessages(regelResponsesChannel);
      assertEquals(1, messages.size());

      var message = messages.getFirst().getPayload();
      assertInstanceOf(RegelResponseMessagePayload.class, message);

      var rtfMaskinellResponse = (RegelResponseMessagePayload) message;
      assertEquals(kundbehovsflodeId, rtfMaskinellResponse.getData().getKundbehovsflodeId());
      assertEquals(expectedUtfall, rtfMaskinellResponse.getData().getUtfall().getValue());
   }
}
