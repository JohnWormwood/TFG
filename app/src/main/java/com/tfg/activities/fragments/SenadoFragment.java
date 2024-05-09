package com.tfg.activities.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tfg.R;
import com.tfg.eventos.listeners.ActualizarInterfazEventListener;
import com.tfg.modelos.Aldea;
import com.tfg.ui.MenuEdificioAsignable;
import com.tfg.ui.MenuEstructuraBase;
import com.tfg.ui.MenuSenado;

import lombok.Getter;

public class SenadoFragment extends Fragment implements ActualizarInterfazEventListener {

    // Componentes de la interfaz
    private MenuSenado menuSenado;
    private MenuEstructuraBase menuCabaniaCaza;
    private MenuEdificioAsignable menuCasetaLeniador;
    private MenuEdificioAsignable menuMina;
    private MenuEdificioAsignable menuCarpinteria;
    private MenuEdificioAsignable menuGranja;
    private MenuEdificioAsignable menuCastillo;

    private Aldea aldea = Aldea.getInstance();

    public SenadoFragment() {
        // Required empty public constructor
    }

    // --- FUNCIONES PARA CONTROLAR EL FRAGMENT ---
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_senado, container, false);
        configInicial(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        menuSenado.addEventListener(this);
        menuSenado.addEventListener((ActualizarInterfazEventListener) getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        menuSenado.removeEventListener(this);
        menuSenado.removeEventListener((ActualizarInterfazEventListener) getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        menuSenado.removeEventListener(this);
        menuSenado.removeEventListener((ActualizarInterfazEventListener) getActivity());
    }

    // IMPLEMENTACION DE ActualizarInterfazEventListener
    @Override
    public void onActualizarInterfaz() {
        actualizarVisibilidadLayouts();
    }

    // --- FUNCIONES PARA INICIALIZAR CORRECTAMENTE LA UI ---
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
        // Granja
        menuGranja = new MenuEdificioAsignable(
                getActivity(),
                aldea.getGranja(),
                view.findViewById(R.id.layoutGranja),
                view.findViewById(R.id.textViewNivelGranja),
                view.findViewById(R.id.textViewPrecioTroncosGranja),
                view.findViewById(R.id.textViewPrecioPiedraGranja),
                view.findViewById(R.id.textViewPrecioTablonesGranja),
                view.findViewById(R.id.textViewPrecioHierroGranja),
                view.findViewById(R.id.textViewPrecioOroGranja),
                view.findViewById(R.id.buttonMejorarGranja),
                view.findViewById(R.id.seekBarGranjeros),
                view.findViewById(R.id.textViewGranjeros)
        );
        // Castillo
        /*menuCastillo = new MenuEdificioAsignable(
                getActivity(),
                aldea.getGranja(),
                view.findViewById(R.id.layoutGranja),
                view.findViewById(R.id.textViewNivelGranja),
                view.findViewById(R.id.textViewPrecioTroncosGranja),
                view.findViewById(R.id.textViewPrecioPiedraGranja),
                view.findViewById(R.id.textViewPrecioTablonesGranja),
                view.findViewById(R.id.textViewPrecioHierroGranja),
                view.findViewById(R.id.textViewPrecioOroGranja),
                view.findViewById(R.id.buttonMejorarGranja),
                view.findViewById(R.id.seekBarGranjeros),
                view.findViewById(R.id.textViewGranjeros)
        );*/
    }

    private void actualizarVisibilidadLayouts() {
        menuMina.getLayout().setVisibility(aldea.getMina().isDesbloqueado() ? View.VISIBLE : View.GONE);
        menuCarpinteria.getLayout().setVisibility(aldea.getCarpinteria().isDesbloqueado() ? View.VISIBLE : View.GONE);
        menuGranja.getLayout().setVisibility(aldea.getGranja().isDesbloqueado() ? View.VISIBLE : View.GONE);
    }

    private void cargarUI() {
        menuSenado.iniciar();
        menuCabaniaCaza.iniciar();
        menuCasetaLeniador.iniciar();
        menuMina.iniciar();
        menuCarpinteria.iniciar();
        menuGranja.iniciar();
        //menuCastillo.iniciar();
    }
}