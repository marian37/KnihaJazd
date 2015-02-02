package sk.upjs.ics.cestak;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 *
 * @author Majlo
 */
public class DtbPrihlasenieDAO implements PrihlasenieDAO {

    private JdbcTemplate jdbcTemplate;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DtbPrihlasenieDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.jdbcTemplate);
    }

    // Prazdny konstruktor pre JUnitTest
    public DtbPrihlasenieDAO() {

    }

    private BeanPropertyRowMapper<Login> mapovac
            = new BeanPropertyRowMapper<>(Login.class);

    private BeanPropertyRowMapper<Pouzivatel> mapovac2
            = new BeanPropertyRowMapper<>(Pouzivatel.class);

    /*@Override
     public Pouzivatel savePouzivatela(Pouzivatel pouzivatel) {
     Map<String, Object> into = new HashMap<String, Object>();
     into.put("meno", pouzivatel.getMeno());
     into.put("priezvisko", pouzivatel.getPriezvisko());
     into.put("pohlavie", pouzivatel.getPohlavie());
     into.put("datum", pouzivatel.getDatum());
     into.put("adresa", pouzivatel.getAdresa());
     into.put("email", pouzivatel.getEmail());
     into.put("tel", pouzivatel.getTel());

     SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);

     insert.setGeneratedKeyName("idPouzivatel");

     insert.setTableName("Pouzivatel");
     Number id = insert.executeAndReturnKey(into);
     pouzivatel.setId(id.intValue());
     return pouzivatel;
     }*/
    @Override
    public Pouzivatel savePouzivatela(Pouzivatel pouzivatel) {
        if (pouzivatel.getId() == 0) {
            Map<String, Object> into = new HashMap<String, Object>();
            into.put("meno", pouzivatel.getMeno());
            into.put("priezvisko", pouzivatel.getPriezvisko());
            into.put("pohlavie", pouzivatel.getPohlavie());
            into.put("datum", pouzivatel.getDatum());
            into.put("adresa", pouzivatel.getAdresa());
            into.put("email", pouzivatel.getEmail());
            into.put("tel", pouzivatel.getTel());

            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);

            insert.setGeneratedKeyName("idPouzivatel");

            insert.setTableName("Pouzivatel");
            Number id = insert.executeAndReturnKey(into);
            pouzivatel.setId(id.intValue());
            return pouzivatel;
        } else {
            // update
            String sql = "UPDATE Pouzivatel\n"
                    + "SET meno = ?,\n"
                    + "priezvisko = ?,\n"
                    + "pohlavie = ?,\n"
                    + "datum = ?,\n"
                    + "adresa = ?,\n"
                    + "email = ?,\n"
                    + "tel = ?\n"
                    + "WHERE idPouzivatel = ?";

            jdbcTemplate.update(sql,
                    pouzivatel.getMeno(),
                    pouzivatel.getPriezvisko(),
                    pouzivatel.getPohlavie(),
                    pouzivatel.getDatum(),
                    pouzivatel.getAdresa(),
                    pouzivatel.getEmail(),
                    pouzivatel.getTel(),
                    pouzivatel.getId()
            );
            return pouzivatel;
        }
    }

    @Override
    public void saveLogin(Login login) {
        String sql = "INSERT INTO Login(`idPouzivatel`, `login`, `heslo`) VALUES(?, ?, ?)";
        jdbcTemplate.update(sql, login.getId(), login.getLogin(), login.getHeslo());
    }

    @Override
    public Login verifyLogin(Login login) {
//        int hash = login.getHeslo().hashCode();
        String sql = "SELECT l.idPouzivatel AS id, l.login, l.heslo FROM Login AS l WHERE l.login = ? AND l.heslo = ?";
        try {
            return jdbcTemplate.queryForObject(sql, mapovac, login.getLogin(), login.getHeslo());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public boolean verifyOnlyLogin(Login login) {
        String sql = "SELECT * FROM Login WHERE login = ?";
        try {
            jdbcTemplate.queryForObject(sql, mapovac, login.getLogin());
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public Pouzivatel getPouzivatelInfo(Login login) {
        Pouzivatel novy = new Pouzivatel();

        String sql = "SELECT * FROM Pouzivatel WHERE idPouzivatel = ?";
        List<Pouzivatel> zoznam = jdbcTemplate.query(sql, mapovac2, login.getId());

        novy.setId(login.getId());
        novy.setMeno(zoznam.get(0).getMeno());
        novy.setPriezvisko(zoznam.get(0).getPriezvisko());
        novy.setPohlavie(zoznam.get(0).getPohlavie());
        novy.setDatum(zoznam.get(0).getDatum());
        novy.setAdresa(zoznam.get(0).getAdresa());
        novy.setEmail(zoznam.get(0).getEmail());
        novy.setTel(zoznam.get(0).getTel());

        return novy;
    }
}
