package com.tfg.activities.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.tfg.R;
import com.tfg.controladores.ControladorRecursos;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.utilidades.Constantes;

public class MercaderFragment extends Fragment {

    private Button buttonComprarTablones, buttonComprarTroncos,buttonComprarComida,buttonComprarHierro,buttonComprarPiedra;


    public MercaderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mercader, container, false);

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


        // Inflate the layout for this fragment
        return view;
    }

    View.OnClickListener comprarRecursoListener = new View.OnClickListener() {
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
            }else if (v.getId() == R.id.buttonComprarComida) {
                recurso = RecursosEnum.COMIDA;
                precio = Constantes.Mercader.PRECIO_COMIDA;
            }else if (v.getId() == R.id.buttonComprarPiedra) {
                recurso = RecursosEnum.PIEDRA;
                precio = Constantes.Mercader.PRECIO_PIEDRA;
            }else if (v.getId() == R.id.buttonComprarHierro) {
                recurso = RecursosEnum.HIERRO;
                precio = Constantes.Mercader.PRECIO_HIERRO;
            } else {
                return;
            }

            // Realiza la compra del recurso
            if (ControladorRecursos.consumirRecurso(Aldea.getInstance().getRecursos(), RecursosEnum.ORO, precio)) {
                ControladorRecursos.agregarRecurso(Aldea.getInstance().getRecursos(), recurso, Constantes.Mercader.CANTIDAD);
            } else {
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "No tienes suficiente Oro", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

}