package sk.upjs.ics.cestak;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 *
 * @author Matej Perejda
 */
public class DtbAutoDAO implements AutoDAO {

    private JdbcTemplate jdbcTemplate;

    public DtbAutoDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private BeanPropertyRowMapper<Auto> mapovac
            = new BeanPropertyRowMapper<>(Auto.class);

    @Override
    public List<Auto> zoznamPodlaPouzivatela(Login login) {
        String sql = "SELECT * FROM Auto WHERE idPouzivatel = ?";
        List<Auto> s = jdbcTemplate.query(sql, mapovac, login.getId());

        return s;
    }

    @Override
    public void saveAuto(Login login, Auto auto) {
        if (auto.getIdPouzivatel() == 0) {
            Map into = new HashMap();
            into.put("znacka", auto.getZnacka());
            into.put("model", auto.getModel());
            into.put("SPZ", auto.getSpz());
            into.put("rok_vyr", auto.getRok_vyr());
            into.put("stav_tach", auto.getStav_tach());
            into.put("vykon", auto.getVykon());
            into.put("spotreba_mesto", auto.getSpotreba_mesto());
            into.put("spotreba_mimo", auto.getSpotreba_mimo());
            into.put("spotreba_avg", auto.getSpotreba_avg());
            into.put("palivo", auto.getPalivo());
            into.put("prevodovka", auto.getPrevodovka());
            into.put("klima", auto.getKlima());
            into.put("farba", auto.getFarba());
            into.put("idPouzivatel", login.getId());
            
            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);

            //insert.setGeneratedKeyName("idPouzivatel");
            insert.setTableName("Auto");
            insert.execute(into);

            String sql = "INSERT INTO PouzivatelAuto(`idPouzivatel`, `SPZ`) VALUES(?, ?)";
            jdbcTemplate.update(sql, login.getId(), auto.getSpz());
        } else {
            String sql = "UPDATE Auto\n"
                    + "SET znacka = ?,\n"
                    + "model = ?,\n"
                    + "SPZ = ?,\n"
                    + "rok_vyr = ?,\n"
                    + "stav_tach = ?,\n"
                    + "spotreba_avg = ?,\n"
                    + "spotreba_mesto = ?,\n"
                    + "spotreba_mimo = ?,\n"
                    + "palivo = ?,\n"
                    + "prevodovka = ?,\n"
                    + "klima = ?,\n"
                    + "farba = ?,\n"
                    + "vykon = ?,\n"
                    + "idPouzivatel = ?\n"
                    + "WHERE SPZ = ?";

            jdbcTemplate.update(sql,
                    auto.getZnacka(),                     
                    auto.getModel(),  
                    auto.getSpz(), 
                    auto.getRok_vyr(), 
                    auto.getStav_tach(), 
                    auto.getSpotreba_avg(), 
                    auto.getSpotreba_mesto(),
                    auto.getSpotreba_mimo(), 
                    auto.getPalivo(), 
                    auto.getPrevodovka(), 
                    auto.getKlima(), 
                    auto.getFarba(), 
                    auto.getVykon(), 
                    auto.getIdPouzivatel(), 
                    auto.getSpz()
            );
        }
    }

    @Override
    public void vymazAuto(Auto auto) {
        jdbcTemplate.update("DELETE FROM Auto WHERE SPZ = ?",
                auto.getSpz());
    }

}
