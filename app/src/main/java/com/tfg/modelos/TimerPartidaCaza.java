package com.tfg.modelos;

import android.os.CountDownTimer;
import android.util.Log;

import com.tfg.eventos.LanzadorEventos;
import com.tfg.eventos.listeners.PartidaCazaEventListener;
import com.tfg.modelos.edificios.CabaniaCaza;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.modelos.generadores_recursos.IGeneradorRecursos;

import lombok.Getter;
import lombok.Setter;

public class TimerPartidaCaza extends CountDownTimer {

    private CabaniaCaza cabaniaCaza;
    @Getter
    @Setter
    private long segundosRestantes;
    @Getter
    private LanzadorEventos<PartidaCazaEventListener> lanzadorEventos;

    public TimerPartidaCaza(long millisInFuture, CabaniaCaza cabaniaCaza) {
        super(millisInFuture, 1000);
        segundosRestantes = millisInFuture / 1000;
        this.cabaniaCaza = cabaniaCaza;
        lanzadorEventos = new LanzadorEventos<>();
    }

    @Override
    public void onTick(long millisUntilFinished) {
        segundosRestantes = millisUntilFinished / 1000;
        System.out.println();
        for (IGeneradorRecursos generadorRecursos : cabaniaCaza.getGeneradoresRecursos()) {
            generadorRecursos.producirRecursos(cabaniaCaza.getRecursos(), RecursosEnum.COMIDA, cabaniaCaza.getAldeanosAsignados());
        }
        lanzadorEventos.lanzarEvento(PartidaCazaEventListener::onTimerTick);
        Log.d(getClass().getSimpleName(), "(onTick) Segundos restantes = " + segundosRestantes);
    }

    @Override
    public void onFinish() {
        lanzadorEventos.lanzarEvento(PartidaCazaEventListener::onFinalizarPartida);
        cabaniaCaza.finalizarPartidaCaza();
    }

}
