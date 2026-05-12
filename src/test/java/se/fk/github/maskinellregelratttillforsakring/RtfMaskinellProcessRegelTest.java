package se.fk.github.maskinellregelratttillforsakring;

import com.github.tomakehurst.wiremock.http.RequestMethod;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import se.fk.github.maskinellregelratttillforsakring.logic.RegelFelkod;
import se.fk.github.maskinellregelratttillforsakring.logic.RtfService;
import se.fk.rimfrost.adapter.arbetsgivare.dto.ArbetsgivareResponse;
import se.fk.rimfrost.adapter.arbetsgivare.dto.ImmutableArbetsgivareResponse;
import se.fk.rimfrost.adapter.folkbokford.dto.FolkbokfordResponse;
import se.fk.rimfrost.adapter.folkbokford.dto.ImmutableFolkbokfordResponse;
import se.fk.rimfrost.framework.regel.Utfall;
import se.fk.rimfrost.framework.regel.logic.RegelUtils;
import se.fk.rimfrost.framework.regel.maskinell.base.AbstractRegelMaskinellTest;
import se.fk.rimfrost.framework.regel.maskinell.logic.dto.RegelMaskinellErrorResult;
import se.fk.rimfrost.framework.regel.maskinell.logic.dto.RegelMaskinellSuccessResult;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static se.fk.github.maskinellregelratttillforsakring.RtfMaskinellTestData.newRegelMaskinellRequest;

