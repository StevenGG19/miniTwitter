package com.steven.minitwitter.retrofit;

import com.steven.minitwitter.retrofit.respuesta.RespuestaAuth;
import com.steven.minitwitter.retrofit.solicitud.SolicitudAcceso;
import com.steven.minitwitter.retrofit.solicitud.SolicitudRegistro;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServicioMiniTwitter {
    @POST("auth/login")
    Call<RespuestaAuth> acceso(@Body SolicitudAcceso solicitudAcceso);

    @POST("auth/signup")
    Call<RespuestaAuth> registro(@Body SolicitudRegistro solicitudRegistro);
}
