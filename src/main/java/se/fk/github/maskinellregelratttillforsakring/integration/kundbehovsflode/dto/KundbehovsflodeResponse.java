package se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.dto;

import jakarta.annotation.Nullable;
import org.immutables.value.Value;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Value.Immutable
public interface KundbehovsflodeResponse
{

   UUID kundbehovsflodeId();

   String personnummer();

}
