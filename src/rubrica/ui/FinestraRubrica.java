package rubrica.ui;

import rubrica.db.ArchivioDatabase;
// Versione con file di testo (disattivata):
// import rubrica.db.ArchivioFile;
import rubrica.model.Persona;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.Vector;

public class FinestraRubrica extends JFrame {
    private final Vector<Persona> persone = new Vector<>();
    private final ArchivioDatabase database = new ArchivioDatabase();
    // Versione con file di testo (disattivata):
    // private final ArchivioFile archivioFile = new ArchivioFile();

    private final DefaultTableModel modello = new DefaultTableModel( new String[]{"Nome", "Cognome", "Telefono"}, 0) {
        @Override
        public boolean isCellEditable(int riga, int colonna) {
            return false;
        }
    };
    private final JTable tabella = new JTable(modello);

    public FinestraRubrica() {
        super("Rubrica");

        tabella.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabella.getTableHeader().setReorderingAllowed(false);

        JButton nuovo = new JButton("Nuovo", Icone.nuovo());
        JButton modifica = new JButton("Modifica", Icone.modifica());
        JButton elimina = new JButton("Elimina", Icone.elimina());
        nuovo.addActionListener(e -> nuovaPersona());
        modifica.addActionListener(e -> modificaPersona());
        elimina.addActionListener(e -> eliminaPersona());

        JToolBar barra = new JToolBar();
        barra.setFloatable(false);
        barra.add(nuovo);
        barra.add(modifica);
        barra.add(elimina);

        add(barra, BorderLayout.NORTH);
        add(new JScrollPane(tabella), BorderLayout.CENTER);

        // Versione con file di testo (disattivata):
        // persone.addAll(archivioFile.carica());
        // aggiornaTabella();
        ricarica();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(520, 380);
        setLocationRelativeTo(null);
    }

    private void nuovaPersona() {
        EditorPersona editor = new EditorPersona(this, null);
        editor.setVisible(true);
        Persona p = editor.getRisultato();
        // Versione con file di testo (disattivata):
        // if (p != null) {
        //     persone.add(p);
        //     aggiornaTabella();
        //     archivioFile.salva(persone);
        // }
        if (p != null) {
            try {
                database.inserisci(p);
                persone.add(p);
                aggiornaTabella();
            } catch (Exception ex) {
                erroreDatabase(ex);
            }
        }
    }

    private void modificaPersona() {
        Persona p = personaSelezionata();
        if (p == null) {
            return;
        }
        EditorPersona editor = new EditorPersona(this, p);
        editor.setVisible(true);
        // Versione con file di testo (disattivata):
        // if (editor.getRisultato() != null) {
        //     aggiornaTabella();
        //     archivioFile.salva(persone);
        // }
        if (editor.getRisultato() != null) {
            try {
                database.aggiorna(p);
                aggiornaTabella();
            } catch (Exception ex) {
                erroreDatabase(ex);
            }
        }
    }

    private void eliminaPersona() {
        Persona p = personaSelezionata();
        if (p == null) {
            return;
        }
        int risposta = JOptionPane.showConfirmDialog(this, "Eliminare la persona " + p.getNome() + " " + p.getCognome() + "?", "Conferma eliminazione", JOptionPane.YES_NO_OPTION);
        // Versione con file di testo (disattivata):
        // if (risposta == JOptionPane.YES_OPTION) {
        //     persone.remove(p);
        //     aggiornaTabella();
        //     archivioFile.salva(persone);
        // }
        if (risposta == JOptionPane.YES_OPTION) {
            try {
                database.elimina(p);
                persone.remove(p);
                aggiornaTabella();
            } catch (Exception ex) {
                erroreDatabase(ex);
            }
        }
    }

    private Persona personaSelezionata() {
        int riga = tabella.getSelectedRow();
        if (riga < 0) {
            JOptionPane.showMessageDialog(this, "È necessario selezionare una persona prima di procedere.", "Errore", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return persone.get(riga);
    }

    private void aggiornaTabella() {
        modello.setRowCount(0);
        for (Persona p : persone) {
            modello.addRow(new Object[]{p.getNome(), p.getCognome(), p.getTelefono()});
        }
    }

    private void ricarica() {
        persone.clear();
        try {
            persone.addAll(database.carica());
        } catch (Exception ex) {
            persone.clear();
            erroreDatabase(ex);
        }
        aggiornaTabella();
    }

    private void erroreDatabase(Exception ex) {
        String dettaglio = (ex instanceof FileNotFoundException)
                ? "File " + ArchivioDatabase.FILE_CREDENZIALI + " non trovato."
                : String.valueOf(ex.getMessage());
        JOptionPane.showMessageDialog(this, "Errore di accesso al database:\n" + dettaglio, "Errore", JOptionPane.ERROR_MESSAGE);
        if (persone.isEmpty()) {
            return;
        }
        try {
            persone.clear();
            persone.addAll(database.carica());
        } catch (Exception ignorata) {
            persone.clear();
        }
        aggiornaTabella();
    }
}
