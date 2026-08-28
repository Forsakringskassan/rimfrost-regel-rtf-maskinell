package se.fk.github.maskinellregelratttillforsakring;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.fk.github.maskinellregelratttillforsakring.logic.RtfKompletteringSvarService;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableHandlaggning;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableYrkande;
import se.fk.rimfrost.regel.rtf.maskinell.jaxrsspec.controllers.generatedsource.model.RtfKompletteringSvar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.fk.github.maskinellregelratttillforsakring.RtfMaskinellTestData.newRegelMaskinellRequest;

class RtfKompletteringSvarServiceTest
{

   private final RtfKompletteringSvarService svarService = new RtfKompletteringSvarService();

   @Test
   void should_return_empty_personnummer_list_when_no_individer()
   {
      var handlaggningUtanIndivider = ImmutableHandlaggning.builder()
            .from(newRegelMaskinellRequest("19990101-1234").handlaggning())
            .yrkande(ImmutableYrkande.builder()
                  .from(newRegelMaskinellRequest("19990101-1234").handlaggning().yrkande())
                  .individYrkandeRoller(List.of())
                  .build())
            .build();

      var svarData = svarService.readSvarData(handlaggningUtanIndivider);

      assertEquals(List.of(), svarData.getPersonnummer());
   }

   @Test
   void should_return_personnummer_for_each_individ()
   {
      var handlaggning = newRegelMaskinellRequest("19990101-1234").handlaggning();

      var svarData = svarService.readSvarData(handlaggning);

      assertEquals(List.of("19990101-1234"), svarData.getPersonnummer());
   }

   @Test
   void should_add_individer_for_each_registered_personnummer()
   {
      var handlaggningUtanIndivider = ImmutableHandlaggning.builder()
            .from(newRegelMaskinellRequest("19990101-1234").handlaggning())
            .yrkande(ImmutableYrkande.builder()
                  .from(newRegelMaskinellRequest("19990101-1234").handlaggning().yrkande())
                  .individYrkandeRoller(List.of())
                  .build())
            .build();

      var svar = new RtfKompletteringSvar(List.of("19990101-1234", "19990101-3333"));

      var update = svarService.registerSvar(handlaggningUtanIndivider, svar);

      var individYrkandeRoller = update.yrkande().individYrkandeRoller();
      assertEquals(2, individYrkandeRoller.size());
      assertEquals("19990101-1234", individYrkandeRoller.get(0).individ().varde());
      assertEquals("19990101-3333", individYrkandeRoller.get(1).individ().varde());
      assertEquals("c5f2e2b4-9143-4160-8f4b-30c172f0ac05", individYrkandeRoller.get(0).individ().typId());
   }

   @Test
   void should_keep_existing_individer_when_adding_new_ones()
   {
      var handlaggning = newRegelMaskinellRequest("19990101-1234").handlaggning();
      var befintligtAntal = handlaggning.yrkande().individYrkandeRoller().size();

      var svar = new RtfKompletteringSvar(List.of("19990101-3333"));

      var update = svarService.registerSvar(handlaggning, svar);

      assertEquals(befintligtAntal + 1, update.yrkande().individYrkandeRoller().size());
   }

   @Test
   void should_not_add_duplicate_individ_for_already_registered_personnummer()
   {
      var handlaggning = newRegelMaskinellRequest("19990101-1234").handlaggning();
      var befintligtAntal = handlaggning.yrkande().individYrkandeRoller().size();

      var svar = new RtfKompletteringSvar(List.of("19990101-1234", "19990101-3333"));

      var update = svarService.registerSvar(handlaggning, svar);

      assertEquals(befintligtAntal + 1, update.yrkande().individYrkandeRoller().size());
   }
}
