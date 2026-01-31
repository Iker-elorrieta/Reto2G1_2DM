package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Horario extends JFrame {
	private static final long serialVersionUID = 1L;
	private JButton botonVolver;
	private JTable tablaVisual;

	public Horario() {
		setTitle("");
		setSize(900, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		String[] dias = { "Hora", "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES" };
		int rows = (20 - 8) + 1; // horas 08:00..20:00
		String[][] tablaHorario = new String[rows][dias.length];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < dias.length; j++) {
				if (j == 0) tablaHorario[i][j] = String.format("%02d:00", 8 + i);
				else tablaHorario[i][j] = "";
			}
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
		tablaVisual = new JTable(modeloTabla);

		// Ajustes visuales
		tablaVisual.setRowHeight(80); // Aumentamos un poco el alto para el texto multi-línea
		tablaVisual.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tablaVisual.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
		tablaVisual.setCellSelectionEnabled(false); // Opcional: evita que se resalten celdas individuales

		JScrollPane panelDesplazable = new JScrollPane(tablaVisual);
		add(panelDesplazable, BorderLayout.CENTER);

		botonVolver = new JButton("⬅ Volver al menú");
		botonVolver.setBackground(new Color(70, 130, 180));
		botonVolver.setForeground(Color.WHITE);
		botonVolver.setFocusPainted(false);

		add(botonVolver, BorderLayout.SOUTH);
	}

	public void setTableModel(DefaultTableModel modeloTabla, String tituloVentana) {
		setTitle(tituloVentana);
		tablaVisual.setModel(modeloTabla);
	}

	public JButton getBtnVolver() {
		return botonVolver;
	}

	public void setBtnVolver(JButton btnVolver) {
		this.botonVolver = btnVolver;
	}
}