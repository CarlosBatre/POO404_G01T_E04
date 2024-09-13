package sv.edu.udb.RegistroPeliculas;

import sv.edu.udb.RegistroSucursales.RegistroSucursales;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class Pelicula {
    private String nombre;
    private String genero;
    private String clasificacion;
    private String formato;
    double valorFuncionTerceraEdad;
    double valorFuncionAdulto;

    public Pelicula(String nombre, String genero, String clasificacion, String formato) {
        this.nombre = nombre;
        this.genero = genero;
        this.clasificacion = clasificacion;
        this.formato = formato;
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

    @Override
    public String toString() {
        return "Nombre: " + nombre + "\nGénero: " + genero + "\nClasificación: " + clasificacion +
                "\nFormato: " + formato + "\nValor Función Tercera Edad: $" + valorFuncionTerceraEdad +
                "\nValor Función Adulto: $" + valorFuncionAdulto;
    }
}

public class RegistroPeliculas extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/primecinema";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private JTextField nombreField, generoField, clasificacionField;
    private JComboBox<String> formatoCombo;

    public RegistroPeliculas() {
        setTitle("Registro de Películas");
        setSize(400, 400);
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

        addComponent(panel, new JLabel("Nombre:"), gbc, 0, 0);
        nombreField = new JTextField(15);
        addComponent(panel, nombreField, gbc, 1, 0);

        addComponent(panel, new JLabel("Género:"), gbc, 0, 1);
        generoField = new JTextField(15);
        addComponent(panel, generoField, gbc, 1, 1);

        addComponent(panel, new JLabel("Clasificación:"), gbc, 0, 2);
        clasificacionField = new JTextField(15);
        addComponent(panel, clasificacionField, gbc, 1, 2);

        addComponent(panel, new JLabel("Formato:"), gbc, 0, 3);
        formatoCombo = new JComboBox<>(new String[]{"Tradicional", "3D"});
        addComponent(panel, formatoCombo, gbc, 1, 3);

        JButton registrarButton = createStyledButton("Registrar Película");
        registrarButton.addActionListener(e -> registrarPelicula());
        gbc.gridwidth = 2;
        addComponent(panel, registrarButton, gbc, 0, 4);

        JButton mostrarButton = createStyledButton("Mostrar Cartelera");
        mostrarButton.addActionListener(e -> mostrarCartelera());
        gbc.gridwidth = 2;
        addComponent(panel, mostrarButton, gbc, 0, 5);

        add(panel);
    }

    private JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.LIGHT_GRAY);
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

        if (nombre.isEmpty() || genero.isEmpty() || clasificacion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Pelicula pelicula = new Pelicula(nombre, genero, clasificacion, formato);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO pelicula (nombre, genero, clasificacion, formato, valorFuncionTerceraEdad, valorFuncionAdulto) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nombre);
                stmt.setString(2, genero);
                stmt.setString(3, clasificacion);
                stmt.setString(4, formato);
                stmt.setDouble(5, pelicula.valorFuncionTerceraEdad);
                stmt.setDouble(6, pelicula.valorFuncionAdulto);
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
        List<Pelicula> peliculas = obtenerPeliculas();

        JFrame carteleraFrame = new JFrame("Cartelera");
        carteleraFrame.setSize(800, 600);
        carteleraFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Cartelera", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel peliculasPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        peliculasPanel.setBackground(Color.WHITE);

        for (int i = 0; i < 3; i++) {
            JPanel peliculaPanel = new JPanel();
            peliculaPanel.setBackground(Color.LIGHT_GRAY);
            peliculaPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            peliculaPanel.setPreferredSize(new Dimension(200, 300));

            if (i < peliculas.size()) {
                Pelicula pelicula = peliculas.get(i);
                JLabel nombreLabel = new JLabel(pelicula.getNombre(), SwingConstants.CENTER);
                nombreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                peliculaPanel.add(nombreLabel);

                if (i == 1) {
                    peliculaPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                }
            }

            peliculasPanel.add(peliculaPanel);
        }

        mainPanel.add(peliculasPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
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

        carteleraFrame.add(mainPanel);
        carteleraFrame.setVisible(true);
    }

    private List<Pelicula> obtenerPeliculas() {
        List<Pelicula> peliculas = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM pelicula";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String genero = rs.getString("genero");
                    String clasificacion = rs.getString("clasificacion");
                    String formato = rs.getString("formato");
                    peliculas.add(new Pelicula(nombre, genero, clasificacion, formato));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al obtener las películas de la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return peliculas;
    }

    private void mostrarSucursales() {

            RegistroSucursales sucursalesFrame = new RegistroSucursales();
            sucursalesFrame.setVisible(true);
        }



    private void buscarPeliculas() {

        JOptionPane.showMessageDialog(this, "Funcionalidad de búsqueda de películas aún no implementada.", "Buscar Películas", JOptionPane.INFORMATION_MESSAGE);
    }

    private void buscarSucursales() {

        JOptionPane.showMessageDialog(this, "Funcionalidad de búsqueda de sucursales aún no implementada.", "Buscar Sucursales", JOptionPane.INFORMATION_MESSAGE);
    }

    private void limpiarCampos() {
        nombreField.setText("");
        generoField.setText("");
        clasificacionField.setText("");
        formatoCombo.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegistroPeliculas frame = new RegistroPeliculas();
            frame.setVisible(true);
        });
    }
}