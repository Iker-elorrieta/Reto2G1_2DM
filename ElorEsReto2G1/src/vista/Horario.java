package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import modelo.Horarios;

public class Horario extends JFrame {
    private static final long serialVersionUID = 1L;
    private JButton botonVolver;

    public Horario(List<Horarios> listaHorario, String tituloVentana) {

        setTitle(tituloVentana);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        
        String[] dias = {"LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES"};

        // 6 horas x 5 días
        String[][] tablaHorario = new String[6][5];
        for (Horarios bloqueHorario : listaHorario) {
            int hora = bloqueHorario.getHora() - 1;
            int columna = diaAColumna(bloqueHorario.getDia());

            if (hora < 0 || hora >= 6 || columna == -1) {
                continue;
            }

            String textoCelda = "";

            if (bloqueHorario.getModulos() != null && bloqueHorario.getModulos().getNombre() != null) {
                textoCelda = bloqueHorario.getModulos().getNombre();
            } else if (bloqueHorario.getObservaciones() != null) {
                textoCelda = bloqueHorario.getObservaciones();
            } else if (bloqueHorario.getAula() != null && !bloqueHorario.getAula().equals("5.005")) {
                textoCelda = bloqueHorario.getAula();
            }


            tablaHorario[hora][columna] = textoCelda;
        }


        

        JTable tablaVisual = new JTable(tablaHorario, dias);
        tablaVisual.setRowHeight(60);
        tablaVisual.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tablaVisual.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));

        JScrollPane panelDesplazable = new JScrollPane(tablaVisual);
        add(panelDesplazable, BorderLayout.CENTER);

        botonVolver = new JButton("⬅ Volver al menú");
        botonVolver.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botonVolver.setBackground(new Color(70, 130, 180));
        botonVolver.setForeground(Color.WHITE);
        botonVolver.setFocusPainted(false);

        add(botonVolver, BorderLayout.SOUTH);
    }

    private int diaAColumna(String dia) {
        return switch (dia.toUpperCase()) {
            case "LUNES" -> 0;
            case "MARTES" -> 1;
            case "MIERCOLES" -> 2;
            case "JUEVES" -> 3;
            case "VIERNES" -> 4;
            default -> -1;
        };
    }

    public JButton getBtnVolver() {
        return botonVolver;
    }

    public void setBtnVolver(JButton btnVolver) {
        this.botonVolver = btnVolver;
    }
}