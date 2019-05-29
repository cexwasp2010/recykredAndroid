package com.recykred.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.recykred.app.models.Usuario;
import com.recykred.app.retrofit.RetrofitApi;
import com.recykred.app.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class RegistroActivity extends AppCompatActivity {
    @BindView(R.id.etNombres)    EditText etNombres;
    @BindView(R.id.etApellidos)    EditText etApellidos;
    @BindView(R.id.etEmail)    EditText etEmail;
    @BindView(R.id.etContraseña)    EditText etContraseña;
    @BindView(R.id.etCelular)    EditText etCelular;
    @BindView(R.id.etIdentificacion)    EditText etIdentificacion;
    @BindView(R.id.spRol)    Spinner spRol;
    @BindView(R.id.btnRegistro)    Button btnRegistro;
    private Retrofit retrofit;
    private RetrofitApi retrofitApi;
    private Disposable disposable;
    private String rol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        ButterKnife.bind(this);

        retrofit = RetrofitClient.getInstance("");
        retrofitApi = retrofit.create(RetrofitApi.class);

        final ArrayList<String> categorias = new ArrayList<>();
        categorias.add("Cliente");
        categorias.add("Centro de Acopio");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.textview_spinner_modify,categorias);
        spRol.setAdapter(adapter);
        rol = "1";



        spRol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    rol = "1";
                }else{
                    rol = "2";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(!estaVacio(etNombres) &&!estaVacio(etApellidos) && !estaVacio(etEmail) &&
                    !estaVacio(etContraseña) && !estaVacio(etCelular) && !estaVacio(etIdentificacion)){
                HashMap<String, String> usuario = new HashMap<>();
                usuario.put("nombres", etNombres.getText().toString().trim());
                usuario.put("apellidos",etApellidos.getText().toString().trim());
                usuario.put("email",etEmail.getText().toString().trim());
                usuario.put("contrasenia",etContraseña.getText().toString().trim());
                usuario.put("numero_identificacion",etIdentificacion.getText().toString().trim());
                usuario.put("numero_celular",etCelular.getText().toString().trim());
                usuario.put("id_rol",rol);

                disposable = retrofitApi.registrarUsuario(usuario).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<Usuario>() {
                            @Override
                            public void onNext(Usuario usuario) {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(RegistroActivity.this, "Error no se puede registrar", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onComplete() {
                                Toast.makeText(RegistroActivity.this, "Usuario Registrado con exito", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });


            }
            if(estaVacio(etIdentificacion)){
                etIdentificacion.requestFocus();
                etIdentificacion.setError("Ingrese dato");
            }
            if(estaVacio(etCelular)){
                etCelular.requestFocus();
                etCelular.setError("Ingrese dato");
                }
                if(estaVacio(etContraseña)){
                    etContraseña.requestFocus();
                    etContraseña.setError("Ingrese dato");
                }

                if(estaVacio(etEmail)){
                    etEmail.requestFocus();
                    etEmail.setError("Ingrese dato");
                }

                if(estaVacio(etApellidos)){
                    etApellidos.requestFocus();
                    etApellidos.setError("Ingrese dato");
                }
                if(estaVacio(etNombres)){
                    etNombres.requestFocus();
                    etNombres.setError("Ingrese dato");
                }

            }
        });
    }

    private boolean estaVacio(EditText editText) {
        if(editText.getText().toString().trim().isEmpty()){
            return true;
        }else{
            return false;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable!=null){
            disposable.dispose();
        }
    }
}
