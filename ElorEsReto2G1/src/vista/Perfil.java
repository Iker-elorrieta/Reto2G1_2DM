package vista;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import modelo.Users;

public class Perfil extends JFrame {

    private static final long serialVersionUID = 1L;
    private Users user;

    public Perfil(Users user) {
        this.user = user;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 451, 781);
        setTitle("Perfil del Profesor");

        JPanel contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 435, 742);
        panel.setLayout(null);
        contentPane.add(panel);

        JPanel panelIzq = new JPanel();
        panelIzq.setBounds(42, 21, 349, 620);
        panelIzq.setLayout(null);
        panelIzq.setBackground(new Color(180, 180, 180));
        panelIzq.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        panel.add(panelIzq);

        int y = 40;

        panelIzq.add(crearLabel("ID: " + user.getId(), y)); y += 40;
        panelIzq.add(crearLabel("Email: " + user.getEmail(), y)); y += 40;
        panelIzq.add(crearLabel("Usuario: " + user.getUsername(), y)); y += 40;
        panelIzq.add(crearLabel("Nombre: " + user.getNombre(), y)); y += 40;
        panelIzq.add(crearLabel("Apellidos: " + user.getApellidos(), y)); y += 40;
        panelIzq.add(crearLabel("DNI: " + user.getDni(), y)); y += 40;
        panelIzq.add(crearLabel("Dirección: " + user.getDireccion(), y)); y += 40;
        panelIzq.add(crearLabel("Teléfono 1: " + user.getTelefono1(), y)); y += 40;
        panelIzq.add(crearLabel("Teléfono 2: " + user.getTelefono2(), y)); y += 40;
    }

    private JLabel crearLabel(String texto, int y) {
        JLabel lbl = new JLabel(texto);
        lbl.setBounds(20, y, 300, 30);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return lbl;
    }
}
