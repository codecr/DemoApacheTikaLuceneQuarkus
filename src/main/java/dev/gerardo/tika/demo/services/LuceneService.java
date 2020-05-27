package dev.gerardo.tika.demo.services;

import dev.gerardo.tika.demo.beans.Documento;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class LuceneService {
    static final Logger log = Logger.getLogger(LuceneService.class.getName());

    @ConfigProperty(name = "INDEX_DIR")
    String INDEX_PATH;

    /**
     * Metodo que realiza la indexacion
     *
     * @param documento
     */
    public void indexar(Documento documento) {
        try {
            Directory dir = FSDirectory.open(Paths.get(INDEX_PATH));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

            // Agregamos el nuevo documento
            iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
            IndexWriter writer = new IndexWriter(dir, iwc);
            Document doc = new Document();
            Field pathField = new StringField("metadata", documento.getMetadata(), Field.Store.YES);
            doc.add(pathField);
            doc.add(new TextField("contents", documento.getContenido(), Field.Store.YES));
            writer.addDocument(doc);
            writer.close();
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Error al indexar", ex);
        }
    }


    /**
     * Realizamos la busqueda de un texto
     * @param texto
     */
    public List<Documento> buscar(String texto) {
        ArrayList<Documento> respuestas = new ArrayList<>();
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(INDEX_PATH)));
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new StandardAnalyzer();

            QueryParser parser = new QueryParser("contents", analyzer);
            Query query = parser.parse(texto);
            TopDocs results = searcher.search(query, 100);
            ScoreDoc[] hits = results.scoreDocs;

            for (int i = 0; i < hits.length; i++) {

                Document doc = searcher.doc(hits[i].doc);
                respuestas.add(new Documento(doc.get("metadata"),doc.get("contents")));

            }
            reader.close();
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Error al indexar", ex);
        }

        return respuestas;
    }

}
