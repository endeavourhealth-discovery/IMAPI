Feature: Code is generated

  Scenario: Can convert a single model into a simple class
    Given a model with iri "http://test" and name "TestModel" and description "This is a test model"
    And a namespace "org.example"
    And a collection wrapper "List<${BASE DATA TYPE}>"
    And a file extension ".java"
    And properties
    """
    [
      { "property": { "@id": "http://prop1", "name": "First property"}, "type": { "@id": "http://www.w3.org/2001/XMLSchema#string", "name": "string" }, "maxExclusive": "1" },
      { "property": { "@id": "http://prop2", "name": "Second property"}, "type": { "@id": "http://www.w3.org/2001/XMLSchema#string", "name": "string" } },
      { "property": { "@id": "http://prop3", "name": "Third property"}, "type": { "@id": "http://www.w3.org/2001/XMLSchema#string", "name": "string" }, "maxExclusive": "0" },
      { "property": { "@id": "http://prop4", "name": "Fourth property"}, "type": { "@id": "http://www.w3.org/2001/XMLSchema#string", "name": "string" }, "maxExclusive": "2" }
    ]
    """
    And a template of
    """
    package ${NAME SPACE};

    // Represents ${MODEL NAME} - ${MODEL COMMENT}

    OBJECT ${ModelName} :
    <template #property>
      VAR ${DATA TYPE} ${propertyName}
    </template #property>
    """
    And a file extension ".foo"
    And a collection wrapper "${BASE DATA TYPE}[]"
    When the code is generated
    Then the output should be
    """
    package org.example;

    // Represents TestModel - This is a test model

    OBJECT Testmodel :
      VAR string firstProperty
      VAR string[] secondProperty
      VAR string[] thirdProperty
      VAR string fourthProperty

    """


  Scenario: Can convert a single model into a single Java class
    Given a model with iri "http://test" and name "TestModel" and description "This is a test model"
    And a namespace "org.example"
    And a collection wrapper "List<${BASE DATA TYPE}>"
    And a file extension ".java"
    And a datatype map
    """
    {
      "http://www.w3.org/2001/XMLSchema#string" : "String"
    }
    """
    And properties
    """
    [
      { "property": { "@id": "http://prop1", "name": "First property"}, "type": { "@id": "http://www.w3.org/2001/XMLSchema#string", "name": "string" }, "maxExclusive": "1" },
      { "property": { "@id": "http://prop2", "name": "Second property"}, "type": { "@id": "http://www.w3.org/2001/XMLSchema#string", "name": "string" } },
      { "property": { "@id": "http://prop3", "name": "Third property"}, "type": { "@id": "http://www.w3.org/2001/XMLSchema#string", "name": "string" }, "maxExclusive": "0" },
      { "property": { "@id": "http://prop4", "name": "Fourth property"}, "type": { "@id": "http://www.w3.org/2001/XMLSchema#string", "name": "string" }, "maxExclusive": "2" }
    ]
    """
    And a template of
    """
    package ${NAME SPACE};

    import java.util.ArrayList;
    import java.util.List;

    /**
    * Represents ${MODEL NAME}
    * ${MODEL COMMENT}
    */

    public class ${ModelName} {
    <template #property>
      private ${DATA TYPE} ${propertyName};

      /**
      * Gets the ${PROPERTY NAME} of this ${MODEL NAME}
      * @return ${propertyName}
      */
      public ${DATA TYPE} get${PropertyName}() {
        return ${propertyName};
      }

      /**
      * Sets the ${PROPERTY NAME} of this ${MODEL NAME}
      * @param ${propertyName} The new ${PROPERTY NAME} to set
      * @return ${ModelName}
      */
      public ${ModelName} set${PropertyName}(${DATA TYPE} value) {
        ${propertyName} = value;
        return this;
      }

    <template #array>
      /**
      * Adds the given ${PROPERTY NAME} to this ${MODEL NAME}
      * @param ${propertyName} The ${PROPERTY NAME} to add
      * @return ${ModelName}
      */
      public ${ModelName} add${PropertyName}(${BASE DATA TYPE} ${propertyName}) {
        ${DATA TYPE} array = this.get${PropertyName}();

        if (null == array) {
          array = new ArrayList();
          this.set${PropertyName}(array);
        }

        array.add(${propertyName});

        return this;
      }

    </template #array>
    </template #property>
    }
    """
    And a file extension ".java"
    And a collection wrapper "List<${BASE DATA TYPE}>"
    When the code is generated
    Then the output should be
    """
    package org.example;

    import java.util.ArrayList;
    import java.util.List;

    /**
    * Represents TestModel
    * This is a test model
    */

    public class Testmodel {
      private String firstProperty;

      /**
      * Gets the First property of this TestModel
      * @return firstProperty
      */
      public String getFirstProperty() {
        return firstProperty;
      }

      /**
      * Sets the First property of this TestModel
      * @param firstProperty The new First property to set
      * @return Testmodel
      */
      public Testmodel setFirstProperty(String value) {
        firstProperty = value;
        return this;
      }

      private List<String> secondProperty;

      /**
      * Gets the Second property of this TestModel
      * @return secondProperty
      */
      public List<String> getSecondProperty() {
        return secondProperty;
      }

      /**
      * Sets the Second property of this TestModel
      * @param secondProperty The new Second property to set
      * @return Testmodel
      */
      public Testmodel setSecondProperty(List<String> value) {
        secondProperty = value;
        return this;
      }

      /**
      * Adds the given Second property to this TestModel
      * @param secondProperty The Second property to add
      * @return Testmodel
      */
      public Testmodel addSecondProperty(String secondProperty) {
        List<String> array = this.getSecondProperty();

        if (null == array) {
          array = new ArrayList();
          this.setSecondProperty(array);
        }

        array.add(secondProperty);

        return this;
      }

      private List<String> thirdProperty;

      /**
      * Gets the Third property of this TestModel
      * @return thirdProperty
      */
      public List<String> getThirdProperty() {
        return thirdProperty;
      }

      /**
      * Sets the Third property of this TestModel
      * @param thirdProperty The new Third property to set
      * @return Testmodel
      */
      public Testmodel setThirdProperty(List<String> value) {
        thirdProperty = value;
        return this;
      }

      /**
      * Adds the given Third property to this TestModel
      * @param thirdProperty The Third property to add
      * @return Testmodel
      */
      public Testmodel addThirdProperty(String thirdProperty) {
        List<String> array = this.getThirdProperty();

        if (null == array) {
          array = new ArrayList();
          this.setThirdProperty(array);
        }

        array.add(thirdProperty);

        return this;
      }

      private String fourthProperty;

      /**
      * Gets the Fourth property of this TestModel
      * @return fourthProperty
      */
      public String getFourthProperty() {
        return fourthProperty;
      }

      /**
      * Sets the Fourth property of this TestModel
      * @param fourthProperty The new Fourth property to set
      * @return Testmodel
      */
      public Testmodel setFourthProperty(String value) {
        fourthProperty = value;
        return this;
      }

    }
    """
