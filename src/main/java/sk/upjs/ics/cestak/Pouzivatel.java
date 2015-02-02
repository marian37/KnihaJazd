package sk.upjs.ics.cestak;

/**
 *
 * @author Majlo
 */
public class Pouzivatel {
    private int idPouzivatel;
    
    private String meno;
    
    private String priezvisko;
    
    private String pohlavie;
    
    private String datum;
    
    private String adresa;
    
    private String email;
    
    private String tel;

    /**
     * @return the id
     */
    public int getId() {
        return idPouzivatel;
    }

    /**
     * @param idPouzivatel the id to set
     */
    public void setId(int idPouzivatel) {
        this.idPouzivatel = idPouzivatel;
    }

    /**
     * @return the meno
     */
    public String getMeno() {
        return meno;
    }

    /**
     * @param meno the meno to set
     */
    public void setMeno(String meno) {
        this.meno = meno;
    }

    /**
     * @return the priezvisko
     */
    public String getPriezvisko() {
        return priezvisko;
    }

    /**
     * @param priezvisko the priezvisko to set
     */
    public void setPriezvisko(String priezvisko) {
        this.priezvisko = priezvisko;
    }

    /**
     * @return the pohlavie
     */
    public String getPohlavie() {
        return pohlavie;
    }

    /**
     * @param pohlavie the pohlavie to set
     */
    public void setPohlavie(String pohlavie) {
        this.pohlavie = pohlavie;
    }

    /**
     * @return the datum
     */
    public String getDatum() {
        return datum;
    }

    /**
     * @param datum the datum to set
     */
    public void setDatum(String datum) {
        this.datum = datum;
    }

    /**
     * @return the adresa
     */
    public String getAdresa() {
        return adresa;
    }

    /**
     * @param adresa the adresa to set
     */
    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the tel
     */
    public String getTel() {
        return tel;
    }

    /**
     * @param tel the tel to set
     */
    public void setTel(String tel) {
        this.tel = tel;
    }
}
