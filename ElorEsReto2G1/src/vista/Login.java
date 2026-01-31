package vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel panelContenido;
	private JTextField campoUsuario;
	private JPasswordField campoContrasena;
	private JButton btnLogin;
	private boolean contrasenaVisible;

	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 450);
		setLocationRelativeTo(null);
		setResizable(false);

		// PANEL PRINCIPAL CON IMAGEN DE FONDO
		panelContenido = new JPanel() {

			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image img = new ImageIcon(getClass().getClassLoader().getResource("fondo2.jpg")).getImage();
				g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
			}
		};
		panelContenido.setLayout(null);
		setContentPane(panelContenido);

		// PANEL SEMITRANSPARENTE
		JPanel panelDifuminado = new JPanel();
		panelDifuminado.setBackground(new Color(255, 255, 255, 150));
		panelDifuminado.setBounds(250, 80, 300, 260);
		panelDifuminado.setLayout(null);
		panelDifuminado.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 180), 2));
		panelContenido.add(panelDifuminado);

		// TÍTULO
		JLabel lblTitulo = new JLabel("Acceso Profesores");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBounds(30, 20, 240, 30);
		panelDifuminado.add(lblTitulo);

		// USUARIO
		JLabel lblUsu = new JLabel("Usuario:");
		lblUsu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblUsu.setBounds(30, 80, 240, 20);
		panelDifuminado.add(lblUsu);

		campoUsuario = new JTextField();
		campoUsuario.setBounds(30, 105, 240, 28);
		campoUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panelDifuminado.add(campoUsuario);

		// PASSWORD
		JLabel lblCntr = new JLabel("Contraseña:");
		lblCntr.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblCntr.setBounds(30, 150, 240, 20);
		panelDifuminado.add(lblCntr);

		campoContrasena = new JPasswordField();
		campoContrasena.setBounds(30, 176, 240, 28);
		campoContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panelDifuminado.add(campoContrasena);

		JButton botonVer = new JButton("");
		botonVer.setBounds(242, 176, 28, 27);
		panelDifuminado.add(botonVer);
		botonVer.setFocusable(false);
		botonVer.setBorderPainted(false);
		botonVer.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ojo.jpg")));
		botonVer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evento) {
				contrasenaVisible = !contrasenaVisible;
				campoContrasena.setEchoChar(contrasenaVisible ? (char) 0 : '◉');
			}
		});

		// BOTÓN LOGIN
		btnLogin = new JButton("Entrar");
		btnLogin.setBounds(94, 215, 120, 35);
		panelDifuminado.add(btnLogin);
		btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnLogin.setFocusPainted(false);
		btnLogin.setBackground(new Color(30, 144, 255));
		btnLogin.setForeground(Color.WHITE);

	}

	public String getUsuario() {
		return campoUsuario.getText().trim();
	}

	public String getContrasena() {
		return new String(campoContrasena.getPassword());
	}

	public void clearPassword() {
		campoContrasena.setText("");
	}

	public void mostrarMensajeInformativo(String mensaje, String titulo) {
		JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
	}

	public void mostrarMensajeAdvertencia(String mensaje, String titulo) {
		JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.WARNING_MESSAGE);
	}

	public void mostrarMensajeError(String mensaje, String titulo) {
		JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
	}

	public JButton getBtnLogin() {
		return btnLogin;
	}

	public void setBtnLogin(JButton btnLogin) {
		this.btnLogin = btnLogin;
	}
}
