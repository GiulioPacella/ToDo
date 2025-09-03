package dao;

import model.Utente;

/**
 * Interfaccia DAO per la gestione degli Utenti.
 * Definisce le operazioni di persistenza principali che possono
 * essere eseguite su un Utente, indipendentemente dal database utilizzato.
 */
public interface UtenteDAO {

    /**
     * Registra un nuovo utente all'interno del database
     *
     * @param utente l'utente da registrare
     *
     */
    void salvaUtente(Utente utente);

    /**
     * Controlla se esiste nel database un utente che rispetti l'username e la password inserite all'interno della schermata di login
     *
     * @param username l'username inserita
     * @param password la password inserita
     * @return 1 se esiste, 0 se non esiste
     */
    boolean verificaLogin(String username, String password);

    /**
     * Restituisce un utente contenuto nel database con un certo username, aggiungendolo al model
     *
     * @param username l'username
     * @return l'utente
     *
     */
    Utente getUtenteByUsername(String username);
}
