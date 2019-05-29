package com.recykred.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.recykred.app.models.Usuario;
import com.recykred.app.retrofit.RetrofitApi;
import com.recykred.app.retrofit.RetrofitClient;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class AgregarReciclador extends AppCompatActivity {
    @BindView(R.id.etNombres)    EditText etNombres;
    @BindView(R.id.etApellidos)    EditText etApellidos;
    @BindView(R.id.etEmail)    EditText etEmail;
    @BindView(R.id.etIdentificacion)    EditText etIdentificacion;
    @BindView(R.id.etCelular)    EditText etCelular;
    @BindView(R.id.btnRegistro)    Button btnRegistro;
    private Usuario usuario;
    private Retrofit retrofit;
    private RetrofitApi retrofitApi;
    private Disposable disposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_reciclador);
        ButterKnife.bind(this);
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        retrofit = RetrofitClient.getInstance("");
        retrofitApi = retrofit.create(RetrofitApi.class);


        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etNombres.getText().toString().trim().isEmpty() && !etApellidos.getText().toString().trim().isEmpty() && !etEmail.getText().toString().trim().isEmpty()
                && !etIdentificacion.getText().toString().trim().isEmpty() && !etCelular.getText().toString().trim().isEmpty()){
                    HashMap<String, String> map = new HashMap<>();
                    map.put("nombres", etNombres.getText().toString().trim());
                    map.put("apellidos",etApellidos.getText().toString().trim());
                    map.put("email",etEmail.getText().toString().trim());
                    map.put("numero_identificacion",etIdentificacion.getText().toString().trim());
                    map.put("numero_celular",etCelular.getText().toString().trim());
                    map.put("id_rol","3");
                    map.put("activo","1");
                    map.put("padre_id",usuario.getId_usuario());


                    disposable = retrofitApi.registrarUsuario(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableObserver<Usuario>() {
                                @Override
                                public void onNext(Usuario reciclador) {
                                    Intent output = new Intent();
                                    output.putExtra("reciclador",reciclador);
                                    setResult(RESULT_OK, output);
                                    Toast.makeText(AgregarReciclador.this,"Reciclador agregado con exito",Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.d("aca","error = " + e);
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent output = new Intent();
        setResult(RESULT_CANCELED, output);
        finish();
    }
}
