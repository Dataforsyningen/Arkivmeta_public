package dk.dataforsyningen.arkivmeta.kort.dao;

import dk.dataforsyningen.arkivmeta.kort.apimodel.KortDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jdbi.v3.core.Jdbi;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class KortDao implements IKortDao {

  @Qualifier("arkivmetaJdbi")
  private final Jdbi arkivmetaJdbi;

  @Autowired
  public KortDao(@Qualifier("arkivmetaJdbi") Jdbi arkivmetaJdbi) {
    this.arkivmetaJdbi = arkivmetaJdbi;
  }

  @Override
  public Optional<KortDto> getKortById(UUID id) {
    return arkivmetaJdbi.withExtension(IKortDao.class, dao -> dao.getKortById(id));
  }

  @Override
  public List<KortDto> getAllKort(List<String> kortgruppe, String daekningsomraade,
                                  String fritekstsoegning, Integer gaeldendeperiode_gaeldendefra,
                                  Integer gaeldendeperiode_gaeldendetil, Geometry area, String kortbladnummer,
                                  List<String> kortvaerk, List<String> maalestok, String tegner,
                                  String titel, int limit, int offset, String sort,
                                  String direction) {
    return arkivmetaJdbi.withExtension(IKortDao.class,
        dao -> dao.getAllKort(kortgruppe, daekningsomraade, fritekstsoegning, gaeldendeperiode_gaeldendefra,
            gaeldendeperiode_gaeldendetil, area, kortbladnummer, kortvaerk, maalestok, tegner, titel, limit, offset,
            sort, direction));
  }

  @Override
  public Long getCount(List<String> kortgruppe, String daekningsomraade,
                       String fritekstsoegning, Integer gaeldendeperiode_gaeldendefra,
                       Integer gaeldendeperiode_gaeldendetil, Geometry area, String kortbladnummer,
                       List<String> kortvaerk, List<String> maalestok, String tegner,
                       String titel) {
    return arkivmetaJdbi.withExtension(IKortDao.class,
        dao -> dao.getCount(kortgruppe, daekningsomraade, fritekstsoegning, gaeldendeperiode_gaeldendefra,
            gaeldendeperiode_gaeldendetil, area, kortbladnummer, kortvaerk, maalestok, tegner, titel));
  }
}
