package se.fk.github.maskinellregelratttillforsakring;

import com.github.tomakehurst.wiremock.http.RequestMethod;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import se.fk.github.maskinellregelratttillforsakring.logic.RtfService;
import se.fk.rimfrost.framework.regel.Utfall;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.fk.github.maskinellregelratttillforsakring.RtfMaskinellTestData.newRegelMaskinellRequest;

@QuarkusTest
@QuarkusTestResource.List(
{
      @QuarkusTestResource(WireMockTestResource.class)
})
public class RtfMaskinellProcessRegelTest extends AbstractRtfMaskinellTest
{

   @Inject
   RtfService rtfService;

   private static final String folkbokforingEndpoint = "/folkbokforing/";
   private static final String arbetsgivareEndpoint = "/arbetsgivare/";

   @ParameterizedTest
   @CsvSource(
   {
         "5367f6b8-cc4a-11f0-8de9-199901011234"
   })
   void process_regel_should_return_correct_process_instans_id(UUID handlaggningId)
   {

      var request = newRegelMaskinellRequest(handlaggningId);

      var result = rtfService.processRegel(request);

      // Verify process instance id
      assertEquals(request.processInstansId(), result.handlaggningUpdate().processInstansId());
   }

   @ParameterizedTest
   @CsvSource(
   {
         "5367f6b8-cc4a-11f0-8de9-199901011234, 19990101-1234",
         "5367f6b8-cc4a-11f0-8de9-199901013333, 19990101-3333"
   })
   void process_regel_should_produce_folkbokforing_requests(UUID handlaggningId,
         String persnr)
   {
      var request = newRegelMaskinellRequest(handlaggningId);

      rtfService.processRegel(request);

      // Verify folkbokföring requests
      var folkbokforingRequests = waitForWireMockRequest(wiremockServer, folkbokforingEndpoint + persnr, 1);
      assertEquals(1, folkbokforingRequests.size());
      assertEquals(folkbokforingEndpoint + persnr, folkbokforingRequests.getFirst().getUrl());
      assertEquals(RequestMethod.GET, folkbokforingRequests.getFirst().getMethod());
   }

   @ParameterizedTest
   @CsvSource(
   {
         "5367f6b8-cc4a-11f0-8de9-199901011234, 19990101-1234",
         "5367f6b8-cc4a-11f0-8de9-199901013333, 19990101-3333"
   })
   void process_regel_should_produce_arbetsgivare_requests(UUID handlaggningId,
         String persnr)
   {
      var request = newRegelMaskinellRequest(handlaggningId);

      rtfService.processRegel(request);

      // Verify arbetsgivare requests
      var arbetsgivareRequests = waitForWireMockRequest(wiremockServer, arbetsgivareEndpoint + persnr, 1);
      assertEquals(1, arbetsgivareRequests.size());
      assertEquals(arbetsgivareEndpoint + persnr, arbetsgivareRequests.getFirst().getUrl());
      assertEquals(RequestMethod.GET, arbetsgivareRequests.getFirst().getMethod());
   }

   @ParameterizedTest
   @CsvSource(
   {
         "5367f6b8-cc4a-11f0-8de9-199901011234"
   })
   void process_regel_should_return_correct_underlag(UUID handlaggningId)
   {
      var request = newRegelMaskinellRequest(handlaggningId);

      var result = rtfService.processRegel(request);

      // Verify underlag
      assertEquals("Folkbokförd", result.handlaggningUpdate().underlag().getFirst().typ());
      assertEquals("Arbetsgivare", result.handlaggningUpdate().underlag().getLast().typ());

   }

   @ParameterizedTest
   @CsvSource(
   {
         "5367f6b8-cc4a-11f0-8de9-199901011234, Ja",
         "5367f6b8-cc4a-11f0-8de9-199901013333, Utredning",
         "5367f6b8-cc4a-11f0-8de9-199901012222, Ja",
         "5367f6b8-cc4a-11f0-8de9-199901014444, Nej"
   })
   void process_regel_should_return_correct_utfall(UUID handlaggningId,
         String expectedUtfall)
   {
      var request = newRegelMaskinellRequest(handlaggningId);

      var result = rtfService.processRegel(request);

      // Verify utfall
      assertEquals(Utfall.fromValue(expectedUtfall), result.utfall());

   }
}
