package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel; 

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
        String[][] tablaHorario = new String[6][5];

        for (Horarios bloqueHorario : listaHorario) {
            int hora = bloqueHorario.getHora() - 1;
            int columna = diaAColumna(bloqueHorario.getDia());

            if (hora < 0 || hora >= 6 || columna == -1) continue;
            StringBuilder texto = new StringBuilder();

            if (bloqueHorario.getNombreModulo() != null) {
                String nombre = bloqueHorario.getNombreModulo().trim().toLowerCase();

           
                if (nombre.contains("Programación multimedia y dispositivos móviles") ||
                    nombre.contains("programación multimedia y dispositivos móviles")) {
                    texto.append("<b>PMDM</b>");
                } else {
                    texto.append("<b>").append(bloqueHorario.getNombreModulo()).append("</b>");
                }
            }



            if (bloqueHorario.getAula() != null) {
                if (texto.length() > 0) texto.append("<br>");
                texto.append("Aula: ").append(bloqueHorario.getAula());
            }

            if (bloqueHorario.getObservaciones() != null) {
                if (texto.length() > 0) texto.append("<br>");
                texto.append(bloqueHorario.getObservaciones());
            }
            
            

            String textoCelda = texto.length() > 0 ? texto.toString() : "Libre";

            tablaHorario[hora][columna] =
           "<html><body style='width: 100px; text-align: center;'>" + textoCelda  + "</body></html>";

        }
        // 1. CREAR MODELO NO EDITABLE
        DefaultTableModel modeloTabla = new DefaultTableModel(tablaHorario, dias) {
            private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false; // Esto bloquea la edición de todas las celdas
            }
        };

        // 2. ASIGNAR MODELO A LA TABLA
        JTable tablaVisual = new JTable(modeloTabla);
        
        // Ajustes visuales
        tablaVisual.setRowHeight(80); // Aumentamos un poco el alto para el texto multi-línea
        tablaVisual.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaVisual.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        tablaVisual.setCellSelectionEnabled(false); // Opcional: evita que se resalten celdas individuales

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

    public JButton getBtnVolver() { return botonVolver; }
    public void setBtnVolver(JButton btnVolver) { this.botonVolver = btnVolver; }
}