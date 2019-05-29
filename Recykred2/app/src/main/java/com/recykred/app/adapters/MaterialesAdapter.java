package com.recykred.app.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.recykred.app.R;
import com.recykred.app.models.Material;
import com.recykred.app.models.Pedidos;
import com.recykred.app.models.Productos;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MaterialesAdapter extends RecyclerView.Adapter<MaterialesAdapter.MyViewHolder> {
    private List<Material> materialList;
    private List<Productos> materialSelect;
    private Context context;

    public MaterialesAdapter(List<Material> materialList, Context context,List<Productos> materialSelect) {
        this.materialList = materialList;
        this.context = context;
        this.materialSelect = materialSelect;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_seleccion_multiple,viewGroup,false);
        return new MaterialesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.txtMaterial.setText(materialList.get(i).getNombre());
        myViewHolder.cbSeleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myViewHolder.etPeso.getText().toString().trim().isEmpty()){
                    myViewHolder.etPeso.setError("");
                    myViewHolder.cbSeleccionar.setChecked(false);

                }else{
                    Productos productos = new Productos();
                    productos.setId_material(materialList.get(i).getId_material());
                    productos.setNombre(materialList.get(i).getNombre());
                    productos.setPeso(myViewHolder.etPeso.getText().toString().trim());

                    if(myViewHolder.cbSeleccionar.isChecked()){
                        materialSelect.add(productos);
                    }else{
                        for(int j = 0;j<materialList.size();j++){
                            if(materialList.get(j).getId_material().equalsIgnoreCase(productos.getId_material())){
                                materialSelect.remove(j);
                            }
                        }
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return materialList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtMaterial)        TextView txtMaterial;
        @BindView(R.id.etPeso)        EditText etPeso;
        @BindView(R.id.cbSeleccionar)        CheckBox cbSeleccionar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
