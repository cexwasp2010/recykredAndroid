package com.recykred.app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PedidosMateriales  {

    private String peso;

    private String material_id_material;

    public String getPeso() {
        return this.peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getMaterial_id_material() {
        return this.material_id_material;
    }

    public void setMaterial_id_material(String material_id_material) {
        this.material_id_material = material_id_material;
    }
}
