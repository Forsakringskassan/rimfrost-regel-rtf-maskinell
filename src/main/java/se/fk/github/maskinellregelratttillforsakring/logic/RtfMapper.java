package se.fk.github.maskinellregelratttillforsakring.logic;

import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import se.fk.github.maskinellregelratttillforsakring.integration.arbetsgivare.dto.ArbetsgivareResponse;
import se.fk.github.maskinellregelratttillforsakring.integration.folkbokford.dto.FolkbokfordResponse;
import se.fk.github.maskinellregelratttillforsakring.integration.kafka.dto.ImmutableRtfMaskinellResponseRequest;
import se.fk.github.maskinellregelratttillforsakring.integration.kafka.dto.RtfMaskinellResponseRequest;
import se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.dto.ImmutableUpdateKundbehovsflodeRequest;
import se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.dto.ImmutableUpdateKundbehovsflodeUnderlag;
import se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.dto.UpdateKundbehovsflodeRequest;
import se.fk.github.maskinellregelratttillforsakring.logic.dto.GetRtfDataRequest;
import se.fk.rimfrost.regel.rtf.maskinell.RattTillForsakring;

@ApplicationScoped
public class RtfMapper
{

   @Inject
   ObjectMapper mapper;

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

   public UpdateKundbehovsflodeRequest toUpdateKundbehovsflodeRequest(UUID kundbehovsflodeId,
         FolkbokfordResponse folkbokfordResponse, ArbetsgivareResponse arbetsgivareResponse,
         RattTillForsakring rattTillForsakring) throws JsonProcessingException
   {

      var folkbokfordUnderlag = ImmutableUpdateKundbehovsflodeUnderlag.builder()
            .typ("FolkbokfördUnderlag")
            .version("1.0")
            .data(mapper.writeValueAsString(folkbokfordResponse))
            .build();

      var arbetsgivareUnderlag = ImmutableUpdateKundbehovsflodeUnderlag.builder()
            .typ("ArbetsgivareUnderlag")
            .version("1.0")
            .data(mapper.writeValueAsString(arbetsgivareResponse))
            .build();

      return ImmutableUpdateKundbehovsflodeRequest.builder()
            .kundbehovsflodeId(kundbehovsflodeId)
            .rattTillForsakring(rattTillForsakring)
            .addUnderlag(folkbokfordUnderlag, arbetsgivareUnderlag)
            .build();
   }
}
