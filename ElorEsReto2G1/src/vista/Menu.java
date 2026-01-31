package vista;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class Menu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel panelContenido;
	private JButton btnPerfil;
	private JButton btnAlumnos;
	private JButton btnConsultarHorario;
	private JButton btnOtrosHorarios;
	private JButton btnCrearReunion;
	private JButton btnVerReuniones;
	private JButton btnDesc;

	public Menu() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 780, 541);
		setTitle("Menú Principal");

		panelContenido = new PanelConFondo("fondo2.jpg");
		panelContenido.setBackground(new Color(245, 245, 245));
		panelContenido.setLayout(null);
		setContentPane(panelContenido);

		// ===== PANEL IZQUIERDO =====
		JPanel panelIzquierdo = new JPanel();
		panelIzquierdo.setBounds(544, 0, 220, 502);
		panelIzquierdo.setLayout(null);
		panelIzquierdo.setBackground(Color.LIGHT_GRAY);
		panelIzquierdo.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
		panelContenido.add(panelIzquierdo);

		btnPerfil = new JButton("⚙");
		btnPerfil.setBackground(Color.WHITE);
		btnPerfil.setBounds(20, 114, 180, 76);
		btnPerfil.setFocusPainted(false);
		panelIzquierdo.add(btnPerfil);

		btnAlumnos = new JButton("👤  Consultar Alumnos");
		btnAlumnos.setBackground(Color.WHITE);
		btnAlumnos.setBounds(20, 201, 180, 145);
		btnAlumnos.setFocusPainted(false);
		panelIzquierdo.add(btnAlumnos);

		JLabel etiquetaPerfil = new JLabel("PERFIL");
		etiquetaPerfil.setBounds(10, 11, 180, 25);
		panelIzquierdo.add(etiquetaPerfil);
		etiquetaPerfil.setFont(new Font("Segoe UI", Font.BOLD, 16));

		btnDesc = new JButton("Desconectar");
		btnDesc.setBackground(Color.WHITE);
		btnDesc.setFocusPainted(false);
		btnDesc.setBounds(20, 371, 180, 56);
		panelIzquierdo.add(btnDesc);

		// ===== PANEL DERECHO =====
		JPanel panelDerecho = new JPanel();
		panelDerecho.setForeground(new Color(255, 255, 255));
		panelDerecho.setBounds(33, 107, 480, 384);
		panelDerecho.setLayout(null);
		panelDerecho.setBackground(new Color(245, 245, 245));
		panelContenido.add(panelDerecho);

		// ===== HORARIO =====
		JPanel panelHorario = new JPanel();
		panelHorario.setBounds(0, 0, 480, 180);
		panelHorario.setLayout(null);
		panelHorario.setBackground(Color.WHITE);
		panelHorario.setBorder(new TitledBorder(new LineBorder(new Color(180, 180, 180)), "📅 Horario",
				TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14)));
		panelDerecho.add(panelHorario);

		btnConsultarHorario = new JButton("Consultar horario");
		btnConsultarHorario.setBounds(40, 31, 180, 138);
		btnConsultarHorario.setBackground(Color.WHITE);
		panelHorario.add(btnConsultarHorario);

		btnOtrosHorarios = new JButton("Consultar otros horarios");
		btnOtrosHorarios.setBackground(Color.WHITE);
		btnOtrosHorarios.setBounds(243, 31, 202, 138);
		panelHorario.add(btnOtrosHorarios);

		// ===== REUNIONES =====
		JPanel panelReuniones = new JPanel();
		panelReuniones.setBounds(0, 204, 480, 180);
		panelReuniones.setLayout(null);
		panelReuniones.setBackground(Color.WHITE);
		panelReuniones.setBorder(new TitledBorder(new LineBorder(new Color(180, 180, 180)), "👥 Reuniones",
				TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14)));
		panelDerecho.add(panelReuniones);

		btnCrearReunion = new JButton("Crear reunión");
		btnCrearReunion.setBackground(Color.WHITE);
		btnCrearReunion.setBounds(40, 29, 180, 140);
		panelReuniones.add(btnCrearReunion);

		btnVerReuniones = new JButton("Ver reuniones");
		btnVerReuniones.setBackground(Color.WHITE);
		btnVerReuniones.setBounds(242, 29, 209, 140);
		panelReuniones.add(btnVerReuniones);

		JLabel etiquetaLogoSuperior = new JLabel("");
		etiquetaLogoSuperior.setBounds(260, 11, 237, 96);
		panelContenido.add(etiquetaLogoSuperior);
		etiquetaLogoSuperior.setIcon(new ImageIcon(getClass().getClassLoader().getResource("logoelo.png")));
	}

	public boolean confirmarCierreSesion() {
		int opcion = JOptionPane.showConfirmDialog(this, "¿Deseas cerrar la sesión?", "Confirmar desconexión",
				JOptionPane.YES_NO_OPTION);
		return opcion == JOptionPane.YES_OPTION;
	}

	public void mostrarMensajeError(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public JButton getBtnPerfil() {
		return btnPerfil;
	}

	public void setBtnPerfil(JButton btnPerfil) {
		this.btnPerfil = btnPerfil;
	}

	public JButton getBtnAlumnos() {
		return btnAlumnos;
	}

	public void setBtnAlumnos(JButton btnAlumnos) {
		this.btnAlumnos = btnAlumnos;
	}

	public JButton getBtnConsultarHorario() {
		return btnConsultarHorario;
	}

	public void setBtnConsultarHorario(JButton btnConsultarHorario) {
		this.btnConsultarHorario = btnConsultarHorario;
	}

	public JButton getBtnOtrosHorarios() {
		return btnOtrosHorarios;
	}

	public void setBtnOtrosHorarios(JButton btnOtrosHorarios) {
		this.btnOtrosHorarios = btnOtrosHorarios;
	}

	public JButton getBtnCrearReunion() {
		return btnCrearReunion;
	}

	public void setBtnCrearReunion(JButton btnCrearReunion) {
		this.btnCrearReunion = btnCrearReunion;
	}

	public JButton getBtnVerReuniones() {
		return btnVerReuniones;
	}

	public void setBtnVerReuniones(JButton btnVerReuniones) {
		this.btnVerReuniones = btnVerReuniones;
	}

	public JButton getBtnDesc() {
		return btnDesc;
	}

	public void setBtnDesc(JButton btnDesc) {
		this.btnDesc = btnDesc;
	}
}
