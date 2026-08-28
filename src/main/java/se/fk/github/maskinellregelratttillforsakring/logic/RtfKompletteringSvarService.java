package se.fk.github.maskinellregelratttillforsakring.logic;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;
import se.fk.rimfrost.framework.handlaggning.model.Handlaggning;
import se.fk.rimfrost.framework.handlaggning.model.HandlaggningUpdate;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableHandlaggningUpdate;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableIdtyp;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableIndividYrkandeRoll;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableYrkande;
import se.fk.rimfrost.framework.handlaggning.model.IndividYrkandeRoll;
import se.fk.rimfrost.framework.regel.logic.KompletteringSvarServiceInterface;
import se.fk.rimfrost.regel.rtf.maskinell.jaxrsspec.controllers.generatedsource.model.RtfKompletteringSvar;

@ApplicationScoped
public class RtfKompletteringSvarService implements KompletteringSvarServiceInterface<RtfKompletteringSvar>
{
   private static final String PERSONNUMMER_TYP_ID = "PERSONNUMMER";

   @Override
   public RtfKompletteringSvar readSvarData(Handlaggning handlaggning)
   {
      var individYrkandeRoller = handlaggning.yrkande().individYrkandeRoller();
      if (individYrkandeRoller.isEmpty())
      {
         return new RtfKompletteringSvar(new ArrayList<>());
      }

      var personnummer = individYrkandeRoller.stream()
            .filter(individYrkandeRoll -> PERSONNUMMER_TYP_ID.equals(individYrkandeRoll.individ().typId()))
            .map(individYrkandeRoll -> individYrkandeRoll.individ().varde())
            .collect(Collectors.toList());

      return new RtfKompletteringSvar(personnummer);
   }

   @Override
   public HandlaggningUpdate registerSvar(Handlaggning handlaggning, RtfKompletteringSvar svar)
   {
      var individYrkandeRoller = new ArrayList<IndividYrkandeRoll>(handlaggning.yrkande().individYrkandeRoller());

      for (var personnummer : svar.getPersonnummer())
      {
         individYrkandeRoller.add(createIndividYrkandeRoll(personnummer));
      }

      var uppdateratYrkande = ImmutableYrkande.builder()
            .from(handlaggning.yrkande())
            .individYrkandeRoller(individYrkandeRoller)
            .build();

      return ImmutableHandlaggningUpdate.builder()
            .id(handlaggning.id())
            .version(handlaggning.version())
            .yrkande(uppdateratYrkande)
            .processInstansId(handlaggning.processInstansId())
            .skapadTS(handlaggning.skapadTS())
            .avslutadTS(handlaggning.avslutadTS())
            .handlaggningspecifikationId(handlaggning.handlaggningspecifikationId())
            .build();
   }

   private IndividYrkandeRoll createIndividYrkandeRoll(String personnummer)
   {
      var individ = ImmutableIdtyp.builder()
            .typId(PERSONNUMMER_TYP_ID)
            .varde(personnummer)
            .build();

      return ImmutableIndividYrkandeRoll.builder()
            .individ(individ)
            .yrkandeRollId(UUID.randomUUID().toString())
            .build();
   }
}
