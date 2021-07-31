# MBTA Coding Exercise

## Introduction  
This project involves downloading and querying MBTA data. It is written in SpringBoot, and uses MySQL and Docker.
There are a few parts to the application
 - **mbta-db**
This contains the data for a Docker Volume. It has all the downloaded data. It also has files :
	- `create_tables.txt` : Contains SQL for generating the db tables should you wish to do so manually.
	- `docker-compose.yml` : Dockerfile for bringing up the database.

- **mbta-data**
  This project loads the data from the MBTA API. For each of Line, Route, Stop, and Facility entities, it invokes the relevant endpoints, parses out the data it needs, and persists it to the Docker database. It can be run independently whenever you wish to refresh the data.
  
- **mbta-service**
  This app is a webservice with endpoints providing MBTA information. There are endpoints for specific use cases. Information is obtained by querying the mbta database that was populated by the `mbta-data` app. I have mostly used native SQL queries to retrieve the data.
  
- **javadoc**
This is a directory which has jar files containing javadoc for for 2 prjects.
  
## Install and deploy
The only requirement for this project is that Docker be installed on your machine.

1. Download the code in this Github repo to a location on your machine.

2. Start the database and create a Docker network. In your Unix (or Unix-ish) command line, navigate to 
    ```
    $ cd <project-dir>/mbta-db
    $ docker-compose up -d
    ```
    This will create a Docker Network called `mbta-network`. By default, this will load up data in the `mbta-db` directory.

3. If you want to reload the MBTA API data, build and run the `mbta-data` app. From your command line, execute the following commands - ( project-dir is the root directory of this project )
     ```
    $ cd <project-dir>/mbta-data
    
    # Build mbta-data image
    $ docker image build -t mbta-data-app .
    
    # Run mbta-data
    $ docker run --network mbta-network -i mbta-data-app
    ```
	This will run the application to fetch the latest data.
	
4. Next you can build and deploy the `mbta-service` app. From your command line, execute the following commands - ( project-dir is the root directory of this project )
     ```
    $ cd <project-dir>/mbta-service
    
    # Build mbta-service image
    $ docker image build -t mbta-service-app .
    
    # Run mbta-service
    $ docker run --network mbta-network -p 8080:8080 -i mbta-service-app
    ```
Now you can navigate to the available endpoints to make queries. You could use a tool such as Restlet or Postman. E.g. `http://localhost:8080/mbta/routes` will provide a list of all MBTA subway routes.

## Javadoc
Javadoc jar files have been provided in the javadoc directory. There are separate jar files for the `mbta-data` and `mbta-service` apps. In the `mbta-service` app, navigate to `MbtaController.java`. This provides the Javadoc for the REST endpoints that can be used to interrogate the data.

## Improvements  
This is a very basic implementation, there are many things to improve. Note for small matters, there are TODOs that have been left in the code.
 1. The `mbta-data` app is designed to persist only the data needed for the use cases specified in the problem sheet. In the real world, maximal information would be stored for each entity. This would provide flexibility in the kinds of queries that can be made on this data. 
 2. Expose the`mbta-service` endpoints through `Swagger/OpenAPI`.  
 3. The `mbta-service` could do with some caching improvements. Depending on requirements (e.g. volume of calls made), we could consider creating a local cache of data instead of querying the database each time an endpoint is invoked. 
 4. The `mbta-service` app endpoints typically return Lists of Objects. Custom classes could be created for this. JSON payloads with descriptive tags could be an approach.
 5.  The `mbta-data` and `mbta-service` apps now have replicated entity classes. This should be extracted out into a common module.
 6. Error handling to be implemented.
 7. Variables need to be extracted to a properties file.
 8. Style checking and static code analysis should be part of the build e.g. `PMD, Checkstyle`
 9. The unit tests at the moment are scanty. They need to be fleshed out. `Jacoco` or some such unit test coverage tool should be incorporated in the build.
 10. Integration tests are needed. This could be setup using a separate Docker instance which would spin up it's own MySQL database. Thus, tests could be conducted without intering with the application database.
 11. `MbtaAPIReader.java`
	 - The code needs to be reactive, at the moment it is a synchronous application.
	 - Paging needs to implemented when querying the API.
	 - The conversion of Response data from the API to objects could probably have been done in a better way. Further, a mapping library like Mapstruct could be used.
10. In the `mbta-service` app, the repository layer typically returns lists of Objects. Custom classes could be created instead which map the results from the database.
11. There are several native SQL queries in `mbta-service`. If performance is an issue, these could be converted to Stored Procedures that are kept at the database itself.
12. There is a remaining use case from the problem sheet this is TBD.
13. Implement a proper branching strategy on github.


