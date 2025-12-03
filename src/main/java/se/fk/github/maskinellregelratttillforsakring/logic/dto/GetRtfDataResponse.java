package se.fk.github.maskinellregelratttillforsakring.logic.dto;

import jakarta.annotation.Nullable;
import org.immutables.value.Value;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Value.Immutable
public interface GetRtfDataResponse
{

   UUID kundbehovsflodeId();

   String fornamn();

   String efternamn();

   String kon();

   String organistaionsnummer();

   String organisationsnamn();

   int arbetstidProcent();

   LocalDate anstallningsdag();

   @Nullable
   LocalDate sistaAnstallningsdag();

   int loneSumma();

   LocalDate lonFrom();

   @Nullable
   LocalDate lonTom();

   List<Ersattning> ersattning();

   @Value.Immutable
   public interface Ersattning
   {

      UUID ersattningsId();

      String ersattningsTyp();

      int omfattningsProcent();

      int belopp();

      int berakningsgrund();

      @Nullable
      String beslutsutfall();

      LocalDate from();

      LocalDate tom();
   }
}
