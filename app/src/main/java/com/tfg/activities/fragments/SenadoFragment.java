package com.tfg.activities.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tfg.R;
import com.tfg.controladores.ControladorAldea;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.ui.MenuEdificio;

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
    private MenuEdificio menuCabaniaCaza = new MenuEdificio();
    private MenuEdificio menuCasetaLeniador = new MenuEdificio();
    // Senado
    private TextView textViewNivelSenado, textViewPrecioTroncosCarpinteria, textViewPrecioPiedraSenado,
            textViewPrecioOroSenado, textViewPrecioTroncosSenado, textViewPrecioTablonesSenado,
            textViewPrecioHierroSenado;
    private Button buttonMejorarSenado;

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
                actualizarUISenado();
            } else {
                Toast.makeText(getActivity(), "No tienes suficientes recursos", Toast.LENGTH_SHORT).show();
            }
        }
    };


    private void configInicial(View view) {
        inicializarComponentes(view);
        establecerVisibilidadLayouts();
        cargarListeners();
        cargarUI();
    }

    private void inicializarComponentes(View view) {
        // Senado
        textViewNivelSenado = view.findViewById(R.id.nivelSenado);
        textViewPrecioTroncosSenado = view.findViewById(R.id.textViewPrecioTroncosSenado);
        textViewPrecioPiedraSenado = view.findViewById(R.id.textViewPrecioPiedraSenado);
        textViewPrecioOroSenado = view.findViewById(R.id.textViewPrecioOroSenado);
        textViewPrecioTablonesSenado = view.findViewById(R.id.textViewPrecioTablonesSenado);
        textViewPrecioHierroSenado = view.findViewById(R.id.textViewPrecioHierroSenado);
        buttonMejorarSenado = view.findViewById(R.id.buttonMejorarSenado);
        // Caseta Leniador
        menuCasetaLeniador.setContext(getActivity());
        menuCasetaLeniador.setEdificio(aldea.getCasetaLeniador());
        menuCasetaLeniador.setTextViewAldeanosAsignados(view.findViewById(R.id.textViewLeniadores));
        menuCasetaLeniador.setTextViewNivel(view.findViewById(R.id.textViewNivelCasetaLeniador));
        menuCasetaLeniador.setTextViewPrecioTroncos(view.findViewById(R.id.textViewPrecioTroncosCasetaLeniador));
        menuCasetaLeniador.setTextViewPrecioPiedra(view.findViewById(R.id.textViewPrecioPiedraCasetaLeniador));
        menuCasetaLeniador.setTextViewPrecioTablones(view.findViewById(R.id.textViewPrecioTablonesCasetaLeniador));
        menuCasetaLeniador.setTextViewPrecioHierro(view.findViewById(R.id.textViewPrecioHierroCasetaLeniador));
        menuCasetaLeniador.setTextViewPrecioOro(view.findViewById(R.id.textViewPrecioOroCasetaLeniador));
        menuCasetaLeniador.setButtonMejorar(view.findViewById(R.id.buttonMejorarCasetaLeniador));
        menuCasetaLeniador.setSeekBarAldeanosAsignados(view.findViewById(R.id.seekBarLeniadores));
        // Cabania Caza
        menuCabaniaCaza.setContext(getActivity());
        menuCabaniaCaza.setEdificio(aldea.getCabaniaCaza());
        menuCabaniaCaza.setTextViewNivel(view.findViewById(R.id.textViewNivelCabaniaCaza));
        menuCabaniaCaza.setTextViewPrecioTroncos(view.findViewById(R.id.textViewPrecioTroncosCabaniaCaza));
        menuCabaniaCaza.setTextViewPrecioPiedra(view.findViewById(R.id.textViewPrecioPiedraCabaniaCaza));
        menuCabaniaCaza.setTextViewPrecioTablones(view.findViewById(R.id.textViewPrecioTablonesCabaniaCaza));
        menuCabaniaCaza.setTextViewPrecioHierro(view.findViewById(R.id.textViewPrecioHierroCabaniaCaza));
        menuCabaniaCaza.setTextViewPrecioOro(view.findViewById(R.id.textViewPrecioOroCabaniaCaza));
        menuCabaniaCaza.setButtonMejorar(view.findViewById(R.id.buttonMejorarCabaniaCaza));

    }

    private void establecerVisibilidadLayouts() {

    }

    private void cargarListeners() {
        buttonMejorarSenado.setOnClickListener(buttonMejorarSenadoOnClick);
    }

    private void cargarUI() {
        actualizarUISenado();
        menuCabaniaCaza.iniciar();
        menuCasetaLeniador.iniciar();
    }

    private void actualizarUISenado() {
        textViewNivelSenado.setText(String.valueOf(aldea.getNivel()));
        textViewPrecioTroncosSenado.setText(String.valueOf(ControladorAldea.getPreciosMejoraAldea().getOrDefault(RecursosEnum.TRONCOS_MADERA, 0)));
        textViewPrecioPiedraSenado.setText(String.valueOf(ControladorAldea.getPreciosMejoraAldea().getOrDefault(RecursosEnum.PIEDRA, 0)));
        textViewPrecioTablonesSenado.setText(String.valueOf(ControladorAldea.getPreciosMejoraAldea().getOrDefault(RecursosEnum.TABLONES_MADERA, 0)));
        textViewPrecioPiedraSenado.setText(String.valueOf(ControladorAldea.getPreciosMejoraAldea().getOrDefault(RecursosEnum.PIEDRA, 0)));
        textViewPrecioHierroSenado.setText(String.valueOf(ControladorAldea.getPreciosMejoraAldea().getOrDefault(RecursosEnum.HIERRO, 0)));
        textViewPrecioOroSenado.setText(String.valueOf(ControladorAldea.getPreciosMejoraAldea().getOrDefault(RecursosEnum.ORO, 0)));
    }
}