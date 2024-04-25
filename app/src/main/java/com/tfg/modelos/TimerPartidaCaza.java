package com.tfg.modelos;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tfg.modelos.edificios.CabaniaCaza;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.interfaces.IGeneradorRecursos;

import lombok.Getter;

public class TimerPartidaCaza extends CountDownTimer {

    private TextView textViewPartidaCaza;
    private ImageButton buttonCaza;
    private CabaniaCaza cabaniaCaza;
    private Context contextActividad;

    @Getter
    private long segundosRestantes;

    public TimerPartidaCaza(long millisInFuture, CabaniaCaza cabaniaCaza, TextView textViewPartidaCaza, ImageButton buttonCaza, Context contextActividad) {
        super(millisInFuture, 1000);
        segundosRestantes = millisInFuture / 1000;
        this.cabaniaCaza = cabaniaCaza;
        this.textViewPartidaCaza = textViewPartidaCaza;
        this.buttonCaza = buttonCaza;
        this.contextActividad = contextActividad;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        segundosRestantes = millisUntilFinished / 1000;
        for (IGeneradorRecursos generadorRecursos : cabaniaCaza.getGeneradoresRecursos()) {
            generadorRecursos.producirRecursos(cabaniaCaza.getRecursosGenerados(), RecursosEnum.COMIDA, cabaniaCaza.getAldeanosAsignados());
        }
        textViewPartidaCaza.setText("Partida actual: "+ cabaniaCaza.getAldeanosAsignados()+" cazadores, Tiempo restante: "+segundosRestantes+" segundos");
    }

    @Override
    public void onFinish() {
        if (cabaniaCaza.getAldeanosMuertosEnPartida() > 0) {
            Toast.makeText(contextActividad, "Han muerto "+cabaniaCaza.getAldeanosMuertosEnPartida()+" cazadores en esta partida", Toast.LENGTH_LONG).show();
        }
        cabaniaCaza.finalizarPartidaCaza();
        buttonCaza.setEnabled(true);
        textViewPartidaCaza.setText("Partida actual: No hay ninguna partida de caza en curso");
    }

    public void actualizarElementosUI(TextView textViewPartidaCaza, ImageButton buttonCaza, Context contextActividad) {
        this.textViewPartidaCaza = textViewPartidaCaza;
        this.buttonCaza = buttonCaza;
        this.contextActividad = contextActividad;
    }
}
