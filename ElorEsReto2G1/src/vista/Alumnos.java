package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import modelo.Users;
import controlador.Controlador;

public class Alumnos extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Alumnos(Controlador controlador, int idProfesor, Menu menu) {
        setTitle("Lista de Alumnos");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        List<Users> alumnos = controlador.obtenerAlumnos(idProfesor);

        DefaultListModel<String> modelo = new DefaultListModel<>();

        for (Users u : alumnos) {
            modelo.addElement(u.getId() + " - " + u.getNombre() + " " + u.getApellidos());
        }
        getContentPane().setLayout(null);

        JList<String> lista = new JList<>(modelo);
        lista.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBounds(0, 0, 484, 530);
        getContentPane().add(scroll);

        JButton btnVolver = new JButton("<--Volver");
        btnVolver.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		menu.setVisible(true);
               	setVisible(false);
        		
        	}
        });
        btnVolver.setBounds(0, 530, 241, 31);
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 16));
        getContentPane().add(btnVolver);
        
        JButton btnDetalles = new JButton("Ver detalles");
        btnDetalles.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDetalles.setBounds(243, 530, 241, 31);
        getContentPane().add(btnDetalles);

        btnDetalles.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            int index = lista.getSelectedIndex();
            if (index >= 0) {
                Users alumno = alumnos.get(index);
                new Perfil(alumno, menu).setVisible(true);
                
            }
            }
        });
        
    }
}
