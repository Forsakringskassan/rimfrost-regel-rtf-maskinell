package se.fk.github.maskinellregelratttillforsakring.logic;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import se.fk.github.maskinellregelratttillforsakring.integration.arbetsgivare.dto.ArbetsgivareResponse;
import se.fk.github.maskinellregelratttillforsakring.integration.folkbokford.dto.FolkbokfordResponse;
import se.fk.github.maskinellregelratttillforsakring.integration.kafka.dto.ImmutableRtfMaskinellResponseRequest;
import se.fk.github.maskinellregelratttillforsakring.integration.kafka.dto.RtfMaskinellResponseRequest;
import se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.dto.ImmutableUpdateKundbehovsflodeLagrum;
import se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.dto.ImmutableUpdateKundbehovsflodeRegel;
import se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.dto.ImmutableUpdateKundbehovsflodeRequest;
import se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.dto.ImmutableUpdateKundbehovsflodeSpecifikation;
import se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.dto.ImmutableUpdateKundbehovsflodeUnderlag;
import se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.dto.ImmutableUpdateKundbehovsflodeUppgift;
import se.fk.github.maskinellregelratttillforsakring.integration.kundbehovsflode.dto.UpdateKundbehovsflodeRequest;
import se.fk.github.maskinellregelratttillforsakring.logic.config.RegelConfig;
import se.fk.github.maskinellregelratttillforsakring.logic.dto.GetRtfDataRequest;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.FSSAinformation;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Roll;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.UppgiftStatus;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Verksamhetslogik;
import se.fk.rimfrost.regel.rtf.maskinell.RattTillForsakring;

@ApplicationScoped
public class RtfMapper
{

   @Inject
   ObjectMapper mapper;

   public RtfMaskinellResponseRequest toRtfResponseRequest(GetRtfDataRequest request, RattTillForsakring rattTillForsakring)
   {
      System.out.printf("toRtfResponseRequest request.id(): %s%n ", request.id());
      return ImmutableRtfMaskinellResponseRequest.builder()
            .id(request.id())
            .kundbehovsflodeId(request.kundbehovsflodeId())
            .kogitoparentprociid(request.kogitoparentprociid())
            .kogitorootprociid(request.kogitorootprociid())
            .kogitoprocid(request.kogitoprocid())
            .kogitorootprocid(request.kogitorootprocid())
            .kogitoprocinstanceid(request.kogitoprocinstanceid())
            .kogitoprocist(request.kogitoprocist())
            .kogitoproctype(request.kogitoproctype())
            .kogitoprocversion(request.kogitoprocversion())
            .rattTillForsakring(rattTillForsakring)
            .build();
   }

   public UpdateKundbehovsflodeRequest toUpdateKundbehovsflodeRequest(UUID kundbehovsflodeId,
         FolkbokfordResponse folkbokfordResponse, ArbetsgivareResponse arbetsgivareResponse,
         RattTillForsakring rattTillForsakring, RegelConfig regelConfig) throws JsonProcessingException
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

      return ImmutableUpdateKundbehovsflodeRequest.builder()
            .kundbehovsflodeId(kundbehovsflodeId)
            .rattTillForsakring(rattTillForsakring)
            .uppgift(uppgift)
            .addUnderlag(folkbokfordUnderlag, arbetsgivareUnderlag)
            .build();
   }
}
