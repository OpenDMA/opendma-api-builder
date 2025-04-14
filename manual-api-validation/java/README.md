# Manual validation of generated Java API

Validation steps:
1. In a new Eclipse workspace, import the generated java api project (`../../src.generated/java`) as well as the test project (`./test`) as maven projects.
2. In the `pom.xml` of the test project, adjust the version of the OpenDMA API in the dependencies. Eclipse should now link both against each other.
3. Run the `Test.java` program in Eclipse.

Alternative validation steps w/o Eclipse:
1. In the generated java api project (`../../src.generated/java`), run `mvn clean install`. This will build the java API and install it locally.
2. In the file `./test/pom.xml`, adjust the version of the OpenDMA API in the dependencies.
3. In the folder `./test`, run `mvn clean package`. This will build the test project
4. Manually run the `Test` programm in java on the command line. Add both generated .jar files to the classpath.