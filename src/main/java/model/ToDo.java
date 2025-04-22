import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class ToDo
{
    private String titoloTodo;
    private String scadenza;
    private String link;
    private String descrizioneTodo;
    private String percorsoImmagine;
    private ArrayList<Utente> condivisioni;
    private String colore;
    private Boolean completato;
    private Bacheca bacheca;

    // Definisco il Costruttore

    public ToDo(String titoloTodo, String scadenza, String link, String descrizioneTodo, String immagine, String colore, String listaUtenti, Bacheca bacheca)
    {
    this.titoloTodo = titoloTodo;
    this.scadenza = scadenza;
    this.link = link;
    this.descrizioneTodo = descrizioneTodo;
    this.percorsoImmagine = immagine;
    this.colore = colore;
    ArrayList<Utente> tmp = new ArrayList<>();
    this.condivisioni = tmp;
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
        System.out.println("Immagine:  "+ percorsoImmagine);
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

    public String getScadenza() {
        return scadenza;
    }

    public String getLink() {
        return link;
    }

    public String getDescrizioneTodo()
    {
        return descrizioneTodo;
    }

    public String getImmagine()
    {
        return percorsoImmagine;
    }

    public String getColore()
    {
        return colore;
    }

    public ArrayList<Utente> getListaUtenti()
    {
        return condivisioni;
    }




    // Setters

    public void setTitoloTodo(String titolo)
    {
        this.titoloTodo = titolo;
    }

    public void setScadenza(String scadenza)
    {
        this.scadenza = scadenza;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescrizioneTodo(String descrizioneTodo) {
        this.descrizioneTodo = descrizioneTodo;
    }

    public void setImmagine(String immagine) {
        this.percorsoImmagine = immagine;
    }

    public void setColore(String colore) {
        this.colore = colore;
    }

    public void setBacheca(Bacheca bacheca) {
        this.bacheca = bacheca;
    }



    // Funziona per completare un To Do

    public void completa()
    {
        this.completato = true;
    }



    // Funzione per spostare un To Do in un'altra Bacheca

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
}
