package sk.upjs.ics.GUI;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import net.miginfocom.swing.MigLayout;
import sk.upjs.ics.cestak.DaoFactory;
import sk.upjs.ics.cestak.Login;
import sk.upjs.ics.cestak.Pouzivatel;
import sk.upjs.ics.cestak.PrihlasenieDAO;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * Registračné okno (Register screen) Beta verzia
 *
 * @author Matej Perejda
 */
public class RegistracnyForm extends JDialog {

    private static final Component CENTER_SCREEN = null;

    // Tlačidlá
    private JButton btnRegistrovat = new JButton("Registrovať");
    private JButton btnZrusit = new JButton("Zrušiť");

    // Labely    
    private JLabel lblLogin = new JLabel("Login:*");
    private JLabel lblHeslo = new JLabel("Heslo:*");
    private JLabel lblHeslo2 = new JLabel("Heslo:*"); // Zopakujte heslo hint
    private JLabel lblMeno = new JLabel("Meno:*");
    private JLabel lblPriezvisko = new JLabel("Priezvisko:*");
    private JLabel lblPohlavie = new JLabel("Pohlavie:*");
    private JLabel lblDatumNarodenia = new JLabel("Dátum narodenia:*");
    private JLabel lblAdresa = new JLabel("Adresa:*");
    private JLabel lblEmail = new JLabel("E-mail:*");
    private JLabel lblTel = new JLabel("Tel.:*");

    // Textove polia
    private JTextField txtLogin = new JTextField();
    private JPasswordField txtHeslo = new JPasswordField();
    private JPasswordField txtHeslo2 = new JPasswordField();
    private JTextField txtMeno = new JTextField();
    private JTextField txtPriezvisko = new JTextField();
    private JTextField txtAdresa = new JTextField();
    private JTextField txtEmail = new JTextField();
    private JTextField txtTel = new JTextField();

    // Combobox
    private JComboBox comboPohlavie = new JComboBox();
    private JComboBox comboDen = new JComboBox();
    private JComboBox comboMesiac = new JComboBox();
    private JComboBox comboRok = new JComboBox();

    private PrihlasenieDAO prihlasenieDao = DaoFactory.INSTANCE.prihlasenieDao();

    private Pouzivatel pouzivatel;
    private Login login;

    // *********************************************************************//
    // Konštruktor slúžiaci na editovanie profilu užívateľa.
    public RegistracnyForm(Login login, Frame parent) {
        this(parent, true);
        this.login = login;

        Pouzivatel pouzivatel2 = prihlasenieDao.getPouzivatelInfo(login);

        txtLogin.setText(login.getLogin());
        txtLogin.setEditable(false);

        txtHeslo.setText(login.getHeslo());
        txtHeslo.setEditable(false);

        txtHeslo2.setText(login.getHeslo());
        txtHeslo2.setEditable(false);

        txtMeno.setText(pouzivatel2.getMeno());
        txtPriezvisko.setText(pouzivatel2.getPriezvisko());
        comboPohlavie.setSelectedItem(pouzivatel2.getPohlavie());

        // Rozdelenie dátumu narodenia po vydolovaní z SQL podľa '-'
        String datumNarodenia = pouzivatel2.getDatum();
        String[] datumRozdeleny = datumNarodenia.split("-");

        int datumRok = Integer.parseInt(datumRozdeleny[0]);
        int datumMesiac = Integer.parseInt(datumRozdeleny[1]);
        int datumDen = Integer.parseInt(datumRozdeleny[2]);

        comboRok.setSelectedItem(datumRok);
        comboMesiac.setSelectedItem(datumMesiac);
        comboDen.setSelectedItem(datumDen);

        txtAdresa.setText(pouzivatel2.getAdresa());
        txtEmail.setText(pouzivatel2.getEmail());
        txtTel.setText(pouzivatel2.getTel());
    }

