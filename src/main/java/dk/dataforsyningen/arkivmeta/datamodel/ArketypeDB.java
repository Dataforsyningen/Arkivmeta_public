package dk.dataforsyningen.arkivmeta.datamodel;

public class ArketypeDB {
  private String arketype;

  private String arkenavn;

  private String kortvaerk;

  public ArketypeDB() {
  }

  public ArketypeDB(String arketype, String arkenavn, String kortvaerk) {
    this.arketype = arketype;
    this.arkenavn = arkenavn;
    this.kortvaerk = kortvaerk;
  }

  public String getArketype() {
    return arketype;
  }

  public void setArketype(String arketype) {
    this.arketype = arketype;
  }

  public String getArkenavn() {
    return arkenavn;
  }

  public void setArkenavn(String arkenavn) {
    this.arkenavn = arkenavn;
  }

  public String getKortvaerk() {
    return kortvaerk;
  }

  public void setKortvaerk(String kortvaerk) {
    this.kortvaerk = kortvaerk;
  }
}
