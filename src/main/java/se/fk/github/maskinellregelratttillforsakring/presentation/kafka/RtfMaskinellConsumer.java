package se.fk.github.maskinellregelratttillforsakring.presentation.kafka;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.fasterxml.jackson.core.JsonProcessingException;

import se.fk.github.logging.callerinfo.model.MDCKeys;
import se.fk.github.maskinellregelratttillforsakring.logic.RtfService;
import se.fk.rimfrost.regel.rtf.maskinell.RtfMaskinellRequestMessagePayload;

@ApplicationScoped
public class RtfMaskinellConsumer
{

   private static final Logger LOGGER = LoggerFactory.getLogger(RtfMaskinellConsumer.class);

   @Inject
   RtfService rtfService;

   @Inject
   RtfMaskinellKafkaMapper mapper;

   @Incoming("rtf-maskinell-requests")
   public void onRtfMaskinellRequest(RtfMaskinellRequestMessagePayload rtfRequest) throws JsonProcessingException
   {
      MDC.put(MDCKeys.PROCESSID.name(), rtfRequest.getData().getKundbehovsflodeId());
      System.out.printf("onRtfMaskinellRequest: %s%n", rtfRequest.getData().getKundbehovsflodeId());
      LOGGER.info("RtfMaskinellRequestMessagePayload received with ID: " + rtfRequest.getData().getKundbehovsflodeId());
      var request = mapper.toGetRtfDataRequest(rtfRequest);
      rtfService.getData(request);
   }
}
