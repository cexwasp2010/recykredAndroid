package com.recykred.app.fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.recykred.app.DireccionesActivity;
import com.recykred.app.R;
import com.recykred.app.models.Direccion;
import com.recykred.app.retrofit.RetrofitApi;
import com.recykred.app.retrofit.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class DireccionFragment extends DialogFragment {

    @BindView(R.id.btnCerrar)    ImageView btnCerrar;
    @BindView(R.id.etDireccion)    EditText etDireccion;
    @BindView(R.id.txtTexto)    TextView txtTexto;
    @BindView(R.id.btnAgregar)    Button btnAgregar;
    private String metodo;
    private String idUsuario;

    private Retrofit retrofit;
    private RetrofitApi retrofitApi;
    private Disposable disposable;


    public DireccionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view =inflater.inflate(R.layout.fragment_direccion, container, false);
        ButterKnife.bind(this,view);
        setCancelable(false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        metodo = getArguments().getString("metodo");
        idUsuario = getArguments().getString("idUsuario");

        retrofit = RetrofitClient.getInstance("");
        retrofitApi = retrofit.create(RetrofitApi.class);



        switch (metodo){
            case "1":
                txtTexto.setText("Agregar Direccion");
                btnAgregar.setText("Agregar");
                break;
            case "2":
                txtTexto.setText("Editar Direccion");
                btnAgregar.setText("Editar");
                etDireccion.setText(getArguments().getString("direccion"));
                break;
        }


        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etDireccion.getText().toString().trim().isEmpty()){
                    HashMap<String,String> map = new HashMap<>();
                    map.put("usuario_id_usuario",idUsuario);
                    map.put("direccion",etDireccion.getText().toString().trim());
                    if(metodo.equalsIgnoreCase("1")){
                        retrofitApi.agregarDireccion(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(new DisposableObserver<Direccion>() {
                                    @Override
                                    public void onNext(Direccion direccion) {
                                        ((DireccionesActivity) getContext()).agregarDireccion(direccion);
                                        Toast.makeText(getContext(), "Direccion Agregada con exito", Toast.LENGTH_SHORT).show();
                                        dismiss();

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(getContext(), "Error al agregar direccion", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onComplete() {
                                        Log.d("aca","Complete ");
                                    }
                                });
                    }else{

                        disposable = retrofitApi.updateDireccion(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(new DisposableObserver<Direccion>() {
                                    @Override
                                    public void onNext(Direccion direccion) {

                                        ((DireccionesActivity)getContext()).editarElemento(direccion,getArguments().getInt("position"));
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.d("aca","error = " + e);
                                    }

                                    @Override
                                    public void onComplete() {
                                        Log.d("aca","completado ");
                                    }
                                });
                    }

                }
            }
        });


        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        return view;
    }

}
