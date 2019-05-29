package com.recykred.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.recykred.app.Pedidos1Activity;
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

public class Pedidos1Adapter extends RecyclerView.Adapter<Pedidos1Adapter.MyViewHolder> {
    private List<Pedido1> pedidosList;
    private Context context;
    private Retrofit retrofit;
    private RetrofitApi retrofitApi;
    private Disposable disposable;

    public Pedidos1Adapter(List<Pedido1> pedidosList, Context context) {
        this.pedidosList = pedidosList;
        this.context = context;
        retrofit = RetrofitClient.getInstance("");
        retrofitApi = retrofit.create(RetrofitApi.class);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_pedido_1,viewGroup,false);
        return new Pedidos1Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.txtPeso.setText(pedidosList.get(i).getPeso());
        myViewHolder.txtFecha.setText(pedidosList.get(i).getFecha_creacion());
        myViewHolder.txtEstado.setText(pedidosList.get(i).getEstado());
        final String estado = pedidosList.get(i).getEstado_id();
        switch (estado) {
            case "1":
                myViewHolder.btnAccion.setText("Cancelar");
                break;
            case "2":
                myViewHolder.btnAccion.setText("ver");
                break;
            case "3":
                myViewHolder.btnAccion.setText("Recogido");
                break;
            case "4":
                myViewHolder.btnAccion.setText("ver");
                break;
            case "5":
                myViewHolder.btnAccion.setText("ver");
                break;
        }
        myViewHolder.btnAccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (estado) {
                    case "1":
                        AndroidNetworking.post("http://conectemonos.com/pedido/delete/{input}")
                                .addPathParameter("input",pedidosList.get(i).getId_pedido())
                                .build()
                                .getAsString(new StringRequestListener() {
                                    @Override
                                    public void onResponse(String response) {
                                        ((Pedidos1Activity)context).eliminarPedido(pedidosList.get(i));
                                        Toast.makeText(context,"Pedido eliminado con exito",Toast.LENGTH_SHORT).show();
                                        Log.d("aca","response = " + response);
                                    }
                                    @Override
                                    public void onError(ANError anError) {
                                        Log.d("aca","error = " + anError.getErrorBody());
                                    }
                                });

                        break;
                    case "2":

                        break;
                    case "3":
                        ((Pedidos1Activity)context).marcarRecogido(pedidosList.get(i));
                        break;
                    case "4":

                        break;
                    case "5":

                        break;
                }
            }
        });

        myViewHolder.cvPedido1.setOnClickListener(new View.OnClickListener() {
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
        @BindView(R.id.txtMaterial)        TextView txtFecha;
        @BindView(R.id.txtPeso)        TextView txtPeso;
        @BindView(R.id.txtEstado)        TextView txtEstado;
        @BindView(R.id.btnAccion)        Button btnAccion;
        @BindView(R.id.cvPedido1)        CardView cvPedido1;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
