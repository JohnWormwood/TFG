package com.tfg.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

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

        soundManager = SoundManager.getInstance(this);

        seekBarAmbiente = findViewById(R.id.seekBarEfectos);
        seekBarMusica = findViewById(R.id.seekBarMusica);

        seekBarAmbiente.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = progress / 100.0f;
                soundManager.getMediaPlayerEfectos().setVolume(volume,volume);
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
                soundManager.getMediaPlayerMusica().setVolume(volume,volume);
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

    public void buttonCerrarSesionOnClick(View view) {
        GestorSesion.cerrarSesion();
        finish();
    }

}