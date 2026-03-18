package com.oct.caccia_al_tesorov2.Model.Luogo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LuogoEntityTest {

    @Test
    void testDefaultConstructor() {
        LuogoEntity luogo = new LuogoEntity();
        assertNull(luogo.getRegione());
        assertEquals(0, luogo.getLivello());
        assertNull(luogo.getNome());
    }

    @Test
    void testSevenArgConstructor() {
        LuogoEntity luogo = new LuogoEntity("NORD-BRA", 1, "Piazza Test", 44.6f, 7.8f, "BRA-1X5", "Indizio test");
        assertEquals("NORD-BRA", luogo.getRegione());
        assertEquals(1, luogo.getLivello());
        assertEquals("Piazza Test", luogo.getNome());
        assertEquals(44.6f, luogo.getCoordX(), 0.001f);
        assertEquals(7.8f, luogo.getCoordY(), 0.001f);
        assertEquals("BRA-1X5", luogo.getCodice());
        assertEquals("Indizio test", luogo.getIndizio());
        assertEquals("", luogo.getDescription());
        assertEquals("", luogo.getDescriptionPhoto());
    }

    @Test
    void testNineArgConstructor() {
        LuogoEntity luogo = new LuogoEntity("SUD-MELFI", 3, "Luogo Test", 40.5f, 15.5f, "MLF-2P6", "Indizio", "Descrizione lunga", "http://photo.jpg");
        assertEquals("SUD-MELFI", luogo.getRegione());
        assertEquals(3, luogo.getLivello());
        assertEquals("Descrizione lunga", luogo.getDescription());
        assertEquals("http://photo.jpg", luogo.getDescriptionPhoto());
    }

    @Test
    void testSetters() {
        LuogoEntity luogo = new LuogoEntity();
        luogo.setRegione("SUD-ENNA");
        luogo.setLivello(5);
        luogo.setNome("Test Nome");
        luogo.setCoordX(37.5f);
        luogo.setCoordY(14.2f);
        luogo.setCodice("ENN-1H3");
        luogo.setIndizio("Indizio test");
        luogo.setDescription("Desc");
        luogo.setDescriptionPhoto("photo.png");

        assertEquals("SUD-ENNA", luogo.getRegione());
        assertEquals(5, luogo.getLivello());
        assertEquals("Test Nome", luogo.getNome());
        assertEquals(37.5f, luogo.getCoordX(), 0.001f);
        assertEquals(14.2f, luogo.getCoordY(), 0.001f);
        assertEquals("ENN-1H3", luogo.getCodice());
        assertEquals("Indizio test", luogo.getIndizio());
        assertEquals("Desc", luogo.getDescription());
        assertEquals("photo.png", luogo.getDescriptionPhoto());
    }

    @Test
    void testLuogoIDEquals() {
        LuogoID id1 = new LuogoID("NORD-BRA", 1);
        LuogoID id2 = new LuogoID("NORD-BRA", 1);
        LuogoID id3 = new LuogoID("SUD-MELFI", 1);
        LuogoID id4 = new LuogoID("NORD-BRA", 2);

        assertEquals(id1, id2);
        assertNotEquals(id1, id3);
        assertNotEquals(id1, id4);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testLuogoIDEqualsEdgeCases() {
        LuogoID id1 = new LuogoID("NORD-BRA", 1);
        assertEquals(id1, id1); // same reference
        assertNotEquals(id1, null);
        assertNotEquals(id1, "not a LuogoID");
    }
}
