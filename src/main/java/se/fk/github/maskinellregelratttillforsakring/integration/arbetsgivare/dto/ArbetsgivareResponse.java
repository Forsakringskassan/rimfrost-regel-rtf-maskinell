package se.fk.github.maskinellregelratttillforsakring.integration.arbetsgivare.dto;

import jakarta.annotation.Nullable;
import org.immutables.value.Value;
import se.fk.rimfrost.api.arbetsgivare.jaxrsspec.controllers.generatedsource.model.Anstallning;

import java.time.LocalDate;
import java.util.List;

@Value.Immutable
public interface ArbetsgivareResponse
{

   String organisationsnummer();

   String organisationsnamn();

   int arbetstidProcent();

   int loneSumma();

   LocalDate anstallningsdag();

   @Nullable
   LocalDate sistaAnstallningsdag();

   LocalDate lonFrom();

   @Nullable
   LocalDate lonTom();
}
