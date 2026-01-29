package vista;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import modelo.Horarios;
import modelo.Reuniones;

public class VerReuniones extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable tablaHorario;
    private JTable tablaPendientes;
    private JButton btnVolver;

    private int idProfesor;

    public VerReuniones(int idProfesor) {

        this.idProfesor = idProfesor;

        setTitle("Reuniones del Profesor");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel superior
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Gestión de Reuniones del Profesor", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panelTop.add(titulo, BorderLayout.CENTER);

        btnVolver = new JButton("⬅ Volver");
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVolver.setBackground(new Color(70, 130, 180));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        panelTop.add(btnVolver, BorderLayout.WEST);

        add(panelTop, BorderLayout.NORTH);

        // 1. Cargar datos
        List<Horarios> horario = Horarios.obtenerHorarioREST(idProfesor);
        List<Reuniones> reuniones = Reuniones.obtenerReunionesProfesorREST(idProfesor);

        // 2. Crear horario base usando la clase Horario
        Horario vistaHorario = new Horario(horario, "Horario del Profesor");
        tablaHorario = vistaHorario.getTabla();

        // 3. Añadir reuniones encima del horario
        pintarReunionesEnHorario(tablaHorario, reuniones);

        // 4. Activar renderer de colores
        tablaHorario.setDefaultRenderer(Object.class, new ColorRenderer());

        JScrollPane scrollHorario = new JScrollPane(tablaHorario);
        scrollHorario.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(scrollHorario, BorderLayout.CENTER);

        // 5. Crear tabla de pendientes
        tablaPendientes = construirTablaPendientes(reuniones);
        aplicarRenderersYEditores(); // ← AHORA tablaPendientes ya existe

        JScrollPane scrollPendientes = new JScrollPane(tablaPendientes);
        scrollPendientes.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(scrollPendientes, BorderLayout.SOUTH);
    }

    // ============================================================
    // AÑADIR REUNIONES ENCIMA DEL HORARIO
    // ============================================================

    private void pintarReunionesEnHorario(JTable tabla, List<Reuniones> reuniones) {

        for (Reuniones r : reuniones) {

            LocalDateTime dt = r.getFecha().toLocalDateTime();

            int fila = dt.getHour() - 1;
            int col = diaAColumna(convertirDia(dt.getDayOfWeek().name()));

            if (fila < 0 || fila >= 6 || col < 0) continue;

            String actual = tabla.getValueAt(fila, col).toString();
            String estado = r.getEstado();

            String reunionHTML = "<br><b>Reunión</b>";

            if (estado.equalsIgnoreCase("Pendiente"))
                reunionHTML += " <span style='color:orange'>(Pendiente)</span>";
            if (estado.equalsIgnoreCase("Aceptada"))
                reunionHTML += " <span style='color:green'>(Aceptada)</span>";
            if (estado.equalsIgnoreCase("Denegada"))
                reunionHTML += " <span style='color:red'>(Denegada)</span>";

            if (!actual.contains("Libre")) {
                reunionHTML = "<br><span style='color:gray'>(Conflicto)</span>" + reunionHTML;
            }

            tabla.setValueAt(
                actual.replace("</body></html>", "") + reunionHTML + "</body></html>",
                fila, col
            );
        }
    }

    private String convertirDia(String dayOfWeek) {
        return switch (dayOfWeek) {
            case "MONDAY" -> "LUNES";
            case "TUESDAY" -> "MARTES";
            case "WEDNESDAY" -> "MIERCOLES";
            case "THURSDAY" -> "JUEVES";
            case "FRIDAY" -> "VIERNES";
            default -> "";
        };
    }

    private int diaAColumna(String dia) {
        return switch (dia.toUpperCase()) {
            case "LUNES" -> 0;
            case "MARTES" -> 1;
            case "MIERCOLES" -> 2;
            case "JUEVES" -> 3;
            case "VIERNES" -> 4;
            default -> -1;
        };
    }

    // ============================================================
    // TABLA DE REUNIONES PENDIENTES
    // ============================================================

    private JTable construirTablaPendientes(List<Reuniones> reuniones) {

        String[] columnas = {"ID", "Alumno", "Título", "Fecha", "Estado", "Aceptar", "Rechazar"};

        List<Reuniones> pendientes = reuniones.stream()
                .filter(r -> r.getEstado().equalsIgnoreCase("Pendiente"))
                .toList();

        Object[][] datos = new Object[pendientes.size()][7];

        for (int i = 0; i < pendientes.size(); i++) {
            Reuniones r = pendientes.get(i);
            datos[i][0] = r.getIdReunion();
            datos[i][1] = r.getAlumnoNombre();
            datos[i][2] = r.getTitulo();
            datos[i][3] = r.getFecha();
            datos[i][4] = r.getEstado();
            datos[i][5] = "Aceptar";
            datos[i][6] = "Rechazar";
        }

        ModeloPendientes modelo = new ModeloPendientes(datos, columnas);
        JTable tabla = new JTable(modelo);

        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabla.setRowHeight(32);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        return tabla;
    }

    // ============================================================
    // APLICAR RENDERERS Y EDITORES
    // ============================================================

    private void aplicarRenderersYEditores() {
        tablaPendientes.getColumn("Aceptar").setCellRenderer(new ButtonRenderer());
        tablaPendientes.getColumn("Aceptar").setCellEditor(new ButtonEditor("Aceptada"));

        tablaPendientes.getColumn("Rechazar").setCellRenderer(new ButtonRenderer());
        tablaPendientes.getColumn("Rechazar").setCellEditor(new ButtonEditor("Denegada"));
    }

    // ============================================================
    // RENDERER DE COLORES
    // ============================================================

    private class ColorRenderer extends javax.swing.table.DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public java.awt.Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            java.awt.Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            String txt = value != null ? value.toString() : "";

            if (txt.contains("Conflicto")) c.setBackground(Color.LIGHT_GRAY);
            else if (txt.contains("Aceptada")) c.setBackground(new Color(144, 238, 144));
            else if (txt.contains("Denegada")) c.setBackground(new Color(255, 160, 122));
            else if (txt.contains("Pendiente")) c.setBackground(new Color(255, 215, 0));
            else c.setBackground(Color.WHITE);

            return c;
        }
    }

    // ============================================================
    // BOTÓN RENDERER
    // ============================================================

    private class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        private static final long serialVersionUID = 1L;

        public ButtonRenderer() {
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setBackground(new Color(70, 130, 180));
            setForeground(Color.WHITE);
        }

        @Override
        public java.awt.Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            setText(value != null ? value.toString() : "");
            return this;
        }
    }

    // ============================================================
    // BOTÓN EDITOR
    // ============================================================

    private class ButtonEditor extends javax.swing.DefaultCellEditor {
        private static final long serialVersionUID = 1L;
        private JButton btn;
        private String action;

        public ButtonEditor(String action) {
            super(new JTextField());
            this.action = action;

            btn = new JButton(action);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setBackground(new Color(46, 139, 87));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);

            btn.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public java.awt.Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected, int row, int column) {

            btn.addActionListener(e -> {
                int idReunion = (int) table.getValueAt(row, 0);
                Reuniones.actualizarEstadoREST(idReunion, action);
                recargarDatos();
            });

            return btn;
        }
    }

    // ============================================================
    // RECARGAR DATOS
    // ============================================================

    private void recargarDatos() {
        List<Horarios> horario = Horarios.obtenerHorarioREST(idProfesor);
        List<Reuniones> reuniones = Reuniones.obtenerReunionesProfesorREST(idProfesor);

        // reconstruir horario
        Horario vistaHorario = new Horario(horario, "Horario del Profesor");
        tablaHorario.setModel(vistaHorario.getTabla().getModel());
        tablaHorario.setDefaultRenderer(Object.class, new ColorRenderer());
        pintarReunionesEnHorario(tablaHorario, reuniones);

        // reconstruir tabla pendientes SIN crear JTable nueva
        DefaultTableModel nuevoModelo = (DefaultTableModel) construirTablaPendientes(reuniones).getModel();
        tablaPendientes.setModel(nuevoModelo);

        aplicarRenderersYEditores();
    }

    // ============================================================
    // MODELO EDITABLE SOLO EN BOTONES
    // ============================================================

    private class ModeloPendientes extends DefaultTableModel {

        private static final long serialVersionUID = 1L;

		public ModeloPendientes(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 5 || column == 6;
        }
    }
}
