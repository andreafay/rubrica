package rubrica.ui;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public final class Icone {
    private static final String CARTELLA = "icone/";

    private Icone() {
    }

    private static Icon carica(String nomeFile) {
        return new ImageIcon(CARTELLA + nomeFile);
    }

    public static Icon nuovo() {
        return carica("nuovo.png");
    }

    public static Icon modifica() {
        return carica("modifica.png");
    }

    public static Icon elimina() {
        return carica("elimina.png");
    }

    public static Icon salva() {
        return carica("salva.png");
    }

    public static Icon annulla() {
        return carica("annulla.png");
    }
}
