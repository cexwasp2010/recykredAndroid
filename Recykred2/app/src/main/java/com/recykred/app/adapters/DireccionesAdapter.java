package com.recykred.app.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.recykred.app.DireccionesActivity;
import com.recykred.app.R;
import com.recykred.app.fragments.DireccionFragment;
import com.recykred.app.models.Direccion;
import com.recykred.app.retrofit.RetrofitApi;
import com.recykred.app.retrofit.RetrofitClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class DireccionesAdapter extends RecyclerView.Adapter<DireccionesAdapter.MyViewHolder> {
    private List<Direccion> direccionList;
    private Context context;
    private Retrofit retrofit;
    private RetrofitApi retrofitApi;
    private Disposable disposable;

    public DireccionesAdapter(List<Direccion> direccionList, Context context) {
        this.direccionList = direccionList;
        this.context = context;
        retrofit = RetrofitClient.getInstance("");
        retrofitApi = retrofit.create(RetrofitApi.class);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_direcciones,viewGroup,false);
        return new DireccionesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.txtDireccion.setText(direccionList.get(i).getDireccion());
        myViewHolder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = ((DireccionesActivity)context).getSupportFragmentManager().beginTransaction();
                Fragment prev = ((DireccionesActivity)context).getSupportFragmentManager().findFragmentByTag("agregarDireccion");
                if (prev != null) {
                    ft.remove(prev);
                }
                DireccionFragment fragment = new DireccionFragment();
                Bundle bundle = new Bundle();
                bundle.putString("metodo","2");
                bundle.putString("idUsuario",direccionList.get(i).getUsuario_id_usuario());
                bundle.putString("direccion",direccionList.get(i).getDireccion());
                bundle.putInt("position",i);
                fragment.setArguments(bundle);
                ft.addToBackStack(null);
                DialogFragment dialogFragment = fragment;
                dialogFragment.show(ft, "agregarDireccion");
            }
        });
        myViewHolder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disposable = retrofitApi.eliminarDireccion(String.valueOf(direccionList.get(i).getId_direccion())).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<Object>() {
                            @Override
                            public void onNext(Object s) {

                                ((DireccionesActivity)context).eliminarElemento(direccionList.get(i));
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
        });
    }

    @Override
    public int getItemCount() {
        return direccionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtNombre)        TextView txtDireccion;
        @BindView(R.id.btnEditar)        Button btnEditar;
        @BindView(R.id.btnEliminar)        Button btnEliminar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
