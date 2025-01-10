package dk.dataforsyningen.arkivmeta.kort.dao;

import dk.dataforsyningen.arkivmeta.kort.apimapper.KortDtoMapper;
import dk.dataforsyningen.arkivmeta.kort.apimodel.KortDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.locationtech.jts.geom.Geometry;

// For printing sql statements
//@LogSqlFactory
public interface IKortDao {

  /**
   * @RegisterRowMapper use the registered mapper to map the select columns from the database to KortDto
   * https://jdbi.org/#_registerrowmapper
   */
  @SqlQuery("""
      SELECT
          id,
          kortgruppe,
          titel,
          alternativtitel,
          originalkortprojektion,
          bemaerkning,
          gaeldendeperiode_gaeldendefra,
          gaeldendeperiode_gaeldendetil,
          ST_AsEWKT(geometri) AS geometri,
          maalestok,
          kortbladnummer,
          kortvaerk,
          daekningsomraade,
          filer,
          aarfordata,
          aarforenkeltrettelser,
          aarforfotografering,
          aarforfotogrametriskudtegning,
          aarforhenlaeggelse,
          aarforkompleteteretimarken,
          aarforkortproeve,
          aarforlineaerrettelse,
          aarformaalt,
          aarforopmaalingsluttet,
          aarforpunktgrundlag,
          aarforrettelse,
          aarfortopografi,
          aarforudarbejdelse,
          aarforudarbejdetmateriale,
          aarforudgivelse,
          aarforudskiftning,
          aarforudtegning,
          aarforvejdata,
          daasenummer,
          farveskalatype,
          flyvehoejde,
          flyverute,
          fotonummer,
          fototid,
          fotovinkel,
          kameraid,
          kortart,
          opmaaltaf,
          plannr,
          producent,
          tegner,
          udgiver,
          udskiftetaf,
          "version"
      FROM
          historiskekort.historiskekort
      WHERE
          id = :id
      """)
  @RegisterRowMapper(KortDtoMapper.class)
  Optional<KortDto> getKortById(@Bind("id") UUID id);


