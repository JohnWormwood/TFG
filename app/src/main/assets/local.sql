CREATE TABLE datos_aldea(
    nivel INTEGER NOT NULL,
    poblacion INTEGER NOT NULL,
    defensas INTEGER NOT NULL
);

CREATE TABLE recursos(
    troncos INTEGER NOT NULL,
    tablones INTEGER NOT NULL,
    comida INTEGER NOT NULL,
    piedra INTEGER NOT NULL,
    hierro INTEGER NOT NULL,
    oro INTEGER NOT NULL
);

CREATE TABLE cabania_caza(
    nivel INTEGER NOT NULL,
    aldeanos_asignados INTEGER NOT NULL,
    desbloqueado INTEGER NOT NULL CHECK(desbloqueado IN (0, 1)),
    aldeanos_muertos_en_partida INTEGER NOT NULL,
    partida_activa INTEGER NOT NULL CHECK(partida_activa IN (0, 1)),
    segundos_restantes INTEGER NOT NULL
);

CREATE TABLE edificio(
    nombre TEXT PRIMARY KEY,
    nivel INTEGER NOT NULL,
    aldeanos_asignados INTEGER NOT NULL,
    desbloqueado INTEGER NOT NULL CHECK(desbloqueado IN (0, 1))
);