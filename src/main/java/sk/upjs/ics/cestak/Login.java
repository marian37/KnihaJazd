package sk.upjs.ics.cestak;

/**
 *
 * @author Majlo
 */
public class Login {
    private int id;
    
    private String login;
    
    private String heslo;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param idPouzivatel the id to set
     */
    public void setId(int idPouzivatel) {
        this.id = idPouzivatel;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the heslo
     */
    public String getHeslo() {
        return heslo;
    }

    /**
     * @param heslo the heslo to set
     */
    public void setHeslo(String heslo) {
        this.heslo = heslo;
    }
}
