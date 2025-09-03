package implementazioni.postgres.dao;

import dao.CondivisioneDAO;
import database.ConnessioneDatabase;
import model.Bacheca;
import model.ToDo;
import model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione DAO per la gestione delle Condivisioni in PostgreSQL.
 * Fornisce metodi per aggiungere, eliminare e recuperare le Condivisioni
 * dal database
 */
public class CondivisioneImplementazionePostgreDAO implements CondivisioneDAO {


    /**
     * Condivide un todo con un utente ricevente, aggiungendo una riga "condivisione" nel database
     * @param idTodo l'id del todo condiviso
     * @param ricevente l'username del ricevente
     *
     */
    public void aggiungiCondivisione(int idTodo, String ricevente) {
        String sql = "INSERT INTO condivisione (idt, ricevente) VALUES (?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection()) {

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idTodo);
                stmt.setString(2, ricevente);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    /**
     * Restituisce l'elenco di utenti che condividono un todo
     *
     * @param idTodo l'id del todo
     * @return la lista di utenti
     */
    public List<Utente> getUtentiCondivisi(int idTodo){
        List<Utente> utenti = new ArrayList<>();

        String sql = "SELECT u.login, u.password " +
                "FROM condivisione c " +
                "JOIN utente u ON c.ricevente = u.login " +
                "WHERE c.idt = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTodo);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String login = rs.getString("login");
                    String password = rs.getString("password");

                    Utente u = new Utente(login, password);

                    utenti.add(u);
                }
            }
        }
        catch (SQLException e) { e.printStackTrace(); }

        return utenti;
    }

    /**
     * Elimina tutti i todo condivisi con l'utente corrente in una sua data bacheca
     *
     * @param bacheca la bacheca da cui voglio eliminare i todo condivisi
     *
     */
    public void eliminaCondivisioniDellaBacheca(Bacheca bacheca) {
        String sql = "DELETE FROM condivisione WHERE idt = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (ToDo todo : bacheca.getTodos()) {
                stmt.setInt(1, todo.getId());
                stmt.addBatch();
            }

            stmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina un todo condiviso da un altro utente dalla bacheca dell'utente ricevente
     *
     * @param idTodo l'id del todo
     * @param loginUtente l'utente ricevente
     */
    public void eliminaCondivisionePerUtente(int idTodo, String loginUtente) {
        String sql = "DELETE FROM condivisione WHERE idt = ? AND ricevente = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTodo);
            stmt.setString(2, loginUtente);

            stmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}