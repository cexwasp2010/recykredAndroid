package com.recykred.app.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.recykred.app.R;
import com.recykred.app.models.PedidoDetallesMateriales;
import com.recykred.app.models.PedidosMateriales;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MyViewHolder> {
    private List<PedidoDetallesMateriales> materialesList;
    private Context context;

    public MaterialAdapter(List<PedidoDetallesMateriales> materialesList, Context context) {
        this.materialesList = materialesList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_materiales,viewGroup,false);
        return new MaterialAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.txtMaterial.setText(materialesList.get(i).getNombre());
        myViewHolder.txtPeso.setText(materialesList.get(i).getPeso()+" KG");
    }

    @Override
    public int getItemCount() {
        return materialesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtMaterial) TextView txtMaterial;
        @BindView(R.id.txtPeso) TextView txtPeso;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
