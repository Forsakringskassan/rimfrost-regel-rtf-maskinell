# rimfrost-regel-rtf-maskinell changelog

Changelog of rimfrost-regel-rtf-maskinell.

## 0.5.0 (2026-02-25)

### Features

-  Update to use the new regel framework (#63) ([9cb03](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/9cb03e54c7dc2a6) NilsElveros)  

## 0.4.0 (2026-02-22)

### Features

-  send more data to kundebehovsflode service and add yamlfile (#51) ([29e8c](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/29e8c6cd7d11859) NilsElveros)  

### Bug Fixes

-  **deps**  update dependency se.fk.rimfrost.framework.arbetsgivare:rimfrost-framework-arbetsgivare-adapter to v0.1.0 ([bc51e](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/bc51e25f5a02150) renovate[bot])  
-  **deps**  update dependency se.fk.rimfrost.api.arbetsgivare:rimfrost-arbetsgivare-api-jaxrs-spec to v1.1.5 ([c42e4](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/c42e42fc7a9895b) renovate[bot])  
-  Bump rimfrost-framework-regel version ([8d2ac](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/8d2ac4e45abb061) Lars Persson)  
-  Use kundbehovsflode adapter from rimfrost-framework-kundbehovsflode-adapter ([6ba0d](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/6ba0d71e32091ef) Lars Persson)  
-  Use updated field names from arbetsgivare adapter response ([5c186](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/5c186c47df58830) Lars Persson)  
-  Use arbetsgivare and folkbokford adapters from framework ([1051b](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/1051b88fcc4e00d) Lars Persson)  
-  Add config path to application properties ([3f195](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/3f195ae2db96094) Lars Persson)  
-  **deps**  update dependency se.fk.rimfrost.framework.regel:rimfrost-framework-regel to v0.1.3 ([e6738](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/e6738c0b1c69044) renovate[bot])  
-  **deps**  update dependency se.fk.rimfrost.framework.regel:rimfrost-framework-regel to v0.1.2 ([0829a](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/0829aa668067cc6) renovate[bot])  
-  Refactor test to use smallrye in-memory and test resources ([16e75](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/16e758a74070fbe) Lars Persson)  
-  Use kundbehovsflode api from framework ([640c8](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/640c82bdd2105d5) Lars Persson)  
-  RegelResponse source and type is now correct (#54) ([34220](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/3422081fb44eaa9) NilsElveros)  
-  add type to RegelResponse (#53) ([22074](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/220747c6e37d4d0) NilsElveros)  
-  Use consumer and producer from rimfrost-common ([e2b21](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/e2b21ab1babe11b) Lars Persson)  

## 0.3.0 (2026-01-15)

### Features

-  add healthcheck (#49) ([fee65](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/fee65c765891173) NilsElveros)  
-  update the PUT to kundbehovsservice with underlag (#47) ([99137](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/99137a5bf3ba512) NilsElveros)  

### Bug Fixes

-  tar bort ej fungerande unit-tester ([ace1e](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/ace1ef7dd903f91) Ulf Slunga)  
-  blankrader för att inte spotless ska joina rader ! ([38c03](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/38c030acb139cea) Ulf Slunga)  
-  spotless apply ([cf4ed](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/cf4edb215be535c) Ulf Slunga)  
-  spotless apply ([c73e8](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/c73e82aa2fcb33a) Ulf Slunga)  
-  evaluera rätt till försäkring ist för hårdkodat utredning ([2d958](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/2d958dfed5b3d34) Ulf Slunga)  

### Dependency updates

- update testcontainers-java monorepo to v1.21.4 ([8dbe9](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/8dbe96a0c3beb16) renovate[bot])  
- update testcontainers-java monorepo to v1.21.3 ([485e1](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/485e1c7cb934b06) renovate[bot])  
- update dependency org.wiremock:wiremock to v3.13.2 ([43e79](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/43e7966cd9fba3c) renovate[bot])  
## 0.2.1 (2025-12-04)

### Bug Fixes

-  tillfällig fix att alltid returnera utredning ([99bf3](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/99bf3fe7798ae39) Ulf Slunga)  
-  tillfällig fix att alltid returnera utredning ([1cc7f](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/1cc7fda55b44c1e) Ulf Slunga)  
-  tillfällig fix att alltid returnera utredning ([98dd9](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/98dd92d92a3a4ef) Ulf Slunga)  
-  tillfällig fix att alltid returnera utredning ([8cb55](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/8cb55859e0ddf52) Ulf Slunga)  

### Other changes

**remove kogitoprocrefid from request**


[2d409](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/2d409e503bc678f) Nils Elveros *2025-12-04 10:26:27*


## 0.2.0 (2025-12-04)

### Bug Fixes

-  reformat pom ([d7576](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/d7576c552004412) Ulf Slunga)  
-  mvn spotless apply ([dc2e1](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/dc2e1c4afb7c94d) Ulf Slunga)  
-  bumpar version av arbetsgivar-api. nullable i anställnings-data i integration. tar bort felaktiga imports ([c525f](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/c525fcf80c047cc) Ulf Slunga)  
-  mvn spotless apply ([b2930](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/b29302f2c51d5dc) Ulf Slunga)  
-  Ta bort ersättning från KundbehovsflodeResponse i integrationen eftersom regeln ej behöver infon för beslut ([f3b3f](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/f3b3f97df3ac19e) Ulf Slunga)  
-  Hantera logik för folkbokford i RtfService för att kunna spara det som underlag efter regeln ([2eb9f](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/2eb9f049d1a8d2f) Ulf Slunga)  
-  Hantera logik för arbetsgivare i RtfService för att kunna spara det som underlag efter regeln ([79811](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/79811341aad160b) Ulf Slunga)  
-  mvn spotless apply ([1daa7](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/1daa7bb04edc493) Ulf Slunga)  
-  minimerar datat i integrationslagret som behövs av regeln ([3693d](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/3693d172aa1cb75) Ulf Slunga)  
-  mvn spotless apply ([81e2b](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/81e2b2b770b1a81) Ulf Slunga)  

### Other changes

**Merge branch 'main' into feature/FKPOC-199-kundbehovsflode-full-logik**


[11627](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/1162706d11ac939) Ulf Slunga *2025-12-04 08:35:52*


## 0.1.0 (2025-12-03)

### Bug Fixes

-  Use the correct import of rtf model and some small refactoring (#34) ([9288b](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/9288ba643907e6e) NilsElveros)  
-  set specversion in the response ([144d3](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/144d36768154325) David Söderberg)  
-  spotless code ([0c5b7](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/0c5b74ed1bf3447) David Söderberg)  
-  swap image building back to true ([cc187](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/cc1872642ac2525) David Söderberg)  
-  spotless format (#30) ([1dc95](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/1dc957e7a18556c) NilsElveros)  
-  messages as cloudevents with kogito routing parameters (#29) ([d873a](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/d873a097b55f69f) NilsElveros)  
-  Seperated the two integration services ([157ca](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/157caa73e23afb0) rikrhen)  
-  last missed variable ([80ee0](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/80ee09e6e880eff) rikrhen)  
-  some missed variable names ([0e2d5](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/0e2d57339bd5994) rikrhen)  
-  minor optimization ([58d97](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/58d97c43666b8c6) rikrhen)  
-  java 21 fixed version from latest ([1feba](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/1febad4cd1f0c40) rikrhen)  
-  duplicate dep from parent ([2df64](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/2df64aed498f6a9) rikrhen)  
-  removed old debugging logger ([7a2b9](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/7a2b965ad72d84c) rikrhen)  
-  integration package put back where it belongs ([314b4](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/314b4b9b305adfa) rikrhen)  
-  FKPOC-86 Changed folkbokford url to prefix rimfrost- ([0544b](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/0544b293d77a1ef) Ulf Slunga)  
-  removing plugin maven-compile-plugin which is inherited from parent ([3a3c0](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/3a3c0b84d926779) Ulf Slunga)  
-  using java 21 ([f2e38](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/f2e38bd4a911960) Ulf Slunga)  
-  removing not required dependencies ([55c7a](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/55c7a869ae28545) Ulf Slunga)  

### Dependency updates

- update testcontainers-java monorepo to v1.21.3 ([32645](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/326451306faa019) renovate[bot])  
- update dependency org.wiremock:wiremock to v3.13.2 ([86fec](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/86fece8c8bc7ebc) renovate[bot])  
### Other changes

**Initial version always returning UTREDNING**


[1689e](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/1689e6e2f27ee4f) Ulf Slunga *2025-12-03 09:22:04*

**Merge branch 'main' into feature/FKPOC-72-add-junit-testing**

* # Conflicts: 
* #	pom.xml 
* #	src/main/java/se/fk/github/regelratttillforsakring/presentation/VahRtfProcessor.java 

[ec2c5](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/ec2c547a346977f) rikrhen *2025-11-06 11:11:21*

**Merge branch 'main' into feature/FKPOC-72-add-junit-testing**

* # Conflicts: 
* #	src/main/java/se/fk/github/regelratttillforsakring/presentation/VahRtfProcessor.java 

[5cbe5](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/5cbe59b73e15c74) rikrhen *2025-11-05 14:56:57*


## 0.0.9 (2025-10-28)

### Bug Fixes

-  serializer please serialize ([a9b2b](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/a9b2ba9611e5b6f) rikrhen)  

## 0.0.8 (2025-10-28)

### Bug Fixes

-  spotless ([e6199](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/e61991640e825f6) rikrhen)  
-  deserializer please deserialize ([e6612](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/e6612130718b5a0) rikrhen)  

## 0.0.7 (2025-10-28)

### Bug Fixes

-  request -> requests ([8e2ba](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/8e2baae60b75e06) rikrhen)  

## 0.0.6 (2025-10-28)

### Bug Fixes

-  smoother deserialization ([aa080](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/aa0805b942781e8) rikrhen)  
-  pointing to correct deserializer ([4df40](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/4df402d99140da9) rikrhen)  
-  added deserializer ([1b9b0](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/1b9b07fb210f711) rikrhen)  

### Other changes

**Merge branch 'main' into fix/get-full-flow-working**


[93865](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/938653d8db529f7) rikrhen *2025-10-28 10:22:16*


## 0.0.5 (2025-10-27)

### Bug Fixes

-  Plural for all topic names in incoming and outgoing annotations ([7d8b0](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/7d8b0b0a746d025) Ulf Slunga)  

## 0.0.4 (2025-10-27)

### Bug Fixes

-  Plural for all topic names ([fc32f](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/fc32f9a3baffeae) Ulf Slunga)  

## 0.0.3 (2025-10-27)

### Bug Fixes

-  implementing Swedish naming scheme, sorted ([36cfc](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/36cfc2b32cd75d0) rikrhen)  
-  implementing Swedish naming scheme ([5ee9c](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/5ee9cb8a87a18f6) rikrhen)  

### Other changes

**Merge branch 'main' into fix/get-full-flow-working**


[db707](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/db7072223648a77) rikrhen *2025-10-27 14:37:43*

**Changing from singular to plural forms.**


[0cc9e](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/0cc9ec679f0eb33) rikrhen *2025-10-27 13:35:00*


## 0.0.2 (2025-10-27)

### Bug Fixes

-  **deps**  update dependency se.fk.maven:fk-maven-quarkus-parent to v1.10.1 ([cf4c1](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/cf4c1e89d865a9c) renovate[bot])  
-  **deps**  update dependency se.fk.github.jaxrsclientfactory:jaxrs-client-factory to v1.1.1 ([22cb7](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/22cb7cfdf17fdc9) renovate[bot])  
-  **deps**  update dependency se.fk.gradle.examples:example-jaxrs-spec to v1.10.1 ([8cf6a](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/8cf6a740621f22b) renovate[bot])  
-  small correction for the folkbokford integration to work (#7) ([151a4](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/151a46d44c2600f) NilsElveros)  

### Other changes

**application.properties changes to hopefully fix pod.**


[1983c](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/1983ca1404c4b3e) rikrhen *2025-10-27 12:53:57*

**No fancy stuff**


[ae67e](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/ae67e964be39a9e) rikrhen *2025-10-24 08:55:38*

**Adding kubernetes value to application.properties base URL**


[753db](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/753dbc753500f5a) rikrhen *2025-10-24 08:46:20*

**API base url sorted**


[b32df](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/b32df3427f59beb) rikrhen *2025-10-23 10:42:52*

**Delete src/main/java/presentation/FolkbokfordController.java**

* Used for testing, irrelevant for final version of branch 

[9a3ec](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/9a3ec03542f47c5) Riki Rhen *2025-10-23 07:16:12*

**Cleanup**


[487d4](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/487d43e545146e3) rikrhen *2025-10-22 13:48:03*

**controller fix**


[a5255](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/a52555ca73a126b) rikrhen *2025-10-22 13:36:21*

**Post comment stuff**


[c0700](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/c07004da12f6a78) rikrhen *2025-10-22 13:33:22*

**Spotless cleaning**


[c36b5](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/c36b520a931addb) rikrhen *2025-10-22 10:54:07*

**Post comment changes - api call from generated specs**


[94bed](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/94bed0909605381) rikrhen *2025-10-22 10:52:39*

**Update application.properties**


[15788](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/15788933e4773eb) Riki Rhen *2025-10-22 10:19:49*

**Update PresentationVahRtfResponse.java**

* last old comment 

[85e1b](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/85e1b33e71f7460) Riki Rhen *2025-10-22 09:05:25*

**Update PresentationVahRtfRequest.java**

* more old comments 

[23287](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/23287f2cdae6b04) Riki Rhen *2025-10-22 09:05:01*

**Update FolkbokfordService.java**

* Cleanup of old comments 

[f1581](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/f1581bab8491501) Riki Rhen *2025-10-22 09:04:31*

**Delete src/main/java/logic/Folkbokford.java**

* Delete old unused interface 

[cf972](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/cf972d9eb44c0b5) Riki Rhen *2025-10-22 09:03:15*

**Update pom.xml**

* fixed double dependency in POM 

[4874e](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/4874eecd6b208df) Riki Rhen *2025-10-22 08:59:59*

**api chain implementation**


[7077c](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/7077c76f14bb7c4) rikrhen *2025-10-22 08:55:25*


## 0.0.1 (2025-10-20)

### Features

-  refactor ([ac844](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/ac844337912a10d) Tomas Bjerre)  
-  operationId ([3fa93](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/3fa930358b1910e) Tomas Bjerre)  
-  stegar API och Docker ([e01d6](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/e01d60f1b1dadad) Tomas Bjerre)  
-  use Spotless plugin with code standard from jar ([d9046](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/d9046c2b6e81fb0) Tomas Bjerre)  
-  publish till gemensamt repo ([ffe1b](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/ffe1b02eed2716e) Tomas Bjerre)  
-  publicerar till gemensamt repository ([5ba77](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/5ba77e7ce9b3acb) Tomas Bjerre)  
-  parent ([92b63](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/92b63d6dac54057) Tomas Bjerre)  
-  parent ([6b9ac](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/6b9ac72b380f482) Tomas Bjerre)  

### Bug Fixes

-  change pom.xml artifactId ([07f34](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/07f3450af228c58) David Söderberg)  
-  **deps**  update dependency org.immutables:value to v2.11.6 ([e0f73](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/e0f73ebfede4a5b) renovate[bot])  
-  **deps**  update quarkus.platform.version to v3.28.3 ([63399](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/63399b2784c0947) renovate[bot])  

### Dependency updates

- add renovate.json ([0814f](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/0814fb3ccde5d3e) renovate[bot])  
- update dependency org.apache.maven.plugins:maven-compiler-plugin to v3.14.1 ([41ac0](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/41ac0d364cf6123) renovate[bot])  
- fk-maven ([ce078](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/ce078ccdc0d9179) Tomas Bjerre)  
- update dependency org.apache.maven.plugins:maven-compiler-plugin to v3.14.1 ([e1c97](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/e1c9733a48854e2) renovate[bot])  
### Other changes

**Spotless cleaning**


[dea02](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/dea0276ef23eb05) rikrhen *2025-10-20 08:36:33*

**REST API call**


[421e2](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/421e2451a16671c) rikrhen *2025-10-20 08:01:19*

**Merge branch 'feature/parent'**


[8c9a8](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/8c9a89e55dcf2fd) Tomas Bjerre *2025-10-09 16:09:20*

**first commit**


[18d4a](https://github.com/Forsakringskassan/rimfrost-regel-rtf-maskinell/commit/18d4ab1d6d92ad5) Tomas Bjerre *2025-10-09 10:47:31*


