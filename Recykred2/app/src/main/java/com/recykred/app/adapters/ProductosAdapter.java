package com.recykred.app.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.recykred.app.R;
import com.recykred.app.models.Productos;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.MyViewHolder> {
    private List<Productos> productosList;
    private Context context;

    public ProductosAdapter(List<Productos> productosList, Context context) {
        this.productosList = productosList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_productos,viewGroup,false);
        return new ProductosAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.txtNombre.setText(productosList.get(i).getNombre());
        myViewHolder.txtPeso.setText(productosList.get(i).getPeso());
        myViewHolder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productosList.remove(i);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productosList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtNombre)        TextView txtNombre;
        @BindView(R.id.txtPeso)        TextView txtPeso;
        @BindView(R.id.btnEliminar)        Button btnEliminar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
