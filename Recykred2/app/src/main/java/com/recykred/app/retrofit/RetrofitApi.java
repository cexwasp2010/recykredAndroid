package com.recykred.app.retrofit;


import com.recykred.app.models.Direccion;
import com.recykred.app.models.Material;
import com.recykred.app.models.Pedido1;
import com.recykred.app.models.PedidoDetalles;
import com.recykred.app.models.Pedidos;
import com.recykred.app.models.PedidosMateriales;
import com.recykred.app.models.Usuario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitApi {

    @POST("usuario/add")
    @FormUrlEncoded
    Observable<Usuario> registrarUsuario(@FieldMap Map<String,String> usuario);

    @GET("usuario/getauth/{input}")
    Observable<Usuario> obtenerUsuario(@Path("input") String email);

    @GET("usuario/getdirecciones/{input}")
    Observable<List<Direccion>> obtenerDirecciones(@Path("input") String id);

    @POST("usuario/adddireccion")
    @FormUrlEncoded
    Observable<Direccion> agregarDireccion(@FieldMap Map<String,String> direccion);

    @POST("Usuario/deletedireccion/{input}")
    Observable<Object> eliminarDireccion(@Path("input") String id);

    @POST("Usuario/updatedireccion")
    @FormUrlEncoded
    Observable<Direccion> updateDireccion(@FieldMap Map<String,String> direccion);

    @GET("material/all")
    Observable<List<Material>> obtenerMateriales();


    @POST("pedido/solicitar")
    @FormUrlEncoded
    Observable<String> insertarPedido(@Field("id_cliente") String idCliente);

    @GET("pedido/getpedidoscliente/{input}")
    Observable<List<Pedido1>> obtenerPedidosUser(@Path("input") String idCliente);

    @GET("Usuario/getusuariosregistrados/{input}")
    Observable<List<Usuario>> obtenerRecicladores(@Path("input") String id);


    @POST("usuario/delete/{input}")
    Observable<String> eliminarReciclador(@Path("input") String idReciclador);

    @POST("pedido/getpedidoscentroacopio/{input}")
    Observable<List<Pedido1>> obtenerPedidosCentro(@Path("input") String idCentro);

    @GET("pedido/get/{input}")
    Observable<PedidoDetalles> obtenerPedidoMaterial(@Path("input") String idPedido);


}
