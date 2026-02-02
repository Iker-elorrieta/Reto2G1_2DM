package vista;

import java.awt.*;
import java.util.Date;

import modelo.Centro;
import modelo.Users;
import javax.swing.*;

public class CrearReuniones extends JFrame {
	private static final long serialVersionUID = 1L;

	private JTextField campoTitulo, campoTema, campoAula;
	private JSpinner spinnerDia;
	private JComboBox<String> comboHora;
	private JComboBox<Users> comboEstudiantes;
	private JComboBox<Centro> comboUbicacion;
	private JButton btnCrear, btnVolver;

	public CrearReuniones() {

		setTitle("Crear Nueva Solicitud de Reunión");
		setSize(600, 550);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());

		JPanel panel = new JPanel(null);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

		// Campos
		campoTitulo = new JTextField();
		campoTitulo.setBounds(297, 23, 257, 39);

		campoTema = new JTextField();
		campoTema.setBounds(297, 72, 257, 39);

		// 📅 CALENDARIO
		spinnerDia = new JSpinner(new SpinnerDateModel());
		spinnerDia.setBounds(297, 121, 257, 39);
		JSpinner.DateEditor editor = new JSpinner.DateEditor(spinnerDia, "dd/MM/yyyy");
		spinnerDia.setEditor(editor);

		// ⏰ HORA (selector normal HH:mm)
		comboHora = new JComboBox<>();
		comboHora.setBounds(297, 170, 257, 39);
		// Horas típicas de jornada escolar (puedes cambiar el rango si lo deseas)
		for (int h = 8; h <= 20; h++) {
			String label = String.format("%02d:00", h);
			comboHora.addItem(label);
		}

		campoAula = new JTextField();
		campoAula.setBounds(297, 219, 257, 39);

		// UBICACIÓN
		comboUbicacion = new JComboBox<>();
		comboUbicacion.setBounds(297, 268, 257, 39);
		panel.add(comboUbicacion);

		// Estudiantes
		comboEstudiantes = new JComboBox<>();
		comboEstudiantes.setBounds(297, 317, 257, 39);

		// Labels
		panel.add(crearLabel("Título (Propósito):", 30, 23));
		panel.add(campoTitulo);

		panel.add(crearLabel("Tema (Descripción):", 30, 72));
		panel.add(campoTema);

		panel.add(crearLabel("Día:", 30, 121));
		panel.add(spinnerDia);

		panel.add(crearLabel("Hora (HH:mm):", 30, 170));
		panel.add(comboHora);

		panel.add(crearLabel("Aula:", 30, 219));
		panel.add(campoAula);

		panel.add(crearLabel("Ubicación/Centro:", 30, 268));
		panel.add(comboUbicacion);

		panel.add(crearLabel("Seleccionar Estudiante:", 30, 317));
		panel.add(comboEstudiantes);

		getContentPane().add(panel, BorderLayout.CENTER);

		// Botones
		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
		btnCrear = new JButton("Enviar Solicitud");
		btnCrear.setBackground(new Color(46, 139, 87));
		btnCrear.setForeground(Color.WHITE);

		btnVolver = new JButton("⬅ Volver");

		panelBotones.add(btnVolver);
		panelBotones.add(btnCrear);

		getContentPane().add(panelBotones, BorderLayout.SOUTH);
	}

	private JLabel crearLabel(String texto, int x, int y) {
		JLabel label = new JLabel(texto);
		label.setBounds(x, y, 250, 39);
		label.setFont(new Font("SansSerif", Font.BOLD, 13));
		return label;
	}

	public String getCampoTituloText() {
		return campoTitulo.getText().trim();
	}

	public String getCampoTemaText() {
		return campoTema.getText().trim();
	}

	public String getCampoAulaText() {
		return campoAula.getText().trim();
	}

	public Date getSpinnerFecha() {
		return (Date) spinnerDia.getValue();
	}

	public JButton getBtnCrear() {
		return btnCrear;
	}

	public JButton getBtnVolver() {
		return btnVolver;
	}

	public JComboBox<Centro> getComboUbicacion() {
		return comboUbicacion;
	}

	public void setComboUbicacion(JComboBox<Centro> comboUbicacion) {
		this.comboUbicacion = comboUbicacion;
	}

	public JComboBox<Users> getComboEstudiantes() {
		return comboEstudiantes;
	}

	public void setComboEstudiantes(JComboBox<Users> comboEstudiantes) {
		this.comboEstudiantes = comboEstudiantes;
	}

	public JComboBox<String> getComboHora() {
		return comboHora;
	}

	public void setComboHora(JComboBox<String> comboHora) {
		this.comboHora = comboHora;
	}
}
