package com.tfg.activities;

import android.content.Context;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
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
import com.tfg.eventos.callbacks.OperacionesDatosCallback;
import com.tfg.eventos.listeners.ActualizarInterfazEventListener;
import com.tfg.eventos.listeners.PartidaCazaEventListener;
import com.tfg.firebase.bbdd.GestorBaseDatos;
import com.tfg.json.GestorJSON;
import com.tfg.json.MejorasEdificiosJSON;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;
import com.tfg.utilidades.UtilidadRed;

import java.io.IOException;

public class JuegoActivity extends AppCompatActivity implements OperacionesDatosCallback, PartidaCazaEventListener, ActualizarInterfazEventListener {
    ActivityJuegoBinding binding;

    private String emailUsuario;
    private GestorBaseDatos gestorBaseDatos = new GestorBaseDatos();

    public static boolean enEjecucion = false;

    // Componentes de la interfaz
    private TextView textViewAldeanos, textViewComida, textViewTroncos, textViewTablones, textViewPiedra, textViewHierro, textViewOro;

    private ImageView imageViewMina, imagewViewGranja, imageViewCastillo, imageViewCarpinteria;
    private ImageView imageViewOveja, imageViewGuerrero1, imageViewGuerrero2;
    private ImageView[] imageViewsCasas;

    private Aldea aldea = Aldea.getInstance();

    // --- FUNCIONES PARA CONTROLAR LA ACTIVITY ---
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        emailUsuario = bundle.getString("email");
        System.out.println(emailUsuario);
        if (!UtilidadRed.hayInternet(this)) {
            Toast.makeText(this, getString(R.string.msj_internet_necesario), Toast.LENGTH_LONG).show();
            finish();
        }
        //gestorBaseDatos.manejarDesconexion();
        gestorBaseDatos.cargarDatos(emailUsuario, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (UtilidadRed.hayInternet(this)) {
            gestorBaseDatos.guardarDatos(emailUsuario, this);
        }
        aldea.getCabaniaCaza().getTimerPartidaCaza().removeEventListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gestorBaseDatos.guardarDatos(emailUsuario, this);
        aldea.getCabaniaCaza().getTimerPartidaCaza().removeEventListener(this);
    }

