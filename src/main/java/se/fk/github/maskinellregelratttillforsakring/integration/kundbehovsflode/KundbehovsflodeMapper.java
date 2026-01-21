package se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode;

import java.time.OffsetDateTime;
import java.util.ArrayList;

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
      var lagrum = new Lagrum();
      lagrum.setId(request.uppgift().specifikation().regel().lagrum().id());
      lagrum.setVersion(request.uppgift().specifikation().regel().lagrum().version());
      lagrum.setForfattning(request.uppgift().specifikation().regel().lagrum().forfattning());
      lagrum.setGiltigFrom(request.uppgift().specifikation().regel().lagrum().giltigFrom());
      lagrum.setGiltigTom(request.uppgift().specifikation().regel().lagrum().giltigTom());
      lagrum.setKapitel(request.uppgift().specifikation().regel().lagrum().kapitel());
      lagrum.setParagraf(request.uppgift().specifikation().regel().lagrum().paragraf());
      lagrum.setPunkt(request.uppgift().specifikation().regel().lagrum().punkt());
      lagrum.setStycke(request.uppgift().specifikation().regel().lagrum().stycke());

      var regel = new Regel();
      regel.setId(request.uppgift().specifikation().regel().id());
      regel.setVersion(request.uppgift().specifikation().regel().version());
      regel.setLagrum(lagrum);

      var uppgiftspecifikation = new Uppgiftspecifikation();
      uppgiftspecifikation.setId(request.uppgift().specifikation().id());
      uppgiftspecifikation.setApplikationsId(request.uppgift().specifikation().applikationsId());
      uppgiftspecifikation.setApplikationsVersion(request.uppgift().specifikation().applikationsversion());
      uppgiftspecifikation.setNamn(request.uppgift().specifikation().namn());
      uppgiftspecifikation.setRoll(request.uppgift().specifikation().roll());
      uppgiftspecifikation.setUppgiftbeskrivning(request.uppgift().specifikation().uppgiftsbeskrivning());
      uppgiftspecifikation.setUppgiftsGui(request.uppgift().specifikation().url());
      uppgiftspecifikation.setVerksamhetslogik(request.uppgift().specifikation().verksamhetslogik());
      uppgiftspecifikation.setVersion(request.uppgift().specifikation().version());
      uppgiftspecifikation.setRegel(regel);

      var underlagList = new ArrayList<Underlag>();
      for (var underlag : request.underlag())
      {
         var underlagitem = new Underlag();
         underlagitem.typ(underlag.typ());
         underlagitem.version(underlag.version());
         underlagitem.data(underlag.data());
         underlagList.add(underlagitem);
      }

      var uppgift = new Uppgift();
      uppgift.setId(request.uppgift().id());
      uppgift.setFsSAinformation(request.uppgift().fsSAinformation());
      uppgift.setSkapadTs(request.uppgift().skapadTs());
      uppgift.setUtfordTs(request.uppgift().utfordTs());
      uppgift.setUppgiftStatus(request.uppgift().uppgiftStatus());
      uppgift.setVersion(request.uppgift().version());
      uppgift.setUppgiftspecifikation(uppgiftspecifikation);
      uppgift.setUnderlag(underlagList);

      var ersattningar = apiResponse.getKundbehovsflode().getKundbehov().getErsattning();
      for (var ersattning : ersattningar)
      {
         ersattning.setBeslutsutfall(toBeslutsutfallEnum(request.rattTillForsakring()));
      }

      var kundbehovflode = apiResponse.getKundbehovsflode();
      var kundbehov = kundbehovflode.getKundbehov();
      kundbehov.setErsattning(ersattningar);
      kundbehovflode.setKundbehov(kundbehov);
      uppgift.setKundbehovsflode(kundbehovflode);

      var putRequest = new PutKundbehovsflodeRequest();
      putRequest.setUppgift(uppgift);
      return putRequest;
   }

   private BeslutsutfallEnum toBeslutsutfallEnum(RattTillForsakring rtf)
   {
      switch (rtf)
      {
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