@QuarkusTest
@QuarkusTestResource.List(
{
      @QuarkusTestResource(WireMockRtfMaskinell.class)
})
public class RtfMaskinellProcessRegelTest extends AbstractRegelMaskinellTest
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

      // Verify result type
      assertInstanceOf(RegelMaskinellSuccessResult.class, result);

      // Verify process instance id
      var successResult = (RegelMaskinellSuccessResult) result;
      assertEquals(request.processInstansId(), successResult.handlaggningUpdate().processInstansId());
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

      // Verify result type
      assertInstanceOf(RegelMaskinellSuccessResult.class, result);

      // Verify underlag
      var successResult = (RegelMaskinellSuccessResult) result;
      assertEquals("Folkbokförd", successResult.handlaggningUpdate().underlag().getFirst().typ());
      assertEquals("Arbetsgivare", successResult.handlaggningUpdate().underlag().getLast().typ());

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

      // Verify result type
      assertInstanceOf(RegelMaskinellSuccessResult.class, result);

      var successResult = (RegelMaskinellSuccessResult) result;
      assertEquals("3", successResult.handlaggningUpdate().uppgift().uppgiftStatus());
      assertNotNull(successResult.handlaggningUpdate().uppgift().utfordTs());
   }

   @ParameterizedTest
   @CsvSource(
   {
         "19990101-1234, Ja",
         "19990101-3333, Utredning",
         "19990101-2222, Ja",
         "19990101-4444, Nej",
         "19990101-9999, Ja"
   })
   void process_regel_should_return_correct_utfall(String persnr, String expectedUtfall)
   {
      var request = newRegelMaskinellRequest(persnr);

      var result = rtfService.processRegel(request);

      // Verify result type
      assertInstanceOf(RegelMaskinellSuccessResult.class, result);

      // Verify utfall
      var successResult = (RegelMaskinellSuccessResult) result;
      assertEquals(Utfall.fromValue(expectedUtfall), successResult.utfall());

   }

   @ParameterizedTest
   @CsvSource(
   {
         "19990102-9999, (?i)^Failed to read arbetsgivare response\\..*, RTF_MASKINELL_ARBETSGIVARE_READ_ERROR",
         "19990103-9999, (?i)^Failed to read folkbokford response\\..*, RTF_MASKINELL_FOLKBOKFORING_READ_ERROR"
   })
   void process_regel_should_return_error_result_due_to_retries_exhausted(String persnr, String expectedMsgRegex,
         String expectedErrorCode)
   {
      var request = newRegelMaskinellRequest(persnr);

      var result = rtfService.processRegel(request);

      // Verify result type
      assertInstanceOf(RegelMaskinellErrorResult.class, result);

      // Verify error
      var errorResult = (RegelMaskinellErrorResult) result;
      var regelErrorInformation = errorResult.regelErrorInformation();

      assertNotNull(regelErrorInformation);
      assertEquals(expectedErrorCode, regelErrorInformation.getFelkod());
      assertTrue(regelErrorInformation.getFelmeddelande().matches(expectedMsgRegex));
   }

   @ParameterizedTest
   @CsvSource(
   {
         "19990101-1234, (?i)^Failed to create underlag from folkbokforing response\\..*"
   })
   void process_regel_should_return_error_result_due_to_folkbokford_underlag_creation_failure(String persnr,
         String expectedMsgRegex)
   {
      var request = newRegelMaskinellRequest(persnr);

      try (MockedStatic<RegelUtils> ctx = mockStatic(RegelUtils.class))
      {
         ctx.when(() -> RegelUtils.createUnderlag(Mockito.any(), Mockito.anyInt(), eq(createFolkbokfordResponse(persnr)),
               Mockito.any())).thenThrow(new InternalError());
         var result = rtfService.processRegel(request);

         // Verify result type
         assertInstanceOf(RegelMaskinellErrorResult.class, result);

         // Verify error
         var errorResult = (RegelMaskinellErrorResult) result;
         var regelErrorInformation = errorResult.regelErrorInformation();

         assertNotNull(regelErrorInformation);
         assertEquals(RegelFelkod.RTF_MASKINELL_FOLKBOKFORING_UNDERLAG_CREATION_FAILURE, regelErrorInformation.getFelkod());
         assertTrue(regelErrorInformation.getFelmeddelande().matches(expectedMsgRegex));
      }
   }

   @ParameterizedTest
   @CsvSource(
   {
         "19990101-1234, (?i)^Failed to create underlag from arbetsgivare response\\..*"
   })
   void process_regel_should_return_error_result_due_to_arbetsgivare_underlag_creation_failure(String persnr,
         String expectedMsgRegex)
   {
      var request = newRegelMaskinellRequest(persnr);

      try (MockedStatic<RegelUtils> ctx = mockStatic(RegelUtils.class))
      {
         ctx.when(() -> RegelUtils.createUnderlag(Mockito.any(), Mockito.anyInt(), eq(createFolkbokfordResponse(persnr)),
               Mockito.any())).thenReturn(null);
         ctx.when(
               () -> RegelUtils.createUnderlag(Mockito.any(), Mockito.anyInt(), eq(createArbetsgivareResponse()), Mockito.any()))
               .thenThrow(new InternalError());
         var result = rtfService.processRegel(request);

         // Verify result type
         assertInstanceOf(RegelMaskinellErrorResult.class, result);

         // Verify error
         var errorResult = (RegelMaskinellErrorResult) result;
         var regelErrorInformation = errorResult.regelErrorInformation();

         assertNotNull(regelErrorInformation);
         assertEquals(RegelFelkod.RTF_MASKINELL_ARBETSGIVARE_UNDERLAG_CREATION_FAILURE, regelErrorInformation.getFelkod());
         assertTrue(regelErrorInformation.getFelmeddelande().matches(expectedMsgRegex));
      }
   }

   private static FolkbokfordResponse createFolkbokfordResponse(String persnr)
   {
      return ImmutableFolkbokfordResponse.builder()
            .id(persnr)
            .fornamn("Lisa")
            .efternamn("Tass")
            .kon(FolkbokfordResponse.Kon.KVINNA)
            .utdelningsadress("Storgatan 75")
            .postnummer("12345")
            .postort("Orsa")
            .careOf("-")
            .build();
   }

   private static ArbetsgivareResponse createArbetsgivareResponse()
   {
      return ImmutableArbetsgivareResponse.builder()
            .organisationsnummer("987654-3210")
            .organisationsnamn("Demo AB")
            .arbetstidProcent(100)
            .anstallningsdag(LocalDate.parse("2021-07-01"))
            .sistaAnstallningsdag(null)
            .build();
   }
}
