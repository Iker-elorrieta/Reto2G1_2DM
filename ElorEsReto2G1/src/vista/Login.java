package vista;

import java.awt.*;
import javax.swing.*;

import controlador.Controlador;

import java.awt.event.ActionListener;

import java.net.Socket;

import java.awt.event.ActionEvent;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsu;
	private JPasswordField txtCntr;
	private Controlador cntrldr;

	public Login(Socket socket) {
		this.cntrldr = new Controlador(socket);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 450);
		setLocationRelativeTo(null);
		setResizable(false);

	// PANEL PRINCIPAL CON IMAGEN DE FONDO
	contentPane = new JPanel() {

		private static final long serialVersionUID = 1L;

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Image img = new ImageIcon(getClass().getClassLoader().getResource("fondo.jpg")).getImage();
			g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
		}
	};
		contentPane.setLayout(null);
		setContentPane(contentPane);

		// PANEL SEMITRANSPARENTE
		JPanel blurPanel = new JPanel();
		blurPanel.setBackground(new Color(255, 255, 255, 150));
		blurPanel.setBounds(250, 80, 300, 260);
		blurPanel.setLayout(null);
		blurPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 180), 2));
		contentPane.add(blurPanel);

		// TÍTULO
		JLabel lblTitulo = new JLabel("Acceso Profesores");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBounds(30, 20, 240, 30);
		blurPanel.add(lblTitulo);

		// USUARIO
		JLabel lblUsu = new JLabel("Usuario:");
		lblUsu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblUsu.setBounds(30, 80, 240, 20);
		blurPanel.add(lblUsu);

		txtUsu = new JTextField();
		txtUsu.setBounds(30, 105, 240, 28);
		txtUsu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		blurPanel.add(txtUsu);

		// PASSWORD
		JLabel lblCntr = new JLabel("Contraseña:");
		lblCntr.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblCntr.setBounds(30, 150, 240, 20);
		blurPanel.add(lblCntr);

	JButton btnVer = new JButton("");
	btnVer.setBounds(242, 176, 28, 27);
	blurPanel.add(btnVer);
	btnVer.setFocusable(false);
	btnVer.setBorderPainted(false);
	btnVer.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ojo.jpg")));

		// BOTÓN LOGIN
		JButton btnLogin = new JButton("Entrar");
		btnLogin.setBounds(94, 215, 120, 35);
		blurPanel.add(btnLogin);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
				
					String usu = txtUsu.getText();
					String cntr = new String(txtCntr.getPassword());

					int codigo = cntrldr.verificarLogin(usu, cntr);				if (codigo == 1) {
					int idUsuario = cntrldr.getUsuarioId();
					JOptionPane.showMessageDialog(null, "Login Correcto", "Acceso Permitido",
							JOptionPane.INFORMATION_MESSAGE);
					Menu menu = new Menu(cntrldr, idUsuario);
					menu.setVisible(true);
					dispose(); // Cerrar Login completamente

				} else if (codigo == 2) {
						JOptionPane.showMessageDialog(null,"Solo los profesores pueden iniciar sesión","Acceso denegado",JOptionPane.WARNING_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos", "Acceso Denegado",
								JOptionPane.ERROR_MESSAGE);
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnLogin.setFocusPainted(false);
		btnLogin.setBackground(new Color(30, 144, 255));
		btnLogin.setForeground(Color.WHITE);

		txtCntr = new JPasswordField();
		txtCntr.setBounds(30, 176, 240, 28);
		txtCntr.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		blurPanel.add(txtCntr);
		btnVer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean mostrando = false;
				if (!mostrando) {
					txtCntr.setEchoChar((char) 0); // Mostrar contraseña
					mostrando = true;
				} else {
					txtCntr.setEchoChar('•'); // Ocultar contraseña
					mostrando = false;
				}
			}
		});

	}
}
