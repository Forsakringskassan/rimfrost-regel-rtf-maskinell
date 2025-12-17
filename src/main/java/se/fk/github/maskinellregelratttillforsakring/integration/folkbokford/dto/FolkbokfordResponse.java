package se.fk.github.maskinellregelratttillforsakring.integration.folkbokford.dto;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableFolkbokfordResponse.class)
@JsonDeserialize(as = ImmutableFolkbokfordResponse.class)
public interface FolkbokfordResponse
{
   String id();

   String fornamn();

   String efternamn();

   String utdelningsadress();

   String postnummer();

   String postort();

}
