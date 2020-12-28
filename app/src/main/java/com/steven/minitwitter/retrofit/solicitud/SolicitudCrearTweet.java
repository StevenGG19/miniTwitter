package com.steven.minitwitter.retrofit.solicitud;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SolicitudCrearTweet {

    @SerializedName("mensaje")
    @Expose
    private String mensaje;

    public SolicitudCrearTweet() {
    }

    public SolicitudCrearTweet(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
