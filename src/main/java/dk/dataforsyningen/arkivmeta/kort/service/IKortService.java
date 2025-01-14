package dk.dataforsyningen.arkivmeta.kort.service;

import dk.dataforsyningen.arkivmeta.kort.apimodel.DaekningsomraadeDto;
import dk.dataforsyningen.arkivmeta.kort.apimodel.KortDto;
import dk.dataforsyningen.arkivmeta.kort.apimodel.Kortvaerk;
import dk.dataforsyningen.arkivmeta.kort.apimodel.KortgruppeWithKortvaerkerDto;
import dk.dataforsyningen.arkivmeta.kort.apimodel.KortParam;
import dk.dataforsyningen.arkivmeta.kort.apimodel.KortResult;
import dk.dataforsyningen.arkivmeta.kort.apimodel.MaalestokDto;
import java.util.List;
import java.util.UUID;

public interface IKortService {
  List<KortgruppeWithKortvaerkerDto> getKortgrupperWithKortvaerker();

  KortDto getKortById(UUID id);

  KortResult getKortResult(KortParam kortParam, Kortvaerk kortvaerk);

  List<DaekningsomraadeDto> getDaekningsomraader(String daekningsomraade);

  List<MaalestokDto> getMaalestokke(String maalestok);
}
