package com.recykred.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.recykred.app.adapters.MaterialAdapter;
import com.recykred.app.models.PedidoDetalles;
import com.recykred.app.models.PedidoDetallesMateriales;
import com.recykred.app.retrofit.RetrofitApi;
import com.recykred.app.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class PedidosDetalles extends AppCompatActivity {

    @BindView(R.id.etDireccion)    TextView etDireccion;
    @BindView(R.id.etFecha)    TextView etFecha;
    @BindView(R.id.etSolicitado)    TextView etSolicitado;
    @BindView(R.id.etFechaCreacion)    TextView etFechaCreacion;
    @BindView(R.id.etTomadoPor)    TextView etTomadoPor;
    @BindView(R.id.etRecogidoPor)    TextView etRecogidoPor;
    @BindView(R.id.etEstado)    TextView etEstado;
    @BindView(R.id.rvMateriales)    RecyclerView rvMateriales;


    private String idPedido;
    private Retrofit retrofit;
    private RetrofitApi retrofitApi;
    private Disposable disposable;
    private MaterialAdapter adapter;
    private List<PedidoDetallesMateriales> pedidosMaterialesList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_detalles);
        ButterKnife.bind(this);
        idPedido = getIntent().getStringExtra("idPedido");

        retrofit = RetrofitClient.getInstance("");
        retrofitApi = retrofit.create(RetrofitApi.class);

        pedidosMaterialesList = new ArrayList<>();
        adapter = new MaterialAdapter(pedidosMaterialesList,this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);

        rvMateriales.setLayoutManager(mLayoutManager);
        rvMateriales.setItemAnimator(new DefaultItemAnimator());
        rvMateriales.addItemDecoration(new DividerItemDecoration(rvMateriales.getContext(), DividerItemDecoration.VERTICAL));
        rvMateriales.setAdapter(adapter);


        disposable = retrofitApi.obtenerPedidoMaterial(idPedido).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<PedidoDetalles>() {
                    @Override
                    public void onNext(PedidoDetalles pedidos) {
                       etDireccion.setText(pedidos.getDireccion_recogida());
                       etFecha.setText(pedidos.getFeha_recogida());
                       etEstado.setText(pedidos.getEstado());
                       etFechaCreacion.setText(pedidos.getFecha_creacion());
                       etRecogidoPor.setText(pedidos.getId_centro_acopio());
                       etSolicitado.setText(pedidos.getId_cliente());
                       etTomadoPor.setText(pedidos.getId_reciclador());
                        pedidosMaterialesList.addAll(pedidos.getMateriales());
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("aca","e " + e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }
}
