package com.steven.minitwitter.retrofit.solicitud;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SolicitudPerfilUsuario {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("password")
    @Expose
    private String password;

    public SolicitudPerfilUsuario() {
    }

    public SolicitudPerfilUsuario(String username, String email, String descripcion, String website, String password) {
        this.username = username;
        this.email = email;
        this.descripcion = descripcion;
        this.website = website;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}