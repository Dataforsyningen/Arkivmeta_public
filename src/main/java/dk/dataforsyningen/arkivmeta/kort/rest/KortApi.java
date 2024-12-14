package dk.dataforsyningen.arkivmeta.kort.rest;

import dk.dataforsyningen.arkivmeta.kort.apimodel.Kortvaerk;
import dk.dataforsyningen.arkivmeta.kort.apimodel.KortgruppeWithKortvaerkerDto;
import dk.dataforsyningen.arkivmeta.kort.apimodel.DaekningsomraadeDto;
import dk.dataforsyningen.arkivmeta.kort.apimodel.KortDto;
import dk.dataforsyningen.arkivmeta.kort.apimodel.KortParam;
import dk.dataforsyningen.arkivmeta.kort.apimodel.KortResult;
import dk.dataforsyningen.arkivmeta.kort.apimodel.MaalestokDto;
import dk.dataforsyningen.arkivmeta.kort.service.IKortService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "KortApi", description = "Kort metadata API")
@RestController
public class KortApi {
  private final IKortService iKortService;

  public KortApi(IKortService iKortService) {
    this.iKortService = iKortService;
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(String[].class, new StringArrayPropertyEditor(null));
  }

  /**
   * Endpoint to check if the API is up and can answer a simple request
   *
   * @return HTTP response with headers, body, and status with type String with a PONG message
   */
  @GetMapping(path = "/ping", produces = "application/json")
  @Operation(
          summary = "ping",
          description = "Liveliness/readiness probe.",
          tags = {"Liveliness/Readiness"})
  @ApiResponses(
          value = {
                  @ApiResponse(
                          responseCode = "200",
                          description = "Success",
                          content = @Content(schema = @Schema(implementation = String.class)))
          })
  public ResponseEntity<String> ping() {
    return new ResponseEntity<>("{\"message\": \"PONG\"}", HttpStatus.OK);
  }

  /**
   * @return list of KortgruppeDto with all kortvaerker belonging to each korgruppe
   */
  @GetMapping(path = "/metadata/kortgrupper/kortvaerker")
  @Operation(summary = "Hent kortgrupper med underliggende kortværker", description = "Leverer en liste af tilgængelige kortgrupper, med de kortværker som hører til", responses = {
          @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KortgruppeWithKortvaerkerDto.class)))),
          @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
          @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content(schema = @Schema(hidden = true))) })
  public ResponseEntity<List<KortgruppeWithKortvaerkerDto>> kortgrupperWithKortvaerker() {
    List<KortgruppeWithKortvaerkerDto> kortgrupperWithKortvaerkerList = iKortService.getKortgrupperWithKortvaerker();

    return new ResponseEntity<>(kortgrupperWithKortvaerkerList, HttpStatus.OK);
  }

  /**
   * @param daekningsomraade
   * @return list of string of all daekningsomraader available
   */
  @GetMapping(path = "/metadata/daekningsomraader")
  @Operation(summary = "Hent dækningsområder", description = "Leverer en liste af dækningsområder", responses = {
          @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class)))),
          @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
          @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content(schema = @Schema(hidden = true))) })
  public ResponseEntity<List<String>> daekningsomraade(
          @Parameter(description = "Filtrer med søgestreng") @RequestParam(defaultValue = "")
          String daekningsomraade) {
    List<String> daekningsomraadeList = iKortService.getDaekningsomraader(daekningsomraade)
            .stream()
            .map(DaekningsomraadeDto::getDaekningsomraade)
            .collect(Collectors.toList());
    return new ResponseEntity<>(daekningsomraadeList, HttpStatus.OK);
  }

  /**
   * @param maalestok
   * @return list of string of all maalestok available
   */
  @GetMapping(path = "/metadata/maalestok")
  @Operation(summary = "Hent målestoksforhold", description = "Leverer en liste af målestoksforhold", responses = {
          @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class)))),
          @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
          @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content(schema = @Schema(hidden = true))) })
  public ResponseEntity<List<String>> maalestok(
          @Parameter(description = "Filtrer med søgestreng") @RequestParam(defaultValue = "")
          String maalestok) {
    List<String> maalestokList = iKortService.getMaalestokke(maalestok)
            .stream()
            .map(MaalestokDto::getMaalestok)
            .collect(Collectors.toList());
    return new ResponseEntity<>(maalestokList, HttpStatus.OK);
  }

  /**
   * @return the method postKort() that handles the request
   */
  @GetMapping(path = "/kort")
  @Operation(summary = "Liste af kort der matcher søgekriterierne", description = "Hvis gaeldendefra og gaeldendetil bliver brugt samtidig, er det alle kort, der er indenfor gyldighedsperioden eller har været gældende fra eller gældende til, i perioden", responses = {
          @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = KortResult.class))),
          @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
          @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content(schema = @Schema(hidden = true))) })
  public ResponseEntity<KortResult> getKort(@Valid @ParameterObject KortParam kortParam, @Valid @ParameterObject Kortvaerk kortvaerk) {
    System.out.println(kortvaerk.toList().size());
    kortvaerk.toList().forEach(System.out::println);
    return postKort(kortParam, kortvaerk);
  }

  /**
   * Returns all kort matching search criteria if given any or else returns all kort.
   * Gets the parameters from KortParam that is initialized with data from getkort
   * <p>
   *
   * @param kortParam
   * @return the kortresult with count of all kort matching search criteria and all kort matching search criteria. If not search criteria given then it returns all kort and the count
   */
  @PostMapping(path = "/kort")
  @Operation(summary = "Liste af kort der matcher søgekriterierne", description = "Hvis gaeldendefra og gaeldendetil bliver brugt samtidig, er det alle kort, der er indenfor gyldighedsperioden eller har været gældende fra eller gældende til, i perioden", responses = {
          @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = KortResult.class))),
          @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
          @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content(schema = @Schema(hidden = true))) })
  public ResponseEntity<KortResult> postKort(
          @Valid @RequestBody @ParameterObject KortParam kortParam, @Valid @ParameterObject Kortvaerk kortvaerk) {
    // For GET and POST direction, limit and offset need a default value, but it should only be set,
    // if the client did not specify them.
    if (StringUtils.isBlank(kortParam.getDirection())) {
      kortParam.setDirection("asc");
    }
    if (ObjectUtils.isEmpty(kortParam.getLimit())) {
      kortParam.setLimit(100);
    }
    if (ObjectUtils.isEmpty(kortParam.getOffset())) {
      kortParam.setOffset(0);
    }
    System.out.println(kortvaerk.toList().size());
    kortvaerk.toList().forEach(System.out::println);

    KortResult kortresult = iKortService.getKortResult(kortParam, kortvaerk);

    return new ResponseEntity<>(kortresult, HttpStatus.OK);
  }

  /**
   * Returns the kort matching with specified {arketype}/{id} as JSON.
   * <p>
   *
   * @param arketype type of kort
   * @param id       the kort's id
   * @return the kort with the arketype and id specified
   */
  @GetMapping(path = "/kort/{arketype}/{id}")
  @Operation(summary = "Vis kort ud fra unik id", responses = {
          @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = KortDto.class))),
          @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
          @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content(schema = @Schema(hidden = true))) })
  public ResponseEntity<KortDto> kortById(
          @Parameter(description = "arketype") @PathVariable String arketype,
          @Parameter(description = "id") @PathVariable String id) {
    KortDto result = iKortService.getKortById(arketype, id);

    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
