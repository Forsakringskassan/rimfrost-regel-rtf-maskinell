package se.fk.github.maskinellregelratttillforsakring.logic;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import se.fk.github.maskinellregelratttillforsakring.integration.arbetsgivare.ArbetsgivareAdapter;
import se.fk.github.maskinellregelratttillforsakring.integration.arbetsgivare.dto.ImmutableArbetsgivareRequest;
import se.fk.github.maskinellregelratttillforsakring.integration.folkbokford.FolkbokfordAdapter;
import se.fk.github.maskinellregelratttillforsakring.integration.folkbokford.dto.ImmutableFolkbokfordRequest;
import se.fk.github.maskinellregelratttillforsakring.integration.kafka.RtfMaskinellKafkaProducer;
import se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.KundbehovsflodeAdapter;
import se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.dto.ImmutableKundbehovsflodeRequest;
import se.fk.github.maskinellregelratttillforsakring.logic.dto.GetRtfDataRequest;
import se.fk.github.maskinellregelratttillforsakring.logic.dto.GetRtfDataResponse;
import se.fk.rimfrost.regel.rtf.maskinell.RattTillForsakring;

@ApplicationScoped
public class RtfService
{

   @Inject
   RtfMaskinellKafkaProducer kafkaProducer;

   @Inject
   RtfMapper mapper;

   @Inject
   FolkbokfordAdapter folkbokfordAdapter;

   @Inject
   ArbetsgivareAdapter arbetsgivareAdapter;

   @Inject
   KundbehovsflodeAdapter kundbehovsflodeAdapter;

   public void getData(GetRtfDataRequest request)
   {
      var kundbehovsflodeRequest = ImmutableKundbehovsflodeRequest.builder().kundbehovsflodeId(request.kundbehovsflodeId())
            .build();
      var kundbehovflodesResponse = kundbehovsflodeAdapter.getKundbehovsflodeInfo(kundbehovsflodeRequest);

      var folkbokfordRequest = ImmutableFolkbokfordRequest.builder().personnummer(kundbehovflodesResponse.personnummer()).build();
      var folkbokfordResponse = folkbokfordAdapter.getFolkbokfordInfo(folkbokfordRequest);
      var arbetsgivareRequest = ImmutableArbetsgivareRequest.builder().personnummer(kundbehovflodesResponse.personnummer())
            .build();
      var arbetsgivareResponse = arbetsgivareAdapter.getArbetsgivareInfo(arbetsgivareRequest);
      RattTillForsakring rattTillForsakring = RattTillForsakring.JA;

      if (folkbokfordResponse == null)
      {
         System.out.printf("folkbokfordResponse is null. anstallningar size: %s%n", arbetsgivareResponse.anstallningar().size());
         if (arbetsgivareResponse.anstallningar().isEmpty())
         {
            rattTillForsakring = RattTillForsakring.NEJ;
         }
         else
         {
            rattTillForsakring = RattTillForsakring.UTREDNING;
         }
      }
      kafkaProducer.sendRtfMaskinellResponse(mapper.toRtfResponseRequest(request, rattTillForsakring));
   }

}
