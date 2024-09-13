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
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configura el aspecto visual
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        // Panel de inicio de sesión
        loginPanel = createStyledPanel();
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        addComponent(loginPanel, new JLabel("Usuario:"), gbc, 0, 0);
        loginUsername = new JTextField(15);
        addComponent(loginPanel, loginUsername, gbc, 1, 0);

        addComponent(loginPanel, new JLabel("Contraseña:"), gbc, 0, 1);
        loginPassword = new JPasswordField(15);
        addComponent(loginPanel, loginPassword, gbc, 1, 1);

        JButton loginButton = createStyledButton("Iniciar Sesión");
        loginButton.addActionListener(e -> iniciarSesion());
        gbc.gridwidth = 2;
        addComponent(loginPanel, loginButton, gbc, 0, 2);

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
            addComponent(registerPanel, new JLabel(labels[i]), gbc, 0, i);
            addComponent(registerPanel, fields[i], gbc, 1, i);
        }

        JButton registerButton = createStyledButton("Registrarse");
        registerButton.addActionListener(e -> registrar());
        gbc.gridwidth = 2;
        addComponent(registerPanel, registerButton, gbc, 0, labels.length);

        tabbedPane.addTab("Iniciar Sesión", loginPanel);
        tabbedPane.addTab("Registrarse", registerPanel);

        add(tabbedPane);
    }

    private JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 130, 180));
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
                this.dispose(); // Cierra la ventana de login
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales inválidas. Intente de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void registrar() {
        // Verifica si los campos están inicializados
        if (regNombre == null || regLogin == null || regPassword == null ||
                regDui == null || regTelefono == null || regEmail == null || regDireccion == null) {
            JOptionPane.showMessageDialog(this, "Error: Uno o más campos de registro no están inicializados.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Validacion Campos Vacios
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

        // Obtener conexión a la base de datos
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            //Validacion Nombre usuario
            String sqlVerificarUsuario = "SELECT 1 FROM usuario WHERE login = ?";
            PreparedStatement pstmtVerificarUsuario = conn.prepareStatement(sqlVerificarUsuario);
            pstmtVerificarUsuario.setString(1, regLogin.getText());

            ResultSet rsUsuario = pstmtVerificarUsuario.executeQuery();
            if (rsUsuario.next()) {
                JOptionPane.showMessageDialog(this, "Nombre de usuario ya está registrado. Intente con otro.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //Validacion Dui
            String sqlVerificarDui = "SELECT 1 FROM usuario WHERE numeroDui = ?";
            PreparedStatement pstmtVerificarDui = conn.prepareStatement(sqlVerificarDui);
            pstmtVerificarDui.setString(1, regDui.getText());

            ResultSet rs = pstmtVerificarDui.executeQuery();
            if (rs.next()) {
                // Si el DUI ya existe, mostrar un mensaje de error y regresar
                JOptionPane.showMessageDialog(this, "DUI ya está registrado. Intente con otro.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Definir la consulta SQL para insertar datos
            String sql = "INSERT INTO usuario (NombreCompleto, login, contraseña, numeroDui, numeroTelefono, correoElectronico, direccionCompleta) VALUES (?, ?, ?, ?, ?, ?, ?)";
            // Preparar la declaración
            PreparedStatement pstmt = conn.prepareStatement(sql);
            // Establecer los parámetros de la consulta
            pstmt.setString(1, regNombre.getText());
            pstmt.setString(2, regLogin.getText());
            pstmt.setString(3, regPassword.getText());
            pstmt.setString(4, regDui.getText());
            pstmt.setString(5, regTelefono.getText());
            pstmt.setString(6, regEmail.getText());
            pstmt.setString(7, regDireccion.getText());

            // Ejecutar la actualización
            int filasAfectadas = pstmt.executeUpdate();
            // Comprobar el resultado y mostrar un mensaje de éxito o error
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(this, "¡Registro exitoso! Por favor, inicie sesión.");
                limpiarCamposRegistro();
                tabbedPane.setSelectedIndex(0); // Cambia a la pestaña de inicio de sesión
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