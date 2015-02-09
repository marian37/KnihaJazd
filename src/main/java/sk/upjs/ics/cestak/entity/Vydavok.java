package sk.upjs.ics.cestak.entity;

public class Vydavok {
    
    private Long id;
    
    private String autoSPZ;
    
    private String kategoria;
    
    private int suma;

    /**
     * @return the auto
     */
    public String getAutoSPZ() {
        return autoSPZ;
    }

    /**
     * @param auto the auto to set
     */
    public void setAutoSPZ(Auto auto) {
        this.autoSPZ = auto.getSpz(); // este v stadiu riesenia asjf[aspdfkasldas][dl
    }

    /**
     * @return the kategoria
     */
    public String getKategoria() {
        return kategoria;
    }

    /**
     * @param kategoria the kategoria to set
     */
    public void setKategoria(String kategoria) {
        this.kategoria = kategoria;
    }

    /**
     * @return the suma
     */
    public int getSuma() {
        return suma;
    }

    /**
     * @param suma the suma to set
     */
    public void setSuma(int suma) {
        this.suma = suma;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

}
