package sk.upjs.ics.cestak;

import java.util.List;

/**
 *
 * @author Majlo
 */
public interface AutoDAO {

    List<Auto> zoznamPodlaPouzivatela(Login login);

    void saveAuto(Login login, Auto auto);

    void vymazAuto(Auto auto);
}
