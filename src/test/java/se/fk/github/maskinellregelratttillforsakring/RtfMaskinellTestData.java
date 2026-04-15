package se.fk.github.maskinellregelratttillforsakring;

import se.fk.rimfrost.framework.handlaggning.model.*;
import se.fk.rimfrost.framework.regel.maskinell.logic.dto.ImmutableRegelMaskinellRequest;
import se.fk.rimfrost.framework.regel.maskinell.logic.dto.RegelMaskinellRequest;
import java.time.OffsetDateTime;
import java.util.UUID;

public class RtfMaskinellTestData
{

   public static RegelMaskinellRequest newRegelMaskinellRequest(String persnr)
   {

      var idTyp = ImmutableIdtyp.builder()
            .typId(UUID.randomUUID().toString())
            .varde(persnr)
            .build();

      var individ = ImmutableIndividYrkandeRoll.builder()
            .individ(idTyp)
            .yrkandeRollId(UUID.randomUUID().toString())
            .build();

      var yrkande = ImmutableYrkande.builder()
            .id(UUID.randomUUID())
            .addIndividYrkandeRoller(individ)
            .erbjudandeId(UUID.randomUUID().toString())
            .version(1)
            .yrkandeDatum(OffsetDateTime.now())
            .yrkandeFrom(OffsetDateTime.now())
            .yrkandeTom(OffsetDateTime.now())
            .yrkandeStatus("UNDER_UTREDNING")
            .avsikt("avsikt")
            .build();

      var handlaggning = ImmutableHandlaggning.builder()
            .id(UUID.randomUUID())
            .version(1)
            .yrkande(yrkande)
            .processInstansId(UUID.randomUUID())
            .skapadTS(OffsetDateTime.now())
            .handlaggningspecifikationId(UUID.randomUUID())
            .build();

      var uppgiftSpecifikation = ImmutableUppgiftSpecifikation.builder()
            .id(UUID.randomUUID())
            .version(1)
            .build();

      var utforare = ImmutableIdtyp.builder()
            .typId(UUID.randomUUID().toString())
            .varde(UUID.randomUUID().toString())
            .build();

      var uppgift = ImmutableUppgift.builder()
            .id(UUID.randomUUID())
            .version(1)
            .skapadTs(OffsetDateTime.now())
            .utforarId(utforare)
            .uppgiftStatus("2") // TILLDELAD
            .aktivitetId(UUID.randomUUID())
            .fSSAinformation("HANDLAGGNING_PAGAR") // TODO: Replace when correct value is available
            .uppgiftSpecifikation(uppgiftSpecifikation)
            .build();

      return ImmutableRegelMaskinellRequest.builder()
            .handlaggning(handlaggning)
            .uppgift(uppgift)
            .processInstansId(UUID.randomUUID())
            .build();
   }
}
