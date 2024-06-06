package com.tfg.utilidades;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tfg.R;

public class PopupManager {

    private AppCompatActivity activity;

    public PopupManager(AppCompatActivity activity) throws IllegalArgumentException {
        if (activity == null) {
            throw new IllegalArgumentException("La Activity no puede ser null");
        }
        this.activity = activity;
    }

    public void showPopup(String message) {
        // Verify that the activity is not finishing
        if (activity.isFinishing()) {
            return;
        }

        // Inflate the popup layout
        LayoutInflater inflater = activity.getLayoutInflater();
        View popupView = inflater.inflate(R.layout.popup_texto, null);

        // Find the TextView in the popup layout
        TextView textViewPopup = popupView.findViewById(R.id.textViewPopup);

        // Set the provided message to the TextView
        textViewPopup.setText(message);

        // Create and show the AlertDialog with the custom style
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.CustomAlertDialog);
        builder.setView(popupView)
                .setPositiveButton("OK", (dialog, id) -> dialog.dismiss());

        activity.runOnUiThread(() -> {
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }
}
