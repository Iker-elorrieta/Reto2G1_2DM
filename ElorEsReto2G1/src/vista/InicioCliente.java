package vista;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import controlador.Controlador;

public class InicioCliente extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel etiquetaLogo;
	private JPanel panelFondo;

	public InicioCliente() {
		setTitle("Inicio Cliente");
		
		setSize(800, 450);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		// PANEL CON IMAGEN DE FONDO
		panelFondo = new JPanel() {
		
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image img = new ImageIcon(getClass().getClassLoader().getResource("fondo2.jpg")).getImage();
				g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
			}
		};

		setContentPane(panelFondo);
		panelFondo.setLayout(null);

		etiquetaLogo = new JLabel();
		etiquetaLogo.setBounds(145, 122, 361, 210);
		panelFondo.add(etiquetaLogo);
		etiquetaLogo.setIcon(new ImageIcon(getClass().getClassLoader().getResource("logoelo.png")));
	}

	public JPanel getPanelFondo() {
		return panelFondo;
	}

	public void setPanelFondo(JPanel panelFondo) {
		this.panelFondo = panelFondo;
	}

	public void mostrarMensajeError(String mensaje) {
		JOptionPane.showMessageDialog(
			this,
			mensaje,
			"Error",
			JOptionPane.ERROR_MESSAGE
		);
	}

	public static void main(String[] args) {
			Controlador controlador = new Controlador();
			controlador.iniciar();
	
	}
}