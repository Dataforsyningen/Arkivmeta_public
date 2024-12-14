package dk.dataforsyningen.arkivmeta.dokument.rest;

import dk.dataforsyningen.arkivmeta.dokument.apimodel.DokumentDto;
import dk.dataforsyningen.arkivmeta.dokument.apimodel.DokumentParam;
import dk.dataforsyningen.arkivmeta.dokument.apimodel.DokumentResult;
import dk.dataforsyningen.arkivmeta.dokument.service.IDokumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ProtokolApi", description = "Protokol metadata API")
@RestController
@Validated
public class DokumentApi {
  private final IDokumentService iDokumentService;

  public DokumentApi(IDokumentService iDokumentService) {
    this.iDokumentService = iDokumentService;
  }

  /**
   * @return list of string of all dokumentsamlinger available
   */
  @GetMapping(path = "/metadata/dokumentsamling")
  @Operation(summary = "Hent dokumentsamlinger", description = "Leverer en liste af dokumentsamlinger", responses = {
          @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class)))),
          @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
          @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content(schema = @Schema(hidden = true))) })
  public ResponseEntity<List<String>> getDokumentSamling() {

    List<String> result = iDokumentService.getDokumentSamling();
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  /**
   * @return list of string of all herredsnavne available
   */
  @GetMapping(path = "/metadata/herredsnavn")
  @Operation(summary = "Hent herredsnavne", description = "Leverer en liste af herredsnavne", responses = {
          @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class)))),
          @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
          @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content(schema = @Schema(hidden = true))) })
  public ResponseEntity<List<String>> getHerredsnavn() {

    List<String> result = iDokumentService.getHerredsnavn();
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  /**
   * @return list of string of all sognenavne available
   */
  @GetMapping(path = "/metadata/sognenavn")
  @Operation(summary = "Hent sognenavne", description = "Leverer en liste af sognenavne", responses = {
          @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class)))),
          @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
          @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content(schema = @Schema(hidden = true))) })
  public ResponseEntity<List<String>> getSognenavn() {
    List<String> result = iDokumentService.getSognenavn();
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  /**
   * @return the method postKort() that handles the request
   */
  @GetMapping(path = "/dokument")
  @Operation(summary = "Liste af dokumenter der matcher søgekriterierne", description = "Disse er parametrerne i DokumentParam", responses = {
          @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DokumentResult.class))),
          @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
          @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content(schema = @Schema(hidden = true))) })
  public ResponseEntity<DokumentResult> getDokument(@Valid @ParameterObject DokumentParam dokumentParam) {
    return postDokument(dokumentParam);
  }

  /**
   * Returns all dokuments matching search criteria if given any or else returns all kort.
   * Gets the parameters from KortParam that is initialized with data from getDokument
   * <p>
   *
   * @param dokumentParam
   * @return the dokumentresult with count of all dokuments matching search criteria and all dokuments matching search criteria. If not search criteria given then it returns all dokuments and the count
   */
  @PostMapping(path = "/dokument")
  @Operation(summary = "Liste af dokumenter der matcher søgekriterierne", description = "Disse er parametrerne i DokumentParam", responses = {
          @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DokumentResult.class))),
          @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
          @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content(schema = @Schema(hidden = true))) })
  public ResponseEntity<DokumentResult> postDokument(
          @Valid @RequestBody DokumentParam dokumentParam) {
    // For GET and POST direction, limit and offset need a default value, but it should only be set,
    // if the client did not specify them.
    if (StringUtils.isBlank(dokumentParam.getDirection())) {
      dokumentParam.setDirection("asc");
    }
    if (ObjectUtils.isEmpty(dokumentParam.getLimit())) {
      dokumentParam.setLimit(100);
    }
    if (ObjectUtils.isEmpty(dokumentParam.getOffset())) {
      dokumentParam.setOffset(0);
    }
    DokumentResult dokumentresult = iDokumentService.getDokumentResult(dokumentParam);

    return new ResponseEntity<>(dokumentresult, HttpStatus.OK);
  }

  @GetMapping(path = "/dokument/{arketype}/{id}")
  @Operation(summary = "Find dokument ud fra unik id", responses = {
          @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DokumentDto.class))),
          @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
          @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content(schema = @Schema(hidden = true))) })
  public ResponseEntity<DokumentDto> dokumentById(
          @Parameter(description = "arketype") @PathVariable String arketype,
          @Parameter(description = "id") @PathVariable String id) {
    DokumentDto result = iDokumentService.getDokumentById(arketype, id);

    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
