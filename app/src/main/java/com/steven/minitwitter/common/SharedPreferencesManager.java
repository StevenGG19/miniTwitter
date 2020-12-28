package com.steven.minitwitter.common;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String APP_SETTINGS_FILE = "APP_SETTINGS";
    private SharedPreferencesManager() { }

    private static SharedPreferences getShared() {
        return MyApp.getContext().getSharedPreferences(APP_SETTINGS_FILE, Context.MODE_PRIVATE);
    }

    public static void setStringValue(String dataLabel, String dataValue) {
        SharedPreferences.Editor editor = getShared().edit();
        editor.putString(dataLabel, dataValue);
        editor.commit();
    }

    public static void setBooleanValue(String dataLabel, boolean dataValue) {
        SharedPreferences.Editor editor = getShared().edit();
        editor.putBoolean(dataLabel, dataValue);
        editor.commit();
    }

    public static String getStringValue(String dataLabel) {
        /**
         * getString metodo necesita dos parametros
         * el primero es el nombre de la variable a buscar
         * el segundo es lo que devuelve en caso de que no exista la variable pasada(primer parametro)
         */
        return getShared().getString(dataLabel, null);
    }

    public static boolean getBooleanValue(String dataLabel) {
        return getShared().getBoolean(dataLabel, false);
    }
}
