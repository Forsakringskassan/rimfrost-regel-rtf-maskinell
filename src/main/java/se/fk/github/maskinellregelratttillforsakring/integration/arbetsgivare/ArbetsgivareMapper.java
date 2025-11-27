package se.fk.github.maskinellregelratttillforsakring.integration.arbetsgivare;

import jakarta.enterprise.context.ApplicationScoped;
import se.fk.github.maskinellregelratttillforsakring.integration.arbetsgivare.dto.ArbetsgivareResponse;
import se.fk.github.maskinellregelratttillforsakring.integration.arbetsgivare.dto.ImmutableArbetsgivareResponse;
import se.fk.rimfrost.api.arbetsgivare.jaxrsspec.controllers.generatedsource.model.GetArbetsgivare200Response;

import java.time.LocalDate;

@ApplicationScoped
public class ArbetsgivareMapper
{

   public ArbetsgivareResponse toArbetsgivareResponse(GetArbetsgivare200Response apiResponse)
   {
      //TOOD hämta all info från responset
      return ImmutableArbetsgivareResponse.builder()
            .organisationsnamn("CoolJobb AB")
            .organisationsnummer("1234-567890")
            .anstallningsdag(LocalDate.now().minusYears(2))
            .arbetstidProcent(100)
            .lonFrom(LocalDate.now().minusYears(1))
            .loneSumma(40000)
            .build();
   }
}
