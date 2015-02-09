package sk.upjs.ics.cestak.datbazoveDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import sk.upjs.ics.cestak.dao.VydavokDAO;
import sk.upjs.ics.cestak.entity.Auto;
import sk.upjs.ics.cestak.entity.Vydavok;

public class DtbVydavokDAO implements VydavokDAO {

    private JdbcTemplate jdbcTemplate;

    public DtbVydavokDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private BeanPropertyRowMapper<Vydavok> mapovac
            = new BeanPropertyRowMapper<>(Vydavok.class);

    @Override
    public List<Vydavok> vydavkyPodlaAuta(Auto auto) {
        String sql = "SELECT * FROM Vydavok WHERE autoSPZ = ?";
        List<Vydavok> s = jdbcTemplate.query(sql, mapovac, auto.getSpz());

        return s;
    }

    @Override
    public void saveVydavok(Vydavok vydavok) {
        if (vydavok.getId() == null) {
            Map vybranyVydavok = new HashMap();
            vybranyVydavok.put("autoSPZ", vydavok.getAutoSPZ());
            vybranyVydavok.put("kategoria", vydavok.getKategoria());
            vybranyVydavok.put("suma", vydavok.getSuma());

            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);

            insert.setTableName("Vydavok");
            insert.execute(vybranyVydavok);
        } else {
            String sql = "UPDATE Vydavok\n"
                    + "SET autoSPZ = ?,\n"
                    + "kategoria = ?,\n"
                    + "suma = ?,\n"
                    + "WHERE id = ?";

            jdbcTemplate.update(sql, vydavok.getAutoSPZ(), vydavok.getKategoria(), vydavok.getSuma(), vydavok.getId());
        }
    }

    @Override
    public void vymazVydavok(Vydavok vydavok) {
        jdbcTemplate.update("DELETE FROM Vydavok WHERE id = ?",
                vydavok.getId());
    }

}
