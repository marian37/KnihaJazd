package sk.upjs.ics.GUI;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import sk.upjs.ics.cestak.DaoFactory;
import sk.upjs.ics.cestak.dao.AutoDAO;
import sk.upjs.ics.cestak.entity.Auto;
import sk.upjs.ics.cestak.entity.Login;

public class VydavkyForm extends JDialog {
    
    private JComboBox comboAuta = new JComboBox();

    private JButton btnPridaj = new JButton("Pridaj");
    private JButton btnUprav = new JButton("Uprav");
    private JButton btnVymaz = new JButton("Vymaž");

    private JPanel panPanel = new JPanel();
    private JScrollPane scrollPane = new JScrollPane();
    private JTable tabVydavky = new JTable();

    private AutoDAO autoDao = DaoFactory.INSTANCE.autoDao();
    private Login login;
    private String autoSPZ;

    
    public VydavkyForm(Login login, Frame parent) {
        this(parent, true);
        this.login = login;
        nastavComboAuta();
    }

    public VydavkyForm(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setLayout(new MigLayout("", "[fill, grow][][][]", "[][][fill, grow]"));
        add(comboAuta, "span 4, wrap");
        add(panPanel);
        add(btnPridaj);
        add(btnUprav);
        add(btnVymaz, "wrap");
        add(scrollPane, "span 4");
        scrollPane.add(tabVydavky);
        tabVydavky.setModel(new DefaultTableModel());

        setPreferredSize(new Dimension(300, 400));
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }
    
    private void nastavComboAuta() {
        List<Auto> auta = autoDao.zoznamPodlaPouzivatela(login);
        List<String> autaSpz = new ArrayList<>();
        for (Auto auto : auta) {
            autaSpz.add(auto.getSpz());
        }
        comboAuta.setModel(new DefaultComboBoxModel(autaSpz.toArray()));
        comboAuta.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                autoSPZ = comboAuta.getSelectedItem().toString();
                // moja aktualizacna metoda
            }
        });
    }

    // Main - PridatCestuForm
    public static void main(String arg[]) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new WindowsLookAndFeel());

        VydavkyForm vydavkyForm = new VydavkyForm(new javax.swing.JFrame(), true);
        vydavkyForm.setVisible(true);
        vydavkyForm.setTitle("Výdavky");
        vydavkyForm.setLocationRelativeTo(null);
    }

}
