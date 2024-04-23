package com.tfg.utilidades;

import android.content.Context;
import android.content.SharedPreferences;

import lombok.Getter;

@Getter
public final class GestorSharedPreferences {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public GestorSharedPreferences(Context context, String key) {
        this.sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        editor = this.sharedPreferences.edit();
    }

}
