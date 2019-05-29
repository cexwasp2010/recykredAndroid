package com.recykred.app;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.recykred.app.adapters.DireccionesAdapter;
import com.recykred.app.fragments.DireccionFragment;
import com.recykred.app.models.Direccion;
import com.recykred.app.models.Usuario;
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

public class DireccionesActivity extends AppCompatActivity {

    @BindView(R.id.btnAgregarDireccion)    Button btnAgregarDireccion;
    @BindView(R.id.rvDirecciones)    RecyclerView rvDirecciones;
    @BindView(R.id.txtUsuario)    TextView txtUsuario;
    @BindView(R.id.btnContinuar) Button btnContinuar;
    private DireccionesAdapter direccionesAdapter;
    private List<Direccion> direccionList;

    private Usuario usuario;
    private Retrofit retrofit;
    private RetrofitApi retrofitApi;
    private Disposable disposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direcciones);
        ButterKnife.bind(this);
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        txtUsuario.setText("Hola "+usuario.getNombres());

        retrofit = RetrofitClient.getInstance("");
        retrofitApi = retrofit.create(RetrofitApi.class);

        direccionList = new ArrayList<>();
        direccionesAdapter = new DireccionesAdapter(direccionList,this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        rvDirecciones.setLayoutManager(mLayoutManager);
        rvDirecciones.setItemAnimator(new DefaultItemAnimator());
        rvDirecciones.addItemDecoration(new DividerItemDecoration(rvDirecciones.getContext(), DividerItemDecoration.VERTICAL));
        rvDirecciones.setAdapter(direccionesAdapter);

        disposable = retrofitApi.obtenerDirecciones(usuario.getId_usuario()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Direccion>>() {
                    @Override
                    public void onNext(List<Direccion> dir) {
                        direccionList.clear();
                        direccionesAdapter.notifyDataSetChanged();
                        direccionList.addAll(dir);
                        direccionesAdapter.notifyDataSetChanged();
                        Log.d("aca","aca");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("aca","aca2" + e);
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        Log.d("aca","aca3");
                        disposable.dispose();
                    }
                });







        btnAgregarDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("agregarDireccion");
                if (prev != null) {
                    ft.remove(prev);
                }
                DireccionFragment fragment = new DireccionFragment();
                Bundle bundle = new Bundle();
                bundle.putString("metodo","1");
                bundle.putString("idUsuario",usuario.getId_usuario());
                fragment.setArguments(bundle);
                ft.addToBackStack(null);
                DialogFragment dialogFragment = fragment;
                dialogFragment.show(ft, "agregarDireccion");
            }
        });


        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usuario.getId_rol().equalsIgnoreCase("1")){
                    Intent intent = new Intent(DireccionesActivity.this,Pedidos1Activity.class);
                    intent.putExtra("usuario",usuario);
                    startActivity(intent);
                }else if(usuario.getId_rol().equalsIgnoreCase("2")){
                    Intent intent = new Intent(DireccionesActivity.this,PanelActivity.class);
                    intent.putExtra("usuario",usuario);
                    startActivity(intent);
                }

            }
        });


    }

    public  void agregarDireccion(Direccion direccion){
        direccionList.add(direccion);
        direccionesAdapter.notifyDataSetChanged();
    }

    public void eliminarElemento(Direccion direccion){
        direccionList.remove(direccion);
        direccionesAdapter.notifyDataSetChanged();
    }
    public void editarElemento(Direccion direccion,int position){
        direccionList.remove(position);
        direccionesAdapter.notifyItemRemoved(position);
        direccionList.add(position,direccion);
        direccionesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
