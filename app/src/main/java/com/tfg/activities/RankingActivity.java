package com.tfg.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
        setContentView(R.layout.activity_ranking);

        textViewUsuario = findViewById(R.id.textViewUsuario);
        textViewUsuarios = findViewById(R.id.textViewUsuarios);

        // Boton Salir
        ImageButton imageButtonSalir = findViewById(R.id.imageButtonSalir);
        imageButtonSalir.setOnClickListener(view -> finish());
        UtilidadActivity.setEfectoBoton(imageButtonSalir);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GestorRealTimeDatabase gestorRealTimeDatabase = new GestorRealTimeDatabase();
        gestorRealTimeDatabase.getRanking(this);
    }

    @Override
    public void onExito(List<UsuarioDTO> ranking) {
        textViewUsuarios.setText("");
        Optional<UsuarioDTO> usuarioActual = ranking.stream()
                .filter(usuario -> usuario.getEmail().equals(Objects.requireNonNull(
                        FirebaseAuth.getInstance().getCurrentUser()).getEmail())).findFirst();
        if (usuarioActual.isPresent()) {
            textViewUsuario.setText(
                    getString(R.string.puntos_usuario, usuarioActual.get().getPuntos()));
        } else {
            textViewUsuario.setText(getString(R.string.msj_error_puntos));
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
        Toast.makeText(this, getString(R.string.msj_error_ranking), Toast.LENGTH_SHORT).show();
        Log.e(getClass().getSimpleName(), databaseError.getMessage(), databaseError.toException());
    }
}