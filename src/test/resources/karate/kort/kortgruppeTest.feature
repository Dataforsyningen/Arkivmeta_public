Feature: Arkivmeta API Integration Test

  Background:
    * url url

  Scenario: /metadata/kortgrupper/kortvaerker - get all kortvaerker belonging to each korgruppe

    Given path '/metadata/kortgrupper/kortvaerker'
    When method get
    Then status 200
    
    # should be an array of objects with size 10
    # https://karatelabs.github.io/karate/#schema-validation
    And match response == '#[10] #object'
    And match response contains
    """
    {
        "kortgruppe": "#string",
        "kortvaerker": "#array"
    }
    """