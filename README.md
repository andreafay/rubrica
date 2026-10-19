# Rubrica

Rubrica telefonica desktop in Java 21 con interfaccia Swing e database MySQL.
Permette di visualizzare, aggiungere, modificare ed eliminare i contatti, dopo aver effettuato il login.

## Requisiti

- JDK 21 o superiore
- MySQL Server 8 (e, se vuoi un'interfaccia grafica, MySQL Workbench)

## Configurazione

1. Crea il database eseguendo lo script `schema_database.sql` (da Workbench oppure con
   `mysql -u root -p < schema_database.sql`). Lo script crea anche l'utente `admin` con password `admin`
   e tre contatti di esempio.
2. Apri `credenziali_database.properties` e inserisci i dati del tuo database
   (indirizzo, porta, nome del database, username e password).
   Non fare commit delle tue credenziali: per evitarlo puoi usare
   `git update-index --skip-worktree credenziali_database.properties`.

Il driver JDBC di MySQL (Connector/J) è già nella cartella `lib`.

## Compilazione ed esecuzione

Dalla cartella principale del progetto (il programma cerca `credenziali_database.properties` e `icone/` lì):

```bash
javac -encoding UTF-8 --release 21 -d out $(find src -name "*.java")
java -cp "out;lib/mysql-connector-j-26.7.0.jar" Main
```

Su Linux e macOS nel classpath usa `:` al posto di `;`.

## Struttura

- `src/Main.java`: punto di ingresso, apre la finestra di login
- `src/rubrica/model`: classi `Persona` e `Utente`
- `src/rubrica/db`: accesso ai dati (`ArchivioDatabase` per MySQL, `ArchivioFile` per il vecchio file di testo)
- `src/rubrica/ui`: finestre Swing (`FinestraLogin`, `FinestraRubrica`, `EditorPersona`)
- `icone`: icone dei pulsanti (Google Material Icons, licenza Apache 2.0)
