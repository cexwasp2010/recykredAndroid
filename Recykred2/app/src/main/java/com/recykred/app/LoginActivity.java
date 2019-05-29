package com.recykred.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;


import com.recykred.app.models.Usuario;
import com.recykred.app.models.Direccion;
import com.recykred.app.retrofit.RetrofitApi;
import com.recykred.app.retrofit.RetrofitClient;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.etUsername)    EditText etUsername;
    @BindView(R.id.etPassword)    EditText etPassword;
    @BindView(R.id.btnLogin)    Button btnLogin;
    @BindView(R.id.btnRegistro)    Button btnRegistro;
    private Retrofit retrofit;
    private RetrofitApi retrofitApi;
    private Disposable disposable;
    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        retrofit = RetrofitClient.getInstance("");
        retrofitApi = retrofit.create(RetrofitApi.class);

        verificarPermisos();

        FirebaseMessaging.getInstance().unsubscribeFromTopic("devices")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Log.d("aca","incorrecto");
                        }else{
                            Log.d("aca","subscrito");
                        }
                    }
                });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etUsername.getText().toString().trim().isEmpty() && !etPassword.getText().toString().trim().isEmpty()){
                    disposable = retrofitApi.obtenerUsuario(etUsername.getText().toString().trim()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableObserver<Usuario>() {
                                @Override
                                public void onNext(final Usuario usuario) {
                                    if(etPassword.getText().toString().trim().equalsIgnoreCase(usuario.getContrasenia())){
                                            disposable = retrofitApi.obtenerDirecciones(usuario.getId_usuario()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                                    .subscribeWith(new DisposableObserver<List<Direccion>>() {
                                                        @Override
                                                        public void onNext(List<Direccion> direccionList) {
                                                            if(usuario.getId_rol().equalsIgnoreCase("1")){
                                                                FirebaseMessaging.getInstance().unsubscribeFromTopic("devices")
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (!task.isSuccessful()) {
                                                                                    Log.d("aca","incorrecto");
                                                                                }else{
                                                                                    Log.d("aca","subscrito");
                                                                                }
                                                                            }
                                                                        });
                                                            }else if(usuario.getId_rol().equalsIgnoreCase("2")){
                                                                FirebaseMessaging.getInstance().subscribeToTopic("devices")
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (!task.isSuccessful()) {
                                                                                    Log.d("aca","incorrecto");
                                                                                }else{
                                                                                    Log.d("aca","subscrito");
                                                                                }
                                                                            }
                                                                        });

                                                            }
                                                            Intent intent = new Intent(LoginActivity.this,DireccionesActivity.class);
                                                            intent.putExtra("usuario",usuario);
                                                            startActivity(intent);
                                                        }

                                                        @Override
                                                        public void onError(Throwable e) {
                                                            if(e.getMessage().equalsIgnoreCase("HTTP 404 No se econctro el reurso solicitado")){
                                                                Intent intent = new Intent(LoginActivity.this,DireccionesActivity.class);
                                                                intent.putExtra("usuario",usuario);
                                                                startActivity(intent);
                                                            }else{
                                                                Toast.makeText(LoginActivity.this, "Error con la conexion, revise su internet", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }

                                                        @Override
                                                        public void onComplete() {
                                                            Log.d("aca","complete");

                                                        }
                                                    });

                                    }else{
                                        etPassword.requestFocus();
                                        etPassword.setError("contraseña incorrecta");
                                    }
                                }   

                                @Override
                                public void onError(Throwable e) {

                                    if(e.getMessage().equalsIgnoreCase("HTTP 404 No se econctro el reurso solicitado")){
                                        etUsername.requestFocus();
                                        etUsername.setError("Usuario no existe");
                                        Toast.makeText(LoginActivity.this, "Error de inicio de sesion", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(LoginActivity.this, "Error con la conexion, revise su internet", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onComplete() {

                                }
                            });

                }
                if(etPassword.getText().toString().isEmpty()){
                    etPassword.requestFocus();
                    etPassword.setError("Ingrese dato");
                }
                if(etUsername.getText().toString().isEmpty()){
                    etUsername.requestFocus();
                    etUsername.setError("Ingrese dato");
                }
            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegistroActivity.class);
                startActivity(intent);
            }
        });

    }

    private void verificarPermisos() {
        //check if the permission is not granted
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            //if the permission is not been granted then check if the user has denied the permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS))
            {

            }
            else
            {
                //a pop up will appear asking for required permission i.e Allow or Deny
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
            }
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable!=null){
            disposable.dispose();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        //will check the requestCode
        switch(requestCode)
        {
            case MY_PERMISSIONS_REQUEST_RECEIVE_SMS:
            {
                //check whether the length of grantResults is greater than 0 and is equal to PERMISSION_GRANTED
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    //Now broadcastreceiver will work in background
                    Toast.makeText(this, "Permisos aceptados!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(this, "No se podran acceder a los mensajes", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
                }
            }
        }
    }

}
