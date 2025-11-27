package se.fk.github.maskinellregelratttillforsakring.presentation.kafka;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import se.fk.rimfrost.regel.rtf.maskinell.RtfMaskinellRequestMessagePayload;

public class RtfMaskinellRequestDeserializer extends ObjectMapperDeserializer<RtfMaskinellRequestMessagePayload>
{
   public RtfMaskinellRequestDeserializer()
   {
      super(RtfMaskinellRequestMessagePayload.class);
   }
}
