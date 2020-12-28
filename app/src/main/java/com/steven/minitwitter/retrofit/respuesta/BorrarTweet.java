package com.steven.minitwitter.retrofit.respuesta;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BorrarTweet {
    @SerializedName("mensaje")
    @Expose
    private String mensaje;

    @SerializedName("user")
    @Expose
    private User user;

    public BorrarTweet() {
    }

    public BorrarTweet(String mensaje, User user) {
        this.mensaje = mensaje;
        this.user = user;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
