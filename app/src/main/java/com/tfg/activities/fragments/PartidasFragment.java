package com.tfg.activities.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.tfg.R;
import com.tfg.activities.RankingActivity;
import com.tfg.bbdd.dto.UsuarioDTO;
import com.tfg.bbdd.firebase.GestorRealTimeDatabase;
import com.tfg.controladores.ControladorRecursos;
import com.tfg.eventos.callbacks.ObtenerUsuarioCallback;
import com.tfg.eventos.listeners.AtaqueEventListener;
import com.tfg.eventos.listeners.PartidaCazaEventListener;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.utilidades.Constantes;
import com.tfg.utilidades.UtilidadActivity;

public class PartidasFragment extends Fragment implements PartidaCazaEventListener, AtaqueEventListener, ObtenerUsuarioCallback {

    // Componentes de la interfaz
    private SeekBar seekBarCazadores, seekBarSoldados;
    private TextView textViewCazadores, textViewPartidaCaza, textViewSoldados, textViewCaza,textViewIncursion, textViewRanking;
    private ImageButton buttonCaza, buttonIncursion, buttonRanking;
    private long ultimoAtaque = 0;
    private LinearLayout linearLayout;
    private TextView textViewMsj;
    private Aldea aldea = Aldea.getInstance();
    private PartidasFragment context = this;

