package org.acme.customer;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.hamcrest.CoreMatchers.is;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(CustomerCustomResource.class)
public class CustomerCustomResourceTest {

    @Inject
    @ConfigProperty(name = "default_page_size")
    private double defaultPageSize;

    @BeforeAll
    public static void setup(){
        persistCustomers(20);
    }

    @Transactional
    public static void persistCustomers(int numberOfCustomerToPersist){
        for (int i = 1; i <= numberOfCustomerToPersist; i++) {
            Customer customer = new Customer.Builder()
                    .id(Long.valueOf(i))
                    .name("Customer " + i)
                    .status(CustomerStatus.SILVER.getStatus())
                    .build();
           customer.persist();
        }
    }

    @Test
    public void totalCountHeaderGetTest() {
        double totalNumberOfCustomers = (double) Customer.findAll().count();
        int expectedNumberOfPageResult = (int) Math.ceil(totalNumberOfCustomers / defaultPageSize);

        given()
            .when()
                .get("/")
            .then()
                .statusCode(200)
                .header("X-Total-Count", String.valueOf(expectedNumberOfPageResult));
    }

    @Test
    public void findByNameTest() {
        given()
                .when()
                .get("/?name=Customer&size=100")
                .then()
                .statusCode(200)
                .body("size()", is(20));
    }
}
