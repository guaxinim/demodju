package org.acme.quickstart.tika;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


/**
 * RestClient
 */
@Path("/api")
@RegisterRestClient
public interface RestClientSend {

    @POST
    @Path("/receive")
    @Produces("application/x-www-form-urlencoded; charset=utf-8")
    String sendData(String data);
}
