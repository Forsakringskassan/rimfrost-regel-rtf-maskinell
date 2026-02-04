package se.fk.github.maskinellregelratttillforsakring.logic;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.InternalServerErrorException;
import se.fk.github.maskinellregelratttillforsakring.integration.arbetsgivare.dto.ArbetsgivareResponse;
import se.fk.github.maskinellregelratttillforsakring.integration.folkbokford.dto.FolkbokfordResponse;
import se.fk.rimfrost.framework.regel.integration.kundbehovsflode.dto.*;
import se.fk.rimfrost.framework.regel.logic.config.RegelConfig;
import se.fk.rimfrost.framework.regel.logic.dto.UppgiftStatus;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Ersattning;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.FSSAinformation;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Roll;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Verksamhetslogik;
import se.fk.rimfrost.framework.regel.Utfall;

@ApplicationScoped
public class RtfMapper
{

   @Inject
   ObjectMapper mapper;

   public UpdateKundbehovsflodeRequest toUpdateKundbehovsflodeRequest(UUID kundbehovsflodeId,
         FolkbokfordResponse folkbokfordResponse, ArbetsgivareResponse arbetsgivareResponse,
         Utfall utfall, RegelConfig regelConfig, KundbehovsflodeResponse kundbehovsflodeResponse) throws JsonProcessingException
   {
      var lagrum = ImmutableUpdateKundbehovsflodeLagrum.builder()
            .id(regelConfig.getLagrum().getId())
            .version(regelConfig.getLagrum().getVersion())
            .forfattning(regelConfig.getLagrum().getForfattning())
            .giltigFrom(regelConfig.getLagrum().getGiltigFom().toInstant().atOffset(ZoneOffset.UTC))
            .kapitel(regelConfig.getLagrum().getKapitel())
            .paragraf(regelConfig.getLagrum().getParagraf())
            .stycke(regelConfig.getLagrum().getStycke())
            .punkt(regelConfig.getLagrum().getPunkt())
            .build();

      var regel = ImmutableUpdateKundbehovsflodeRegel.builder()
            .id(regelConfig.getRegel().getId())
            .beskrivning(regelConfig.getRegel().getBeskrivning())
            .namn(regelConfig.getRegel().getNamn())
            .version(regelConfig.getRegel().getVersion())
            .lagrum(lagrum)
            .build();

      var specifikation = ImmutableUpdateKundbehovsflodeSpecifikation.builder()
            .id(regelConfig.getSpecifikation().getId())
            .version(regelConfig.getSpecifikation().getVersion())
            .namn(regelConfig.getSpecifikation().getNamn())
            .uppgiftsbeskrivning(regelConfig.getSpecifikation().getUppgiftbeskrivning())
            .verksamhetslogik(Verksamhetslogik.fromString(regelConfig.getSpecifikation().getVerksamhetslogik()))
            .roll(Roll.fromString(regelConfig.getSpecifikation().getRoll()))
            .applikationsId(regelConfig.getSpecifikation().getApplikationsId())
            .applikationsversion(regelConfig.getSpecifikation().getApplikationsversion())
            .url(regelConfig.getUppgift().getPath())
            .regel(regel)
            .build();

      var uppgift = ImmutableUpdateKundbehovsflodeUppgift.builder()
            .id(UUID.randomUUID())
            .version(regelConfig.getUppgift().getVersion())
            .skapadTs(OffsetDateTime.now())
            .utfordTs(OffsetDateTime.now())
            .planeradTs(OffsetDateTime.now())
            .uppgiftStatus(UppgiftStatus.AVSLUTAD)
            .aktivitet(regelConfig.getUppgift().getAktivitet())
            .fsSAinformation(FSSAinformation.HANDLAGGNING_PAGAR)
            .specifikation(specifikation)
            .build();

      var folkbokfordUnderlag = ImmutableUpdateKundbehovsflodeUnderlag.builder()
            .typ("FolkbokfördUnderlag")
            .version("1.0")
            .data(mapper.writeValueAsString(folkbokfordResponse))
            .build();

      var arbetsgivareUnderlag = ImmutableUpdateKundbehovsflodeUnderlag.builder()
            .typ("ArbetsgivareUnderlag")
            .version("1.0")
            .data(mapper.writeValueAsString(arbetsgivareResponse))
            .build();

      var ersattningar = new ArrayList<UpdateKundbehovsflodeErsattning>();
      for (KundbehovsflodeResponse.Ersattning responseErsattning : kundbehovsflodeResponse.ersattning())
      {
         var ersattning = ImmutableUpdateKundbehovsflodeErsattning.builder()
               .beslutsutfall(toBeslutsutfallEnum(utfall))
               .id(responseErsattning.ersattningsId())
               .build();
         ersattningar.add(ersattning);
      }

      return ImmutableUpdateKundbehovsflodeRequest.builder()
            .kundbehovsflodeId(kundbehovsflodeId)
            .uppgift(uppgift)
            .addUnderlag(folkbokfordUnderlag, arbetsgivareUnderlag)
            .addAllErsattningar(ersattningar)
            .build();
   }

   private Ersattning.BeslutsutfallEnum toBeslutsutfallEnum(Utfall utfall)
   {
      return switch (utfall)
      {
         case JA -> Ersattning.BeslutsutfallEnum.JA;
         case NEJ -> Ersattning.BeslutsutfallEnum.NEJ;
         case UTREDNING -> Ersattning.BeslutsutfallEnum.FU;
         default -> throw new InternalServerErrorException("Could not map Utfall: " + utfall);
      };
   }
}
