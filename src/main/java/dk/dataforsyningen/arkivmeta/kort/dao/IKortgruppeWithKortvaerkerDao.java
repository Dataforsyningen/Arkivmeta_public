package dk.dataforsyningen.arkivmeta.kort.dao;

import dk.dataforsyningen.arkivmeta.kort.apimapper.KortgruppeWithKortvaerkerMapper;
import dk.dataforsyningen.arkivmeta.kort.apimodel.KortgruppeWithKortvaerkerDto;
import java.util.List;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface IKortgruppeWithKortvaerkerDao {
  /**
   * @RegisterRowMapper use the registered mapper to map the select columns from the database to KortgruppeDto
   * https://jdbi.org/#_registerrowmapper
   */
  @SqlQuery("""
      SELECT kortgruppe, kortvaerk
      FROM historiskekort.kortgruppe_kortvaerk
      """)
  @RegisterRowMapper(KortgruppeWithKortvaerkerMapper.class)
  List<KortgruppeWithKortvaerkerDto> getAllKortgrupperWithKortvaerker();
}
