package com.recykred.app;

import android.app.Activity;
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

import com.recykred.app.adapters.RecicladoresAdapter;
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

public class RecycladoresActivity extends AppCompatActivity {
    @BindView(R.id.btnAgregar)    Button btnAgregar;
    @BindView(R.id.rvRecicladores)    RecyclerView rvRecicladores;
    private RecicladoresAdapter recicladoresAdapter;
    private List<Usuario> recicladoresList;

    private Usuario usuario;
    private Retrofit retrofit;
    private RetrofitApi retrofitApi;
    private Disposable disposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycladores);
        ButterKnife.bind(this);
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        retrofit = RetrofitClient.getInstance("");
        retrofitApi = retrofit.create(RetrofitApi.class);




        recicladoresList = new ArrayList<>();
        recicladoresAdapter = new RecicladoresAdapter(recicladoresList,this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        rvRecicladores.setLayoutManager(mLayoutManager);
        rvRecicladores.setItemAnimator(new DefaultItemAnimator());
        rvRecicladores.addItemDecoration(new DividerItemDecoration(rvRecicladores.getContext(), DividerItemDecoration.VERTICAL));
        rvRecicladores.setAdapter(recicladoresAdapter);

        disposable = retrofitApi.obtenerRecicladores(usuario.getId_usuario()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Usuario>>() {
                    @Override
                    public void onNext(List<Usuario> usuarios) {
                        recicladoresList.addAll(usuarios);
                        recicladoresAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("aca2","error = " + e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent(RecycladoresActivity.this,AgregarReciclador.class);
                resultIntent.putExtra("usuario",usuario);
                startActivityForResult(resultIntent,1);

            }
        });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1  && resultCode == RESULT_OK && data!=null){
            Usuario reciclador = (Usuario) data.getSerializableExtra("reciclador");
            recicladoresList.add(reciclador);
            recicladoresAdapter.notifyDataSetChanged();
        }else if(requestCode == 2  && resultCode == RESULT_OK && data!=null){
            Usuario reciclador = (Usuario) data.getSerializableExtra("reciclador");
            int position = data.getIntExtra("position",0);
            recicladoresList.remove(position);
            recicladoresAdapter.notifyItemRemoved(position);
            recicladoresList.add(position,reciclador);
            recicladoresAdapter.notifyDataSetChanged();
        }
    }

    public void eliminarReciclador(Usuario usuario, int i) {
        disposable = retrofitApi.eliminarReciclador(usuario.getId_usuario()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        
        recicladoresList.remove(i);
        recicladoresAdapter.notifyItemRemoved(i);
        recicladoresAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Reciclador eliminado exitosamente", Toast.LENGTH_SHORT).show();
    }
}
