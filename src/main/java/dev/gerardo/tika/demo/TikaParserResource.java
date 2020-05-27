package dev.gerardo.tika.demo;

import dev.gerardo.tika.demo.beans.Documento;
import dev.gerardo.tika.demo.services.LuceneService;
import dev.gerardo.tika.demo.services.TikaService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/api")
public class TikaParserResource {

    private static final Logger log = Logger.getLogger(TikaParserResource.class.getName());

    @Inject
    TikaService serviceTika;

    @Inject
    LuceneService luceneService;

    @POST
    @Path("/guardar")
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    public Documento extractText(InputStream stream) {
        Documento doc = serviceTika.parsear(stream);

        luceneService.indexar(doc);

        log.log(Level.INFO, doc.toString());
        return doc;
    }

    @GET
    @Path("/buscar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Documento> consultar(@QueryParam("query") String query) {
        return luceneService.buscar(query);
    }
}