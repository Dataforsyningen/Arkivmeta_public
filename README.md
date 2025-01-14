# ArkivMeta

REST API til søgning i metadata til brug for udstilling af SDFEs og GSTs skannede kort.

Baseret på Spring Boot, Hibernate og OpenAPI. Skrevet i Java.

## Breaking change:

### kortgruppe

Man skal ikke længere angive flere værdier med `|` som seperator men i stedet for bruge komma `,`.

### daekningsomraade

Man skal ikke længere angive flere værdier med `|` som seperator men i stedet for bruge komma `,`.

### gaeldendeperiode_gaeldendefra

Har ikke længere end default værdi, så hvis man ikke angiver et årstal, søges der for alle år

### gaeldendeperiode_gaeldendetil

Har ikke længere end default værdi, så hvis man ikke angiver et årstal, søges der for alle år

### kortvaerk

Hvis man ønsker at angive mere end et kortværk der skal søges efter, skal man i stedet angive `kortvaerk` query
parameteren for hvert eneste en kortværk man vil søge efter.

### maalestok

Man skal ikke længere angive flere værdier med `|` som seperator men i stedet for bruge komma `,`.

### pagesize

Er blevet ændret til `limit`.