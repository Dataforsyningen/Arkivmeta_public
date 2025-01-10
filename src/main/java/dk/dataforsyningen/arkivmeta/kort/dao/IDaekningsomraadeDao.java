package dk.dataforsyningen.arkivmeta.kort.dao;

import dk.dataforsyningen.arkivmeta.kort.apimapper.DaekningsomraadeMapper;
import dk.dataforsyningen.arkivmeta.kort.apimodel.DaekningsomraadeDto;
import java.util.List;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface IDaekningsomraadeDao {

  /**
   * @RegisterRowMapper use the registered mapper to map the select columns from the database to DaekningsomraadeDto
   * https://jdbi.org/#_registerrowmapper
   */
  @SqlQuery("""
      SELECT
          daekningsomraade
      FROM
          historiskekort.daekningsomraade
      WHERE
          (:daekningsomraade IS NULL OR daekningsomraade ilike '%' || :daekningsomraade || '%')
      """)
  @RegisterRowMapper(DaekningsomraadeMapper.class)
  List<DaekningsomraadeDto> getDaekningsomraade(String daekningsomraade);
}

