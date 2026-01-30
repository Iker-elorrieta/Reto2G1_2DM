package vista;

import java.awt.BorderLayout;
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import modelo.Horarios;
import modelo.Reuniones;

public class VerReuniones extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable tablaHorario;
    private JTable tablaPendientes;
    private JButton btnVolver;

    public VerReuniones() {

        setTitle("Reuniones del Profesor");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Componentes iniciales vacíos
        tablaHorario = new JTable(new String[6][5], new String[]{"LUNES","MARTES","MIERCOLES","JUEVES","VIERNES"});
        tablaHorario.setRowHeight(80);
        tablaPendientes = new JTable(new Object[0][5], new String[]{"ID","Alumno","Título","Fecha","Estado"});
        tablaPendientes.setRowHeight(30);

        // Botón volver
        btnVolver = new JButton("⬅ Volver");

        // Añadir componentes
        add(new JScrollPane(tablaHorario), BorderLayout.CENTER);
        add(new JScrollPane(tablaPendientes), BorderLayout.SOUTH);
        add(btnVolver, BorderLayout.NORTH);
    }

    // Setter para popular datos desde el controlador
    public void setData(java.util.List<Horarios> horario, java.util.List<Reuniones> reuniones) {
        JTable nuevoHorario = construirHorarioConReuniones(horario, reuniones);
        JTable nuevasPendientes = construirTablaPendientes(reuniones);

        getContentPane().removeAll();
        add(new JScrollPane(nuevoHorario), BorderLayout.CENTER);
        add(new JScrollPane(nuevasPendientes), BorderLayout.SOUTH);
        add(btnVolver, BorderLayout.NORTH);

        revalidate();
        repaint();

        this.tablaHorario = nuevoHorario;
        this.tablaPendientes = nuevasPendientes;
    }

    // ============================================================
    // HORARIO + REUNIONES
    // ============================================================

    private JTable construirHorarioConReuniones(List<Horarios> horario, List<Reuniones> reuniones) {

        String[] dias = {"LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES"};
        String[][] tabla = new String[6][5];

        // 1. Pintar asignaturas
        if (horario != null) for (Horarios h : horario) {
            int fila = h.getHora() - 1;
            int col = diaAColumna(h.getDia());
            if (fila >= 0 && fila < 6 && col >= 0)
                tabla[fila][col] = "<b>" + h.getNombreModulo() + "</b>";
        }

        // 2. Pintar reuniones
        if (reuniones != null) for (Reuniones r : reuniones) {

            LocalDateTime dt = r.getFecha().toLocalDateTime();

            int fila = dt.getHour() - 1; // tu horario usa horas 1–6
            int col = diaAColumna(convertirDia(dt.getDayOfWeek().name()));

            if (fila < 0 || fila >= 6 || col < 0) continue;

            String estado = r.getEstado();
            String textoReunion = "<br><b>Reunión</b>";

            if (estado.equalsIgnoreCase("Pendiente"))
                textoReunion += " <span style='color:orange'>(Pendiente)</span>";

            if (estado.equalsIgnoreCase("Aceptada"))
                textoReunion += " <span style='color:green'>(Aceptada)</span>";

            if (estado.equalsIgnoreCase("Denegada"))
                textoReunion += " <span style='color:red'>(Denegada)</span>";

            // Si ya hay asignatura → conflicto
            if (tabla[fila][col] != null) {
                tabla[fila][col] += "<br><span style='color:gray'>(Conflicto)</span>" + textoReunion;
            } else {
                tabla[fila][col] = textoReunion;
            }
        }

        JTable t = new JTable(tabla, dias);
        t.setRowHeight(80);
        return t;
    }

    // Convierte MONDAY → LUNES
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

        String[] columnas = {"ID", "Alumno", "Título", "Fecha", "Estado"};
        List<Reuniones> pendientes = (reuniones == null) ? java.util.Collections.emptyList() : reuniones.stream()
                .filter(r -> r.getEstado().equalsIgnoreCase("Pendiente"))
                .toList();

        Object[][] datos = new Object[pendientes.size()][5];

        for (int i = 0; i < pendientes.size(); i++) {
            Reuniones r = pendientes.get(i);
            datos[i][0] = r.getIdReunion();
            datos[i][1] = r.getIdAlumno();
            datos[i][2] = r.getTitulo();
            datos[i][3] = r.getFecha();
            datos[i][4] = "Pendiente";
        }

        JTable tabla = new JTable(datos, columnas);
        tabla.setRowHeight(30);
        return tabla;
    }
}
