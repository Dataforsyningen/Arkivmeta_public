package dk.dataforsyningen.arkivmeta.kort.apimapper;

import dk.dataforsyningen.arkivmeta.kort.apimodel.DaekningsomraadeDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class DaekningsomraadeMapper implements RowMapper<DaekningsomraadeDto> {
  /**
   * The mapper must have a default constructor https://jdbi.org/#_registerrowmapper
   */
  public DaekningsomraadeMapper() {
  }

  @Override
  public DaekningsomraadeDto map(ResultSet rs, StatementContext ctx) throws SQLException {
    DaekningsomraadeDto dto = new DaekningsomraadeDto(rs.getString("daekningsomraade"));

    return dto;
  }
}