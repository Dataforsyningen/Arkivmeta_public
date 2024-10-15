Feature: Historiske Dokumenter API Integration Test

  Background:
    * url url


  Scenario: GET - Returns the 100 first json objects of all historiske dokumenter

    Given path '/dokument'
    When method get
    Then status 200
    And match response.dokumenter == '#[100]'


  Scenario: GET - Search not existing sognenavn

    Given path '/dokument'
    And param sognenavn = 'findes ikke'
    When method get
    Then status 200
    And match response.total == 0

  Scenario: POST - Search not existing sognenavn

    Given path '/dokument'
    And request
    """
    {
        "sognenavn": "findes ikke"
    }
    """
    When method post
    Then status 200
    And match response.total == 0

  Scenario: GET - Search sognenavn insensitive

    Given path '/dokument'
    And param sognenavn = 'dronninglund'
    When method get
    Then status 200
    # match the response with the keys from the json objects
    And match response == { total: '#present', dokumenter: '#present' }
    # assigning response to lower
    Then def lower = response

    Given path '/dokument'
    And param sognenavn = 'DRONNINGLUND'
    When method get
    Then status 200
    # match the new response match with the variable lower
    And match response == lower

  Scenario: POST - Search sognenavn insensitive

    Given path '/dokument'
    And request
    """
    {
      "sognenavn": "dronninglund"
    }
    """
    When method post
    Then status 200
    # match the response with the keys from the json objects
    And match response == { total: '#present', dokumenter: '#present' }
    # assigning response to lower
    Then def lower = response

    Given path '/dokument'
    And request
    """
    {
      "sognenavn": "DRONNINGLUND"
    }
    """
    When method post
    Then status 200
    # match the new response match with the variable lower
    And match response == lower

  Scenario: GET - Search herredsnummer

    Given path '/dokument'
    And param herredsnummer = 1
    When method get
    Then status 200
    # match the response with the keys from the json objects
    And match response == { total: '#present', dokumenter: '#present' }

  Scenario: POST - Search herredsnummer

    Given path '/dokument'
    And request
    """
    {
      "herredsnummer": 1
    }
    """
    When method post
    Then status 200
    # match the response with the keys from the json objects
    And match response == { total: '#present', dokumenter: '#present' }

  Scenario: GET - Search fritekstsoegning

    Given path '/dokument'
    And param fritekstsoegning = "vester"
    When method get
    Then status 200
    # match the response with the keys from the json objects
    And match response == { total: '#present', dokumenter: '#present' }

  Scenario: POST - Search fritekstsoegning

    Given path '/dokument'
    And request
    """
    {
      "fritekstsoegning": "vester"
    }
    """
    When method post
    Then status 200
    # match the response with the keys from the json objects
    And match response == { total: '#present', dokumenter: '#present' }


  Scenario: Search mulitple dokumentsamling

    Given path '/dokument'
    And param dokumentsamling = 'sogneprotokoller,hartkornsekstrakter'
    When method get
    Then status 200
    # match the response with the keys from the json objects
    And match response == { total: '#present', dokumenter: '#present' }
    # assigning response.total to firstDokumentsamlingTotal
    Then def firstDokumentsamlingTotal = response.total

    Given path '/dokument'
    # Use the delimiter way right now, because switchboards does not understand string arrays
    And param dokumentsamling = ['sogneprotokoller', 'hartkornsekstrakter']
    When method get
    Then status 200
    # match the response with the keys from the json objects
    And match response == { total: '#present', dokumenter: '#present' }
    Then match response.total == firstDokumentsamlingTotal

  Scenario: Search with geometry

    Given path '/dokument'
    And param geometri = 'POINT(12.226727129244841 55.86164621853605)'
    When method get
    Then status 200
    And match response.dokumenter == '#[4]'

  Scenario: GET - Limit -1

    Given path '/dokument'
    And param limit = -1
    When method get
    Then status 422
    And match response ==
    """
    {
      "status": "UNPROCESSABLE_ENTITY",
      "message":null,
      "errors": ["limit: must be greater than or equal to 1"]
    }
    """

  Scenario: GET - Limit 1000

    Given path '/dokument'
    And param limit = 1001
    When method get
    Then status 422
    And match response ==
    """
    {
        "status": "UNPROCESSABLE_ENTITY",
        "message":null,
        "errors": ["limit: must be less than or equal to 1000"]
    }
    """

  Scenario: POST - Limit -1

    Given path '/dokument'
    And request
    """
    {
      "limit": -1
    }
    """
    When method post
    Then status 422
    And match response ==
    """
    {
      "status": "UNPROCESSABLE_ENTITY",
      "message":null,
      "errors": ["limit: must be greater than or equal to 1"]
    }
    """

  Scenario: POST - Limit 1000

    Given path '/dokument'
    And request
    """
    {
      "limit": 1001
    }
    """
    When method post
    Then status 422
    And match response ==
    """
    {
      "status": "UNPROCESSABLE_ENTITY",
      "message":null,
        "errors": ["limit: must be less than or equal to 1000"]
    }
    """

  Scenario: GET - Offset -1

    Given path '/dokument'
    And param offset = -1
    When method get
    Then status 422
    And match response ==
    """
    {
      "status": "UNPROCESSABLE_ENTITY",
      "message":null,
      "errors": ["offset: must be greater than or equal to 0"]
    }
    """

  Scenario: GET - Offset text

    Given path '/dokument'
    And param offset = 'test'
    When method get
    Then status 422
    And match response ==
    """
    {
      "status": "UNPROCESSABLE_ENTITY",
      "message":null,
        "errors": ["offset: Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; For input string: \"test\""]
    }
    """

  Scenario: POST - Offset -1

    Given path '/dokument'
    And request
    """
    {
      "offset": -1
    }
    """
    When method post
    Then status 422
    And match response ==
    """
    {
      "status": "UNPROCESSABLE_ENTITY",
      "message":null,
      "errors": ["offset: must be greater than or equal to 0"]
    }
    """

  Scenario: POST - Offset text

    Given path '/dokument'
    And request
    """
    {
      "offset": 'test'
    }
    """
    When method post
    Then status 422
    And match response ==
    """
    {
        "status": "UNPROCESSABLE_ENTITY",
        "message": "JSON parse error: Cannot deserialize value of type `java.lang.Integer` from String \"test\": not a valid `java.lang.Integer` value",
        "errors":["com.fasterxml.jackson.databind.exc.InvalidFormatException: Cannot deserialize value of type `java.lang.Integer` from String \"test\": not a valid `java.lang.Integer` value\n at [Source: REDACTED (`StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION` disabled); line: 1, column: 11] (through reference chain: dk.dataforsyningen.arkivmeta.dokument.apimodel.DokumentParam[\"offset\"])"]
    }
    """

  Scenario: GET - direction casesensitive

    Given path '/dokument'
    And param direction = 'ASC'
    When method get
    Then status 422
    And match response ==
    """
    {
      "status": "UNPROCESSABLE_ENTITY",
      "message":null,
      "errors": ["direction: must match \"asc|desc\""]
    }
    """

  Scenario: POST - direction casesensitive

    Given path '/dokument'
    And header Accept = 'application/json'
    And request { direction: 'ASC' }
    When method post
    Then status 422
    And match response ==
    """
    {
      "status": "UNPROCESSABLE_ENTITY",
      "message":null,
      "errors": ["direction: must match \"asc|desc\""]
    }
    """
