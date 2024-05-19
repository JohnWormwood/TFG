CREATE TABLE usuarios(
    email TEXT PRIMARY KEY
);

CREATE TABLE datos_aldea(
    usuario TEXT PRIMARY KEY,
    nivel INTEGER NOT NULL,
    poblacion INTEGER NOT NULL,
    defensas INTEGER NOT NULL,
    FOREIGN KEY(usuario) REFERENCES usuarios(email)
);

CREATE TABLE recursos(
    usuario TEXT PRIMARY KEY,
    troncos INTEGER NOT NULL,
    tablones INTEGER NOT NULL,
    comida INTEGER NOT NULL,
    piedra INTEGER NOT NULL,
    hierro INTEGER NOT NULL,
    oro INTEGER NOT NULL,
    FOREIGN KEY(usuario) REFERENCES usuarios(email)
);

CREATE TABLE cabania_caza(
    usuario TEXT PRIMARY KEY,
    nivel INTEGER NOT NULL,
    aldeanos_asignados INTEGER NOT NULL,
    desbloqueado INTEGER NOT NULL CHECK(desbloqueado IN (0, 1)),
    aldeanos_muertos_en_partida INTEGER NOT NULL,
    partida_activa INTEGER NOT NULL CHECK(partida_activa IN (0, 1)),
    segundos_restantes INTEGER NOT NULL,
    FOREIGN KEY(usuario) REFERENCES usuarios(email)
);

CREATE TABLE edificio(
    usuario TEXT PRIMARY KEY,
    nombre TEXT UNIQUE,
    nivel INTEGER NOT NULL,
    aldeanos_asignados INTEGER NOT NULL,
    desbloqueado INTEGER NOT NULL CHECK(desbloqueado IN (0, 1)),
    FOREIGN KEY(usuario) REFERENCES usuarios(email)
);