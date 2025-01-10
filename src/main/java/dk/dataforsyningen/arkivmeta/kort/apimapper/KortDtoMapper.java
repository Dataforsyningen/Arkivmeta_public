package dk.dataforsyningen.arkivmeta.kort.apimapper;

import dk.dataforsyningen.arkivmeta.kort.apimodel.KortDto;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.UUID;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

// RowMapper is a functional interface, which maps the current row of a JDBC ResultSet to a mapped
// type. Row mappers are invoked once for each row in the result set.
public class KortDtoMapper implements RowMapper<KortDto> {

  /**
   * The mapper must have a default constructor https://jdbi.org/#_registerrowmapper
   */
  public KortDtoMapper() {
  }

  @Override
  public KortDto map(ResultSet rs, StatementContext ctx) throws SQLException {
    KortDto kortDto = new KortDto();
    kortDto.setId(rs.getObject("id", UUID.class));
    kortDto.setKortgruppe(rs.getString("kortgruppe"));
    kortDto.setTitel(rs.getString("titel"));
    kortDto.setAlternativtitel(rs.getString("alternativtitel"));
    kortDto.setOriginalkortprojektion(rs.getString("originalkortprojektion"));
    kortDto.setBemaerkning(rs.getString("bemaerkning"));
    kortDto.setGaeldendeperiode_gaeldendefra(rs.getBigDecimal("gaeldendeperiode_gaeldendefra"));
    kortDto.setGaeldendeperiode_gaeldendetil(rs.getBigDecimal("gaeldendeperiode_gaeldendetil"));
    kortDto.setGeometri(rs.getString("geometri"));
    kortDto.setMaalestok(rs.getString("maalestok"));
    kortDto.setKortbladnummer(rs.getString("kortbladnummer"));
    kortDto.setKortvaerk(rs.getString("kortvaerk"));

    Array sqlArrayDaekningsomraade = rs.getArray("daekningsomraade");
    String[] arrayDaekningsomraade = (String[]) sqlArrayDaekningsomraade.getArray();

    kortDto.setDaekningsomraade(Arrays.asList(arrayDaekningsomraade));

    Array sqlArrayFiler = rs.getArray("filer");
    String[] arrayFiler = (String[]) sqlArrayFiler.getArray();

    kortDto.setFiler(Arrays.asList(arrayFiler));

    kortDto.setAarfordata(rs.getBigDecimal("aarfordata"));

    Array sqlArrayAarforenkeltrettelser = rs.getArray("aarforenkeltrettelser");
    BigDecimal[] arrayAarforenkeltrettelser = (BigDecimal[]) sqlArrayAarforenkeltrettelser.getArray();

    kortDto.setAarforenkeltrettelser(Arrays.asList(arrayAarforenkeltrettelser));

    kortDto.setAarforfotografering(rs.getBigDecimal("aarforfotografering"));
    kortDto.setAarforfotogrametriskudtegning(rs.getBigDecimal("aarforfotogrametriskudtegning"));
    kortDto.setAarforhenlaeggelse(rs.getBigDecimal("aarforhenlaeggelse"));
    kortDto.setAarforkompleteteretimarken(rs.getBigDecimal("aarforkompleteteretimarken"));
    kortDto.setAarforkortproeve(rs.getBigDecimal("aarforkortproeve"));
    kortDto.setAarforlineaerrettelse(rs.getBigDecimal("aarforlineaerrettelse"));
    kortDto.setAarformaalt(rs.getBigDecimal("aarformaalt"));
    kortDto.setAarforopmaalingsluttet(rs.getBigDecimal("aarforopmaalingsluttet"));
    kortDto.setAarforpunktgrundlag(rs.getBigDecimal("aarforpunktgrundlag"));

    Array sqlArrayAarforrettelse = rs.getArray("aarforrettelse");
    BigDecimal[] arrayAarforrettelse = (BigDecimal[]) sqlArrayAarforrettelse.getArray();

    kortDto.setAarforrettelse(Arrays.asList(arrayAarforrettelse));

    kortDto.setAarfortopografi(rs.getBigDecimal("aarfortopografi"));
    kortDto.setAarforudarbejdelse(rs.getBigDecimal("aarforudarbejdelse"));
    kortDto.setAarforudarbejdetmateriale(rs.getBigDecimal("aarforudarbejdetmateriale"));
    kortDto.setAarforudgivelse(rs.getBigDecimal("aarforudgivelse"));
    kortDto.setAarforudskiftning(rs.getBigDecimal("aarforudskiftning"));
    kortDto.setAarforudtegning(rs.getBigDecimal("aarforudtegning"));
    kortDto.setAarforvejdata(rs.getBigDecimal("aarforvejdata"));
    kortDto.setDaasenummer(rs.getString("daasenummer"));
    kortDto.setFarveskalatype(rs.getString("farveskalatype"));
    kortDto.setFlyvehoejde((Double) rs.getObject("flyvehoejde"));
    kortDto.setFlyverute(rs.getString("flyverute"));
    kortDto.setFotonummer(rs.getString("fotonummer"));
    kortDto.setFototid(rs.getObject("fototid", OffsetDateTime.class));
    kortDto.setFotovinkel(rs.getString("fotovinkel"));
    kortDto.setKameraid(rs.getString("kameraid"));
    kortDto.setKortart(rs.getString("kortart"));
    kortDto.setOpmaaltaf(rs.getString("opmaaltaf"));
    kortDto.setPlannr(rs.getString("plannr"));
    kortDto.setProducent(rs.getString("producent"));
    kortDto.setTegner(rs.getString("tegner"));
    kortDto.setUdgiver(rs.getString("udgiver"));
    kortDto.setUdskiftetaf(rs.getString("udskiftetaf"));
    kortDto.setVersion(rs.getString("version"));
    
    return kortDto;
  }
}
