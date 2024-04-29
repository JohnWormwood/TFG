package com.tfg.firebase.bbdd;

import com.google.firebase.firestore.FirebaseFirestore;
import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;

public class GestorBaseDatos {

    public void guardarDatos(String email) {
        FirebaseFirestore.getInstance()
                .collection("usuarios")
                .document(email)
                .set(mapearDatosAldea());
    }

    private DatosAldea mapearDatosAldea() {
        Aldea aldea = Aldea.getInstance();
        DatosAldea datosAldea = new DatosAldea();

        return datosAldea;
    }
}
