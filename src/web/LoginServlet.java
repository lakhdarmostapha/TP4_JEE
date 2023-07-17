package web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;




public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Configurer les informations de connexion à la base de données
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_mvc_cat";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Traitement pour la méthode GET
        response.getWriter().println("La méthode GET n'est pas autorisée pour cette URL.");
        // Traitement Test sur paramtetre sur session
        // if true despatcher to index.do
        HttpSession session = request.getSession();
        String Us = (String) session.getAttribute("username");
        String Pa = (String) session.getAttribute("password");
        if (authenticate(Us, Pa)) {
            // Rediriger vers la page index.do
            response.sendRedirect("index.do");
        }
        // else dispatcher to Login
        else {
            // Rediriger vers une page d'erreur d'authentification si les informations sont incorrectes
            response.sendRedirect("login.jsp");
        }




    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Vérifier les informations d'authentification
        if (authenticate(username, password)) {
            // Créer une session et stocker l'identifiant de l'utilisateur
            HttpSession session = request.getSession();
            session.setAttribute("username", username);

            // Rediriger vers la page index.do
            response.sendRedirect("index.do");
        } else {
            // Rediriger vers une page d'erreur d'authentification si les informations sont incorrectes
            response.sendRedirect("login.jsp?error=auth_failed");
        }
    }

    private boolean authenticate(String username, String password) {
        try {
            // Charger le pilote JDBC de MySQL
            Class.forName("com.mysql.jdbc.Driver");

            // Établir une connexion à la base de données
            Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            // Préparer une requête pour vérifier les informations d'authentification
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            // Exécuter la requête
            ResultSet rs = pstmt.executeQuery();

            // Vérifier si des résultats ont été renvoyés
            boolean success = rs.next();

            // Fermer les ressources
            rs.close();
            pstmt.close();
            conn.close();

            return success;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

