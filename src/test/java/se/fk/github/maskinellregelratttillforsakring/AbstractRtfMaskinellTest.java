package se.fk.github.maskinellregelratttillforsakring;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeAll;
import se.fk.rimfrost.framework.regel.maskinell.AbstractRegelMaskinellTest;

@QuarkusTest
@QuarkusTestResource.List(
{
      @QuarkusTestResource(WireMockTestResource.class)
})
public class AbstractRtfMaskinellTest extends AbstractRegelMaskinellTest
{
   @BeforeAll
   static void setup()
   {
      wiremockServer = WireMockTestResource.getWireMockServer();
   }

}
