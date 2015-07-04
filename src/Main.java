import javax.swing.*;
import java.awt.*;

/**
 * Created by James on 6/24/2015.
 */
public class Main {
    public static void main(String args[]){
        JFrame window = new JFrame("Keith Pack V2");
        MainPanel mp = new MainPanel(window);
        window.setContentPane(mp.thepanel);
        window.setPreferredSize(new Dimension(1000,1000));
        window.setResizable(false);
        window.pack();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }
}
