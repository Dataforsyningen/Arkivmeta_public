package dk.dataforsyningen.arkivmeta.kort.dao;

import dk.dataforsyningen.arkivmeta.kort.apimodel.KortgruppeWithKortvaerkerDto;
import java.util.List;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class KortgruppeWithKortvaerkerDao implements IKortgruppeWithKortvaerkerDao {

  @Qualifier("arkivmetaJdbi")
  private final Jdbi arkivmetaJdbi;

  @Autowired
  public KortgruppeWithKortvaerkerDao(@Qualifier("arkivmetaJdbi") Jdbi arkivmetaJdbi) {
    this.arkivmetaJdbi = arkivmetaJdbi;
  }

  @Override
  public List<KortgruppeWithKortvaerkerDto> getAllKortgrupperWithKortvaerker() {
    return arkivmetaJdbi.withExtension(IKortgruppeWithKortvaerkerDao.class, dao -> dao.getAllKortgrupperWithKortvaerker());
  }
}
