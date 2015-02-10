
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import sk.upjs.ics.cestak.DaoFactory;
import sk.upjs.ics.cestak.dao.VydavokDAO;
import sk.upjs.ics.cestak.entity.Login;
import sk.upjs.ics.cestak.entity.Vydavok;

public class DtbVydavokDAOTest {

    private Login login;
    private String autoSPZ;
    private final VydavokDAO vydavokDAO = DaoFactory.INSTANCE.vydavokDao();

    private static final int POCET_VYDAVKOV_V_DB = 3;

    @Before
    public void setUp() {
        login = new Login();
        login.setId(7);
        login.setLogin("marian");
        login.setHeslo("marian");

        autoSPZ = "DS546DS";
    }

    @Test
    public void testVydavkyPodlaAuta() {
        List<Vydavok> vydavky = vydavokDAO.vydavkyPodlaAuta(autoSPZ);
        assertEquals(POCET_VYDAVKOV_V_DB, vydavky.size());
    }

    @Test
    public void testSaveVymazVydavok() {
        List<Vydavok> vydavky = vydavokDAO.vydavkyPodlaAuta(autoSPZ);
        int pocetVydavkov = vydavky.size();

        Vydavok vydavok = new Vydavok();
        vydavok.setAutoSPZ(autoSPZ);
        vydavok.setKategoria("test");
        vydavok.setSuma(5);

        vydavokDAO.saveVydavok(vydavok);

        vydavky = vydavokDAO.vydavkyPodlaAuta(autoSPZ);
        assertEquals(pocetVydavkov + 1, vydavky.size());

        for (Vydavok v : vydavky) {
            if ("test".equals(v.getKategoria())) {
                vydavok = v;
            }
        }

        vydavokDAO.vymazVydavok(vydavok);

        vydavky = vydavokDAO.vydavkyPodlaAuta(autoSPZ);
        assertEquals(pocetVydavkov, vydavky.size());
    }

    @Test
    public void testUpravVydavok() {
        Vydavok vydavok = vydavokDAO.vydavkyPodlaAuta(autoSPZ).get(0);
        String staraKategoria = vydavok.getKategoria();
        String novaKategoria = "testovanie";

        vydavok.setKategoria(novaKategoria);

        vydavokDAO.saveVydavok(vydavok);

        vydavok = vydavokDAO.vydavkyPodlaAuta(autoSPZ).get(0);

        assertEquals(novaKategoria, vydavok.getKategoria());

        vydavok.setKategoria(staraKategoria);
        vydavokDAO.saveVydavok(vydavok);

        vydavok = vydavokDAO.vydavkyPodlaAuta(autoSPZ).get(0);

        assertEquals(staraKategoria, vydavok.getKategoria());
    }

}
