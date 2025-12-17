package se.fk.github.maskinellregelratttillforsakring.integration.arbetsgivare.dto;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.annotation.Nullable;
import java.time.LocalDate;

@Value.Immutable
@JsonSerialize(as = ImmutableArbetsgivareResponse.class)
@JsonDeserialize(as = ImmutableArbetsgivareResponse.class)
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
