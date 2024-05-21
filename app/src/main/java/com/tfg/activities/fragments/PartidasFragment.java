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
import com.tfg.controladores.ControladorRecursos;
import com.tfg.eventos.listeners.PartidaCazaEventListener;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;

public class PartidasFragment extends Fragment implements PartidaCazaEventListener {

    // Componentes de la interfaz
    private SeekBar seekBarCazadores, seekBarSoldados;
    private TextView textViewCazadores, textViewPartidaCaza, textViewSoldados;
    private ImageButton buttonCaza, buttonIncursion;

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

        // Listeners
        seekBarCazadores.setOnSeekBarChangeListener(seekBarCazaChangeListener);
        seekBarSoldados.setOnSeekBarChangeListener(seekBarAtaqueChangeListener);
        buttonCaza.setOnClickListener(buttonCazaOnClickListener);
        buttonIncursion.setOnClickListener(buttonIncursionOnClickListener);

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
        addListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Aldea.getInstance().getCabaniaCaza().getTimerPartidaCaza() != null)
            Aldea.getInstance().getCabaniaCaza().getTimerPartidaCaza().removeEventListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Aldea.getInstance().getCabaniaCaza().getTimerPartidaCaza() != null)
            Aldea.getInstance().getCabaniaCaza().getTimerPartidaCaza().removeEventListener(this);
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
                addListener();
                Aldea.getInstance().getCabaniaCaza().getTimerPartidaCaza().addEventListener((PartidaCazaEventListener) getActivity());
            }
        }
    };

    View.OnClickListener buttonIncursionOnClickListener = new View.OnClickListener()  {
        @Override
        public void onClick(View v) {
            int soldadosSeleccionados = seekBarSoldados.getProgress();

            if (soldadosSeleccionados < 1) {
                Toast.makeText(getActivity(), getString(R.string.msj_selecciona_minimo, 1), Toast.LENGTH_SHORT).show();
            } else if (soldadosSeleccionados > Aldea.getInstance().getPoblacion()) {
                Toast.makeText(getActivity(), getString(R.string.msj_aldeanos_insuficientes), Toast.LENGTH_SHORT).show();
            } else {
                Aldea.getInstance().getCastillo().iniciarIncursion(soldadosSeleccionados);
            }
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

    // Manejar eventos de la partida de caza
    private void addListener() {
        if (Aldea.getInstance().getCabaniaCaza().getTimerPartidaCaza() != null)
            Aldea.getInstance().getCabaniaCaza().getTimerPartidaCaza().addEventListener(this);
    }

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
}