package se.fk.github.maskinellregelratttillforsakring.integration.folkbokford;

import jakarta.enterprise.context.ApplicationScoped;
import se.fk.github.maskinellregelratttillforsakring.integration.folkbokford.dto.FolkbokfordResponse;
import se.fk.github.maskinellregelratttillforsakring.integration.folkbokford.dto.FolkbokfordResponse.Kon;
import se.fk.github.maskinellregelratttillforsakring.integration.folkbokford.dto.ImmutableFolkbokfordResponse;
import se.fk.rimfrost.api.folkbokforing.jaxrsspec.controllers.generatedsource.model.FolkbokforingPersnrGet200Response;

@ApplicationScoped
public class FolkbokfordMapper
{

   public FolkbokfordResponse toFolkbokfordResponse(FolkbokforingPersnrGet200Response apiResponse)
   {
      return ImmutableFolkbokfordResponse.builder()
            .id(apiResponse.getId())
            .kon(apiResponse.getKon() == se.fk.rimfrost.api.folkbokforing.jaxrsspec.controllers.generatedsource.model.Kon.K
                  ? Kon.KVINNA
                  : Kon.MAN)
            .fornamn(apiResponse.getFornamn())
            .efternamn(apiResponse.getEfternamn())
            .adress(apiResponse.getAdress())
            .build();
   }

}
