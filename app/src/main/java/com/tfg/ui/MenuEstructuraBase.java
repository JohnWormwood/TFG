package com.tfg.ui;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.tfg.controladores.ControladorAldea;
import com.tfg.controladores.ControladorEstructuraBase;
import com.tfg.modelos.EstructuraBase;
import com.tfg.modelos.enums.RecursosEnum;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuEstructuraBase {
    protected Context context;
    protected EstructuraBase estructura;
    protected ConstraintLayout layout;
    protected TextView textViewNivel;
    protected TextView textViewPrecioTroncos;
    protected TextView textViewPrecioPiedra;
    protected TextView textViewPrecioTablones;
    protected TextView textViewPrecioHierro;
    protected TextView textViewPrecioOro;
    protected Button buttonMejorar;

    public void iniciar() {
        actualizar();
        buttonMejorar.setOnClickListener(buttonMejorarOnClick());
    }

    public void actualizar() {
        textViewNivel.setText(String.valueOf(estructura.getNivel()));
        Map<RecursosEnum, Integer> preciosMejora = ControladorEstructuraBase.getPreciosMejoraEstructura(estructura);
        textViewPrecioTroncos.setText(String.valueOf(preciosMejora.getOrDefault(RecursosEnum.TRONCOS_MADERA, 0)));
        textViewPrecioPiedra.setText(String.valueOf(preciosMejora.getOrDefault(RecursosEnum.PIEDRA, 0)));
        textViewPrecioTablones.setText(String.valueOf(preciosMejora.getOrDefault(RecursosEnum.TABLONES_MADERA, 0)));
        textViewPrecioPiedra.setText(String.valueOf(preciosMejora.getOrDefault(RecursosEnum.PIEDRA, 0)));
        textViewPrecioHierro.setText(String.valueOf(preciosMejora.getOrDefault(RecursosEnum.HIERRO, 0)));
        textViewPrecioOro.setText(String.valueOf(preciosMejora.getOrDefault(RecursosEnum.ORO, 0)));
    }


    public View.OnClickListener buttonMejorarOnClick() {
        return view -> {
            if (estructura.aumentarNivel()) {
                actualizar();
            } else {
                Toast.makeText(context, "No tienes suficientes recursos", Toast.LENGTH_SHORT).show();
            }
        };
    }
}
