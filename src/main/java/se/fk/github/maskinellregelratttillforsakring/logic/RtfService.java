package se.fk.github.maskinellregelratttillforsakring.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.fk.rimfrost.framework.arbetsgivare.adapter.ArbetsgivareAdapter;
import se.fk.rimfrost.framework.arbetsgivare.adapter.dto.ImmutableArbetsgivareRequest;
import se.fk.rimfrost.framework.folkbokford.adapter.FolkbokfordAdapter;
import se.fk.rimfrost.framework.folkbokford.adapter.dto.ImmutableFolkbokfordRequest;
import se.fk.rimfrost.framework.regel.Utfall;
import se.fk.rimfrost.framework.regel.logic.entity.ImmutableUnderlag;
import se.fk.rimfrost.framework.regel.logic.entity.Underlag;
import se.fk.rimfrost.framework.regel.maskinell.logic.RegelMaskinellServiceInterface;
import se.fk.rimfrost.framework.regel.maskinell.logic.dto.ImmutableRegelMaskinellResult;
import se.fk.rimfrost.framework.regel.maskinell.logic.dto.RegelMaskinellRequest;
import se.fk.rimfrost.framework.regel.maskinell.logic.dto.RegelMaskinellResult;

@ApplicationScoped
public class RtfService implements RegelMaskinellServiceInterface
{

   private static final Logger LOGGER = LoggerFactory.getLogger(RtfService.class);

   @Inject
   ObjectMapper objectMapper;

   @Inject
   FolkbokfordAdapter folkbokfordAdapter;

   @Inject
   ArbetsgivareAdapter arbetsgivareAdapter;

   @Inject
   DmnService dmnService;

   @Override
   public RegelMaskinellResult processRegel(RegelMaskinellRequest regelRequest)
   {

      LOGGER.info("Started process regel for kundbehovsflodeId: {}", regelRequest.kundbehovsflodeId());

      var folkbokfordRequest = ImmutableFolkbokfordRequest.builder().personnummer(regelRequest.personnummer()).build();
      var folkbokfordResponse = folkbokfordAdapter.getFolkbokfordInfo(folkbokfordRequest);

      var arbetsgivareRequest = ImmutableArbetsgivareRequest.builder().personnummer(regelRequest.personnummer()).build();
      var arbetsgivareResponse = arbetsgivareAdapter.getArbetsgivareInfo(arbetsgivareRequest);

      boolean folkbokford = folkbokfordResponse != null;
      boolean harAnstallning = arbetsgivareResponse != null && arbetsgivareResponse.organisationsnummer() != null;

      var utfall = evaluteDmn(folkbokford, harAnstallning);

      var folkbokfordUnderlag = createUnderlag("Folkbokförd", "1.0", folkbokfordResponse);
      var arbetsgivareUnderlag = createUnderlag("Arbetsgivare", "1.0", arbetsgivareResponse);

      LOGGER.info("Finished process regel for kundbehovsflodeId: {} with utfall: {}", regelRequest.kundbehovsflodeId(), utfall);

      return ImmutableRegelMaskinellResult.builder()
            .addUnderlag(folkbokfordUnderlag, arbetsgivareUnderlag)
            .utfall(utfall)
            .build();
   }

   private Utfall evaluteDmn(boolean folkbokford, boolean harAnstallning)
   {
      String namespace = "https://se.fk/github/maskinellregelratttillforsakring";
      String modelName = "RtfDecisionModel";

      var dmnRuntime = dmnService.getRuntime();
      var model = dmnRuntime.getModel(namespace, modelName);

      var ctx = dmnRuntime.newContext();
      ctx.set("folkbokford", folkbokford);
      ctx.set("harAnstallning", harAnstallning);

      var result = dmnRuntime.evaluateAll(model, ctx);
      var raw = result.getDecisionResultByName("utfall").getResult();

      String dmnValue = raw != null ? raw.toString() : "UTREDNING";
      return mapRtf(dmnValue);
   }

   private Utfall mapRtf(String dmnValue)
   {
      return switch(dmnValue){case"NEJ"->Utfall.NEJ;case"JA"->Utfall.JA;default->Utfall.UTREDNING;};
   }

   private Underlag createUnderlag(String typ, String version, Object object)
   {
      try
      {
         return ImmutableUnderlag.builder()
               .typ(typ)
               .version(version)
               .data(objectMapper.writeValueAsString(object))
               .build();
      }
      catch (JsonProcessingException e)
      {
         throw new InternalError("Could not parse object to String", e);
      }
   }

}
