package com.steven.minitwitter.retrofit;

import com.steven.minitwitter.retrofit.respuesta.BorrarTweet;
import com.steven.minitwitter.retrofit.respuesta.PerfilUsuario;
import com.steven.minitwitter.retrofit.respuesta.Tweet;
import com.steven.minitwitter.retrofit.respuesta.UploadFoto;
import com.steven.minitwitter.retrofit.solicitud.SolicitudCrearTweet;
import com.steven.minitwitter.retrofit.solicitud.SolicitudPerfilUsuario;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AuthServicioMiniTwitter {

    //tweets
    @GET("tweets/all")
    Call<List<Tweet>> getAllTweets();

    @POST("tweets/create")
    Call<Tweet> crearTweet(@Body SolicitudCrearTweet crearTweet);

    @POST("tweets/like/{idLike}")
    Call<Tweet> tweetLike(@Path("idLike") int idTweet);

    @DELETE("tweets/{idTweet}")
    Call<BorrarTweet> eliminarTweet(@Path("idTweet") int idTweet);

    //perfil de usuario
    @GET("users/profile")
    Call<PerfilUsuario> getDatosUsuario();

    @PUT("users/profile")
    Call<PerfilUsuario> updatePerfil(@Body SolicitudPerfilUsuario solicitud);

    @Multipart
    @POST("users/uploadprofilephoto")
    Call<UploadFoto> uploadFile(@Part("file\"; filename=\"photo.jpeg\" ")RequestBody file);

}
