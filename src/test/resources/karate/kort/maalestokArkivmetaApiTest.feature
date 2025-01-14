Feature: Arkivmeta API Integration Test

  Background:
    * url url

  Scenario: Arkiv API /metadata/maalestok - get all maalestok

    Given path '/metadata/maalestok'
    When method get
    Then status 200
    
    # should be an array of strings with size 100
    # https://karatelabs.github.io/karate/#schema-validation
    And match response == '#[68] #string'

  Scenario: Arkiv API /metadata/maalestok - search containing maalestok

    Given path '/metadata/maalestok'
    And param maalestok = '1:5500'
    When method get
    Then status 200
    
    # should be an array of strings with size 3
    # https://karatelabs.github.io/karate/#schema-validation
    And match response == '#[1] #string'
    And match response == ["1:5500"]