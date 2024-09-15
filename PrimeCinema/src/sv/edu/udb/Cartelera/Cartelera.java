package sv.edu.udb.Cartelera;

import sv.edu.udb.RegistroSucursales.RegistroSucursales;
import javax.swing.*;
import java.awt.*;

public class Cartelera extends JFrame {
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

        // Panel para las películas de la cartelera
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
        // Redirige a la vista de sucursales
        SwingUtilities.invokeLater(() -> {
            RegistroSucursales sucursalesFrame = new RegistroSucursales();
            sucursalesFrame.setVisible(true);
        });
    }

    private void buscarPeliculas() {
        JOptionPane.showMessageDialog(this, "Funcionalidad de búsqueda de películas aún no implementada.", "Buscar Películas", JOptionPane.INFORMATION_MESSAGE);
    }

    private void buscarSucursales() {
        JOptionPane.showMessageDialog(this, "Funcionalidad de búsqueda de sucursales aún no implementada.", "Buscar Sucursales", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Cartelera().setVisible(true);
        });
    }
}
