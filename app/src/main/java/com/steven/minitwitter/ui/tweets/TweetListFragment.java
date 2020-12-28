package com.steven.minitwitter.ui.tweets;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.steven.minitwitter.R;
import com.steven.minitwitter.common.Constante;
import com.steven.minitwitter.data.TweetViewModel;
import com.steven.minitwitter.retrofit.respuesta.Tweet;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class TweetListFragment extends Fragment {
    private int tweetListType;
    private RecyclerView recyclerView;
    private MyTweetRecyclerViewAdapter adapter;
    private List<Tweet> tweetList;
    private TweetViewModel tweetViewModel;
    private SwipeRefreshLayout swipe;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TweetListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TweetListFragment newInstance(int tweetListType) {
        TweetListFragment fragment = new TweetListFragment();
        Bundle args = new Bundle();
        args.putInt(Constante.TWEET_LIST_TYPE, tweetListType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweetViewModel = new ViewModelProvider(getActivity()).get(TweetViewModel.class);

        if (getArguments() != null) {
            tweetListType = getArguments().getInt(Constante.TWEET_LIST_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet, container, false);

        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.list);
        swipe = view.findViewById(R.id.swipe);
        swipe.setColorSchemeResources(R.color.azul);

        swipe.setOnRefreshListener(() -> {
            swipe.setRefreshing(true);
            if (tweetListType == Constante.TWEET_LIST_ALL) {
                cargarNuevosTweets();
            }else if (tweetListType == Constante.TWEET_LIST_FAV){
                cargarFavNuevosTweets();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new MyTweetRecyclerViewAdapter(getActivity(), tweetList);
        recyclerView.setAdapter(adapter);

        if (tweetListType == Constante.TWEET_LIST_ALL) {
            cargarTweets();
        }else if (tweetListType == Constante.TWEET_LIST_FAV){
            cargarFavTweets();
        }
        return view;
    }

    private void cargarFavNuevosTweets() {
        tweetViewModel.getNuevosFavTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList = tweets;
                swipe.setRefreshing(false);
                adapter.setDatos(tweetList);
                tweetViewModel.getNuevosFavTweets().removeObserver(this);
            }
        });
    }

    private void cargarFavTweets() {
        tweetViewModel.getFavTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList = tweets;
                adapter.setDatos(tweetList);
            }
        });
    }

    private void cargarTweets() {
        tweetViewModel.getTweets().observe(getActivity(), tweets -> {
            tweetList = tweets;
            adapter.setDatos(tweetList);
        });
    }

    private void cargarNuevosTweets() {
        tweetViewModel.getNuevosTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList = tweets;
                swipe.setRefreshing(false);
                adapter.setDatos(tweetList);
                tweetViewModel.getNuevosTweets().removeObserver(this);
            }
        });
    }
}