    public RegistracnyForm(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setLayout(new MigLayout("", "[fill, grow][fill,grow][][]", "[][][][][][][nogrid][][][][nogrid]"));
        nastavPrihlasovacieUdajeGUI();
        nastavOsobneUdajeGUI();
        nastavDatumNarodeniaGUI();
        nastavKontaktInfoGUI();

        // Tlačidlo REGISTROVAŤ.
        add(btnRegistrovat, "tag ok");
        // Akcia pre stlačenie tlačidla registrovať.
        btnRegistrovat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnRegistrovatActionPerformed(e);
                System.out.println("Registrujem...");
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

        setPreferredSize(new Dimension(313, 310));
        setResizable(false);
        pack();
    }

    // Nastavenie pre kontaktné údaje.
    private void nastavKontaktInfoGUI() {
        add(lblAdresa);
        add(txtAdresa, "wrap, span 3");
        txtAdresa.setToolTipText("ulica/PSČ/mesto/krajina");
        add(lblEmail);
        add(txtEmail, "wrap, span 3");
        txtEmail.setToolTipText("mailto@server.com");
        add(lblTel);
        add(txtTel, "wrap, span 3");
    }

    // Nastavenie pre dátum narodenia.
    private void nastavDatumNarodeniaGUI() {
        add(lblDatumNarodenia);

        // Combobox - deň
        add(comboDen);
        comboDen.setToolTipText("Deň");
        comboDen.setMaximumSize(new Dimension(40, 30));
        generujDen();
        comboDen.setSelectedItem(null);

        // Combobox - mesiac
        add(comboMesiac);
        comboMesiac.setToolTipText("Mesiac");
        comboMesiac.setMaximumSize(new Dimension(40, 30));
        generujMesiac();
        comboMesiac.setSelectedItem(null);

        // Combobox - rok
        add(comboRok, "wrap");
        comboRok.setToolTipText("Rok");
        comboRok.setMaximumSize(new Dimension(55, 30));
        generujRok();
        comboRok.setSelectedItem(null);
    }

    // Nastavenie pre osobne údaje.
    private void nastavOsobneUdajeGUI() {
        add(lblMeno);
        add(txtMeno, "wrap, span 3");
        add(lblPriezvisko);
        add(txtPriezvisko, "wrap, span 3");
        add(lblPohlavie);
        add(comboPohlavie, "wrap, span 3");
        comboPohlavie.addItem("muž");
        comboPohlavie.addItem("žena");
        comboPohlavie.setSelectedItem(null);
    }

    // Nastavenie pre login a heslo.
    private void nastavPrihlasovacieUdajeGUI() {
        add(lblLogin);
        add(txtLogin, "wrap, span 3");
        txtLogin.setToolTipText("Prihlasovacie meno");
        add(lblHeslo);
        add(txtHeslo, "wrap, span 3");
        txtHeslo.setToolTipText("Zadajte heslo");
        add(lblHeslo2);
        add(txtHeslo2, "wrap, span 3");
        txtHeslo2.setToolTipText("Zopakujte heslo");
    }

