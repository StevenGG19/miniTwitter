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
import com.steven.minitwitter.retrofit.solicitud.SolicitudRegistro;
import com.steven.minitwitter.ui.DashBoardActivity;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textView;
    private EditText edtUsuario;
    private EditText edtCorreo;
    private EditText edtContrasena;
    private Button btnRegistro;
    private final String KEY = "UDEMYANDROID";
    private ClienteMinitwitter clienteMinitwitter;
    private ServicioMiniTwitter servicioMiniTwitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        retrofit();
        finds();
        events();
    }

    private void retrofit() {
        clienteMinitwitter = ClienteMinitwitter.getInstance();
        servicioMiniTwitter = clienteMinitwitter.getServicioMiniTwitter();
    }

    private void finds() {
        textView = findViewById(R.id.iniciarSesion);
        btnRegistro = findViewById(R.id.btnRegistrar);
        edtUsuario = findViewById(R.id.edtNomUsuario);
        edtCorreo = findViewById(R.id.edtCorreoRegistro);
        edtContrasena = findViewById(R.id.edtContrasenaRegistro);

    }

    private void events() {
        textView.setOnClickListener(this);
        btnRegistro.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegistrar:
                registrarUsuario();
                break;
            case R.id.iniciarSesion:
                irIniciarSesion();
                break;
        }
    }

    private void registrarUsuario() {
        String nomUsuario = edtUsuario.getText().toString();
        String correo = edtCorreo.getText().toString();
        String contrasena = edtContrasena.getText().toString();

        if (nomUsuario.isEmpty()) {
            edtUsuario.setError("Este campo no puede estar vacio");
        }else if (correo.isEmpty()) {
            edtCorreo.setError("Este campo no puede estar vacio");
        }else if (contrasena.isEmpty() || contrasena.length() < 4) {
            edtContrasena.setError("Contraseña requerida y debe tener al menos 4 caracteres");
        }else {
            SolicitudRegistro solicitud = new SolicitudRegistro(nomUsuario, correo, contrasena, KEY);
            Call<RespuestaAuth> call = servicioMiniTwitter.registro(solicitud);
            call.enqueue(new Callback<RespuestaAuth>() {
                @Override
                public void onResponse(Call<RespuestaAuth> call, Response<RespuestaAuth> response) {
                    if (response.isSuccessful()) {

                        SharedPreferencesManager.setStringValue(Constante.PREF_TOKEN, response.body().getToken());
                        SharedPreferencesManager.setStringValue(Constante.PREF_USUARIO, response.body().getUsername());
                        SharedPreferencesManager.setStringValue(Constante.PREF_CORREO, response.body().getEmail());
                        SharedPreferencesManager.setStringValue(Constante.PREF_FOTOURL, response.body().getPhotoUrl());
                        SharedPreferencesManager.setStringValue(Constante.PREF_CREADO, response.body().getCreated());
                        SharedPreferencesManager.setBooleanValue(Constante.PREF_ACTIVO, response.body().getActive());

                        Intent intent = new Intent(RegistroActivity.this, DashBoardActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(RegistroActivity.this, "Error en el registro, intentelo de nuevo", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RespuestaAuth> call, Throwable t) {
                    Toast.makeText(RegistroActivity.this, "Error en la conexión, intentelo de nuevo", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void irIniciarSesion() {
        Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}