package se.fk.github.maskinellregelratttillforsakring.tests.presentation;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import se.fk.github.maskinellregelratttillforsakring.common.KafkaTestBase;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@QuarkusTest
public class VahRtfProcessorTesQ extends KafkaTestBase
{

   /*   @Test
   void testReceiveVahRtfRequest_withOkPnr()
   {
   
      var rtfRequestPayload = createRtfPayload();
      inMemorySource.send(rtfRequestPayload);
   
      await()
            .atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> {
               assertThat(inMemorySink.received())
                     .as("One message should have been sent")
                     .hasSize(1);
   
               VahRtfResponseMessagePayload response = inMemorySink.received().get(0).getPayload();
               assertThat(response.getData().getProcessId()).isEqualTo(rtfRequestPayload.getData().getProcessId());
               assertThat(response.getData().getPersonNummer()).isEqualTo(rtfRequestPayload.getData().getPersonNummer());
               assertThat(response.getData().getRattTillForsakring()).isEqualTo(RattTillForsakring.JA);
               assertThat(response.getId()).isEqualTo(rtfRequestPayload.getId());
               assertThat(response.getSource()).isEqualTo(rtfRequestPayload.getSource());
               assertThat(response.getType()).isEqualTo("vah-rtf-responses");
               assertThat(response.getKogitorootprocid()).isEqualTo(rtfRequestPayload.getKogitorootprocid());
               assertThat(response.getKogitoparentprociid()).isEqualTo(rtfRequestPayload.getKogitoparentprociid());
               assertThat(response.getKogitoprocinstanceid()).isEqualTo(rtfRequestPayload.getKogitoprocinstanceid());
               assertThat(response.getKogitoproctype()).isEqualTo(rtfRequestPayload.getKogitoproctype());
               assertThat(response.getKogitoprocid()).isEqualTo(rtfRequestPayload.getKogitoprocid());
               assertThat(response.getKogitoprocrefid()).isEqualTo(rtfRequestPayload.getKogitoprocinstanceid());
               assertThat(response.getKogitoprocversion()).isEqualTo(rtfRequestPayload.getKogitoprocversion());
   
            });
   }
   
   @Test
   void testReceiveMultipleRequests()
   {
   
      var request1 = createRtfPayload();
      var request2 = createRtfPayload();
   
      inMemorySource.send(request1);
      inMemorySource.send(request2);
   
      await()
            .atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> {
               assertThat(inMemorySink.received())
                     .as("Two messages should have been sent")
                     .hasSize(2);
            });
   }
   
   private VahRtfRequestMessagePayload createRtfPayload()
   {
   
      UUID processId = UUID.randomUUID();
      String personnummer = "12345678-1234";
   
      var data = new VahRtfRequestMessageData();
      data.setProcessId(processId.toString());
      data.setPersonNummer(personnummer);
   
      var request = new VahRtfRequestMessagePayload();
      request.setData(data);
      request.setId("123");
      request.setSource("234");
      request.setType("vah-rtf-requests");
      request.setTime(OffsetDateTime.now());
      request.setKogitorootprocid("345");
      request.setKogitorootprociid("456");
      request.setKogitoparentprociid("567");
      request.setKogitoprocid("678");
      request.setKogitoprocinstanceid("789");
      request.setKogitoprocrefid("890");
      request.setKogitoprocist("901");
      request.setKogitoproctype(KogitoProcType.BPMN);
      request.setKogitoprocversion("1.1");
      return request;
   }*/

}
