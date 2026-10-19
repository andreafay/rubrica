import rubrica.ui.FinestraLogin;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        UIManager.put("OptionPane.yesButtonText", "Sì");
        UIManager.put("OptionPane.noButtonText", "No");
        SwingUtilities.invokeLater(() -> new FinestraLogin().setVisible(true));
    }
}
