package se.fk.github.maskinellregelratttillforsakring.integration.kafka.dto;

import org.immutables.value.Value;
import se.fk.rimfrost.regel.rtf.maskinell.RattTillForsakring;

import java.util.UUID;

@Value.Immutable
public interface RtfMaskinellResponseRequest
{

   UUID id();

   UUID kundbehovsflodeId();

   String kogitorootprocid();

   UUID kogitorootprociid();

   UUID kogitoparentprociid();

   String kogitoprocid();

   UUID kogitoprocinstanceid();

   String kogitoprocist();

   String kogitoproctype();

   String kogitoprocversion();

   String kogitoprocrefid();

   RattTillForsakring rattTillForsakring();

}
