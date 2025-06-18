package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ToDo
{
    private String titoloTodo;
    private LocalDate scadenza;
    private String link;
    private String descrizioneTodo;
    private Image immagine;
    private ArrayList<Utente> condivisioni;
    private Color colore;
    private Boolean completato;
    private Bacheca bacheca;

    // Definisco il Costruttore

    public ToDo(String titoloTodo, LocalDate scadenza, String link, String descrizioneTodo, Image immagine, Color colore, ArrayList<Utente> condivisioni, Bacheca bacheca)
    {
    this.titoloTodo = titoloTodo;
    this.scadenza = scadenza;
    this.link = link;
    this.descrizioneTodo = descrizioneTodo;
    this.immagine = immagine;
    this.colore = colore;
    this.condivisioni = new ArrayList<>();
    this.completato = false;
    this.bacheca = bacheca;

    bacheca.getTodos().add(this);
    }


   // Funzione di stampa

    public void stampaTodo()
    {
        System.out.println("Titolo:  "+ titoloTodo);
        System.out.println("Scadenza:  "+ scadenza);
        System.out.println("Link:  "+ link);
        System.out.println("Descrizione:  "+ descrizioneTodo);
        System.out.println("Immagine:  ");
        System.out.println("Colore:  "+ colore);
        System.out.print("Condiviso con:  ");
        for(int i = 0; i<condivisioni.size(); i++)
        {
            System.out.print(condivisioni.get(i).getLogin());
        }
        System.out.println("\nCompletato:  "+ completato + "\n\n");
    }





    // Getters

    public String getTitoloTodo() {
        return titoloTodo;
    }

    public LocalDate getScadenza() {
        return scadenza;
    }

    public String getLink() {
        return link;
    }

    public String getDescrizioneTodo()
    {
        return descrizioneTodo;
    }

    public Image getImmagine()
    {
        return immagine;
    }

    public Color getColore()
    {
        return colore;
    }

    public Boolean getCompletato() { return completato; }

    public ArrayList<Utente> getListaUtenti()
    {
        return condivisioni;
    }




    // Setters

    public void setTitoloTodo(String titolo)
    {
        this.titoloTodo = titolo;
    }

    public void setScadenza(LocalDate scadenza)
    {
        this.scadenza = scadenza;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescrizioneTodo(String descrizioneTodo) {
        this.descrizioneTodo = descrizioneTodo;
    }

    public void setImmagine(Image immagine) {
        this.immagine = immagine;
    }

    public void setColore(Color colore) {
        this.colore = colore;
    }

    public void setBacheca(Bacheca bacheca) {
        this.bacheca = bacheca;
    }

    public void setCompletato(Boolean completato) { this.completato = completato; }


    // Funziona per completare un To Do

    public void completa()
    {
        this.completato = true;
    }



    // Funzione per spostare un To Do in un'altra model.Bacheca

    public void cambiaBacheca(Bacheca nuovaBacheca)
    {
        this.bacheca.getTodos().remove(this);
        this.bacheca = nuovaBacheca;
        this.bacheca.getTodos().add(this);

    }

    // Funzione per la condivisione di un To Do

    public void condividiTodo(Utente utente)
    {
        this.condivisioni.add(utente);
    }

    @Override
    public String toString()
    {
        return getTitoloTodo();
    }
}
