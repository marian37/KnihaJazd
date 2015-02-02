package sk.upjs.ics.GUI;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import net.miginfocom.swing.MigLayout;
import sk.upjs.ics.cestak.Auto;
import sk.upjs.ics.cestak.AutoDAO;
import sk.upjs.ics.cestak.DaoFactory;
import sk.upjs.ics.cestak.Login;

/**
 * PridatAuto okno. Beta verzia
 *
 * @author Matej Perejda
 */
public class PridatAutoForm extends JDialog {

    private static final Component CENTER_SCREEN = null;

    // Tlačidlá
    private JButton btnUlozit = new JButton("Uložiť");
    private JButton btnZrusit = new JButton("Zrušiť");

    // Labely
    private JLabel lblZnacka = new JLabel("Značka:");
    private JLabel lblZnackaIna = new JLabel("Iná:");
    private JLabel lblModel = new JLabel("Model:");
    private JLabel lblECV = new JLabel("EČV");
    private JLabel lblPrevodovka = new JLabel("Prevodovka:");
    private JLabel lblRokVyroby = new JLabel("Rok výroby:");
    private JLabel lblStavTachometra = new JLabel("Stav tachometra (km):");
    private JLabel lblVykon = new JLabel("Výkon (kW):");
    private JLabel lblPalivo = new JLabel("Palivo:");
    private JLabel lblFarba = new JLabel("Farba:");
    private JLabel lblFarbaIna = new JLabel("Iná:");
    private JLabel lblKlimatizacia = new JLabel("Klimatizácia:");
    private JLabel lblSpotrebaMesto = new JLabel("Spotreba - v meste (l/100km):");
    private JLabel lblSpotrebaMimo = new JLabel("Spotreba - mimo mesta (l/100km):");
    private JLabel lblSpotrebaKomb = new JLabel("Spotreba - kombinácia (l/100km):");

    // Textové polia
    private JTextField txtZnackaIna = new JTextField();
    private JTextField txtModel = new JTextField();
    private JTextField txtECV = new JTextField();
    private JTextField txtRokVyroby = new JTextField();
    private JTextField txtStavTachometra = new JTextField();
    private JTextField txtVykon = new JTextField();
    private JTextField txtFarbaIna = new JTextField();
    private JTextField txtSpotrebaMesto = new JTextField();
    private JTextField txtSpotrebaMimo = new JTextField();
    private JTextField txtSpotrebaKomb = new JTextField();

    // Combobox
    private JComboBox comboZnacka = new JComboBox();
    private JComboBox comboPrevodovka = new JComboBox();
    private JComboBox comboPalivo = new JComboBox();
    private JComboBox comboFarba = new JComboBox();
    private JComboBox comboKlimatizacia = new JComboBox();

    private Login login;
    private Auto auto; // Matej

    private AutoDAO autoDao = DaoFactory.INSTANCE.autoDao();

// ********************** Konštruktory ************************************** 
    public PridatAutoForm(Login login, Frame parent) {
        this(parent, true);
        this.login = login;
    }

    public PridatAutoForm(Login login, Auto auto, Frame parent) {
        this(parent, true);
        this.login = login;
        this.auto = auto;
        System.out.println("ID log.: " + login.getId() + ", auto: " + auto.getModel()
                + ", SPZ:" + auto.getSpz() + "auto idPouz.: " + auto.getIdPouzivatel()); // edit
    }

    public PridatAutoForm(Frame parent) {
        this(new Login(), new Auto(), parent);
    }

    // Editovacie okno.
    public PridatAutoForm(Auto auto, Frame parent) {
        this(parent, true);
        this.auto = auto;
        DecimalFormat df = new DecimalFormat("#.##");

        comboZnacka.setSelectedItem(auto.getZnacka());
        txtModel.setText(auto.getModel());
        txtECV.setText(auto.getSpz());

        txtRokVyroby.setText(auto.getRok_vyr());
        txtStavTachometra.setText(Long.toString(auto.getStav_tach()));
        txtVykon.setText(df.format(auto.getVykon()));
        txtSpotrebaMesto.setText(df.format(auto.getSpotreba_mesto()));
        txtSpotrebaMimo.setText(df.format(auto.getSpotreba_mimo()));
        txtSpotrebaKomb.setText(df.format(auto.getSpotreba_avg()));

        comboPalivo.setSelectedItem(auto.getPalivo());
        comboPrevodovka.setSelectedItem(auto.getPrevodovka());
        comboKlimatizacia.setSelectedItem(auto.getKlima());
        comboFarba.setSelectedItem(auto.getFarba());
    }

