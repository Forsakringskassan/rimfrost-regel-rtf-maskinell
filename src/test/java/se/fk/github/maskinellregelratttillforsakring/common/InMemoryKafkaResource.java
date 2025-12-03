package se.fk.github.maskinellregelratttillforsakring.common;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;

import java.util.HashMap;
import java.util.Map;

public class InMemoryKafkaResource implements QuarkusTestResourceLifecycleManager
{
   @Override
   public Map<String, String> start()
   {
      Map<String, String> config = new HashMap<>();
      config.put("mp.messaging.incoming.vah-rtf-requests.connector", "smallrye-in-memory");
      config.put("mp.messaging.outgoing.vah-rtf-responses.connector", "smallrye-in-memory");
      return config;
   }

   @Override
   public void stop()
   {
      InMemoryConnector.clear();
   }
}
