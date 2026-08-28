# Kravdokument — rimfrost-regel-rtf-maskinell

## 1. Bakgrund och syfte

Tjänsten genomför en maskinell (automatiserad) bedömning av **rätt till försäkring (RTF)** — en generell inträdesprövning som andra förmånsprocesser använder innan de går vidare i sin handläggning. Bedömningen avgör om den enskilde är folkbokförd i Sverige och/eller har en anställning, och sätter utifrån detta ett utfall (Ja/Nej/Utredning) på det yrkande som prövas.

Tjänsten är en av flera "regel-maskinell"-tjänster som körs på en gemensam plattform (Rimfrost). Den tar emot regelförfrågningar via ett meddelandeflöde, hämtar uppgifter från externa register (folkbokföring, arbetsgivare), utvärderar en beslutstabell och skickar tillbaka ett svar. Om yrkandet saknar de personuppgifter (personnummer) som krävs för att bedömningen ska kunna genomföras, pausas flödet och en handläggare kompletterar uppgifterna via ett REST-gränssnitt innan bedömningen återupptas.

Merparten av den tekniska infrastrukturen (meddelandehantering, kompletterings-API:ets kontrakt, lagring, återförsöksmekanik, domänmodell för handläggning) tillhandahålls av delade ramverksbibliotek och är gemensam för alla regel-maskinell-tjänster. Detta dokument beskriver kraven på **RTF-tjänstens egna** beslutslogik och komplettering, samt hur den använder den delade plattformen.

## 2. Intressenter och aktörer

| Aktör | Roll |
|---|---|
| Handläggare (ansvarig handläggare) | Kompletterar saknade personuppgifter på ett yrkande via komplettering-API:et när den maskinella bedömningen inte kan genomföras. Slutför komplettering när uppgifterna är inlagda. |
| Anropande process (Kogito-processmotor / annan förmånsprocess) | Initierar en regelförfrågan via meddelandeflödet och tar emot regelsvaret (utfall eller fel) för att fortsätta sin egen handläggning. |
| Handläggningstjänsten | Källa till och mottagare av uppdaterad information om handläggningen (yrkande, individer, underlag, uppgift). |
| Folkbokföringstjänsten | Extern uppgiftskälla för folkbokföringsstatus per individ. |
| Arbetsgivartjänsten | Extern uppgiftskälla för anställningsstatus per individ. |
| Operativ uppgiftslista (OUL) | Mottagare av den handläggaruppgift som skapas när komplettering krävs, och som avslutas när kompletteringen är klar. |
| Driftsplattformen (Kubernetes) | Övervakar tjänstens hälsotillstånd och styr konfiguration via miljövariabler. |

## 3. Funktionella krav

### FR-01 — Maskinell bedömning av rätt till försäkring

- **FR-01.1** Tjänsten ska för varje individ knuten till yrkandet kunna hämta individens folkbokföringsstatus från folkbokföringstjänsten.
- **FR-01.2** Tjänsten ska för varje individ knuten till yrkandet kunna hämta individens anställningsstatus från arbetsgivartjänsten.
- **FR-01.3** Tjänsten ska avgöra utfallet (Ja, Nej eller Utredning) genom att utvärdera folkbokförings- och anställningsstatus mot en beslutstabell.
- **FR-01.4** Ett utfall "Ja" ska ges om personen är folkbokförd i Sverige, oavsett anställningsstatus.
- **FR-01.5** Ett utfall "Utredning" ska ges om personen inte är folkbokförd men har en anställning, för manuell utredning.
- **FR-01.6** Ett utfall "Nej" ska ges om personen varken är folkbokförd eller har en anställning.
- **FR-01.7** Resultatet från respektive extern uppslagning ska sparas som underlag på handläggningen, oavsett om uppslagningen gav ett positivt eller negativt resultat, för att ge spårbarhet till beslutet.
- **FR-01.8** Den uppgift som den maskinella bedömningen utför ska markeras som utförd när bedömningen är klar.

### FR-02 — Felhantering vid extern integrationsstörning

- **FR-02.1** Om en extern uppgiftskälla svarar att uppgiften saknas ska detta tolkas som ett giltigt negativt resultat (t.ex. "ej folkbokförd") och inte som ett tekniskt fel.
- **FR-02.2** Vid tillfälliga tekniska fel mot en extern uppgiftskälla ska tjänsten göra ett antal återförsök innan bedömningen avbryts.
- **FR-02.3** Om återförsöken tar slut utan att ett svar kunnat hämtas ska bedömningen avbrytas och ett fel med en särskild felkod rapporteras istället för ett utfall.
- **FR-02.4** Om underlaget för ett externt svar inte kan skapas (t.ex. vid serialiseringsfel) ska detta rapporteras som ett fel med en särskild felkod istället för ett utfall.

### FR-03 — Komplettering vid saknade individer

- **FR-03.1** Innan en maskinell bedömning genomförs ska tjänsten kunna avgöra om yrkandet saknar de individer (personer) som krävs för att bedömningen ska kunna göras.
- **FR-03.2** Om yrkandet saknar individer ska detta rapporteras som ett kompletteringsbehov med typ och beskrivning, så att en handläggaruppgift kan skapas.
- **FR-03.3** En handläggare ska via ett API kunna läsa av om ett yrkande för närvarande saknar individer.
- **FR-03.4** En handläggare ska via ett API kunna registrera en eller flera personnummer som nya individer på yrkandet.
- **FR-03.5** Redan registrerade individer på yrkandet ska bevaras när nya individer läggs till genom komplettering.
- **FR-03.6** En handläggare ska via ett API kunna slutföra kompletteringen, vilket ska kontrollera att yrkandet inte längre saknar individer innan den maskinella bedömningen återupptas.

