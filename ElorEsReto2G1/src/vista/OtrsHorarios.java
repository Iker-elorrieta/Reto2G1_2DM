package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import controlador.Controlador;
import modelo.Users;

public class OtrsHorarios extends JFrame {

    private static final long serialVersionUID = 1L;

    public OtrsHorarios(Controlador controlador, Menu menu) {

        setTitle("Consultar Horarios de Otros Profesores");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel lblTitulo = new JLabel("Selecciona un profesor:");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setBounds(30, 20, 300, 30);
        add(lblTitulo);

        // Obtener lista de profesores desde Spring Boot
        List<Users> profesores = controlador.obtenerProfesores();

        // ComboBox con nombres
        JComboBox<String> comboProfes = new JComboBox<>();
        comboProfes.setBounds(30, 70, 420, 30);
        comboProfes.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        add(comboProfes);

        // Guardamos IDs en paralelo
        DefaultListModel<Integer> idsProfes = new DefaultListModel<>();

        for (Users u : profesores) {
            comboProfes.addItem(u.getNombre() + " " + u.getApellidos());
            idsProfes.addElement(u.getId());
        }

        JButton btnVerHorario = new JButton("Ver horario");
        btnVerHorario.setBounds(150, 130, 180, 40);
        btnVerHorario.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnVerHorario.setBackground(new Color(70, 130, 180));
        btnVerHorario.setForeground(Color.WHITE);
        btnVerHorario.setFocusPainted(false);
        add(btnVerHorario);

        JButton btnVolver = new JButton("⬅ Volver");
        btnVolver.setBounds(150, 190, 180, 40);
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnVolver.setBackground(new Color(100, 100, 100));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        add(btnVolver);

        // Acción: ver horario del profesor seleccionado
        btnVerHorario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int index = comboProfes.getSelectedIndex();
                if (index == -1) {
                    JOptionPane.showMessageDialog(null, "Selecciona un profesor.");
                    return;
                }

                int idProfesor = idsProfes.get(index);

                Horario ventanaHorario = new Horario(controlador, idProfesor, menu);
                ventanaHorario.setVisible(true);
                setVisible(false);
            }
        });

        // Acción: volver al menú
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menu.setVisible(true);
                setVisible(false);
            }
        });
    }
}
