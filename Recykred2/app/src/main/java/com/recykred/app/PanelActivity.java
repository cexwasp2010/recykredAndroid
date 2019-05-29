package com.recykred.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.recykred.app.models.Usuario;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PanelActivity extends AppCompatActivity {
    @BindView(R.id.btnRecicladores)    Button btnRecicladores;
    @BindView(R.id.btnPedidos)    Button btnPedidos;
    private Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);
        ButterKnife.bind(this);
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        btnPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PanelActivity.this,Pedidos2Activity.class);
                intent.putExtra("usuario",usuario);
                startActivity(intent);
            }
        });

        btnRecicladores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PanelActivity.this,RecycladoresActivity.class);
                intent.putExtra("usuario",usuario);
                startActivity(intent);
            }
        });


    }
}
