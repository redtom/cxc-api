# CXC Coding Test - Back End Java 8 / Spring boot code.

To run this application do the following steps:
1. Ensure Java 8 is installed
2. Ensure mvn 3.5+ is installed
3. Ensure git is installed
4. Run git clone https://github.com/redtom/cxc-api.git
5. Run mvn spring-boot:run

## Comments
1. Unit tests exist for all Java code.
2. As mentioned in both front and back end code comments, there is no authentication. The user ID is hard coded in the front end, and passed in as part of the REST url, or part of the JSON body. In a real system it should come in as a JWT token in a the Authorization HTTP header.
3. Persistence is handled by JPA by way of Spring data. The database used in an in memory HSQLDB server. Technically this isn't persistent, as the data is lost when the application is killed. Only simple configuration changes would be required to connect to an external database. The point of this test isn't to show devops ability ... 
