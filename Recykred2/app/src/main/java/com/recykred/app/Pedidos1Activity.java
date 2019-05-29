package com.recykred.app;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.recykred.app.adapters.Pedidos1Adapter;
import com.recykred.app.models.Pedido1;
import com.recykred.app.models.Pedidos;
import com.recykred.app.models.Usuario;
import com.recykred.app.retrofit.RetrofitApi;
import com.recykred.app.retrofit.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Pedidos1Activity extends AppCompatActivity {
    private Usuario usuario;
    @BindView(R.id.rvPedidos)    RecyclerView rvPedidos;
    @BindView(R.id.btnSolicitar)    Button btnSolicitar;

    private Retrofit retrofit;
    private RetrofitApi retrofitApi;
    private Disposable disposable;

    private List<Pedido1> pedidosList;
    private Pedidos1Adapter pedidos1Adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos1);
        ButterKnife.bind(this);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        retrofit = RetrofitClient.getInstance("");
        retrofitApi = retrofit.create(RetrofitApi.class);

        pedidosList = new ArrayList<>();
        pedidos1Adapter = new Pedidos1Adapter(pedidosList,this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);

        rvPedidos.setLayoutManager(mLayoutManager);
        rvPedidos.setItemAnimator(new DefaultItemAnimator());
        rvPedidos.addItemDecoration(new DividerItemDecoration(rvPedidos.getContext(), DividerItemDecoration.VERTICAL));
        rvPedidos.setAdapter(pedidos1Adapter);

        obtenerData();
        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pedidos1Activity.this,SolicitarPedidoActivity.class);
                intent.putExtra("usuario",usuario);
                startActivityForResult(intent,123);
            }
        });




    }

    private void obtenerData() {
        disposable = retrofitApi.obtenerPedidosUser(usuario.getId_usuario()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Pedido1>>() {
                    @Override
                    public void onNext(List<Pedido1> ped) {
                        pedidosList.clear();
                        pedidosList.addAll(ped);
                        pedidos1Adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("aca","error = " + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d("aca","complete");
                    }
                });
    }

    public void eliminarPedido(Pedido1 pedido1) {
        pedidosList.remove(pedido1);
        pedidos1Adapter.notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123  && resultCode == RESULT_OK && data!=null){
           obtenerData();
        }
    }

    public void marcarRecogido(Pedido1 pedido1) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());

        AndroidNetworking.post("http://conectemonos.com/pedido/marcarrecogido/{id_pedido}")
                .addPathParameter("id_pedido",pedido1.getId_pedido())
                .addBodyParameter("estado_id","4")
                .addPathParameter("fecha_actualizacion",currentDateandTime)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        finish();
                        startActivity(getIntent().putExtra("usuario",usuario).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        Toast.makeText(Pedidos1Activity.this, "Pedido recogido", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(Pedidos1Activity.this, "Error al marcar recogido", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
