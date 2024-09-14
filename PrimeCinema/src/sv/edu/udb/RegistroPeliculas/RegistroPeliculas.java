package sv.edu.udb.RegistroPeliculas;
import sv.edu.udb.Cartelera.Cartelera;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

class Pelicula {
    private String nombre;
    private String genero;
    private String clasificacion;
    private String formato;
    private String horaProyeccion;
    double valorFuncionTerceraEdad;
    double valorFuncionAdulto;


    public Pelicula(String nombre, String genero, String clasificacion, String formato, String horaProyeccion) {
        this.nombre = nombre;
        this.genero = genero;
        this.clasificacion = clasificacion;
        this.formato = formato;
        this.horaProyeccion = horaProyeccion;
        setValores();
    }

    private void setValores() {
        if (formato.equalsIgnoreCase("Tradicional")) {
            this.valorFuncionTerceraEdad = 3.90;
            this.valorFuncionAdulto = 5.00;
        } else if (formato.equalsIgnoreCase("3D")) {
            this.valorFuncionTerceraEdad = 5.60;
            this.valorFuncionAdulto = 6.55;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public String getGenero() {
        return genero;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public String getFormato() {
        return formato;
    }

    public String getHoraProyeccion() {
        return horaProyeccion;
    }


    @Override
    public String toString() {
        return "Nombre: " + nombre + "\nGénero: " + genero + "\nClasificación: " + clasificacion +
                "\nFormato: " + formato  + "\nHora de Proyección: " + horaProyeccion +
                "\nValor Función Tercera Edad: $" + valorFuncionTerceraEdad +
                "\nValor Función Adulto: $" + valorFuncionAdulto;
    }
}

public class RegistroPeliculas extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/primecinema";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private JTextField nombreField, generoField, clasificacionField, horaProyeccionField;
    private JComboBox<String> formatoCombo;

    public RegistroPeliculas() {
        setTitle("Registro de Películas");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel panel = createStyledPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        addComponent(panel, createStyledLabel("Nombre:"), gbc, 0, 0);
        nombreField = new JTextField(15);
        addComponent(panel, nombreField, gbc, 1, 0);

        addComponent(panel, createStyledLabel("Género:"), gbc, 0, 1);
        generoField = new JTextField(15);
        addComponent(panel, generoField, gbc, 1, 1);

        addComponent(panel, createStyledLabel("Clasificación:"), gbc, 0, 2);
        clasificacionField = new JTextField(15);
        addComponent(panel, clasificacionField, gbc, 1, 2);

        addComponent(panel, createStyledLabel("Formato:"), gbc, 0, 3);
        formatoCombo = new JComboBox<>(new String[]{"Tradicional", "3D"});
        formatoCombo.setFont(new Font("Arial", Font.BOLD, 16));
        formatoCombo.setForeground(Color.BLACK);
        addComponent(panel, formatoCombo, gbc, 1, 3);

        addComponent(panel, createStyledLabel("Hora de Proyección (HH:mm):"), gbc, 0, 4);
        horaProyeccionField = new JTextField(15);
        addComponent(panel, horaProyeccionField, gbc, 1, 4);

        JButton registrarButton = createStyledButton("Registrar Película");
        registrarButton.setForeground(Color.BLACK);
        registrarButton.addActionListener(e -> registrarPelicula());
        gbc.gridwidth = 2;
        addComponent(panel, registrarButton, gbc, 0, 5);

        JButton mostrarButton = createStyledButton("Mostrar Cartelera");
        mostrarButton.setForeground(Color.BLACK);
        mostrarButton.addActionListener(e -> mostrarCartelera());
        gbc.gridwidth = 2;
        addComponent(panel, mostrarButton, gbc, 0, 6);

        add(panel);
    }
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(122, 38, 38));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
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

    private void addComponent(JPanel panel, JComponent component, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(component, gbc);
    }

    private void registrarPelicula() {
        String nombre = nombreField.getText();
        String genero = generoField.getText();
        String clasificacion = clasificacionField.getText();
        String formato = (String) formatoCombo.getSelectedItem();
        String horaProyeccion = horaProyeccionField.getText();

        if (nombre.isEmpty() || genero.isEmpty() || clasificacion.isEmpty() || horaProyeccion.isEmpty() || !horaProyeccion.matches("\\d{2}:\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos y asegúrese de que la hora esté en formato HH:mm.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Pelicula pelicula = new Pelicula(nombre, genero, clasificacion, formato, horaProyeccion);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO pelicula (nombre, genero, clasificacion, formato, valorFuncionTerceraEdad, valorFuncionAdulto, horaProyeccion) VALUES (?, ?, ?, ?, ?, ?,?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nombre);
                stmt.setString(2, genero);
                stmt.setString(3, clasificacion);
                stmt.setString(4, formato);
                stmt.setDouble(5, pelicula.valorFuncionTerceraEdad);
                stmt.setDouble(6, pelicula.valorFuncionAdulto);
                stmt.setTime(7, Time.valueOf(horaProyeccion + ":00"));
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Película registrada con éxito.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al registrar la película en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        limpiarCampos();
    }

    private void mostrarCartelera() {
        Cartelera carteleraFrame = new Cartelera();
        carteleraFrame.setVisible(true);
    }

    /*private List<Pelicula> obtenerPeliculas() {
        List<Pelicula> peliculas = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM pelicula";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String genero = rs.getString("genero");
                    String clasificacion = rs.getString("clasificacion");
                    String formato = rs.getString("formato");
                    String horaProyeccion = rs.getString("horaProyeccion");
                    peliculas.add(new Pelicula(nombre, genero, clasificacion, formato, horaProyeccion));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al obtener las películas de la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return peliculas;
    }*/

    private void limpiarCampos() {
        nombreField.setText("");
        generoField.setText("");
        clasificacionField.setText("");
        formatoCombo.setSelectedIndex(0);
        horaProyeccionField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegistroPeliculas frame = new RegistroPeliculas();
            frame.setVisible(true);
        });
    }
}