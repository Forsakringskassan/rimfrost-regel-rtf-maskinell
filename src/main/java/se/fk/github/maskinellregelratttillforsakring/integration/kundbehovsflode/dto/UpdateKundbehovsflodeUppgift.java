package se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.immutables.value.Value;

import jakarta.annotation.Nullable;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.FSSAinformation;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.UppgiftStatus;

@Value.Immutable
public interface UpdateKundbehovsflodeUppgift
{
   UUID id();

   String version();

   OffsetDateTime skapadTs();

   @Nullable
   OffsetDateTime utfordTs();

   @Nullable
   OffsetDateTime planeradTs();

   UppgiftStatus uppgiftStatus();

   String aktivitet();

   FSSAinformation fsSAinformation();

   UpdateKundbehovsflodeSpecifikation specifikation();
}
