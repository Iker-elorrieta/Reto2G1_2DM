package vista;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import modelo.Users;

public class OtrsHorarios extends JFrame {

    private static final long serialVersionUID = 1L;
    private final List<Users> profesores;
    private final JComboBox<String> comboProfesores;
    private JButton btnVerHorario;
    private JButton btnVolver;

    public OtrsHorarios(List<Users> profesores) {
    	this.profesores = profesores;

        setTitle("Consultar Horarios de Otros Profesores");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel etiquetaTitulo = new JLabel("Selecciona un profesor:");
        etiquetaTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        etiquetaTitulo.setBounds(30, 20, 300, 30);
        add(etiquetaTitulo);

        comboProfesores = new JComboBox<>();
        comboProfesores.setBounds(30, 70, 420, 30);
        comboProfesores.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        add(comboProfesores);

        for (Users docente : profesores) {
            comboProfesores.addItem(docente.getNombre() + " " + docente.getApellidos());
        }

        btnVerHorario = new JButton("Ver horario");
        btnVerHorario.setBounds(150, 130, 180, 40);
        btnVerHorario.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnVerHorario.setBackground(new Color(70, 130, 180));
        btnVerHorario.setForeground(Color.WHITE);
        btnVerHorario.setFocusPainted(false);
        add(btnVerHorario);

        btnVolver = new JButton("⬅ Volver");
        btnVolver.setBounds(150, 190, 180, 40);
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnVolver.setBackground(new Color(100, 100, 100));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        add(btnVolver);
    }

    public Users getSelectedProfesor() {
        int indiceSeleccionado = comboProfesores.getSelectedIndex();
        if (indiceSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un profesor.");
            return null;
        }
        return profesores.get(indiceSeleccionado);
    }

    public JButton getBtnVerHorario() {
        return btnVerHorario;
    }

    public void setBtnVerHorario(JButton btnVerHorario) {
        this.btnVerHorario = btnVerHorario;
    }

    public JButton getBtnVolver() {
        return btnVolver;
    }

    public void setBtnVolver(JButton btnVolver) {
        this.btnVolver = btnVolver;
    }
}
