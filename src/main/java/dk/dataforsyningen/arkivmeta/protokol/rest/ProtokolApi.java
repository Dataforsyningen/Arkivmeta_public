package dk.dataforsyningen.arkivmeta.protokol.rest;

import dk.dataforsyningen.arkivmeta.protokol.apimodel.ProtokolParam;
import dk.dataforsyningen.arkivmeta.protokol.apimodel.ProtokolResult;
import dk.dataforsyningen.arkivmeta.protokol.apimodel.ProtokolDto;
import dk.dataforsyningen.arkivmeta.protokol.service.IProtokolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ProtokolApi", description = "Protokol metadata API")
@RestController
@Validated
public class ProtokolApi {
  private final IProtokolService iProtokolService;

  public ProtokolApi(IProtokolService iProtokolService) {
    this.iProtokolService = iProtokolService;
  }


  /**
   * @return the method postKort() that handles the request
   */
  @GetMapping(path = "/protokol")
  @Operation(summary = "Liste af protokoller der matcher søgekriterierne", description = "Disse er parametrerne i ProtokolParam")
  @CrossOrigin
  ResponseEntity<ProtokolResult> getProtokoller(@Valid @ParameterObject ProtokolParam protokolParam) {
    return postProtokol(protokolParam);
  }

  /**
   * Returns all protokols matching search criteria if given any or else returns all kort.
   * Gets the parameters from KortParam that is initialized with data from getprotokoller
   * <p>
   *
   * @param protokolParam
   * @return the protokolresult with count of all protokols matching search criteria and all protokols matching search criteria. If not search criteria given then it returns all protokols and the count
   */
  @PostMapping(path = "/protokol")
  @Operation(summary = "Liste af protokoller der matcher søgekriterierne", description = "Disse er parametrerne i ProtokolParam")
  @CrossOrigin
  ResponseEntity<ProtokolResult> postProtokol(
          @Valid @RequestBody ProtokolParam protokolParam) {
    // For GET and POST direction, limit and offset need a default value, but it should only be set,
    // if the client did not specify them.
    if (StringUtils.isBlank(protokolParam.getDirection())) {
      protokolParam.setDirection("asc");
    }
    if (ObjectUtils.isEmpty(protokolParam.getLimit())) {
      protokolParam.setLimit(100);
    }
    if (ObjectUtils.isEmpty(protokolParam.getOffset())) {
      protokolParam.setOffset(0);
    }
    ProtokolResult protokolresult = iProtokolService.getProtokolResult(protokolParam);

    return new ResponseEntity<>(protokolresult, HttpStatus.OK);
  }

  @GetMapping(path = "/protokol/{arketype}/{id}")
  @Operation(summary = "Find protokol ud fra unik id")
  @CrossOrigin
  public ResponseEntity<ProtokolDto> protokolById(
      @Parameter(description = "arketype") @PathVariable String arketype,
      @Parameter(description = "id") @PathVariable String id) {
    ProtokolDto result = iProtokolService.getProtokolById(arketype, id);

    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}