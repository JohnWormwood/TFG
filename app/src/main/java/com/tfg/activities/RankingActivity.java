package com.tfg.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.tfg.R;
import com.tfg.bbdd.dto.UsuarioDTO;
import com.tfg.bbdd.firebase.GestorRealTimeDatabase;
import com.tfg.eventos.callbacks.ObtenerRankingCallback;
import com.tfg.utilidades.UtilidadActivity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class RankingActivity extends AppCompatActivity implements ObtenerRankingCallback {

    private TextView textViewUsuario, textViewUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ranking);

        // Configura la pantalla completa y el modo inmersivo
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        textViewUsuario = findViewById(R.id.textViewUsuario);
        textViewUsuarios = findViewById(R.id.textViewUsuarios);

        //boton Salir
        ImageButton imageButtonSalir = findViewById(R.id.imageButtonSalir);
        imageButtonSalir.setOnClickListener(view -> onBackPressed());
        UtilidadActivity.setEfectoBoton(imageButtonSalir);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        GestorRealTimeDatabase gestorRealTimeDatabase = new GestorRealTimeDatabase();
        gestorRealTimeDatabase.getRanking(this);

    }

    @Override
    public void onExito(List<UsuarioDTO> ranking) {
        Optional<UsuarioDTO> usuarioActual = ranking.stream()
                .filter(usuario -> usuario.getEmail().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()))
                .findFirst();

        if (usuarioActual.isPresent()) {
            textViewUsuario.setText(String.valueOf("Tu puntuacion: "+usuarioActual.get().getPuntos()));
        } else {
            textViewUsuario.setText("No hemos podido obtener tu puntuacion");
        }

        // Ordenar la lista por puntos en orden descendente
        ranking.sort((u1, u2) -> Integer.compare(u2.getPuntos(), u1.getPuntos()));

        for (int i = 0; i < Math.min(ranking.size(), 10); i++) {
            String email = ranking.get(i).getEmail().split("@")[0];
            String puntos = String.valueOf(ranking.get(i).getPuntos());

            // Usar String.format para formatear el texto con longitud fija
            String formattedText = String.format("%-20s %s", email, puntos);


            // Agregar el texto al TextView
            textViewUsuarios.append(formattedText + "\n");
        }
    }

    @Override
    public void onError(DatabaseError databaseError) {
        Toast.makeText(this, "Error al cargar el ranking", Toast.LENGTH_SHORT).show();
    }
}