package sk.upjs.ics.cestak;

import java.util.List;
import java.util.LinkedList;
import javax.swing.table.AbstractTableModel;
import sun.awt.SunToolkit;

/**
 *
 * @author Majlo
 */
public class JazdaTableModel extends AbstractTableModel {

    private static final int POCET_STLPCOV = 5;

    private static final Class[] TYPY_STLPCOV = {
        String.class,
        String.class,
        int.class,
        Double.class,
        String.class
    };

    private static final String[] NAZVY_STLPCOV = {"Odkiaľ", "Kam", "Prejdené (km)", "Čerpanie PHM", "Dátum"};

    private JazdaDAO jazdaDao = DaoFactory.INSTANCE.jazdaDao();

    private List<Jazda> jazda = new LinkedList<>();

    // Počet riadkov tabuľky JTable.
    @Override
    public int getRowCount() {
        return jazda.size();
    }

    // Počet stĺpcov tabuľky JTable.
    @Override
    public int getColumnCount() {
        return POCET_STLPCOV;
    }

    // Pridaná hodnotu na zadaný riadok, stĺpec JTable.
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Jazda selectedJazda = jazda.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return selectedJazda.getVyjazd();
            case 1:
                return selectedJazda.getPrijazd();
            case 2:
                return selectedJazda.getPrejdeneKilometre();
            case 3:
                return selectedJazda.getCerpaniePHM();
            case 4:
                return selectedJazda.getDatum();
            default:
                return "!!!";
        }
    }

    // Meno stĺpca.
    @Override
    public String getColumnName(int column) {
        return NAZVY_STLPCOV[column];
    }

    // Typ stĺpca.
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return TYPY_STLPCOV[columnIndex];
    }

    public Jazda dajPodlaCislaRiadku(int riadok) {
        return jazda.get(riadok);
    }

    // Obnoví zoznam aút v JTable.
    public void obnov(Auto auto, Login login) {
        this.jazda = jazda;
        jazda = jazdaDao.zoznamPodlaAut(auto, login);
    }

}
