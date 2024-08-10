# Sean Welch - gRPC - Smart Office - Distributed Systems
* A java program designed to implement a Smart Office application that uses gRPC as the underlying client <-> server communication.

### Project Structure
* Project was built with Intellij using the maven build system - therefore maven will be required to run the project.
* Dependencies are of course listed in the `pom.xml` file.
* Unit testing is implemented using the Junit testing frameowork, please run tests as per your IDE instructions if necessary.
* gRPC backend is available in the `src.main.java.services` directory, while the client code is available in `src.main.java.client`

### Project Instructions
* Project must be compiled with Maven in order to have access to generated gRPC stubs.
* In order to run the project, database access will be required; Database schema and dummy data is provided in the `tables.sql` file. 
* Please use the makefile commands to run the database scripts - `make database` in the terminal
* If necessary, an alternative UI is available for testing using the grcpui package
* Download grpcui as per your operating system e.g `brew install grcpui` and then run the client command in makefile: `make client`
* Run the official project UI as per your IDE by pointing the entry point to the `client.UI.ClientUI` class and run the `services.GrpcServer` on another port