    public PridatAutoForm(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setLayout(new MigLayout("", "[fill, grow][fill, grow][fill, grow][fill, grow]", "[][][][][][][][][][][][][][nogrid]"));

        nastavZnackuGUI();
        add(lblModel);
        add(txtModel, "wrap");
        add(lblECV);
        add(txtECV, "wrap");
        txtECV.setToolTipText("Evidenčné číslo vozidla");
        add(lblRokVyroby);
        add(txtRokVyroby, "wrap");
        add(lblStavTachometra);
        add(txtStavTachometra, "wrap");
        add(lblVykon);
        add(txtVykon, "wrap");
        add(lblSpotrebaMesto);
        add(txtSpotrebaMesto, "wrap");
        add(lblSpotrebaMimo);
        add(txtSpotrebaMimo, "wrap");
        add(lblSpotrebaKomb);
        add(txtSpotrebaKomb, "wrap");
        nastavPalivoGUI();
        nastavPrevodovkuGUI();
        nastavKlimatizaciuGUI();
        nastavFarbuGUI();

        // Tlačidlo ULOŽIŤ.
        add(btnUlozit, "tag ok");
        // Akcia pre stlačenie tlačidla uložiť
        btnUlozit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Zaregistrovať
                btnUlozitActionPerformed(e);
                System.out.println("Ukladam...");
            }
        });

        // Tlačídlo ZRUŠIŤ.
        add(btnZrusit, "tag cancel");
        // Akcia pre stlačenie tlačidla zrušiť.
        btnZrusit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setPreferredSize(new Dimension(500, 380));
        setResizable(false); // ZMENIŤ NA FALSE !!!             
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }

    // Nastavenie pre klimatizáciu. [DONE]
    private void nastavKlimatizaciuGUI() {
        add(lblKlimatizacia);
        add(comboKlimatizacia, "wrap");
        comboKlimatizacia.addItem(null);
        comboKlimatizacia.addItem("Áno");
        comboKlimatizacia.addItem("Nie");
        comboKlimatizacia.setSelectedItem(null);
    }

    // Nastavenie pre farbu. [DONE]
    private void nastavFarbuGUI() {
        add(lblFarba);
        add(comboFarba);
        comboFarba.addItem(null);
        comboFarba.addItem("Biela");
        comboFarba.addItem("Červená");
        comboFarba.addItem("Čierna");
        comboFarba.addItem("Modrá");
        comboFarba.addItem("Zelená");
        comboFarba.addItem("Žltá");
        comboFarba.setSelectedItem(null);
        add(lblFarbaIna);
        add(txtFarbaIna, "wrap");
    }

    // Nastavenie pre palivo. [DONE]
    private void nastavPalivoGUI() {
        add(lblPalivo);
        add(comboPalivo, "wrap");
        comboPalivo.addItem(null);
        comboPalivo.addItem("Benzín");
        comboPalivo.addItem("Diesel");
        comboPalivo.addItem("Benzín + plyn");
        comboPalivo.addItem("Elektro");
        comboPalivo.setSelectedItem(null);
    }

    // Nastavenie pre prevodovku. [DONE]
    private void nastavPrevodovkuGUI() {
        add(lblPrevodovka);
        add(comboPrevodovka, "wrap");
        comboPrevodovka.addItem(null);
        comboPrevodovka.addItem("Manuálna");
        comboPrevodovka.addItem("Automatická");
        comboPrevodovka.setSelectedItem(null);
    }

    // Nastavenie pre značku. [DONE]
    private void nastavZnackuGUI() {
        add(lblZnacka);
        add(comboZnacka);
        comboZnacka.addItem(null);
        try {
            pridajZnackyAutGUI();
        } catch (FileNotFoundException ex) {
            System.out.println("Nenašiel sa súbor so značkami áut.");
        }
        comboZnacka.setSelectedItem(null);
        add(lblZnackaIna);
        add(txtZnackaIna, "wrap");
    }

    // Pridá značky áut do comboBoxu. [DONE]
    private void pridajZnackyAutGUI() throws FileNotFoundException {
        ArrayList<String> zoznamAut = nacitajZnackyAutGUI();

        for (int i = 0; i < zoznamAut.size(); i++) {
            comboZnacka.addItem(zoznamAut.get(i));
        }
    }

    // Načíta značky áut z txt súboru. [DONE]
    private ArrayList<String> nacitajZnackyAutGUI() {
        ArrayList<String> zoznam = new ArrayList<>();
        Scanner scanner = null;

        try {
            scanner = new Scanner(new File("znackyAut.txt"), "UTF-8");
            while (scanner.hasNextLine()) {
                zoznam.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Nenašiel sa súbor so značkami áut.");
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return zoznam;
    }

    // Akcia pre uloženie auta. [DONE]
    private void btnUlozitActionPerformed(ActionEvent e) {
        // Ak pridávame auto po prvýkrát.
        if (auto.getSpz() == null) {
            auto = new Auto();
            auto.setZnacka((String) comboZnacka.getSelectedItem());
            auto.setModel(txtModel.getText());
            auto.setSpz(txtECV.getText());
            auto.setRok_vyr(txtRokVyroby.getText());
            auto.setStav_tach(Integer.parseInt(txtStavTachometra.getText()));
            auto.setVykon(Double.parseDouble(txtVykon.getText()));
            auto.setSpotreba_mesto(Integer.parseInt(txtSpotrebaMesto.getText()));
            auto.setSpotreba_mimo(Double.parseDouble(txtSpotrebaMimo.getText()));
            auto.setSpotreba_avg(Double.parseDouble(txtSpotrebaKomb.getText()));
            auto.setPalivo((String) comboPalivo.getSelectedItem());
            auto.setPrevodovka((String) comboPrevodovka.getSelectedItem());
            auto.setKlima((String) comboKlimatizacia.getSelectedItem());
            auto.setFarba((String) comboFarba.getSelectedItem());

            autoDao.saveAuto(login, auto);
            dispose();
        } else {
            // Ak auto len upravujeme.
            auto.setZnacka((String) comboZnacka.getSelectedItem());
            auto.setModel(txtModel.getText());
            auto.setSpz(txtECV.getText());
            auto.setRok_vyr(txtRokVyroby.getText());
            auto.setStav_tach(Integer.parseInt(txtStavTachometra.getText()));
            auto.setVykon(Double.parseDouble(txtVykon.getText()));
            auto.setSpotreba_mesto(Integer.parseInt(txtSpotrebaMesto.getText()));
            auto.setSpotreba_mimo(Double.parseDouble(txtSpotrebaMimo.getText()));
            auto.setSpotreba_avg(Double.parseDouble(txtSpotrebaKomb.getText()));
            auto.setPalivo((String) comboPalivo.getSelectedItem());
            auto.setPrevodovka((String) comboPrevodovka.getSelectedItem());
            auto.setKlima((String) comboKlimatizacia.getSelectedItem());
            auto.setFarba((String) comboFarba.getSelectedItem());

            autoDao.saveAuto(login, auto);
            dispose();
        }

    }
    // Main - PridatAutoForm

    public static void main(String args[]) throws UnsupportedLookAndFeelException, HeadlessException, FileNotFoundException {
        UIManager.setLookAndFeel(new WindowsLookAndFeel());

        PridatAutoForm pridatAutoForm = new PridatAutoForm(new javax.swing.JFrame(), true);
        pridatAutoForm.setVisible(true);
        pridatAutoForm.setTitle("Kniha jázd - pridanie vozidla");
        pridatAutoForm.setLocationRelativeTo(CENTER_SCREEN);
    }
}
