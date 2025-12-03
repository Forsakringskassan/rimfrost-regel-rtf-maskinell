package se.fk.github.maskinellregelratttillforsakring.tests.logic;

import io.quarkus.test.junit.QuarkusTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class LogicMapperTest
{
   /*
   
   private LogicMapper mapper;
   private final String personnummer = "12345678-1234";
   
   @BeforeEach
   void setUp()
   {
      mapper = new LogicMapper();
   }
   
   @Test
   void toFolkbokfordIntegration_mapsPersonnummer()
   {
      var logicReq = ImmutableLogicRtfRequest.builder()
            .personnummer(personnummer)
            .build();
   
      var fbReq = mapper.toFolkbokfordIntegration(logicReq);
   
      assertEquals(personnummer, fbReq.personnummer());
      assertEquals(ImmutableFolkbokfordApiRequest.class, fbReq.getClass());
   }
   
   @Test
   void toArbetsgivareIntegration_mapsPersonnummer()
   {
      var logicReq = ImmutableLogicRtfRequest.builder()
            .personnummer(personnummer)
            .build();
   
      var abReq = mapper.toArbetsgivareIntegration(logicReq);
   
      assertEquals(personnummer, abReq.personnummer());
   }
   
   @ParameterizedTest
   @EnumSource(LogicRattTillForsakring.class)
   
   void toLogic_mapsCorrectly(LogicRattTillForsakring c)
   {
      var logicResp = mapper.toLogic(c);
   
      assertEquals(c, logicResp.rattTillForsakring());
      assertEquals(ImmutableLogicRtfResponse.class, logicResp.getClass());
   }
   */

}
