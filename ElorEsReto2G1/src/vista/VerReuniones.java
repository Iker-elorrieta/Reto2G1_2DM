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

    public VerReuniones(int idProfesor) {

        setTitle("Reuniones del Profesor");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. Cargar datos
        List<Horarios> horario = Horarios.obtenerHorarioREST(idProfesor);
        List<Reuniones> reuniones = Reuniones.obtenerReunionesProfesorREST(idProfesor);

        // 2. Crear horario visual combinado
        tablaHorario = construirHorarioConReuniones(horario, reuniones);

        // 3. Crear tabla de pendientes
        tablaPendientes = construirTablaPendientes(reuniones);

        // 4. Botón volver
        btnVolver = new JButton("⬅ Volver");

        // 5. Añadir componentes
        add(new JScrollPane(tablaHorario), BorderLayout.CENTER);
        add(new JScrollPane(tablaPendientes), BorderLayout.SOUTH);
        add(btnVolver, BorderLayout.NORTH);
    }

    // ============================================================
    // HORARIO + REUNIONES
    // ============================================================

    private JTable construirHorarioConReuniones(List<Horarios> horario, List<Reuniones> reuniones) {

        String[] dias = {"LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES"};
        String[][] tabla = new String[6][5];

        // 1. Pintar asignaturas
        for (Horarios h : horario) {
            int fila = h.getHora() - 1;
            int col = diaAColumna(h.getDia());
            if (fila >= 0 && fila < 6 && col >= 0)
                tabla[fila][col] = "<b>" + h.getNombreModulo() + "</b>";
        }

        // 2. Pintar reuniones
        for (Reuniones r : reuniones) {

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
        List<Reuniones> pendientes = reuniones.stream()
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
