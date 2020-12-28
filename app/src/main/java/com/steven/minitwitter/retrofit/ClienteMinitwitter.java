package com.steven.minitwitter.retrofit;

import com.steven.minitwitter.common.Constante;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClienteMinitwitter {
    private static ClienteMinitwitter instance;
    private ServicioMiniTwitter servicioMiniTwitter;
    private Retrofit retrofit;

    public ClienteMinitwitter() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constante.MINITWITTER_BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build();
        servicioMiniTwitter = retrofit.create(ServicioMiniTwitter.class);
    }

    public static ClienteMinitwitter getInstance() {
        if (instance == null) {
            instance = new ClienteMinitwitter();
        }
        return instance;
    }

    public ServicioMiniTwitter getServicioMiniTwitter() {
        return servicioMiniTwitter;
    }
}
