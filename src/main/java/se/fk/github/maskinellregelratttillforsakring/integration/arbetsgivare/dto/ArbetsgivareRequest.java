package se.fk.github.maskinellregelratttillforsakring.integration.arbetsgivare.dto;

import org.immutables.value.Value;

@Value.Immutable
public interface ArbetsgivareRequest
{

   String personnummer();

}
