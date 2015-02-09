package sk.upjs.ics.cestak.dao;

import java.util.List;
import sk.upjs.ics.cestak.entity.Auto;
import sk.upjs.ics.cestak.entity.Login;

/**
 *
 * @author Majlo
 */
public interface AutoDAO {

    List<Auto> zoznamPodlaPouzivatela(Login login);

    void saveAuto(Login login, Auto auto);

    void vymazAuto(Auto auto);
}