    public void cambiarFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutJuego, fragment);
        fragmentTransaction.commit();
    }

    // --- IMPLEMENTACION DE OperacionesDatosCallback ---
    @Override
    public void onDatosCargados() {
        /*
         * Es necesario hacerlo de esta forma, ya que firebase obtiene los datos de manera
         * asincrona, asi que si no se espera a que termine podria iniciarse el juego
         * sin haber cargado bien todos los datos
         */
        // Iniciar el juego
        enEjecucion = true;
        actualizarVisibilidadImageViews();
        ejecutarHiloJuego();
    }

    @Override
    public void onDatosGuardados() {
        ControladorAldea.finalizarAldea();
    }

    // --- IMPLEMANTACION DE PartidaCazaEventListener ---
    @Override
    public void onTimerTick() {
        // No hacer nada
    }

    @Override
    public void onFinalizarPartida() {
        if (Aldea.getInstance().getCabaniaCaza().getAldeanosMuertosEnPartida() > 0) {
            Toast.makeText(this, getString(R.string.msj_aldeanos_muertos_caza, aldea.getCabaniaCaza().getAldeanosMuertosEnPartida()), Toast.LENGTH_LONG).show();
        }
    }

    // IMPLEMENTACION DE ActualizarInterfazEventListener
    @Override
    public void onActualizarInterfaz() {
        actualizarVisibilidadImageViews();
    }

    // --- HILO PRINCIPAL DEL JUEGO ---
    private void ejecutarHiloJuego() {
        Context context = this;
        // Iniciar un hilo secundario para ejecutar el código continuamente
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    aldea.getCabaniaCaza().getTimerPartidaCaza().addEventListener((PartidaCazaEventListener) context);
                    ControladorAldea.iniciarAldea();
                    while (enEjecucion) {
                        Thread.sleep(1);
                        if (!UtilidadRed.hayInternet(context)) {
                            finish();
                        }
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
                textViewTablones.setText(String.valueOf(Aldea.getInstance().getRecursos().get(RecursosEnum.TABLONES_MADERA)));
                textViewPiedra.setText(String.valueOf(Aldea.getInstance().getRecursos().get(RecursosEnum.PIEDRA)));
                textViewHierro.setText(String.valueOf(Aldea.getInstance().getRecursos().get(RecursosEnum.HIERRO)));
                textViewOro.setText(String.valueOf(Aldea.getInstance().getRecursos().get(RecursosEnum.ORO)));
            }
        });
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

    // --- FUNCIONES PARA INICIALIZAR CORRECTAMENTE LA UI ---
    private void configInicial() {
        inicializarComponentes();
        cargarListeners();
        cargarDatos();
        cargarGifs();
    }

    private void inicializarComponentes() {
        // Inicializar los componentes de la interfaz
        // TextViews
        textViewAldeanos = findViewById(R.id.textViewAldeanos);
        textViewComida = findViewById(R.id.textViewComida);
        textViewTroncos = findViewById(R.id.textViewTroncos);
        textViewTablones = findViewById(R.id.textViewTablones);
        textViewPiedra = findViewById(R.id.textViewPiedra);
        textViewHierro = findViewById(R.id.textViewHierro);
        textViewOro = findViewById(R.id.textViewOro);

        // ImageViews
        imagewViewGranja = findViewById(R.id.imageViewGranja);
        imageViewMina = findViewById(R.id.imageViewMina);
        imageViewCastillo = findViewById(R.id.imageViewCastillo);
        imageViewCarpinteria = findViewById(R.id.imageViewCarpinteria);
        imageViewOveja = findViewById(R.id.imageViewOveja);

        imageViewsCasas = new ImageView[10];
        for (int i = 0; i < imageViewsCasas.length; i++) {
            int id = getResources().getIdentifier("imageViewCasa"+(i+1), "id", getPackageName());
            imageViewsCasas[i] = findViewById(id);
        }
    }

    private void cargarListeners() {
        // Configurar listeners
        binding.menuInferior.setOnItemSelectedListener(itemSelectedListener);
    }

    private void actualizarVisibilidadImageViews() {
        for (int i = 0; i < aldea.getNivel(); i++) {
            imageViewsCasas[i].setImageResource(R.drawable.casa1);
        }
        if (aldea.getMina().isDesbloqueado()) imageViewMina.setImageResource(R.drawable.mina2);
        if (aldea.getGranja().isDesbloqueado()) imagewViewGranja.setImageResource(R.drawable.casa2);
        if (aldea.getCarpinteria().isDesbloqueado()) imageViewCarpinteria.setImageResource(R.drawable.carpinteria);
        if (aldea.getCastillo().isDesbloqueado()) imageViewCastillo.setImageResource(R.drawable.castillo);

        imageViewOveja.setVisibility(aldea.getGranja().isDesbloqueado() ? View.VISIBLE : View.INVISIBLE);
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
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.msj_error_cargar_datos), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void cargarGifs() {
        cargarGif(R.id.taladorView, R.drawable.talador);
        cargarGif(R.id.imageViewOveja, R.drawable.oveja);
        cargarGif(R.id.cazadorView, R.drawable.cazador);
        cargarGif(R.id.imageViewGuerrero1, R.drawable.guerrero1);
        cargarGif(R.id.imageViewGuerrero2, R.drawable.guerrero2);
    }

    private void cargarGif(int id, int d) {
        ImageView imageView = findViewById(id);
        Glide.with(this)
                .asGif()
                .fitCenter() // Ajustar la escala para que la imagen se adapte al ImageView
                .override(imageView.getWidth() * 10, imageView.getHeight() * 10)
                .load(d).into(imageView);
    }
}