package sk.upjs.ics.GUI;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import net.miginfocom.swing.MigLayout;
import sk.upjs.ics.cestak.DaoFactory;
import sk.upjs.ics.cestak.dao.VydavokDAO;
import sk.upjs.ics.cestak.entity.Login;
import sk.upjs.ics.cestak.entity.Vydavok;

public class PridatVydavokForm extends JDialog {

    private static final Component CENTER_SCREEN = null;

    // Tlačidlá
    private JButton btnUlozit = new JButton("Uložiť");
    private JButton btnZrusit = new JButton("Zrušiť");

    // Labely
    private JLabel lblKategoria = new JLabel("Kategória:");
    private JLabel lblSuma = new JLabel("Suma:");

    // Textové polia
    private JTextField txtKategoria = new JTextField();
    private JTextField txtSuma = new JTextField();

    private Login login;
    private String autoSPZ;
    private Vydavok vydavok;

    private VydavokDAO vydavokDAO = DaoFactory.INSTANCE.vydavokDAO();

    // ************************* Konštruktory *********************************
    public PridatVydavokForm(Login login, String autoSPZ, Frame parent) {
        this(parent, true);
        this.autoSPZ = autoSPZ;
        this.login = login;
    }

    private PridatVydavokForm(Frame parent) {
        this(new Login(), new String(), parent);
    }

    // Konštruktor pre dolovanie dát do editovacieho okna. [DONE]
    public PridatVydavokForm(Vydavok vydavok, Frame parent) {
        this(parent, true);
        this.vydavok = vydavok;
        DecimalFormat df = new DecimalFormat("#.##");

        txtKategoria.setText(vydavok.getKategoria());
        txtSuma.setText(df.format(vydavok.getSuma()));
    }

    public PridatVydavokForm(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setLayout(new MigLayout("", "[fill][fill, grow]", "[][][nogrid]"));

        add(lblKategoria);
        add(txtKategoria, "wrap");

        add(lblSuma);
        add(txtSuma);

        /* ******************** AKCIE ************************ */
        add(btnUlozit, "tag ok");
        // Akcia pre stlačenie tlačidla uložiť
        btnUlozit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ulozit
                btnUlozitActionPerformed(e);
            }
        });

        add(btnZrusit, "tag cancel");
        // Akcia pre stlačenie tlačidla zrušiť.
        btnZrusit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        /* ******************** AKCIE ************************ */

        // Nastavenia
        setPreferredSize(new Dimension(500, 285));
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }

    // Uloží nový výdavok / resp. aktualizovaný. [DONE]
    private void btnUlozitActionPerformed(ActionEvent e) {
        // Ak pridávame výdavok po prvýkrát.
        if (vydavok == null) {
            vydavok = new Vydavok();
            vydavok.setAutoSPZ(autoSPZ);
            vydavok.setKategoria(txtKategoria.getText());
            vydavok.setSuma(Double.parseDouble(txtSuma.getText()));

            vydavokDAO.saveVydavok(vydavok);

            dispose();
        } else {
            // Ak výdavok len upravujeme.
            vydavok.setAutoSPZ(autoSPZ);
            vydavok.setKategoria(txtKategoria.getText());
            vydavok.setSuma(Double.parseDouble(txtSuma.getText()));

            vydavokDAO.saveVydavok(vydavok);

            dispose();
        }
    }

    // Main - PridatCestuForm
    public static void main(String arg[]) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new WindowsLookAndFeel());

        PridatVydavokForm pridatVydavokForm = new PridatVydavokForm(new javax.swing.JFrame(), true);
        pridatVydavokForm.setVisible(true);
        pridatVydavokForm.setTitle("Kniha jázd - pridanie nového výdavku");
        pridatVydavokForm.setLocationRelativeTo(CENTER_SCREEN);
    }

}
