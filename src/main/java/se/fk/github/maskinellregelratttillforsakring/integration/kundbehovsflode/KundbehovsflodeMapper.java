package se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode;

import jakarta.enterprise.context.ApplicationScoped;
import se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.dto.ImmutableKundbehovsflodeResponse;
import se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.dto.KundbehovsflodeResponse;
import se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.dto.UpdateKundbehovsflodeRequest;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.*;

@ApplicationScoped
public class KundbehovsflodeMapper
{

   public KundbehovsflodeResponse toKundbehovsflodeResponse(GetKundbehovsflodeResponse apiResponse)
   {

      return ImmutableKundbehovsflodeResponse.builder()
            .personnummer(apiResponse.getKundbehovsflode().getKundbehov().getKundbehovsroll().getFirst().getIndivid().getId())
            .kundbehovsflodeId(apiResponse.getKundbehovsflode().getId())
            .build();
   }

   public PutKundbehovsflodeRequest toApiRequest(UpdateKundbehovsflodeRequest request)
   {
      //TODO implement   
      var putRequest = new PutKundbehovsflodeRequest();
      var kundbehovflode = new Kundbehovsflode();
      kundbehovflode.setId(request.kundbehovsflodeId());
      putRequest.setKundbehovsflode(kundbehovflode);
      return putRequest;
   }

}
