package com.steven.minitwitter.ui.tweets;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.steven.minitwitter.R;
import com.steven.minitwitter.common.Constante;
import com.steven.minitwitter.data.TweetViewModel;

public class BottomModalViewFragment extends BottomSheetDialogFragment {

    private TweetViewModel mViewModel;
    private int idTweetEliminar;

    public static BottomModalViewFragment newInstance(int idTweet) {
        BottomModalViewFragment bt = new BottomModalViewFragment();
        Bundle arg = new Bundle();
        arg.putInt(Constante.ARG_TWEET_ID, idTweet);
        bt.setArguments(arg);
        return bt;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idTweetEliminar = getArguments().getInt(Constante.ARG_TWEET_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_modal_view_fragment, container, false);
        final NavigationView nav = v.findViewById(R.id.navigationView);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_delete) {
                    mViewModel.eliminar(idTweetEliminar);
                    //obtiene la referencia al cuadro de dialogo que se abrio y lo cierra
                    getDialog().dismiss();
                    return true;
                }
                return false;
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(TweetViewModel.class);
        // TODO: Use the ViewModel
    }

}