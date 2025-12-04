package se.fk.github.maskinellregelratttillforsakring.logic;

import jakarta.enterprise.context.ApplicationScoped;
import se.fk.github.maskinellregelratttillforsakring.integration.kafka.dto.ImmutableRtfMaskinellResponseRequest;
import se.fk.github.maskinellregelratttillforsakring.integration.kafka.dto.RtfMaskinellResponseRequest;
import se.fk.github.maskinellregelratttillforsakring.logic.dto.GetRtfDataRequest;
import se.fk.rimfrost.regel.rtf.maskinell.RattTillForsakring;

@ApplicationScoped
public class RtfMapper
{

   public RtfMaskinellResponseRequest toRtfResponseRequest(GetRtfDataRequest request, RattTillForsakring rattTillForsakring)
   {
      System.out.printf("toRtfResponseRequest request.id(): %s%n ", request.id());
      return ImmutableRtfMaskinellResponseRequest.builder()
            .id(request.id())
            .kundbehovsflodeId(request.kundbehovsflodeId())
            .kogitoparentprociid(request.kogitoparentprociid())
            .kogitorootprociid(request.kogitorootprociid())
            .kogitoprocid(request.kogitoprocid())
            .kogitorootprocid(request.kogitorootprocid())
            .kogitoprocinstanceid(request.kogitoprocinstanceid())
            .kogitoprocist(request.kogitoprocist())
            .kogitoproctype(request.kogitoproctype())
            .kogitoprocversion(request.kogitoprocversion())
            .rattTillForsakring(rattTillForsakring)
            .build();
   }
}
