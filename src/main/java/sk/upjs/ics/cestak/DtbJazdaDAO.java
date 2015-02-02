package sk.upjs.ics.cestak;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 *
 *
 * @author Majlo
 */
public class DtbJazdaDAO implements JazdaDAO {

    private JdbcTemplate jdbcTemplate;

    public DtbJazdaDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private BeanPropertyRowMapper<Jazda> mapovac
            = new BeanPropertyRowMapper<>(Jazda.class);

    @Override
    public List<Jazda> zoznamPodlaAut(Auto auto, Login login) {
        String sql = "SELECT * FROM Jazda WHERE idPouzivatel = ? AND SPZ = ?";
        List<Jazda> s = jdbcTemplate.query(sql, mapovac, login.getId(), auto.getSpz());

        return s;
    }

    @Override
    public void saveJazda(Jazda jazda) {
        // Ak jazda ešte nemá žiadné id, tzn je ukladaná po prvýkrát.
        if (jazda.getIdJazda() == 0) {
            Map into = new HashMap();
            into.put("datum", jazda.getDatum());
            into.put("vyjazd", jazda.getVyjazd());
            into.put("prijazd", jazda.getPrijazd());
            into.put("SPZ", jazda.getSPZ());
            into.put("poc_stav_km", jazda.getPoc_stav_km());
            into.put("cas_odchod", jazda.getCas_odchod());
            into.put("cas_prichod", jazda.getCas_prichod());
            into.put("poznamka", jazda.getPoznamka());
            into.put("prejdeneKilometre", jazda.getPrejdeneKilometre());
            into.put("cerpaniePHM", jazda.getCerpaniePHM());
            into.put("idPouzivatel", jazda.getIdPouzivatel());

            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);

            insert.setTableName("Jazda");
            insert.execute(into);
        } else {
            // Ak aktualizujeme jazdu.
            String sql = "UPDATE Jazda\n"
                    + "SET datum = ?,\n"
                    + "vyjazd = ?,\n"
                    + "prijazd = ?,\n"
                    + "SPZ = ?,\n"
                    + "poc_stav_km = ?,\n"
                    + "cas_odchod = ?,\n"
                    + "cas_prichod = ?,\n"
                    + "poznamka = ?,\n"
                    + "prejdeneKilometre = ?,\n"
                    + "cerpaniePHM = ?,\n"
                    + "idPouzivatel = ?\n"
                    + "WHERE idJazda = ?";

            jdbcTemplate.update(sql,
                    jazda.getDatum(),
                    jazda.getVyjazd(),
                    jazda.getPrijazd(),
                    jazda.getSPZ(),
                    jazda.getPoc_stav_km(),
                    jazda.getCas_odchod(),
                    jazda.getCas_prichod(),
                    jazda.getPoznamka(),
                    jazda.getPrejdeneKilometre(),
                    jazda.getCerpaniePHM(),
                    jazda.getIdPouzivatel(),
                    jazda.getIdJazda()
            );
        }
    }

    @Override
    public void vymazJazda(Jazda jazda) {
        jdbcTemplate.update("DELETE FROM Jazda WHERE idJazda = ?",
                jazda.getIdJazda());
    }
}
