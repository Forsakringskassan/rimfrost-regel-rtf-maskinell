package se.fk.github.maskinellregelratttillforsakring.integration.arbetsgivare;

import jakarta.enterprise.context.ApplicationScoped;
import se.fk.github.maskinellregelratttillforsakring.integration.arbetsgivare.dto.ArbetsgivareResponse;
import se.fk.github.maskinellregelratttillforsakring.integration.arbetsgivare.dto.ImmutableArbetsgivareResponse;
import se.fk.rimfrost.api.arbetsgivare.jaxrsspec.controllers.generatedsource.model.Anstallning;
import se.fk.rimfrost.api.arbetsgivare.jaxrsspec.controllers.generatedsource.model.GetArbetsgivare200Response;

import java.time.LocalDate;

@ApplicationScoped
public class ArbetsgivareMapper
{

   public ArbetsgivareResponse toArbetsgivareResponse(GetArbetsgivare200Response apiResponse)
   {

      if (apiResponse.getAnstallningar().isEmpty())
      {
         return ImmutableArbetsgivareResponse.builder()
               .organisationsNr("")
               .arbetstid(0)
               .startdag("")
               .slutdag("")
               .build();
      }
      Anstallning anstallning = apiResponse.getAnstallningar().getLast();
      String slutdag = "";
      if (anstallning.getSlutdag() != null)
      {
         slutdag = anstallning.getSlutdag();
      }
      return ImmutableArbetsgivareResponse.builder()
            .organisationsNr(anstallning.getOrganisation().getNummer())
            .arbetstid(anstallning.getArbetstid())
            .startdag(anstallning.getStartdag().toString())
            .slutdag(slutdag)
            .build();
   }
}
