package se.fk.github.maskinellregelratttillforsakring.logic;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.fk.github.maskinellregelratttillforsakring.integration.arbetsgivare.ArbetsgivareAdapter;
import se.fk.github.maskinellregelratttillforsakring.integration.arbetsgivare.dto.ImmutableArbetsgivareRequest;
import se.fk.github.maskinellregelratttillforsakring.integration.folkbokford.FolkbokfordAdapter;
import se.fk.github.maskinellregelratttillforsakring.integration.folkbokford.dto.ImmutableFolkbokfordRequest;
import se.fk.rimfrost.framework.regel.Utfall;
import se.fk.rimfrost.framework.regel.integration.config.RegelConfigProvider;
import se.fk.rimfrost.framework.regel.integration.kafka.RegelKafkaProducer;
import se.fk.rimfrost.framework.regel.integration.kundbehovsflode.KundbehovsflodeAdapter;
import se.fk.rimfrost.framework.regel.integration.kundbehovsflode.dto.ImmutableKundbehovsflodeRequest;
import se.fk.rimfrost.framework.regel.logic.RegelMapper;
import se.fk.rimfrost.framework.regel.logic.config.RegelConfig;
import se.fk.rimfrost.framework.regel.logic.dto.RegelDataRequest;
import se.fk.rimfrost.framework.regel.logic.entity.ImmutableCloudEventData;
import se.fk.rimfrost.framework.regel.presentation.kafka.RegelRequestHandlerInterface;

@ApplicationScoped
public class RtfService implements RegelRequestHandlerInterface
{

   private static final Logger LOGGER = LoggerFactory.getLogger(RtfService.class);

   @Inject
   RegelKafkaProducer regelKafkaProducer;

   @Inject
   RtfMapper mapper;

   @Inject
   RegelMapper regelMapper;

   @Inject
   FolkbokfordAdapter folkbokfordAdapter;

   @Inject
   ArbetsgivareAdapter arbetsgivareAdapter;

   @Inject
   KundbehovsflodeAdapter kundbehovsflodeAdapter;

   @Inject
   DmnService dmnService;

   @Inject
   RegelConfigProvider regelConfigProvider;

   private RegelConfig regelConfig;

   @PostConstruct
   void init()
   {
      this.regelConfig = regelConfigProvider.getConfig();
   }

   @ConfigProperty(name = "kafka.source")
   String kafkaSource;

   @ConfigProperty(name = "mp.messaging.outgoing.regel-responses.topic")
   String responseTopic;

   @Override
   public void handleRegelRequest(RegelDataRequest request)
   {
      try
      {
         processRegelRequest(request);
      }
      catch (JsonProcessingException e)
      {
         LOGGER.error("Failed to process request with ID: " + request.kundbehovsflodeId());
      }
   }

   private void processRegelRequest(RegelDataRequest request) throws JsonProcessingException
   {
      // Hämta kundbehovsflöde
      var kundbehovsflodeRequest = ImmutableKundbehovsflodeRequest.builder().kundbehovsflodeId(request.kundbehovsflodeId())
            .build();

      var kundbehovflodesResponse = kundbehovsflodeAdapter.getKundbehovsflodeInfo(kundbehovsflodeRequest);

      var cloudevent = ImmutableCloudEventData.builder()
            .id(request.id())
            .kogitoparentprociid(request.kogitoparentprociid())
            .kogitoprocid(request.kogitoprocid())
            .kogitoprocinstanceid(request.kogitoprocinstanceid())
            .kogitoprocist(request.kogitoprocist())
            .kogitoprocversion(request.kogitoprocversion())
            .kogitorootprocid(request.kogitorootprocid())
            .kogitorootprociid(request.kogitorootprociid())
            .source(kafkaSource)
            .type(responseTopic)
            .build();

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

      var raw = result.getDecisionResultByName("utfall").getResult();

      String dmnValue = raw != null ? raw.toString() : "UTREDNING";

      Utfall utfall = mapRtf(dmnValue);

      kundbehovsflodeAdapter.updateKundbehovsflodeInfo(mapper.toUpdateKundbehovsflodeRequest(request.kundbehovsflodeId(),
            folkbokfordResponse, arbetsgivareResponse, utfall, regelConfig, kundbehovflodesResponse));

      var rtfResponse = regelMapper.toRegelResponse(request.kundbehovsflodeId(), cloudevent, utfall);
      regelKafkaProducer.sendRegelResponse(rtfResponse);
   }

   private Utfall mapRtf(String dmnValue)
   {
      return switch(dmnValue){case"NEJ"->Utfall.NEJ;case"JA"->Utfall.JA;default->Utfall.UTREDNING;};
   }
}
