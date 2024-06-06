package com.tfg.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.tfg.R;
import com.tfg.controladores.ControladorRecursos;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.utilidades.Constantes;

public class MercaderFragment extends Fragment {
    // Componentes interfaz
    private Button buttonComprarTablones, buttonComprarTroncos, buttonComprarComida,
            buttonComprarHierro, buttonComprarPiedra;
    private LinearLayout layoutMercado;
    private TextView textViewMsj;

    // Referencia a la aldea
    private final Aldea aldea = Aldea.getInstance();

    public MercaderFragment() {
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
        return inflater.inflate(R.layout.fragment_mercader, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        inicializarComponentes(requireView());
        actualizarVisibilidadLayouts();
    }

    // --- INTERFAZ ---
    private void inicializarComponentes(View view) {
        layoutMercado = view.findViewById(R.id.linearLayoutMercado);
        textViewMsj = view.findViewById(R.id.textViewMsj);
        // Botones de compra
        buttonComprarTablones = view.findViewById(R.id.buttonComprarTablones);
        buttonComprarTroncos = view.findViewById(R.id.buttonComprarTroncos);
        buttonComprarComida = view.findViewById(R.id.buttonComprarComida);
        buttonComprarPiedra = view.findViewById(R.id.buttonComprarPiedra);
        buttonComprarHierro = view.findViewById(R.id.buttonComprarHierro);

        // Establece el mismo listener para ambos botones
        buttonComprarTablones.setOnClickListener(comprarRecursoListener);
        buttonComprarTroncos.setOnClickListener(comprarRecursoListener);
        buttonComprarComida.setOnClickListener(comprarRecursoListener);
        buttonComprarPiedra.setOnClickListener(comprarRecursoListener);
        buttonComprarHierro.setOnClickListener(comprarRecursoListener);
    }

    private void actualizarVisibilidadLayouts() {
        layoutMercado.setVisibility(aldea.getNivel() >= Constantes.Aldea.NIVEL_DESBLOQUEO_MERCADER
                ? View.VISIBLE : View.GONE);
        textViewMsj.setVisibility(aldea.getNivel() >= Constantes.Aldea.NIVEL_DESBLOQUEO_MERCADER
                ? View.GONE : View.VISIBLE);
    }

    // --- LISTENERS ---
    private final View.OnClickListener comprarRecursoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecursosEnum recurso = null;
            int precio = 0;

            // Determina el recurso y el precio en función del botón presionado
            if (v.getId() == R.id.buttonComprarTablones) {
                recurso = RecursosEnum.TABLONES_MADERA;
                precio = Constantes.Mercader.PRECIO_TABLONES;
            } else if (v.getId() == R.id.buttonComprarTroncos) {
                recurso = RecursosEnum.TRONCOS_MADERA;
                precio = Constantes.Mercader.PRECIO_TRONCOS;
            } else if (v.getId() == R.id.buttonComprarComida) {
                recurso = RecursosEnum.COMIDA;
                precio = Constantes.Mercader.PRECIO_COMIDA;
            } else if (v.getId() == R.id.buttonComprarPiedra) {
                recurso = RecursosEnum.PIEDRA;
                precio = Constantes.Mercader.PRECIO_PIEDRA;
            } else if (v.getId() == R.id.buttonComprarHierro) {
                recurso = RecursosEnum.HIERRO;
                precio = Constantes.Mercader.PRECIO_HIERRO;
            } else {
                return;
            }

            // Realiza la compra del recurso si tiene menos del maximo
            if (ControladorRecursos.getCantidadRecurso(aldea.getRecursos(), recurso)
                    < recurso.getMax()) {
                if (ControladorRecursos.consumirRecurso(aldea.getRecursos(),
                        RecursosEnum.ORO, precio)) {
                    ControladorRecursos.agregarRecursoSinExcederMax(aldea.getRecursos(),
                            recurso, Constantes.Mercader.CANTIDAD);
                } else {
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), getString(R.string.msj_oro_insuficiente),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    };
}