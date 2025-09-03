package model;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Rappresenta un'attività ToDo all'interno del sistema.
 * Un ToDo contiene titolo, descrizione, scadenza, eventuale immagine,
 * stato di completamento e il riferimento alla bacheca e all'autore.
 * I ToDo possono essere condivisi con altri utenti, mantenendo però
 * un'unica istanza originale nel database.
 */
public class ToDo
{
    private int id;
    private String titoloTodo;
    private LocalDate scadenza;
    private String link;
    private String descrizioneTodo;
    private Image immagine;
    private Utente autore;
    private ArrayList<Utente> condivisioni;
    private Color colore;
    private Boolean completato = false;
    private Bacheca bacheca;
    private String immaginePath;


    /**
     * Costruttore della classe ToDo
     *
     */
    public ToDo(String titoloTodo, LocalDate scadenza, String link, String descrizioneTodo, Image immagine, String immaginePath, Color colore, Utente autore, Bacheca bacheca, Boolean completato)
    {
    this.titoloTodo = titoloTodo;
    this.scadenza = scadenza;
    this.link = link;
    this.descrizioneTodo = descrizioneTodo;
    this.immagine = immagine;
    this.colore = colore;
    this.autore = autore;
    this.condivisioni = new ArrayList<>();
    this.bacheca = bacheca;
    this.immaginePath = immaginePath;
    this.completato = completato;

    bacheca.getTodos().add(this);
    setImmaginePath(immaginePath);
    }

    /**
     * Restituisce l'id
     *
     * @return l'id
     *
     */
    public int  getId() {return id;}

    /**
     * Restituisce il titolo
     *
     * @return il titolo
     *
     */
    public String getTitoloTodo() {
        return titoloTodo;
    }

    /**
     * Restituisce la scadenza del todo
     *
     * @return la scadenza
     *
     */
    public LocalDate getScadenza() {
        return scadenza;
    }

    /**
     * Restituisce il link
     *
     * @return il link
     *
     */
    public String getLink() {
        return link;
    }

    /**
     * Restituisce la descrizione
     *
     * @return la descrizione
     *
     */
    public String getDescrizioneTodo()
    {
        return descrizioneTodo;
    }

    /**
     * Restituisce l'immagine
     *
     * @return l'immagine
     *
     */
    public Image getImmagine()
    {
        return immagine;
    }

    /**
     * Restituisce il colore
     *
     * @return il colore
     *
     */
    public Color getColore()
    {
        return colore;
    }

    /**
     * Restituisce lo stato
     *
     * @return lo stato
     *
     */
    public Boolean getCompletato() { return completato; }

    /**
     * Restituisce l'elenco degli utenti conc ui è condiviso il todo
     *
     * @return la lista degli utenti
     *
     */
    public ArrayList<Utente> getCondivisioni()
    {
        return condivisioni;
    }

    /**
     * Restituisce l'autore del todo
     *
     * @return l'autore
     *
     */
    public Utente getAutore() { return autore; }

    /**
     * Restituisce la bacheca dove è contenuto il todo
     *
     * @return la bacheca
     *
     */
    public Bacheca getBacheca() { return bacheca; }

    /**
     * Restituisce il path dell'immagine collegata al todo
     *
     * @return il path dell'immagine
     *
     */
    public String getImmaginePath()
    {
        return immaginePath;
    }


    /**
     * Imposta l'id del todo
     *
     * @param id il nuovo id
     *
     */
    public void setId(int id) {this.id = id;}

    /**
     * Imposta il titolo del todo
     *
     * @param titolo il nuovo titolo
     *
     */
    public void setTitoloTodo(String titolo)
    {
        this.titoloTodo = titolo;
    }

    /**
     * Imposta la scadenza del todo
     *
     * @param scadenza la nuova scadenza
     *
     */
    public void setScadenza(LocalDate scadenza)
    {
        this.scadenza = scadenza;
    }

    /**
     * Imposta il link legato al todo
     *
     * @param link il nuovo link
     *
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Imposta la descrizione del todo
     *
     * @param descrizioneTodo la nuova descrizione
     *
     */
    public void setDescrizioneTodo(String descrizioneTodo) {
        this.descrizioneTodo = descrizioneTodo;
    }

    /**
     * Imposta l'immagine del todo
     *
     * @param immagine la nuova immagine*
     */
    public void setImmagine(Image immagine) {
        this.immagine = immagine;
    }

    /**
     * Imposta il path dell'immagine del todo
     *
     * @param path il nuovo path
     *
     */
    public void setImmaginePath(String path)
    {
        this.immaginePath = path;
    }

    /**
     * Imposta il colore del todo
     *
     * @param colore il nuovo colore
     *
     */
    public void setColore(Color colore) {
        this.colore = colore;
    }

    /**
     * Imposta la bacheca del todo
     *
     * @param bacheca la nuova bacheca
     *
     */
    public void setBacheca(Bacheca bacheca) {
        this.bacheca = bacheca;
    }

    /**
     * Imposta lo stato del todo
     *
     * @param completato il nuovo stato
     *
     */
    public void setCompletato(Boolean completato) { this.completato = completato; }

    /**
     * Imposta l'autore del todo
     *
     * @param autore il nuovo autore
     *
     */
    public void setAutore(Utente autore) { this.autore = autore; }


    @Override
    public String toString()
    {
        return getTitoloTodo();
    }
}
