package com.tfg.controladores;

import com.tfg.modelos.Aldea;
import com.tfg.modelos.enums.RecursosEnum;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ControladorRecursosTest {

    private Map<RecursosEnum, Integer> recursos;

    @Before
    public void setUp() {
        recursos = new HashMap<>();
    }

    @Test
    public void testGetCantidadRecurso() {
        recursos.put(RecursosEnum.TRONCOS_MADERA, 100);
        Assert.assertEquals(100,
                ControladorRecursos.getCantidadRecurso(recursos, RecursosEnum.TRONCOS_MADERA)
        );
        Assert.assertEquals(0,
                ControladorRecursos.getCantidadRecurso(recursos, RecursosEnum.PIEDRA)
        );
    }

    @Test
    public void testAgregarRecurso() {
        ControladorRecursos.agregarRecurso(recursos, RecursosEnum.TRONCOS_MADERA, 50);
        Assert.assertEquals(50,
                ControladorRecursos.getCantidadRecurso(recursos, RecursosEnum.TRONCOS_MADERA));
        ControladorRecursos.agregarRecurso(recursos, RecursosEnum.TRONCOS_MADERA, 25);
        Assert.assertEquals(75,
                ControladorRecursos.getCantidadRecurso(recursos, RecursosEnum.TRONCOS_MADERA));
    }

    @Test
    public void testAgregarRecursoSinExcederMax() {
        // Se pasa el hashmap de la aldea, ya que se comrpueba
        // siempre que el maximo que no se excede sea el de la aldea
        // y no el del hashmap local del edificio
        Aldea aldea = Aldea.getInstance();
        aldea.setRecursos(new HashMap<>());
        aldea.getRecursos().put(RecursosEnum.TRONCOS_MADERA, 0);

        RecursosEnum.TRONCOS_MADERA.setMax(30);
        aldea.getRecursos().put(RecursosEnum.TRONCOS_MADERA, 10);
        ControladorRecursos.agregarRecursoSinExcederMax(aldea.getRecursos(), RecursosEnum.TRONCOS_MADERA, 10);
        Assert.assertEquals(20, ControladorRecursos.getCantidadRecurso(aldea.getRecursos(), RecursosEnum.TRONCOS_MADERA));
        ControladorRecursos.agregarRecursoSinExcederMax(aldea.getRecursos(), RecursosEnum.TRONCOS_MADERA, 20);
        Assert.assertEquals(30, ControladorRecursos.getCantidadRecurso(aldea.getRecursos(), RecursosEnum.TRONCOS_MADERA));
    }

    @Test
    public void testPuedeConsumirRecurso() {
        recursos.put(RecursosEnum.PIEDRA, 50);
        Assert.assertTrue(ControladorRecursos.puedeConsumirRecurso(recursos, RecursosEnum.PIEDRA, 25));
        Assert.assertFalse(ControladorRecursos.puedeConsumirRecurso(recursos, RecursosEnum.PIEDRA, 60));
    }

    @Test
    public void testConsumirRecurso() {
        recursos.put(RecursosEnum.PIEDRA, 50);
        Assert.assertTrue(ControladorRecursos.consumirRecurso(recursos, RecursosEnum.PIEDRA, 25));
        Assert.assertEquals(25, ControladorRecursos.getCantidadRecurso(recursos, RecursosEnum.PIEDRA));
        Assert.assertFalse(ControladorRecursos.consumirRecurso(recursos, RecursosEnum.PIEDRA, 30));
        Assert.assertEquals(25, ControladorRecursos.getCantidadRecurso(recursos, RecursosEnum.PIEDRA));
    }
}
