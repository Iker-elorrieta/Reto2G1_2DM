package vista;

import java.awt.Font;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

import modelo.Users;

public class Alumnos extends JFrame {
    private static final long serialVersionUID = 1L;
    private final List<Users> alumnos;
    private final JList<String> listaAlumnos;
        private JButton btnVolver;
        private JButton btnDetalles;

    public Alumnos(List<Users> alumnos) {
        this.alumnos = alumnos;
        setTitle("Lista de Alumnos");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultListModel<String> modeloLista = new DefaultListModel<>();

        for (Users alumno : alumnos) {
            modeloLista.addElement(alumno.getId() + " - " + alumno.getNombre() + " " + alumno.getApellidos());
        }
        getContentPane().setLayout(null);

        listaAlumnos = new JList<>(modeloLista);
        listaAlumnos.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JScrollPane panelDesplazable = new JScrollPane(listaAlumnos);
        panelDesplazable.setBounds(0, 0, 484, 530);
        getContentPane().add(panelDesplazable);

		btnVolver = new JButton("<--Volver");
        btnVolver.setBounds(0, 530, 241, 31);
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 16));
        getContentPane().add(btnVolver);
        
		btnDetalles = new JButton("Ver detalles");
        btnDetalles.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDetalles.setBounds(243, 530, 241, 31);
        getContentPane().add(btnDetalles);
        
    }

	public Users getSelectedAlumno() {
        int indiceSeleccionado = listaAlumnos.getSelectedIndex();
        if (indiceSeleccionado >= 0) {
            return alumnos.get(indiceSeleccionado);
		}
		return null;
	}

    public JButton getBtnVolver() {
        return btnVolver;
    }

    public void setBtnVolver(JButton btnVolver) {
        this.btnVolver = btnVolver;
    }

    public JButton getBtnDetalles() {
        return btnDetalles;
    }

    public void setBtnDetalles(JButton btnDetalles) {
        this.btnDetalles = btnDetalles;
    }
}
