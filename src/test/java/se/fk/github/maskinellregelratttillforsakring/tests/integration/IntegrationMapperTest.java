package se.fk.github.maskinellregelratttillforsakring.tests.integration;

import io.quarkus.test.junit.QuarkusTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class IntegrationMapperTest
{
   /*
   private IntegrationMapper mapper;
   private final String personnummer = "12345678-1234";
   
   @BeforeEach
   void setUp()
   {
      mapper = new IntegrationMapper();
   }
   
   @Test
   void toFolkbokfordApi_mapsPersonnummer()
   {
      var folkbokfordRequest = ImmutableFolkbokfordApiRequest.builder()
            .personnummer(personnummer)
            .build();
   
      String apiReq = mapper.toFolkbokfordApi(folkbokfordRequest);
   
      assertEquals(personnummer, apiReq);
   }
   
   @Test
   void toArbetsgivareApi_mapsPersonnummer()
   {
      var arbetsgivareRequest = ImmutableArbetsgivareApiRequest.builder()
            .personnummer(personnummer)
            .build();
   
      String apiReq = mapper.toArbetsgivareApi(arbetsgivareRequest);
   
      assertEquals(personnummer, apiReq);
   }
   
   @ParameterizedTest
   @CsvSource(
   {
         "true",
         "false"
   })
   void fromFolkbokfordApi_mapsBooleanCorrectly(boolean result)
   {
      var folkbokfordResponse = new FolkbokforingPersnrGet200Response();
      folkbokfordResponse.setResult(result);
   
      var integrationResponse = mapper.fromFolkbokfordApi(folkbokfordResponse);
   
      assertEquals(result, integrationResponse.isFolkbokford());
   }
   
   @ParameterizedTest
   @CsvSource(
   {
         "true",
         "false"
   })
   void fromArbetsgivareApi_mapsBooleanCorrectly(boolean result)
   {
      var ArbetsgivareResponse = new GetArbetsgivare200Response();
      ArbetsgivareResponse.setResult(result);
   
      var integrationResponse = mapper.fromArbetsgivareApi(ArbetsgivareResponse);
   
      assertEquals(result, integrationResponse.hasArbetsgivare());
   }*/
}
