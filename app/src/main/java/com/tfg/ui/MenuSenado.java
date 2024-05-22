package com.tfg.ui;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.tfg.controladores.ControladorAldea;
import com.tfg.eventos.listeners.ActualizarInterfazEventListener;
import com.tfg.eventos.listeners.AtaqueEventListener;
import com.tfg.modelos.EstructuraBase;

public class MenuSenado extends MenuEstructuraBase {

    public MenuSenado(Context context, EstructuraBase estructura,
                      ConstraintLayout layout, TextView textViewNivel,
                      TextView textViewPrecioTroncos, TextView textViewPrecioPiedra,
                      TextView textViewPrecioTablones, TextView textViewPrecioHierro,
                      TextView textViewPrecioOro, Button buttonMejorar) {
        super(context, estructura, layout, textViewNivel, textViewPrecioTroncos,
                textViewPrecioPiedra, textViewPrecioTablones, textViewPrecioHierro,
                textViewPrecioOro, buttonMejorar);
    }

    @Override
    public void actualizar() {
        super.actualizar();
        ControladorAldea.manejarSubidaNivel();
        lanzarEvento(ActualizarInterfazEventListener::onActualizarInterfaz);
    }
}
