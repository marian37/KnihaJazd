package sk.upjs.ics.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialCap;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialPointer;
import org.jfree.chart.plot.dial.DialTextAnnotation;
import org.jfree.chart.plot.dial.DialValueIndicator;
import org.jfree.chart.plot.dial.StandardDialFrame;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.data.jdbc.JDBCCategoryDataset;
import org.jfree.data.jdbc.JDBCPieDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.StandardGradientPaintTransformer;
import org.springframework.jdbc.core.JdbcTemplate;
import sk.upjs.ics.cestak.DaoFactory;
import sk.upjs.ics.cestak.dao.AutoDAO;
import sk.upjs.ics.cestak.entity.Auto;
import sk.upjs.ics.cestak.entity.Login;

public class Grafy extends JDialog {

    private static final Component CENTER_SCREEN = null;

    // Labely
    private JLabel lblGraf = new JLabel("Graf:");
    private JLabel lblAuto = new JLabel("Auto:");

    // ComboBoxy
    private JComboBox comboGraf = new JComboBox();
    private JComboBox comboAuto = new JComboBox();

    private DataSource dataSource = DaoFactory.INSTANCE.dataSource();
    private AutoDAO autoDao = DaoFactory.INSTANCE.autoDao();
    private ChartPanel chartPanel = new ChartPanel(null);

    int aktualnyGraf = 0;
    String aktualneAutoSpz = null;

    private Login login;

    public Grafy(Login login, Frame parent) {
        this(parent, true);
        this.login = login;
        this.setTitle("Štatistika");
        nastavComboGrafy();
        nastavComboAuta();
        ukazAuto(false);
        nastavGrafNajazdeneKmPodlaAut();
    }

    public Grafy(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setLayout(new MigLayout("", "[][fill, grow]", "[][][]"));
        add(lblGraf);
        add(comboGraf, "wrap");
        add(lblAuto);
        add(comboAuto, "wrap");
        add(chartPanel, "wrap, span 2");

        setResizable(false);
        pack();
    }

