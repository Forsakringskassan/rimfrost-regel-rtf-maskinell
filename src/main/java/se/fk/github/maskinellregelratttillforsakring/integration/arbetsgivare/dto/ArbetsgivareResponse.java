package se.fk.github.maskinellregelratttillforsakring.integration.arbetsgivare.dto;

import org.immutables.value.Value;

@Value.Immutable
public interface ArbetsgivareResponse
{
   String organisationsNr();

   Integer arbetstid();

   String startdag();

   String slutdag();

}
