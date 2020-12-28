package com.steven.minitwitter.data;

import android.app.Application;
import android.content.Context;

import com.steven.minitwitter.retrofit.respuesta.Tweet;
import com.steven.minitwitter.ui.tweets.BottomModalViewFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TweetViewModel extends AndroidViewModel {
    private TweetRepository tweetRepository;
    private LiveData<List<Tweet>> tweets;
    private LiveData<List<Tweet>> favTweets;

    public TweetViewModel(@NonNull Application application) {
        super(application);
        tweetRepository = new TweetRepository();
        tweets = tweetRepository.getAllTweets();
    }

    public LiveData<List<Tweet>> getTweets() {return tweets;}

    public LiveData<List<Tweet>> getFavTweets() {
        favTweets = tweetRepository.getFavTweets();
        return favTweets;
    }

    public LiveData<List<Tweet>> getNuevosTweets() {
        tweets = tweetRepository.getAllTweets();
        return tweets;
    }

    public LiveData<List<Tweet>> getNuevosFavTweets() {
        getNuevosTweets();
        return getFavTweets();
    }

    public void insertarTweet(String mensaje) {
        tweetRepository.crearTweet(mensaje);
    }

    public void likeTweet(int idTweet) {
        tweetRepository.likeTweet(idTweet);
    }

    public void eliminar(int id) {
        tweetRepository.eliminarTweet(id);
    }

    public void abrirDialogTweetMenu(Context ctx, int id) {
        BottomModalViewFragment frag = BottomModalViewFragment.newInstance(id);
        frag.show(((AppCompatActivity)ctx).getSupportFragmentManager(), "BottomModalViewFragment");
    }
}
