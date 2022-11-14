Feature: Recipes API

  Background:
    Given The database is clean

  Scenario: client makes call to create recipe endpoint with valid headers and body
    When the client call the create recipe endpoint with valid headers and body
    """
    {
        "name": "Pad Thai",
        "numberOfServings": 2,
        "category": "main-course",
        "ingredients": [
            "palm sugar",
            "water",
            "fish sauce",
            "tamarind"
        ],
        "instructions": "1.To make the sauce, melt and caramelize the palm sugar.\\n2.Add water to stop the caramelization.\\n3.Add fish sauce and tamarind and bring to a simmer."
    }
    """
    Then the client receives status code 201 with body
    """
    {
        "id": 1,
        "name": "Pad Thai",
        "numberOfServings": 2,
        "category": "main-course",
        "ingredients": [
            "palm sugar",
            "water",
            "fish sauce",
            "tamarind"
        ],
        "instructions": "1.To make the sauce, melt and caramelize the palm sugar.\\n2.Add water to stop the caramelization.\\n3.Add fish sauce and tamarind and bring to a simmer."
    }
    """

  Scenario: client makes call to create recipe endpoint with invalid request
    When the client call the create recipe endpoint with valid headers and body
    """
    {
        "name": "Pad Thai",
        "numberOfServings": 2,
        "ingredients": [
            "palm sugar",
            "water",
            "fish sauce",
            "tamarind"
        ],
        "instructions": "1.To make the sauce, melt and caramelize the palm sugar.\\n2.Add water to stop the caramelization.\\n3.Add fish sauce and tamarind and bring to a simmer."
    }
    """
    Then the client receives status code 400

  Scenario: client makes call to get a non existent recipe
    When the client call the get recipe endpoint with id 1
    Then the client receives status code 404

  Scenario: client makes call to get an existent recipe
    Given the client call the create recipe endpoint with valid headers and body
    """
    {
        "name": "Pad Thai",
        "numberOfServings": 2,
        "category": "main-course",
        "ingredients": [
            "palm sugar",
            "water",
            "fish sauce",
            "tamarind"
        ],
        "instructions": "1.To make the sauce, melt and caramelize the palm sugar.\\n2.Add water to stop the caramelization.\\n3.Add fish sauce and tamarind and bring to a simmer."
    }
    """
    When the client call the get recipe endpoint with id 1
    Then the client receives status code 200 with body
    """
    {
        "id": 1,
        "name": "Pad Thai",
        "numberOfServings": 2,
        "category": "main-course",
        "ingredients": [
            "palm sugar",
            "water",
            "fish sauce",
            "tamarind"
        ],
        "instructions": "1.To make the sauce, melt and caramelize the palm sugar.\\n2.Add water to stop the caramelization.\\n3.Add fish sauce and tamarind and bring to a simmer."
    }
    """

  Scenario: client makes call to update a non existent recipe
    When the client call the update recipe endpoint with valid headers and body
    """
    {
        "id": 1,
        "name": "Pad Thai",
        "numberOfServings": 2,
        "category": "main-course",
        "ingredients": [
            "palm sugar",
            "water",
            "fish sauce",
            "tamarind"
        ],
        "instructions": "1.To make the sauce, melt and caramelize the palm sugar.\\n2.Add water to stop the caramelization.\\n3.Add fish sauce and tamarind and bring to a simmer."
    }
    """
    Then the client receives status code 404

  Scenario: client makes call to update a recipe with invalid request
    When the client call the update recipe endpoint with valid headers and body
    """
    {
        "id": 1,
        "numberOfServings": 2,
        "category": "main-course",
        "ingredients": [
            "palm sugar",
            "water",
            "fish sauce",
            "tamarind"
        ],
        "instructions": "1.To make the sauce, melt and caramelize the palm sugar.\\n2.Add water to stop the caramelization.\\n3.Add fish sauce and tamarind and bring to a simmer."
    }
    """
    Then the client receives status code 400

  Scenario: client makes call to update an existent recipe
    Given the client call the create recipe endpoint with valid headers and body
    """
    {
        "name": "Pad Thai",
        "numberOfServings": 2,
        "category": "main-course",
        "ingredients": [
            "palm sugar",
            "water",
            "fish sauce",
            "tamarind"
        ],
        "instructions": "1.To make the sauce, melt and caramelize the palm sugar.\\n2.Add water to stop the caramelization.\\n3.Add fish sauce and tamarind and bring to a simmer."
    }
    """
    When the client call the update recipe endpoint with valid headers and body
    """
    {
        "id": 1,
        "name": "Pad Thai",
        "numberOfServings": 2,
        "category": "main-course",
        "ingredients": [
            "palm sugar",
            "water",
            "fish sauce",
            "tamarind",
            "new ingredient"
        ],
        "instructions": "1.To make the sauce, melt and caramelize the palm sugar.\\n2.Add water to stop the caramelization.\\n3.Add fish sauce and tamarind and bring to a simmer."
    }
    """
    Then the client receives status code 200 with body
    """
    {
        "id": 1,
        "name": "Pad Thai",
        "numberOfServings": 2,
        "category": "main-course",
        "ingredients": [
            "palm sugar",
            "water",
            "fish sauce",
            "tamarind",
            "new ingredient"
        ],
        "instructions": "1.To make the sauce, melt and caramelize the palm sugar.\\n2.Add water to stop the caramelization.\\n3.Add fish sauce and tamarind and bring to a simmer."
    }
    """

  Scenario: client makes call to delete a non existent recipe
    When the client call the delete recipe endpoint with id 1
    Then the client receives status code 404

  Scenario: client makes call to delete an existent recipe
    Given the client call the create recipe endpoint with valid headers and body
    """
    {
        "name": "Pad Thai",
        "numberOfServings": 2,
        "category": "main-course",
        "ingredients": [
            "palm sugar",
            "water",
            "fish sauce",
            "tamarind"
        ],
        "instructions": "1.To make the sauce, melt and caramelize the palm sugar.\\n2.Add water to stop the caramelization.\\n3.Add fish sauce and tamarind and bring to a simmer."
    }
    """
    When the client call the delete recipe endpoint with id 1
    Then the client receives status code 204

  Scenario: client makes call to get an existent recipe
    Given the client call the create recipe endpoint with valid headers and body
    """
    {
        "name": "Pad Thai",
        "numberOfServings": 2,
        "category": "main-course",
        "ingredients": [
            "palm sugar",
            "water",
            "fish sauce",
            "tamarind"
        ],
        "instructions": "1.To make the sauce, melt and caramelize the palm sugar.\\n2.Add water to stop the caramelization.\\n3.Add fish sauce and tamarind and bring to a simmer."
    }
    """
    When the client call the get recipe endpoint with id 1
    Then the client receives status code 200 with body
    """
    {
        "id": 1,
        "name": "Pad Thai",
        "numberOfServings": 2,
        "category": "main-course",
        "ingredients": [
            "palm sugar",
            "water",
            "fish sauce",
            "tamarind"
        ],
        "instructions": "1.To make the sauce, melt and caramelize the palm sugar.\\n2.Add water to stop the caramelization.\\n3.Add fish sauce and tamarind and bring to a simmer."
    }
    """

  Scenario: client makes a call to get all recipes with no data in the db
    When the client call the get all recipes endpoint
    Then the client receives status code 404

  Scenario: client makes call to get all recipes
    Given The database contains test data
    When the client call the get all recipes endpoint
    Then the client receives status code 200 with body
    """
    [
      {
          "id": 1,
          "name": "Pad Thai",
          "numberOfServings": 2,
          "category": "main-course",
          "ingredients": [
              "palm sugar",
              "water",
              "fish sauce",
              "tamarind"
          ],
          "instructions": "1.To make the sauce, melt and caramelize the palm sugar.\n2.Add water to stop the caramelization.\n3.Add fish sauce and tamarind and bring to a simmer."
      },
      {
          "id": 2,
          "name": "Scrambled eggs",
          "numberOfServings": 1,
          "category": "breakfast",
          "ingredients": [
              "2 large eggs",
              "6 tbsp single cream milk",
              "a knob of butter"
          ],
          "instructions": "1.Lightly whisk 2 large eggs, 6 tbsp single cream or full cream milk and a pinch of salt together until the mixture has just one consistency.\n2.Heat a small non-stick frying pan for a minute or so, then add a knob of butter and let it melt. Don’t allow the butter to brown or it will discolour the eggs.\n3.Pour in the egg mixture and let it sit, without stirring, for 20 seconds. Stir with a wooden spoon, lifting and folding it over from the bottom of the pan."
      }
    ]
    """

  Scenario: client makes call to get all recipes filtering by instructions
    Given The database contains test data
    When the client call the get all recipes endpoint with instructionContains 'pan' numberOfServings -1 category '' includeIngredients '' excludeIngredients ''
    Then the client receives status code 200 with body
    """
    [
      {
          "id": 2,
          "name": "Scrambled eggs",
          "numberOfServings": 1,
          "category": "breakfast",
          "ingredients": [
              "2 large eggs",
              "6 tbsp single cream milk",
              "a knob of butter"
          ],
          "instructions": "1.Lightly whisk 2 large eggs, 6 tbsp single cream or full cream milk and a pinch of salt together until the mixture has just one consistency.\n2.Heat a small non-stick frying pan for a minute or so, then add a knob of butter and let it melt. Don’t allow the butter to brown or it will discolour the eggs.\n3.Pour in the egg mixture and let it sit, without stirring, for 20 seconds. Stir with a wooden spoon, lifting and folding it over from the bottom of the pan."
      }
    ]
    """

  Scenario: client makes call to get all recipes filtering by number of servings
    Given The database contains test data
    When the client call the get all recipes endpoint with instructionContains '' numberOfServings 2 category '' includeIngredients '' excludeIngredients ''
    Then the client receives status code 200 with body
    """
    [
      {
          "id": 1,
          "name": "Pad Thai",
          "numberOfServings": 2,
          "category": "main-course",
          "ingredients": [
              "palm sugar",
              "water",
              "fish sauce",
              "tamarind"
          ],
          "instructions": "1.To make the sauce, melt and caramelize the palm sugar.\n2.Add water to stop the caramelization.\n3.Add fish sauce and tamarind and bring to a simmer."
      }
    ]
    """

  Scenario: client makes call to get all recipes filtering by category
    Given The database contains test data
    When the client call the get all recipes endpoint with instructionContains '' numberOfServings -1 category 'breakfast' includeIngredients '' excludeIngredients ''
    Then the client receives status code 200 with body
    """
    [
      {
          "id": 2,
          "name": "Scrambled eggs",
          "numberOfServings": 1,
          "category": "breakfast",
          "ingredients": [
              "2 large eggs",
              "6 tbsp single cream milk",
              "a knob of butter"
          ],
          "instructions": "1.Lightly whisk 2 large eggs, 6 tbsp single cream or full cream milk and a pinch of salt together until the mixture has just one consistency.\n2.Heat a small non-stick frying pan for a minute or so, then add a knob of butter and let it melt. Don’t allow the butter to brown or it will discolour the eggs.\n3.Pour in the egg mixture and let it sit, without stirring, for 20 seconds. Stir with a wooden spoon, lifting and folding it over from the bottom of the pan."
      }
    ]
    """

  Scenario: client makes call to get all recipes filtering by included ingredients
    Given The database contains test data
    When the client call the get all recipes endpoint with instructionContains '' numberOfServings -1 category '' includeIngredients 'fish sauce' excludeIngredients ''
    Then the client receives status code 200 with body
    """
    [
      {
          "id": 1,
          "name": "Pad Thai",
          "numberOfServings": 2,
          "category": "main-course",
          "ingredients": [
              "palm sugar",
              "water",
              "fish sauce",
              "tamarind"
          ],
          "instructions": "1.To make the sauce, melt and caramelize the palm sugar.\n2.Add water to stop the caramelization.\n3.Add fish sauce and tamarind and bring to a simmer."
      }
    ]
    """

  Scenario: client makes call to get all recipes filtering by excluded ingredients
    Given The database contains test data
    When the client call the get all recipes endpoint with instructionContains '' numberOfServings -1 category '' includeIngredients '' excludeIngredients 'fish sauce'
    Then the client receives status code 200 with body
    """
    [
      {
          "id": 2,
          "name": "Scrambled eggs",
          "numberOfServings": 1,
          "category": "breakfast",
          "ingredients": [
              "2 large eggs",
              "6 tbsp single cream milk",
              "a knob of butter"
          ],
          "instructions": "1.Lightly whisk 2 large eggs, 6 tbsp single cream or full cream milk and a pinch of salt together until the mixture has just one consistency.\n2.Heat a small non-stick frying pan for a minute or so, then add a knob of butter and let it melt. Don’t allow the butter to brown or it will discolour the eggs.\n3.Pour in the egg mixture and let it sit, without stirring, for 20 seconds. Stir with a wooden spoon, lifting and folding it over from the bottom of the pan."
      }
    ]
    """

  Scenario: client makes call to get all recipes using all filters
    Given The database contains test data
    When the client call the get all recipes endpoint with instructionContains 'simmer' numberOfServings 2 category 'main-course' includeIngredients 'tamarind' excludeIngredients 'eggs'
    Then the client receives status code 200 with body
    """
    [
      {
          "id": 1,
          "name": "Pad Thai",
          "numberOfServings": 2,
          "category": "main-course",
          "ingredients": [
              "palm sugar",
              "water",
              "fish sauce",
              "tamarind"
          ],
          "instructions": "1.To make the sauce, melt and caramelize the palm sugar.\n2.Add water to stop the caramelization.\n3.Add fish sauce and tamarind and bring to a simmer."
      }
    ]
    """

  Scenario: client makes call to get all recipes using all filters with no results
    Given The database contains test data
    When the client call the get all recipes endpoint with instructionContains 'oven' numberOfServings 3 category 'vegetarian' includeIngredients 'chicken' excludeIngredients 'water'
    Then the client receives status code 404