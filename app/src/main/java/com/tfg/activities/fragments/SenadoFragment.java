package com.tfg.activities.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tfg.R;
import com.tfg.modelos.Aldea;
import com.tfg.ui.MenuEdificioAsignable;
import com.tfg.ui.MenuEstructuraBase;
import com.tfg.ui.MenuSenado;

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
    private MenuSenado menuSenado;
    private MenuEstructuraBase menuCabaniaCaza;
    private MenuEdificioAsignable menuCasetaLeniador;
    private static MenuEdificioAsignable menuMina;
    private static MenuEdificioAsignable menuCarpinteria;

    private Aldea aldea = Aldea.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_senado, container, false);
        configInicial(view);
        return view;
    }



    private void configInicial(View view) {
        inicializarComponentes(view);
        actualizarVisibilidadLayouts();
        cargarUI();
    }

    private void inicializarComponentes(View view) {
        // Senado
        menuSenado = new MenuSenado(
                getActivity(),
                aldea,
                null,
                view.findViewById(R.id.textViewnivelSenado),
                view.findViewById(R.id.textViewPrecioTroncosSenado),
                view.findViewById(R.id.textViewPrecioPiedraSenado),
                view.findViewById(R.id.textViewPrecioTablonesSenado),
                view.findViewById(R.id.textViewPrecioHierroSenado),
                view.findViewById(R.id.textViewPrecioOroSenado),
                view.findViewById(R.id.buttonMejorarSenado)
        );
        // Caseta Leniador
        menuCasetaLeniador = new MenuEdificioAsignable(
                getActivity(),
                aldea.getCasetaLeniador(),
                null,
                view.findViewById(R.id.textViewNivelCasetaLeniador),
                view.findViewById(R.id.textViewPrecioTroncosCasetaLeniador),
                view.findViewById(R.id.textViewPrecioPiedraCasetaLeniador),
                view.findViewById(R.id.textViewPrecioTablonesCasetaLeniador),
                view.findViewById(R.id.textViewPrecioHierroCasetaLeniador),
                view.findViewById(R.id.textViewPrecioOroCasetaLeniador),
                view.findViewById(R.id.buttonMejorarCasetaLeniador),
                view.findViewById(R.id.seekBarLeniadores),
                view.findViewById(R.id.textViewLeniadores)
        );
        // Cabania Caza
        menuCabaniaCaza = new MenuEstructuraBase(
                getActivity(),
                aldea.getCabaniaCaza(),
                null,
                view.findViewById(R.id.textViewNivelCabaniaCaza),
                view.findViewById(R.id.textViewPrecioTroncosCabaniaCaza),
                view.findViewById(R.id.textViewPrecioPiedraCabaniaCaza),
                view.findViewById(R.id.textViewPrecioTablonesCabaniaCaza),
                view.findViewById(R.id.textViewPrecioHierroCabaniaCaza),
                view.findViewById(R.id.textViewPrecioOroCabaniaCaza),
                view.findViewById(R.id.buttonMejorarCabaniaCaza)
        );
        // Mina
        menuMina = new MenuEdificioAsignable(
                getActivity(),
                aldea.getMina(),
                view.findViewById(R.id.layoutMina),
                view.findViewById(R.id.textViewNivelMina),
                view.findViewById(R.id.textViewPrecioTroncosMina),
                view.findViewById(R.id.textViewPrecioPiedraMina),
                view.findViewById(R.id.textViewPrecioTablonesMina),
                view.findViewById(R.id.textViewPrecioHierroMina),
                view.findViewById(R.id.textViewPrecioOroMina),
                view.findViewById(R.id.buttonMejorarMina),
                view.findViewById(R.id.seekBarMineros),
                view.findViewById(R.id.textViewMineros)
        );
        // Carpinteria
        menuCarpinteria = new MenuEdificioAsignable(
                getActivity(),
                aldea.getCarpinteria(),
                view.findViewById(R.id.layoutCarpinteria),
                view.findViewById(R.id.textViewNivelCarpinteria),
                view.findViewById(R.id.textViewPrecioTroncosCarpinteria),
                view.findViewById(R.id.textViewPrecioPiedraCarpinteria),
                view.findViewById(R.id.textViewPrecioTablonesCarpinteria),
                view.findViewById(R.id.textViewPrecioHierroCarpinteria),
                view.findViewById(R.id.textViewPrecioOroCarpinteria),
                view.findViewById(R.id.buttonMejorarCarpinteria),
                view.findViewById(R.id.seekBarCarpinteros),
                view.findViewById(R.id.textViewCarpinteros)
        );
    }

    /*
     * Esta funcion es publica y estatica para poder llamarla desde el MenuSenado para
     * añadir los layouts a la interfaz segun las subidas de nivel
     */
    public static void actualizarVisibilidadLayouts() {
        Aldea aldea = Aldea.getInstance();
        menuMina.getLayout().setVisibility(aldea.getMina().isDesbloqueado() ? View.VISIBLE : View.GONE);
        menuCarpinteria.getLayout().setVisibility(aldea.getCarpinteria().isDesbloqueado() ? View.VISIBLE : View.GONE);
    }

    private void cargarUI() {
        menuSenado.iniciar();
        menuCabaniaCaza.iniciar();
        menuCasetaLeniador.iniciar();
        menuMina.iniciar();
        menuCarpinteria.iniciar();
    }
}