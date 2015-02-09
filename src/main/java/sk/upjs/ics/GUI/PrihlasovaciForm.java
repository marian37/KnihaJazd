
package sk.upjs.ics.GUI;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import net.miginfocom.swing.MigLayout;
import sk.upjs.ics.cestak.DaoFactory;
import sk.upjs.ics.cestak.entity.Login;
import sk.upjs.ics.cestak.dao.PrihlasenieDAO;

/**
 * Prihlasovacia obrazovka (Login screen) Beta verzia
 *
 * @author Matej Perejda
 */
public class PrihlasovaciForm extends JFrame {

    private static final Component CENTER_SCREEN = null;

    // Tlačidlá
    private JButton btnPrihlasit = new JButton("Prihlásiť");
    private JButton btnRegistrovat = new JButton("Registrovať");

    // Textové polia 
    private JTextField txtLogin = new JTextField();
    private JPasswordField txtHeslo = new JPasswordField();

    // Labely
    private JLabel lblLogin = new JLabel("Login:");
    private JLabel lblHeslo = new JLabel("Heslo:");
    private JLabel lblWarning = new JLabel("Zlé meno alebo heslo!", SwingConstants.RIGHT);

    private PrihlasenieDAO prihlasenieDao = DaoFactory.INSTANCE.prihlasenieDao();
    private Login login;

    public void nacitajBanner() throws IOException {
        try {
            BufferedImage myPicture = ImageIO.read(new File("banner.png"));

            JLabel picLabel = new JLabel(new ImageIcon(myPicture), SwingConstants.CENTER);
            add(picLabel, "wrap, span 2");
        } catch (FileNotFoundException e) {
            System.out.println("Banner sa nepodarilo načítať.");
        }
    }

    // Konštruktor
    public PrihlasovaciForm() throws IOException {
        setLayout(new MigLayout("", "[fill, grow][fill, grow]", "[][][][]"));

        nacitajBanner();
        add(lblLogin);
        add(txtLogin, "wrap");
        add(lblHeslo);
        add(txtHeslo, "wrap");
        add(lblWarning, "wrap, span 2");
        lblWarning.setVisible(false); // Nastaviť na TRUE, ak zadané zlé meno alebo heslo.
        lblWarning.setForeground(Color.RED);

        // Tlačidlo PRIHLÁSIŤ.
        add(btnPrihlasit);
        getRootPane().setDefaultButton(btnPrihlasit);
        // Akcia pre stlačenie tlačídla prihlásiť.
        btnPrihlasit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    btnPrihlasitActionPerformed(e);
                } catch (IOException ex) {
                    Logger.getLogger(PrihlasovaciForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Prihlasujem...");
            }
        });

        // Tlačídlo REGISTROVAŤ.
        add(btnRegistrovat);
        // Akcia pre stlačenie tlačídla registrovať.
        btnRegistrovat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnRegistrovatActionPerformed(e);
                System.out.println("Registrujem...");
            }
        });
        /* ******************** AKCIE ************************ */

        ImageIcon img = new ImageIcon("75x54.png");
        setIconImage(img.getImage());
        //setPreferredSize(new Dimension(350, 150));
        setPreferredSize(new Dimension(400, 320));
        setResizable(false);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }

    // Akcia pre prihlásenie.
    private void btnPrihlasitActionPerformed(ActionEvent event) throws IOException {
        login = new Login();
        login.setLogin(txtLogin.getText());
        login.setHeslo(String.valueOf(txtHeslo.getPassword()));
        login = prihlasenieDao.verifyLogin(login);
        if (login != null) {
            dispose();
            System.out.println(login.getId());
            MainForm mainForm = new MainForm(login);
            mainForm.setTitle("Kniha jázd - hlavné okno. Používateľ: " + login.getLogin().toString().toUpperCase());
            mainForm.setLocationRelativeTo(CENTER_SCREEN);
            mainForm.setVisible(true);
        } else {
            lblWarning.setVisible(true);
        }
    }

    // Akcia pre registrovanie.
    private void btnRegistrovatActionPerformed(ActionEvent event) {
        RegistracnyForm registrujPouzivatela = new RegistracnyForm(this, true);
        registrujPouzivatela.setTitle("Kniha jázd - registrácia užívateľa");
        registrujPouzivatela.setLocationRelativeTo(CENTER_SCREEN);
        registrujPouzivatela.setVisible(true);
    }

    // Main - PrihlasovaciForm
    public static void main(String args[]) throws UnsupportedLookAndFeelException, IOException {
        LookAndFeel lookAndFeel = new WindowsLookAndFeel();
        if (lookAndFeel.isSupportedLookAndFeel()) {
            UIManager.setLookAndFeel(lookAndFeel);
        }

        PrihlasovaciForm prihlasovaciForm = new PrihlasovaciForm();
        prihlasovaciForm.setVisible(true);
        prihlasovaciForm.setTitle("Kniha jázd - prihlásenie");
        prihlasovaciForm.setLocationRelativeTo(CENTER_SCREEN);
    }
}
