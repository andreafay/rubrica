package rubrica.db;

import rubrica.model.Persona;
import rubrica.model.Utente;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.Vector;

public class ArchivioDatabase {
    public static final String FILE_CREDENZIALI = "credenziali_database.properties";

    private Connection connetti() throws Exception {
        Properties prop = new Properties();
        try (Reader r = new InputStreamReader(new FileInputStream(FILE_CREDENZIALI), StandardCharsets.UTF_8)) {
            prop.load(r);
        }
        String url = "jdbc:mysql://" + prop.getProperty("db.ip", "localhost").trim()
                + ":" + prop.getProperty("db.porta", "3306").trim()
                + "/" + prop.getProperty("db.nome", "rubrica").trim()
                + "?useUnicode=true&characterEncoding=UTF-8";
        return DriverManager.getConnection(url,
                prop.getProperty("db.username", "").trim(),
                prop.getProperty("db.password", ""));
    }

    public boolean login(Utente u) throws Exception {
        String sql = "SELECT COUNT(*) FROM utente WHERE username = ? AND password = SHA2(?, 256)";
        try (Connection c = connetti();
             PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, u.getUsername());
                ps.setString(2, u.getPassword());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public Vector<Persona> carica() throws Exception {
        Vector<Persona> elenco = new Vector<>();
        String sql = "SELECT id, nome, cognome, indirizzo, telefono, eta FROM persone ORDER BY id";
        try (Connection c = connetti();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Persona p = new Persona(rs.getString("nome"), rs.getString("cognome"), rs.getString("indirizzo"), rs.getString("telefono"), rs.getInt("eta"));
                p.setId(rs.getInt("id"));
                elenco.add(p);
            }
        }
        return elenco;
    }

    public void inserisci(Persona p) throws Exception {
        String sql = "INSERT INTO persone (nome, cognome, indirizzo, telefono, eta) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = connetti();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getCognome());
            ps.setString(3, p.getIndirizzo());
            ps.setString(4, p.getTelefono());
            ps.setInt(5, p.getEta());
            ps.executeUpdate();
            try (ResultSet chiavi = ps.getGeneratedKeys()) {
                if (chiavi.next()) {
                    p.setId(chiavi.getInt(1));
                }
            }
        }
    }

    public void aggiorna(Persona p) throws Exception {
        String sql = "UPDATE persone SET nome = ?, cognome = ?, indirizzo = ?, telefono = ?, eta = ? WHERE id = ?";
        try (Connection c = connetti();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getCognome());
            ps.setString(3, p.getIndirizzo());
            ps.setString(4, p.getTelefono());
            ps.setInt(5, p.getEta());
            ps.setInt(6, p.getId());
            ps.executeUpdate();
        }
    }

    public void elimina(Persona p) throws Exception {
        try (Connection c = connetti();
             PreparedStatement ps = c.prepareStatement("DELETE FROM persone WHERE id = ?")) {
            ps.setInt(1, p.getId());
            ps.executeUpdate();
        }
    }
}
