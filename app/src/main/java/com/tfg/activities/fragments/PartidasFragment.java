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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PartidasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PartidasFragment extends Fragment implements PartidaCazaEventListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PartidasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PartidasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PartidasFragment newInstance(String param1, String param2) {
        PartidasFragment fragment = new PartidasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // Componentes de la interfaz
    private SeekBar seekBarCazadores;
    private TextView textViewCazadores, textViewPartidaCaza;
    private ImageButton buttonCaza;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_partidas, container, false);;

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
        textViewCazadores.setText(String.format(getString(R.string.texto_num_cazadores), seekBarCazadores.getProgress()));

        comprobarEstadoBoton();
        addListener();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Aldea.getInstance().getCabaniaCaza().getTimerPartidaCaza() != null)
            Aldea.getInstance().getCabaniaCaza().getTimerPartidaCaza().removeEventListener(this);
    }

    // Manejar el boton
    View.OnClickListener buttonCazaOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int cazadoresSeleccionados = seekBarCazadores.getProgress();

            if (cazadoresSeleccionados < 1) {
                Toast.makeText(getActivity(), "Debes seleccionar al menos 1", Toast.LENGTH_SHORT).show();
            } else if (cazadoresSeleccionados > Aldea.getInstance().getPoblacion()) {
                Toast.makeText(getActivity(), "No tienes suficientes aldeanos", Toast.LENGTH_SHORT).show();
            } else {
                int tiempoTotal = 10000;

                Toast.makeText(getActivity(), "Partida de caza iniciada con "+cazadoresSeleccionados+" cazadores", Toast.LENGTH_LONG).show();
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
            textViewCazadores.setText(String.format(getString(R.string.texto_num_cazadores), progress));
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
        String mensaje = getString(R.string.texto_partida_caza);
        textViewPartidaCaza.setText(String.format(
                mensaje,
                Aldea.getInstance().getCabaniaCaza().getAldeanosAsignados(),
                Aldea.getInstance().getCabaniaCaza().getTimerPartidaCaza().getSegundosRestantes()
        ));
    }

    @Override
    public void onFinalizarPartida() {
        buttonCaza.setEnabled(true);
        textViewPartidaCaza.setText(getString(R.string.texto_partida_caza_inactiva));
    }
}