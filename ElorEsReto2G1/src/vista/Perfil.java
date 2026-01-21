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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Perfil extends JFrame {

    private static final long serialVersionUID = 1L;

    public Perfil(Users user,Menu menu) {
       

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 500, 750);
        setTitle("Perfil");

        JPanel contentPane = new JPanel();
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(245, 245, 245));
        setContentPane(contentPane);

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
        contentPane.add(panel);

        // PANEL INTERIOR
        JPanel panelInfo = new JPanel();
        panelInfo.setBounds(0, 0, 430, 621);
        panelInfo.setLayout(null);
        panelInfo.setBackground(Color.WHITE);
        panelInfo.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        panel.add(panelInfo);

        // CABECERA
        JLabel lblTitulo = new JLabel("Perfil del Profesor");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setBounds(20, 20, 300, 30);
        panelInfo.add(lblTitulo);

        JLabel lblSub = new JLabel(user.getNombre() + " " + user.getApellidos());
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblSub.setForeground(new Color(80, 80, 80));
        lblSub.setBounds(20, 55, 350, 25);
        panelInfo.add(lblSub);

        // SEPARADOR
        JPanel separador = new JPanel();
        separador.setBackground(new Color(200, 200, 200));
        separador.setBounds(20, 90, 390, 1);
        panelInfo.add(separador);

        // DATOS
        int y = 120;

        panelInfo.add(crearCampo("ID", String.valueOf(user.getId()), y)); y += 55;
        panelInfo.add(crearCampo("Email", user.getEmail(), y)); y += 55;
        panelInfo.add(crearCampo("Usuario", user.getUsername(), y)); y += 55;
        panelInfo.add(crearCampo("Nombre", user.getNombre(), y)); y += 55;
        panelInfo.add(crearCampo("Apellidos", user.getApellidos(), y)); y += 55;
        panelInfo.add(crearCampo("DNI", user.getDni(), y)); y += 55;
        panelInfo.add(crearCampo("Dirección", user.getDireccion(), y)); y += 55;
        panelInfo.add(crearCampo("Teléfono 1", user.getTelefono1(), y)); y += 55;
        panelInfo.add(crearCampo("Teléfono 2", user.getTelefono2(), y)); 
        
        JButton btnVolver = new JButton("<-- Volver");
        btnVolver.setBackground(Color.WHITE);
        btnVolver.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
            menu.setVisible(true);
        	setVisible(false);
   
        		
        	}
        });
        btnVolver.setBounds(30, 665, 430, 35);
        contentPane.add(btnVolver);y += 55;
        
      
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
}
