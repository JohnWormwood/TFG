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
import com.tfg.eventos.listeners.PartidaCazaEventListener;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.utilidades.Constantes;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MercaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MercaderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button buttonComprarTablones;


    public MercaderFragment() {
        // Required empty public constructor
    }

    public static MercaderFragment newInstance(String param1, String param2) {
        MercaderFragment fragment = new MercaderFragment();
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