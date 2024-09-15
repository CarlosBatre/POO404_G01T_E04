package sv.edu.udb.RegistroSucursales;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import sv.edu.udb.Salas.RegistroSalas;

public class RegistroSucursales extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/primecinema";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public RegistroSucursales() {
        setTitle("Sucursales");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(122, 38, 38));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        List<String> sucursales = obtenerSucursales();
        int index = 0;

        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 3; columna++) {
                if (index < sucursales.size()) {
                    JButton sucursalButton = new JButton(sucursales.get(index));
                    sucursalButton.setBackground(Color.LIGHT_GRAY);
                    sucursalButton.setForeground(Color.BLACK);
                    sucursalButton.setFont(new Font("Arial", Font.BOLD, 14));
                    sucursalButton.setFocusPainted(false);
                    sucursalButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    sucursalButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

                    gbc.gridx = columna;
                    gbc.gridy = fila;
                    panel.add(sucursalButton, gbc);
                    index++;

                    sucursalButton.addActionListener(e -> {
                        // Obtener el idSucursal basado en el nombre del botón
                        int idSucursal = obtenerIdSucursalPorNombre(sucursalButton.getText());
                        if (idSucursal != -1) {
                            new RegistroSalas(idSucursal).setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(this, "Sucursal no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                } else {
                    break; // No hay más sucursales, sale del bucle.
                }
            }
        }

        // Añadir el botón de registrar sucursal
        JButton addSucursalButton = new JButton("Registrar Nueva Sucursal");
        addSucursalButton.setBackground(Color.LIGHT_GRAY);
        addSucursalButton.setForeground(Color.BLACK);
        addSucursalButton.setFont(new Font("Arial", Font.BOLD, 14));
        addSucursalButton.setFocusPainted(false);
        addSucursalButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        addSucursalButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addSucursalButton.addActionListener(e -> mostrarFormularioRegistro());

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(addSucursalButton, gbc);

        add(panel);
    }

    private int obtenerIdSucursalPorNombre(String nombreSucursal) {
        int idSucursal = -1;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT idSucursal FROM sucursal WHERE nombre = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nombreSucursal);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        idSucursal = rs.getInt("idSucursal");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al obtener el id de la sucursal.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return idSucursal;
    }

    private List<String> obtenerSucursales() {
        List<String> sucursales = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT nombre FROM sucursal";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String nombre = rs.getString("nombre");
                    sucursales.add(nombre);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al obtener las sucursales de la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return sucursales;
    }

    private void mostrarFormularioRegistro() {
        JFrame registroFrame = new JFrame("Registrar Nueva Sucursal");
        registroFrame.setSize(400, 400);
        registroFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(122, 38, 38));

        // Crear los campos de texto
        JTextField nombreField = new JTextField();
        JTextField nombreGerenteField = new JTextField();
        JTextField numeroTelefonoField = new JTextField();
        JTextField direccionCompletaField = new JTextField();

        // Crear y configurar los labels
        JLabel nombreLabel = new JLabel("Nombre de Sucursal:");
        nombreLabel.setForeground(Color.WHITE);
        nombreLabel.setFont(new Font("Arial", Font.PLAIN, 15));

        JLabel nombreGerenteLabel = new JLabel("Nombre del Gerente:");
        nombreGerenteLabel.setForeground(Color.WHITE);
        nombreGerenteLabel.setFont(new Font("Arial", Font.PLAIN, 15));

        JLabel numeroTelefonoLabel = new JLabel("Número de Teléfono:");
        numeroTelefonoLabel.setForeground(Color.WHITE);
        numeroTelefonoLabel.setFont(new Font("Arial", Font.PLAIN, 15));

        JLabel direccionCompletaLabel = new JLabel("Dirección Completa:");
        direccionCompletaLabel.setForeground(Color.WHITE);
        direccionCompletaLabel.setFont(new Font("Arial", Font.PLAIN, 15));

        // Añadir los componentes al panel
        panel.add(nombreLabel);
        panel.add(nombreField);

        panel.add(nombreGerenteLabel);
        panel.add(nombreGerenteField);

        panel.add(numeroTelefonoLabel);
        panel.add(numeroTelefonoField);

        panel.add(direccionCompletaLabel);
        panel.add(direccionCompletaField);

        // Configurar el botón de registro
        JButton registrarButton = new JButton("Registrar");
        registrarButton.setBackground(Color.LIGHT_GRAY);
        registrarButton.setForeground(Color.BLACK);
        registrarButton.setFont(new Font("Arial", Font.BOLD, 14));
        registrarButton.setFocusPainted(false);
        registrarButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        registrarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registrarButton.addActionListener(e -> registrarSucursal(
                nombreField.getText(),
                nombreGerenteField.getText(),
                numeroTelefonoField.getText(),
                direccionCompletaField.getText()
        ));

        panel.add(new JLabel()); // Espacio vacío
        panel.add(registrarButton);

        // Añadir el panel al frame
        registroFrame.add(panel);
        registroFrame.setVisible(true);
    }

    private void registrarSucursal(String nombre, String nombreGerente, String numeroTelefono, String direccionCompleta) {
        if (nombre.isEmpty() || nombreGerente.isEmpty() || numeroTelefono.isEmpty() || direccionCompleta.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO sucursal (nombre, nombreGerente, numeroTelefono, direccionCompleta) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nombre);
                stmt.setString(2, nombreGerente);
                stmt.setString(3, numeroTelefono);
                stmt.setString(4, direccionCompleta);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Sucursal registrada con éxito.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al registrar la sucursal en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegistroSucursales frame = new RegistroSucursales();
            frame.setVisible(true);
        });
    }
}
