package se.fk.github.maskinellregelratttillforsakring.logic.config;

public class RegelConfig
{

   private Uppgift uppgift;
   private Specifikation specifikation;
   private Regel regel;
   private Lagrum lagrum;

   public RegelConfig()
   {
   }

   public RegelConfig(Uppgift uppgift, Specifikation specifikation, Regel regel, Lagrum lagrum)
   {
      this.uppgift = uppgift;
      this.specifikation = specifikation;
      this.regel = regel;
      this.lagrum = lagrum;
   }

   public Uppgift getUppgift()
   {
      return uppgift;
   }

   public void setUppgift(Uppgift uppgift)
   {
      this.uppgift = uppgift;
   }

   public Specifikation getSpecifikation()
   {
      return specifikation;
   }

   public void setSpecifikation(Specifikation specifikation)
   {
      this.specifikation = specifikation;
   }

   public Regel getRegel()
   {
      return regel;
   }

   public void setRegel(Regel regel)
   {
      this.regel = regel;
   }

   public Lagrum getLagrum()
   {
      return lagrum;
   }

   public void setLagrum(Lagrum lagrum)
   {
      this.lagrum = lagrum;
   }
}
