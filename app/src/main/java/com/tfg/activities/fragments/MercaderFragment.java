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

    private Button buttonComprarTablones;


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

        buttonComprarTablones = view.findViewById(R.id.buttonComprarTablones);
        buttonComprarTablones.setOnClickListener(buttonComprarTablonesOnClickListener);


        // Inflate the layout for this fragment
        return view;
    }

    View.OnClickListener buttonComprarTablonesOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (ControladorRecursos.consumirRecurso(Aldea.getInstance().getRecursos(),RecursosEnum.ORO,Constantes.Mercader.PRECIO_TABLONES)){
                ControladorRecursos.agregarRecurso(Aldea.getInstance().getRecursos(), RecursosEnum.TABLONES_MADERA,Constantes.Mercader.CANTIDAD);
            } else {
                Toast.makeText(getActivity(), "No tienes suficiente Oro", Toast.LENGTH_SHORT).show();
            }
        }
    };
}