package com.tfg.activities;

import static com.tfg.utilidades.UtilidadActivity.setEfectoBoton;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.api.SystemParameterOrBuilder;
import com.tfg.R;
import com.tfg.bbdd.firebase.auth.GestorSesion;
import com.tfg.utilidades.SoundManager;
import com.tfg.utilidades.UtilidadActivity;

public class OpcionesActivity extends AppCompatActivity {
    private SeekBar seekBarAmbiente, seekBarMusica;
    private SoundManager soundManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);

        ImageButton buttonJugar = findViewById(R.id.buttonCerrarSesion);
        TextView textViewJugar = findViewById(R.id.textView2);
        setEfectoBoton(buttonJugar, textViewJugar);

        soundManager = SoundManager.getInstance(this);

        seekBarAmbiente = findViewById(R.id.seekBarEfectos);
        seekBarMusica = findViewById(R.id.seekBarMusica);



        seekBarAmbiente.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = progress / 100.0f;
                soundManager.getMediaPlayerEfectos().setVolume(volume, volume);
                soundManager.setVolEfectos(volume);
                getSharedPreferences(getString(R.string.fichero_preferences), Context.MODE_PRIVATE)
                        .edit().putFloat(getString(R.string.key_vol_efectos), volume)
                        .apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // No se necesita implementación
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // No se necesita implementación
            }
        });

        seekBarMusica.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = progress / 100.0f;
                soundManager.getMediaPlayerMusica().setVolume(volume, volume);
                soundManager.setVolMusica(volume);
                getSharedPreferences(getString(R.string.fichero_preferences), Context.MODE_PRIVATE)
                        .edit().putFloat(getString(R.string.key_vol_musica), volume)
                        .apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // No se necesita implementación
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // No se necesita implementación
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        seekBarAmbiente.setProgress((int) (soundManager.getVolEfectos()*100));
        seekBarMusica.setProgress((int) (soundManager.getVolMusica()*100));
    }

    public void buttonCerrarSesionOnClick(View view) {
        GestorSesion.cerrarSesion();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SoundManager soundManager = SoundManager.getInstance(this);
        soundManager.getMediaPlayerMusica().pause();
        soundManager.getMediaPlayerEfectos().pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SoundManager soundManager = SoundManager.getInstance(this);
        soundManager.getMediaPlayerMusica().start();
        soundManager.getMediaPlayerEfectos().start();
    }

}