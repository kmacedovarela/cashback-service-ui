# cashback-service

short notes:

* Start postgres database:
    ```
    docker run -it --rm -e POSTGRESQL_USER=retail -e POSTGRESQL_DATABASE=cashback -e POSTGRESQL_PASSWORD=retail -p 5432:5432  quay.io/solution-pattern-cdc/cashback-db:ed80f7b
    ```
* Import some data 
  * Example at (`src/main/test/resources/import.sql`)
* Start the service
  * `mvn quarkus:dev`
* Access it on [localhost:8080/]()
