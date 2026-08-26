package se.fk.github.maskinellregelratttillforsakring;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.fk.github.maskinellregelratttillforsakring.logic.RtfService;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableHandlaggning;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableYrkande;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.fk.github.maskinellregelratttillforsakring.RtfMaskinellTestData.newRegelMaskinellRequest;

class RtfServiceKompletteringTest
{

   private final RtfService rtfService = new RtfService();

   @Test
   void should_return_empty_list_when_all_individer_have_personnummer()
   {
      var handlaggning = newRegelMaskinellRequest("19990101-1234").handlaggning();

      assertTrue(rtfService.checkKomplettering(handlaggning).isEmpty());
   }

   @Test
   void should_return_komplettering_when_yrkande_has_no_individer()
   {
      var handlaggningUtanIndivider = ImmutableHandlaggning.builder()
            .from(newRegelMaskinellRequest("19990101-1234").handlaggning())
            .yrkande(ImmutableYrkande.builder()
                  .from(newRegelMaskinellRequest("19990101-1234").handlaggning().yrkande())
                  .individYrkandeRoller(List.of())
                  .build())
            .build();

      var komplettering = rtfService.checkKomplettering(handlaggningUtanIndivider);

      assertEquals(1, komplettering.size());
      assertEquals("Individ", komplettering.getFirst().underlagTyp());
   }
}
