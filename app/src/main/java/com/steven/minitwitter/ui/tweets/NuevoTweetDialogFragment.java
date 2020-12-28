package com.steven.minitwitter.ui.tweets;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.steven.minitwitter.R;
import com.steven.minitwitter.common.Constante;
import com.steven.minitwitter.common.SharedPreferencesManager;
import com.steven.minitwitter.data.TweetViewModel;

public class NuevoTweetDialogFragment extends DialogFragment implements View.OnClickListener {
    ImageView imgCerrar;
    ImageView imgFotoU;
    Button btnTweetear;
    EditText edtTexto;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.fullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nuevo_tweet_dialog, container, false);
        imgCerrar = view.findViewById(R.id.imgCerrar);
        imgFotoU = view.findViewById(R.id.imgFotoP);
        btnTweetear = view.findViewById(R.id.btnTweet);
        edtTexto = view.findViewById(R.id.edtNuevoTweet);

        imgCerrar.setOnClickListener(this);
        btnTweetear.setOnClickListener(this);

        String foto = SharedPreferencesManager.getStringValue(Constante.PREF_FOTOURL);
        if (foto != null && !foto.isEmpty()) {
            Glide.with(getActivity())
                    .load(Constante.MINITWITTER_FILE_URL + foto)
                    .into(imgFotoU);
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        String mensaje = edtTexto.getText().toString();
        if (view.getId() == R.id.btnTweet) {
            if (mensaje.isEmpty()) {
                Toast.makeText(getActivity(), "Debe escribir un mensaje", Toast.LENGTH_SHORT).show();
            }else {
                TweetViewModel viewModel = new ViewModelProvider(getActivity()).get(TweetViewModel.class);
                viewModel.insertarTweet(mensaje);
                getDialog().dismiss();
            }
        }else if (view.getId() == R.id.imgCerrar) {
            if (!mensaje.isEmpty()) {
                dialogoConfirmacion();
            }else {
                getDialog().dismiss();
            }
        }
    }

    private void dialogoConfirmacion() {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("¿Realmente desea cancelar el tweet?, El mensaje se eliminará")
                .setTitle("Cancelar Tweet")
                .setPositiveButton("Eliminar", (dialog, id) -> {
                    dialog.dismiss();
                    getDialog().dismiss();
                })
                .setNegativeButton("Cancelar", (dialog, id) -> dialog.dismiss());
        // Create the AlertDialog object and return it
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}