package com.tfg.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationBarView;
import com.tfg.R;
import com.tfg.activities.fragments.AldeaFragment;
import com.tfg.activities.fragments.MercaderFragment;
import com.tfg.activities.fragments.PartidasFragment;
import com.tfg.activities.fragments.SenadoFragment;
import com.tfg.controladores.ControladorAldea;
import com.tfg.databinding.ActivityJuegoBinding;
import com.tfg.json.GestorJSON;
import com.tfg.json.MejorasEdificiosJSON;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;

import java.io.IOException;

public class JuegoActivity extends AppCompatActivity {
    ActivityJuegoBinding binding;

    public static boolean enEjecucion = true;

    // Componentes de la interfaz
    private TextView textViewAldeanos;
    private TextView textViewComida;
    private TextView textViewTroncos;

    private Aldea aldea = Aldea.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Cargar los fragments
        binding = ActivityJuegoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Cargar el fragment segun el item del menu
        itemSelectedListener.onNavigationItemSelected(binding.menuInferior.getMenu().findItem(binding.menuInferior.getSelectedItemId()));
        binding.menuInferior.setItemIconTintList(null); // Esto es para que los iconos se vean bien

        configInicial();

        // Iniciar el juego
        enEjecucion = true;
        ejecutarHiloJuego();
    }

    private void ejecutarHiloJuego() {
        // Iniciar un hilo secundario para ejecutar el código continuamente
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ControladorAldea.iniciarAldea();
                    while (enEjecucion) {
                        Thread.sleep(1);
                        // Actualizar la interfaz al final de cada ciclo
                        actualizarUI();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void actualizarUI() {
        // Actualizar la interfaz de usuario desde el hilo principal
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewAldeanos.setText(String.valueOf(Aldea.getInstance().getPoblacion()));
                textViewComida.setText(String.valueOf(Aldea.getInstance().getRecursos().get(RecursosEnum.COMIDA)));
                textViewTroncos.setText(String.valueOf(Aldea.getInstance().getRecursos().get(RecursosEnum.TRONCOS_MADERA)));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ControladorAldea.finalizarAldea();
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

    private void configInicial() {
        inicializarComponentes();
        cargarListeners();
        cargarDatos();
        cargarGifs();
    }

    private void inicializarComponentes() {
        // Inicializar los componentes de la interfaz
        textViewAldeanos = findViewById(R.id.textViewAldeanos);
        textViewComida = findViewById(R.id.textViewComida);
        textViewTroncos = findViewById(R.id.textViewTroncos);
    }

    private void cargarListeners() {
        // Configurar listeners
        binding.menuInferior.setOnItemSelectedListener(itemSelectedListener);
    }

    private void cargarDatos() {
        GestorJSON gestorJSON = new GestorJSON();
        try {
            String json = gestorJSON.cargarJsonDesdeAssets(getString(R.string.fichero_mejoras_json), this);
            MejorasEdificiosJSON mejorasEdificiosJSON = new MejorasEdificiosJSON(json);
            aldea.setPreciosMejoras(mejorasEdificiosJSON.getDatosMejoras(getString(R.string.senado_nodo_json)));
            aldea.getCabaniaCaza().setPreciosMejoras(mejorasEdificiosJSON.getDatosMejoras(getString(R.string.cabania_caza_nodo_json)));
            aldea.getCasetaLeniador().setPreciosMejoras(mejorasEdificiosJSON.getDatosMejoras(getString(R.string.caseta_leniador_nodo_json)));
            aldea.getCarpinteria().setPreciosMejoras(mejorasEdificiosJSON.getDatosMejoras(getString(R.string.carpinteria_nodo_json)));
            aldea.getGranja().setPreciosMejoras(mejorasEdificiosJSON.getDatosMejoras(getString(R.string.granja_nodo_json)));
            aldea.getMina().setPreciosMejoras(mejorasEdificiosJSON.getDatosMejoras(getString(R.string.mina_nodo_json)));


            System.out.println("ALDEA: "+aldea.getPreciosMejoras());
            System.out.println("CAZA: "+aldea.getCabaniaCaza().getPreciosMejoras());
            System.out.println("LEÑADOR: "+aldea.getCasetaLeniador().getPreciosMejoras());
            System.out.println("CARPINTERO: "+aldea.getCarpinteria().getPreciosMejoras());
            System.out.println("GRANJA: "+aldea.getGranja().getPreciosMejoras());
            System.out.println("MINA: "+aldea.getMina().getPreciosMejoras());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al cargar datos del juego", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void cargarGifs() {
        cargarGif(R.id.taladorView);
    }

    private void cargarGif(int id) {
        ImageView imageView = findViewById(id);
        Glide.with(this)
                .asGif()
                .fitCenter() // Ajustar la escala para que la imagen se adapte al ImageView
                .override(imageView.getWidth()*10, imageView.getHeight()*10)
                .load(R.drawable.talador).into(imageView);
    }
}