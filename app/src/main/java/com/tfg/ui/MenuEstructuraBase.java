package com.tfg.ui;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.tfg.R;
import com.tfg.controladores.ControladorEstructuraBase;
import com.tfg.eventos.LanzadorEventos;
import com.tfg.eventos.listeners.ActualizarInterfazEventListener;
import com.tfg.modelos.EstructuraBase;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.utilidades.PopupManager;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class MenuEstructuraBase extends LanzadorEventos<ActualizarInterfazEventListener> {
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
    protected PopupManager popupManager;

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
            String msj = "";
            try {
                if (estructura.aumentarNivel()) {
                    msj = context.getString(R.string.msj_subida_nivel, estructura.getNivel());
                    manejarSubidaNivel(estructura.getNivel());
                } else {
                    msj = context.getString(R.string.msj_recursos_insuficientes);
                }
            } catch (IllegalArgumentException e) {
                msj = e.getMessage();
            } finally {
                Toast.makeText(context, msj, Toast.LENGTH_LONG).show();
            }
        };
    }

    protected void manejarSubidaNivel(int nivel) {
        actualizar();
    }
}
