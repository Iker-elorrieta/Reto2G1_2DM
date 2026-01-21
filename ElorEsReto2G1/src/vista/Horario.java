package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import controlador.Controlador;
import modelo.Horarios;

public class Horario extends JFrame {
	private static final long serialVersionUID = 1L;

	public Horario(Controlador controlador, int idProfesor, Menu menu) {

        setTitle("Horario del Profesor");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        List<Horarios> horario = controlador.obtenerHorario(idProfesor);

        
        String[] dias = {"LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES"};

        // 6 horas x 5 días
        String[][] tabla = new String[6][5];
        for (Horarios h : horario) {
            int hora = h.getHora() - 1;
            int col = diaAColumna(h.getDia());

            if (hora < 0 || hora >= 6 || col == -1) continue;

            String texto = "";

            if (h.getModulos() != null && h.getModulos().getNombre() != null) {
                texto = h.getModulos().getNombre();
            } else if (h.getObservaciones() != null) {
                texto = h.getObservaciones();
            } else if (h.getAula() != null && !h.getAula().equals("5.005")) {
                texto = h.getAula();
            }


            tabla[hora][col] = texto;
        }


        

        JTable table = new JTable(tabla, dias);
        table.setRowHeight(60);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        JButton btnVolver = new JButton("⬅ Volver al menú");
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnVolver.setBackground(new Color(70, 130, 180));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);

        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	menu.setVisible(true);
            	setVisible(false);
            	
            }
        });

        add(btnVolver, BorderLayout.SOUTH);
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
}