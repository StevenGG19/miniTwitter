package com.steven.minitwitter.data;

import android.widget.Toast;

import com.steven.minitwitter.common.Constante;
import com.steven.minitwitter.common.MyApp;
import com.steven.minitwitter.common.SharedPreferencesManager;
import com.steven.minitwitter.retrofit.AuthClienteMinitwitter;
import com.steven.minitwitter.retrofit.AuthServicioMiniTwitter;
import com.steven.minitwitter.retrofit.respuesta.BorrarTweet;
import com.steven.minitwitter.retrofit.respuesta.Like;
import com.steven.minitwitter.retrofit.respuesta.Tweet;
import com.steven.minitwitter.retrofit.solicitud.SolicitudCrearTweet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {
    private AuthServicioMiniTwitter servicioMiniTwitter;
    private AuthClienteMinitwitter clienteMinitwitter;
    //MutableLiveData puede definir o devolver un LiveData que puede modificarse
    MutableLiveData<List<Tweet>> allTweets;
    MutableLiveData<List<Tweet>> favTweets;
    private String nombreUsuario;

    public TweetRepository() {
        clienteMinitwitter = AuthClienteMinitwitter.getInstance();
        servicioMiniTwitter = clienteMinitwitter.getAuthServicioMiniTwitter();
        allTweets = getAllTweets();
        nombreUsuario = SharedPreferencesManager.getStringValue(Constante.PREF_USUARIO);
    }

    public MutableLiveData<List<Tweet>> getAllTweets() {
        if (allTweets == null) {
            allTweets = new MutableLiveData<>();
        }
        Call<List<Tweet>> call = servicioMiniTwitter.getAllTweets();
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if (response.isSuccessful()) {
                    allTweets.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), "Algo ha salido mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
        return allTweets;
    }

    public MutableLiveData<List<Tweet>> getFavTweets() {
        if (favTweets == null) {
            favTweets = new MutableLiveData<>();
        }
        List<Tweet> newFavTweets = new ArrayList<>();
        Iterator itTweet = allTweets.getValue().iterator();

        while (itTweet.hasNext()) {
            Tweet elemento = (Tweet) itTweet.next();
            Iterator itLike = elemento.getLikes().iterator();
            boolean ec = false;
            while (itLike.hasNext() && !ec) {
                Like elementoLike = (Like) itLike.next();
                if (elementoLike.getUsername().equals(nombreUsuario)) {
                    ec = true;
                    newFavTweets.add(elemento);
                }
            }
        }
        favTweets.setValue(newFavTweets);
        return favTweets;
    }

    public void crearTweet(String mensaje) {
        SolicitudCrearTweet nuevoTweet = new SolicitudCrearTweet(mensaje);
        Call<Tweet> call = servicioMiniTwitter.crearTweet(nuevoTweet);
        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if (response.isSuccessful()) {
                    List<Tweet> listaClone = new ArrayList<>();
                    listaClone.add(response.body());
                    for (int i = 0; i < allTweets.getValue().size(); i++) {
                        listaClone.add(new Tweet(allTweets.getValue().get(i)));
                    }
                    allTweets.setValue(listaClone);
                } else {
                    Toast.makeText(MyApp.getContext(), "Algo ha salido mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void eliminarTweet(final int id) {
        Call<BorrarTweet> call = servicioMiniTwitter.eliminarTweet(id);
        call.enqueue(new Callback<BorrarTweet>() {
            @Override
            public void onResponse(Call<BorrarTweet> call, Response<BorrarTweet> response) {
                if (response.isSuccessful()) {
                    List<Tweet> clone = new ArrayList<>();
                    for (int i = 0; i < allTweets.getValue().size(); i++) {
                        if (allTweets.getValue().get(i).getId() != id) {
                            clone.add(new Tweet(allTweets.getValue().get(i)));
                        }
                    }
                    allTweets.setValue(clone);
                    getFavTweets();
                } else {
                    Toast.makeText(MyApp.getContext(), "Algo ha salido mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BorrarTweet> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void likeTweet(int idLike) {
        Call<Tweet> call = servicioMiniTwitter.tweetLike(idLike);
        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if (response.isSuccessful()) {
                    List<Tweet> listaClone = new ArrayList<>();
                    for (int i = 0; i < allTweets.getValue().size(); i++) {
                        if (allTweets.getValue().get(i).getId() == idLike) {
                            listaClone.add(response.body());
                        } else {
                            listaClone.add(new Tweet(allTweets.getValue().get(i)));
                        }
                    }
                    allTweets.setValue(listaClone);
                    getFavTweets();
                } else {
                    Toast.makeText(MyApp.getContext(), "Algo ha salido mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
