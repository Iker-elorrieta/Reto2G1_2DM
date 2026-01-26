package vista;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import controlador.Controlador;
import modelo.Centro;
import modelo.Reuniones;
import modelo.Users;

public class CrearReuiniones extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTextField campoTitulo, campoTema, campoDia, campoHora, campoAula;
    private JComboBox<Users> comboEstudiantes;
    private JComboBox<Centro> comboUbicacion;
    private JButton btnCrear, btnVolver;

    public CrearReuiniones(Controlador controlador, List<Users> estudiantes) {

        setTitle("Crear Nueva Solicitud de Reunión");
        setSize(600, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        JPanel panel = new JPanel(null);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Campos
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

        // UBICACIÓN DESDE JSON
        comboUbicacion = new JComboBox<>();
        comboUbicacion.setBounds(297, 268, 257, 39);
        panel.add(comboUbicacion);
        List<Centro> centros = controlador.obtenerCentros();
        for (Centro c : centros) comboUbicacion.addItem(c);

     
        for (Centro c : centros) {
            if (c.getNOM() != null && c.getNOM().equalsIgnoreCase("ELORRIETA-ERREKA MARI")) {
                comboUbicacion.setSelectedItem(c);
                break;
            }
        }


        // Estudiantes
        comboEstudiantes = new JComboBox<>();
        comboEstudiantes.setBounds(297, 317, 257, 39);
        for (Users u : estudiantes) comboEstudiantes.addItem(u);

        // Labels
        panel.add(crearLabel("Título (Propósito):", 30, 23));
        panel.add(campoTitulo);

        panel.add(crearLabel("Tema (Descripción):", 30, 72));
        panel.add(campoTema);

        panel.add(crearLabel("Día (dd/MM/yyyy):", 30, 121));
        panel.add(campoDia);

        panel.add(crearLabel("Hora (HH:mm):", 30, 170));
        panel.add(campoHora);

        panel.add(crearLabel("Aula:", 30, 219));
        panel.add(campoAula);

        panel.add(crearLabel("Ubicación/Centro:", 30, 268));
        panel.add(comboUbicacion);

        panel.add(crearLabel("Seleccionar Estudiante:", 30, 317));
        panel.add(comboEstudiantes);

        getContentPane().add(panel, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        btnCrear = new JButton("Enviar Solicitud");
        btnCrear.setBackground(new Color(46, 139, 87));
        btnCrear.setForeground(Color.WHITE);

        btnVolver = new JButton("⬅ Volver");

        panelBotones.add(btnVolver);
        panelBotones.add(btnCrear);

        getContentPane().add(panelBotones, BorderLayout.SOUTH);
    }

    private JLabel crearLabel(String texto, int x, int y) {
        JLabel label = new JLabel(texto);
        label.setBounds(x, y, 250, 39);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        return label;
    }

    public Reuniones construirReunion(Users profesor) {

        if (estaVacio(campoTitulo) || estaVacio(campoTema) || estaVacio(campoDia)
                || estaVacio(campoHora) || estaVacio(campoAula)) {

            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos obligatorios.",
                    "Atención", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Reuniones r = new Reuniones();
        r.setTitulo(campoTitulo.getText().trim());
        r.setAsunto(campoTema.getText().trim());
        r.setAula(campoAula.getText().trim());

        // CENTRO SELECCIONADO
        Centro centro = (Centro) comboUbicacion.getSelectedItem();
        r.setIdCentro(centro.getCCEN());
   


        r.setEstado("Pendiente");
        r.setUsersByProfesorId(profesor);
        r.setUsersByAlumnoId((Users) comboEstudiantes.getSelectedItem());

        try {
            String fechaCompleta = campoDia.getText().trim() + " " + campoHora.getText().trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(fechaCompleta, formatter);
            r.setFecha(Timestamp.valueOf(dateTime));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Formato incorrecto.\nUsa: 26/01/2026 para el día y 14:30 para la hora.",
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return r;
    }

    private boolean estaVacio(JTextField campo) {
        return campo.getText().strip().isEmpty();
    }

    public JButton getBtnCrear() { return btnCrear; }
    public JButton getBtnVolver() { return btnVolver; }
}
