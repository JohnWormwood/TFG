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
import com.tfg.eventos.listeners.PartidaCazaEventListener;
import com.tfg.modelos.Aldea;

public class PartidasFragment extends Fragment implements PartidaCazaEventListener {

    // Componentes de la interfaz
    private SeekBar seekBarCazadores;
    private TextView textViewCazadores, textViewPartidaCaza;
    private ImageButton buttonCaza;

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
        textViewCazadores = view.findViewById(R.id.textViewCazadores);
        textViewPartidaCaza = view.findViewById(R.id.textViewPartidaCaza);
        buttonCaza = view.findViewById(R.id.buttonCaza);

        // Listeners
        seekBarCazadores.setOnSeekBarChangeListener(seekBarChangeListener);
        buttonCaza.setOnClickListener(buttonCazaOnClickListener);

        // Establecer el minimo y el maximo de la seekbar
        int maxCazadores = Aldea.getInstance().getCabaniaCaza().getAldeanosMaximos();
        seekBarCazadores.setMin(0);
        seekBarCazadores.setMax(maxCazadores);

        // Mostrar el valor de la seekbar en el textview
        textViewCazadores.setText(getString(R.string.texto_num_cazadores, seekBarCazadores.getProgress()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
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

    // Manejar el boton
    View.OnClickListener buttonCazaOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int cazadoresSeleccionados = seekBarCazadores.getProgress();

            if (cazadoresSeleccionados < 1) {
                Toast.makeText(getActivity(), getString(R.string.msj_selecciona_minimo, 1), Toast.LENGTH_SHORT).show();
            } else if (cazadoresSeleccionados > Aldea.getInstance().getPoblacion()) {
                Toast.makeText(getActivity(), getString(R.string.msj_aldeanos_insuficientes), Toast.LENGTH_SHORT).show();
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

    // Manejar la seekbar
    private final SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
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