  /**
   * To @BindList we need to use < instead of :
   * https://jdbi.org/#_binding_arguments_2
   *
   * @RegisterRowMapper use the registered mapper to map the select columns from the database to KortDto
   * https://jdbi.org/#_registerrowmapper
   */
  @SqlQuery("""
      SELECT
          id,
          kortgruppe,
          titel,
          alternativtitel,
          originalkortprojektion,
          bemaerkning,
          gaeldendeperiode_gaeldendefra,
          gaeldendeperiode_gaeldendetil,
          ST_AsEWKT(geometri) AS geometri,
          maalestok,
          kortbladnummer,
          kortvaerk,
          daekningsomraade,
          filer,
          aarfordata,
          aarforenkeltrettelser,
          aarforfotografering,
          aarforfotogrametriskudtegning,
          aarforhenlaeggelse,
          aarforkompleteteretimarken,
          aarforkortproeve,
          aarforlineaerrettelse,
          aarformaalt,
          aarforopmaalingsluttet,
          aarforpunktgrundlag,
          aarforrettelse,
          aarfortopografi,
          aarforudarbejdelse,
          aarforudarbejdetmateriale,
          aarforudgivelse,
          aarforudskiftning,
          aarforudtegning,
          aarforvejdata,
          daasenummer,
          farveskalatype,
          flyvehoejde,
          flyverute,
          fotonummer,
          fototid,
          fotovinkel,
          kameraid,
          kortart,
          opmaaltaf,
          plannr,
          producent,
          tegner,
          udgiver,
          udskiftetaf,
          "version"
      FROM
          historiskekort.historiskekort
      WHERE
          ((<kortgruppe>) IS NULL
              OR kortgruppe IN (<kortgruppe>))
          AND (:daekningsomraade IS NULL
              -- How to use SIMILAR TO instead of like, when the parameter value can be a list
              -- https://stackoverflow.com/questions/4928054/postgresql-wildcard-like-for-any-of-a-list-of-words
              OR lower(daekningsomraade::TEXT) SIMILAR TO lower('%(' || :daekningsomraade || ')%'))
          AND (:fritekstsoegning IS NULL
              OR fritekstsoegning @@ plainto_tsquery('simple', :fritekstsoegning))
          AND
              CASE
                  -- If 'fra' is defined and 'til' is defined assume user want everything in between, and what is overlapping with 'til's
                  WHEN (:gaeldendeperiode_gaeldendefra IS NOT NULL AND :gaeldendeperiode_gaeldendetil IS NOT NULL)
                      THEN (gaeldendeperiode_gaeldendetil >= :gaeldendeperiode_gaeldendefra AND gaeldendeperiode_gaeldendefra <= :gaeldendeperiode_gaeldendetil)
                  -- If 'fra' is defined assume user want everything after the 'fra' year
                  WHEN (:gaeldendeperiode_gaeldendefra IS NOT NULL)
                      THEN (gaeldendeperiode_gaeldendetil >= :gaeldendeperiode_gaeldendefra)
                  -- If 'til' is defined assume user want everything before the 'til' year
                  WHEN (:gaeldendeperiode_gaeldendetil IS NOT NULL)
                      THEN (gaeldendeperiode_gaeldendefra <= :gaeldendeperiode_gaeldendetil)
                  -- If the user did not define a year
                  ELSE
                      1 = 1
              END
          AND (:area IS NULL
              OR ST_Intersects(geometri,
              ST_SetSRID(CAST(:area AS geometry),
              4326)))
          AND (:kortbladnummer IS NULL
              OR kortbladnummer ILIKE '%' || :kortbladnummer || '%')
          AND ((<kortvaerk>)IS NULL
              OR kortvaerk IN (<kortvaerk>))
          AND ((<maalestok>) IS NULL
              OR maalestok IN (<maalestok>))
          AND (:tegner IS NULL
              OR tegner ILIKE :tegner)
          AND (:titel IS NULL
              OR titel ILIKE :titel)
      ORDER BY
          -- Sql statements can not take user values and use them as column name. So we need to make
          -- a match with a CASE to map the user value to the correct column name.
          -- We also need to split ASC and DESC because it is SQL feature and can not be a given
          -- user value
          CASE
              WHEN (:direction = 'asc' AND :sort = 'kortgruppe') THEN kortgruppe
              -- We don't know why we need to cast gaeldendeperiode_gaeldendefra and
              -- gaeldendeperiode_gaeldendetil to varchar when it is the column name we are interested in
              WHEN (:direction = 'asc' AND :sort = 'gaeldendeperiode_gaeldendefra') THEN gaeldendeperiode_gaeldendefra::varchar
              WHEN (:direction = 'asc' AND :sort = 'gaeldendeperiode_gaeldendetil') THEN gaeldendeperiode_gaeldendetil::varchar
              WHEN (:direction = 'asc' AND :sort = 'gaeldendeperiode_gaeldendefra') THEN gaeldendeperiode_gaeldendefra::varchar
              WHEN (:direction = 'asc' AND :sort = 'kortvaerk') THEN kortvaerk
              WHEN (:direction = 'asc' AND :sort = 'maalestok') THEN maalestok
              WHEN (:direction = 'asc' AND :sort = 'titel') THEN titel
          END ASC,
          CASE
              WHEN (:direction = 'desc' AND :sort = 'kortgruppe') THEN kortgruppe
              -- We don't know why we need to cast gaeldendeperiode_gaeldendefra and
              -- gaeldendeperiode_gaeldendetil to varchar when it is the column name we are interested in
              WHEN (:direction = 'desc' AND :sort = 'gaeldendeperiode_gaeldendefra') THEN gaeldendeperiode_gaeldendefra::varchar
              WHEN (:direction = 'desc' AND :sort = 'gaeldendeperiode_gaeldendetil') THEN gaeldendeperiode_gaeldendetil::varchar
              WHEN (:direction = 'desc' AND :sort = 'gaeldendeperiode_gaeldendefra') THEN gaeldendeperiode_gaeldendefra::varchar
              WHEN (:direction = 'desc' AND :sort = 'kortvaerk') THEN kortvaerk
              WHEN (:direction = 'desc' AND :sort = 'maalestok') THEN maalestok
              WHEN (:direction = 'desc' AND :sort = 'titel') THEN titel
          END DESC,
          CASE
              WHEN :fritekstsoegning IS NOT NULL THEN ts_rank(fritekstsoegning, plainto_tsquery('simple', :fritekstsoegning))
          END ASC,
          -- There should always be an order by on id for consistent result because we have limit
          -- and offset
          CASE
              WHEN :direction = 'asc' THEN id
          END ASC,
          CASE
              WHEN :direction = 'desc' THEN id
          END DESC
      LIMIT :limit
      OFFSET :offset
      """)
  @RegisterRowMapper(KortDtoMapper.class)
  List<KortDto> getAllKort(
      // We need to be able to pass an empty list to the sql, but JDBI as default throws an exception
      // So we need to change @BindList emptyhandling to give a String with "null"
      // https://github.com/jdbi/jdbi/issues/1131
      // https://jdbi.org/apidocs/org/jdbi/v3/sqlobject/customizer/BindList.EmptyHandling.html#NULL_STRING
      @BindList(value = "kortgruppe", onEmpty = BindList.EmptyHandling.NULL_STRING)
          List<String> kortgruppe,
      @Bind("daekningsomraade") String daekningsomraade,
      @Bind("fritekstsoegning") String fritekstsoegning,
      @Bind("gaeldendeperiode_gaeldendefra") Integer gaeldendeperiode_gaeldendefra, @Bind("gaeldendeperiode_gaeldendetil") Integer gaeldendeperiode_gaeldendetil,
      @Bind("area") Geometry area, @Bind("kortbladnummer") String kortbladnummer,
      @BindList(value = "kortvaerk", onEmpty = BindList.EmptyHandling.NULL_STRING)
      List<String> kortvaerk,
      @BindList(value = "maalestok", onEmpty = BindList.EmptyHandling.NULL_STRING)
          List<String> maalestok,
      @Bind("tegner") String tegner,
      @Bind("titel") String titel,
      @Bind("limit") int limit,
      @Bind("offset") int offset,
      @Bind("sort") String sort,
      @Bind("direction") String direction);

