package com.steven.minitwitter.data;

import android.app.Application;

import com.steven.minitwitter.retrofit.respuesta.PerfilUsuario;
import com.steven.minitwitter.retrofit.solicitud.SolicitudPerfilUsuario;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class PerfilViewModel extends AndroidViewModel {
    public PerfilRepository perfil;
    public LiveData<PerfilUsuario> perfilUsuarioLiveData;
    public LiveData<String> fotoUsuario;

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        perfil = new PerfilRepository();
        perfilUsuarioLiveData = perfil.getDatosPerfil();
        fotoUsuario = perfil.getFotoPerfil();
    }

    public void actualizarDatos(SolicitudPerfilUsuario datos) {
        perfil.updateDatosPerfil(datos);
    }

    public void subirFoto(String foto) {
        perfil.uploadFoto(foto);
    }
}