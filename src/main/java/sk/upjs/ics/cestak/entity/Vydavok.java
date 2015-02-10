package sk.upjs.ics.cestak.entity;

public class Vydavok {

    private Long id;

    private String autoSPZ;

    private String kategoria;

    private double suma;

    /**
     * @return the auto
     */
    public String getAutoSPZ() {
        return autoSPZ;
    }

    /**
     * @param autoSPZ the autoSPZ to set
     */
    public void setAutoSPZ(String autoSPZ) {
        this.autoSPZ = autoSPZ;
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
    public double getSuma() {
        return suma;
    }

    /**
     * @param suma the suma to set
     */
    public void setSuma(double suma) {
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
