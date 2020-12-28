package com.steven.minitwitter.data;

import android.widget.Toast;

import com.steven.minitwitter.common.Constante;
import com.steven.minitwitter.common.MyApp;
import com.steven.minitwitter.common.SharedPreferencesManager;
import com.steven.minitwitter.retrofit.AuthClienteMinitwitter;
import com.steven.minitwitter.retrofit.AuthServicioMiniTwitter;
import com.steven.minitwitter.retrofit.respuesta.PerfilUsuario;
import com.steven.minitwitter.retrofit.respuesta.UploadFoto;
import com.steven.minitwitter.retrofit.solicitud.SolicitudPerfilUsuario;

import java.io.File;

import androidx.lifecycle.MutableLiveData;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilRepository {
    private AuthServicioMiniTwitter servicioMiniTwitter;
    private AuthClienteMinitwitter clienteMinitwitter;
    MutableLiveData<PerfilUsuario> perfilUsuario;
    MutableLiveData<String> fotoPerfil;

    public PerfilRepository() {
        clienteMinitwitter = AuthClienteMinitwitter.getInstance();
        servicioMiniTwitter = clienteMinitwitter.getAuthServicioMiniTwitter();
        perfilUsuario = getDatosPerfil();
        if (fotoPerfil == null) {
            fotoPerfil = new MutableLiveData<>();
        }
    }

    public MutableLiveData<String> getFotoPerfil() {
        return fotoPerfil;
    }
    public MutableLiveData<PerfilUsuario> getDatosPerfil() {
        if (perfilUsuario == null) {
            perfilUsuario = new MutableLiveData<>();
        }

        Call<PerfilUsuario> call = servicioMiniTwitter.getDatosUsuario();
        call.enqueue(new Callback<PerfilUsuario>() {
            @Override
            public void onResponse(Call<PerfilUsuario> call, Response<PerfilUsuario> response) {
                if (response.isSuccessful()) {
                    perfilUsuario.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), "Algo salio mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PerfilUsuario> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
        return perfilUsuario;
    }

    public void updateDatosPerfil(SolicitudPerfilUsuario solicitud) {
        Call<PerfilUsuario> datos = servicioMiniTwitter.updatePerfil(solicitud);
        datos.enqueue(new Callback<PerfilUsuario>() {
            @Override
            public void onResponse(Call<PerfilUsuario> call, Response<PerfilUsuario> response) {
                if (response.isSuccessful()) {
                    perfilUsuario.setValue(response.body());
                    Toast.makeText(MyApp.getContext(), "Datos guardados correctamente", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MyApp.getContext(), "Algo salio mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PerfilUsuario> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadFoto(String foto) {
        File file = new File(foto);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        Call<UploadFoto> call = servicioMiniTwitter.uploadFile(requestBody);
        call.enqueue(new Callback<UploadFoto>() {
            @Override
            public void onResponse(Call<UploadFoto> call, Response<UploadFoto> response) {
                if (response.isSuccessful()) {
                    SharedPreferencesManager.setStringValue(Constante.PREF_FOTOURL, response.body().getFilename());
                    fotoPerfil.setValue(response.body().getFilename());
                } else {
                    Toast.makeText(MyApp.getContext(), "Algo salio mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadFoto> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
