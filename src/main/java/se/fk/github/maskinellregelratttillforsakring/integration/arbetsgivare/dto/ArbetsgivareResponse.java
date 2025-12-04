package se.fk.github.maskinellregelratttillforsakring.integration.arbetsgivare.dto;

import jakarta.validation.constraints.Null;
import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.time.LocalDate;

@Value.Immutable
public interface ArbetsgivareResponse
{

   @Nullable
   String organisationsNr();

   @Nullable
   Integer arbetstid();

   @Nullable
   LocalDate startdag();

   @Nullable
   LocalDate slutdag();

}
