package com.recykred.app.models;

public class Material {
    private String descripcion;
    private String url_imagen;
    private String id_material;
    private String nombre;

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrl_imagen() {
        return this.url_imagen;
    }

    public void setUrl_imagen(String url_imagen) {
        this.url_imagen = url_imagen;
    }

    public String getId_material() {
        return this.id_material;
    }

    public void setId_material(String id_material) {
        this.id_material = id_material;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
