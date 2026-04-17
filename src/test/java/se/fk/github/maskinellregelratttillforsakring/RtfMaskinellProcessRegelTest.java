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
import se.fk.rimfrost.framework.regel.maskinell.RegelMaskinellTestBase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static se.fk.github.maskinellregelratttillforsakring.RtfMaskinellTestData.newRegelMaskinellRequest;
import static se.fk.rimfrost.framework.regel.test.WireMockHandlaggning.waitForRequest;

@QuarkusTest
@QuarkusTestResource.List(
{
      @QuarkusTestResource(WireMockRtfMaskinell.class)
})
public class RtfMaskinellProcessRegelTest extends RegelMaskinellTestBase
{

   @Inject
   RtfService rtfService;

   private static final String folkbokforingEndpoint = "/folkbokforing/";
   private static final String arbetsgivareEndpoint = "/arbetsgivare/";

   @ParameterizedTest
   @CsvSource(
   {
         "19990101-1234"
   })
   void process_regel_should_return_correct_process_instans_id(String persnr)
   {

      var request = newRegelMaskinellRequest(persnr);

      var result = rtfService.processRegel(request);

      // Verify process instance id
      assertEquals(request.processInstansId(), result.handlaggningUpdate().processInstansId());
   }

   @ParameterizedTest
   @CsvSource(
   {
         "19990101-1234",
         "19990101-3333"
   })
   void process_regel_should_produce_folkbokforing_requests(String persnr)
   {
      var request = newRegelMaskinellRequest(persnr);

      rtfService.processRegel(request);

      // Verify folkbokföring requests
      var folkbokforingRequests = WireMockRtfMaskinell.waitForRequest(folkbokforingEndpoint + persnr, RequestMethod.GET, 1);
      assertEquals(1, folkbokforingRequests.size());
      assertEquals(folkbokforingEndpoint + persnr, folkbokforingRequests.getFirst().getUrl());
   }

   @ParameterizedTest
   @CsvSource(
   {
         "19990101-1234",
         "19990101-3333"
   })
   void process_regel_should_produce_arbetsgivare_requests(String persnr)
   {
      var request = newRegelMaskinellRequest(persnr);

      rtfService.processRegel(request);

      // Verify arbetsgivare requests
      var arbetsgivareRequests = WireMockRtfMaskinell.waitForRequest(arbetsgivareEndpoint + persnr, RequestMethod.GET, 1);
      assertEquals(1, arbetsgivareRequests.size());
      assertEquals(arbetsgivareEndpoint + persnr, arbetsgivareRequests.getFirst().getUrl());
   }

   @ParameterizedTest
   @CsvSource(
   {
         "19990101-1234"
   })
   void process_regel_should_return_correct_underlag(String persnr)
   {
      var request = newRegelMaskinellRequest(persnr);

      var result = rtfService.processRegel(request);

      // Verify underlag
      assertEquals("Folkbokförd", result.handlaggningUpdate().underlag().getFirst().typ());
      assertEquals("Arbetsgivare", result.handlaggningUpdate().underlag().getLast().typ());

   }

   @ParameterizedTest
   @CsvSource(
   {
         "19990101-1234"
   })
   void process_regel_should_set_uppgift_uppgiftStatus_and_utfordTs_fields(String persnr)
   {
      var request = newRegelMaskinellRequest(persnr);

      var result = rtfService.processRegel(request);

      assertEquals("3", result.handlaggningUpdate().uppgift().uppgiftStatus());
      assertNotNull(result.handlaggningUpdate().uppgift().utfordTs());
   }

   @ParameterizedTest
   @CsvSource(
   {
         "19990101-1234, Ja",
         "19990101-3333, Utredning",
         "19990101-2222, Ja",
         "19990101-4444, Nej"
   })
   void process_regel_should_return_correct_utfall(String persnr, String expectedUtfall)
   {
      var request = newRegelMaskinellRequest(persnr);

      var result = rtfService.processRegel(request);

      // Verify utfall
      assertEquals(Utfall.fromValue(expectedUtfall), result.utfall());

   }
}
