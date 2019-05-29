package com.recykred.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.recykred.app.DatosReciclador;
import com.recykred.app.R;
import com.recykred.app.RecycladoresActivity;
import com.recykred.app.models.Usuario;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecicladoresAdapter extends RecyclerView.Adapter<RecicladoresAdapter.MyViewHolder> {
    private List<Usuario> usuarioList;
    private Context context;

    public RecicladoresAdapter(List<Usuario> usuarioList, Context context) {
        this.usuarioList = usuarioList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_reciclador,viewGroup,false);
        return new RecicladoresAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.txtNombre.setText(usuarioList.get(i).getNombres());
        myViewHolder.txtId.setText(usuarioList.get(i).getNumero_identificacion());
        myViewHolder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RecycladoresActivity)context).eliminarReciclador(usuarioList.get(i),i);
            }
        });

        myViewHolder.cvReciclador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DatosReciclador.class);
                intent.putExtra("reciclador",usuarioList.get(i));
                intent.putExtra("position",i);
                ((RecycladoresActivity)context).startActivityForResult(intent,2);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usuarioList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtNombre)        TextView txtNombre;
        @BindView(R.id.txtId)        TextView txtId;
        @BindView(R.id.btnEliminar)        Button btnEliminar;
        @BindView(R.id.cvReciclador)        CardView cvReciclador;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