### FR-04 — Meddelandebaserad integration

- **FR-04.1** Tjänsten ska ta emot regelförfrågningar asynkront via ett meddelandeflöde.
- **FR-04.2** Tjänsten ska skicka regelsvaret (utfall eller fel) till den svarskanal som angavs i den mottagna förfrågan.
- **FR-04.3** Processinstansidentiteten från den mottagna förfrågan ska följa med oförändrad i regelsvaret, så att den anropande processen kan korrelera svaret.

### FR-05 — Hälsokontroll

- **FR-05.1** Tjänsten ska exponera ett gränssnitt som driftsplattformen kan använda för att avgöra om tjänsten är tillgänglig och redo att ta emot trafik.

## 4. Uppgiftsstatus / Statusmodell

Nedan beskrivs de tillstånd ett yrkande kan befinna sig i under den maskinella prövningen av rätt till försäkring, samt vad som utlöser respektive tillstånd.

| Status | Beskrivning | Villkor |
|---|---|---|
| Väntar på komplettering | Bedömningen kan inte genomföras eftersom yrkandet saknar registrerade individer. En handläggaruppgift skapas och väntar på svar. | `individYrkandeRoller` är tom vid mottagen regelförfrågan. |
| Ja | Rätt till försäkring bedöms föreligga maskinellt. | Personen är folkbokförd i Sverige (oavsett anställningsstatus). |
| Utredning | Ärendet kräver manuell utredning innan beslut kan fattas. | Personen är inte folkbokförd men har en anställning. |
| Nej | Rätt till försäkring bedöms inte föreligga maskinellt. | Personen är varken folkbokförd eller har en anställning. |
| Fel | Den maskinella bedömningen kunde inte genomföras. | Återförsök mot extern uppgiftskälla är uttömda, eller underlag för ett externt svar kunde inte skapas. |

## 5. Icke-funktionella krav

### NFR-01 — Spårbarhet

- **NFR-01.1** Varje extern uppgiftshämtning som ligger till grund för ett utfall ska sparas som underlag kopplat till handläggningen.
- **NFR-01.2** Underlaget ska innehålla den ursprungliga informationen från den externa uppgiftskällan, inte bara det aggregerade resultatet, för att möjliggöra granskning i efterhand.

### NFR-02 — Tillförlitlighet

- **NFR-02.1** Tillfälliga fel mot externa uppgiftskällor ska hanteras med återförsök enligt en konfigurerbar intervallpolicy innan bedömningen avbryts som fel.
- **NFR-02.2** Kompletteringsflödet ska tåla att en pågående komplettering av tidsskäl kan hinna avbrytas innan handläggaren slutför den, och ska då ge en tydlig indikation om detta istället för att misslyckas tyst.

### NFR-03 — Datakvalitet vid komplettering

- **NFR-03.1** Personnummer som registreras via komplettering-API:et bör valideras avseende format innan de sparas på yrkandet, så att felaktigt inmatade värden inte förs vidare till externa uppgiftskällor. *(Ej uppfyllt i nuvarande implementation — noterat som brist.)*
- **NFR-03.2** Upprepad registrering av samma personnummer på ett yrkande ska inte skapa dubbletter av individer.

### NFR-04 — Konfigurerbarhet och drift

- **NFR-04.1** Anslutningar till externa uppgiftskällor och intern plattformsinfrastruktur ska kunna konfigureras per driftmiljö utan kodändring.
- **NFR-04.2** Tjänsten ska kunna köras utan att själv behöva hantera datalagring för kompletteringstillstånd, meddelandekorrelation eller liknande plattformsgemensam funktionalitet — detta tillhandahålls av den delade plattformen.

### NFR-05 — Prestanda

- **NFR-05.1** Bedömningen av ett enskilt yrkande ska kunna genomföras utan att kräva mänsklig inblandning i normalfallet (dvs. när individer redan är registrerade och externa uppgiftskällor svarar korrekt).
- **NFR-05.2** Återförsök mot externa uppgiftskällor bör inte orimligt fördröja hanteringen av efterföljande regelförfrågningar. *(Nuvarande implementation blockerar meddelandekonsumtion under pågående återförsök — noterat som brist.)*

## 6. API-gränssnitt (översikt)

| API | Målgrupp | Specifikationsartefakt |
|---|---|---|
| Komplettering (läsa kompletteringsbehov, registrera individer, slutföra komplettering) | Handläggare, via klientapplikation | Delat REST-kontrakt i det gemensamma regelramverket |
| Hälsokontroll | Driftsplattform (Kubernetes) | Standardiserat hälsokontrollgränssnitt, ingen tjänstespecifik spec |
| Regelförfrågan / regelsvar (meddelandeflöde) | Anropande processer (t.ex. processmotor) och övriga regeltjänster | Delat meddelandekontrakt i det gemensamma regelramverket |

## 7. Integration med angränsande tjänster

Tjänsten är beroende av handläggningstjänsten för att läsa och uppdatera handläggningar (yrkande, individer, underlag, uppgift), samt av folkbokförings- och arbetsgivartjänsterna för de sakuppgifter som ligger till grund för bedömningen. Kompletteringsflödet är kopplat till den operativa uppgiftslistan, där en handläggaruppgift skapas när komplettering krävs och avslutas när kompletteringen är genomförd. Regelförfrågningar och regelsvar utväxlas asynkront med anropande processer via ett meddelandeflöde, vilket gör att tjänsten kan bearbeta bedömningar oberoende av den anropande processens egen livscykel.
