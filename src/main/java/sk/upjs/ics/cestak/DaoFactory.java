package sk.upjs.ics.cestak;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Majlo
 */
public enum DaoFactory {

    INSTANCE;

    private PrihlasenieDAO prihlasenieDao;

    private AutoDAO autoDao;

    private JazdaDAO jazdaDao;

    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate jdbcTemplate() {
        if (this.jdbcTemplate == null) {
            this.jdbcTemplate = new JdbcTemplate(dataSource());
        }
        return this.jdbcTemplate;

    }

    public DataSource dataSource() {
        MysqlDataSource dataSource = new MysqlDataSource();
        Properties properties = getProperties();
        dataSource.setURL("jdbc:mysql://sql3.freemysqlhosting.net:3306/sql366146?zeroDateTimeBehavior=convertToNull");
        dataSource.setUser("sql366146");
        dataSource.setPassword(properties.getProperty("heslo"));

        return dataSource;
    }

    public PrihlasenieDAO prihlasenieDao() {
        if (this.prihlasenieDao == null) {
            this.prihlasenieDao = new DtbPrihlasenieDAO(jdbcTemplate());
        }
        return this.prihlasenieDao;
    }

    public AutoDAO autoDao() {
        if (this.autoDao == null) {
            this.autoDao = new DtbAutoDAO(jdbcTemplate());
        }
        return this.autoDao;
    }

    public JazdaDAO jazdaDao() {
        if (this.jazdaDao == null) {
            this.jazdaDao = new DtbJazdaDAO(jdbcTemplate());
        }
        return this.jazdaDao;
    }

    private Properties getProperties() {
        try {
            String propertiesFile = "/cestak.properties";

            InputStream in = DaoFactory.class.getResourceAsStream(propertiesFile);

            Properties properties = new Properties();
            properties.load(in);

            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("Nenasiel sa konfiguracny subor");
        }
    }

}
