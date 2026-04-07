package se.fk.github.maskinellregelratttillforsakring;

import se.fk.rimfrost.framework.handlaggning.model.*;
import se.fk.rimfrost.framework.regel.maskinell.logic.dto.ImmutableRegelMaskinellRequest;
import se.fk.rimfrost.framework.regel.maskinell.logic.dto.RegelMaskinellRequest;
import java.time.OffsetDateTime;
import java.util.UUID;

public class RtfMaskinellTestData
{

   public static RegelMaskinellRequest newRegelMaskinellRequest(UUID handlaggningId)
   {

      var individ = ImmutableIndividYrkandeRoll.builder()
            .individId(handlaggningId)
            .yrkandeRollId(UUID.randomUUID())
            .build();

      var yrkande = ImmutableYrkande.builder()
            .id(UUID.randomUUID())
            .addIndividYrkandeRoller(individ)
            .erbjudandeId(UUID.randomUUID())
            .version(1)
            .yrkandeDatum(OffsetDateTime.now())
            .yrkandeFrom(OffsetDateTime.now())
            .yrkandeTom(OffsetDateTime.now())
            .yrkandeStatus(Yrkandestatus.UNDER_UTREDNING)
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

      var uppgift = ImmutableUppgift.builder()
            .id(UUID.randomUUID())
            .version(1)
            .skapadTs(OffsetDateTime.now())
            .utforarId(UUID.randomUUID())
            .uppgiftStatus(UppgiftStatus.TILLDELAD)
            .aktivitetId(UUID.randomUUID())
            .fSSAinformation(FSSAinformation.HANDLAGGNING_PAGAR)
            .uppgiftSpecifikation(uppgiftSpecifikation)
            .build();

      return ImmutableRegelMaskinellRequest.builder()
            .handlaggning(handlaggning)
            .uppgift(uppgift)
            .processInstansId(UUID.randomUUID())
            .build();
   }
}
