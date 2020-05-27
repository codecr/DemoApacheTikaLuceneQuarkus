package dev.gerardo.tika.demo.beans;

import java.io.Serializable;

public class Documento implements Serializable {
    String metadata, contenido;

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Documento() {

    }

    public Documento(String metadata, String contenido) {
        this.metadata = metadata;
        this.contenido = contenido;
    }

    @Override
    public String toString() {
        return "Documento{" +
                "metadata='" + metadata + '\'' +
                ", contenido='" + contenido + '\'' +
                '}';
    }
}
