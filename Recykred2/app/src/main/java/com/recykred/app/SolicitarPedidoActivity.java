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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.recykred.app.adapters.ProductosAdapter;
import com.recykred.app.fragments.MaterialesFragment;
import com.recykred.app.models.Direccion;
import com.recykred.app.models.Material;
import com.recykred.app.models.Pedidos;
import com.recykred.app.models.PedidosMateriales;
import com.recykred.app.models.Productos;
import com.recykred.app.models.Usuario;
import com.recykred.app.retrofit.RetrofitApi;
import com.recykred.app.retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class SolicitarPedidoActivity extends AppCompatActivity {
    @BindView(R.id.rvMateriales)    RecyclerView rvMateriales;
    @BindView(R.id.btnAgregarMateriales)    Button btnAgregarMateriales;
    @BindView(R.id.etSelectDireccion)    Spinner spDireccion;
    @BindView(R.id.etSelectFecha)    TextView etSelectFecha;
    @BindView(R.id.cbSeleccionar)    CheckBox cbGuia;
    @BindView(R.id.btnSolicitar)    Button btnSolicitar;

    private List<Productos> productosList;
    private Usuario usuario;
    private ProductosAdapter productosAdapter;

    private Retrofit retrofit;
    private RetrofitApi retrofitApi;
    private Disposable disposable;
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private List<Direccion> direcList;
    private int idDireccion;
    private Date dat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_pedido);
        ButterKnife.bind(this);
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        retrofit = RetrofitClient.getInstance("");
        retrofitApi = retrofit.create(RetrofitApi.class);
        direcList = new ArrayList<>();
        final ArrayList<String> categorias = new ArrayList<>();
        categorias.add("Selecciona Direccion");
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.textview_spinner_modify,categorias);
        spDireccion.setAdapter(adapter);



        disposable = retrofitApi.obtenerDirecciones(usuario.getId_usuario()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Direccion>>() {
                    @Override
                    public void onNext(List<Direccion> direccionList) {
                        direcList.addAll(direccionList);
                        for (int i=0;i<direccionList.size();i++){
                            categorias.add(direccionList.get(i).getDireccion());
                        }
                        adapter.notifyDataSetChanged();
                        if(categorias.size()==0){
                            categorias.add("No hay elementos");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        spDireccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(categorias.size()>0 && position>0){
                    idDireccion = direcList.get(position-1).getId_direccion();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        productosList = new ArrayList<>();
        productosAdapter = new ProductosAdapter(productosList,this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        rvMateriales.setLayoutManager(mLayoutManager);
        rvMateriales.setItemAnimator(new DefaultItemAnimator());
        rvMateriales.addItemDecoration(new DividerItemDecoration(rvMateriales.getContext(), DividerItemDecoration.VERTICAL));
        rvMateriales.setAdapter(productosAdapter);

        etSelectFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(listener)
                        .setInitialDate(new Date())
                        //.setMinDate(minDate)
                        //.setMaxDate(maxDate)
                        //.setIs24HourTime(true)
                        //.setTheme(SlideDateTimePicker.HOLO_DARK)
                        //.setIndicatorColor(Color.parseColor("#990000"))
                        .build()
                        .show();
            }
        });



        btnAgregarMateriales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("seleccionarMaterial");
                if (prev != null) {
                    ft.remove(prev);
                }
                MaterialesFragment fragment = new MaterialesFragment();

                ft.addToBackStack(null);
                DialogFragment dialogFragment = fragment;
                dialogFragment.show(ft, "seleccionarMaterial");
            }
        });


        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etSelectFecha.getText().toString().trim().isEmpty() && productosList.size()>0 && !spDireccion.getSelectedItem().toString().equalsIgnoreCase("No hay elementos")){
                    final Pedidos pedidos = new Pedidos();
                    pedidos.setDireccion_recogida(String.valueOf(idDireccion));
                    pedidos.setEstado_id("1");
                    pedidos.setFeha_recogida(etSelectFecha.getText().toString());
                    pedidos.setId_cliente(usuario.getId_usuario());
                    List<PedidosMateriales> pedidosMaterialesList = new ArrayList<>();
                    for(int i = 0;i<productosList.size();i++){
                        PedidosMateriales materiales = new PedidosMateriales();
                        materiales.setPeso(productosList.get(i).getPeso());
                        materiales.setMaterial_id_material(productosList.get(i).getId_material());
                        pedidosMaterialesList.add(materiales);
                    }
                    pedidos.setMateriales(pedidosMaterialesList);

                    Gson gson = new Gson();

                    String json = gson.toJson(pedidos);
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("direccion_recogida",pedidos.getDireccion_recogida());
                    map.put("estado_id",pedidos.getEstado_id());
                    map.put("fecha_creacion",mFormatter.format(new Date()));
                    map.put("feha_recogida",mFormatter.format(dat));
                    map.put("id_cliente",pedidos.getId_cliente());

                    try {
                        JSONObject obj = new JSONObject(json);
                        map.put("materiales",obj.getJSONArray("materiales").toString());
                        AndroidNetworking.post("http://conectemonos.com/pedido/solicitar")
                                .addUrlEncodeFormBodyParameter(map)
                                .build()
                                .getAsString(new StringRequestListener() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("aca","response = " + response);

                                        AndroidNetworking.post("http://192.168.1.61:8080/aca/EnviarNotificacionDesdePHP.php")
                                                .build().getAsString(new StringRequestListener() {
                                            @Override
                                            public void onResponse(String response) {
                                                Log.d("aca","response = " + response);
                                            }

                                            @Override
                                            public void onError(ANError anError) {
                                                Log.d("aca","error = " + anError.getErrorBody());
                                            }
                                        });

                                        Intent output = new Intent();
                                        setResult(RESULT_OK, output);
                                        finish();
                                        Toast.makeText(SolicitarPedidoActivity.this, "Pedido realizado con exito", Toast.LENGTH_SHORT).show();


                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        Log.d("aca","error = " + anError.getErrorBody());
                                    }
                                });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }






                }else{
                    Toast.makeText(SolicitarPedidoActivity.this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void agregarMaterial(Productos productos){
        productosList.add(productos);
        productosAdapter.notifyDataSetChanged();
    }

    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date)
        {
            dat = date;
            etSelectFecha.setText(mFormatter.format(date));

        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel()
        {

        }
    };


    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

}
