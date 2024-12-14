package dk.dataforsyningen.arkivmeta.kort.apimodel;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;

public class KortgruppeWithKortvaerkerDto {

  @Schema(description = "Kortgruppe.")
  private String kortgruppe;

  @ArraySchema(arraySchema = @Schema(description = "Liste af logiske samlinger af skannede kort som tilhører kortgruppen."))
  private List<String> kortvaerker = new ArrayList<>();

  public KortgruppeWithKortvaerkerDto() {
  }

  public String getKortgruppe() {
    return kortgruppe;
  }

  public void setKortgruppe(String kortgruppe) {
    this.kortgruppe = kortgruppe;
  }

  public List<String> getKortvaerker() {
    return kortvaerker;
  }

  public void setKortvaerker(List<String> kortvaerker) {
    this.kortvaerker = kortvaerker;
  }

  @Override
  public String toString() {
    return "KortgruppeDto{" +
            ", kortgruppe='" + kortgruppe + '\'' +
            ", kortvaerker='" + kortvaerker + '\'' +
            '}';
  }
}
