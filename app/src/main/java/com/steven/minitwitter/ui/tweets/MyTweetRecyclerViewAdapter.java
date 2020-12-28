package com.steven.minitwitter.ui.tweets;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.steven.minitwitter.R;
import com.steven.minitwitter.common.Constante;
import com.steven.minitwitter.common.SharedPreferencesManager;
import com.steven.minitwitter.data.TweetViewModel;
import com.steven.minitwitter.retrofit.respuesta.Like;
import com.steven.minitwitter.retrofit.respuesta.Tweet;

import java.util.List;

public class MyTweetRecyclerViewAdapter extends RecyclerView.Adapter<MyTweetRecyclerViewAdapter.ViewHolder> {

    private List<Tweet> mValues;
    private Context ctx;
    private String usuario;
    private TweetViewModel viewModel;

    public MyTweetRecyclerViewAdapter(Context context, List<Tweet> items) {
        ctx = context;
        mValues = items;
        usuario = SharedPreferencesManager.getStringValue(Constante.PREF_USUARIO);
        viewModel = new ViewModelProvider((FragmentActivity)ctx).get(TweetViewModel.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tweet_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mValues != null) {
            holder.mItem = mValues.get(position);
            holder.txtNombre.setText(holder.mItem.getUser().getUsername());
            holder.txtContenido.setText(holder.mItem.getMensaje());
            holder.txtLike.setText(String.valueOf(holder.mItem.getLikes().size()));

            if (holder.mItem.getUser().getUsername().equals(usuario)) {
                holder.imgDialog.setVisibility(View.VISIBLE);
            }else {
                holder.imgDialog.setVisibility(View.GONE);
            }

            holder.imgDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.abrirDialogTweetMenu(ctx, holder.mItem.getId());
                }
            });
            String foto = holder.mItem.getUser().getPhotoUrl();
            if (!foto.equals("")) {
                Glide.with(ctx).load(Constante.MINITWITTER_FILE_URL + foto)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .into(holder.imgFotoPerfil);
            }else {
                Glide.with(ctx).load(R.drawable.ic_baseline_account_circle_24)
                        .into(holder.imgFotoPerfil);
            }

            Glide.with(ctx).load(R.drawable.ic_like).into(holder.imgLike);
            holder.txtLike.setTextColor(ctx.getResources().getColor(android.R.color.black));
            holder.txtLike.setTypeface(null, Typeface.NORMAL);

            holder.imgLike.setOnClickListener(view -> {
                viewModel.likeTweet(holder.mItem.getId());
            });

            for (Like like : holder.mItem.getLikes()) {
                if (like.getUsername().equals(usuario)) {
                    Glide.with(ctx).load(R.drawable.ic_like_pink).into(holder.imgLike);
                    holder.txtLike.setTextColor(ctx.getResources().getColor(R.color.rosa));
                    holder.txtLike.setTypeface(null, Typeface.BOLD);
                }
            }
        }
    }
    public void setDatos(List<Tweet> tweetList){
        this.mValues = tweetList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (mValues != null) {
            return mValues.size();
        }else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView txtNombre;
        public final TextView txtContenido;
        public final TextView txtLike;
        public final ImageView imgFotoPerfil;
        public final ImageView imgLike;
        public final ImageView imgDialog;
        public Tweet mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtNombre = view.findViewById(R.id.txtNombreUsuario);
            txtContenido = view.findViewById(R.id.txtContenido);
            txtLike = view.findViewById(R.id.txtLikes);
            imgFotoPerfil = view.findViewById(R.id.imgFotoPerfil);
            imgLike = view.findViewById(R.id.imgLike);
            imgDialog = view.findViewById(R.id.imgDialog);
        }
    }
}