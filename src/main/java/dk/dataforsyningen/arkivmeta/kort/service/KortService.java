package dk.dataforsyningen.arkivmeta.kort.service;

import dk.dataforsyningen.arkivmeta.kort.apimodel.Kortvaerk;
import dk.dataforsyningen.arkivmeta.kort.apimodel.KortgruppeWithKortvaerkerDto;
import dk.dataforsyningen.arkivmeta.kort.apimodel.DaekningsomraadeDto;
import dk.dataforsyningen.arkivmeta.kort.apimodel.KortDto;
import dk.dataforsyningen.arkivmeta.kort.apimodel.KortParam;
import dk.dataforsyningen.arkivmeta.kort.apimodel.KortResult;
import dk.dataforsyningen.arkivmeta.kort.apimodel.MaalestokDto;
import dk.dataforsyningen.arkivmeta.kort.dao.IKortgruppeWithKortvaerkerDao;
import dk.dataforsyningen.arkivmeta.kort.dao.IDaekningsomraadeDao;
import dk.dataforsyningen.arkivmeta.kort.dao.IKortDao;
import dk.dataforsyningen.arkivmeta.kort.dao.IMaalestokDao;
import dk.dataforsyningen.arkivmeta.kort.rest.KortApi;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class KortService implements IKortService {
  private static final Logger logger = LoggerFactory.getLogger(KortApi.class);
  private final IKortgruppeWithKortvaerkerDao iKortgruppeWithKortvaerkerDao;
  private final IDaekningsomraadeDao iDaekningsomraadeDao;
  private final IKortDao iKortDao;
  private final IMaalestokDao iMaalestokDao;

  /**
   * Constructor injection
   * We do not have to specify @Autowired, as long as the class only have one constructor and the class itself
   * is annotated with a Spring bean, because Spring automatic make the dependency to be injected via the constructor.
   * It is used here just for readability
   * <p>
   * To understand how constructor injection works:
   * https://stackoverflow.com/questions/40620000/spring-autowire-on-properties-vs-constructor
   * https://reflectoring.io/constructor-injection/
   */
  public KortService(@Qualifier("kortgruppeWithKortvaerkerDao")
                     IKortgruppeWithKortvaerkerDao iKortgruppeWithKortvaerkerDao,
                     @Qualifier("daekningsomraadeDao") IDaekningsomraadeDao iDaekningsomraadeDao,
                     @Qualifier("kortDao") IKortDao iKortDao,
                     @Qualifier("maalestokDao") IMaalestokDao iMaalestokDao) {
    this.iKortgruppeWithKortvaerkerDao = iKortgruppeWithKortvaerkerDao;
    this.iDaekningsomraadeDao = iDaekningsomraadeDao;
    this.iKortDao = iKortDao;
    this.iMaalestokDao = iMaalestokDao;
  }

  /**
   * @return list of KortgruppeDto with all kortvaerker belonging to each korgruppe
   */
  @Override
  @Cacheable("kortgrupper")
  public List<KortgruppeWithKortvaerkerDto> getKortgrupperWithKortvaerker() {
    return iKortgruppeWithKortvaerkerDao.getAllKortgrupperWithKortvaerker();
  }

  /**
   * Kort's id in the database consists of it's arketype and id. There can be given incorrect arketype and/or id, so
   * there is used Optional to take care if the result is null.
   * Because Gravitee does not set X-Forwarded-* headers it's needed to manually set them.
   * Need to replace the url return from concatXFHeaders and add /iiif/3 to the url because of Cantaloupe
   *
   * @param arketype
   * @param id
   * @return kortDto with the matching datamodel of the kort
   */
  @Override
  public KortDto getKortById(String arketype, String id) {
    String searchId = arketype + "/" + id;

    Optional<KortDto> returnedKort = iKortDao.getKortById(searchId);

    return returnedKort.orElseThrow(
        () -> new NoSuchElementException("Intet kort matchede det givne id: " + searchId));
  }

  /**
   * Returns the list of DaekningsomraadeDto starting with the given search criteria and ignorering the case
   *
   * @return list of DaekningsomraadeDto of all daekningsomraader available
   */
  @Override
  @Cacheable("daekningsomraader")
  public List<DaekningsomraadeDto> getDaekningsomraader(String daekningsomraade) {
    List<DaekningsomraadeDto> daekningsomraadeDtoList = iDaekningsomraadeDao
        .getDaekningsomraade(daekningsomraade);
    return daekningsomraadeDtoList;
  }

  /**
   * Returns the list of MaalestokDto starting with the given search criteria and ignorering the case
   *
   * @return list of MaalestokDto of all maalestok available
   */
  @Override
  @Cacheable("maalestokke")
  public List<MaalestokDto> getMaalestokke(String maalestok) {
    List<MaalestokDto> maalestokDtoList =
        iMaalestokDao.getMaalestok(maalestok);
    return maalestokDtoList;
  }

  /**
   *
   * @param kortParam
   * @param kortvaerk
   * @return the object with a list of kort that matched users requirements (up to 1000) and a total of how many
   * match result there was in total
   */
  @Cacheable(cacheNames = "kort", key = "#kortParam")
  public KortResult getKortResult(KortParam kortParam, Kortvaerk kortvaerk) {

    Geometry area = new GeometryFactory().createGeometry(null);
    if (StringUtils.isNotBlank(kortParam.getGeometri())) {
      try {
        area = new WKTReader().read(kortParam.getGeometri());
      } catch (ParseException parseException) {
        throw new IllegalArgumentException(
            "Could not read geometry. Should be a WKT with SRS = EPSG:4326", parseException);
      }
    }

    // For using SIMILAR TO in sql
    String daekningsomraade = StringUtils.join(kortParam.getDaekningsomraade(), "|");
    System.out.println(kortvaerk);

    List<KortDto> kortDtoList = iKortDao.getAllKort(
        kortParam.getArketype(), daekningsomraade, kortParam.getFritekstsoegning(),
        kortParam.getGaeldendefra(), kortParam.getGaeldendetil(), area,
        kortParam.getKortbladnummer(), kortvaerk.toList(), kortParam.getMaalestok(), kortParam.getTegner(),
        kortParam.getTitel(), kortParam.getLimit(), kortParam.getOffset(), kortParam.getSort(),
        kortParam.getDirection());

    long count;

    if (kortDtoList.size() >= kortParam.getLimit()) {
      count = iKortDao.getCount(kortParam.getArketype(), daekningsomraade,
          kortParam.getFritekstsoegning(), kortParam.getGaeldendefra(), kortParam.getGaeldendetil(),
          area, kortParam.getKortbladnummer(), kortvaerk.toList(), kortParam.getMaalestok(),
          kortParam.getTegner(), kortParam.getTitel());
    } else {
      count = kortDtoList.size();
    }

    KortResult kortresult = new KortResult(count, kortDtoList);
    return kortresult;
  }
}
