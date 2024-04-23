package com.tfg.activities.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tfg.R;
import com.tfg.controladores.ControladorAldea;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.EdificiosEnum;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SenadoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SenadoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SenadoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SenadoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SenadoFragment newInstance(String param1, String param2) {
        SenadoFragment fragment = new SenadoFragment();
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
    private SeekBar seekBarLeniadores;
    private TextView textViewLeniadores, textViewNivelCasetaLeniador, textViewNivelSenado;

    private Aldea aldea = Aldea.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_senado, container, false);

        // Inicializar componentes de la interfaz
        seekBarLeniadores = view.findViewById(R.id.seekBarLeniadores);
        textViewLeniadores = view.findViewById(R.id.textViewLeniadores);
        textViewNivelCasetaLeniador = view.findViewById(R.id.textViewNivelCasetaLeniador);
        textViewNivelSenado = view.findViewById(R.id.nivelSenado);

        // Listeners
        seekBarLeniadores.setOnSeekBarChangeListener(seekBarChangeListener);

        // Establecer el minimo y el maximo de la seekbar
        int maxLeniadores = ControladorAldea.getAldeanosMaximosEdificio(EdificiosEnum.CASETA_LENIADOR);
        seekBarLeniadores.setProgress(ControladorAldea.getAldeanosAsignadosEdificio(EdificiosEnum.CASETA_LENIADOR));
        seekBarLeniadores.setMin(0);
        seekBarLeniadores.setMax(maxLeniadores);
        textViewLeniadores.setText(String.valueOf(seekBarLeniadores.getProgress()));
        textViewNivelCasetaLeniador.setText(String.valueOf(aldea.getCasetaLeniador().getNivel()));

        //senado
        textViewNivelSenado.setText(String.valueOf(aldea.getNivel()));

        return view;
    }


    public void MejorarSenado(View view){
        aldea.setNivel(aldea.getNivel()+1);
    }


    private final SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            textViewLeniadores.setText(String.valueOf(seekBar.getProgress()));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            ControladorAldea.modificarAldeanosAsignados(EdificiosEnum.CASETA_LENIADOR, seekBar.getProgress());
        }
    };
}