package se.fk.github.maskinellregelratttillforsakring.logic;

import com.fasterxml.jackson.core.JsonProcessingException;

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

   @Inject
   DmnService dmnService;

   public void getData(GetRtfDataRequest request) throws JsonProcessingException
   {
      // Hämta kundbehovsflöde
      var kundbehovsflodeRequest = ImmutableKundbehovsflodeRequest.builder().kundbehovsflodeId(request.kundbehovsflodeId())
            .build();

      var kundbehovflodesResponse = kundbehovsflodeAdapter.getKundbehovsflodeInfo(kundbehovsflodeRequest);

      // Evaluera logik
      var folkbokfordRequest = ImmutableFolkbokfordRequest.builder().personnummer(kundbehovflodesResponse.personnummer()).build();

      var folkbokfordResponse = folkbokfordAdapter.getFolkbokfordInfo(folkbokfordRequest);

      var arbetsgivareRequest = ImmutableArbetsgivareRequest.builder().personnummer(kundbehovflodesResponse.personnummer())
            .build();

      var arbetsgivareResponse = arbetsgivareAdapter.getArbetsgivareInfo(arbetsgivareRequest);

      boolean folkbokfordFinns = folkbokfordResponse != null;

      boolean harAnstallning = arbetsgivareResponse != null && arbetsgivareResponse.organisationsNr() != null;

      String namespace = "https://se.fk/github/maskinellregelratttillforsakring";

      String modelName = "RtfDecisionModel";

      var dmnRuntime = dmnService.getRuntime();

      var model = dmnRuntime.getModel(namespace, modelName);

      var ctx = dmnRuntime.newContext();

      ctx.set("folkbokford", folkbokfordFinns);

      ctx.set("harAnstallning", harAnstallning);

      var result = dmnRuntime.evaluateAll(model, ctx);

      var raw = result.getDecisionResultByName("rattTillForsakring").getResult();

      String dmnValue = raw != null ? raw.toString() : "UTREDNING";

      RattTillForsakring rattTillForsakring = mapRtf(dmnValue);

      kundbehovsflodeAdapter.updateKundbehovsflodeInfo(mapper.toUpdateKundbehovsflodeRequest(request.kundbehovsflodeId(),
            folkbokfordResponse, arbetsgivareResponse, rattTillForsakring));
      kafkaProducer.sendRtfMaskinellResponse(mapper.toRtfResponseRequest(request, rattTillForsakring));
   }

   private RattTillForsakring mapRtf(String dmnValue)
   {
      switch (dmnValue)
      {
         case "NEJ":
            return RattTillForsakring.NEJ;

         case "UTREDNING":
            return RattTillForsakring.UTREDNING;

         case "JA":
            return RattTillForsakring.JA;

         default:
            return RattTillForsakring.UTREDNING;
      }
   }
}
