package com.recykred.app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Pedidos  {

    private String estado_id;

    private String feha_recogida;

    private String id_cliente;

    private List<PedidosMateriales> materiales;

    private String direccion_recogida;

    public String getEstado_id() {
        return this.estado_id;
    }

    public void setEstado_id(String estado_id) {
        this.estado_id = estado_id;
    }

    public String getFeha_recogida() {
        return this.feha_recogida;
    }

    public void setFeha_recogida(String feha_recogida) {
        this.feha_recogida = feha_recogida;
    }

    public String getId_cliente() {
        return this.id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public List<PedidosMateriales> getMateriales() {
        return this.materiales;
    }

    public void setMateriales(List<PedidosMateriales> materiales) {
        this.materiales = materiales;
    }

    public String getDireccion_recogida() {
        return this.direccion_recogida;
    }

    public void setDireccion_recogida(String direccion_recogida) {
        this.direccion_recogida = direccion_recogida;
    }
}
