package se.fk.github.maskinellregelratttillforsakring.presentation.kafka;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import se.fk.rimfrost.regel.rtf.maskinell.RtfMaskinellRequestMessagePayload;
import se.fk.rimfrost.regel.rtf.maskinell.RtfMaskinellResponseMessagePayload;

public class RtfMaskinellResponseDeserializer extends ObjectMapperDeserializer<RtfMaskinellResponseMessagePayload>
{
   public RtfMaskinellResponseDeserializer()
   {
      super(RtfMaskinellResponseMessagePayload.class);
   }
}
