package dk.dataforsyningen.arkivmeta.protokol.apimapper;

import dk.dataforsyningen.arkivmeta.enums.Dokumentsamling;
import dk.dataforsyningen.arkivmeta.protokol.apimapper.MapperDaekningsomraade;
import dk.dataforsyningen.arkivmeta.kort.apimodel.AeldretopografiskekortDto;
import dk.dataforsyningen.arkivmeta.kort.apimodel.KortDto;
import dk.dataforsyningen.arkivmeta.mapperhelper.MapperFiler;
import dk.dataforsyningen.arkivmeta.mapperhelper.MapperGeometri;
import dk.dataforsyningen.arkivmeta.protokol.apimodel.ProtokolDto;
import dk.dataforsyningen.arkivmeta.protokol.apimodel.ArbejdsjournalDto;
import dk.dataforsyningen.arkivmeta.protokol.apimodel.HartkornsEkstraktDto;
import dk.dataforsyningen.arkivmeta.protokol.apimodel.KortfortegnelseDto;
import dk.dataforsyningen.arkivmeta.protokol.apimodel.SogneprotokolDto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;

public class ProtokolDtoMapper implements RowMapper<ProtokolDto> {

    /**
     * The mapper must have a default constructor https://jdbi.org/#_registerrowmapper
     */
    public ProtokolDtoMapper() {
    }

    @Override
    public ProtokolDto map(ResultSet rs, StatementContext ctx) throws SQLException {
        String dokumentsamling = rs.getString("dokumentsamling").toUpperCase();

        Dokumentsamling type = Dokumentsamling.valueOf(dokumentsamling);

        if (type == Dokumentsamling.ARBEJDSJOURNALER) {
            return mapArbejdsjournalDto(rs, ctx);
        }
        if (type == Dokumentsamling.HARTKORNSEKSTRAKTER) {
            return mapHartkornsEkstraktDto(rs, ctx);
        }
        if (type == Dokumentsamling.KORTFORTEGNELSER) {
            return mapKortfortegnelseDto(rs, ctx);
        }
        if (type == Dokumentsamling.SOGNEPROTOKOLLER) {
            return mapSogneprotokolDto(rs, ctx);
        }
        throw new IllegalStateException("Could not resolve mapping strategy for object");
    }

    public <T extends ProtokolDto> T mapProtokolDto(ResultSet rs, StatementContext ctx, T dto)
            throws SQLException {
        MapperDaekningsomraade mapperDaekningsomraade = new MapperDaekningsomraade();
        MapperFiler mapperFiler = new MapperFiler();
        dto.setArketype(rs.getString("arketype"));

        dto.setId(rs.getString("id"));
        dto.setArketype(rs.getString("arketype"));
        dto.setTitel(rs.getString("titel"));
        dto.setRegistreringfra(rs.getTimestamp("registreringfra").toLocalDateTime());
        if (rs.getTimestamp("registreringtil") != null) {
            dto.setRegistreringtil(rs.getTimestamp("registreringtil").toLocalDateTime());
        } else {
            dto.setRegistreringtil(null);
        }
        dto.setUniktdokumentnavn(rs.getString("uniktdokumentnavn"));
        dto.setStinavn(rs.getString("stinavn"));
        dto.setFiler(mapperFiler.mapFiler(rs.getString("filer")));
        dto.setDokumentsamling(rs.getString("dokumentsamling"));
        return dto;
    }

    public ArbejdsjournalDto mapArbejdsjournalDto(ResultSet rs, StatementContext ctx)
            throws SQLException {
        // Mapper db og giver en ny instans med af ArbejdsjournalDto
        ArbejdsjournalDto dto = mapProtokolDto(rs, ctx, new ArbejdsjournalDto());

        // ArbejdsjournalDto har nogle felter der skal sættes ud over hvad ProtokolDto har
        dto.setBemaerkning(rs.getString("bemaerkning"));
        dto.setDatatype(rs.getString("datatype"));
        dto.setFiltype(rs.getString("filtype"));

        return dto;
    }

    public HartkornsEkstraktDto mapHartkornsEkstraktDto(ResultSet rs, StatementContext ctx)
            throws SQLException {
        // Mapper db og giver en ny instans med af AeldretopografiskekortDto
        HartkornsEkstraktDto dto = mapProtokolDto(rs, ctx, new HartkornsEkstraktDto());
        MapperGeometri mapperGeometri = new MapperGeometri();

        // HartkornsEkstraktDto har nogle felter der skal sættes ud over hvad ProtokolDto har
        if (rs.getString("geometri") != null) {
            // Geometri in database is the database geomtry, but JDBI does not have map/converter for
            // that datatype, so we fetch it as a String, convert it to datatype Geometry
            byte[] bytes = hexStringToByteArray(rs.getString("geometri"));

            Geometry geometry = (deserialize(bytes));

            // Then we take the geomtry and convert it to String as WKT formattet geomtry
            dto.setGeometri(mapperGeometri.mapGeometri(geometry));
        } else {
            dto.setGeometri(mapperGeometri.mapGeometri(null));
        }

        dto.setHerredsnavn(rs.getString("herredsnavn"));
        dto.setProtokoltype(rs.getString("protokoltype"));
        return dto;
    }

    public KortfortegnelseDto mapKortfortegnelseDto(ResultSet rs, StatementContext ctx)
            throws SQLException {
        // Mapper db og giver en ny instans med af AeldretopografiskekortDto
        KortfortegnelseDto dto = mapProtokolDto(rs, ctx, new KortfortegnelseDto());

        // KortfortegnelseDto har nogle felter der skal sættes ud over hvad protokolDto har
        dto.setDatatype(rs.getString("datatype"));
        dto.setFiltype(rs.getString("filtype"));

        return dto;
    }

    public SogneprotokolDto mapSogneprotokolDto(ResultSet rs, StatementContext ctx)
            throws SQLException {
        // Mapper db og giver en ny instans med af AeldretopografiskekortDto
        SogneprotokolDto dto = mapProtokolDto(rs, ctx, new SogneprotokolDto());
        MapperGeometri mapperGeometri = new MapperGeometri();

        // SogneprotokolDto har nogle felter der skal sættes ud over hvad KortDto har
        dto.setAlternativtitel(rs.getString("alternativtitel"));
        if (rs.getString("geometri") != null) {
            // Geometri in database is the database geomtry, but JDBI does not have map/converter for
            // that datatype, so we fetch it as a String, convert it to datatype Geometry
            byte[] bytes = hexStringToByteArray(rs.getString("geometri"));

            Geometry geometry = (deserialize(bytes));

            // Then we take the geomtry and convert it to String as WKT formattet geomtry
            dto.setGeometri(mapperGeometri.mapGeometri(geometry));
        } else {
            dto.setGeometri(mapperGeometri.mapGeometri(null));
        }
        dto.setHerredsnavn(rs.getString("herredsnavn"));
        dto.setProtokoltype(rs.getString("protokoltype"));
        dto.setSognenavn(rs.getString("sognenavn"));
        dto.setSogneid(rs.getBigDecimal("sogneid"));

        return dto;
    }

    private static Geometry deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            WKBReader reader = new WKBReader(new GeometryFactory());
            return reader.read(bytes);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] =
                    (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

}