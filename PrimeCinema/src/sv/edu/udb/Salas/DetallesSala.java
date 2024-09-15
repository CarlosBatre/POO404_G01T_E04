package sv.edu.udb.Salas;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Random;

public class DetallesSala extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/primecinema";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private String numeroSala;

    public DetallesSala(String numeroSala) {
        this.numeroSala = numeroSala;
        setTitle("Detalles de la Sala " + numeroSala);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(122, 38, 38));

        // Title
        JLabel titleLabel = new JLabel("Sala " + numeroSala, SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Movie details
        JTextArea movieDetailsArea = new JTextArea(10, 40);
        movieDetailsArea.setEditable(false);
        movieDetailsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        movieDetailsArea.setBackground(new Color(200, 200, 200));

        JScrollPane scrollPane = new JScrollPane(movieDetailsArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Load and display movie details
        String movieDetails = obtenerDetallesPelicula();
        movieDetailsArea.setText(movieDetails);

        add(panel);
    }

    private String obtenerDetallesPelicula() {
        StringBuilder details = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM pelicula ORDER BY RAND() LIMIT 1"; // Get a random movie
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String genero = rs.getString("genero");
                    String clasificacion = rs.getString("clasificacion");
                    String formato = rs.getString("formato");
                    double valorFuncionTerceraEdad = rs.getDouble("valorFuncionTerceraEdad");
                    double valorFuncionAdulto = rs.getDouble("valorFuncionAdulto");
                    String horaProyeccion = rs.getString("horaProyeccion");

                    details.append("Nombre: ").append(nombre).append("\n")
                            .append("Género: ").append(genero).append("\n")
                            .append("Clasificación: ").append(clasificacion).append("\n")
                            .append("Formato: ").append(formato).append("\n")
                            .append("Valor Funcion Tercera Edad: ").append(valorFuncionTerceraEdad).append("\n")
                            .append("Valor Funcion Adulto: ").append(valorFuncionAdulto).append("\n")
                            .append("Hora de Proyección: ").append(horaProyeccion).append("\n");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error al obtener los detalles de la película.";
        }
        return details.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Ejemplo con sala número 1
            new DetallesSala("1").setVisible(true);
        });
    }
}
