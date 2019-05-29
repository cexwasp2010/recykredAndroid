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

import com.recykred.app.Pedidos2Activity;
import com.recykred.app.PedidosDetalles;
import com.recykred.app.R;
import com.recykred.app.models.Pedido1;
import com.recykred.app.retrofit.RetrofitApi;
import com.recykred.app.retrofit.RetrofitClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import retrofit2.Retrofit;

public class Pedidos2Adapter extends RecyclerView.Adapter<Pedidos2Adapter.MyViewHolder> {
    private List<Pedido1> pedidosList;
    private Context context;


    public Pedidos2Adapter(List<Pedido1> pedidosList, Context context) {
        this.pedidosList = pedidosList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_pedido_2,viewGroup,false);
        return new Pedidos2Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.txtEstado.setText(pedidosList.get(i).getEstado());
        myViewHolder.txtMaterial.setText(pedidosList.get(i).getId_cliente());
        myViewHolder.txtPeso.setText(pedidosList.get(i).getPeso());
        myViewHolder.btnAccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        final String estado = pedidosList.get(i).getEstado_id();
        switch (estado) {
            case "1":
                myViewHolder.btnAccion.setText("Tomar");
                break;
            case "2":
                myViewHolder.btnAccion.setText("Recibido");
                break;
            case "3":
                myViewHolder.btnAccion.setText("ver");
                break;
            case "4":
                myViewHolder.btnAccion.setText("ver");
                break;
        }

        myViewHolder.btnAccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (estado) {
                    case "1":
                        ((Pedidos2Activity)context).tomarPedido(pedidosList.get(i));
                        break;
                    case "2":

                        break;
                    case "3":
                       ;
                        break;
                    case "4":
                        ((Pedidos2Activity)context).marcarRecibido(pedidosList.get(i));
                        break;
                }
            }
        });

        myViewHolder.cvPedidos2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PedidosDetalles.class);
                intent.putExtra("idPedido",pedidosList.get(i).getId_pedido());
                context.startActivity(intent);
            }
        });
        }


    @Override
    public int getItemCount() {
        return pedidosList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtMaterial)        TextView txtMaterial;
        @BindView(R.id.txtPeso)        TextView txtPeso;
        @BindView(R.id.txtEstado)        TextView txtEstado;
        @BindView(R.id.btnAccion)        Button btnAccion;
        @BindView(R.id.cvPedidos2)        CardView cvPedidos2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
