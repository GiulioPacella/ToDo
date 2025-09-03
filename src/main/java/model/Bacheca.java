package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta una bacheca personale di un utente.
 * Ogni utente possiede tre bacheche (numerate da 1 a 3),
 * nelle quali può inserire, organizzare e spostare i propri ToDo.
 * La bacheca mantiene una lista dei ToDo a essa associati.
 */
public class Bacheca
{
    private int id;
    private String titoloBacheca;
    private String descrizioneBacheca;
    private ArrayList<ToDo> todos;


    /**
     * Costruttore della classe Bacheca
     *
     * @param titolo il titolo della bacheca
     * @param descrizioneBacheca la descrizione della bacheca
     *
     */
    public Bacheca(String titolo, String descrizioneBacheca)
    {
    ArrayList<ToDo> tmp = new ArrayList<>();
    this.titoloBacheca = titolo;
    this.descrizioneBacheca = descrizioneBacheca;
    this.todos = tmp;
    }

    /**
     * Restituisce l'id della bacheca
     *
     * @return l'id
     *
     */
    public int getId() { return id;}

    /**
     * Restituisce il titolo della bacheca
     *
     * @return il titolo
     *
     */
    public String getTitolo()
    {
        return titoloBacheca;
    }

    /**
     * Restituisce la descrizione della bacheca
     *
     * @return la descrizione
     *
     */
    public String getDescrizioneBacheca() {
        return descrizioneBacheca;
    }

    /**
     * Restituisce la lista dei todo contenuti nella bacheca
     *
     * @return la lista di todo
     *
     */
    public List<ToDo> getTodos()
    {
        return todos;
    }

    /**
     * Imposta l'id della bacheca
     *
     * @param id il nuovo id
     *
     */
    public void setId(int id) {this.id = id;}

    /**
     * Imposta l'id della bacheca
     *
     * @param titolo il nuovo titolo
     *
     */
    public void setTitolo(String titolo)
    {
        this.titoloBacheca = titolo;
    }

    /**
     * Imposta la descrizione della bacheca
     *
     * @param descrizioneBacheca la nuova descrizione
     *
     */
    public void setDescrizioneBacheca(String descrizioneBacheca) { this.descrizioneBacheca = descrizioneBacheca; }

    /**
     * Imposta la lista dei todo contenuti nella bacheca
     *
     * @param todos la nuova lista di todo
     *
     */
    public void setTodos(ArrayList<ToDo> todos)
    {
        this.todos = todos;
    }

    /**
     * Aggiunge un todo alla bacheca
     *
     * @param t il todo da aggiungere
     *
     */
    public void addToDo(ToDo t)
    {
        todos.add(t);
    }

}
