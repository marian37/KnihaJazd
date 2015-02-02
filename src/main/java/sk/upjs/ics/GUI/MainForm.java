package sk.upjs.ics.GUI;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.miginfocom.swing.MigLayout;
import sk.upjs.ics.cestak.Auto;
import sk.upjs.ics.cestak.AutoDAO;
import sk.upjs.ics.cestak.AutoListCellRenderer;
import sk.upjs.ics.cestak.DaoFactory;
import sk.upjs.ics.cestak.Jazda;
import sk.upjs.ics.cestak.JazdaDAO;
import sk.upjs.ics.cestak.JazdaTableModel;
import sk.upjs.ics.cestak.Login;

/**
 * MainForm
 *
 * @author Matej Perejda
 */
public class MainForm extends JFrame {

    private static final Component CENTER_SCREEN = null;

    // Tlačidlá
    private JButton btnNovaCesta = new JButton("Nová cesta");
    private JButton btnVymazatCestu = new JButton("Vymazať cestu");
    private JButton btnPridatAuto = new JButton("Pridať auto");
    private JButton btnUpravitAuto = new JButton("Upraviť auto");
    private JButton btnVymazatAuto = new JButton("Vymazať auto");
    private JButton btnUpravitUzivatela = new JButton("Upraviť profil");
    private JButton btnOdhlasit = new JButton("Odhlásiť");

    // Comboboxy
    private JComboBox comboAuta = new JComboBox();

    // Labely
    private JLabel lblJazdy = new JLabel("Aktuálny zoznam jázd:");

    // Tabulky
    private JTable tabJazdy;

    // JPanel
    private JPanel panPanel = new JPanel();
    private JPanel panPanel2 = new JPanel();
    private JPanel panPanel3 = new JPanel();
    private JPanel vacsiComboBox = new JPanel();

    private Login login;
    private Auto selectedAuto;

    private AutoDAO autoDao = DaoFactory.INSTANCE.autoDao();
    private JazdaDAO jazdaDao = DaoFactory.INSTANCE.jazdaDao();

    private ListCellRenderer autoListCellRenderer = new AutoListCellRenderer();
    private JazdaTableModel jazdaTableModel = new JazdaTableModel();

    // ***********************************************************************
    public void nacitajIcon() throws IOException {
        try {
            BufferedImage myPicture = ImageIO.read(new File("75x54.png"));

            JLabel picLabel = new JLabel(new ImageIcon(myPicture), SwingConstants.CENTER);
            add(picLabel);
        } catch (FileNotFoundException e) {
            System.out.println("Banner sa nepodarilo načítať.");
        }
    }

    public MainForm(Login login) throws IOException {
        this();
        this.login = login;
        obnovAuta();
        obnovJazdy();
    }

