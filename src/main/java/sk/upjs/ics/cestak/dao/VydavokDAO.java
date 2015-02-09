package sk.upjs.ics.cestak.dao;

import java.util.List;
import sk.upjs.ics.cestak.entity.Auto;
import sk.upjs.ics.cestak.entity.Vydavok;

public interface VydavokDAO {
    
    List<Vydavok> vydavkyPodlaAuta(Auto auto);
    
    void saveVydavok(Vydavok vydavok);
    
    void vymazVydavok(Vydavok vydavok);
    

}
