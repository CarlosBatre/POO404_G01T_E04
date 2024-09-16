package sv.edu.udb.Salas;

import sv.edu.udb.Cartelera.Cartelera;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DetallesSala extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/primecinema";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private String numeroSala;
    private JTextArea movieDetailsArea;
    private JButton[][] asientos;
    private static final int FILAS = 8;
    private static final int COLUMNAS = 5;

    public DetallesSala(String numeroSala) {
        this.numeroSala = numeroSala;
        setTitle("Detalles de la Sala " + numeroSala);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(122, 38, 38));

        JLabel titleLabel = new JLabel("Sala " + numeroSala, SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        movieDetailsArea = new JTextArea(10, 40);
        movieDetailsArea.setEditable(false);
        movieDetailsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        movieDetailsArea.setBackground(new Color(200, 200, 200));
        JScrollPane scrollPane = new JScrollPane(movieDetailsArea);
        mainPanel.add(scrollPane, BorderLayout.WEST);

        JPanel asientosPanel = new JPanel(new GridLayout(FILAS, COLUMNAS, 5, 5));
        asientos = new JButton[FILAS][COLUMNAS];
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                asientos[i][j] = new JButton("F" + (i+1) + "C" + (j+1));
                asientos[i][j].setBackground(Color.GREEN);
                asientos[i][j].addActionListener(e -> toggleAsiento((JButton)e.getSource()));
                asientosPanel.add(asientos[i][j]);
            }
        }
        mainPanel.add(asientosPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton comprarButton = new JButton("Comprar Entradas");
        comprarButton.addActionListener(e -> comprarEntradas());
        JButton desocuparButton = new JButton("Desocupar Todos");
        desocuparButton.addActionListener(e -> desocuparTodos());
        controlPanel.add(comprarButton);
        controlPanel.add(desocuparButton);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        add(mainPanel);

        cargarDetallesPelicula();
    }

    private void toggleAsiento(JButton asiento) {
        if (asiento.getBackground() == Color.GREEN) {
            asiento.setBackground(Color.RED);
        } else {
            asiento.setBackground(Color.GREEN);
        }
    }

    private void cargarDetallesPelicula() {
        String movieDetails = obtenerDetallesPelicula();
        movieDetailsArea.setText(movieDetails);
    }

    private String obtenerDetallesPelicula() {
        StringBuilder details = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM pelicula ORDER BY RAND() LIMIT 1";
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
                            .append("Valor Funcion Tercera Edad: $").append(String.format("%.2f", valorFuncionTerceraEdad)).append("\n")
                            .append("Valor Funcion Adulto: $").append(String.format("%.2f", valorFuncionAdulto)).append("\n")
                            .append("Hora de Proyección: ").append(horaProyeccion).append("\n");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error al obtener los detalles de la película.";
        }
        return details.toString();
    }

    private void comprarEntradas() {
        StringBuilder asientosSeleccionados = new StringBuilder();
        int count = 0;
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (asientos[i][j].getBackground() == Color.RED) {
                    asientosSeleccionados.append(asientos[i][j].getText()).append(", ");
                    count++;
                }
            }
        }

        if (count == 0) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione al menos un asiento.");
            return;
        }

        int option = JOptionPane.showConfirmDialog(this,
                "¿Confirma la compra de " + count + " entrada(s) para la Sala " + numeroSala + "?\nAsientos: " + asientosSeleccionados.toString(),
                "Confirmar Compra",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {

            String peliculaActual = obtenerDetallesPelicula();


            double precioEntrada = 5.00;
            double totalDinero = count * precioEntrada;


            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "INSERT INTO ventas (numeroSala, pelicula, cantidadEntradas, totalDinero) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, numeroSala);
                    stmt.setString(2, peliculaActual);
                    stmt.setInt(3, count);
                    stmt.setDouble(4, totalDinero);
                    stmt.executeUpdate();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al registrar la venta.");
                return;
            }

            JOptionPane.showMessageDialog(this,
                    "Compra realizada: " + count + " entrada(s) para la Sala " + numeroSala + "\nTotal: $" + String.format("%.2f", totalDinero),
                    "Compra Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);


            generarReporte();


            this.dispose();
            SwingUtilities.invokeLater(() -> new Cartelera().setVisible(true));
        }
    }


    private void desocuparTodos() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                asientos[i][j].setBackground(Color.GREEN);
            }
        }
    }
    private void generarReporte() {
        String sql = "SELECT numeroSala, SUM(totalDinero) AS totalIngresos " +
                "FROM ventas GROUP BY numeroSala ORDER BY totalIngresos DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            StringBuilder reporte = new StringBuilder("Reporte de Ingresos por Sala:\n\n");

            while (rs.next()) {
                String sala = rs.getString("numeroSala");
                double ingresos = rs.getDouble("totalIngresos");
                reporte.append("Sala: ").append(sala).append(", Ingresos: $").append(ingresos).append("\n");
            }

            JOptionPane.showMessageDialog(this, reporte.toString(), "Reporte de Ingresos", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al generar el reporte.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DetallesSala("1").setVisible(true));
    }
}