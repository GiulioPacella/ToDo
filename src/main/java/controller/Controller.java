package controller;
import javax.swing.*;

import model.*;
import gui.*;

import java.util.List;

public class Controller
{
    // Utenti che scorre nella verifica di login e password

    private List<Utente> utenti;

    // Utente entrato nel sistema

    private Utente utenteCorrente;
    private Bacheca bachecaCorrente;

    private JFrame frame;
    private LoginView loginView;
    private HomeView homeView;
    private CreateView createView;
    private ModifyView modifyView;
    // Aggiungere altre view qui!


    // Costruttore
    public Controller(List<Utente> utenti)
    {
        this.utenti = utenti;

        frame = new JFrame("ToDo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 300);
    }

    public void startTodo()
    {
        loginView = new LoginView(this);
        frame.setContentPane(loginView.$$$getRootComponent$$$());
        frame.setVisible(true);
    }


    public void checkLogin(JFrame LoginView, String username, String password) {
        for (Utente utente : utenti)
        {
            if (utente.getLogin().equals(username) && utente.getPassword().equals(password))
            {
                utenteCorrente = utente;
                break;
            }
        }

        if (utenteCorrente != null)
        {
            showHome(utenteCorrente);
        }
        else
        {
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Accesso negato");
        }

    }

    public  void showHome(Utente utente)
    {
        homeView = new HomeView(this, utente);
        frame.setContentPane(homeView.getRootPanel());
        frame.revalidate();
        frame.repaint();
        frame.setSize(1100, 600);
    }

    public void showCreation(Utente utente, Bacheca bacheca)
    {
        JFrame createFrame = new JFrame("Crea ToDo");
        CreateView createView = new CreateView(this, utente, bacheca);
        createFrame.setContentPane(createView.$$$getRootComponent$$$());
        createFrame.setSize(400, 500);
        createFrame.setLocationRelativeTo(frame); // centra rispetto alla Home
        createFrame.setVisible(true);

    }

    public void showModify(Utente utente, Bacheca bacheca, ToDo toDo)
    {
        JFrame createFrame = new JFrame("Modifica ToDo");
        ModifyView modifyView = new ModifyView(this, utente, bacheca, toDo);
        createFrame.setContentPane(modifyView.$$$getRootComponent$$$());
        createFrame.setSize(600, 500);
        createFrame.setLocationRelativeTo(frame); // centra rispetto alla Home
        createFrame.setVisible(true);


    }

    public void shareTodo(ToDo todo, String ToUser, Bacheca.titoloBacheca titolo)
    {
        for(Utente utente : utenti)
        {
            if(ToUser.equals(utente.getLogin()))
            {
                if(titolo.equals(Bacheca.titoloBacheca.Universita))
                { utente.getUniversita().getTodos().add(todo);}
                if(titolo.equals(Bacheca.titoloBacheca.Lavoro))
                { utente.getLavoro().getTodos().add(todo);}
                if(titolo.equals(Bacheca.titoloBacheca.TempoLibero))
                { utente.getTempoLibero().getTodos().add(todo); }
            }
        }

    }

}
