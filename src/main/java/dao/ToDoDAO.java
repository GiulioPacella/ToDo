package dao;

import model.Bacheca;
import model.ToDo;
import model.Utente;


/**
 * Interfaccia DAO per la gestione dei ToDo.
 * Definisce le operazioni di persistenza principali che possono
 * essere eseguite su un ToDo, indipendentemente dal database utilizzato.
 */
public interface ToDoDAO
{

    /**
     * Inserisce all'interno del Database un todo
     *
     * @param todo il todo da inserire nel db
     * @param idBacheca l'id della bacheca in cui inserire il todo
     * @param autore l'autore del todo
     *
     */
    void salvaToDo(ToDo todo, int idBacheca, Utente autore);

    /**
     * Metodo che popola le bacheche all'avvio SOLO con i todo creati dall'utente che ha effettuato l'accesso, aggiungendoli al model
     *
     * @param idBacheca id della bacheca da popolare
     * @param bacheca bacheca da popolare
     * @param autore utente corrente
     *
     */
    void popolaBacheca(int idBacheca, Bacheca bacheca, Utente autore);

    /**
     * Metodo per l'update di un todo nel database dopo la sua modifica
     *
     * @param todo il todo
     * @param idToDo l'id del todo
     *
     */
    void updateToDo(ToDo todo, int idToDo);

    /**
     * Metodo per cambiare la bacheca di un todo all'interno del database
     *
     * @param todo il todo
     * @param idToDo l'id del todo
     * @param idB l'id della bacheca di destinazione
     */
    void cambiaBachecaToDo(ToDo todo, int idToDo, int idB);

    /**
     * Metodo per eliminare tutti i todo di una bacheca all'interno del database
     *
     * @param idB bacheca dove eliminare i todo
     *
     */
    void svuotaBacheca(int idB);

    /**
     * Metodo per eliminare un todo dal database
     *
     * @param todo il todo
     * @param idToDo l'id del todo
     *
     */
    void eliminaToDo(ToDo todo, int idToDo);

    /**
     * Restituisce l'autore di un todo conoscendo l'id del todo
     *
     * @param idTodo l'id del todo
     * @return l'autore
     */
    String getAutoreToDo(int idTodo);

    /**
     * Popola una bacheca dell'utente che ha effettuato il login SOLO con i todo che sono stati condivisi con lui, aggiungendoli al model
     *
     * @param utenteCorrente l'utente corrente
     * @param bacheca la bacheca da popolare
     * @param numeroBachecaDest numero della bacheca da cui prendere i todo condivisi (di default: 1 = Università, 2 = Lavoro, 3 = Tempo libero)
     */
    void popolaTodoCondivisi(Utente utenteCorrente, Bacheca bacheca, int numeroBachecaDest);

}
