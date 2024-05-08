package com.tfg.modelos;

import android.os.CountDownTimer;

import com.tfg.eventos.listeners.PartidaCazaEventListener;
import com.tfg.modelos.edificios.CabaniaCaza;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.generadores_recursos.IGeneradorRecursos;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class TimerPartidaCaza extends CountDownTimer {

    private CabaniaCaza cabaniaCaza;
    @Getter @Setter
    private long segundosRestantes;

    // Listeners eventos
    private List<PartidaCazaEventListener> listeners = new ArrayList<>();

    public void addEventListener(PartidaCazaEventListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    public void removeEventListener(PartidaCazaEventListener listener) {
        listeners.remove(listener);
    }

    public void lanzarEventoOnTick() {
        listeners.forEach(PartidaCazaEventListener::onTimerTick);
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
        System.out.println("timer tick, segs restantes = "+segundosRestantes);
        for (IGeneradorRecursos generadorRecursos : cabaniaCaza.getGeneradoresRecursos()) {
            generadorRecursos.producirRecursos(cabaniaCaza.getRecursos(), RecursosEnum.COMIDA, cabaniaCaza.getAldeanosAsignados());
        }
        lanzarEventoOnTick();
    }

    @Override
    public void onFinish() {
        lanzarEventoFinalizarPartida();
        cabaniaCaza.finalizarPartidaCaza();
    }

}
