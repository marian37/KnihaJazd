package sk.upjs.ics.cestak;

import sk.upjs.ics.cestak.entity.Auto;
import java.util.List;
import java.util.LinkedList;
import javax.swing.table.AbstractTableModel;
import sk.upjs.ics.cestak.dao.VydavokDAO;
import sk.upjs.ics.cestak.entity.Vydavok;

/**
 *
 * @author Majlo
 */
public class VydavokTableModel extends AbstractTableModel {

    private static final int POCET_STLPCOV = 2;

    private static final Class[] TYPY_STLPCOV = {
        String.class,
        double.class,};

    private static final String[] NAZVY_STLPCOV = {"Kategória", "Suma"};

    private VydavokDAO vydavokDAO = DaoFactory.INSTANCE.vydavokDao();

    private List<Vydavok> vydavky = new LinkedList<>();

    // Počet riadkov tabuľky JTable.
    @Override
    public int getRowCount() {
        return vydavky.size();
    }

    // Počet stĺpcov tabuľky JTable.
    @Override
    public int getColumnCount() {
        return POCET_STLPCOV;
    }

    // Pridaná hodnotu na zadaný riadok, stĺpec JTable.
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Vydavok selectedVydavok = vydavky.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return selectedVydavok.getKategoria();
            case 1:
                return selectedVydavok.getSuma();
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

    public Vydavok dajPodlaCislaRiadku(int riadok) {
        return vydavky.get(riadok);
    }

    // Obnoví zoznam aút v JTable.
    public void obnov(String autoSPZ) {
        this.vydavky = vydavky;
        vydavky = vydavokDAO.vydavkyPodlaAuta(autoSPZ);
    }

}
