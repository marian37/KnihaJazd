package sk.upjs.ics.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
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
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.jdbc.JDBCCategoryDataset;
import org.jfree.data.jdbc.JDBCPieDataset;
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
        String[] grafy = {"Najazdené km podľa áut", "Najazdené km za mesiac", "Priemerná rýchlosť"};
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
        }
    }

    private void nastavGrafNajazdeneKmPodlaAut() {
        JDBCPieDataset dataset = null;
        try {
            dataset = new JDBCPieDataset(dataSource.getConnection(),
                    "SELECT SPZ, SUM(prejdeneKilometre) \n"
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

        chartPanel.setChart(chart);
    }

    private void nastavGrafNajazdeneKmZaMesiac() {
        aktualneAutoSpz = comboAuto.getSelectedItem().toString();
        JDBCCategoryDataset dataset = null;
        try {
            dataset = new JDBCCategoryDataset(dataSource.getConnection(),
                    "SELECT DATE_FORMAT(datum, '%M %Y'), SUM(prejdeneKilometre)\n"
                    + "FROM Jazda\n"
                    + "WHERE idPouzivatel = " + login.getId() + "\n"
                    + "AND SPZ = \"" + aktualneAutoSpz + "\"\n"
                    + "GROUP BY DATE_FORMAT(datum, '%M %Y')\n"
                    + "ORDER BY datum");
        } catch (SQLException ex) {
            System.out.println("Chyba pripojenia k databáze pri grafe Najazdené km za mesiac");
        }
        JFreeChart chart = ChartFactory.createBarChart("Najazdené km za mesiac", "mesiace", "km", dataset);
        chart.removeLegend();
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        BarRenderer br = new BarRenderer();
        br.setSeriesPaint(0, Color.BLUE);
        br.setMaximumBarWidth(0.2);
        plot.setRenderer(br);
        chartPanel.setChart(chart);
    }

    private void nastavGrafPriemernaRychlost() {
        // DOROB GRAF        
        System.out.println("Ešte nie je podporované");
    }

    public static void main(String args[]) {

        Grafy grafy = new Grafy(new javax.swing.JFrame(), true);
        grafy.setVisible(true);
        grafy.setTitle("Kniha jázd - štatistika");
        grafy.setLocationRelativeTo(CENTER_SCREEN);
    }

}
