package com.recykred.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.recykred.app.adapters.Pedidos1Adapter;
import com.recykred.app.adapters.Pedidos2Adapter;
import com.recykred.app.models.Pedido1;
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

public class Pedidos2Activity extends AppCompatActivity {
    private Usuario usuario;
    @BindView(R.id.rvPedidos)    RecyclerView rvPedidos;


    private Retrofit retrofit;
    private RetrofitApi retrofitApi;
    private Disposable disposable;

    private List<Pedido1> pedidosList;
    private Pedidos2Adapter pedidos2Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos2);
        ButterKnife.bind(this);
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        retrofit = RetrofitClient.getInstance("");
        retrofitApi = retrofit.create(RetrofitApi.class);

        pedidosList = new ArrayList<>();
        pedidos2Adapter =new Pedidos2Adapter(pedidosList,this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);

        rvPedidos.setLayoutManager(mLayoutManager);
        rvPedidos.setItemAnimator(new DefaultItemAnimator());
        rvPedidos.addItemDecoration(new DividerItemDecoration(rvPedidos.getContext(), DividerItemDecoration.VERTICAL));
        rvPedidos.setAdapter(pedidos2Adapter);



        disposable = retrofitApi.obtenerPedidosCentro(usuario.getId_usuario()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Pedido1>>() {
                    @Override
                    public void onNext(List<Pedido1> pedido1s) {
                        pedidosList.addAll(pedido1s);
                        pedidos2Adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });



    }

    public void tomarPedido(final Pedido1 pedido1) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        Log.d("aca","peido = " + pedido1.getId_pedido());
        AndroidNetworking.post("http://conectemonos.com/pedido/tomar/{input}")
                .addPathParameter("input",pedido1.getId_pedido())
                .addBodyParameter("id_centro_acopio",usuario.getId_usuario())
                .addBodyParameter("estado_id","2")
                .addBodyParameter("fecha_actualizacion",currentDateandTime)
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                Log.d("aca","respuesta = " + response);



                disposable = retrofitApi.obtenerRecicladores(usuario.getId_usuario()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<Usuario>>() {
                            @Override
                            public void onNext(List<Usuario> usuarios) {
                                for(int i=0;i<usuarios.size(); i++){
                                    Log.d("aca","usuario = " + usuarios.get(i).getNumero_celular());
                                    AndroidNetworking.post("https://rest.nexmo.com/sms/json")
                                            .addHeaders("Content-Type","application/x-www-form-urlencoded")
                                            .addBodyParameter("api_key","f4fa3858")
                                            .addBodyParameter("api_secret","iCqZxpmgFOTlPLB7")
                                            .addBodyParameter("from","user")
                                            .addBodyParameter("text","El pedido " +pedido1.getId_pedido() + " esta Disponible para recoleccion el "+
                                                    pedido1.getFecha_creacion() +" en la calle " + pedido1.getDireccion_recogida() + " por favor responda " +
                                                    pedido1.getId_pedido()+"RC+ tu codigo al 3169826425")
                                            .addBodyParameter("to","+"+usuarios.get(i).getNumero_celular())

                                            .build().getAsString(new StringRequestListener() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d("aca","respuesta = " + response);
                                            finish();
                                            startActivity(getIntent().putExtra("usuario",usuario).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

                                        }

                                        @Override
                                        public void onError(ANError anError) {
                                            Log.d("aca","error = " + anError.getErrorBody());
                                        }
                                    });


                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("aca2","error = " + e);
                            }

                            @Override
                            public void onComplete() {

                            }
                        });



            }

            @Override
            public void onError(ANError anError) {
                Log.d("aca","error = " + anError);
            }
        });


    }

    public void marcarRecibido(Pedido1 pedido1) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        AndroidNetworking.post("http://conectemonos.com/pedido/maracarrecibido/{id_pedido}")
                .addPathParameter("id_pedido",pedido1.getId_pedido())
                .addBodyParameter("estado_id","5")
                .addBodyParameter("fecha_actualizacion",currentDateandTime)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        finish();
                        startActivity(getIntent().putExtra("usuario",usuario).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}
