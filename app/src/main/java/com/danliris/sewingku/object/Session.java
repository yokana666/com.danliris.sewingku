package com.danliris.sewingku.object;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private final SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void AddSession(String npk, String firstname, String lastname, String unit, String id_proses) {
        prefs.edit().putString("npk", npk).apply();
        prefs.edit().putString("firstname", firstname).apply();
        prefs.edit().putString("lastname", lastname).apply();
        prefs.edit().putString("unit", unit).apply();
        prefs.edit().putString("id_proses", id_proses).apply();
    }

    public void DestroySession(){
        prefs.edit().clear().apply();
    }

    public String getNpk() {
        return prefs.getString("npk","");
    }

    public String getFirstname() {
        return prefs.getString("firstname","");
    }

    public String getLastname() {
        return prefs.getString("lastname","");
    }

    public String getUnit() {
        return prefs.getString("unit","");
    }

    public String getIdProcess() {
        return prefs.getString("id_proses","");
    }

}