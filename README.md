# Rubrica

Rubrica telefonica desktop in Java 21 con interfaccia Swing e database MySQL.
Permette di visualizzare, aggiungere, modificare ed eliminare i contatti, dopo aver effettuato il login.

## Requisiti

- JDK 21 o superiore
- MySQL Server 8

## Configurazione

1. Crea il database eseguendo lo script `schema_database.sql`. Lo script crea anche l'utente `admin` con password `admin`
   e tre contatti di esempio (temporaneamente commentati).
2. Apri `credenziali_database.properties` e inserisci i dati del tuo database
   (indirizzo, porta, nome del database, username e password).

Il driver JDBC di MySQL (Connector/J) è già nella cartella `lib`.
