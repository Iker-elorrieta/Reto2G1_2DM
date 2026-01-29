	package vista;
	
	import javax.swing.*;
	import java.awt.*;
	import java.sql.Timestamp;
	import java.time.LocalDateTime;
	import java.util.List;
	
	import controlador.Controlador;
	import modelo.Centro;
	import modelo.Reuniones;
	import modelo.Users;
	
	public class CrearReuiniones extends JFrame {
	    private static final long serialVersionUID = 1L;
	
	    private JTextField campoTitulo, campoTema, campoAula;
	    private JSpinner spinnerDia;
	    private JComboBox<Integer> comboHora;
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
	
	        // 📅 CALENDARIO
	        spinnerDia = new JSpinner(new SpinnerDateModel());
	        spinnerDia.setBounds(297, 121, 257, 39);
	        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinnerDia, "dd/MM/yyyy");
	        spinnerDia.setEditor(editor);
	
	        // ⏰ HORA 1–6
	        comboHora = new JComboBox<>();
	        comboHora.setBounds(297, 170, 257, 39);
	        for (int i = 1; i <= 6; i++) comboHora.addItem(i);
	
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
	
	        panel.add(crearLabel("Día:", 30, 121));
	        panel.add(spinnerDia);
	
	        panel.add(crearLabel("Hora (1–6):", 30, 170));
	        panel.add(comboHora);
	
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
	
	        if (campoTitulo.getText().isBlank() ||
	            campoTema.getText().isBlank() ||
	            campoAula.getText().isBlank()) {
	
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
	
	        // PROFESOR → SOLO SU ID
	        r.setIdProfesor(profesor.getId());
	
	        // ALUMNO SELECCIONADO → SOLO SU ID
	        Users alumno = (Users) comboEstudiantes.getSelectedItem();
	        r.setIdAlumno(alumno.getId());
	
	        try {
	            // Día seleccionado
	            java.util.Date fechaSeleccionada = (java.util.Date) spinnerDia.getValue();
	            LocalDateTime date = fechaSeleccionada.toInstant()
	                    .atZone(java.time.ZoneId.systemDefault())
	                    .toLocalDateTime();
	
	            // Hora 1–6
	            int hora = (int) comboHora.getSelectedItem();
	            date = date.withHour(hora).withMinute(0).withSecond(0);
	
	            r.setFecha(Timestamp.valueOf(date));
	
	        } catch (Exception e) {
	            JOptionPane.showMessageDialog(this,
	                    "Error al procesar la fecha u hora.",
	                    "Error", JOptionPane.ERROR_MESSAGE);
	            return null;
	        }
	
	        return r;
	    }
	
	    public JButton getBtnCrear() { return btnCrear; }
	    public JButton getBtnVolver() { return btnVolver; }
	}
