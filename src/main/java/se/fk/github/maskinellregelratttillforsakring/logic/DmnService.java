package se.fk.github.maskinellregelratttillforsakring.logic;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieRuntimeFactory;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.internal.io.ResourceFactory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DmnService
{

   private DMNRuntime dmnRuntime;

   public DmnService()
   {
   }

   @PostConstruct
   void init()
   {
      KieServices ks = KieServices.Factory.get();

      // Explicitly load DMN file from the classpath
      KieFileSystem kfs = ks.newKieFileSystem();
      kfs.write(ResourceFactory.newClassPathResource("dmn/RtfDecisionModel.dmn"));

      // Build it
      KieBuilder kb = ks.newKieBuilder(kfs).buildAll();
      Results results = kb.getResults();
      if (results.hasMessages(Message.Level.ERROR))
      {
         throw new IllegalStateException("Error building DMN model: " + results.getMessages());
      }

      // Create container and KieBase from the built DMN
      KieContainer kContainer = ks.newKieContainer(ks.getRepository().getDefaultReleaseId());
      KieBase kbase = kContainer.getKieBase();

      // Get DMNRuntime from that KieBase
      this.dmnRuntime = KieRuntimeFactory.of(kbase).get(DMNRuntime.class);
   }

   public DMNRuntime getRuntime()
   {
      return dmnRuntime;
   }
}
