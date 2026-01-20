package vista;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.Socket;

public class InicioCliente extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel lbllogo;
	private JPanel panelFondo;

	public InicioCliente() {
		setTitle("Inicio Cliente");
		setSize(800, 450);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		// PANEL CON IMAGEN DE FONDO
		panelFondo = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image img = new ImageIcon("main/media/fondo2.jpg").getImage();
				g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
			}
		};

		setContentPane(panelFondo);
		panelFondo.setLayout(null);

		lbllogo = new JLabel();
		lbllogo.setBounds(145, 122, 361, 210);
		panelFondo.add(lbllogo);
		lbllogo.setIcon(new ImageIcon("logoelo.png"));

		// EVENTO CLICK EN TODA LA PANTALLA
		panelFondo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Socket socket = new Socket("localhost", 5000);
					System.out.println("Conectado al servidor");
					Login login = new Login(socket);
					login.setVisible(true);
					setVisible(false);

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "No se pudo conectar al servidor", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	public static void main(String[] args) {
		InicioCliente inicio = new InicioCliente();
		inicio.setVisible(true);
	}
}