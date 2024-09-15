package sv.edu.udb.Salas;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistroSalas extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/primecinema";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private int idSucursal;

    public RegistroSalas(int idSucursal) {
        this.idSucursal = idSucursal;
        setTitle("Salas de la Sucursal");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        actualizarBotonesSalas();
    }

    private List<String> obtenerSalas() {
        List<String> salas = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT numero FROM sala WHERE idSucursal = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idSucursal);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String numero = rs.getString("numero");
                        salas.add(numero);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al obtener las salas de la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return salas;
    }

    private void mostrarFormularioRegistro() {
        JFrame registroFrame = new JFrame("Registrar Nueva Sala");
        registroFrame.setSize(400, 300);
        registroFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(122, 38, 38));

        JTextField numeroField = new JTextField();

        JLabel numeroLabel = new JLabel("Número de Sala:");
        numeroLabel.setForeground(Color.WHITE);
        numeroLabel.setFont(new Font("Arial", Font.PLAIN, 15));

        panel.add(numeroLabel);
        panel.add(numeroField);

        JButton registrarButton = new JButton("Registrar");
        registrarButton.setBackground(Color.LIGHT_GRAY);
        registrarButton.setForeground(Color.BLACK);
        registrarButton.setFont(new Font("Arial", Font.BOLD, 14));
        registrarButton.setFocusPainted(false);
        registrarButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        registrarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registrarButton.addActionListener(e -> registrarSala(numeroField.getText()));

        panel.add(new JLabel()); // Espacio vacío
        panel.add(registrarButton);

        registroFrame.add(panel);
        registroFrame.setVisible(true);
    }

    private void registrarSala(String numero) {
        if (numero.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete el campo de número de sala.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (contarSalas() >= 6) {
            JOptionPane.showMessageDialog(this, "No se pueden registrar más de 6 salas en esta sucursal.", "Límite Alcanzado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO sala (numero, idSucursal) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, numero);
                stmt.setInt(2, idSucursal);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Sala registrada con éxito.");
                actualizarBotonesSalas();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al registrar la sala en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int contarSalas() {
        int count = 0;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT COUNT(*) FROM sala WHERE idSucursal = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idSucursal);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        count = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al contar las salas de la sucursal.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return count;
    }

    private void actualizarBotonesSalas() {
        getContentPane().removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(122, 38, 38));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        List<String> salas = obtenerSalas();

        int index = 0;
        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 3; columna++) {
                if (index < salas.size()) {
                    JButton salaButton = new JButton("Sala " + salas.get(index));
                    salaButton.setBackground(Color.LIGHT_GRAY);
                    salaButton.setForeground(Color.BLACK);
                    salaButton.setFont(new Font("Arial", Font.BOLD, 14));
                    salaButton.setFocusPainted(false);
                    salaButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    salaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    int finalIndex = index;
                    salaButton.addActionListener(e -> mostrarDetallesSala(salas.get(finalIndex)));

                    gbc.gridx = columna;
                    gbc.gridy = fila;
                    panel.add(salaButton, gbc);
                    index++;
                } else {
                    break;
                }
            }
        }

        JButton addSalaButton = new JButton("Registrar Nueva Sala");
        addSalaButton.setBackground(Color.LIGHT_GRAY);
        addSalaButton.setForeground(Color.BLACK);
        addSalaButton.setFont(new Font("Arial", Font.BOLD, 14));
        addSalaButton.setFocusPainted(false);
        addSalaButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        addSalaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addSalaButton.addActionListener(e -> mostrarFormularioRegistro());

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(addSalaButton, gbc);

        add(panel);
        revalidate();
        repaint();
    }

    private void mostrarDetallesSala(String numeroSala) {
        DetallesSala detallesFrame = new DetallesSala(numeroSala);
        detallesFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Aquí podrías pasar un idSucursal para pruebas.
            new RegistroSalas(1).setVisible(true);
        });
    }
}
