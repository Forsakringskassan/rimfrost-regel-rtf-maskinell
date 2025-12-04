package se.fk.github.maskinellregelratttillforsakring.logic.dto;

import org.immutables.value.Value;

import java.util.UUID;

@Value.Immutable
public interface GetRtfDataRequest
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
}
