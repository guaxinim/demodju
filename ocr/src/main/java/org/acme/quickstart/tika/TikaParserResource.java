package org.acme.quickstart.tika;

import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.tika.TikaParser;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;


@Path("/parse")
public class TikaParserResource {
    private static final Logger log = Logger.getLogger(TikaParserResource.class);

    @Inject
    TikaParser parser;

    @Inject
    @RestClient
    RestClientSend restClient;

    @POST
    @Path("/text")
    @Consumes({"application/pdf", "application/vnd.oasis.opendocument.text"})
    @Produces(MediaType.TEXT_PLAIN)
    public String extractText(InputStream stream) {
        Instant start = Instant.now();

        String text = parser.getText(stream);

        Instant finish = Instant.now();

        log.info(Duration.between(start, finish).toMillis() + " mls have passed");

        restClient.sendData(text);

        return text;
    }
}