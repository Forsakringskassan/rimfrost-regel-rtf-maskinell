package se.fk.github.maskinellregelratttillforsakring.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.OffsetDateTime;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.fk.rimfrost.adapter.arbetsgivare.ArbetsgivareAdapter;
import se.fk.rimfrost.adapter.arbetsgivare.dto.ImmutableArbetsgivareRequest;
import se.fk.rimfrost.adapter.folkbokford.FolkbokfordAdapter;
import se.fk.rimfrost.adapter.folkbokford.dto.ImmutableFolkbokfordRequest;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableHandlaggningUpdate;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableUppgift;
import se.fk.rimfrost.framework.handlaggning.model.Underlag;
import se.fk.rimfrost.framework.regel.Utfall;
import se.fk.rimfrost.framework.regel.logic.RegelUtils;
import se.fk.rimfrost.framework.regel.maskinell.logic.RegelMaskinellServiceInterface;
import se.fk.rimfrost.framework.regel.maskinell.logic.dto.ImmutableRegelMaskinellResult;
import se.fk.rimfrost.framework.regel.maskinell.logic.dto.RegelMaskinellRequest;
import se.fk.rimfrost.framework.regel.maskinell.logic.dto.RegelMaskinellResult;
import se.fk.rimfrost.framework.uppgiftstatusprovider.UppgiftStatusProvider;

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
   UppgiftStatusProvider uppgiftStatusProvider;

   @Inject
   DmnService dmnService;

   @Override
   public RegelMaskinellResult processRegel(RegelMaskinellRequest regelRequest)
   {

      LOGGER.info("Started process regel for yrkandeId: {}", regelRequest.handlaggning().yrkande().id());

      boolean folkbokford = false;
      boolean harAnstallning = false;
      var underlag = new ArrayList<Underlag>();

      for (var individYrkandeRoll : regelRequest.handlaggning().yrkande().individYrkandeRoller())
      {
         var individ = individYrkandeRoll.individ();

         var folkbokfordRequest = ImmutableFolkbokfordRequest.builder().personnummer(individ.varde()).build();
         var folkbokfordResponse = folkbokfordAdapter.getFolkbokfordInfo(folkbokfordRequest);

         var arbetsgivareRequest = ImmutableArbetsgivareRequest.builder().personnummer(individ.varde()).build();
         var arbetsgivareResponse = arbetsgivareAdapter.getArbetsgivareInfo(arbetsgivareRequest);

         var folkbokfordUnderlag = RegelUtils.createUnderlag("Folkbokförd", 1, folkbokfordResponse, objectMapper);
         var arbetsgivareUnderlag = RegelUtils.createUnderlag("Arbetsgivare", 1, arbetsgivareResponse, objectMapper);

         underlag.add(folkbokfordUnderlag);
         underlag.add(arbetsgivareUnderlag);

         folkbokford = folkbokfordResponse != null;
         harAnstallning = arbetsgivareResponse != null && arbetsgivareResponse.organisationsnummer() != null;

         if (!folkbokford || !harAnstallning)
         {
            break;
         }
      }

      var utfall = evaluteDmn(folkbokford, harAnstallning);

      var uppgift = ImmutableUppgift.builder().from(regelRequest.uppgift())
            .utfordTs(OffsetDateTime.now())
            .uppgiftStatus(uppgiftStatusProvider.getAvslutadId())
            .build();

      var handlaggningUpdate = ImmutableHandlaggningUpdate.builder()
            .id(regelRequest.handlaggning().id())
            .version(regelRequest.handlaggning().version())
            .yrkande(regelRequest.handlaggning().yrkande())
            .processInstansId(regelRequest.processInstansId())
            .skapadTS(regelRequest.handlaggning().skapadTS())
            .avslutadTS(regelRequest.handlaggning().avslutadTS())
            .handlaggningspecifikationId(regelRequest.handlaggning().handlaggningspecifikationId())
            .underlag(underlag)
            .uppgift(uppgift)
            .build();

      LOGGER.info("Finished process regel for yrkande: {} with utfall: {}", regelRequest.handlaggning().yrkande().id(), utfall);

      return ImmutableRegelMaskinellResult.builder()
            .utfall(utfall)
            .handlaggningUpdate(handlaggningUpdate)
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
}
