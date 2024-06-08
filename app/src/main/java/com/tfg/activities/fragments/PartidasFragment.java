package com.tfg.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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

import java.util.concurrent.TimeUnit;

public class PartidasFragment extends Fragment
        implements PartidaCazaEventListener, AtaqueEventListener, ObtenerUsuarioCallback {

    // Componentes de la interfaz
    private SeekBar seekBarCazadores, seekBarSoldados;
    private TextView textViewCazadores, textViewPartidaCaza, textViewSoldados,
            textViewCaza, textViewIncursion, textViewRanking;
    private ImageButton buttonCaza, buttonIncursion, buttonRanking;
    private LinearLayout linearLayout;
    private TextView textViewMsj;

    // Referencias
    private final Aldea aldea = Aldea.getInstance();
    private final PartidasFragment context = this;

    public PartidasFragment() {
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
        View view = inflater.inflate(R.layout.fragment_partidas, container, false);

        configInicial(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Establecer el minimo y el maximo de las seekbar
        int maxCazadores = aldea.getCabaniaCaza().getAldeanosMaximos();
        UtilidadActivity.setLimitesSeekbar(seekBarCazadores, 0, maxCazadores);

        int maxSoldados = aldea.getCastillo().getAldeanosMaximos();
        UtilidadActivity.setLimitesSeekbar(seekBarSoldados, 0, maxSoldados);

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

    // --- FUNCIONES INTERFAZ GRAFICA ---

    // Deshabilitar el boton de caza si hay una partida activa
    private void comprobarEstadoBoton() {
        buttonCaza.setEnabled(!aldea.getCabaniaCaza().isPartidaActiva());
    }

    private void configInicial(View view) {
        inicializarComponentes(view);
        cargarListeners();
        setTextoInicial();
    }

    private void inicializarComponentes(View view) {
        // Inicializar componentes de la interfaz
        linearLayout = view.findViewById(R.id.linearLayoutIncursion);
        textViewMsj = view.findViewById(R.id.textViewMsj);

        // Caza
        seekBarCazadores = view.findViewById(R.id.seekBarCazadores);
        textViewCazadores = view.findViewById(R.id.textViewCazadores);
        textViewPartidaCaza = view.findViewById(R.id.textViewPartidaCaza);
        buttonCaza = view.findViewById(R.id.buttonCaza);
        textViewCaza = view.findViewById(R.id.textViewCaza);
        UtilidadActivity.setEfectoBoton(buttonCaza, textViewCaza);

        // Ataques
        seekBarSoldados = view.findViewById(R.id.seekBarSoldados);
        textViewSoldados = view.findViewById(R.id.textViewSoldados);
        buttonIncursion = view.findViewById(R.id.buttonIncursion);
        buttonRanking = view.findViewById(R.id.buttonRanking);
        textViewIncursion = view.findViewById(R.id.textViewButtonIncursion);
        textViewRanking = view.findViewById(R.id.textViewButtonRanking);
        UtilidadActivity.setEfectoBoton(buttonIncursion, textViewIncursion);
        UtilidadActivity.setEfectoBoton(buttonRanking, textViewRanking);
    }

    private void cargarListeners() {
        // Listeners
        seekBarCazadores.setOnSeekBarChangeListener(seekBarCazaChangeListener);
        seekBarSoldados.setOnSeekBarChangeListener(seekBarAtaqueChangeListener);
        buttonCaza.setOnClickListener(buttonCazaOnClickListener);
        buttonIncursion.setOnClickListener(buttonIncursionOnClickListener);
        buttonRanking.setOnClickListener(buttonRankingOnClickListener);
    }

    private void setTextoInicial() {
        // Mostrar el valor de las seekbars en los textviews
        textViewCazadores.setText(
                getString(R.string.texto_num_cazadores, seekBarCazadores.getProgress()));
        textViewSoldados.setText(
                getString(R.string.texto_num_soldados, seekBarSoldados.getProgress()));
    }

    private void actualizarVisibilidadLayouts() {
        linearLayout.setVisibility(aldea.getNivel() >= Constantes.Aldea.NIVEL_DESBLOQUEO_CASTILLO
                ? View.VISIBLE : View.GONE);
        textViewMsj.setVisibility(aldea.getNivel() >= Constantes.Aldea.NIVEL_DESBLOQUEO_CASTILLO
                ? View.GONE : View.VISIBLE);
    }

    // --- LISTENERS ---
    View.OnClickListener buttonCazaOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int cazadoresSeleccionados = seekBarCazadores.getProgress();

            if (cazadoresSeleccionados < 1) {
                Toast.makeText(getActivity(),
                        getString(R.string.msj_selecciona_minimo, 1),
                        Toast.LENGTH_SHORT).show();
            } else if (cazadoresSeleccionados > Aldea.getInstance().getPoblacion()) {
                Toast.makeText(getActivity(),
                        getString(R.string.msj_aldeanos_insuficientes), Toast.LENGTH_SHORT).show();
            } else if (!(ControladorRecursos.getCantidadRecurso(Aldea.getInstance().getRecursos(),
                    RecursosEnum.COMIDA) < RecursosEnum.COMIDA.getMax())) {
                Toast.makeText(getActivity(),
                        getString(R.string.msj_caza_sin_sentido), Toast.LENGTH_LONG).show();
            } else {
                int tiempoTotal = 10000;

                Toast.makeText(getActivity(),
                        getString(R.string.msj_partida_caza_iniciada, cazadoresSeleccionados),
                        Toast.LENGTH_LONG).show();
                buttonCaza.setEnabled(false);

                aldea.getCabaniaCaza()
                        .iniciarPartidaCaza(cazadoresSeleccionados, tiempoTotal);
                addEventListeners();
                aldea.getCabaniaCaza().getTimerPartidaCaza().getLanzadorEventos()
                        .addEventListener((PartidaCazaEventListener) getActivity());
            }
        }
    };

    private final View.OnClickListener buttonIncursionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int soldadosSeleccionados = seekBarSoldados.getProgress();

            if (soldadosSeleccionados < 1) {
                Toast.makeText(getActivity(),
                        getString(R.string.msj_selecciona_minimo, 1),
                        Toast.LENGTH_SHORT).show();
            } else if (soldadosSeleccionados > Aldea.getInstance().getPoblacion()) {
                Toast.makeText(getActivity(),
                        getString(R.string.msj_aldeanos_insuficientes), Toast.LENGTH_SHORT).show();
            } else {
                GestorRealTimeDatabase gestorRealTimeDatabase = new GestorRealTimeDatabase();
                gestorRealTimeDatabase.getUsuarioActual(context);
            }
        }
    };

    private final View.OnClickListener buttonRankingOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UtilidadActivity.lanzarIntent(getActivity(), RankingActivity.class, null);
        }
    };

    private final SeekBar.OnSeekBarChangeListener seekBarCazaChangeListener =
            new SeekBar.OnSeekBarChangeListener() {
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

    private final SeekBar.OnSeekBarChangeListener seekBarAtaqueChangeListener =
            new SeekBar.OnSeekBarChangeListener() {
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

    // Listeners de eventos externos
    private void addEventListeners() {
        if (aldea.getCabaniaCaza().getTimerPartidaCaza() != null)
            aldea.getCabaniaCaza().getTimerPartidaCaza().getLanzadorEventos()
                    .addEventListener(this);
        aldea.getCastillo().getLanzadorEventos().addEventListener(this);
    }

    private void removeEventListeners() {
        if (aldea.getCabaniaCaza().getTimerPartidaCaza() != null)
            aldea.getCabaniaCaza().getTimerPartidaCaza().getLanzadorEventos()
                    .removeEventListener(this);
        aldea.getCastillo().getLanzadorEventos().removeEventListener(this);
    }

    // --- IMPLEMENTACION DE PartidaCazaEventListener
    @Override
    public void onTimerTick() {
        textViewPartidaCaza.setText(
                getString(R.string.texto_partida_caza,
                        aldea.getCabaniaCaza().getAldeanosAsignados(),
                        aldea.getCabaniaCaza().getTimerPartidaCaza().getSegundosRestantes())
        );
    }

    @Override
    public void onFinalizarPartida() {
        buttonCaza.setEnabled(true);
        textViewPartidaCaza.setText(getString(R.string.texto_partida_caza_inactiva));
    }

    // --- IMPLEMENTACION DE AtaqueEventListener
    @Override
    public void onAtaqueTerminado(boolean victoria) {
        Toast.makeText(getActivity(),
                victoria ? getString(R.string.msj_victoria_ataque)
                        : getString(R.string.msj_derrota_ataque),
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
        long ahora = System.currentTimeMillis();
        if (ahora - usuarioDTO.getUltimoAtaque() >= Constantes.Castillo.TIEMPO_ENTRE_ATAQUES) {
            aldea.getCastillo().iniciarIncursion(seekBarSoldados.getProgress());
        } else
            Toast.makeText(getActivity(),
                    getString(R.string.msj_tiempo_ataque,
                            TimeUnit.MILLISECONDS.toMinutes(Constantes.Castillo.TIEMPO_ENTRE_ATAQUES)),
                    Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(DatabaseError databaseError) {
        Toast.makeText(getActivity(),
                databaseError.toException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}