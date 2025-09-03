package controller;
import javax.swing.*;

import implementazioni.postgres.dao.BachecaImplementazionePostgreDAO;
import implementazioni.postgres.dao.CondivisioneImplementazionePostgreDAO;
import implementazioni.postgres.dao.ToDoImplementazionePostgreDAO;
import implementazioni.postgres.dao.UtenteImplementazionePostgreDAO;
import dao.BachecaDAO;
import dao.CondivisioneDAO;
import dao.ToDoDAO;
import dao.UtenteDAO;
import model.*;
import gui.*;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  Rappresenta il controller, che si occupa della comunicazione tra Model, View e Database
 */
public class Controller
{
    final List<Utente> utenti;
    private Utente utenteCorrente;

    final JFrame frame;

    private UtenteDAO utenteDAO;
    private ToDoDAO   todoDAO;
    private BachecaDAO bachecaDAO;
    private CondivisioneDAO condivisioneDAO;


    /**
     * Costruttore della classe controller
     *
     * @param utenti lista utenti del model
     */
    public Controller(List<Utente> utenti)
    {
        this.utenteDAO = new UtenteImplementazionePostgreDAO();
        this.todoDAO = new ToDoImplementazionePostgreDAO();
        this.bachecaDAO = new BachecaImplementazionePostgreDAO();
        this.condivisioneDAO = new CondivisioneImplementazionePostgreDAO();

        this.utenti = utenti;

        frame = new JFrame("ToDo");
        frame.setSize(450, 300);
    }

    /**
     * Avvia l'applicazione
     */
    public void startTodo()
    {
        LoginView loginView = new LoginView(this);
        frame.setContentPane(loginView.$$$getRootComponent$$$());
        frame.setVisible(true);
        frame.setSize(450, 300);
    }


