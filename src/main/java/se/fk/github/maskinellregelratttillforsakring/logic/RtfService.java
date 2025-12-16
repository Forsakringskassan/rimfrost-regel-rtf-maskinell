package se.fk.github.maskinellregelratttillforsakring.logic;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import se.fk.github.maskinellregelratttillforsakring.integration.arbetsgivare.ArbetsgivareAdapter;
import se.fk.github.maskinellregelratttillforsakring.integration.arbetsgivare.dto.ArbetsgivareResponse;
import se.fk.github.maskinellregelratttillforsakring.integration.arbetsgivare.dto.ImmutableArbetsgivareRequest;
import se.fk.github.maskinellregelratttillforsakring.integration.folkbokford.FolkbokfordAdapter;
import se.fk.github.maskinellregelratttillforsakring.integration.folkbokford.dto.ImmutableFolkbokfordRequest;
import se.fk.github.maskinellregelratttillforsakring.integration.kafka.RtfMaskinellKafkaProducer;
import se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.KundbehovsflodeAdapter;
import se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.dto.ImmutableKundbehovsflodeRequest;
import se.fk.github.maskinellregelratttillforsakring.logic.dto.GetRtfDataRequest;
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
      // Hämta kundbehovsflöde
      var kundbehovsflodeRequest = ImmutableKundbehovsflodeRequest.builder().kundbehovsflodeId(request.kundbehovsflodeId())
            .build();
      var kundbehovflodesResponse = kundbehovsflodeAdapter.getKundbehovsflodeInfo(kundbehovsflodeRequest);
      // Evaluera logik
      var folkbokfordRequest = ImmutableFolkbokfordRequest.builder().personnummer(kundbehovflodesResponse.personnummer()).build();
      var folkbokfordResponse = folkbokfordAdapter.getFolkbokfordInfo(folkbokfordRequest);
      folkbokfordAdapter.getFolkbokfordInfo(folkbokfordRequest);
      var arbetsgivareRequest = ImmutableArbetsgivareRequest.builder().personnummer(kundbehovflodesResponse.personnummer())
            .build();
      var arbetsgivareResponse = arbetsgivareAdapter.getArbetsgivareInfo(arbetsgivareRequest);
      arbetsgivareAdapter.getArbetsgivareInfo(arbetsgivareRequest);
      RattTillForsakring rattTillForsakring = RattTillForsakring.JA;

      if (folkbokfordResponse == null)
      {
         System.out.printf("folkbokfordResponse is null. harAnstallning: %s%n", harAnstallning(arbetsgivareResponse));
         if (harAnstallning(arbetsgivareResponse))
         {
            rattTillForsakring = RattTillForsakring.UTREDNING;
         }
         else
         {
            rattTillForsakring = RattTillForsakring.NEJ;
         }
      }
      // Skicka resultat av regel
      kafkaProducer.sendRtfMaskinellResponse(mapper.toRtfResponseRequest(request, rattTillForsakring));
   }

   private boolean harAnstallning(ArbetsgivareResponse arbetsgivareResponse)
   {
      return arbetsgivareResponse.organisationsNr() != null;
   }

}
