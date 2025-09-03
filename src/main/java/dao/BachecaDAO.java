package dao;

import model.Bacheca;


import java.util.List;

/**
 * Interfaccia DAO per la gestione delle Bacheche.
 * Definisce le operazioni di persistenza principali che possono
 * essere eseguite su una Bacheca, indipendentemente dal database utilizzato.
 */
public interface BachecaDAO {

    /**
     * Metodo per l'Update della bacheca quando vengono modificati il suo titolo o la sua descrizione
     *
     * @param bacheca la bacheca
     * @param idBacheca l'id della bacheca
     */
    void updateBacheca(Bacheca bacheca, int idBacheca);

    /**
     * Restituisce le bacheche di un utente dal suo username
     *
     * @param login l'username dell'utente
     * @return le bacheche in ordine di id
     *
     */
    List<Bacheca> getBachecheByUtente(String login);
}
