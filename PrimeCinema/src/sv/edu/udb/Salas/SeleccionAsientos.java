package sv.edu.udb.Salas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SeleccionAsientos extends JFrame {
    private JButton[][] asientos;
    private static final int FILAS = 8;
    private static final int COLUMNAS = 5;
    private JButton confirmarButton;
    private JButton desocuparButton;

    public SeleccionAsientos() {
        setTitle("Selección de Asientos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel asientosPanel = new JPanel(new GridLayout(FILAS, COLUMNAS, 5, 5));
        asientos = new JButton[FILAS][COLUMNAS];

        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                asientos[i][j] = new JButton("F" + (i+1) + "C" + (j+1));
                asientos[i][j].setBackground(Color.GREEN);
                asientos[i][j].addActionListener(new AsientoListener());
                asientosPanel.add(asientos[i][j]);
            }
        }

        add(asientosPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        confirmarButton = new JButton("Confirmar Selección");
        confirmarButton.addActionListener(e -> confirmarSeleccion());
        controlPanel.add(confirmarButton);

        desocuparButton = new JButton("Desocupar Todos");
        desocuparButton.addActionListener(e -> desocuparTodos());
        controlPanel.add(desocuparButton);

        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private class AsientoListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton asiento = (JButton) e.getSource();
            if (asiento.getBackground() == Color.GREEN) {
                asiento.setBackground(Color.RED);
            } else {
                asiento.setBackground(Color.GREEN);
            }
        }
    }

    private void confirmarSeleccion() {
        StringBuilder seleccionados = new StringBuilder("Asientos seleccionados:\n");
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (asientos[i][j].getBackground() == Color.RED) {
                    seleccionados.append(asientos[i][j].getText()).append(", ");
                }
            }
        }
        JOptionPane.showMessageDialog(this, seleccionados.toString());
        // Aquí puedes agregar la lógica para registrar la compra en la base de datos
    }

    private void desocuparTodos() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                asientos[i][j].setBackground(Color.GREEN);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SeleccionAsientos().setVisible(true));
    }
}