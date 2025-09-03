package implementazioni.postgres.dao;

import dao.ToDoDAO;
import database.ConnessioneDatabase;
import model.Bacheca;
import model.ToDo;
import model.Utente;

import javax.imageio.ImageIO;
import java.io.File;
import java.sql.*;
import java.awt.*;

/**
 * Implementazione DAO per la gestione dei ToDo in PostgreSQL.
 * Fornisce metodi per creare, modificare, eliminare e recuperare i ToDo
 * dal database, oltre a popolare le bacheche con i dati persistenti.
 * Si occupa anche di caricare i ToDo condivisi con un utente.
 */
public class ToDoImplementazionePostgreDAO implements ToDoDAO {

    /**
     * Inserisce all'interno del Database un todo
     *
     * @param todo il todo da inserire nel db
     * @param idBacheca l'id della bacheca in cui inserire il todo
     * @param autore l'autore del todo
     *
     */
    @Override
    public void salvaToDo(ToDo todo, int idBacheca, Utente autore) {
        String sql = "INSERT INTO todo (titolo, link, immagine, descrizione, scadenza, colore, autore, idb, stato) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, todo.getTitoloTodo());
            stmt.setString(2, todo.getLink());
            stmt.setString(3, todo.getImmaginePath());
            stmt.setString(4, todo.getDescrizioneTodo());
            stmt.setDate(5, Date.valueOf(todo.getScadenza()));
            String coloreStr = colorToString(todo.getColore());
            stmt.setString(6, coloreStr);
            stmt.setString(7, autore.getLogin());
            stmt.setInt(8, idBacheca);
            stmt.setBoolean(9, todo.getCompletato());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGenerato = generatedKeys.getInt(1);
                    todo.setId(idGenerato);
                } else {
                    throw new SQLException("Creazione todo fallita, nessun ID ottenuto.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /**
     * Metodo che popola le bacheche all'avvio SOLO con i todo creati dall'utente che ha effettuato l'accesso, aggiungendoli al model
     *
     * @param idBacheca id della bacheca da popolare
     * @param bacheca bacheca da popolare
     * @param utenteCorrente utente corrente
     *
     */
    public void popolaBacheca(int idBacheca, Bacheca bacheca, Utente utenteCorrente) {
        String sql = "SELECT id, titolo, stato, link, immagine, descrizione, scadenza, colore, posizione, autore, idb  FROM todo WHERE idb = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idBacheca);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String imagePath = rs.getString("immagine");
                    Image image = null;
                    if (imagePath != null && !imagePath.isBlank()) {
                        File file = new File(imagePath);
                        if (file.exists()) {
                            image = ImageIO.read(file);
                        }
                    }

                    String autoreLogin = rs.getString("autore");
                    Utente autore = new Utente(autoreLogin, "");

                    ToDo t = new ToDo(
                            rs.getString("titolo"),
                            rs.getDate("scadenza").toLocalDate(),
                            rs.getString("link"),
                            rs.getString("descrizione"),
                            image,
                            rs.getString("immagine"),
                            stringToColor(rs.getString("colore")),
                            autore,
                            bacheca,
                            rs.getBoolean("stato")
                    );

                    t.setId(rs.getInt("id"));
                    boolean esiste = bacheca.getTodos().stream()
                            .anyMatch(existing -> existing.getId() == t.getId());
                    if (!esiste) {
                        bacheca.addToDo(t);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    /**
     * Metodo per l'update di un todo nel database dopo la sua modifica
     *
     * @param todo il todo
     * @param idToDo l'id del todo
     *
     */
    public void updateToDo(ToDo todo, int idToDo)
    {
        String sql = "UPDATE todo SET titolo = ?, scadenza = ?, link = ?, descrizione = ?, immagine = ?, colore = ?, stato = ? WHERE id = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, todo.getTitoloTodo());
            stmt.setDate(2, Date.valueOf(todo.getScadenza()));
            stmt.setString(3, todo.getLink());
            stmt.setString(4, todo.getDescrizioneTodo());
            stmt.setString(5, todo.getImmaginePath());
            stmt.setString(6, colorToString(todo.getColore()));
            stmt.setBoolean(7, todo.getCompletato());
            stmt.setInt(8, idToDo);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Metodo per cambiare la bacheca di un todo all'interno del database
     *
     * @param todo il todo
     * @param idToDo l'id del todo
     * @param idB l'id della bacheca di destinazione
     */
    public void cambiaBachecaToDo(ToDo todo, int idToDo, int idB)
    {
        String sql = "UPDATE todo SET idB = ? WHERE id = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idB);
            stmt.setInt(2, idToDo);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Metodo per eliminare tutti i todo di una bacheca all'interno del database
     *
     * @param idB bacheca dove eliminare i todo
     *
     */
    public void svuotaBacheca(int idB)
    {
        String sql = "DELETE FROM todo WHERE idB = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idB);

            stmt.executeUpdate();
        } catch (SQLException e) {
           e.printStackTrace();
        }

    }


    /**
     * Metodo per eliminare un todo dal database
     *
     * @param todo il todo
     * @param idToDo l'id del todo
     *
     */
    public void eliminaToDo(ToDo todo, int idToDo)
    {
        String sql = "DELETE FROM todo WHERE id = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idToDo);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * Restituisce l'autore di un todo conoscendo l'id del todo
     *
     * @param idTodo l'id del todo
     * @return l'autore
     */
    public String getAutoreToDo(int idTodo)
    {

        String autore = null;

        String sql = "SELECT autore FROM todo WHERE id = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTodo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    autore = rs.getString("autore");
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return autore;

    }

    /**
     * Popola una bacheca dell'utente che ha effettuato il login SOLO con i todo che sono stati condivisi con lui, aggiungendoli al model
     *
     * @param utenteCorrente l'utente corrente
     * @param bacheca la bacheca da popolare
     * @param numeroBachecaDest numero della bacheca da cui prendere i todo condivisi (di default: 1 = Università, 2 = Lavoro, 3 = Tempo libero)
     */
    public void popolaTodoCondivisi(Utente utenteCorrente, Bacheca bacheca, int numeroBachecaDest) {
        String sql =
                "SELECT t.*, b.numero as numeroAutore " +
                        "FROM todo t " +
                        "JOIN condivisione c ON t.id = c.idt " +
                        "JOIN bacheca b ON t.idb = b.id " +
                        "WHERE c.ricevente = ? AND b.numero = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utenteCorrente.getLogin());
            stmt.setInt(2, numeroBachecaDest);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ToDo t = new ToDo(
                        rs.getString("titolo"),
                        rs.getDate("scadenza").toLocalDate(),
                        rs.getString("link"),
                        rs.getString("descrizione"),
                        null, // immagine opzionale
                        rs.getString("immagine"),
                        stringToColor(rs.getString("colore")),
                        new Utente(rs.getString("autore"), ""),
                        bacheca,
                        rs.getBoolean("stato")
                );
                t.setId(rs.getInt("id"));

                if (bacheca.getTodos().stream().noneMatch(todo -> todo.getId() == t.getId()))
                    bacheca.addToDo(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    /**
     * Converte una stringa esadecimale che rappresenta un colore nel colore in formato "Color"
     *
     * @param color il colore sotto forma di String
     * @return il colore in formato "Color"
     *
     */
    public static Color stringToColor(String color) {
        return Color.decode(color);
    }


    /**
     * Converte una colore in formato "Color" nella stringa esadecimale corrispondente
     *
     * @param color il colore sotto forma di "Color"
     * @return il colore in formato "String"
     *
     */
    public static String colorToString(Color color) {
        if(color == null) return "#808080";
        return String.format("#%06X", (0xFFFFFF & color.getRGB()));
    }


}
