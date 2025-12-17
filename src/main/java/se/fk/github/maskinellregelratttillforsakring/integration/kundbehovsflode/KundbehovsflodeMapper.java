package se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.InternalServerErrorException;
import se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.dto.ImmutableKundbehovsflodeResponse;
import se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.dto.KundbehovsflodeResponse;
import se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.dto.UpdateKundbehovsflodeRequest;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.*;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Ersattning.BeslutsutfallEnum;
import se.fk.rimfrost.regel.rtf.maskinell.RattTillForsakring;

@ApplicationScoped
public class KundbehovsflodeMapper
{

   public KundbehovsflodeResponse toKundbehovsflodeResponse(GetKundbehovsflodeResponse apiResponse)
   {

      return ImmutableKundbehovsflodeResponse.builder()
            .personnummer(apiResponse.getKundbehovsflode().getKundbehov().getKundbehovsroll().getFirst().getIndivid().getId())
            .kundbehovsflodeId(apiResponse.getKundbehovsflode().getId())
            .build();
   }

   public PutKundbehovsflodeRequest toApiRequest(UpdateKundbehovsflodeRequest request, GetKundbehovsflodeResponse apiResponse)
   {
      var putRequest = new PutKundbehovsflodeRequest();
      
      //Vart ska hämta denna uppgiftdata från?
      var uppgift = new Uppgift();
      uppgift.setFsSAinformation(FSSAinformation.HANDLAGGNING_PAGAR);
      uppgift.setSkapadTs(OffsetDateTime.now());
      uppgift.setUtfordTs(OffsetDateTime.now());
      uppgift.setUppgiftStatus(UppgiftStatus.AVSLUTAD);
      uppgift.setVersion("1.0");
      uppgift.setId(UUID.randomUUID()); //Ska vi dnena vara nullable kanske?

      var uppgiftspecifikation = new Uppgiftspecifikation();
      uppgiftspecifikation.setId(UUID.randomUUID());
      uppgiftspecifikation.setApplikationsId("rtf-maskinell");
      uppgiftspecifikation.setApplikationsVersion("1.0");
      uppgiftspecifikation.setNamn("Rätt till försäkring - maskinell kontroll");
      uppgiftspecifikation.setRoll(Roll.AGARE);
      uppgiftspecifikation.setUppgiftbeskrivning("Kontrollera om personen är folkbokförd");
      uppgiftspecifikation.setVerksamhetslogik(Verksamhetslogik.A);
      uppgiftspecifikation.setVersion("1.0");
      uppgiftspecifikation.setRegel(new ArrayList<Regel>());
      uppgiftspecifikation.setUppgiftsGui(""); //TODO denna bör vara nullable?
      uppgift.setUppgiftspecifikation(uppgiftspecifikation);

       var kundbehovflode = apiResponse.getKundbehovsflode();
      var ersattningar = apiResponse.getKundbehovsflode().getKundbehov().getErsattning();

      for (var ersattning : ersattningar)
      {
         ersattning.setBeslutsutfall(toBeslutsutfallEnum(request.rattTillForsakring()));
      }

      for (var underlag : request.underlag())
      {
         var underlagitem = new Underlag();
         underlagitem.typ(underlag.typ());
         underlagitem.version(underlag.version());
         underlagitem.data(underlag.data());

         uppgift.addUnderlagItem(underlagitem);
      }

      var kundbehov = kundbehovflode.getKundbehov();
      kundbehov.setErsattning(ersattningar);
      kundbehovflode.setKundbehov(kundbehov);
      uppgift.setKundbehovsflode(kundbehovflode);
      putRequest.setUppgift(uppgift);
      return putRequest;
   }

   private BeslutsutfallEnum toBeslutsutfallEnum(RattTillForsakring rtf) {
      switch (rtf) {
         case JA:
            return BeslutsutfallEnum.JA;
         case NEJ:
            return BeslutsutfallEnum.NEJ;
         case UTREDNING:
            return BeslutsutfallEnum.FU;
         default:
            throw new InternalServerErrorException("Could not map RattTillForsakring: " + rtf);
      }
   } 
}
