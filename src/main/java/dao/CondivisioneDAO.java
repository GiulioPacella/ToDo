package dao;

import model.Bacheca;
import model.Utente;
import java.util.List;

/**
 * Interfaccia DAO per la gestione delle Condivisioni.
 * Definisce le operazioni di persistenza principali che possono
 * essere eseguite su una Condivisione, indipendentemente dal database utilizzato.
 */
public interface CondivisioneDAO {

    /**
     * Condivide un todo con un utente ricevente, aggiungendo una riga "condivisione" nel database
     * @param idTodo l'id del todo condiviso
     * @param ricevente l'username del ricevente
     *
     */
    void aggiungiCondivisione(int idTodo, String ricevente);

    /**
     * Restituisce l'elenco di utenti che condividono un todo
     *
     * @param idTodo l'id del todo
     * @return la lista di utenti
     */
    List<Utente> getUtentiCondivisi(int idTodo);

    /**
     * Elimina tutti i todo condivisi con l'utente corrente in una sua data bacheca
     *
     * @param bacheca la bacheca da cui voglio eliminare i todo condivisi
     *
     */
     void eliminaCondivisioniDellaBacheca(Bacheca bacheca);

    /**
     * Elimina un todo condiviso da un altro utente dalla bacheca dell'utente ricevente
     *
     * @param idTodo l'id del todo
     * @param loginUtente l'utente ricevente
     */
    void eliminaCondivisionePerUtente(int idTodo, String loginUtente);
}
