package se.fk.github.maskinellregelratttillforsakring.integration.kafka;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.fk.github.maskinellregelratttillforsakring.integration.kafka.dto.RtfMaskinellResponseRequest;
import se.fk.rimfrost.regel.rtf.maskinell.RtfMaskinellResponseMessagePayload;

import java.util.UUID;

@ApplicationScoped
public class RtfMaskinellKafkaProducer
{
   @Inject
   RtfMaskinellKafkaMapper mapper;

   @Inject
   @Channel("rtf-maskinell-responses")
   @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 1024)
   Emitter<RtfMaskinellResponseMessagePayload> rtfMaskinellResponseEmitter;

   private static final Logger LOGGER = LoggerFactory.getLogger(RtfMaskinellKafkaProducer.class);

   public void sendRtfMaskinellResponse(RtfMaskinellResponseRequest rtfResponseRequest)
   {
      var response = mapper.toRtfMaskinellResponse(rtfResponseRequest);
      rtfMaskinellResponseEmitter.send(response);
   }
}
