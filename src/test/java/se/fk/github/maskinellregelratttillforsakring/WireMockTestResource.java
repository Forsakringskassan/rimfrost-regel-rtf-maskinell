package se.fk.github.maskinellregelratttillforsakring;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import se.fk.rimfrost.framework.regel.maskinell.WireMockRegelMaskinell;

import java.util.HashMap;
import java.util.Map;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class WireMockTestResource extends WireMockRegelMaskinell
{
   @Override
   protected Map<String, String> wiremockMapping(WireMockServer server)
   {
      return Map.of(
            "folkbokford.api.base-url", server.baseUrl(),
            "arbetsgivare.api.base-url", server.baseUrl(),
            "handlaggning.api.base-url", server.baseUrl(),
            "individ.api.base-url", server.baseUrl());
   }
}
