
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import sk.upjs.ics.cestak.Auto;
import sk.upjs.ics.cestak.AutoDAO;
import sk.upjs.ics.cestak.DaoFactory;
import sk.upjs.ics.cestak.Jazda;
import sk.upjs.ics.cestak.JazdaDAO;
import sk.upjs.ics.cestak.Login;

/**
 *
 * @author Matej
 */
public class DtbJazdaDAOTest {
    
    private Login login;
    private Auto auto;
    private final AutoDAO autoDAO = DaoFactory.INSTANCE.autoDao();
    private final JazdaDAO jazdaDAO = DaoFactory.INSTANCE.jazdaDao();

    // Počet uložených jázd v dbs na základe konkrétneho loginu a auta.
    private final int POCET_JAZD_PRE_LOGIN_PODLAAUTA = 2;
    
    @Before
    public void setUp() {
        login = new Login();
        login.setId(35);
        login.setLogin("admin");
        login.setHeslo("admin");

        // prvé auto v zozname
        auto = autoDAO.zoznamPodlaPouzivatela(login).get(0);
    }
    
    @Test
    public void testZoznamPodlaAut() {
        List<Jazda> zoznamJazd = jazdaDAO.zoznamPodlaAut(auto, login);
        assertEquals(POCET_JAZD_PRE_LOGIN_PODLAAUTA, zoznamJazd.size());
    }
    
    @Test
    public void testSaveVymazJazda() {
        List<Jazda> zoznamJazd = jazdaDAO.zoznamPodlaAut(auto, login);
        int pocetJazdPredUlozenim = zoznamJazd.size();
        
        Jazda jazda = new Jazda();
        jazda.setVyjazd("Košice");
        jazda.setPrijazd("Bratislava");
        jazda.setDatum("2015-01-01");
        jazda.setSPZ(auto.getSpz());
        jazda.setPoc_stav_km(12453);
        jazda.setCas_odchod("18:30");
        jazda.setCas_prichod("23:30");
        jazda.setPoznamka("Služobná cesta");
        jazda.setPrejdeneKilometre("350");
        jazda.setCerpaniePHM(10.0);
        jazda.setIdPouzivatel(login.getId());

        // Uloženie novovytvorenej jazdy do zoznamu jázd.
        jazdaDAO.saveJazda(jazda);
        zoznamJazd = jazdaDAO.zoznamPodlaAut(auto, login);
        int pocetJazdPoUlozeni = zoznamJazd.size();
        assertEquals(pocetJazdPredUlozenim + 1, pocetJazdPoUlozeni);

        // Je potrebné nastaviť jazde idčko aké dostaneme v DBS poďla PK, 
        // inak vymazJazda nevie, ktorú jazdu má vymazať.
        jazda.setIdJazda(63); // naposledy bol PK 62 -> PRAVIDELNE ITEROVAŤ ! 

        // Vymazanie novovytvorenej jazdy zo zoznamu jázd.
        jazdaDAO.vymazJazda(jazda);
        zoznamJazd = jazdaDAO.zoznamPodlaAut(auto, login);
        int pocetJazdPoVymazani = zoznamJazd.size();
        assertEquals(pocetJazdPredUlozenim, pocetJazdPoVymazani);
    }
    
    public void testUpravJazda() {
        List<Jazda> zoznamJazd = jazdaDAO.zoznamPodlaAut(auto, login);
        Jazda jazda = zoznamJazd.get(0);
        
        String povodnaOdkial = jazda.getVyjazd();
        String novaOdkial = "Prešov";
        jazda.setVyjazd(novaOdkial);
        
        jazdaDAO.saveJazda(jazda);
        zoznamJazd = jazdaDAO.zoznamPodlaAut(auto, login);
        assertEquals(novaOdkial, zoznamJazd.get(0).getVyjazd());
        
        jazda.setVyjazd(povodnaOdkial);
        jazdaDAO.saveJazda(jazda);
    }
}
