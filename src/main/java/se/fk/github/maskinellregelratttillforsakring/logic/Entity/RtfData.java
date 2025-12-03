package se.fk.github.maskinellregelratttillforsakring.logic.Entity;

import org.immutables.value.Value;

import java.util.UUID;

@Value.Immutable
public interface RtfData
{

   UUID kundebehovsflodeId();

   boolean rattTillForsakring();

}
