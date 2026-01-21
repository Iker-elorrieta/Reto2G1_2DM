package vista;

import java.awt.Color;
import java.awt.Font;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import modelo.Users;
import javax.swing.JButton;

public class Perfil extends JFrame {

    private static final long serialVersionUID = 1L;
    private JButton btnVolver;

    public Perfil(Users user) {
       

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 500, 750);
        setTitle("Perfil");

        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(null);
        panelContenido.setBackground(new Color(245, 245, 245));
        setContentPane(panelContenido);

        // PANEL PRINCIPAL CON SOMBRA
        JPanel panel = new JPanel() {
 
			private static final long serialVersionUID = 1L;

			@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ((Graphics) g).setColor(new Color(220, 220, 220));
                g.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 20, 20);
            }
        };
        panel.setBounds(30, 30, 430, 620);
        panel.setLayout(null);
        panel.setOpaque(false);
        panelContenido.add(panel);

        // PANEL INTERIOR
        JPanel panelInformacion = new JPanel();
        panelInformacion.setBounds(0, 0, 430, 621);
        panelInformacion.setLayout(null);
        panelInformacion.setBackground(Color.WHITE);
        panelInformacion.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        panel.add(panelInformacion);

        // CABECERA
        JLabel lblTitulo = new JLabel("Perfil del Profesor");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setBounds(20, 20, 300, 30);
        panelInformacion.add(lblTitulo);

        JLabel lblSub = new JLabel(user.getNombre() + " " + user.getApellidos());
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblSub.setForeground(new Color(80, 80, 80));
        lblSub.setBounds(20, 55, 350, 25);
        panelInformacion.add(lblSub);

        // SEPARADOR
        JPanel panelSeparador = new JPanel();
        panelSeparador.setBackground(new Color(200, 200, 200));
        panelSeparador.setBounds(20, 90, 390, 1);
        panelInformacion.add(panelSeparador);

        // DATOS
        int posicionVertical = 120;

        panelInformacion.add(crearCampo("ID", String.valueOf(user.getId()), posicionVertical)); posicionVertical += 55;
        panelInformacion.add(crearCampo("Email", user.getEmail(), posicionVertical)); posicionVertical += 55;
        panelInformacion.add(crearCampo("Usuario", user.getUsername(), posicionVertical)); posicionVertical += 55;
        panelInformacion.add(crearCampo("Nombre", user.getNombre(), posicionVertical)); posicionVertical += 55;
        panelInformacion.add(crearCampo("Apellidos", user.getApellidos(), posicionVertical)); posicionVertical += 55;
        panelInformacion.add(crearCampo("DNI", user.getDni(), posicionVertical)); posicionVertical += 55;
        panelInformacion.add(crearCampo("Dirección", user.getDireccion(), posicionVertical)); posicionVertical += 55;
        panelInformacion.add(crearCampo("Teléfono 1", user.getTelefono1(), posicionVertical)); posicionVertical += 55;
        panelInformacion.add(crearCampo("Teléfono 2", user.getTelefono2(), posicionVertical)); 
        
        btnVolver = new JButton("<-- Volver");
        btnVolver.setBackground(Color.WHITE);
        btnVolver.setBounds(30, 665, 430, 35);
        panelContenido.add(btnVolver);
        
      
    }
 
    private JPanel crearCampo(String titulo, String valor, int y) {
        JPanel campo = new JPanel();
        campo.setLayout(null);
        campo.setBounds(20, y, 390, 45);
        campo.setBackground(new Color(245, 245, 245));
        campo.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));

        JLabel lblTitulo = new JLabel(titulo + ":");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setBounds(10, 5, 200, 18);
        campo.add(lblTitulo);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblValor.setBounds(10, 22, 350, 20);
        campo.add(lblValor);

        return campo;
    }

    public JButton getBtnVolver() {
        return btnVolver;
    }

    public void setBtnVolver(JButton btnVolver) {
        this.btnVolver = btnVolver;
    }
}
