package sk.upjs.ics.cestak;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import java.io.IOException;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import sk.upjs.ics.GUI.MainForm;
import sk.upjs.ics.cestak.dao.PrihlasenieDAO;
import sk.upjs.ics.cestak.entity.Login;

public class starter {

    private static PrihlasenieDAO prihlasenieDao = DaoFactory.INSTANCE.prihlasenieDao();

    public static void main(String[] args) throws IOException, UnsupportedLookAndFeelException {
        LookAndFeel lookAndFeel = new WindowsLookAndFeel();
        if (lookAndFeel.isSupportedLookAndFeel()) {
            UIManager.setLookAndFeel(lookAndFeel);
        }

        Login login = new Login();
        login.setLogin("admin");
        login.setHeslo("admin");
        login = prihlasenieDao.verifyLogin(login);
        MainForm mainForm = new MainForm(login);
        mainForm.setTitle("Kniha jázd - hlavné okno. Používateľ: " + login.getLogin().toString().toUpperCase());
        mainForm.setLocationRelativeTo(null);
        mainForm.setVisible(true);
    }

}
