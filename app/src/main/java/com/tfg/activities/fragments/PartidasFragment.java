package com.tfg.activities.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tfg.R;
import com.tfg.bbdd.firebase.GestorRealTimeDatabase;
import com.tfg.controladores.ControladorRecursos;
import com.tfg.eventos.listeners.AtaqueEventListener;
import com.tfg.eventos.listeners.PartidaCazaEventListener;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;

import org.checkerframework.checker.units.qual.A;

public class PartidasFragment extends Fragment implements PartidaCazaEventListener, AtaqueEventListener {

    // Componentes de la interfaz
    private SeekBar seekBarCazadores, seekBarSoldados;
    private TextView textViewCazadores, textViewPartidaCaza, textViewSoldados;
    private ImageButton buttonCaza, buttonIncursion, buttonRanking;
    private long ultimoAtaque = 0;

    public PartidasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_partidas, container, false);

        // Inicializar componentes de la interfaz
        seekBarCazadores = view.findViewById(R.id.seekBarCazadores);
        seekBarSoldados = view.findViewById(R.id.seekBarSoldados);
        textViewCazadores = view.findViewById(R.id.textViewCazadores);
        textViewPartidaCaza = view.findViewById(R.id.textViewPartidaCaza);
        textViewSoldados = view.findViewById(R.id.textViewSoldados);
        buttonCaza = view.findViewById(R.id.buttonCaza);
        buttonIncursion = view.findViewById(R.id.buttonIncursion);
        buttonRanking = view.findViewById(R.id.buttonRanking);

        // Listeners
        seekBarCazadores.setOnSeekBarChangeListener(seekBarCazaChangeListener);
        seekBarSoldados.setOnSeekBarChangeListener(seekBarAtaqueChangeListener);
        buttonCaza.setOnClickListener(buttonCazaOnClickListener);
        buttonIncursion.setOnClickListener(buttonIncursionOnClickListener);
        buttonRanking.setOnClickListener(buttonRankingOnClickListener);

        // Mostrar el valor de la seekbar en el textview
        textViewCazadores.setText(getString(R.string.texto_num_cazadores, seekBarCazadores.getProgress()));
        textViewSoldados.setText(getString(R.string.texto_num_soldados, seekBarSoldados.getProgress()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Establecer el minimo y el maximo de la seekbar
        int maxCazadores = Aldea.getInstance().getCabaniaCaza().getAldeanosMaximos();
        seekBarCazadores.setMin(0);
        seekBarCazadores.setMax(maxCazadores);

        int maxSoldados = Aldea.getInstance().getCastillo().getAldeanosMaximos();
        seekBarSoldados.setMin(0);
        seekBarSoldados.setMax(maxSoldados);

        comprobarEstadoBoton();
        addEventListeners();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeEventListeners();
    }

    @Override
    public void onPause() {
        super.onPause();
        removeEventListeners();
    }

    // Manejar los botones
    View.OnClickListener buttonCazaOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int cazadoresSeleccionados = seekBarCazadores.getProgress();

            if (cazadoresSeleccionados < 1) {
                Toast.makeText(getActivity(), getString(R.string.msj_selecciona_minimo, 1), Toast.LENGTH_SHORT).show();
            } else if (cazadoresSeleccionados > Aldea.getInstance().getPoblacion()) {
                Toast.makeText(getActivity(), getString(R.string.msj_aldeanos_insuficientes), Toast.LENGTH_SHORT).show();
            } else if (!(ControladorRecursos.getCantidadRecurso(Aldea.getInstance().getRecursos(), RecursosEnum.COMIDA) < RecursosEnum.COMIDA.getMax())) {
                Toast.makeText(getActivity(), getString(R.string.msj_caza_sin_sentido), Toast.LENGTH_LONG).show();
            } else {
                int tiempoTotal = 10000;

                Toast.makeText(getActivity(), getString(R.string.msj_partida_caza_iniciada, cazadoresSeleccionados), Toast.LENGTH_LONG).show();
                buttonCaza.setEnabled(false);

                Aldea.getInstance().getCabaniaCaza().iniciarPartidaCaza(cazadoresSeleccionados, tiempoTotal);
                addEventListeners();
                Aldea.getInstance().getCabaniaCaza().getTimerPartidaCaza().getLanzadorEventos().addEventListener((PartidaCazaEventListener) getActivity());
            }
        }
    };

    View.OnClickListener buttonIncursionOnClickListener = new View.OnClickListener()  {
        @Override
        public void onClick(View v) {
            final long TIEMPO_ENTRE_ACCIONES = 10 * 60 * 1000; // 10 minutos
            int soldadosSeleccionados = seekBarSoldados.getProgress();

            if (soldadosSeleccionados < 1) {
                Toast.makeText(getActivity(), getString(R.string.msj_selecciona_minimo, 1), Toast.LENGTH_SHORT).show();
            } else if (soldadosSeleccionados > Aldea.getInstance().getPoblacion()) {
                Toast.makeText(getActivity(), getString(R.string.msj_aldeanos_insuficientes), Toast.LENGTH_SHORT).show();
            } else {
                long ahora = System.currentTimeMillis();
                if (ahora - Aldea.getInstance().getCastillo().getUltimoAtaque() >= TIEMPO_ENTRE_ACCIONES) {
                    Aldea.getInstance().getCastillo().iniciarIncursion(soldadosSeleccionados);
                    Aldea.getInstance().getCastillo().setUltimoAtaque(ahora);
                } else Toast.makeText(getActivity(), "Puedes atacar cada 10 minutos", Toast.LENGTH_SHORT).show();
            }
        }
    };

    View.OnClickListener buttonRankingOnClickListener = new View.OnClickListener()  {
        @Override
        public void onClick(View v) {
            GestorRealTimeDatabase gestorRealTimeDatabase = new GestorRealTimeDatabase();
            gestorRealTimeDatabase.printRanking();
        }
    };

    // Manejar la seekbar
    private final SeekBar.OnSeekBarChangeListener seekBarCazaChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // Actualizar el textview con el progreso de la seekbar
            textViewCazadores.setText(getString(R.string.texto_num_cazadores, progress));
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private final SeekBar.OnSeekBarChangeListener seekBarAtaqueChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // Actualizar el textview con el progreso de la seekbar
            textViewSoldados.setText(getString(R.string.texto_num_soldados, progress));
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


    public void comprobarEstadoBoton() {
        buttonCaza.setEnabled(!Aldea.getInstance().getCabaniaCaza().isPartidaActiva());
    }


    private void addEventListeners() {
        if (Aldea.getInstance().getCabaniaCaza().getTimerPartidaCaza() != null)
            Aldea.getInstance().getCabaniaCaza().getTimerPartidaCaza().getLanzadorEventos().addEventListener(this);
        Aldea.getInstance().getCastillo().getLanzadorEventos().addEventListener(this);
    }

    private void removeEventListeners() {
        if (Aldea.getInstance().getCabaniaCaza().getTimerPartidaCaza() != null)
            Aldea.getInstance().getCabaniaCaza().getTimerPartidaCaza().getLanzadorEventos().removeEventListener(this);
        Aldea.getInstance().getCastillo().getLanzadorEventos().removeEventListener(this);
    }

    // Manejar eventos de la partida de caza
    @Override
    public void onTimerTick() {
        textViewPartidaCaza.setText(
                getString(R.string.texto_partida_caza,
                        Aldea.getInstance().getCabaniaCaza().getAldeanosAsignados(),
                        Aldea.getInstance().getCabaniaCaza().getTimerPartidaCaza().getSegundosRestantes())
        );
    }

    @Override
    public void onFinalizarPartida() {
        buttonCaza.setEnabled(true);
        textViewPartidaCaza.setText(getString(R.string.texto_partida_caza_inactiva));
    }
    
    // Manejar eventos del ataque
    @Override
    public void onAtaqueTerminado(boolean victoria) {
        Toast.makeText(getActivity(),
                victoria ? getString(R.string.msj_victoria_ataque) : getString(R.string.msj_derrota_ataque),
                Toast.LENGTH_LONG
        ).show();
    }

    @Override
    public void onError(Exception e) {
        Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}