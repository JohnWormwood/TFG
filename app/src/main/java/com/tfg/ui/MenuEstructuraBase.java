package com.tfg.ui;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.tfg.R;
import com.tfg.controladores.ControladorEstructuraBase;
import com.tfg.eventos.LanzadorEventos;
import com.tfg.eventos.listeners.ActualizarInterfazEventListener;
import com.tfg.modelos.EstructuraBase;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.utilidades.Constantes;
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
                    msj = context.getString(R.string.msj_subida_nivel,
                            estructura.getClass().getSimpleName(), estructura.getNivel());
                    actualizar();
                    if (estructura.getClass().getSimpleName().equals("Aldea")){
                        tutorialSegunNivel(estructura.getNivel());
                    }
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
    private void tutorialSegunNivel(int nivel){
        if (popupManager != null){
            switch (nivel){
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
