package sv.edu.udb.Login;

import sv.edu.udb.cine.VentanaBienvenidaCine;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SistemaLoginSwing extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/primecinema";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private JTabbedPane tabbedPane;
    private JPanel loginPanel, registerPanel;
    private JTextField loginUsername, loginPassword;
    private JTextField regNombre, regLogin, regPassword, regDui, regTelefono, regEmail, regDireccion;

    public SistemaLoginSwing() {
        setTitle("PrimeCinema - Iniciar Sesión / Registrarse");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 15));


        loginPanel = createStyledPanel();
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);


        JLabel userLabel = new JLabel("Usuario:", JLabel.CENTER);
        userLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userLabel.setForeground(Color.WHITE);
        addComponent(loginPanel, userLabel, gbc, 0, 1);
        loginUsername = new JTextField(15);
        addComponent(loginPanel, loginUsername, gbc, 0, 2);


        JLabel passwordLabel = new JLabel("Contraseña:", JLabel.CENTER);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 16));
        passwordLabel.setForeground(Color.WHITE);
        addComponent(loginPanel, passwordLabel, gbc, 0, 3);
        loginPassword = new JPasswordField(15);
        addComponent(loginPanel, loginPassword, gbc, 0, 4);


        gbc.gridwidth = 2;
        gbc.gridy = 5;
        addComponent(loginPanel, (JComponent) Box.createVerticalStrut(20), gbc, 0, 5);


        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.setBackground(new Color(0, 0, 0));
        loginButton.setForeground(Color.BLACK);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setHorizontalAlignment(SwingConstants.CENTER);
        loginButton.addActionListener(e -> iniciarSesion());
        gbc.gridwidth = 2;
        addComponent(loginPanel, loginButton, gbc, 0, 6);


        // Panel de registro
        registerPanel = createStyledPanel();
        registerPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Inicializa los campos de texto
        regNombre = new JTextField(15);
        regLogin = new JTextField(15);
        regPassword = new JTextField(15);
        regDui = new JTextField(15);
        regTelefono = new JTextField(15);
        regEmail = new JTextField(15);
        regDireccion = new JTextField(15);

        String[] labels = {"Nombre Completo:", "Usuario:", "Contraseña:", "DUI:", "Teléfono:", "Email:", "Dirección:"};
        JTextField[] fields = {regNombre, regLogin, regPassword, regDui, regTelefono, regEmail, regDireccion};

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Arial", Font.BOLD, 16));
            label.setForeground(Color.WHITE);  // Color blanco
            addComponent(registerPanel, label, gbc, 0, i);
            addComponent(registerPanel, fields[i], gbc, 1, i);
        }
        gbc.gridwidth = 2;
        gbc.gridy = labels.length;
        addComponent(registerPanel, (JComponent) Box.createVerticalStrut(60), gbc, 0, labels.length);

        JButton registerButton = createStyledButton("Registrarse");
        registerButton.setForeground(Color.BLACK);
        registerButton.addActionListener(e -> registrar());
        gbc.gridwidth = 2;
        addComponent(registerPanel, registerButton, gbc, 0, labels.length);


        tabbedPane.addTab("Iniciar Sesión", loginPanel);
        tabbedPane.addTab("Registrarse", registerPanel);

        add(tabbedPane);
    }

    private JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(122, 38, 38));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 0, 0));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        return button;
    }

    private void addComponent(JPanel panel, JComponent component, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(component, gbc);
    }

    private void iniciarSesion() {
        String username = loginUsername.getText();
        String password = loginPassword.getText();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM usuario WHERE login = ? AND contraseña = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String nombreCompleto = rs.getString("NombreCompleto");
                SwingUtilities.invokeLater(() -> {
                    new VentanaBienvenidaCine(nombreCompleto).setVisible(true);
                });
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales inválidas. Intente de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void registrar() {

        if (regNombre == null || regLogin == null || regPassword == null ||
                regDui == null || regTelefono == null || regEmail == null || regDireccion == null) {
            JOptionPane.showMessageDialog(this, "Error: Uno o más campos de registro no están inicializados.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (regNombre.getText().trim().isEmpty() ||
                regLogin.getText().trim().isEmpty() ||
                regPassword.getText().trim().isEmpty() ||
                regDui.getText().trim().isEmpty() ||
                regTelefono.getText().trim().isEmpty() ||
                regEmail.getText().trim().isEmpty() ||
                regDireccion.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Complete todos los campos!! Por favor :).", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {


            String sqlVerificarUsuario = "SELECT 1 FROM usuario WHERE login = ?";
            PreparedStatement pstmtVerificarUsuario = conn.prepareStatement(sqlVerificarUsuario);
            pstmtVerificarUsuario.setString(1, regLogin.getText());

            ResultSet rsUsuario = pstmtVerificarUsuario.executeQuery();
            if (rsUsuario.next()) {
                JOptionPane.showMessageDialog(this, "Nombre de usuario ya está registrado. Intente con otro.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            String sqlVerificarDui = "SELECT 1 FROM usuario WHERE numeroDui = ?";
            PreparedStatement pstmtVerificarDui = conn.prepareStatement(sqlVerificarDui);
            pstmtVerificarDui.setString(1, regDui.getText());

            ResultSet rs = pstmtVerificarDui.executeQuery();
            if (rs.next()) {

                JOptionPane.showMessageDialog(this, "DUI ya está registrado. Intente con otro.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }




















            String sql = "INSERT INTO usuario (NombreCompleto, login, contraseña, numeroDui, numeroTelefono, correoElectronico, direccionCompleta) VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, regNombre.getText());
            pstmt.setString(2, regLogin.getText());
            pstmt.setString(3, regPassword.getText());
            pstmt.setString(4, regDui.getText());
            pstmt.setString(5, regTelefono.getText());
            pstmt.setString(6, regEmail.getText());
            pstmt.setString(7, regDireccion.getText());


            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(this, "¡Registro exitoso! Por favor, inicie sesión.");
                limpiarCamposRegistro();
                tabbedPane.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "El registro falló. Intente de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCamposRegistro() {
        regNombre.setText("");
        regLogin.setText("");
        regPassword.setText("");
        regDui.setText("");
        regTelefono.setText("");
        regEmail.setText("");
        regDireccion.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SistemaLoginSwing frame = new SistemaLoginSwing();
            frame.setVisible(true);
        });
    }
}