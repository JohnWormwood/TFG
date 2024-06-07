package com.tfg.ui;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.tfg.R;
import com.tfg.controladores.ControladorAldea;
import com.tfg.eventos.listeners.ActualizarInterfazEventListener;
import com.tfg.modelos.EstructuraBase;
import com.tfg.utilidades.Constantes;
import com.tfg.utilidades.PopupManager;

public class MenuSenado extends MenuEstructuraBase {

    public MenuSenado(Context context, EstructuraBase estructura,
                      ConstraintLayout layout, TextView textViewNivel,
                      TextView textViewPrecioTroncos, TextView textViewPrecioPiedra,
                      TextView textViewPrecioTablones, TextView textViewPrecioHierro,
                      TextView textViewPrecioOro, Button buttonMejorar, PopupManager popupManager) {
        super(context, estructura, layout, textViewNivel, textViewPrecioTroncos,
                textViewPrecioPiedra, textViewPrecioTablones, textViewPrecioHierro,
                textViewPrecioOro, buttonMejorar, popupManager);
    }

    @Override
    public void actualizar() {
        super.actualizar();
        ControladorAldea.manejarSubidaNivel();
        lanzarEvento(ActualizarInterfazEventListener::onActualizarInterfaz);
    }

    @Override
    protected void manejarSubidaNivel(int nivel) {
        super.manejarSubidaNivel(nivel);
        tutorialSegunNivel(nivel);
    }

    private void tutorialSegunNivel(int nivel) {
        Toast.makeText(context, "Ha aumentado la poblacion maxima", Toast.LENGTH_SHORT).show();
        if (popupManager != null) {
            switch (nivel) {
                case Constantes.Aldea.NIVEL_DESBLOQUEO_PIEDRA:
                    popupManager.showPopup(context.getString(R.string.msj_desbloqueo_mina));
                    break;
                case Constantes.Aldea.NIVEL_DESBLOQUEO_TABLONES:
                    popupManager.showPopup(context.getString(R.string.msj_desbloqueo_carpinteria));
                    break;
                case Constantes.Aldea.NIVEL_DESBLOQUEO_CASTILLO:
                    popupManager.showPopup(context.getString(R.string.msj_desbloqueo_castillo));
                    break;
                case Constantes.Aldea.NIVEL_DESBLOQUEO_HIERRO:
                    popupManager.showPopup(context.getString(R.string.msj_desbloqueo_hierro));
                    break;
                case Constantes.Aldea.NIVEL_DESBLOQUEO_GRANJA:
                    popupManager.showPopup(context.getString(R.string.msj_desbloqueo_granja));
                    break;
                case Constantes.Aldea.NIVEL_DESBLOQUEO_ORO:
                    popupManager.showPopup(context.getString(R.string.msj_desbloqueo_oro));
                    break;
                case Constantes.Aldea.NIVEL_DESBLOQUEO_MERCADER:
                    popupManager.showPopup(context.getString(R.string.msj_desbloqueo_mercado));
                    break;
            }
        }
    }
}