    public MainForm() throws IOException {
        setLayout(new MigLayout("", "[fill, grow][fill][fill][fill][fill][fill]", "[][][][][][][nogrid]"));

        // Vrchny panel s tlacidlami
        add(btnNovaCesta);
        btnNovaCesta.setFont(new Font(null, Font.BOLD, 11));
        add(btnVymazatCestu);
        add(btnPridatAuto);
        add(btnUpravitAuto);
        add(btnVymazatAuto);
        add(btnUpravitUzivatela, "wrap");

        // Prázdny panel 1
        panPanel.setPreferredSize(new Dimension(700, 30));
        add(panPanel, "wrap");

        // Label a combo
        //add(lblJazdy);
        nacitajIcon();

        comboAuta.setFont(new Font("Dialog", Font.BOLD, 15));
        add(vacsiComboBox.add(comboAuta), "grow, wrap, span 5");

        //add(comboAuta, "wrap, span 5");
        // Prazdny panel 2
        panPanel2.setPreferredSize(new Dimension(700, 10));
        add(panPanel2, "wrap");

        // Nastavnie JTable       
        setJTable();

        // Prázdny panel 3
        panPanel3.setPreferredSize(new Dimension(700, 10));
        add(panPanel3, "wrap");

        btnVymazatCestu.setEnabled(false);
        btnUpravitUzivatela.setEnabled(true);
        add(btnOdhlasit, "tag cancel, span 1");
        // ////////////////////////////////////////////////////////////////////

        // Tlačídlo NOVÁ CESTA. [DONE]
        btnNovaCesta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnNovaCestaActionPerformed(e);
            }
        });

        // Tlačidlo VYMAZAŤ CESTU. [DONE]
        btnVymazatCestu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnVymazatCestuActionPerformed(e);
            }
        });

        // Tlačidlo PRIDAŤ AUTO. [DONE]
        btnPridatAuto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnPridatAutoActionPerformed(e);
            }
        });

        // Combobox pre autá.
        comboAuta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedAuto = (Auto) comboAuta.getSelectedItem();
                obnovJazdy();
            }
        });

        // Tlačidlo UPRAVIŤ AUTO. [DONE]
        btnUpravitAuto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    btnUpravitAutoActionPerformed(e);
                } catch (HeadlessException ex) {
                    System.out.println(ex);
                } catch (FileNotFoundException ex) {
                    System.out.println("Súbor sa nenašiel.");
                }
            }
        });

        // Tlačídlo VYMAZAŤ AUTO. [DONE]
        btnVymazatAuto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnVymazatAutoActionPerformed(e);
            }
        });

        // Tlačidlo UPRAVIŤ PROFIL. 
        btnUpravitUzivatela.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnUpravitUzivatelaActionPerformed(e);
            }
        });

        // Tlačidlo ODHLÁSIŤ. [DONE]
        btnOdhlasit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    btnOdhlasitActionPerformed(e);
                } catch (IOException ex) {
                    Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        tabJazdy.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                tabJazdySelectionValueChanged(e);
            }
        });

        // ////////////////////////////////////////////////////////////////////
        ImageIcon img = new ImageIcon("75x54.png");
        setIconImage(img.getImage());
        
        setPreferredSize(new Dimension(700, 500));
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }

    // Akcia tlačidla pre pridanie nového auta.  [DONE]
    private void btnPridatAutoActionPerformed(ActionEvent e) {

        PridatAutoForm pridatAutoForm = null;
        pridatAutoForm = new PridatAutoForm(login, new Auto(), this);
        pridatAutoForm.setTitle("Kniha jázd - pridanie vozidla");
        pridatAutoForm.setLocationRelativeTo(CENTER_SCREEN);
        pridatAutoForm.setVisible(true);

        obnovAuta();
        obnovJazdy();
    }

    // Akcie tlačidla pre úpravu uloženého auta. [DONE]
    private void btnUpravitAutoActionPerformed(ActionEvent e) throws HeadlessException, FileNotFoundException {

        PridatAutoForm editableAuto = new PridatAutoForm(selectedAuto, this);
        editableAuto.setLocationRelativeTo(CENTER_SCREEN);
        editableAuto.setTitle("Kniha jázd - úprava vozidla");
        editableAuto.setVisible(true);

        obnovAuta();
        obnovJazdy();
    }

    // Akcia tlačidla pre vymazanie vyznačeného auta. [DONE]
    private void btnVymazatAutoActionPerformed(ActionEvent e) {
        Object[] options = {"Áno", "Nie"};
        int n = JOptionPane.showOptionDialog(this, "Skutočne chcete vymazať auto [" + selectedAuto.getSpz() + "] " + selectedAuto.getZnacka() + " " + selectedAuto.getModel(), "Vymazanie auta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        // Ak užívateľ vyberie "ÁNO".
        if (n == 0) {
            autoDao.vymazAuto(selectedAuto);
            obnovAuta();
            obnovJazdy();
        }
    }

    // Akcia tlačidla pre pridanie novej cesty. [DONE]
    private void btnNovaCestaActionPerformed(ActionEvent e) {
        PridatCestuForm pridatCestuForm = new PridatCestuForm(login, selectedAuto, this);
        pridatCestuForm.setTitle("Kniha jázd - pridanie nového záznamu");
        pridatCestuForm.setLocationRelativeTo(CENTER_SCREEN);

        pridatCestuForm.setVisible(true);
        obnovJazdy();
    }

    // Akcie tlačidla pre vymazanie vyznačenej cesty. [DONE]
    private void btnVymazatCestuActionPerformed(ActionEvent e) {
        int vybranyRiadok = tabJazdy.getSelectedRow();
        int vybratyIndexVModeli = tabJazdy.convertRowIndexToModel(vybranyRiadok);
        Jazda jazda = jazdaTableModel.dajPodlaCislaRiadku(vybratyIndexVModeli);

        Object[] options = {"Áno", "Nie"};
        int n = JOptionPane.showOptionDialog(this, "Skutočne chcete vymazať vyznačenú jazdu ?", "Vymazanie jazdy", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]
        );

        if (n == 0) {
            jazdaDao.vymazJazda(jazda);
            obnovJazdy();
        }
    }

    private void btnUpravitUzivatelaActionPerformed(ActionEvent e) {
        RegistracnyForm editableLogin = new RegistracnyForm(login, this); // matej
        // IN PROGRESS..

        editableLogin.setLocationRelativeTo(CENTER_SCREEN);
        editableLogin.setTitle("Kniha jázd - úprava profilu");
        editableLogin.setVisible(true);
    }

    // Akcia tlačidla pre odhlásenie. [DONE]
    private void btnOdhlasitActionPerformed(ActionEvent e) throws IOException {
        Object[] options = {"Áno", "Nie"};
        int n = JOptionPane.showOptionDialog(this, "Skutočne sa chcete odhlásiť ?", "Odhlásenie používateľa '" + login.getLogin() + "'", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        // Ak užívateľ vyberie "ÁNO".
        if (n == 0) {
            dispose();
            PrihlasovaciForm prihlasovaciForm = new PrihlasovaciForm();
            prihlasovaciForm.setTitle("Kniha jázd - prihlásenie");
            prihlasovaciForm.setLocationRelativeTo(CENTER_SCREEN);
            prihlasovaciForm.setVisible(true);
        }
    }

    // Ošetrenia pre tabuľku, deaktivácia tlačidiel. [DONE]
    private void tabJazdySelectionValueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            if (!tabJazdy.getSelectionModel().isSelectionEmpty()) {
                btnVymazatCestu.setEnabled(true);
            } else {
                btnVymazatCestu.setEnabled(false);
            }
        }
    }

    // Všetky nastavenia tabuľky - ÚPRAVA CESTY. [DONE]
    public void setJTable() {
        tabJazdy = new JTable();
        tabJazdy.setModel(jazdaTableModel);
        tabJazdy.setPreferredScrollableViewportSize(new Dimension(700, 320));
        tabJazdy.setFillsViewportHeight(true);
        tabJazdy.setAutoCreateRowSorter(true);
        add(tabJazdy, "wrap, span 6");

        JScrollPane scrollPane = new JScrollPane(tabJazdy);
        add(scrollPane, "wrap, span 6");

        // Klikanie na polozky v tabuľke + editácia vybratej položky.      
        tabJazdy.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                JTable table = (JTable) me.getSource();

                Point bod = me.getPoint();
                //System.out.println("[" + bod.getX() + ", " + bod.getY() + "]");
                int riadok = table.rowAtPoint(bod);
                System.out.println("Riadok: " + riadok);

                // Ak spravíme dvojklik ľavým tlačídlom a zároveň sme s kurzorom na nejakom riadku.
                if (me.getClickCount() == 2 && riadok != -1 && SwingUtilities.isLeftMouseButton(me)) {

                    // Uložená vyznačená jazda v tabuľke na ktorú aktuálne dvojklikáme.
                    Jazda vybranaJazda = (Jazda) jazdaTableModel.dajPodlaCislaRiadku(riadok);

                    // Otvoríme editovacie okno a predvyplníme vydolované dáta.
                    PridatCestuForm editableCesta = new PridatCestuForm(vybranaJazda, null);
                    editableCesta.setLocationRelativeTo(CENTER_SCREEN);
                    editableCesta.setTitle("Kniha jázd - úprava cesty");
                    editableCesta.setVisible(true);

                    obnovJazdy();
                } else if (riadok == -1 && SwingUtilities.isLeftMouseButton(me)) {
                    // V pripade, že klikneme mimo tabuľky, aktuálne vybratý riadok sa odznačí.
                    tabJazdy.clearSelection();
                }
            }
        });
    }

    // Uchováva info o aktuálne vyzerajúcom comboboxe. [DONE]
    private ComboBoxModel getAutaModel() {
        List<Auto> auto = autoDao.zoznamPodlaPouzivatela(login);
        ((JLabel) comboAuta.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        // Ak combobox neuchováva žiadne položky.
        if (!auto.isEmpty()) {
            //System.out.println("Načítaný zoznam aut nie je prazdny."); // Matej           
            System.out.println("SelectedAuto v comboBoxe:" + auto.get(0).getSpz()); // Matej

            // TU BY BOLO DOBRE NEJAKÉ OŠETRENIE, LEBO SELECTEDAUTO JE VŽDY NASTAVENÉ ROVNAKO 
            // SPôSOBUJE TO PROBLÉMY: AK PRIDÁME NOVÉ AUTO DO ZOZNAMU, COMBOBOX SA ZMENÍ NA 
            // PRVÉ AUTO V ZOZNAME, A ZOZNAM SA NEAKTUALIZUJE
            selectedAuto = auto.get(0);
        }
        return new DefaultComboBoxModel(auto.toArray());
    }

    // Obnovenie áut v comboboxe. [DONE]
    public void obnovAuta() {
        comboAuta.setModel(getAutaModel());

        // Ak combobox nie je prázdny.
        if (getAutaModel().getSize() != 0) {
            comboAuta.setRenderer(autoListCellRenderer);
            btnUpravitAuto.setEnabled(true);
            btnVymazatAuto.setEnabled(true);
            btnNovaCesta.setEnabled(true);
        } else {
            // Ak combobox je prázdny.
            btnUpravitAuto.setEnabled(false);
            btnVymazatAuto.setEnabled(false);
            btnNovaCesta.setEnabled(false);
        }

    }

    // Obnovenie jázd v JTable. [DONE]
    private void obnovJazdy() {
        // Ak sa v comboboxe nachádzajú nejaké autá.
        if (getAutaModel().getSize() != 0) {
            selectedAuto = (Auto) comboAuta.getSelectedItem(); // TOTO ZABEZPEČI, KOMPLET REFRESH

            jazdaTableModel.obnov(selectedAuto, login);
            System.out.println("\nRok výroby: " + selectedAuto.getRok_vyr() + "\nStav tachometra: " + selectedAuto.getStav_tach() + "\nSptreba avg: " + selectedAuto.getSpotreba_avg() + "\nID používateľa: " + selectedAuto.getIdPouzivatel());
            System.out.println("Aktuálne selected auto: " + selectedAuto.getSpz() + " " + selectedAuto.getZnacka() + " " + selectedAuto.getModel() + "\n");
            jazdaTableModel.fireTableDataChanged(); // Matej
        }
    }

    // Main - MainForm
    public static void main(String args[]) throws UnsupportedLookAndFeelException, IOException {
        UIManager.setLookAndFeel(new WindowsLookAndFeel());

        MainForm mainForm = new MainForm();
        mainForm.setVisible(true);
        mainForm.setTitle("Kniha jázd - hlavné okno");
        mainForm.setLocationRelativeTo(CENTER_SCREEN);
    }
}
