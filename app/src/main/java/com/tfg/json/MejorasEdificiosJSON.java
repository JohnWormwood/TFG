package com.tfg.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfg.modelos.PrecioMejora;

import java.util.ArrayList;
import java.util.List;


public class MejorasEdificiosJSON {
    private final JsonNode NODO_MEJORAS;

    public MejorasEdificiosJSON(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode nodoRaiz = objectMapper.readTree(json);
        NODO_MEJORAS = nodoRaiz.path("mejoras_edificios");
    }

    public List<PrecioMejora> getDatosMejoras(String edificio) {
        List<PrecioMejora> listaPreciosMejoras = new ArrayList<>();
        JsonNode nodoEdificio = NODO_MEJORAS.path(edificio);
        for (int i = 1; i <= 9; i++) {
            String nivel = "nivel_" + i;
            JsonNode nodoNivel = nodoEdificio.path(nivel);
            int troncos = nodoNivel.path("troncos").asInt();
            int tablones = nodoNivel.path("tablones").asInt();
            int comida = nodoNivel.path("comida").asInt();
            int piedra = nodoNivel.path("piedra").asInt();
            int hierro = nodoNivel.path("hierro").asInt();
            int oro = nodoNivel.path("oro").asInt();

            listaPreciosMejoras.add(new PrecioMejora(troncos, tablones, comida, piedra, hierro, oro));
        }

        return listaPreciosMejoras;
    }
}
