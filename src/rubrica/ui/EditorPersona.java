package rubrica.ui;

import rubrica.model.Persona;
import javax.swing.*;
import java.awt.*;

public class EditorPersona extends JDialog {
    private final JTextField campoNome = new JTextField(20);
    private final JTextField campoCognome = new JTextField(20);
    private final JTextField campoIndirizzo = new JTextField(20);
    private final JTextField campoTelefono = new JTextField(20);
    private final JTextField campoEta = new JTextField(20);

    private final Persona persona;
    private Persona risultato;

    public EditorPersona(Frame parent, Persona persona) {
        super(parent, "editor-persona", true);
        this.persona = persona;

        JPanel campi = new JPanel(new GridLayout(5, 2, 8, 8));
        campi.setBorder(BorderFactory.createEmptyBorder(12, 12, 6, 12));
        campi.add(new JLabel("Nome:"));      campi.add(campoNome);
        campi.add(new JLabel("Cognome:"));   campi.add(campoCognome);
        campi.add(new JLabel("Indirizzo:")); campi.add(campoIndirizzo);
        campi.add(new JLabel("Telefono:"));  campi.add(campoTelefono);
        campi.add(new JLabel("Età:"));       campi.add(campoEta);

        JButton salva = new JButton("Salva", Icone.salva());
        JButton annulla = new JButton("Annulla", Icone.annulla());
        JToolBar barra = new JToolBar();
        barra.setFloatable(false);
        barra.add(salva);
        barra.add(annulla);

        if (persona != null) {
            campoNome.setText(persona.getNome());
            campoCognome.setText(persona.getCognome());
            campoIndirizzo.setText(persona.getIndirizzo());
            campoTelefono.setText(persona.getTelefono());
            campoEta.setText(String.valueOf(persona.getEta()));
        }

        salva.addActionListener(e -> salva());
        annulla.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(salva);

        setLayout(new BorderLayout());
        add(barra, BorderLayout.NORTH);
        add(campi, BorderLayout.CENTER);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void salva() {
        int eta;
        try {
            eta = Integer.parseInt(campoEta.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "L'età deve essere un numero intero.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String nome = pulisci(campoNome);
        String cognome = pulisci(campoCognome);
        String indirizzo = pulisci(campoIndirizzo);
        String telefono = pulisci(campoTelefono);

        if (persona == null) {
            risultato = new Persona(nome, cognome, indirizzo, telefono, eta);
        } else {
            persona.setNome(nome);
            persona.setCognome(cognome);
            persona.setIndirizzo(indirizzo);
            persona.setTelefono(telefono);
            persona.setEta(eta);
            risultato = persona;
        }
        dispose();
    }

    private static String pulisci(JTextField campo) {
        return campo.getText().replace(";", ",").trim();
    }

    public Persona getRisultato() {
        return risultato;
    }
}