    public PartidasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_partidas, container, false);

        linearLayout = view.findViewById(R.id.linearLayoutIncursion);
        textViewMsj = view.findViewById(R.id.textViewMsj);

        // Inicializar componentes de la interfaz
        seekBarCazadores = view.findViewById(R.id.seekBarCazadores);
        seekBarSoldados = view.findViewById(R.id.seekBarSoldados);
        textViewCazadores = view.findViewById(R.id.textViewCazadores);
        textViewPartidaCaza = view.findViewById(R.id.textViewPartidaCaza);
        textViewSoldados = view.findViewById(R.id.textViewSoldados);
        buttonCaza = view.findViewById(R.id.buttonCaza);
        buttonIncursion = view.findViewById(R.id.buttonIncursion);
        buttonRanking = view.findViewById(R.id.buttonRanking);

        textViewCaza = view.findViewById(R.id.textViewCaza);
        UtilidadActivity.setEfectoBoton(buttonCaza,textViewCaza);

        textViewIncursion = view.findViewById(R.id.textViewRegistrarse);
        UtilidadActivity.setEfectoBoton(buttonIncursion,textViewIncursion);

        textViewRanking = view.findViewById(R.id.textViewLogin);
        UtilidadActivity.setEfectoBoton(buttonRanking,textViewRanking);

        // Listeners
        seekBarCazadores.setOnSeekBarChangeListener(seekBarCazaChangeListener);
        seekBarSoldados.setOnSeekBarChangeListener(seekBarAtaqueChangeListener);
        buttonCaza.setOnClickListener(buttonCazaOnClickListener);
        buttonIncursion.setOnClickListener(buttonIncursionOnClickListener);
        buttonRanking.setOnClickListener(buttonRankingOnClickListener);

        // Mostrar el valor de la seekbar en el textview
        textViewCazadores.setText(getString(R.string.texto_num_cazadores, seekBarCazadores.getProgress()));
        textViewSoldados.setText(getString(R.string.texto_num_soldados, seekBarSoldados.getProgress()));

        return view;
    }
    private void actualizarVisibilidadLayouts(){
        linearLayout.setVisibility(aldea.getNivel() >= Constantes.Aldea.NIVEL_DESBLOQUEO_CASTILLO ?  View.VISIBLE: View.GONE);
        textViewMsj.setVisibility(aldea.getNivel() >= Constantes.Aldea.NIVEL_DESBLOQUEO_CASTILLO ? View.GONE : View.VISIBLE);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Establecer el minimo y el maximo de la seekbar
        int maxCazadores = Aldea.getInstance().getCabaniaCaza().getAldeanosMaximos();
        seekBarCazadores.setMin(0);
        seekBarCazadores.setMax(maxCazadores);

        int maxSoldados = Aldea.getInstance().getCastillo().getAldeanosMaximos();
        seekBarSoldados.setMin(0);
        seekBarSoldados.setMax(maxSoldados);

        comprobarEstadoBoton();
        addEventListeners();
        actualizarVisibilidadLayouts();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeEventListeners();
    }

    @Override
    public void onPause() {
        super.onPause();
        removeEventListeners();
    }

    // Manejar los botones
    View.OnClickListener buttonCazaOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int cazadoresSeleccionados = seekBarCazadores.getProgress();

            if (cazadoresSeleccionados < 1) {
                Toast.makeText(getActivity(), getString(R.string.msj_selecciona_minimo, 1), Toast.LENGTH_SHORT).show();
            } else if (cazadoresSeleccionados > Aldea.getInstance().getPoblacion()) {
                Toast.makeText(getActivity(), getString(R.string.msj_aldeanos_insuficientes), Toast.LENGTH_SHORT).show();
            } else if (!(ControladorRecursos.getCantidadRecurso(Aldea.getInstance().getRecursos(), RecursosEnum.COMIDA) < RecursosEnum.COMIDA.getMax())) {
                Toast.makeText(getActivity(), getString(R.string.msj_caza_sin_sentido), Toast.LENGTH_LONG).show();
            } else {
                int tiempoTotal = 10000;

                Toast.makeText(getActivity(), getString(R.string.msj_partida_caza_iniciada, cazadoresSeleccionados), Toast.LENGTH_LONG).show();
                buttonCaza.setEnabled(false);

                Aldea.getInstance().getCabaniaCaza().iniciarPartidaCaza(cazadoresSeleccionados, tiempoTotal);
                addEventListeners();
                Aldea.getInstance().getCabaniaCaza().getTimerPartidaCaza().getLanzadorEventos().addEventListener((PartidaCazaEventListener) getActivity());
            }
        }
    };

    View.OnClickListener buttonIncursionOnClickListener = new View.OnClickListener()  {
        @Override
        public void onClick(View v) {
            int soldadosSeleccionados = seekBarSoldados.getProgress();

            if (soldadosSeleccionados < 1) {
                Toast.makeText(getActivity(), getString(R.string.msj_selecciona_minimo, 1), Toast.LENGTH_SHORT).show();
            } else if (soldadosSeleccionados > Aldea.getInstance().getPoblacion()) {
                Toast.makeText(getActivity(), getString(R.string.msj_aldeanos_insuficientes), Toast.LENGTH_SHORT).show();
            } else {
                GestorRealTimeDatabase gestorRealTimeDatabase = new GestorRealTimeDatabase();
                gestorRealTimeDatabase.getUsuarioActual(context);
            }
        }
    };

    View.OnClickListener buttonRankingOnClickListener = new View.OnClickListener()  {
        @Override
        public void onClick(View v) {
            UtilidadActivity.lanzarIntent(getActivity(), RankingActivity.class, null);
        }
    };

    // Manejar la seekbar
    private final SeekBar.OnSeekBarChangeListener seekBarCazaChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // Actualizar el textview con el progreso de la seekbar
            textViewCazadores.setText(getString(R.string.texto_num_cazadores, progress));
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private final SeekBar.OnSeekBarChangeListener seekBarAtaqueChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // Actualizar el textview con el progreso de la seekbar
            textViewSoldados.setText(getString(R.string.texto_num_soldados, progress));
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


    private void addEventListeners() {
        if (Aldea.getInstance().getCabaniaCaza().getTimerPartidaCaza() != null)
            Aldea.getInstance().getCabaniaCaza().getTimerPartidaCaza().getLanzadorEventos().addEventListener(this);
        Aldea.getInstance().getCastillo().getLanzadorEventos().addEventListener(this);
    }

    private void removeEventListeners() {
        if (Aldea.getInstance().getCabaniaCaza().getTimerPartidaCaza() != null)
            Aldea.getInstance().getCabaniaCaza().getTimerPartidaCaza().getLanzadorEventos().removeEventListener(this);
        Aldea.getInstance().getCastillo().getLanzadorEventos().removeEventListener(this);
    }

    // Manejar eventos de la partida de caza
    @Override
    public void onTimerTick() {
        textViewPartidaCaza.setText(
                getString(R.string.texto_partida_caza,
                        Aldea.getInstance().getCabaniaCaza().getAldeanosAsignados(),
                        Aldea.getInstance().getCabaniaCaza().getTimerPartidaCaza().getSegundosRestantes())
        );
    }

    @Override
    public void onFinalizarPartida() {
        buttonCaza.setEnabled(true);
        textViewPartidaCaza.setText(getString(R.string.texto_partida_caza_inactiva));
    }
    
    // Manejar eventos del ataque
    @Override
    public void onAtaqueTerminado(boolean victoria) {
        Toast.makeText(getActivity(),
                victoria ? getString(R.string.msj_victoria_ataque) : getString(R.string.msj_derrota_ataque),
                Toast.LENGTH_LONG
        ).show();
    }

    @Override
    public void onError(Exception e) {
        Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }


    // --- IMPLEMENTACION DE ObtenerUsuarioCallback
    @Override
    public void onExito(UsuarioDTO usuarioDTO) {
        final long TIEMPO_ENTRE_ACCIONES = 10 * 60 * 1000; // 10 minutos
        long ahora = System.currentTimeMillis();
        if (ahora - usuarioDTO.getUltimoAtaque()  >= TIEMPO_ENTRE_ACCIONES) {
            Aldea.getInstance().getCastillo().iniciarIncursion(seekBarSoldados.getProgress());
        } else Toast.makeText(getActivity(), "Puedes atacar cada 10 minutos", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(DatabaseError databaseError) {
        Toast.makeText(getActivity(), databaseError.toException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}