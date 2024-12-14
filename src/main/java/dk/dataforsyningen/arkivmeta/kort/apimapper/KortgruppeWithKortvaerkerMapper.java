package dk.dataforsyningen.arkivmeta.kort.apimapper;

import dk.dataforsyningen.arkivmeta.kort.apimodel.KortgruppeWithKortvaerkerDto;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class KortgruppeWithKortvaerkerMapper implements RowMapper<KortgruppeWithKortvaerkerDto> {

  /**
   * The mapper must have a default constructor https://jdbi.org/#_registerrowmapper
   */
  public KortgruppeWithKortvaerkerMapper() {
  }

  @Override
  public KortgruppeWithKortvaerkerDto map(ResultSet rs, StatementContext ctx) throws SQLException {
    KortgruppeWithKortvaerkerDto dto = new KortgruppeWithKortvaerkerDto();
    dto.setKortgruppe(rs.getString("kortgruppe"));
    Array sqlArrayKortvaerker = rs.getArray("kortvaerk");
    String[] arrayKortvaerker = (String[]) sqlArrayKortvaerker.getArray();

    // Until there is no NULL values in the database we use Arrays.asList
    //dto.setKortvaerker(List.of(arrayKortvaerker));
    dto.setKortvaerker(Arrays.asList(arrayKortvaerker));

    return dto;
  }
}