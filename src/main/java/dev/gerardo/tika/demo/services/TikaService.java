package dev.gerardo.tika.demo.services;

import dev.gerardo.tika.demo.beans.Documento;
import io.quarkus.tika.TikaContent;
import io.quarkus.tika.TikaMetadata;
import io.quarkus.tika.TikaParser;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class TikaService {
    private static final Logger log = Logger.getLogger(TikaService.class.getName());

    @Inject
    TikaParser parser;

    /**
     * Metodo que hace el parsing de un documento
     * @param input
     * @return
     */
    public Documento parsear(InputStream input) {
        TikaContent contenido = parser.parse(input);
        TikaMetadata meta = contenido.getMetadata();
        StringBuilder metadata = new StringBuilder();
        Set<String> names = meta.getNames();
        for (String name : names) {
            metadata.append(name + " = " + meta.getValues(name) + "\n");
        }

        return new Documento(metadata.toString(), contenido.getText());
    }
}
