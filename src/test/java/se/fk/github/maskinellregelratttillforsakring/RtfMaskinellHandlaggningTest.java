package se.fk.github.maskinellregelratttillforsakring;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableUppgiftsdatakopia;
import se.fk.rimfrost.framework.handlaggning.model.Uppgiftsdata;
import se.fk.rimfrost.framework.handlaggning.model.Uppgiftsdatakoppling;
import se.fk.rimfrost.framework.regel.maskinell.base.AbstractRegelMaskinellHandlaggningTest;
import java.util.List;

@QuarkusTest
@QuarkusTestResource.List(
{
      @QuarkusTestResource(WireMockRtfMaskinell.class)
})
public class RtfMaskinellHandlaggningTest extends AbstractRegelMaskinellHandlaggningTest
{

   @Override
   protected List<Uppgiftsdata> createExpectedUnderlag()
   {
      var folkbokfordUnderlagData = "{\"id\":\"19990101-1234\",\"fornamn\":\"Lisa\",\"efternamn\":\"Tass\",\"kon\":\"KVINNA\",\"utdelningsadress\":\"Storgatan 75\",\"postnummer\":\"12345\",\"postort\":\"Orsa\",\"careOf\":\"-\"}";
      var arbetsgivareUnderlagData = "{\"organisationsnummer\":\"987654-3210\",\"organisationsnamn\":\"Demo AB\",\"arbetstidProcent\":100,\"anstallningsdag\":\"2021-07-01\",\"sistaAnstallningsdag\":null}";
      return List.of(
            ImmutableUppgiftsdatakopia.builder()
                  .informationsobjekttyp("Folkbokförd")
                  .data(folkbokfordUnderlagData)
                  .build(),
            ImmutableUppgiftsdatakopia.builder()
                  .informationsobjekttyp("Arbetsgivare")
                  .data(arbetsgivareUnderlagData)
                  .build());
   }

   @Override
   protected List<Uppgiftsdatakoppling> createExpectedResultat()
   {
      // RTF maskinell producerar inga resultat på uppgiften
      return List.of();
   }
}
