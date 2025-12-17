package se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.dto;

import org.immutables.value.Value;

import se.fk.rimfrost.regel.rtf.maskinell.RattTillForsakring;

import java.util.List;
import java.util.UUID;

@Value.Immutable
public interface UpdateKundbehovsflodeRequest
{
   UUID kundbehovsflodeId();

   RattTillForsakring rattTillForsakring();

   List<UpdateKundbehovsflodeUnderlag> underlag();
}