    /**
     * Controlla le credenziali inserite dall'utente per verificare il login
     *
     * @param username l'username
     * @param password la password
     */
    public void checkLogin(String username, String password) {


        if(utenteDAO.verificaLogin(username, password))
        {
            utenteCorrente = utenteDAO.getUtenteByUsername(username);

            List<Bacheca> bacheche = bachecaDAO.getBachecheByUtente(username);

            utenteCorrente.setBacheca1(bacheche.get(0));
            utenteCorrente.setBacheca2(bacheche.get(1));
            utenteCorrente.setBacheca3(bacheche.get(2));

            caricaDati(utenteCorrente);
            showHome(utenteCorrente);

        }
        else
        {
            JOptionPane.showMessageDialog(null,
                    "Username o password errati. Riprova!",
                    "Errore login",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Mostra la schermata Home
     *
     * @param utente l'utente
     */
    public  void showHome(Utente utente)
    {
        HomeView homeView = new HomeView(this, utente);
        frame.setContentPane(homeView.getRootPanel());
        frame.revalidate();
        frame.repaint();
        frame.setSize(1200, 600);
    }

    /**
     * Mostra la schermata di Creazione di un todo
     *
     * @param utente  l'utente
     * @param bacheca la bacheca
     */
    public void showCreation(Utente utente, Bacheca bacheca)
    {
        JFrame createFrame = new JFrame("Crea ToDo");
        CreateView createView = new CreateView(this, utente, bacheca);
        createFrame.setContentPane(createView.$$$getRootComponent$$$());
        createFrame.setSize(400, 500);
        createFrame.setLocationRelativeTo(frame); // centra rispetto alla Home
        createFrame.setVisible(true);

    }

    /**
     * Mostra la schermata di modifica di un todo
     *
     * @param utente  l'utente
     * @param bacheca la bacheca contenente il todo
     * @param toDo    il todo
     */
    public void showModify(Utente utente, Bacheca bacheca, ToDo toDo)
    {
        JFrame createFrame = new JFrame("Modifica ToDo");
        ModifyView modifyView = new ModifyView(this, utente, bacheca, toDo);
        createFrame.setContentPane(modifyView.$$$getRootComponent$$$());
        createFrame.setSize(600, 500);
        createFrame.setLocationRelativeTo(frame);
        createFrame.setVisible(true);
    }



    /**
     * Aggiunge un utente al sistema
     *
     * @param username l'username
     * @param password la password
     * @return l'utente aggiunto
     */
    public Utente aggiungiUtente(String username, String password) {

        Utente utente = new Utente(username, password);
        utenteDAO.salvaUtente(utente);

        List<Bacheca> bachecheDb = bachecaDAO.getBachecheByUtente(username);
        for (Bacheca bDb : bachecheDb) {
            switch (bDb.getTitolo()) {
                case "Università":
                    utente.getBacheca1().setId(bDb.getId());
                    break;
                case "Lavoro":
                    utente.getBacheca2().setId(bDb.getId());
                    break;
                case "Tempo Libero":
                    utente.getBacheca3().setId(bDb.getId());
                    break;
                default: break;
            }
        }

        return utente;
    }

    /**
     * Aggiorna la bacheca quando viene effettuata una modifica sul suo titolo o sulla sua descrizione
     *
     * @param bacheca   la bacheca
     * @param nuovoNome il nuovo nome
     * @param nuovaDesc la nuova descrizione
     */
    public void aggiornaBacheca(Bacheca bacheca, String nuovoNome, String nuovaDesc)
    {
        bacheca.setTitolo(nuovoNome);
        bacheca.setDescrizioneBacheca(nuovaDesc);

        bachecaDAO.updateBacheca(bacheca, bacheca.getId());
    }


    /**
     * Restituisce la bacheca di un utente
     *
     * @param numeroBacheca il numero della bacheca che voglio
     * @return la bacheca corrispondente a quel numero
     */
    public Bacheca getBachecaCorrente(int numeroBacheca)
    {
        if(numeroBacheca == 1)
        { return utenteCorrente.getBacheca1(); }
        if(numeroBacheca == 2)
        {return utenteCorrente.getBacheca2(); }
        if(numeroBacheca == 3)
        { return utenteCorrente.getBacheca3(); }
        return null;
    }


    /**
     * Cerca un todo dal titolo e se lo trova apre la sua schermata di modifica
     *
     * @param titolo il titolo
     */
    public void searchToDo(String titolo)
    {
        int found = 0;

        for(ToDo todo : utenteCorrente.getBacheca1().getTodos())
        {
            if(todo.getTitoloTodo().equals(titolo))
            {
                showModify(utenteCorrente, utenteCorrente.getBacheca1(), todo);
                found = 1;
            }
        }

        for(ToDo todo : utenteCorrente.getBacheca2().getTodos())
        {
            if(todo.getTitoloTodo().equals(titolo))
            {
                showModify(utenteCorrente, utenteCorrente.getBacheca2(), todo);
                found = 1;
            }
        }

        for(ToDo todo : utenteCorrente.getBacheca3().getTodos())
        {
            if(todo.getTitoloTodo().equals(titolo))
            {
                showModify(utenteCorrente, utenteCorrente.getBacheca3(), todo);
                found = 1;
            }
        }

        if( found == 0)
        { JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Nessun ToDo trovato con questo nome"); }
    }


    /**
     * Restituisce l'utente recuperandolo dal database in base al suo username
     *
     * @param username l'username
     * @return l'utente
     */
    public Utente getUtenteByUsername(String username) {
       return utenteDAO.getUtenteByUsername(username);
    }


    /**
     * Aggiunge una condivisione
     *
     * @param destinatario  l'utente con cui voglio condividere il todo
     * @param todo          il todo
     */
    public void aggiungiCondivisione(Utente destinatario, ToDo todo)
    {
        List<Bacheca> bacheche = bachecaDAO.getBachecheByUtente(destinatario.getLogin());
        destinatario.setBacheca1(bacheche.get(0));
        destinatario.setBacheca2(bacheche.get(1));
        destinatario.setBacheca3(bacheche.get(2));

        condivisioneDAO.aggiungiCondivisione(todo.getId(), destinatario.getLogin());


    }



    /**
     * Crea un nuovo todo e aggiorna la home per renderlo subito visibile
     *
     */
    public void createToDo(String titoloTodo, LocalDate scadenza, String link, String descrizioneTodo, Image immagine, String immaginepath, Color colore, Utente autore, Bacheca bacheca, Boolean completato) {
        if (titoloTodo.isEmpty()) {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Inserire un titolo valido");
            return;
        }
        ToDo nuovo = new ToDo(titoloTodo, scadenza, link, descrizioneTodo, immagine, immaginepath, colore, autore, bacheca, completato);
        todoDAO.salvaToDo(nuovo, bacheca.getId(), utenteCorrente);

        showHome(utenteCorrente);
    }


    /**
     *  Metodo per la modifica di una o piu' caratteristiche di un todo
     *
     */
    public void modificaToDo(ToDo todo,
                             String nuovoTitolo,
                             String nuovaDescrizione,
                             String nuovoLink,
                             LocalDate nuovaScadenza,
                             Color nuovoColore,
                             Image nuovaImmagine,
                             String nuovaImmaginePath) {



        if (nuovoTitolo != null && !nuovoTitolo.trim().isEmpty()) {
            todo.setTitoloTodo(nuovoTitolo);
        }

        todo.setImmaginePath(nuovaImmaginePath);
        todo.setDescrizioneTodo(nuovaDescrizione);
        todo.setLink(nuovoLink);
        todo.setScadenza(nuovaScadenza);
        todo.setColore(nuovoColore);
        todo.setImmagine(nuovaImmagine);

        todoDAO.updateToDo(todo, todo.getId());
    }


    /**
     * Metodo che imposta lo stato di un todo
     *
     * @param todo il todo
     * @param stato lo stato che voglio impostare
     *
     */
    public void setToDoCompletato(ToDo todo, Boolean stato)
    {
        todo.setCompletato(stato);
    }



    /**
     * Metodo per eliminare un todo, elimina prima la condivisione se è un todo condiviso e poi lo elimina definitivamente. Inoltre aggiorna la home per rendere la modifica subito visualizzabile
     *
     * @param todo il todo
     * @param bacheca la bacheca da cui rimuovere il todo
     * @param utente l'utente corrente
     *
     */
    public void eliminaToDo(ToDo todo, Bacheca bacheca, Utente utente) {


        condivisioneDAO.eliminaCondivisionePerUtente(todo.getId(), utente.getLogin());
        todoDAO.eliminaToDo(todo, todo.getId());

        if (bacheca != null) {
            bacheca.getTodos().remove(todo);
        }

        showHome(utente);
    }

    /**
     * Mostra la schermata delle condivisioni di un todo
     *
     * @param utenteCorrente  l'utente corrente
     * @param bachecaCorrente  la bacheca contenente il todo
     * @param todoCorrente    il todo
     */
    public void showCondivisioni(Utente utenteCorrente, Bacheca bachecaCorrente, ToDo todoCorrente)
    {
        JFrame createFrame = new JFrame("Condivisioni ToDo");
        ShareView shareView = new ShareView(this, utenteCorrente, todoCorrente, bachecaCorrente);
        createFrame.setContentPane(shareView.$$$getRootComponent$$$());
        createFrame.setSize(600, 500);
        createFrame.setLocationRelativeTo(frame);
        createFrame.setVisible(true);

    }

    /**
     * Restituisce l'elenco di utenti che condividono un todo
     *
     * @param todo the todo
     * @return la lista di utenti
     */
    public List<Utente> getUtentiCondivisi(ToDo todo)
    {
        return condivisioneDAO.getUtentiCondivisi(todo.getId());
    }

    /**
     * Controlla se la persona che sta cercando di effettuare una modifica sulle condivisioni è l'autore del todo
     *
     * @param autore      l' autore
     * @param richiedente il richiedente
     * @return 1 se lo è, 0 se non lo è
     */
    public boolean checkAutore(Utente autore, Utente richiedente)
    {
        if(autore.getLogin().equals(richiedente.getLogin()))
        {return Boolean.TRUE;}
        else
        {return Boolean.FALSE;}
    }

    /**
     * Elimina una condivisione di un todo verso un utente destinatario
     *
     * @param destinatario  l'utente destinatario
     * @param todo          il todo
     * @param numeroBacheca il numero della bacheca dove è contenuto il todo
     */
    public void rimuoviCondivisione(Utente destinatario, ToDo todo, int numeroBacheca) {
        switch (numeroBacheca) {
            case 1:
                destinatario.getBacheca1().getTodos().remove(todo);
                todo.getCondivisioni().remove(destinatario);
                condivisioneDAO.eliminaCondivisionePerUtente(todo.getId(), destinatario.getLogin());
                break;
            case 2:
                destinatario.getBacheca2().getTodos().remove(todo);
                todo.getCondivisioni().remove(destinatario);
                condivisioneDAO.eliminaCondivisionePerUtente(todo.getId(), destinatario.getLogin());
                break;
            case 3:
                destinatario.getBacheca3().getTodos().remove(todo);
                todo.getCondivisioni().remove(destinatario);
                condivisioneDAO.eliminaCondivisionePerUtente(todo.getId(), destinatario.getLogin());
                break;

            default:
        }

    }

    /**
     * Restituisce una lista di todo dell'utente corrente in scadenza entro una certa data
     *
     * @param utente     l'utente corrente
     * @param dataLimite la data
     * @return la lista di todo
     */
    public List<ToDo> getToDoEntroData(Utente utente, LocalDate dataLimite) {
        List<ToDo> risultati = new ArrayList<>();

        for (Bacheca b : Arrays.asList(utente.getBacheca1(), utente.getBacheca2(), utente.getBacheca3())) {
            for (ToDo t : b.getTodos()) {
                if (t.getScadenza() != null && !t.getScadenza().isAfter(dataLimite) && !t.getScadenza().isBefore(LocalDate.now()) && Boolean.TRUE.equals(!t.getCompletato()))
                {
                    risultati.add(t);
                }
            }
        }
        return risultati;
    }

    /**
     * Mostra la schermata dei todo in scadenza entro una certa data
     *
     * @param scadenze     i todo in scadenza entro la data
     * @param dataInserita la data
     */
    public void showExpiring(List<ToDo> scadenze, LocalDate dataInserita)
    {
        JFrame createFrame = new JFrame("ToDo in scadenza");
        ExpiringView expiringView = new ExpiringView(scadenze, dataInserita);
        createFrame.setContentPane(expiringView.$$$getRootComponent$$$());
        createFrame.setSize(600, 500);
        createFrame.setLocationRelativeTo(frame);
        createFrame.setVisible(true);

    }

    /**
     * Carica i dati dell'utente nel momento del login
     *
     * @param utente l'utente corrente
     */
    public void caricaDati(Utente utente) {

        todoDAO.popolaBacheca(utente.getBacheca1().getId(), utente.getBacheca1(), utente);
        todoDAO.popolaBacheca(utente.getBacheca2().getId(), utente.getBacheca2(), utente);
        todoDAO.popolaBacheca(utente.getBacheca3().getId(), utente.getBacheca3(), utente);

        todoDAO.popolaTodoCondivisi(utente, utente.getBacheca1(), 1);
        todoDAO.popolaTodoCondivisi(utente, utente.getBacheca2(), 2);
        todoDAO.popolaTodoCondivisi(utente, utente.getBacheca3(), 3);
    }

    /**
     * Salva la posizione del todo una volta che è stata cambiata la sua bacheca di appartenenza
     *
     * @param todo il todo
     * @param bacheca la bacheca di destinazione
     */
    public void salvaPosizione(ToDo todo, Bacheca bacheca)
    {
        todo.setBacheca(bacheca);

        if (todo.getAutore().getLogin().equals(utenteCorrente.getLogin())) {
            todoDAO.cambiaBachecaToDo(todo, todo.getId(), bacheca.getId());
        }
    }


    /**
     * Verifica che la persona che sta spostando un todo da una bacheca a un'altra sia il proprietario del todo
     *
     * @param todo il todo
     * @param utenteCorrente l'utente che sta provando a effettuare lo spostamento
     * @return 1 se lo è, 0 se non lo è
     */
    public boolean checkAutorePerSpostamento(ToDo todo, Utente utenteCorrente) {
        if (todo.getAutore().getLogin().equals(utenteCorrente.getLogin())) {
            return true;
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "Non puoi spostare questo ToDo perché non sei l'autore.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }

    /**
     * Metodo che elimina una bacheca
     *
     * @param bacheca la bacheca
     */
    public void eliminaBacheca(Bacheca bacheca)
    {
        condivisioneDAO.eliminaCondivisioniDellaBacheca(bacheca);
        todoDAO.svuotaBacheca(bacheca.getId());
        bacheca.getTodos().clear();
    }


    /**
     * Restituisce l'autore di un todo
     *
     * @param idTodo l'id del todo
     * @return l'autore del todo
     */
    public String getAutoreToDo(int idTodo)
    {
    return todoDAO.getAutoreToDo(idTodo);
    }

}
