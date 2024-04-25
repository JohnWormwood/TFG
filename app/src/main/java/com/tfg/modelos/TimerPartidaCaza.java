package com.tfg.modelos;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tfg.eventos.PartidaCazaEventListener;
import com.tfg.modelos.edificios.CabaniaCaza;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.generadores_recursos.IGeneradorRecursos;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class TimerPartidaCaza extends CountDownTimer {

    private CabaniaCaza cabaniaCaza;
    @Getter
    private long segundosRestantes;

    // Listeners eventos
    private List<PartidaCazaEventListener> listeners = new ArrayList<>();

    public void addEventListener(PartidaCazaEventListener listener) {
        listeners.add(listener);
    }

    public void removeEventListener(PartidaCazaEventListener listener) {
        listeners.remove(listener);
    }

    public void lanzarEventoOnTick() {
        for (PartidaCazaEventListener listener : listeners) {
            listener.onTimerTick();
        }
    }

    public void lanzarEventoFinalizarPartida() {
        for (PartidaCazaEventListener listener : listeners) {
            listener.onFinalizarPartida();
        }
    }

    public TimerPartidaCaza(long millisInFuture, CabaniaCaza cabaniaCaza) {
        super(millisInFuture, 1000);
        segundosRestantes = millisInFuture / 1000;
        this.cabaniaCaza = cabaniaCaza;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        segundosRestantes = millisUntilFinished / 1000;
        for (IGeneradorRecursos generadorRecursos : cabaniaCaza.getGeneradoresRecursos()) {
            generadorRecursos.producirRecursos(cabaniaCaza.getRecursosGenerados(), RecursosEnum.COMIDA, cabaniaCaza.getAldeanosAsignados());
        }
        lanzarEventoOnTick();
    }

    @Override
    public void onFinish() {
        lanzarEventoFinalizarPartida();
        cabaniaCaza.finalizarPartidaCaza();
    }

}
