package com.steven.minitwitter.retrofit;

import com.steven.minitwitter.common.Constante;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthClienteMinitwitter {
    private static AuthClienteMinitwitter instance;
    private AuthServicioMiniTwitter servicioMiniTwitter;
    private Retrofit retrofit;

    public AuthClienteMinitwitter() {
        //incluir en la cabezera de la petición el TOKEN de acceso
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(new AuthInterceptor());
        OkHttpClient client = okHttpClient.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constante.MINITWITTER_BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        servicioMiniTwitter = retrofit.create(AuthServicioMiniTwitter.class);
    }

    public static AuthClienteMinitwitter getInstance() {
        if (instance == null) {
            instance = new AuthClienteMinitwitter();
        }
        return instance;
    }

    public AuthServicioMiniTwitter getAuthServicioMiniTwitter() {
        return servicioMiniTwitter;
    }
}
