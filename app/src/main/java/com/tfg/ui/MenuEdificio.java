package com.tfg.ui;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.tfg.controladores.ControladorAldea;
import com.tfg.modelos.edificios.Edificio;
import com.tfg.modelos.enums.RecursosEnum;

import java.util.Map;

import lombok.Data;

@Data
public class MenuEdificio {
    private Context context;
    private Edificio edificio;
    private ConstraintLayout layout;
    private TextView textViewAldeanosAsignados;
    private TextView textViewNivel;
    private TextView textViewPrecioTroncos;
    private TextView textViewPrecioPiedra;
    private TextView textViewPrecioTablones;
    private TextView textViewPrecioHierro;
    private TextView textViewPrecioOro;
    private SeekBar seekBarAldeanosAsignados;
    private Button buttonMejorar;

    public void iniciar() {
        actualizar();
        buttonMejorar.setOnClickListener(buttonMejorarOnClick());
        if (seekBarAldeanosAsignados != null) {
            seekBarAldeanosAsignados.setOnSeekBarChangeListener(seekBarAldeanosAsignadosOnChange());
        }
    }

    public void actualizar() {
        textViewNivel.setText(String.valueOf(edificio.getNivel()));
        Map<RecursosEnum, Integer> preciosMejora = ControladorAldea.getPreciosMejoraEdificio(edificio);
        textViewPrecioTroncos.setText(String.valueOf(preciosMejora.getOrDefault(RecursosEnum.TRONCOS_MADERA, 0)));
        textViewPrecioPiedra.setText(String.valueOf(preciosMejora.getOrDefault(RecursosEnum.PIEDRA, 0)));
        textViewPrecioTablones.setText(String.valueOf(preciosMejora.getOrDefault(RecursosEnum.TABLONES_MADERA, 0)));
        textViewPrecioPiedra.setText(String.valueOf(preciosMejora.getOrDefault(RecursosEnum.PIEDRA, 0)));
        textViewPrecioHierro.setText(String.valueOf(preciosMejora.getOrDefault(RecursosEnum.HIERRO, 0)));
        textViewPrecioOro.setText(String.valueOf(preciosMejora.getOrDefault(RecursosEnum.ORO, 0)));

        if (seekBarAldeanosAsignados != null) {
            seekBarAldeanosAsignados.setMin(0);
            seekBarAldeanosAsignados.setMax(edificio.getAldeanosMaximos());
            seekBarAldeanosAsignados.setProgress(edificio.getAldeanosAsignados());
            textViewAldeanosAsignados.setText(String.valueOf(seekBarAldeanosAsignados.getProgress()));
        }
    }


    public View.OnClickListener buttonMejorarOnClick() {
        return view -> {
            if (edificio.aumentarNivel()) {
                actualizar();
            } else {
                Toast.makeText(context, "No tienes suficientes recursos", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public SeekBar.OnSeekBarChangeListener seekBarAldeanosAsignadosOnChange() {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewAldeanosAsignados.setText(String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                edificio.modificarAldeanosAsignados(seekBar.getProgress());
            }
        };
    }
}
