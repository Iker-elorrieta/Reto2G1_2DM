package vista;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import modelo.Reuniones;
import modelo.Users;

public class CrearReuiniones extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTextField campoTitulo, campoTema, campoDia, campoHora, campoAula, campoUbicacion;
    private JComboBox<Users> comboEstudiantes;
    private JButton btnCrear, btnVolver;

    public CrearReuiniones(List<Users> estudiantes) {
        setTitle("Crear Reunión");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        JPanel panel = new JPanel(null);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        campoTitulo = new JTextField();
        campoTitulo.setBounds(297, 23, 257, 39);
        campoTema = new JTextField();
        campoTema.setBounds(297, 72, 257, 39);
        campoDia = new JTextField();
        campoDia.setBounds(297, 121, 257, 39);
        campoHora = new JTextField();
        campoHora.setBounds(297, 170, 257, 39);
        campoAula = new JTextField();
        campoAula.setBounds(297, 219, 257, 39);
        campoUbicacion = new JTextField("ELORRIETA");
        campoUbicacion.setBounds(297, 268, 257, 39);

        comboEstudiantes = new JComboBox<>();
        comboEstudiantes.setBounds(297, 317, 257, 39);
        for (Users u : estudiantes) {
            comboEstudiantes.addItem(u);
        }

        panel.add(crearLabel("Título:", 30, 23));
        panel.add(campoTitulo);
        panel.add(crearLabel("Tema:", 30, 72));
        panel.add(campoTema);
        panel.add(crearLabel("Día (dd/MM/yyyy):", 30, 121));
        panel.add(campoDia);
        panel.add(crearLabel("Hora (HH:mm):", 30, 170));
        panel.add(campoHora);
        panel.add(crearLabel("Aula:", 30, 219));
        panel.add(campoAula);
        panel.add(crearLabel("Ubicación:", 30, 268));
        panel.add(campoUbicacion);
        panel.add(crearLabel("Estudiante:", 30, 317));
        panel.add(comboEstudiantes);

        getContentPane().add(panel, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnCrear = new JButton("Crear reunión");
        btnVolver = new JButton("<-- Volver");
        panelBotones.add(btnCrear);
        panelBotones.add(btnVolver);

        getContentPane().add(panelBotones, BorderLayout.SOUTH);
    }

    private JLabel crearLabel(String texto, int x, int y) {
        JLabel label = new JLabel(texto);
        label.setBounds(x, y, 257, 39);
        return label;
    }

    public Reuniones construirReunion(Users profesor) {
        if (campoTitulo.getText().isBlank() || campoTema.getText().isBlank() ||
            campoDia.getText().isBlank() || campoHora.getText().isBlank() ||
            campoAula.getText().isBlank() || comboEstudiantes.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Todos los campos deben estar completos.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Reuniones r = new Reuniones();
        r.setTitulo(campoTitulo.getText());
        r.setAsunto(campoTema.getText());
        r.setAula(campoAula.getText());
        r.setIdCentro(campoUbicacion.getText());
        r.setEstado("Pendiente");
        r.setUsersByProfesorId(profesor);
        r.setUsersByAlumnoId((Users) comboEstudiantes.getSelectedItem());

        try {
            String fechaCompleta = campoDia.getText().trim() + " " + campoHora.getText().trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(fechaCompleta, formatter);
            r.setFecha(Timestamp.valueOf(dateTime));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha u hora incorrecto. Usa 'yyyy-MM-dd' y 'HH:mm'.", "Error de formato", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return r;
    }

    public JButton getBtnCrear() {
        return btnCrear;
    }

    public JButton getBtnVolver() {
        return btnVolver;
    }
}
