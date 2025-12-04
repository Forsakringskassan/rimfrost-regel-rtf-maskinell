package se.fk.github.maskinellregelratttillforsakring.integration.folkbokford.dto;

import org.immutables.value.Value;
import se.fk.rimfrost.api.folkbokforing.jaxrsspec.controllers.generatedsource.model.Adress;

@Value.Immutable
public interface FolkbokfordResponse
{
   String id();

   String fornamn();

   String efternamn();

   String utdelningsadress();

   String postnummer();

   String postort();

}