    private void nastavComboGrafy() {
        String[] grafy = {"Najazdené km podľa áut",
            "Najazdené km za mesiac",
            "Priemerná rýchlosť",
            "Čerpanie PHM",
            "Výdavky za autá"};
        comboGraf.setModel(new DefaultComboBoxModel(grafy));
        comboGraf.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                aktualnyGraf = comboGraf.getSelectedIndex();
                nastavGraf();
            }

        });
    }

    private void nastavComboAuta() {
        List<Auto> auta = autoDao.zoznamPodlaPouzivatela(login);
        List<String> autaSpz = new ArrayList<>();
        for (Auto auto : auta) {
            autaSpz.add(auto.getSpz());
        }
        comboAuto.setModel(new DefaultComboBoxModel(autaSpz.toArray()));
        comboAuto.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                aktualneAutoSpz = comboAuto.getSelectedItem().toString();
                nastavGraf();
            }
        });
    }

    private void ukazAuto(boolean ukaz) {
        lblAuto.setVisible(ukaz);
        comboAuto.setVisible(ukaz);
    }

    private void nastavGraf() {
        switch (aktualnyGraf) {
            case 0:
                ukazAuto(false);
                nastavGrafNajazdeneKmPodlaAut();
                break;
            case 1:
                ukazAuto(true);
                nastavGrafNajazdeneKmZaMesiac();
                break;
            case 2:
                ukazAuto(true);
                nastavGrafPriemernaRychlost();
                break;
            case 3:
                ukazAuto(true);
                nastavGrafTankovanie();
                break;
            case 4:
                ukazAuto(false);
                nastavGrafVydavkyZaAuta();
                break;
        }
    }

    private void nastavGrafNajazdeneKmPodlaAut() {
        JDBCPieDataset dataset = null;
        try {
            dataset = new JDBCPieDataset(dataSource.getConnection(),
                    "SELECT SPZ, SUM(prejdeneKilometre) AS 'Prejdene kilometre' \n"
                    + "FROM Jazda\n"
                    + "WHERE idPouzivatel = " + login.getId() + "\n"
                    + "GROUP BY SPZ\n"
            );
        } catch (SQLException ex) {
            System.out.println("Chyba pripojenia k databáze pri grafe Najazdené km podľa áut");
        }
        JFreeChart chart = ChartFactory.createPieChart("Najazdené km podľa áut", dataset);
        chart.removeLegend();
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} km ({2})"));

        chart.setBackgroundPaint(new Color(0, 0, 0, 0));
        chartPanel.setChart(chart);
    }

    private void nastavGrafNajazdeneKmZaMesiac() {
        aktualneAutoSpz = comboAuto.getSelectedItem().toString();
        JDBCCategoryDataset dataset = null;
        try {
            dataset = new JDBCCategoryDataset(dataSource.getConnection(),
                    "SELECT DATE_FORMAT(datum, '%M %Y') AS 'Mesiac', SUM(prejdeneKilometre) AS 'Prejdene kilometre'\n"
                    + "FROM Jazda\n"
                    + "WHERE idPouzivatel = " + login.getId() + "\n"
                    + "AND SPZ = '" + aktualneAutoSpz + "'\n"
                    + "GROUP BY DATE_FORMAT(datum, '%M %Y')\n"
                    + "ORDER BY datum"
            );
        } catch (SQLException ex) {
            System.out.println("Chyba pripojenia k databáze pri grafe Najazdené km za mesiac");
        }
        JFreeChart chart = ChartFactory.createBarChart("Najazdené km za mesiac", "mesiace", "km", dataset);
        chart.removeLegend();
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer br = new BarRenderer();
        br.setSeriesPaint(0, Color.BLUE);
        br.setMaximumBarWidth(0.2);
        plot.setRenderer(br);
        chart.setBackgroundPaint(new Color(0, 0, 0, 0));
        chartPanel.setChart(chart);
    }

    private void nastavGrafPriemernaRychlost() {
        aktualneAutoSpz = comboAuto.getSelectedItem().toString();

        JdbcTemplate jdbcTemplate = DaoFactory.INSTANCE.jdbcTemplate();
        Double priemernaRychlost = jdbcTemplate.queryForObject(
                "SELECT AVG(prejdeneKilometre / (TIMEDIFF(cas_prichod, cas_odchod)) * 10000) AS 'Priemerna rychlost'\n"
                + "FROM Jazda\n"
                + "WHERE idPouzivatel = ?\n"
                + "AND SPZ = ?",
                Double.class, login.getId(), aktualneAutoSpz);

        // Ak auto nejazdilo 
        if (priemernaRychlost == null) {
            priemernaRychlost = 0D;
        }

        // Ak vyjde zaporna hodnota - pretecenie premennej
        if (priemernaRychlost < 0) {
            priemernaRychlost = -1D;
        }

        // Ak vyjde viac ako je max. na stupnici
        if (priemernaRychlost > 200) {
            priemernaRychlost = 200D;
        }

        ValueDataset dataset = new DefaultValueDataset(priemernaRychlost);

        DialPlot plot = new DialPlot(dataset);

        StandardDialFrame dialFrame = new StandardDialFrame();
        dialFrame.setBackgroundPaint(Color.lightGray);
        dialFrame.setForegroundPaint(Color.darkGray);
        plot.setDialFrame(dialFrame);

        GradientPaint gp = new GradientPaint(new Point(),
                new Color(255, 255, 255), new Point(),
                new Color(170, 170, 220));
        DialBackground db = new DialBackground(gp);
        db.setGradientPaintTransformer(new StandardGradientPaintTransformer(
                GradientPaintTransformType.VERTICAL));
        plot.setBackground(db);
        // Nastavenie napisu
        DialTextAnnotation annotation1 = new DialTextAnnotation("Priemerná rýchlosť");
        annotation1.setFont(new Font("Dialog", Font.BOLD, 14));
        annotation1.setPaint(Color.RED);
        annotation1.setRadius(0.45);

        plot.addLayer(annotation1);

        // Nastavenie ukazovatela hodnoty
        DialValueIndicator dvi = new DialValueIndicator(0);
        dvi.setFont(new Font("Dialog", Font.PLAIN, 14));
        dvi.setOutlinePaint(Color.darkGray);
        dvi.setRadius(0.68);
        dvi.setAngle(-90.0);
        plot.addLayer(dvi);

        // Nastavenie stupnice
        StandardDialScale scale = new StandardDialScale(0, 200, -120, -300, 10, 4);
        scale.setTickRadius(0.88);
        scale.setTickLabelOffset(0.15);
        scale.setTickLabelFont(new Font("Dialog", Font.PLAIN, 13));
        scale.setTickLabelPaint(Color.DARK_GRAY);
        scale.setMajorTickPaint(new Color(139, 69, 19));
        scale.setMinorTickPaint(new Color(139, 69, 19));
        plot.addScale(0, scale);
        DialPointer needle = new DialPointer.Pointer();
        plot.addLayer(needle);

        // Kruzok v strede
        DialCap cap = new DialCap();
        cap.setRadius(0.1);
        plot.setCap(cap);

        JFreeChart chart = new JFreeChart(plot);
        chart.setTitle("Priemerná rýchlosť pre auto " + aktualneAutoSpz);

        chart.setPadding(new RectangleInsets(0, 135, 0, 135));
        chartPanel.setChart(chart);
    }

    private void nastavGrafTankovanie() {
        aktualneAutoSpz = comboAuto.getSelectedItem().toString();
        JDBCCategoryDataset dataset = null;
        try {
            dataset = new JDBCCategoryDataset(dataSource.getConnection(),
                    "SELECT DATE_FORMAT(datum, '%M %Y') AS 'Mesiac', SUM(cerpaniePHM) AS 'Cerpanie PHM'\n"
                    + "FROM Jazda\n"
                    + "WHERE idPouzivatel = " + login.getId() + "\n"
                    + "AND SPZ = '" + aktualneAutoSpz + "'\n"
                    + "GROUP BY DATE_FORMAT(datum, '%M %Y')\n"
                    + "ORDER BY datum"
            );
        } catch (SQLException ex) {
            System.out.println("Chyba pripojenia k databáze pri grafe Čerpanie PHM");
        }
        JFreeChart chart = ChartFactory.createBarChart("Čerpanie PHM", "mesiace", "suma", dataset);
        chart.removeLegend();
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer br = new BarRenderer();
        br.setSeriesPaint(0, Color.ORANGE);
        br.setMaximumBarWidth(0.2);
        plot.setRenderer(br);
        chart.setBackgroundPaint(new Color(0, 0, 0, 0));
        chartPanel.setChart(chart);
    }

    private void nastavGrafVydavkyZaAuta() {
        JDBCPieDataset dataset = null;
        try {
            dataset = new JDBCPieDataset(dataSource.getConnection(),
                    "SELECT Auto.SPZ, SUM(Vydavok.suma) AS 'Vydavok'\n"
                    + "FROM Auto\n"
                    + "JOIN Vydavok ON Auto.SPZ = Vydavok.autoSPZ\n"
                    + "WHERE idPouzivatel = " + login.getId() + "\n"
                    + "GROUP BY Auto.SPZ\n"
            );
        } catch (SQLException ex) {
            System.out.println("Chyba pripojenia k databáze pri grafe Výdavky za autá");
        }
        JFreeChart chart = ChartFactory.createPieChart("Výdavky za autá", dataset);
        chart.removeLegend();
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} € ({2})"));

        chart.setBackgroundPaint(new Color(0, 0, 0, 0));
        chartPanel.setChart(chart);
    }

    public static void main(String args[]) {

        Grafy grafy = new Grafy(new javax.swing.JFrame(), true);
        grafy.setVisible(true);
        grafy.setTitle("Kniha jázd - štatistika");
        grafy.setLocationRelativeTo(CENTER_SCREEN);
    }

}
