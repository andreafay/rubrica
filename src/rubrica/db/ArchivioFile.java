package rubrica.db;

import rubrica.model.Persona;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Vector;

public class ArchivioFile {
    private static final String NOME_FILE = "informazioni.txt";

    public Vector<Persona> carica() {
        Vector<Persona> elenco = new Vector<>();
        File file = new File(NOME_FILE);
        if (!file.exists()) {
            return elenco;
        }
        try (Scanner scanner = new Scanner(file, "UTF-8")) {
            while (scanner.hasNextLine()) {
                String riga = scanner.nextLine().trim();
                if (riga.isEmpty()) {
                    continue;
                }
                String[] campi = riga.split(";");
                if (campi.length != 5) {
                    continue; // riga malformata: ignorata
                }
                try {
                    elenco.add(new Persona(campi[0], campi[1], campi[2], campi[3], Integer.parseInt(campi[4].trim())));
                } catch (NumberFormatException ex) {
                }
            }
        } catch (FileNotFoundException | RuntimeException ex) {
        }
        return elenco;
    }

    public void salva(Vector<Persona> persone) throws Exception {
        try (PrintStream out = new PrintStream(new File(NOME_FILE), "UTF-8")) {
            for (Persona p : persone) {
                out.println(p.formattaPersona());
            }
        }
    }
}
