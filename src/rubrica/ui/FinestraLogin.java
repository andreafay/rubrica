package rubrica.ui;

import rubrica.db.ArchivioDatabase;
import rubrica.model.Utente;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.FileNotFoundException;

public class FinestraLogin extends JFrame {
    private final JTextField campoUsername = new JTextField(15);
    private final JPasswordField campoPassword = new JPasswordField(15);
    private final JLabel erroreUsername = creaEtichettaErrore();
    private final JLabel errorePassword = creaEtichettaErrore();
    private final Border bordoNormale = campoUsername.getBorder();
    private final Border bordoErrore = BorderFactory.createLineBorder(Color.RED, 2);
    private final ArchivioDatabase database = new ArchivioDatabase();

    public FinestraLogin() {
        super("Login");

        JPanel campi = new JPanel(new GridBagLayout());
        campi.setBorder(BorderFactory.createEmptyBorder(12, 12, 6, 12));
        aggiungiRiga(campi, 0, "Username:", campoUsername, erroreUsername);
        aggiungiRiga(campi, 2, "Password:", campoPassword, errorePassword);

        JButton login = new JButton("LOGIN");
        login.addActionListener(e -> effettuaLogin());
        JPanel pulsanti = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        pulsanti.add(login);
        getRootPane().setDefaultButton(login);

        setLayout(new BorderLayout());
        add(campi, BorderLayout.CENTER);
        add(pulsanti, BorderLayout.SOUTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private static JLabel creaEtichettaErrore() {
        JLabel etichetta = new JLabel(" ");
        etichetta.setForeground(Color.RED);
        etichetta.setFont(etichetta.getFont().deriveFont(Font.PLAIN, 11f));
        return etichetta;
    }

    private static void aggiungiRiga(JPanel pannello, int riga, String testo, JTextField campo, JLabel errore) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 4, 2, 4);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        c.gridy = riga;
        pannello.add(new JLabel(testo), c);

        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        pannello.add(campo, c);

        c.gridy = riga + 1;
        c.insets = new Insets(0, 4, 4, 4);
        pannello.add(errore, c);
    }

    private boolean segnalaSeVuoto(JTextField campo, JLabel errore, boolean vuoto) {
        campo.setBorder(vuoto ? bordoErrore : bordoNormale);
        errore.setText(vuoto ? "Campo obbligatorio" : " ");
        return vuoto;
    }

    private void effettuaLogin() {
        String username = campoUsername.getText().trim();
        String password = new String(campoPassword.getPassword());

        boolean usernameVuoto = segnalaSeVuoto(campoUsername, erroreUsername, username.isEmpty());
        boolean passwordVuota = segnalaSeVuoto(campoPassword, errorePassword, password.isEmpty());
        if (usernameVuoto || passwordVuota) {
            (usernameVuoto ? campoUsername : campoPassword).requestFocusInWindow();
            return;
        }

        try {
            if (database.login(new Utente(username, password))) {
                dispose();
                new FinestraRubrica().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Login errato", "Errore", JOptionPane.ERROR_MESSAGE);
                campoPassword.setText("");
            }
        } catch (Exception ex) {
            String dettaglio = (ex instanceof FileNotFoundException)
                    ? "File " + ArchivioDatabase.FILE_CREDENZIALI + " non trovato."
                    : String.valueOf(ex.getMessage());
            JOptionPane.showMessageDialog(this, "Errore di accesso al database:\n" + dettaglio, "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}
