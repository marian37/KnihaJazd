package sk.upjs.ics.GUI;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import sk.upjs.ics.cestak.DaoFactory;
import sk.upjs.ics.cestak.VydavokTableModel;
import sk.upjs.ics.cestak.dao.AutoDAO;
import sk.upjs.ics.cestak.dao.VydavokDAO;
import sk.upjs.ics.cestak.entity.Auto;
import sk.upjs.ics.cestak.entity.Login;
import sk.upjs.ics.cestak.entity.Vydavok;

public class VydavkyForm extends JFrame {

    private static final Component CENTER_SCREEN = null;

    private JComboBox comboAuta = new JComboBox();

    private JPanel panPanel = new JPanel();
    private JButton btnPridaj = new JButton("Pridaj");
    private JButton btnUprav = new JButton("Uprav");
    private JButton btnVymaz = new JButton("Vymaž");

    private JTable tabVydavky = new JTable();
    private VydavokTableModel vydavokTableModel = new VydavokTableModel();

    private AutoDAO autoDao = DaoFactory.INSTANCE.autoDao();
    private VydavokDAO vydavokDao = DaoFactory.INSTANCE.vydavokDao();
    private Login login;
    private String autoSPZ;

    public VydavkyForm(Login login, Frame parent) {
        this();
        this.login = login;
        nastavComboAuta();
        obnovVydavky();
    }

    private VydavkyForm() {
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

        tabVydavky.setModel(vydavokTableModel);
        tabVydavky.setFillsViewportHeight(true);
        tabVydavky.setAutoCreateRowSorter(true);
        add(tabVydavky, "span 4");

        JScrollPane scrollPane = new JScrollPane(tabVydavky);
        add(scrollPane, "span 4");

        setPreferredSize(new Dimension(300, 400));
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();

        // ////////////////////////////////////////////////////////////////////
        btnPridaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnPridajActionPerformed(e);
            }
        });

        btnUprav.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnUpravActionPerformed(e);
            }
        });

        btnVymaz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnVymazActionPerformed(e);
            }
        });
    }

    private void btnPridajActionPerformed(ActionEvent e) {
        PridatVydavokForm pridatVydavokForm = new PridatVydavokForm(login, autoSPZ, this);
        pridatVydavokForm.setLocationRelativeTo(CENTER_SCREEN);
        pridatVydavokForm.setVisible(true);
    }

    private void btnUpravActionPerformed(ActionEvent e) {
        int vybranyRiadok = tabVydavky.getSelectedRow();
        int vybratyIndexVModeli = tabVydavky.convertRowIndexToModel(vybranyRiadok);
        Vydavok vydavok = vydavokTableModel.dajPodlaCislaRiadku(vybratyIndexVModeli);

        PridatVydavokForm pridatVydavokForm = new PridatVydavokForm(vydavok, this);
        pridatVydavokForm.setLocationRelativeTo(CENTER_SCREEN);
        pridatVydavokForm.setVisible(true);
    }

    private void btnVymazActionPerformed(ActionEvent e) {
        Object[] options = {"Áno", "Nie"};
        int n = JOptionPane.showOptionDialog(this, "Skutočne chcete vymazať výdavok?", "Vymazanie výdavku", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        // Ak užívateľ vyberie "ÁNO".
        if (n == 0) {
            int vybranyRiadok = tabVydavky.getSelectedRow();
            int vybratyIndexVModeli = tabVydavky.convertRowIndexToModel(vybranyRiadok);
            Vydavok vydavok = vydavokTableModel.dajPodlaCislaRiadku(vybratyIndexVModeli);
            vydavokDao.vymazVydavok(vydavok);
            obnovVydavky();
        }
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
                obnovVydavky();
            }
        });
    }

    public void obnovVydavky() {
        autoSPZ = comboAuta.getSelectedItem().toString();
        vydavokTableModel.obnov(autoSPZ);
        vydavokTableModel.fireTableDataChanged();
    }

    // Main - PridatCestuForm
    public static void main(String arg[]) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new WindowsLookAndFeel());

        VydavkyForm vydavkyForm = new VydavkyForm();
        vydavkyForm.setVisible(true);
        vydavkyForm.setTitle("Výdavky");
        vydavkyForm.setLocationRelativeTo(null);
    }

}
