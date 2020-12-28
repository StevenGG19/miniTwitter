package com.steven.minitwitter.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.steven.minitwitter.R;
import com.steven.minitwitter.common.Constante;
import com.steven.minitwitter.common.SharedPreferencesManager;
import com.steven.minitwitter.retrofit.ClienteMinitwitter;
import com.steven.minitwitter.retrofit.ServicioMiniTwitter;
import com.steven.minitwitter.retrofit.respuesta.RespuestaAuth;
import com.steven.minitwitter.retrofit.solicitud.SolicitudAcceso;
import com.steven.minitwitter.ui.DashBoardActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textView;
    private EditText edtCorreo;
    private EditText edtContrasena;
    private Button button;
    private ClienteMinitwitter clienteMinitwitter;
    private ServicioMiniTwitter servicioMiniTwitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofitInit();
        finds();
        events();
    }

    private void retrofitInit() {
        clienteMinitwitter = ClienteMinitwitter.getInstance();
        servicioMiniTwitter = clienteMinitwitter.getServicioMiniTwitter();
    }

    private void finds() {
        textView = findViewById(R.id.txtRegistro);
        edtCorreo = findViewById(R.id.edtCorreo);
        edtContrasena = findViewById(R.id.contrasena);
        button = findViewById(R.id.btnEntrar);
    }

    private void events() {
        textView.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEntrar:
                acceso();
                break;
            case R.id.txtRegistro:
                irPanelRegistro();
                break;
        }
    }

    private void acceso() {
        String correo = edtCorreo.getText().toString();
        String contrasena = edtContrasena.getText().toString();

        if (correo.isEmpty()) {
            edtCorreo.setError("El email es requerido");
        } else if (contrasena.isEmpty()) {
            edtContrasena.setError("La contraseña es requerida");
        } else {
            SolicitudAcceso solicitudAcceso = new SolicitudAcceso(correo, contrasena);
            Call<RespuestaAuth> call = servicioMiniTwitter.acceso(solicitudAcceso);
            call.enqueue(new Callback<RespuestaAuth>() {
                @Override
                public void onResponse(Call<RespuestaAuth> call, Response<RespuestaAuth> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Sesion iniciada correctamente", Toast.LENGTH_SHORT).show();

                        SharedPreferencesManager.setStringValue(Constante.PREF_TOKEN, response.body().getToken());
                        SharedPreferencesManager.setStringValue(Constante.PREF_USUARIO, response.body().getUsername());
                        SharedPreferencesManager.setStringValue(Constante.PREF_CORREO, response.body().getEmail());
                        SharedPreferencesManager.setStringValue(Constante.PREF_FOTOURL, response.body().getPhotoUrl());
                        SharedPreferencesManager.setStringValue(Constante.PREF_CREADO, response.body().getCreated());
                        SharedPreferencesManager.setBooleanValue(Constante.PREF_ACTIVO, response.body().getActive());

                        Intent intent = new Intent(MainActivity.this, DashBoardActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Algo salio mal, revise los datos", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RespuestaAuth> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Error en la conexión, intentelo de nuevo", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void irPanelRegistro() {
        Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
        startActivity(intent);
        finish();
    }
}