package com.recykred.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.recykred.app.models.Usuario;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DatosReciclador extends AppCompatActivity {
    @BindView(R.id.txtNombre)    TextView txtNombre;
    @BindView(R.id.txtApellidos)    TextView txtApellidos;
    @BindView(R.id.txtEmail)    TextView txtEmail;
    @BindView(R.id.txtNumIdent)    TextView txtNumIdent;
    @BindView(R.id.txtEstado)    TextView txtEstado;
    @BindView(R.id.txtCelular)    TextView txtCelular;
    @BindView(R.id.txtId) TextView txtId;

    @BindView(R.id.etNombre)    EditText etNombre;
    @BindView(R.id.etApellidos)    EditText etApellidos;
    @BindView(R.id.etEmail)    EditText etEmail;
    @BindView(R.id.etNumIdent)    EditText etNumIdent;
    @BindView(R.id.etEstado)    EditText etEstado;
    @BindView(R.id.etCelular)    EditText etCelular;
    @BindView(R.id.etId) EditText etId;

    @BindView(R.id.btnListado)    Button btnListado;
    @BindView(R.id.btnEditar)    Button btnEditar;

    private Usuario reciclador;
    private  int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_reciclador);
        ButterKnife.bind(this);
        reciclador = (Usuario) getIntent().getSerializableExtra("reciclador");
        position = getIntent().getIntExtra("position",0);

        txtNombre.setText(reciclador.getNombres());
        txtApellidos.setText(reciclador.getApellidos());
        txtEmail.setText(reciclador.getEmail());
        txtNumIdent.setText(reciclador.getNumero_identificacion());
        txtId.setText(reciclador.getId_usuario());
        if(reciclador.getActivo().equalsIgnoreCase("1")){
            txtEstado.setText("Activo");
        }
        txtCelular.setText(reciclador.getNumero_celular());


        btnListado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent output = new Intent();
                output.putExtra("position",position);
                output.putExtra("reciclador",reciclador);
                setResult(RESULT_OK, output);
                finish();
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(btnEditar.getText().toString().trim().equalsIgnoreCase("Editar")){
                   etNombre.setVisibility(View.VISIBLE);
                   etNombre.setText(txtNombre.getText());
                   etApellidos.setVisibility(View.VISIBLE);
                   etApellidos.setText(txtApellidos.getText());
                   etEmail.setVisibility(View.VISIBLE);
                   etEmail.setText(txtEmail.getText());
                   etNumIdent.setVisibility(View.VISIBLE);
                   etNumIdent.setText(txtNumIdent.getText());
                   etEstado.setVisibility(View.VISIBLE);
                   etEstado.setText(txtEstado.getText());
                   etCelular.setVisibility(View.VISIBLE);
                   etCelular.setText(txtCelular.getText());


                   btnListado.setVisibility(View.GONE);
                   btnEditar.setText("Guardar Cambios");
               }else{
                   btnListado.setVisibility(View.VISIBLE);
                   btnEditar.setText("Editar");
                   etNombre.setVisibility(View.GONE);
                   txtNombre.setText(etNombre.getText());
                   etApellidos.setVisibility(View.GONE);
                   txtApellidos.setText(etApellidos.getText());
                   etEmail.setVisibility(View.GONE);
                   txtEmail.setText(etEmail.getText());
                   etNumIdent.setVisibility(View.GONE);
                   txtNumIdent.setText(etNumIdent.getText());
                   etEstado.setVisibility(View.GONE);
                   txtEstado.setText(etEstado.getText());
                   etCelular.setVisibility(View.GONE);
                   txtCelular.setText(etCelular.getText());
                   reciclador.setNombres(etNombre.getText().toString());
                   reciclador.setApellidos(etApellidos.getText().toString());
                   reciclador.setEmail(etEmail.getText().toString());
                   reciclador.setNumero_identificacion(etNumIdent.getText().toString());
                   reciclador.setNumero_celular(etCelular.getText().toString());


                   AndroidNetworking.post("http://conectemonos.com/usuario/update")
                           .addBodyParameter("nombres",reciclador.getNombres())
                           .addBodyParameter("apellidos",reciclador.getApellidos())
                           .addBodyParameter("email",reciclador.getEmail())
                           .addBodyParameter("contrasenia",reciclador.getContrasenia())
                           .addBodyParameter("numero_identificacion",reciclador.getNumero_identificacion())
                           .addBodyParameter("numero_celular",reciclador.getNumero_celular())
                           .addBodyParameter("id_rol",reciclador.getId_rol())
                           .addBodyParameter("padre_id",reciclador.getPadre_id())
                           .build()
                           .getAsString(new StringRequestListener() {
                               @Override
                               public void onResponse(String response) {
                                   Log.d("aca","response = " + response);
                               }

                               @Override
                               public void onError(ANError anError) {
                                Log.d("aca","anerror=  " + anError.getErrorBody());
                               }
                           });





               }
            }
        });

    }
}
