package com.recykred.app.models;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String apellidos;
    private String id_rol;
    private String numero_identificacion;
    private String nombrerol;
    private String id_usuario;
    private String numero_celular;
    private String contrasenia;
    private String email;
    private String padre_id;
    private String nombres;
    private String activo;

    public String getApellidos() {
        return this.apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getId_rol() {
        return this.id_rol;
    }

    public void setId_rol(String id_rol) {
        this.id_rol = id_rol;
    }

    public String getNumero_identificacion() {
        return this.numero_identificacion;
    }

    public void setNumero_identificacion(String numero_identificacion) {
        this.numero_identificacion = numero_identificacion;
    }

    public String getNombrerol() {
        return this.nombrerol;
    }

    public void setNombrerol(String nombrerol) {
        this.nombrerol = nombrerol;
    }

    public String getId_usuario() {
        return this.id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNumero_celular() {
        return this.numero_celular;
    }

    public void setNumero_celular(String numero_celular) {
        this.numero_celular = numero_celular;
    }

    public String getContrasenia() {
        return this.contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPadre_id() {
        return this.padre_id;
    }

    public void setPadre_id(String padre_id) {
        this.padre_id = padre_id;
    }

    public String getNombres() {
        return this.nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getActivo() {
        return this.activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }
}
