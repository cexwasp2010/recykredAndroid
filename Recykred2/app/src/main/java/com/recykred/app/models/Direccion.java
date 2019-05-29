package com.recykred.app.models;

public class Direccion {
    private String usuario_id_usuario;
    private int id_direccion;
    private String direccion;

    public String getUsuario_id_usuario() {
        return this.usuario_id_usuario;
    }

    public void setUsuario_id_usuario(String usuario_id_usuario) {
        this.usuario_id_usuario = usuario_id_usuario;
    }

    public int getId_direccion() {
        return this.id_direccion;
    }

    public void setId_direccion(int id_direccion) {
        this.id_direccion = id_direccion;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
