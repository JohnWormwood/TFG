package com.tfg.activities;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;
import com.tfg.R;
import com.tfg.activities.fragments.AldeaFragment;
import com.tfg.activities.fragments.MercaderFragment;
import com.tfg.activities.fragments.PartidasFragment;
import com.tfg.activities.fragments.SenadoFragment;
import com.tfg.controladores.ControladorAldea;
import com.tfg.databinding.ActivityJuegoBinding;
import com.tfg.utilidades.Utilidades;

public class JuegoActivity extends AppCompatActivity {
    ActivityJuegoBinding binding;

    public static boolean enEjecucion = true;

    // Componentes de la actividad
    private TextView textViewAldeanos;
    private TextView textViewComida;

    // Componentes de PartidasFragment
    private SeekBar seekBarCazadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Cargar los fragments
        binding = ActivityJuegoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurar listeners
        binding.menuInferior.setOnItemSelectedListener(itemSelectedListener);

        // Cargar el fragment segun el item del menu
        itemSelectedListener.onNavigationItemSelected(binding.menuInferior.getMenu().findItem(binding.menuInferior.getSelectedItemId()));
        binding.menuInferior.setItemIconTintList(null); // Esto es para que los iconos se vean bien

        // Inicializar los componentes de la interfaz
        textViewAldeanos = findViewById(R.id.textViewAldeanos);
        textViewComida = findViewById(R.id.textViewComida);

        // Inicializar componentes del PartidasFragment
        seekBarCazadores = findViewById(R.id.seekBarCazadores);
        seekBarCazadores.setOnSeekBarChangeListener();

        // Iniciar el juego
        ejecutarHiloPrincipal();

    }

    private void ejecutarHiloPrincipal() {
        // Iniciar un hilo secundario para ejecutar el código continuamente
        new Thread(new Runnable() {
            @Override
            public void run() {
                actualizarUI();
                while (enEjecucion) {
                    // Logica del juego
                    ControladorAldea.generarAldeano();
                    Utilidades.esperar(1);


                    // Actualizar la interfaz al final de cada ciclo
                    actualizarUI();
                }
            }
        }).start();
    }



    private void actualizarUI() {
        // Actualizar la interfaz de usuario desde el hilo principal
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewAldeanos.setText(String.valueOf(ControladorAldea.getPoblacion()));
                textViewComida.setText(String.valueOf(ControladorAldea.getComida()));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detener el bucle infinito cuando la actividad se destruye para evitar problemas de memoria
        enEjecucion = false;
    }

    public void cambiarFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutJuego, fragment);
        fragmentTransaction.commit();
    }

    // --- LISTENERS ---
    private final NavigationBarView.OnItemSelectedListener itemSelectedListener = menuItem -> {
        if (menuItem.getItemId() == R.id.aldea) {
            cambiarFragment(new AldeaFragment());
        } else if (menuItem.getItemId() == R.id.mercader) {
            cambiarFragment(new MercaderFragment());
        } else if (menuItem.getItemId() == R.id.senado) {
            cambiarFragment(new SenadoFragment());
        } else if (menuItem.getItemId() == R.id.partidas) {
            cambiarFragment(new PartidasFragment());
        }

        return true;
    };

    private final SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // Este método se llama cada vez que se cambia el progreso de la SeekBar
            // Aquí puedes realizar acciones basadas en el progreso cambiado

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // Este método se llama cuando se inicia el seguimiento del dedo en la SeekBar
            // Aquí puedes realizar acciones al inicio del seguimiento
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // Este método se llama cuando se detiene el seguimiento del dedo en la SeekBar
            // Aquí puedes realizar acciones al detener el seguimiento
        }
    };
}