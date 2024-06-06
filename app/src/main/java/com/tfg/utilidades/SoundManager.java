package com.tfg.utilidades;

import android.content.Context;
import android.media.MediaPlayer;

import com.tfg.R;

import lombok.Data;

@Data
public class SoundManager {

    private static SoundManager instance;
    private MediaPlayer mediaPlayerMusica, mediaPlayerEfectos;
    private Context context;
    private float volMusica;
    private float volEfectos;

    private SoundManager(Context context) {
        this.context = context.getApplicationContext(); // Usar ApplicationContext para evitar memory leaks
        mediaPlayerMusica = new MediaPlayer();
        volMusica = context.getSharedPreferences(context.getString(R.string.fichero_preferences), Context.MODE_PRIVATE)
                .getFloat(context.getString(R.string.key_vol_musica), 1);

        mediaPlayerEfectos = new MediaPlayer();
        volEfectos = context.getSharedPreferences(context.getString(R.string.fichero_preferences), Context.MODE_PRIVATE)
                .getFloat(context.getString(R.string.key_vol_efectos), 1);

    }

    public static synchronized SoundManager getInstance(Context context) {
        if (instance == null) {
            instance = new SoundManager(context);
        }
        return instance;
    }

    public void playMusica(int resId) {
        if (mediaPlayerMusica.isPlaying()) {
            mediaPlayerMusica.stop();
            mediaPlayerMusica.reset();
        }
        mediaPlayerMusica = MediaPlayer.create(context, resId);
        mediaPlayerMusica.setVolume(volMusica, volMusica);
        mediaPlayerMusica.start();
    }

    public void playEfectos(int resId) {
        if (mediaPlayerEfectos.isPlaying()) {
            mediaPlayerEfectos.stop();
            mediaPlayerEfectos.reset();
        }
        mediaPlayerEfectos = MediaPlayer.create(context, resId);
        mediaPlayerEfectos.setVolume(volEfectos, volEfectos);
        mediaPlayerEfectos.start();
    }


    public void release() {
        if (mediaPlayerMusica != null) {
            mediaPlayerMusica.release();
            mediaPlayerMusica = null;
        }
        if (mediaPlayerEfectos != null) {
            mediaPlayerEfectos.release();
            mediaPlayerEfectos = null;
        }
    }

}
