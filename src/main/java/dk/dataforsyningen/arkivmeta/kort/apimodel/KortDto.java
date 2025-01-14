package dk.dataforsyningen.arkivmeta.kort.apimodel;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class KortDto {
  @Schema(description = "Unik id for kortet.")
  private UUID id;

  @Schema(description = "Kortets kortgruppe.")
  private String kortgruppe;

  @Schema(description = "Titlen på kortet.")
  private String titel;

  @Schema(description = "En eventuel anden titel kortet kan have.")
  private String alternativtitel;

  @Schema(description = "Den projektion (afbildning af jorden på en plan flade) det oprindelige kort blev defineret i, f.eks. GS for Generalstabens projektion.")
  private String originalkortprojektion;

  @Schema(description = "Yderligere kommentarer, f.eks. vedr. fremstilling eller placering ved kendt gods eller lign.")
  private String bemaerkning;

  @Schema(description = "Starttid for kortets gyldighedsperiode. Angives i hele år. Gyldighedsperiodens starttid er et korts trykke-, tegne-, optage- eller opmålingsår – dvs. det år hvor kortet kan siges at være nyeste kort.")
  private BigDecimal gaeldendeperiode_gaeldendefra;

  @Schema(description = "Sluttid for kortets gyldighedsperiode. Angives i hele år. Typisk fordi kortet erstattes af et nyere. Hvis der ikke er fundet en specifik gældende til periode angives et årstal der ligger 50 år efter gældende fra.")
  private BigDecimal gaeldendeperiode_gaeldendetil;

  @Schema(description = "Det geografiske område, ofte en polygon, som kortet ligger indenfor. WKT med SRS = EPSG:4326")
  private String geometri;

  @Schema(description = "Størrelsesforholdet mellem landskabet og kortets repræsentation heraf.")
  private String maalestok;

  @Schema(description = "Angivelse af geografisk område, f.eks. indenfor kortbladsinddelingen eller ejerlav. For topografiske kort er inddelingen et overordnet grid hvorimod matrikelkort er indelt efter ejerlav. Kan også være f.eks. administrative inddelinger.")
  private String kortbladnummer;

  @Schema(description = "Navnet på en logisk samling af skannede kort som kortet hører til, f.eks. atlasblade.")
  private String kortvaerk;

  @Schema(description = "Geografisk område, som kortet dækker helt eller delvist. For eksempel Danmark, Grønland. Et kort kan have flere dækningsområder.For prøvekort og lignende vil dækningsområdet kunne angives som intet.")
  private List<String> daekningsomraade;

  @Schema(description = "En liste af URL-stier til kortfiler efter IIIF-specifikationen.")
  private List<String> filer;

  @Schema(description = "Kort udarbejdet på grundlag af data fra dette år eller data sammenstillet i dette år.")
  private BigDecimal aarfordata;

  @Schema(description = "År for opdatering af kortet med enkelte rettelser, f.eks. en ny bro.")
  private List<BigDecimal> aarforenkeltrettelser;

  @Schema(description = "År for optagelse af flyfoto til brug for produktion af kortet.")
  private BigDecimal aarforfotografering;

  @Schema(description = "År hvor kortet er lavet på basis af flyfoto.")
  private BigDecimal aarforfotogrametriskudtegning;

  @Schema(description = "Arkivteknisk betegnelse for kort der er gået i arkiv. 9999 anvendes for ukendt år.")
  private BigDecimal aarforhenlaeggelse;

  @Schema(description = "År, hvor kortet er opdateret efter opmåling i marken.")
  private BigDecimal aarforkompleteteretimarken;

  @Schema(description = "Anvendes pt ikke.")
  private BigDecimal aarforkortproeve;

  @Schema(description = "År for rettelser på grundlag af lodret fotografering.")
  private BigDecimal aarforlineaerrettelse;

  @Schema(description = "Det år den oprindelige opmåling til kortet blev afsluttet. Kortet kan senere være nymålt eller rettet.")
  private BigDecimal aarformaalt;

  @Schema(description = "År for opmåling af matrikelkortet. Hvis kortet er opmålt i flere etaper, er det afslutningen på sidste etape.")
  private BigDecimal aarforopmaalingsluttet;

  @Schema(description = "År hvor kortets punktgrundlag er skabt eller opdateret.")
  private BigDecimal aarforpunktgrundlag;

  @Schema(description = "År for opdatering af kortet med rettelser, typisk efter at kontrolmålinger er udført.")
  private List<BigDecimal> aarforrettelse;

  @Schema(description = "År hvor kortets topografiske elementer er kortlagt eller opdateret.")
  private BigDecimal aarfortopografi;

  @Schema(description = "Året hvor udarbejdelsen blev afsluttet første gang.")
  private BigDecimal aarforudarbejdelse;

  @Schema(description = "Året hvor kortet blev færdigtegnet.")
  private BigDecimal aarforudarbejdetmateriale;

  @Schema(description = "Det år kortet blev udgivet eller trykt. Kortene er udgivet efter deres opmåling og eventuelt genudgivet som følge af rettelser eller at kortet blev udsolgt.")
  private BigDecimal aarforudgivelse;

  @Schema(description = "År for udskiftningen af den pågældende landsby eller ejerlav.")
  private BigDecimal aarforudskiftning;

  @Schema(description = "År for udtegning i 1:200000 på grundlag af skråfotografering.")
  private BigDecimal aarforudtegning;

  @Schema(description = "År hvor kortet er opdateret med vejdata.")
  private BigDecimal aarforvejdata;

  @Schema(description = "Nummeret på dåsen, som indeholder billedets originalnegativ.")
  private String daasenummer;

  @Schema(description = "Farveskema anvendt i det digitale billede, f.eks. sort/hvid 8 bit.")
  private String farveskalatype;

  @Schema(description = "Flyvehøjden ved optagelsen af billedet.")
  private Double flyvehoejde;

  @Schema(description = "Navnet på flyets flyverute. Der er optaget ét eller flere fotos pr flyverute.")
  private String flyverute;

  @Schema(description = "Fortløbende nummerering af flyfotos. Nummeret er unikt indenfor en flyverute eller evt. kun indenfor et flyfotograferingsprojekt bestående af flere flyveruter.")
  private String fotonummer;

  @Schema(description = "Tidspunkt for optagelse af flyvefotoet. Tiden består typisk af år, måned, dag og tidspunkt.")
  private OffsetDateTime fototid;

  @Schema(description = "Beskrivelse af vinkel mod jordoverfladen for flyfotografiet, f.eks. lodfoto eller skråfoto.")
  private String fotovinkel;

  @Schema(description = "Kameratype eller identifikation af kameraet, der er anvendt til flyfotograferingen.")
  private String kameraid;

  @Schema(description = "Oplysninger vedr. produktionen af kortet, f.eks. om det er aktivt, henlagt el.lign.")
  private String kortart;

  @Schema(description = "Navn på person der har opmålt og/eller tegnet kortet.")
  private String opmaaltaf;

  @Schema(description = "Plannummer for matrikelkortet. Plannummer anvendes hvis kort over et ejerlav er fordelt på flere kortblade.")
  private String plannr;

  @Schema(description = "Navn på den organisation der har forestået flyfotograferingen.")
  private String producent;

  @Schema(description = "Navn på personen der har tegnet kortet, kortet kan være opmålt af en eller flere opmålere, evt. også af tegneren.")
  private String tegner;

  @Schema(description = "Navn på den organisation der har produceret kortet.")
  private String udgiver;

  @Schema(description = "Navn på person der har forestået udskiftningen af en landsby eller ejerlav.")
  private String udskiftetaf;

  @Schema(description = "Versionsnummer for kortet. Et kort kan være udgivet i flere versioner.")
  private String version;

  public KortDto() {

  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getKortgruppe() {
    return kortgruppe;
  }

  public void setKortgruppe(String kortgruppe) {
    this.kortgruppe = kortgruppe;
  }

  public String getTitel() {
    return titel;
  }

  public void setTitel(String titel) {
    this.titel = titel;
  }

  public String getAlternativtitel() {
    return alternativtitel;
  }

  public void setAlternativtitel(String alternativtitel) {
    this.alternativtitel = alternativtitel;
  }

  public String getOriginalkortprojektion() {
    return originalkortprojektion;
  }

  public void setOriginalkortprojektion(String originalkortprojektion) {
    this.originalkortprojektion = originalkortprojektion;
  }

  public String getBemaerkning() {
    return bemaerkning;
  }

  public void setBemaerkning(String bemaerkning) {
    this.bemaerkning = bemaerkning;
  }

  public BigDecimal getGaeldendeperiode_gaeldendefra() {
    return gaeldendeperiode_gaeldendefra;
  }

  public void setGaeldendeperiode_gaeldendefra(BigDecimal gaeldendeperiode_gaeldendefra) {
    this.gaeldendeperiode_gaeldendefra = gaeldendeperiode_gaeldendefra;
  }

  public BigDecimal getGaeldendeperiode_gaeldendetil() {
    return gaeldendeperiode_gaeldendetil;
  }

  public void setGaeldendeperiode_gaeldendetil(BigDecimal gaeldendeperiode_gaeldendetil) {
    this.gaeldendeperiode_gaeldendetil = gaeldendeperiode_gaeldendetil;
  }

  public String getGeometri() {
    return geometri;
  }

  public void setGeometri(String geometri) {
    this.geometri = geometri;
  }

  public String getMaalestok() {
    return maalestok;
  }

  public void setMaalestok(String maalestok) {
    this.maalestok = maalestok;
  }

  public String getKortbladnummer() {
    return kortbladnummer;
  }

  public void setKortbladnummer(String kortbladnummer) {
    this.kortbladnummer = kortbladnummer;
  }

  public String getKortvaerk() {
    return kortvaerk;
  }

  public void setKortvaerk(String kortvaerk) {
    this.kortvaerk = kortvaerk;
  }

  public List<String> getDaekningsomraade() {
    return daekningsomraade;
  }

  public void setDaekningsomraade(List<String> daekningsomraade) {
    this.daekningsomraade = daekningsomraade;
  }

  public List<String> getFiler() {
    return filer;
  }

  public void setFiler(List<String> filer) {
    this.filer = filer;
  }

  public BigDecimal getAarfordata() {
    return aarfordata;
  }

  public void setAarfordata(BigDecimal aarfordata) {
    this.aarfordata = aarfordata;
  }

  public List<BigDecimal> getAarforenkeltrettelser() {
    return aarforenkeltrettelser;
  }

  public void setAarforenkeltrettelser(List<BigDecimal> aarforenkeltrettelser) {
    this.aarforenkeltrettelser = aarforenkeltrettelser;
  }

  public BigDecimal getAarforfotografering() {
    return aarforfotografering;
  }

  public void setAarforfotografering(BigDecimal aarforfotografering) {
    this.aarforfotografering = aarforfotografering;
  }

  public BigDecimal getAarforfotogrametriskudtegning() {
    return aarforfotogrametriskudtegning;
  }

  public void setAarforfotogrametriskudtegning(BigDecimal aarforfotogrametriskudtegning) {
    this.aarforfotogrametriskudtegning = aarforfotogrametriskudtegning;
  }

  public BigDecimal getAarforhenlaeggelse() {
    return aarforhenlaeggelse;
  }

  public void setAarforhenlaeggelse(BigDecimal aarforhenlaeggelse) {
    this.aarforhenlaeggelse = aarforhenlaeggelse;
  }

  public BigDecimal getAarforkompleteteretimarken() {
    return aarforkompleteteretimarken;
  }

  public void setAarforkompleteteretimarken(BigDecimal aarforkompleteteretimarken) {
    this.aarforkompleteteretimarken = aarforkompleteteretimarken;
  }

  public BigDecimal getAarforkortproeve() {
    return aarforkortproeve;
  }

  public void setAarforkortproeve(BigDecimal aarforkortproeve) {
    this.aarforkortproeve = aarforkortproeve;
  }

  public BigDecimal getAarforlineaerrettelse() {
    return aarforlineaerrettelse;
  }

  public void setAarforlineaerrettelse(BigDecimal aarforlineaerrettelse) {
    this.aarforlineaerrettelse = aarforlineaerrettelse;
  }

  public BigDecimal getAarformaalt() {
    return aarformaalt;
  }

  public void setAarformaalt(BigDecimal aarformaalt) {
    this.aarformaalt = aarformaalt;
  }

  public BigDecimal getAarforopmaalingsluttet() {
    return aarforopmaalingsluttet;
  }

  public void setAarforopmaalingsluttet(BigDecimal aarforopmaalingsluttet) {
    this.aarforopmaalingsluttet = aarforopmaalingsluttet;
  }

  public BigDecimal getAarforpunktgrundlag() {
    return aarforpunktgrundlag;
  }

  public void setAarforpunktgrundlag(BigDecimal aarforpunktgrundlag) {
    this.aarforpunktgrundlag = aarforpunktgrundlag;
  }

  public List<BigDecimal> getAarforrettelse() {
    return aarforrettelse;
  }

  public void setAarforrettelse(List<BigDecimal> aarforrettelse) {
    this.aarforrettelse = aarforrettelse;
  }

  public BigDecimal getAarfortopografi() {
    return aarfortopografi;
  }

  public void setAarfortopografi(BigDecimal aarfortopografi) {
    this.aarfortopografi = aarfortopografi;
  }

  public BigDecimal getAarforudarbejdelse() {
    return aarforudarbejdelse;
  }

  public void setAarforudarbejdelse(BigDecimal aarforudarbejdelse) {
    this.aarforudarbejdelse = aarforudarbejdelse;
  }

  public BigDecimal getAarforudarbejdetmateriale() {
    return aarforudarbejdetmateriale;
  }

  public void setAarforudarbejdetmateriale(BigDecimal aarforudarbejdetmateriale) {
    this.aarforudarbejdetmateriale = aarforudarbejdetmateriale;
  }

  public BigDecimal getAarforudgivelse() {
    return aarforudgivelse;
  }

  public void setAarforudgivelse(BigDecimal aarforudgivelse) {
    this.aarforudgivelse = aarforudgivelse;
  }

  public BigDecimal getAarforudskiftning() {
    return aarforudskiftning;
  }

  public void setAarforudskiftning(BigDecimal aarforudskiftning) {
    this.aarforudskiftning = aarforudskiftning;
  }

  public BigDecimal getAarforudtegning() {
    return aarforudtegning;
  }

  public void setAarforudtegning(BigDecimal aarforudtegning) {
    this.aarforudtegning = aarforudtegning;
  }

  public BigDecimal getAarforvejdata() {
    return aarforvejdata;
  }

  public void setAarforvejdata(BigDecimal aarforvejdata) {
    this.aarforvejdata = aarforvejdata;
  }

  public String getDaasenummer() {
    return daasenummer;
  }

  public void setDaasenummer(String daasenummer) {
    this.daasenummer = daasenummer;
  }

  public String getFarveskalatype() {
    return farveskalatype;
  }

  public void setFarveskalatype(String farveskalatype) {
    this.farveskalatype = farveskalatype;
  }

  public Double getFlyvehoejde() {
    return flyvehoejde;
  }

  public void setFlyvehoejde(Double flyvehoejde) {
    this.flyvehoejde = flyvehoejde;
  }

  public String getFlyverute() {
    return flyverute;
  }

  public void setFlyverute(String flyverute) {
    this.flyverute = flyverute;
  }

  public String getFotonummer() {
    return fotonummer;
  }

  public void setFotonummer(String fotonummer) {
    this.fotonummer = fotonummer;
  }

  public OffsetDateTime getFototid() {
    return fototid;
  }

  public void setFototid(OffsetDateTime fototid) {
    this.fototid = fototid;
  }

  public String getFotovinkel() {
    return fotovinkel;
  }

  public void setFotovinkel(String fotovinkel) {
    this.fotovinkel = fotovinkel;
  }

  public String getKameraid() {
    return kameraid;
  }

  public void setKameraid(String kameraid) {
    this.kameraid = kameraid;
  }

  public String getKortart() {
    return kortart;
  }

  public void setKortart(String kortart) {
    this.kortart = kortart;
  }

  public String getOpmaaltaf() {
    return opmaaltaf;
  }

  public void setOpmaaltaf(String opmaaltaf) {
    this.opmaaltaf = opmaaltaf;
  }

  public String getPlannr() {
    return plannr;
  }

  public void setPlannr(String plannr) {
    this.plannr = plannr;
  }

  public String getProducent() {
    return producent;
  }

  public void setProducent(String producent) {
    this.producent = producent;
  }

  public String getTegner() {
    return tegner;
  }

  public void setTegner(String tegner) {
    this.tegner = tegner;
  }

  public String getUdgiver() {
    return udgiver;
  }

  public void setUdgiver(String udgiver) {
    this.udgiver = udgiver;
  }

  public String getUdskiftetaf() {
    return udskiftetaf;
  }

  public void setUdskiftetaf(String udskiftetaf) {
    this.udskiftetaf = udskiftetaf;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }
}
