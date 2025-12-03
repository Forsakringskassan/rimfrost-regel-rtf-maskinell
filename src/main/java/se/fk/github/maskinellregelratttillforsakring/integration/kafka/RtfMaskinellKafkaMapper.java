package se.fk.github.maskinellregelratttillforsakring.integration.kafka;

import jakarta.enterprise.context.ApplicationScoped;
import se.fk.github.maskinellregelratttillforsakring.integration.kafka.dto.RtfMaskinellResponseRequest;
import se.fk.rimfrost.regel.rtf.maskinell.*;

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
}
