package com.recykred.app.fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.recykred.app.R;
import com.recykred.app.SolicitarPedidoActivity;
import com.recykred.app.adapters.MaterialesAdapter;
import com.recykred.app.models.Material;
import com.recykred.app.models.Productos;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class MaterialesFragment extends DialogFragment {
    @BindView(R.id.rvMateriales)    RecyclerView rvMateriales;
    @BindView(R.id.btnCancelar)    Button btnCancelar;
    @BindView(R.id.btnAceptar)    Button btnAceptar;

    private MaterialesAdapter materialesAdapter;
    private List<Productos> productosList;
    private List<Material> materialList;

    private Retrofit retrofit;
    private RetrofitApi retrofitApi;
    private Disposable disposable;

    public MaterialesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_materiales, container, false);
        ButterKnife.bind(this,view);

        retrofit = RetrofitClient.getInstance("");
        retrofitApi = retrofit.create(RetrofitApi.class);
        productosList = new ArrayList<>();
        materialList = new ArrayList<>();
        materialesAdapter = new MaterialesAdapter(materialList,getContext(),productosList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        rvMateriales.setLayoutManager(mLayoutManager);
        rvMateriales.setItemAnimator(new DefaultItemAnimator());
        rvMateriales.addItemDecoration(new DividerItemDecoration(rvMateriales.getContext(), DividerItemDecoration.VERTICAL));
        rvMateriales.setAdapter(materialesAdapter);

        disposable = retrofitApi.obtenerMateriales().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Material>>() {
                    @Override
                    public void onNext(List<Material> mList) {
                        materialList.addAll(mList);
                        materialesAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(),"Error al obtener materiales",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                });


        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<productosList.size();i++){
                    ((SolicitarPedidoActivity)getContext()).agregarMaterial(productosList.get(i));
                }
                if(productosList.size()>0){
                    Toast.makeText(getContext(), "materiales agregados con exito", Toast.LENGTH_SHORT).show();
                }else{
                }
                dismiss();
            }
        });

        return view;
    }

}
