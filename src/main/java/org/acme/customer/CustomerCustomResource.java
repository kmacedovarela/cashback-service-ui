package org.acme.customer;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@Path("customer")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class CustomerCustomResource {

    @ConfigProperty(name = "default_page_size")
    int defaultPageSize;

    private static final Logger logger = Logger.getLogger(CustomerCustomResource.class.getName());

    /**
     * Retrieves a paginated search and sets the total page count of the last search
     * in the response header X-Total-Count.
     */
    @GET
    public Response get(@QueryParam("page") @DefaultValue("0") int pageIndex,
                        @QueryParam("size") int pageSize,
                        @QueryParam("name") @DefaultValue("") String name) {

        List<Customer> customerList;
        pageSize = (pageSize == 0) ? defaultPageSize : pageSize;

        logger.info("Searching for: "+name+" pageSize "+pageSize+" pageIndex "+pageIndex);

        customerList = Customer.findAll(pageIndex, pageSize, name);

        logger.info("Returning a list of "+customerList.size()+ " customers.");

        return Response.ok(customerList)
                .header("X-Total-Count", Customer.getPageCount())
                .build();
    }

}
