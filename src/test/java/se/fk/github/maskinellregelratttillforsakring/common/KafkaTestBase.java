package se.fk.github.maskinellregelratttillforsakring.common;

import io.quarkus.test.common.QuarkusTestResource;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySink;
import io.smallrye.reactive.messaging.memory.InMemorySource;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

@QuarkusTestResource(InMemoryKafkaResource.class)
public class KafkaTestBase
{
   /*   @Inject
   @Any
   public InMemoryConnector connector;
   
   @Inject
   public VahRtfProcessor processor;
   
   public InMemorySource<VahRtfRequestMessagePayload> inMemorySource;
   public InMemorySink<VahRtfResponseMessagePayload> inMemorySink;
   
   @BeforeEach
   void setUp()
   {
      inMemorySource = connector.source("vah-rtf-requests");
      inMemorySink = connector.sink("vah-rtf-responses");
      inMemorySink.clear();
   }
   
   @AfterEach
   void tearDown()
   {
      InMemoryConnector.clear();
   }*/
}
