package com.recykred.app.models;

import java.io.Serializable;

public class Pedido1 implements Serializable {
    private String estado_id;
    private String id_centro_acopio;
    private String feha_recogida;
    private String estado;
    private String id_cliente;
    private String fecha_eliminacion;
    private String peso;
    private String id_reciclador;
    private String fecha_actualizacion;
    private String fecha_creacion;
    private String id_pedido;
    private String direccion_recogida;

    public String getEstado_id() {
        return this.estado_id;
    }

    public void setEstado_id(String estado_id) {
        this.estado_id = estado_id;
    }

    public String getId_centro_acopio() {
        return this.id_centro_acopio;
    }

    public void setId_centro_acopio(String id_centro_acopio) {
        this.id_centro_acopio = id_centro_acopio;
    }

    public String getFeha_recogida() {
        return this.feha_recogida;
    }

    public void setFeha_recogida(String feha_recogida) {
        this.feha_recogida = feha_recogida;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getId_cliente() { return this.id_cliente; }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getFecha_eliminacion() {
        return this.fecha_eliminacion;
    }

    public void setFecha_eliminacion(String fecha_eliminacion) {
        this.fecha_eliminacion = fecha_eliminacion;
    }

    public String getPeso() {
        return this.peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getId_reciclador() {
        return this.id_reciclador;
    }

    public void setId_reciclador(String id_reciclador) {
        this.id_reciclador = id_reciclador;
    }

    public String getFecha_actualizacion() {
        return this.fecha_actualizacion;
    }

    public void setFecha_actualizacion(String fecha_actualizacion) {
        this.fecha_actualizacion = fecha_actualizacion;
    }

    public String getFecha_creacion() {
        return this.fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public String getId_pedido() {
        return this.id_pedido;
    }

    public void setId_pedido(String id_pedido) {
        this.id_pedido = id_pedido;
    }

    public String getDireccion_recogida() {
        return this.direccion_recogida;
    }

    public void setDireccion_recogida(String direccion_recogida) {
        this.direccion_recogida = direccion_recogida;
    }
}
