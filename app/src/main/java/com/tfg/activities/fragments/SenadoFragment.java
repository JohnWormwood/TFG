package com.tfg.activities.fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tfg.R;
import com.tfg.controladores.ControladorAldea;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    private SeekBar seekBarLeniadores, seekBarCarpinteros;
    // Senado
    private TextView textViewNivelSenado, textViewPrecioTroncosCarpinteria, textViewPrecioPiedraSenado,
            textViewPrecioOroSenado, textViewPrecioTroncosSenado, textViewPrecioTablonesSenado,
            textViewPrecioHierroSenado;

    private List<TextView> textViewsSenado;


    private TextView textViewLeniadores, textViewNivelCasetaLeniador, textViewNivelCabaniaCaza,
            textViewNivelCarpinteria, textViewCarpinteros, textViewPrecioTroncosCabainaCaza,
            textViewPrecioTroncosCasetaLeniador;
    private Button buttonMejorarSenado, buttonMejoraraCabaniaCaza;

    private ConstraintLayout layoutCarpinteria;

    private Aldea aldea = Aldea.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_senado, container, false);
        configInicial(view);
        return view;
    }

    // --- LISTENERS ---
    public final View.OnClickListener buttonMejorarSenadoOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (aldea.aumentarNivel()) {
                textViewNivelSenado.setText(String.valueOf(aldea.getNivel()));
                layoutCarpinteria.setVisibility(View.VISIBLE);
                textViewPrecioTroncosSenado.setText(String.valueOf(ControladorAldea.getPreciosMejoraAldea().getOrDefault(RecursosEnum.TRONCOS_MADERA, 0)));
                textViewPrecioPiedraSenado.setText(String.valueOf(ControladorAldea.getPreciosMejoraAldea().getOrDefault(RecursosEnum.PIEDRA, 0)));
                textViewPrecioTablonesSenado.setText(String.valueOf(ControladorAldea.getPreciosMejoraAldea().getOrDefault(RecursosEnum.TABLONES_MADERA, 0)));
                textViewPrecioPiedraSenado.setText(String.valueOf(ControladorAldea.getPreciosMejoraAldea().getOrDefault(RecursosEnum.PIEDRA, 0)));
                textViewPrecioHierroSenado.setText(String.valueOf(ControladorAldea.getPreciosMejoraAldea().getOrDefault(RecursosEnum.HIERRO, 0)));
                textViewPrecioOroSenado.setText(String.valueOf(ControladorAldea.getPreciosMejoraAldea().getOrDefault(RecursosEnum.ORO, 0)));
            } else {
                Toast.makeText(getActivity(), "No tienes suficientes recursos", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public final View.OnClickListener buttonMejorarCabaniaCazaOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (aldea.getCabaniaCaza().aumentarNivel()) {
                textViewNivelCabaniaCaza.setText(String.valueOf(aldea.getCabaniaCaza().getNivel()));
                textViewPrecioTroncosCabainaCaza.setText(String.valueOf(ControladorAldea.getPreciosMejoraEdificio(aldea.getCabaniaCaza()).get(RecursosEnum.TRONCOS_MADERA)));
            } else {
                Toast.makeText(getActivity(), "No tienes suficientes recursos", Toast.LENGTH_SHORT).show();
            }
        }
    };

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
            aldea.getCasetaLeniador().modificarAldeanosAsignados(seekBar.getProgress());
        }
    };

    private void configInicial(View view) {
        inicializarComponentes(view);
        establecerVisibilidadLayouts();
        cargarListeners();
        configurarSeekbars();
        cargarValoresInicialesTextViews();
    }

    private void inicializarComponentes(View view) {
        // Layouts
        layoutCarpinteria = view.findViewById(R.id.layoutCarpinteria);
        // Seekbars
        seekBarLeniadores = view.findViewById(R.id.seekBarLeniadores);
        seekBarCarpinteros = view.findViewById(R.id.seekBarCarpinteros);
        // TextViews
        // Senado
        textViewNivelSenado = view.findViewById(R.id.nivelSenado);
        textViewPrecioTroncosSenado = view.findViewById(R.id.textViewPrecioTroncosSenado);
        textViewPrecioPiedraSenado = view.findViewById(R.id.textViewPrecioPiedraSenado);
        textViewPrecioOroSenado = view.findViewById(R.id.textViewPrecioOroSenado);
        textViewPrecioTablonesSenado = view.findViewById(R.id.textViewPrecioTablonesSenado);
        textViewPrecioHierroSenado = view.findViewById(R.id.textViewPrecioHierroSenado);
        textViewsSenado = Arrays.asList(textViewNivelSenado, textViewPrecioTroncosCarpinteria, textViewPrecioPiedraSenado,
                textViewPrecioOroSenado, textViewPrecioTroncosSenado, textViewPrecioTablonesSenado,
                textViewPrecioHierroSenado);
        // Caseta Leniador
        textViewLeniadores = view.findViewById(R.id.textViewLeniadores);
        textViewNivelCasetaLeniador = view.findViewById(R.id.textViewNivelCasetaLeniador);
        textViewPrecioTroncosCasetaLeniador = view.findViewById(R.id.textViewPrecioTroncosLeniador);
        // Cabania Caza
        textViewNivelCabaniaCaza = view.findViewById(R.id.textViewNivelCabaniaCaza);
        textViewPrecioTroncosCabainaCaza = view.findViewById(R.id.textViewPrecioTroncosCaza);
        // Carpinteria
        textViewCarpinteros = view.findViewById(R.id.textViewCarpinteros);
        textViewNivelCarpinteria = view.findViewById(R.id.textViewNivelCarpinteria);
        textViewPrecioTroncosCarpinteria = view.findViewById(R.id.textViewPrecioTroncosCarpinteria);

        // Botones
        buttonMejorarSenado = view.findViewById(R.id.btnMejorarSenado);
        buttonMejoraraCabaniaCaza = view.findViewById(R.id.buttonMejorarCabaniaCaza);
    }

    private void establecerVisibilidadLayouts() {
        layoutCarpinteria.setVisibility(View.GONE);
    }

    private void cargarListeners() {
        seekBarLeniadores.setOnSeekBarChangeListener(seekBarChangeListener);
        buttonMejorarSenado.setOnClickListener(buttonMejorarSenadoOnClick);
        buttonMejoraraCabaniaCaza.setOnClickListener(buttonMejorarCabaniaCazaOnClick);
    }
    private void configurarSeekbars() {
        // Establecer el minimo y el maximo de la seekbar
        int maxLeniadores = aldea.getCasetaLeniador().getAldeanosMaximos();
        seekBarLeniadores.setProgress(aldea.getCasetaLeniador().getAldeanosAsignados());
        seekBarLeniadores.setMin(0);
        seekBarLeniadores.setMax(maxLeniadores);
    }

    private void cargarValoresInicialesTextViews() {
        Map<RecursosEnum, Integer> preciosMejora;
        // Senado
        preciosMejora = ControladorAldea.getPreciosMejoraAldea();
        textViewNivelSenado.setText(String.valueOf(aldea.getNivel()));
        textViewPrecioTroncosSenado.setText(String.valueOf(preciosMejora.getOrDefault(RecursosEnum.TRONCOS_MADERA, 0)));
        textViewPrecioPiedraSenado.setText(String.valueOf(preciosMejora.getOrDefault(RecursosEnum.PIEDRA, 0)));
        textViewPrecioOroSenado.setText(String.valueOf(preciosMejora.getOrDefault(RecursosEnum.ORO, 0)));
        textViewPrecioTablonesSenado.setText(String.valueOf(preciosMejora.getOrDefault(RecursosEnum.TABLONES_MADERA, 0)));
        textViewPrecioHierroSenado.setText(String.valueOf(preciosMejora.getOrDefault(RecursosEnum.HIERRO, 0)));
        // Caseta leniador
        preciosMejora = ControladorAldea.getPreciosMejoraEdificio(aldea.getCasetaLeniador());
        textViewLeniadores.setText(String.valueOf(seekBarLeniadores.getProgress()));
        textViewNivelCasetaLeniador.setText(String.valueOf(aldea.getCasetaLeniador().getNivel()));
        textViewPrecioTroncosCasetaLeniador.setText(String.valueOf(preciosMejora.get(RecursosEnum.TRONCOS_MADERA)));
        // Cabania caza
        preciosMejora = ControladorAldea.getPreciosMejoraEdificio(aldea.getCabaniaCaza());
        textViewNivelCabaniaCaza.setText(String.valueOf(aldea.getCabaniaCaza().getNivel()));
        textViewPrecioTroncosCabainaCaza.setText(String.valueOf(preciosMejora.get(RecursosEnum.TRONCOS_MADERA)));
        // Carpinteria
        preciosMejora = ControladorAldea.getPreciosMejoraEdificio(aldea.getCarpinteria());
        textViewCarpinteros.setText(String.valueOf(seekBarCarpinteros.getProgress()));
        textViewNivelCarpinteria.setText(String.valueOf(aldea.getCarpinteria().getNivel()));
        textViewPrecioTroncosCarpinteria.setText(String.valueOf(preciosMejora.get(RecursosEnum.TRONCOS_MADERA)));
    }
}