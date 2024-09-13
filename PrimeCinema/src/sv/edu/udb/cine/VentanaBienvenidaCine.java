package sv.edu.udb.cine;

import sv.edu.udb.RegistroPeliculas.RegistroPeliculas;

import javax.swing.*;
import java.awt.*;

public class VentanaBienvenidaCine extends JFrame {
    public VentanaBienvenidaCine(String nombreCompleto) {
        setTitle("Bienvenido a PrimeCinema");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Agregar un fondo con estilo de cine
                ImageIcon background = new ImageIcon("../../../../../../../../../Login cine/fondo.jpg");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Logo de cine
        ImageIcon logoIcon = new ImageIcon("../../../../../../../../../Login cine/logo.jpg");
        JLabel labelLogo = new JLabel(logoIcon);
        labelLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(labelLogo);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Texto de bienvenida
        JLabel labelBienvenida = new JLabel("¡Bienvenido a PrimeCinema!");
        labelBienvenida.setFont(new Font("Arial", Font.BOLD, 28));
        labelBienvenida.setForeground(Color.YELLOW); // Color cinematográfico
        labelBienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(labelBienvenida);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Nombre del usuario
        JLabel labelNombre = new JLabel(nombreCompleto);
        labelNombre.setFont(new Font("Arial", Font.PLAIN, 20));
        labelNombre.setForeground(Color.WHITE);
        labelNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(labelNombre);

        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Mensaje adicional
        JLabel labelMensaje = new JLabel("Disfruta de nuestras películas y promociones.");
        labelMensaje.setFont(new Font("Arial", Font.ITALIC, 18));
        labelMensaje.setForeground(Color.LIGHT_GRAY);
        labelMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(labelMensaje);

        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Botón de continuar
        JButton botonContinuar = new JButton("Continuar");
        botonContinuar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonContinuar.setBackground(new Color(255, 69, 0)); // Color más intenso
        botonContinuar.setForeground(Color.WHITE);
        botonContinuar.setFont(new Font("Arial", Font.BOLD, 16));
        botonContinuar.setFocusPainted(false);
        botonContinuar.setPreferredSize(new Dimension(150, 40));
        botonContinuar.addActionListener(e -> {
            dispose(); // Cierra la ventana actual
            SwingUtilities.invokeLater(() -> {
                RegistroPeliculas registroPeliculas = new RegistroPeliculas();
                registroPeliculas.setVisible(true);
            });
        });
        panel.add(botonContinuar);

        add(panel);
    }
}