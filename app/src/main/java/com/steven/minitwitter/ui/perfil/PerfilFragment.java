package com.steven.minitwitter.ui.perfil;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.steven.minitwitter.R;
import com.steven.minitwitter.common.Constante;
import com.steven.minitwitter.data.PerfilViewModel;
import com.steven.minitwitter.retrofit.respuesta.PerfilUsuario;
import com.steven.minitwitter.retrofit.solicitud.SolicitudPerfilUsuario;
import com.steven.minitwitter.ui.DashBoardActivity;

public class PerfilFragment extends Fragment {

    private PerfilViewModel mViewModel;
    Button bntGuardar;
    Button btnCambiar;
    EditText edtNombre;
    EditText edtCorreo;
    EditText edtWeb;
    EditText edtdes;
    EditText edtContra;
    ImageView imgFoto;
    PermissionListener todosPermisos;

    public static PerfilFragment newInstance() {
        return new PerfilFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(PerfilViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.perfil_fragment, container, false);
        btnCambiar = v.findViewById(R.id.btnCombiar);
        bntGuardar = v.findViewById(R.id.btnGuardar);
        edtNombre = v.findViewById(R.id.edtNombre);
        edtCorreo = v.findViewById(R.id.edtCorreoUsuario);
        edtContra = v.findViewById(R.id.edtContra);
        edtWeb = v.findViewById(R.id.edtWebsite);
        edtdes = v.findViewById(R.id.edtDescripcion);
        imgFoto = v.findViewById(R.id.imgPerfil);

        bntGuardar.setOnClickListener(view -> {
            String username = edtNombre.getText().toString();
            String email = edtCorreo.getText().toString();
            String descripcion = edtdes.getText().toString();
            String website = edtWeb.getText().toString();
            String pass = edtContra.getText().toString();

            if (username.isEmpty()) {
                edtNombre.setError("Debe llenar este campo");
            }else if (email.isEmpty()) {
                edtCorreo.setError("Debe llenar este campo");
            }else if (pass.isEmpty()) {
                edtContra.setError("Debe llenar este campo");
            }else {
                SolicitudPerfilUsuario solicitud = new SolicitudPerfilUsuario(username, email, descripcion, website, pass);
                mViewModel.actualizarDatos(solicitud);
            }
        });

        imgFoto.setOnClickListener(view -> {
            //invocamos al metodo para verificar los permisos
            verificarPermisos();
        });
        
        mViewModel.perfilUsuarioLiveData.observe(getActivity(), new Observer<PerfilUsuario>() {
            @Override
            public void onChanged(PerfilUsuario perfilUsuario) {
                edtNombre.setText(perfilUsuario.getUsername());
                edtCorreo.setText(perfilUsuario.getEmail());
                edtWeb.setText(perfilUsuario.getWebsite());
                edtdes.setText(perfilUsuario.getDescripcion());

                if (!perfilUsuario.getPhotoUrl().isEmpty()) {
                    Glide.with(getActivity())
                            .load(Constante.MINITWITTER_FILE_URL + perfilUsuario.getPhotoUrl())
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .centerCrop()
                            .skipMemoryCache(true)
                            .into(imgFoto);
                }
            }
        });

        mViewModel.fotoUsuario.observe(getActivity(), s ->
                Glide.with(getActivity())
                        .load(Constante.MINITWITTER_FILE_URL + s)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .into(imgFoto));

        return v;

    }

    private void verificarPermisos() {
        PermissionListener dialogPermisos = DialogOnDeniedPermissionListener.Builder
                .withContext(getActivity())
                .withTitle("Permisos")
                .withMessage("Se necesita aceptar los permisos para cambiar la foto")
                .withButtonText("Aceptar")
                .withIcon(R.drawable.ic_baseline_warning_24)
                .build();

        todosPermisos = new CompositePermissionListener((PermissionListener) getActivity(),
                dialogPermisos);

        Dexter.withContext(getActivity())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(todosPermisos)
                .check();
    }

}