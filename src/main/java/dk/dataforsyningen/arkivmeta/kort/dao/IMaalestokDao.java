package dk.dataforsyningen.arkivmeta.kort.dao;

import dk.dataforsyningen.arkivmeta.kort.apimapper.MaalestokMapper;
import dk.dataforsyningen.arkivmeta.kort.apimodel.MaalestokDto;
import java.util.List;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface IMaalestokDao {

  /**
   * @RegisterRowMapper use the registered mapper to map the select columns from the database to MaalestokDto
   * https://jdbi.org/#_registerrowmapper
   */
  @SqlQuery("""
      SELECT
          maalestok
      FROM
          historiskekort.maalestok
      WHERE
          (:maalestok IS NULL OR maalestok ilike '%' || :maalestok || '%')
      ORDER BY
          maalestok
      """)
  @RegisterRowMapper(MaalestokMapper.class)
  List<MaalestokDto> getMaalestok(String maalestok);
}
