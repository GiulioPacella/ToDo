package model;

import java.util.ArrayList;

public class Bacheca
{
    public enum titoloBacheca
    {
        Universita,
        Lavoro,
        TempoLibero,
    }
    private titoloBacheca titolo;

    private String descrizioneBacheca;
    private ArrayList<ToDo> todos;


    // Costruttore

    public Bacheca(titoloBacheca titolo, String descrizioneBacheca)
    {
    ArrayList<ToDo> tmp = new ArrayList<>();
    this.titolo = titolo;
    this.descrizioneBacheca = descrizioneBacheca;
    this.todos = tmp;
    }


    // Funzione di stampa

    public void stampaBacheca()
    {
        System.out.println("\n\n");
        System.out.println("model.Bacheca: " + titolo);
        System.out.println("Descrizione: " + descrizioneBacheca + "\n\n");


        for(int i=0; i<this.todos.size(); i++)
        {
          this.todos.get(i).stampaTodo();

        }
    }



    // Getters

    public titoloBacheca getTitolo()
    {
        return titolo;
    }

    public String getDescrizioneBacheca() {
        return descrizioneBacheca;
    }

    public ArrayList<ToDo> getTodos()
    {
        return todos;
    }


    // Setters

    public void setTitolo(titoloBacheca titolo)
    {
        this.titolo = titolo;
    }

    public void setDescrizioneBacheca(String descrizioneBacheca)
    {
        this.descrizioneBacheca = descrizioneBacheca;
    }

    public void setTodos(ArrayList<ToDo> todos)
    {
        this.todos = todos;
    }
}
