package se.fk.github.maskinellregelratttillforsakring;

import io.quarkus.test.junit.QuarkusTestProfile;

public class RtfMaskinellTestProfile implements QuarkusTestProfile
{
   @Override
   public String getConfigProfile()
   {
      return "test";
   }
}
