package com.tfg.utilidades;

import android.content.Context;
import android.media.MediaPlayer;

import lombok.Data;

@Data
public class SoundManager {

    private static SoundManager instance;
    private MediaPlayer mediaPlayerMusica, mediaPlayerEfectos;
    private Context context;

    private SoundManager(Context context) {
        this.context = context.getApplicationContext(); // Usar ApplicationContext para evitar memory leaks
        mediaPlayerMusica = new MediaPlayer();
        mediaPlayerEfectos = new MediaPlayer();
    }

    public static synchronized SoundManager getInstance(Context context) {
        if (instance == null) {
            instance = new SoundManager(context);
        }
        return instance;
    }
    public void playSound1(int resId) {
        if (mediaPlayerMusica.isPlaying()) {
            mediaPlayerMusica.stop();
            mediaPlayerMusica.reset();
        }
        mediaPlayerMusica = MediaPlayer.create(context, resId);
        mediaPlayerMusica.start();
    }

    public void playSound2(int resId) {
        if (mediaPlayerEfectos.isPlaying()) {
            mediaPlayerEfectos.stop();
            mediaPlayerEfectos.reset();
        }
        mediaPlayerEfectos = MediaPlayer.create(context, resId);
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
