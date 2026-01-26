package vista;


import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import javax.swing.JPanel;


public class PanelConFondo extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -530553973330218742L;
	private Image imagen;

    public PanelConFondo(String ruta) {
        imagen = new ImageIcon(getClass().getClassLoader().getResource(ruta)).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
    }


}
