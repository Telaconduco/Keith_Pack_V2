import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;


public class MainPanel {

    public JPanel thepanel;
    private JButton acceptButton;
    private JButton denyButton;
    private JButton termsAndConditionsButton;

    public MainPanel(final JFrame window) {
        termsAndConditionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    URI tc = new URI("telaconduco.ddns.net/termsandconditions.html");
                    Desktop.getDesktop().browse(tc);
                }catch(Exception u){
                    u.printStackTrace();
                }
            }
        });
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String getVersion = System.getProperties().getProperty("java.version");
                if (getVersion.equals("1.8.0_45") || getVersion.equals("1.8.0_40")) {
                    InstallMods IM = new InstallMods();
                    window.setContentPane(IM);
                    window.invalidate();
                    window.validate();
                    window.repaint();
                }else{
                    JOptionPane.showConfirmDialog(thepanel,"You need to update Java before continuing");
                }
            }
        });
        denyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showConfirmDialog(thepanel,"You cannot install the Keith Pack V2");
                System.exit(0);
            }
        });
    }
}
