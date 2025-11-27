package se.fk.github.maskinellregelratttillforsakring.presentation.kafka;

import jakarta.enterprise.context.ApplicationScoped;
import se.fk.github.maskinellregelratttillforsakring.integration.kafka.dto.RtfMaskinellResponseRequest;
import se.fk.github.maskinellregelratttillforsakring.logic.dto.CreateRtfDataRequest;
import se.fk.github.maskinellregelratttillforsakring.logic.dto.GetRtfDataRequest;
import se.fk.github.maskinellregelratttillforsakring.logic.dto.ImmutableCreateRtfDataRequest;
import se.fk.github.maskinellregelratttillforsakring.logic.dto.ImmutableGetRtfDataRequest;
import se.fk.rimfrost.regel.rtf.maskinell.*;

import java.util.UUID;

@ApplicationScoped
public class RtfMaskinellKafkaMapper
{

   public RtfMaskinellResponseMessagePayload toRtfMaskinellResponse(RtfMaskinellResponseRequest request)
   {
      var data = new RtfMaskinellResponseMessageData();
      data.setKundbehovsflodeId(request.kundbehovsflodeId().toString());
      data.setRattTillForsakring(request.rattTillForsakring());

      var response = new RtfMaskinellResponseMessagePayload();
      response.setId(request.id().toString());
      response.setKogitorootprocid(request.kogitorootprocid());
      response.setKogitorootprociid(request.kogitorootprociid().toString());
      response.setKogitoparentprociid(request.kogitoparentprociid().toString());
      response.setKogitoprocid(request.kogitoprocid());
      response.setKogitoprocinstanceid(request.kogitoprocinstanceid().toString());
      response.setKogitoprocrefid(request.kogitoprocinstanceid().toString());
      response.setKogitoprocist(request.kogitoprocist());
      response.setKogitoprocversion(request.kogitoprocversion());
      response.setSpecversion(SpecVersion.NUMBER_1_DOT_0);
      response.setSource("/regel/rtf-maskinell");
      response.setType("rtf-maskinell-responses");
      response.setKogitoproctype(KogitoProcType.BPMN);
      response.setData(data);

      return response;
   }

   public CreateRtfDataRequest toCreateRtfDataRequest(RtfMaskinellRequestMessagePayload rtfRequest)
   {

      return ImmutableCreateRtfDataRequest.builder()
            .id(UUID.fromString(rtfRequest.getId()))
            .kogitorootprociid(UUID.fromString(rtfRequest.getKogitorootprociid()))
            .kogitorootprocid(rtfRequest.getKogitorootprocid())
            .kogitoparentprociid(UUID.fromString(rtfRequest.getKogitoparentprociid()))
            .kogitoprocid(rtfRequest.getKogitoprocid())
            .kogitoprocinstanceid(UUID.fromString(rtfRequest.getKogitoprocinstanceid()))
            .kogitoprocist(rtfRequest.getKogitoprocist())
            .kogitoproctype(rtfRequest.getType())
            .kogitoprocversion(rtfRequest.getKogitoprocversion())
            .kundbehovsflodeId(UUID.fromString(rtfRequest.getData().getKundbehovsflodeId()))
            .build();
   }

   public GetRtfDataRequest toGetRtfDataRequest(RtfMaskinellRequestMessagePayload rtfRequest)
   {
      return ImmutableGetRtfDataRequest.builder()
            .id(UUID.fromString(rtfRequest.getId()))
            .kogitorootprociid(UUID.fromString(rtfRequest.getKogitorootprociid()))
            .kogitorootprocid(rtfRequest.getKogitorootprocid())
            .kogitoparentprociid(UUID.fromString(rtfRequest.getKogitoparentprociid()))
            .kogitoprocid(rtfRequest.getKogitoprocid())
            .kogitoprocinstanceid(UUID.fromString(rtfRequest.getKogitoprocinstanceid()))
            .kogitoprocist(rtfRequest.getKogitoprocist())
            .kogitoprocversion(rtfRequest.getKogitoprocversion())
            .kogitoproctype(rtfRequest.getKogitoproctype().getValue())
            .kogitoprocrefid(rtfRequest.getKogitoprocrefid())
            .kundbehovsflodeId(UUID.fromString(rtfRequest.getData().getKundbehovsflodeId()))
            .build();
   }
}
