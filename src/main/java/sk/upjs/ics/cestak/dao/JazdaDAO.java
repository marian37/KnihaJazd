package sk.upjs.ics.cestak.dao;

import java.util.List;
import sk.upjs.ics.cestak.entity.Auto;
import sk.upjs.ics.cestak.entity.Jazda;
import sk.upjs.ics.cestak.entity.Login;

/**
 *
 * @author Matej Perejda
 */
public interface JazdaDAO {

    List<Jazda> zoznamPodlaAut(Auto auto, Login login);

    void saveJazda(Jazda jazda);

    void vymazJazda(Jazda jazda);
}
