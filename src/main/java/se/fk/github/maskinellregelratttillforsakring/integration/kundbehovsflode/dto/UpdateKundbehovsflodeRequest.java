package se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.dto;

import org.immutables.value.Value;

import java.util.UUID;

@Value.Immutable
public interface UpdateKundbehovsflodeRequest
{
   UUID kundbehovsflodeId();
}