  /**
   * To @BindList we need to use < instead of :
   * https://jdbi.org/#_binding_arguments_2
   */
  @SqlQuery("""
      SELECT
        COUNT(*)
      FROM
          historiskekort.historiskekort
      WHERE
          ((<kortgruppe>) IS NULL
              OR kortgruppe IN (<kortgruppe>))
          AND (:daekningsomraade IS NULL
              -- How to use SIMILAR TO instead of like, when the parameter value can be a list
              -- https://stackoverflow.com/questions/4928054/postgresql-wildcard-like-for-any-of-a-list-of-words
              OR lower(daekningsomraade::TEXT) SIMILAR TO lower('%(' || :daekningsomraade || ')%'))
          AND (:fritekstsoegning IS NULL
              OR fritekstsoegning @@ plainto_tsquery('simple', :fritekstsoegning))
          AND
              CASE
                  -- If 'fra' is defined and 'til' is defined assume user want everything in between, and what is overlapping with 'til's
                  WHEN (:gaeldendeperiode_gaeldendefra IS NOT NULL AND :gaeldendeperiode_gaeldendetil IS NOT NULL)
                      THEN (gaeldendeperiode_gaeldendetil >= :gaeldendeperiode_gaeldendefra AND gaeldendeperiode_gaeldendefra <= :gaeldendeperiode_gaeldendetil)
                  -- If 'fra' is defined assume user want everything after the 'fra' year
                  WHEN (:gaeldendeperiode_gaeldendefra IS NOT NULL)
                      THEN (gaeldendeperiode_gaeldendetil >= :gaeldendeperiode_gaeldendefra)
                  -- If 'til' is defined assume user want everything before the 'til' year
                  WHEN (:gaeldendeperiode_gaeldendetil IS NOT NULL)
                      THEN (gaeldendeperiode_gaeldendefra <= :gaeldendeperiode_gaeldendetil)
                  -- If the user did not define a year
                  ELSE
                      1 = 1
              END
          AND (:area IS NULL
              OR ST_Intersects(geometri,
              ST_SetSRID(CAST(:area AS geometry),
              4326)))
          AND (:kortbladnummer IS NULL
              OR kortbladnummer ILIKE '%' || :kortbladnummer || '%')
          AND ((<kortvaerk>) IS NULL
              OR kortvaerk IN (<kortvaerk>))
          AND ((<maalestok>) IS NULL
              OR maalestok IN (<maalestok>))
          AND (:tegner IS NULL
              OR tegner ILIKE :tegner)
          AND (:titel IS NULL
              OR titel ILIKE :titel)
      """)
  Long getCount(
      // We need to be able to pass an empty list to the sql, but JDBI as default throws an exception
      // So we need to change @BindList emptyhandling to give a String with "null"
      // https://github.com/jdbi/jdbi/issues/1131
      // https://jdbi.org/apidocs/org/jdbi/v3/sqlobject/customizer/BindList.EmptyHandling.html#NULL_STRING
      @BindList(value = "kortgruppe", onEmpty = BindList.EmptyHandling.NULL_STRING)
          List<String> kortgruppe,
      @Bind("daekningsomraade") String daekningsomraade,
      @Bind("fritekstsoegning") String fritekstsoegning,
      @Bind("gaeldendeperiode_gaeldendefra") Integer gaeldendeperiode_gaeldendefra, @Bind("gaeldendeperiode_gaeldendetil") Integer gaeldendeperiode_gaeldendetil,
      @Bind("area") Geometry area, @Bind("kortbladnummer") String kortbladnummer,
      @BindList(value = "kortvaerk", onEmpty = BindList.EmptyHandling.NULL_STRING)
      List<String> kortvaerk,
      @BindList(value = "maalestok", onEmpty = BindList.EmptyHandling.NULL_STRING)
          List<String> maalestok,
      @Bind("tegner") String tegner,
      @Bind("titel") String titel);
}