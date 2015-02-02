package sk.upjs.ics.cestak;

/**
 *
 * @author Majlo
 */
public interface PrihlasenieDAO {

    Pouzivatel savePouzivatela(Pouzivatel pouzivatel);

    void saveLogin(Login login);

    Login verifyLogin(Login login);

    boolean verifyOnlyLogin(Login login);

    Pouzivatel getPouzivatelInfo(Login login); // pridane
}
