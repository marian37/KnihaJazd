package sk.upjs.ics.cestak;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author Majlo
 */
public class AutoListCellRenderer extends DefaultListCellRenderer {

    @Override
    public void setHorizontalAlignment(int alignment) {
        super.setHorizontalAlignment(DefaultListCellRenderer.CENTER); 
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Auto auto = (Auto) value;

        StringBuilder sb = new StringBuilder();
        sb.append(auto.getSpz()).append(" |    ").append(auto.getZnacka()).append(" ").append(auto.getModel()).append(", ").append(auto.getRok_vyr()).append(", ").append(auto.getVykon() + " kW, ").append(auto.getPalivo());
        return super.getListCellRendererComponent(list, sb, index, isSelected, cellHasFocus); //To change body of generated methods, choose Tools | Templates.
    }

}
