package se.fk.github.maskinellregelratttillforsakring.tests.presentation;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class PresentationMapperTest
{
   /*
   private PresentationMapper mapper;
   private final String personnummer = "12345678-1234";
   private final UUID processId = UUID.randomUUID();
   
   @BeforeEach
   void setUp()
   {
      mapper = new PresentationMapper();
   }
   
   @Test
   void toRattTillForsakringRequest_mapsCorrectly()
   {
      var rtfRequest = new VahRtfRequestMessageData();
      rtfRequest.setPersonNummer(personnummer);
   
      var rattTillforsakringRequest = mapper.toRattTillForsakringRequest(rtfRequest);
   
      assertEquals(personnummer, rattTillforsakringRequest.personnummer());
      assertEquals(ImmutableLogicRtfRequest.class, rattTillforsakringRequest.getClass());
   }
   
   private static RattTillForsakring expected(LogicRattTillForsakring c){
       return switch(c){
           case JA -> RattTillForsakring.JA;
           case NEJ -> RattTillForsakring.NEJ;
           case UTREDNING -> RattTillForsakring.UTREDNING;
       };
   }
   
   @ParameterizedTest
   @EnumSource(LogicRattTillForsakring.class)
   void toExternalApi_mapsCorrectly(LogicRattTillForsakring r)
   {
      var logicResponse = ImmutableLogicRtfResponse.builder()
            .rattTillForsakring(r)
            .build();
   
      var dummyData = new VahRtfRequestMessageData();
      dummyData.setProcessId(processId.toString());
      dummyData.setPersonNummer(personnummer);
   
      var dummyRequest = new VahRtfRequestMessagePayload();
      dummyRequest.setData(dummyData);
      dummyRequest.setId("123");
      dummyRequest.setSource("234");
      dummyRequest.setType("vah-rtf-responses");
      dummyRequest.setKogitorootprocid("345");
      dummyRequest.setKogitorootprociid("456");
      dummyRequest.setKogitoparentprociid("567");
      dummyRequest.setKogitoprocid("678");
      dummyRequest.setKogitoprocinstanceid("789");
      dummyRequest.setKogitoprocrefid("890");
      dummyRequest.setKogitoprocist("901");
      dummyRequest.setKogitoproctype(KogitoProcType.BPMN);
      dummyRequest.setKogitoprocversion("1.1");
   
      var toExternalResponse = mapper.toRtfResponsePayload(logicResponse, dummyRequest);
   
      assertEquals(expected(r), toExternalResponse.getData().getRattTillForsakring());
      assertEquals(VahRtfResponseMessagePayload.class, toExternalResponse.getClass());
   }*/
}
