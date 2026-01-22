package se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.dto;

import org.immutables.value.Value;

import se.fk.rimfrost.regel.common.Utfall;

import java.util.List;
import java.util.UUID;

@Value.Immutable
public interface UpdateKundbehovsflodeRequest
{
   UUID kundbehovsflodeId();

   UpdateKundbehovsflodeUppgift uppgift();

   Utfall utfall();

   List<UpdateKundbehovsflodeUnderlag> underlag();
}
