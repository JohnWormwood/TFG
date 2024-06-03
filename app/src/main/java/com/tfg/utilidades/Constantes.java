package com.tfg.utilidades;

public final class Constantes {
    public static final int NIVEL_INICIAL = 1;

    public static class Estructura {
        public static final int NIVEL_MAXIMO = 10;
        public final static int AUMENTO_MAX_RECURSO_POR_NIVEL = 10;
        public final static int MAX_RECURSOS = 99;
    }

    public static class Aldea {
        public static final int POBLACION_INICIAL = 10;
        public static final int COMIDA_INICIAL = 10;
        public static final int AUMENTO_MAX_ALDEANOS_POR_NIVEL = 10;
        public static final int NIVEL_DESBLOQUEO_PIEDRA = 2;
        public static final int NIVEL_DESBLOQUEO_TABLONES = 3;
        public static final int NIVEL_DESBLOQUEO_CASTILLO = 4;
        public static final int NIVEL_DESBLOQUEO_HIERRO = 5;
        public static final int NIVEL_DESBLOQUEO_GRANJA = 6;
        public static final int NIVEL_DESBLOQUEO_ORO = 7;
        public static final int NIVEL_DESBLOQUEO_MERCADER = 8;
    }

    public static class Edificio {
        public static final int AUMENTO_MAX_ALDEANOS_POR_NIVEL = 3;
    }

    public static class CabaniaCaza {
        public static final int PROBABILIDAD_MUERTE = 10;
    }

    public static class Mercader {
        public static final int CANTIDAD = 5;
        public static final int PRECIO_TRONCOS = 5;
        public static final int PRECIO_COMIDA = 5;
        public static final int PRECIO_TABLONES = 7;
        public static final int PRECIO_PIEDRA = 8;
        public static final int PRECIO_HIERRO = 10;
    }

    public static class Castillo {
        public static final int CANTIDAD_RECURSOS_ROBADOS = 10;
        public static final int PUNTOS_VICTORIA = 10;
        public static final int PUNTOS_DERROTA = -5;
    }

    public static class BaseDatos {
        public static final String COLECCION_USUARIOS = "usuarios";
        public static final String ALDEA = "datos_aldea";
        public static final String RECURSOS = "recursos";
        public static final String CABANIA_CAZA = "cabania_caza";
        public static final String CASETA_LENIADOR = "caseta_leniador";
        public static final String CARPINTERIA = "carpinteria";
        public static final String GRANJA = "granja";
        public static final String MINA = "mina";
        public static final String CASTILLO = "castillo";
    }
}
