
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import sk.upjs.ics.cestak.Auto;
import sk.upjs.ics.cestak.AutoDAO;
import sk.upjs.ics.cestak.DaoFactory;
import sk.upjs.ics.cestak.Login;

/**
 *
 * @author Matej
 */
public class DtbAutoDAOTest {

    private final AutoDAO autoDAO = DaoFactory.INSTANCE.autoDao();
    private Login login;
    private Auto auto;

    // Aktuálny počet áut v databáze pre konkrétneho užívateľa.
    private final int POCET_AUT_PRE_LOGIN_DBS = 2;

    @Before
    public void setUp() {
        login = new Login();
        login.setId(35);
        login.setLogin("admin");
        login.setHeslo("admin");
    }

    @Test
    public void testZoznamPodlaPouzivatela() {
        List<Auto> zoznamAut = autoDAO.zoznamPodlaPouzivatela(login);
        assertEquals(POCET_AUT_PRE_LOGIN_DBS, zoznamAut.size());
    }

    @Test
    public void testSaveVymazAuto() {
        List<Auto> zoznamAut = autoDAO.zoznamPodlaPouzivatela(login);
        int pocetAutPredUlozenim = zoznamAut.size();

        // Vytvorenie nového testovacieho auta.        
        auto = new Auto();
        auto.setZnacka("Audi");
        auto.setModel("S5");
        auto.setSpz("KE042EK");
        auto.setVykon(260);
        auto.setFarba("Čierna");
        auto.setKlima("Áno");
        auto.setPalivo("Diesel");
        auto.setStav_tach(5230);
        auto.setSpotreba_avg(10);
        auto.setSpotreba_mesto(12);
        auto.setSpotreba_mimo(9);
        auto.setPrevodovka("Automatická");
        auto.setRok_vyr("2014");

        // Uloženie novovytvoreného auta do zoznamu áut.
        autoDAO.saveAuto(login, auto);
        zoznamAut = autoDAO.zoznamPodlaPouzivatela(login);
        int pocetAutPoUlozeni = zoznamAut.size();
        assertEquals(pocetAutPredUlozenim + 1, pocetAutPoUlozeni);

        // Vymazanie novovytvoreného auta zo zoznamu áut.
        autoDAO.vymazAuto(auto);
        zoznamAut = autoDAO.zoznamPodlaPouzivatela(login);
        int pocetAutPoVymazani = zoznamAut.size();
        assertEquals(pocetAutPredUlozenim, pocetAutPoVymazani);
    }

    @Test
    public void upravAuto() {
        List<Auto> zoznamAut = autoDAO.zoznamPodlaPouzivatela(login); 
        auto = zoznamAut.get(0);
        
        String povodnaZnacka = auto.getZnacka();
        String novaZnacka = "Mercedes";
        auto.setZnacka(novaZnacka);
        
        autoDAO.saveAuto(login, auto);
        zoznamAut = autoDAO.zoznamPodlaPouzivatela(login);
        assertEquals(novaZnacka, zoznamAut.get(0).getZnacka());
        
        auto.setZnacka(povodnaZnacka);
        autoDAO.saveAuto(login, auto);
    }
}
