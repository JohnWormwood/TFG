package com.tfg.ui;

import android.content.Context;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.tfg.R;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.EstructuraBase;
import com.tfg.modelos.edificios.Edificio;
import com.tfg.utilidades.PopupManager;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuEdificioAsignable extends MenuEstructuraBase {

    private SeekBar seekBarAldeanosAsignados;
    private TextView textViewAldeanosAsignados;

    private int valorInicialSeekbar;

    public MenuEdificioAsignable(Context context, EstructuraBase estructura,
                                 ConstraintLayout layout, TextView textViewNivel,
                                 TextView textViewPrecioTroncos, TextView textViewPrecioPiedra,
                                 TextView textViewPrecioTablones, TextView textViewPrecioHierro,
                                 TextView textViewPrecioOro, Button buttonMejorar,
                                 SeekBar seekBarAldeanosAsignados, TextView textViewAldeanosAsignados,
                                 PopupManager popupManager) {
        super(context, estructura, layout, textViewNivel, textViewPrecioTroncos,
                textViewPrecioPiedra, textViewPrecioTablones, textViewPrecioHierro,
                textViewPrecioOro, buttonMejorar,
                popupManager);
        this.seekBarAldeanosAsignados = seekBarAldeanosAsignados;
        this.textViewAldeanosAsignados = textViewAldeanosAsignados;
    }


    @Override
    public void iniciar() {
        super.iniciar();
        seekBarAldeanosAsignados.setOnSeekBarChangeListener(seekBarAldeanosAsignadosOnChange());
    }

    @Override
    public void actualizar() {
        super.actualizar();
        seekBarAldeanosAsignados.setMin(0);
        seekBarAldeanosAsignados.setMax(estructura.getAldeanosMaximos());
        seekBarAldeanosAsignados.setProgress(estructura.getAldeanosAsignados());
        textViewAldeanosAsignados.setText(String.valueOf(seekBarAldeanosAsignados.getProgress()));
    }

    public SeekBar.OnSeekBarChangeListener seekBarAldeanosAsignadosOnChange() {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewAldeanosAsignados.setText(String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                valorInicialSeekbar = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Edificio edificio = (Edificio) estructura;
                if (seekBar.getProgress() <= Aldea.getInstance().getPoblacion() + edificio.getAldeanosAsignados()) {
                    edificio.modificarAldeanosAsignados(seekBar.getProgress());
                } else {
                    seekBar.setProgress(valorInicialSeekbar);
                    Toast.makeText(context, context.getString(R.string.msj_aldeanos_insuficientes), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    protected void manejarSubidaNivel(int nivel) {
        super.manejarSubidaNivel(nivel);
        Toast.makeText(context, context.getString(R.string.msj_subida_nivel_edificio_asignable),
                Toast.LENGTH_LONG).show();
    }
}