    // Akcia pre registráciu - Povodna verzia bez UPDATE
    /*private void btnRegistrovatActionPerformed2(ActionEvent e) {
     if (txtLogin.getText().isEmpty()) {
     JOptionPane.showMessageDialog(this, "Zadajte login (povinný údaj)!", "Login", JOptionPane.ERROR_MESSAGE);
     return;
     } else if (txtHeslo.getPassword().length == 0) {
     JOptionPane.showMessageDialog(this, "Zadajte heslo (povinný údaj)!", "Heslo", JOptionPane.ERROR_MESSAGE);
     return;
     } else if (txtHeslo2.getPassword().length == 0) {
     JOptionPane.showMessageDialog(this, "Zapakujte heslo (povinný údaj)!", "Heslo", JOptionPane.ERROR_MESSAGE);
     return;
     } else if (txtMeno.getText().isEmpty()) {
     JOptionPane.showMessageDialog(this, "Zadajte meno (povinný údaj)!", "Meno", JOptionPane.ERROR_MESSAGE);
     return;
     } else if (txtPriezvisko.getText().isEmpty()) {
     JOptionPane.showMessageDialog(this, "Zadajte priezvisko (povinný údaj)!", "Priezvisko", JOptionPane.ERROR_MESSAGE);
     return;
     } else if (comboPohlavie.getSelectedItem() == null) {
     JOptionPane.showMessageDialog(this, "Zadajte pohlavie (povinný údaj)!", "Pohlavie", JOptionPane.ERROR_MESSAGE);
     return;
     } else if (comboDen.getSelectedItem() == null || comboMesiac.getSelectedItem() == null || comboRok.getSelectedItem() == null) {
     JOptionPane.showMessageDialog(this, "Zadajte dátum narodenia (povinný údaj)!", "Dát.narodenia", JOptionPane.ERROR_MESSAGE);
     return;
     } else if (txtAdresa.getText().isEmpty()) {
     JOptionPane.showMessageDialog(this, "Zadajte adresu (povinný údaj)!", "Adresa", JOptionPane.ERROR_MESSAGE);
     return;
     } else if (txtEmail.getText().isEmpty()) {
     JOptionPane.showMessageDialog(this, "Zadajte e-mail (povinný údaj)!", "E-mail", JOptionPane.ERROR_MESSAGE);
     return;
     } else if (txtTel.getText().isEmpty()) {
     JOptionPane.showMessageDialog(this, "Zadajte tel.číslo (povinný údaj)!", "Tel.číslo", JOptionPane.ERROR_MESSAGE);
     return;
     }

     // Ak sa heslá pri registrácii nezhodujú.
     if (!String.valueOf(txtHeslo2.getPassword()).equals(String.valueOf(txtHeslo.getPassword()))) {
     JOptionPane.showMessageDialog(this, "Zadané heslá sa nezhodujú!", "Upozornenie", JOptionPane.ERROR_MESSAGE);
     return;
     }

     pouzivatel = new Pouzivatel();
     pouzivatel.setMeno(txtMeno.getText());
     pouzivatel.setPriezvisko(txtPriezvisko.getText());
     pouzivatel.setAdresa(txtAdresa.getText());
     pouzivatel.setPohlavie((String) comboPohlavie.getSelectedItem());

     // Namiesto StringBuildera a append použité Date, Calendar, DateFormat
     DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
     Calendar calendar = Calendar.getInstance();

     int rok = Integer.parseInt(comboRok.getSelectedItem().toString());
     int mesiac = Integer.parseInt(comboMesiac.getSelectedItem().toString());
     int den = Integer.parseInt(comboDen.getSelectedItem().toString());

     calendar.set(Calendar.YEAR, rok);
     calendar.set(Calendar.MONTH, mesiac - 1); // Mesiace sú číslované od 0..11
     calendar.set(Calendar.DAY_OF_MONTH, den);

     Date date = calendar.getTime();
     String datum = dateFormat.format(date);

     pouzivatel.setDatum(datum);
     pouzivatel.setEmail(txtEmail.getText());
     pouzivatel.setTel(txtTel.getText());

     login = new Login();
     login.setLogin(txtLogin.getText());
     login.setHeslo(String.valueOf(txtHeslo2.getPassword()));

     // Ošetrenie, ak je login už obsadený.
     if (prihlasenieDao.verifyOnlyLogin(login)) {
     JOptionPane.showMessageDialog(this, "Login '" + login.getLogin().toString() + "' je už obsadený!", "Upozornenie", JOptionPane.ERROR_MESSAGE);
     return;
     }

     prihlasenieDao.savePouzivatela(pouzivatel);
     login.setId(pouzivatel.getId());
     prihlasenieDao.saveLogin(login);
     JOptionPane.showMessageDialog(this, "Registrácia prebehla úspešne. Teraz sa môžete prihlásiť.", "Úspešná registrácia", JOptionPane.INFORMATION_MESSAGE);
     dispose();
     }*/
    
