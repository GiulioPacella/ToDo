package implementazioni.postgres.dao;

import dao.UtenteDAO;
import database.ConnessioneDatabase;
import model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementazione DAO per la gestione degli Utenti in PostgreSQL.
 * Fornisce metodi per aggiungere, recuperare, confrontare gli Utenti
 * dal database
 */
public class UtenteImplementazionePostgreDAO implements UtenteDAO {



    /**
     * Registra un nuovo utente all'interno del database
     *
     * @param utente l'utente da registrare
     *
     */
    @Override
    public void salvaUtente(Utente utente) {
        String sql = "INSERT INTO utente (login, password) VALUES (?, ?)";

        try (Connection connection = ConnessioneDatabase.getConnection();
             PreparedStatement query = connection.prepareStatement(sql)) {

            query.setString(1, utente.getLogin());
            query.setString(2, utente.getPassword());

            query.executeUpdate();



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Controlla se esiste nel database un utente che rispetti l'username e la password inserite all'interno della schermata di login
     *
     * @param username l'username inserita
     * @param password la password inserita
     * @return 1 se esiste, 0 se non esiste
     */
    @Override
    public boolean verificaLogin(String username, String password) {
        String sql = "SELECT COUNT(*) FROM utente WHERE login = ? AND password = ?";

        try (Connection connection = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Restituisce un utente contenuto nel database con un certo username, aggiungendolo al model
     *
     * @param username l'username
     * @return l'utente
     *
     */
    public Utente getUtenteByUsername(String username) {
        Utente utente = null;

        String sqlUtente = "SELECT login, password FROM utente WHERE login = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmtUtente = conn.prepareStatement(sqlUtente)) {

            stmtUtente.setString(1, username);

            try (ResultSet rs = stmtUtente.executeQuery()) {
                if (rs.next()) {
                    utente = new Utente(rs.getString("login"), rs.getString("password"));

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return utente;
    }

}
