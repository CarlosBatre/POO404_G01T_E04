package sv.edu.udb.Cartelera;

import sv.edu.udb.RegistroSucursales.RegistroSucursales;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;

public class Cartelera extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/primecinema";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public Cartelera() {
        setTitle("Cartelera");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(122, 38, 38));

        JLabel titleLabel = new JLabel("Cartelera", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel peliculasPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        JPanel peliculaPanel = new JPanel();
        peliculaPanel.setBackground(Color.GRAY);
        peliculaPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        peliculaPanel.setPreferredSize(new Dimension(200, 300));
        peliculasPanel.add(peliculaPanel);
        mainPanel.add(peliculasPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton verSucursalesButton = createStyledButton("Ver Sucursales");
        verSucursalesButton.addActionListener(e -> mostrarSucursales());
        buttonPanel.add(verSucursalesButton);

        JButton buscarPeliculasButton = createStyledButton("Buscar Películas");
        buscarPeliculasButton.addActionListener(e -> buscarPeliculas());
        buttonPanel.add(buscarPeliculasButton);

        JButton buscarSucursalesButton = createStyledButton("Buscar Sucursales");
        buscarSucursalesButton.addActionListener(e -> buscarSucursales());
        buttonPanel.add(buscarSucursalesButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void mostrarSucursales() {
        SwingUtilities.invokeLater(() -> {
            RegistroSucursales sucursalesFrame = new RegistroSucursales();
            sucursalesFrame.setVisible(true);
        });
    }

    private void buscarPeliculas() {
        String nombrePelicula = JOptionPane.showInputDialog(this, "Ingrese el nombre de la película:");
        if (nombrePelicula != null && !nombrePelicula.isEmpty()) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "SELECT p.nombre AS pelicula, p.genero, p.clasificacion, p.formato, " +
                        "p.valorFuncionAdulto, p.valorFuncionTerceraEdad, p.horaProyeccion, " +
                        "s.nombre AS sucursal, sa.numero AS numeroSala " +
                        "FROM pelicula p " +
                        "LEFT JOIN sala sa ON 1=1 " +
                        "LEFT JOIN sucursal s ON sa.idSucursal = s.idSucursal " +
                        "WHERE p.nombre LIKE ?";

                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, "%" + nombrePelicula + "%");
                ResultSet rs = pstmt.executeQuery();

                StringBuilder mensaje = new StringBuilder("Resultados para '" + nombrePelicula + "':\n\n");
                boolean encontrado = false;
                Map<String, Set<String>> sucursalSalas = new HashMap<>();

                while (rs.next()) {
                    if (!encontrado) {
                        encontrado = true;
                        mensaje.append("Película: ").append(rs.getString("pelicula")).append("\n")
                                .append("Género: ").append(rs.getString("genero")).append("\n")
                                .append("Clasificación: ").append(rs.getString("clasificacion")).append("\n")
                                .append("Formato: ").append(rs.getString("formato")).append("\n")
                                .append("Precio Adulto: $").append(rs.getDouble("valorFuncionAdulto")).append("\n")
                                .append("Precio Tercera Edad: $").append(rs.getDouble("valorFuncionTerceraEdad")).append("\n")
                                .append("Hora de Proyección: ").append(rs.getTime("horaProyeccion")).append("\n\n")
                                .append("Salas disponibles:\n");
                    }

                    String sucursal = rs.getString("sucursal");
                    String sala = rs.getString("numeroSala");
                    if (sucursal != null && sala != null) {
                        sucursalSalas.computeIfAbsent(sucursal, k -> new HashSet<>()).add(sala);
                    }
                }

                if (encontrado) {
                    for (Map.Entry<String, Set<String>> entry : sucursalSalas.entrySet()) {
                        mensaje.append("Sucursal: ").append(entry.getKey()).append("\n")
                                .append("Salas: ").append(String.join(", ", entry.getValue())).append("\n\n");
                    }
                    JOptionPane.showMessageDialog(this, mensaje.toString(), "Resultados de búsqueda", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontraron resultados para la película.", "Búsqueda de Películas", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al buscar la película: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarSucursales() {
        String nombreSucursal = JOptionPane.showInputDialog(this, "Ingrese el nombre de la sucursal:");
        if (nombreSucursal != null && !nombreSucursal.isEmpty()) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "SELECT s.nombre AS sucursal, s.nombreGerente, s.numeroTelefono, s.direccionCompleta, " +
                        "sa.numero AS numeroSala, p.nombre AS pelicula " +
                        "FROM sucursal s " +
                        "LEFT JOIN sala sa ON s.idSucursal = sa.idSucursal " +
                        "LEFT JOIN pelicula p ON 1=1 " +
                        "WHERE s.nombre LIKE ?";

                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, "%" + nombreSucursal + "%");
                ResultSet rs = pstmt.executeQuery();

                StringBuilder mensaje = new StringBuilder("Información de la sucursal '" + nombreSucursal + "':\n\n");
                boolean encontrado = false;
                Map<Integer, Set<String>> salaPeliculas = new HashMap<>();

                while (rs.next()) {
                    if (!encontrado) {
                        encontrado = true;
                        mensaje.append("Sucursal: ").append(rs.getString("sucursal")).append("\n")
                                .append("Gerente: ").append(rs.getString("nombreGerente")).append("\n")
                                .append("Teléfono: ").append(rs.getString("numeroTelefono")).append("\n")
                                .append("Dirección: ").append(rs.getString("direccionCompleta")).append("\n\n")
                                .append("Salas y películas disponibles:\n");
                    }

                    int numeroSala = rs.getInt("numeroSala");
                    String pelicula = rs.getString("pelicula");
                    if (pelicula != null) {
                        salaPeliculas.computeIfAbsent(numeroSala, k -> new HashSet<>()).add(pelicula);
                    }
                }

                if (encontrado) {
                    for (Map.Entry<Integer, Set<String>> entry : salaPeliculas.entrySet()) {
                        mensaje.append("Sala ").append(entry.getKey()).append(":\n")
                                .append("Películas: ").append(String.join(", ", entry.getValue())).append("\n\n");
                    }
                    JOptionPane.showMessageDialog(this, mensaje.toString(), "Resultados de búsqueda", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontraron resultados para la sucursal.", "Búsqueda de Sucursales", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al buscar la sucursal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Cartelera().setVisible(true);
        });
    }
}