    // Akcia pre registráciu - Nová verzia s UPDATE
    private void btnRegistrovatActionPerformed(ActionEvent e) {
        if (login == null) {
            if (txtLogin.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Zadajte login (povinný údaj)!", "Login", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (txtHeslo.getPassword().length == 0) {
                JOptionPane.showMessageDialog(this, "Zadajte heslo (povinný údaj)!", "Heslo", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (txtHeslo2.getPassword().length == 0) {
                JOptionPane.showMessageDialog(this, "Zapakujte heslo (povinný údaj)!", "Heslo", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (txtMeno.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Zadajte meno (povinný údaj)!", "Meno", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (txtPriezvisko.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Zadajte priezvisko (povinný údaj)!", "Priezvisko", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (comboPohlavie.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Zadajte pohlavie (povinný údaj)!", "Pohlavie", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (comboDen.getSelectedItem() == null || comboMesiac.getSelectedItem() == null || comboRok.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Zadajte dátum narodenia (povinný údaj)!", "Dát.narodenia", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (txtAdresa.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Zadajte adresu (povinný údaj)!", "Adresa", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (txtEmail.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Zadajte e-mail (povinný údaj)!", "E-mail", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (txtTel.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Zadajte tel.číslo (povinný údaj)!", "Tel.číslo", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Ak sa heslá pri registrácii nezhodujú.
            if (!String.valueOf(txtHeslo2.getPassword()).equals(String.valueOf(txtHeslo.getPassword()))) {
                JOptionPane.showMessageDialog(this, "Zadané heslá sa nezhodujú!", "Upozornenie", JOptionPane.ERROR_MESSAGE);
                return;
            }

            pouzivatel = new Pouzivatel();
            pouzivatel.setMeno(txtMeno.getText());
            pouzivatel.setPriezvisko(txtPriezvisko.getText());
            pouzivatel.setAdresa(txtAdresa.getText());
            pouzivatel.setPohlavie((String) comboPohlavie.getSelectedItem());

            // Namiesto StringBuildera a append použité Date, Calendar, DateFormat
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();

            int rok = Integer.parseInt(comboRok.getSelectedItem().toString());
            int mesiac = Integer.parseInt(comboMesiac.getSelectedItem().toString());
            int den = Integer.parseInt(comboDen.getSelectedItem().toString());

            calendar.set(Calendar.YEAR, rok);
            calendar.set(Calendar.MONTH, mesiac - 1); // Mesiace sú číslované od 0..11
            calendar.set(Calendar.DAY_OF_MONTH, den);

            Date date = calendar.getTime();
            String datum = dateFormat.format(date);

            pouzivatel.setDatum(datum);
            pouzivatel.setEmail(txtEmail.getText());
            pouzivatel.setTel(txtTel.getText());

            login = new Login();
            login.setLogin(txtLogin.getText());
            login.setHeslo(String.valueOf(txtHeslo2.getPassword()));

            // Ošetrenie, ak je login už obsadený.
            if (prihlasenieDao.verifyOnlyLogin(login)) {
                JOptionPane.showMessageDialog(this, "Login '" + login.getLogin().toString() + "' je už obsadený!", "Upozornenie", JOptionPane.ERROR_MESSAGE);
                return;
            }

            prihlasenieDao.savePouzivatela(pouzivatel);
            login.setId(pouzivatel.getId());
            prihlasenieDao.saveLogin(login);
            JOptionPane.showMessageDialog(this, "Registrácia prebehla úspešne. Teraz sa môžete prihlásiť.", "Úspešná registrácia", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            pouzivatel = prihlasenieDao.getPouzivatelInfo(login);

            if (txtLogin.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Zadajte login (povinný údaj)!", "Login", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (txtHeslo.getPassword().length == 0) {
                JOptionPane.showMessageDialog(this, "Zadajte heslo (povinný údaj)!", "Heslo", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (txtHeslo2.getPassword().length == 0) {
                JOptionPane.showMessageDialog(this, "Zapakujte heslo (povinný údaj)!", "Heslo", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (txtMeno.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Zadajte meno (povinný údaj)!", "Meno", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (txtPriezvisko.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Zadajte priezvisko (povinný údaj)!", "Priezvisko", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (comboPohlavie.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Zadajte pohlavie (povinný údaj)!", "Pohlavie", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (comboDen.getSelectedItem() == null || comboMesiac.getSelectedItem() == null || comboRok.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Zadajte dátum narodenia (povinný údaj)!", "Dát.narodenia", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (txtAdresa.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Zadajte adresu (povinný údaj)!", "Adresa", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (txtEmail.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Zadajte e-mail (povinný údaj)!", "E-mail", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (txtTel.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Zadajte tel.číslo (povinný údaj)!", "Tel.číslo", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Ak sa heslá pri registrácii nezhodujú.
            if (!String.valueOf(txtHeslo2.getPassword()).equals(String.valueOf(txtHeslo.getPassword()))) {
                JOptionPane.showMessageDialog(this, "Zadané heslá sa nezhodujú!", "Upozornenie", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update
            pouzivatel.setMeno(txtMeno.getText());
            pouzivatel.setPriezvisko(txtPriezvisko.getText());
            pouzivatel.setAdresa(txtAdresa.getText());
            pouzivatel.setPohlavie((String) comboPohlavie.getSelectedItem());

            // Namiesto StringBuildera a append použité Date, Calendar, DateFormat
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();

            int rok = Integer.parseInt(comboRok.getSelectedItem().toString());
            int mesiac = Integer.parseInt(comboMesiac.getSelectedItem().toString());
            int den = Integer.parseInt(comboDen.getSelectedItem().toString());

            calendar.set(Calendar.YEAR, rok);
            calendar.set(Calendar.MONTH, mesiac - 1); // Mesiace sú číslované od 0..11
            calendar.set(Calendar.DAY_OF_MONTH, den);

            Date date = calendar.getTime();
            String datum = dateFormat.format(date);

            pouzivatel.setDatum(datum);
            pouzivatel.setEmail(txtEmail.getText());
            pouzivatel.setTel(txtTel.getText());

            // Odkomentovat, ak chceme upravit aj heslo.
            // Doplnit aj update.
            /*login.setLogin(txtLogin.getText());
             login.setHeslo(String.valueOf(txtHeslo2.getPassword()));

             // Ošetrenie, ak je login už obsadený.
             if (prihlasenieDao.verifyOnlyLogin(login)) {
             JOptionPane.showMessageDialog(this, "Login '" + login.getLogin().toString() + "' je už obsadený!", "Upozornenie", JOptionPane.ERROR_MESSAGE);
             return;
             }*/
            prihlasenieDao.savePouzivatela(pouzivatel);
            login.setId(pouzivatel.getId());

            JOptionPane.showMessageDialog(this, "Profil bol úspešne aktualizovaný.", "Aktualizácia profilu", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }

    // Generuje dni pre dátum narodenia.
    private void generujDen() {
        for (int i = 1; i <= 31; i++) {
            comboDen.addItem(new Integer(i));
        }
    }

    // Generuje mesiac pre dátum narodenia.
    private void generujMesiac() {
        for (int i = 1; i <= 12; i++) {
            comboMesiac.addItem(new Integer(i));
        }
    }

    // Generuje rok pre dátum narodenia.
    private void generujRok() {
        for (int i = 1920; i <= 2050; i++) {
            comboRok.addItem(new Integer(i));
        }
    }

    // Main - RegistracnyForm 
    public static void main(String args[]) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new WindowsLookAndFeel());

        RegistracnyForm registracnyForm = new RegistracnyForm(new javax.swing.JFrame(), true); // Matej
        registracnyForm.setVisible(true);
        registracnyForm.setTitle("Kniha jázd - registrácia užívateľa");
        registracnyForm.setLocationRelativeTo(CENTER_SCREEN);
    }
}
