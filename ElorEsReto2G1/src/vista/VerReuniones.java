package vista;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel; 

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

        // Componentes iniciales vacíos (columna Hora + LUNES..VIERNES)
        String[] dias = { "Hora", "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES" };
        int rows = (20 - 8) + 1; // coincide con MIN_HOUR..MAX_HOUR por defecto (08:00..20:00)
        tablaHorario = new JTable(new String[rows][dias.length], dias);
        tablaHorario.setRowHeight(60);
        tablaPendientes = new JTable(new Object[0][5], new String[]{"ID","Alumno","Título","Fecha","Estado"});
        tablaPendientes.setRowHeight(30);

        // Botón volver
        btnVolver = new JButton("⬅ Volver");

        // Añadir componentes
        add(new JScrollPane(tablaHorario), BorderLayout.CENTER);
        add(new JScrollPane(tablaPendientes), BorderLayout.SOUTH);
        add(btnVolver, BorderLayout.NORTH);
    }

    public void setHorarioModel(TableModel modelo) {
        tablaHorario.setModel(modelo);
        tablaHorario.setRowHeight(60);

        // Renderer centrado para las columnas de contenido
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        centerRenderer.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        tablaHorario.setDefaultRenderer(Object.class, centerRenderer);

        // Resaltar y centrar la columna Hora
        DefaultTableCellRenderer horaRenderer = new DefaultTableCellRenderer();
        horaRenderer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        horaRenderer.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        horaRenderer.setFont(horaRenderer.getFont().deriveFont(java.awt.Font.BOLD));
        tablaHorario.getColumnModel().getColumn(0).setCellRenderer(horaRenderer);
        tablaHorario.getColumnModel().getColumn(0).setPreferredWidth(60);

        // Ajustar ancho de columnas de días
        for (int i = 1; i < tablaHorario.getColumnCount(); i++) {
            tablaHorario.getColumnModel().getColumn(i).setPreferredWidth(160);
        }

        rebuildLayout();
    }

    public void setPendientesModel(TableModel modelo) {
        tablaPendientes.setModel(modelo);
        tablaPendientes.setRowHeight(30);

        // Centrar textos de la tabla de pendientes
        javax.swing.table.DefaultTableCellRenderer rendererPendientes = new javax.swing.table.DefaultTableCellRenderer();
        rendererPendientes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rendererPendientes.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        tablaPendientes.setDefaultRenderer(Object.class, rendererPendientes);

        rebuildLayout();
    }

    // Permite que el controlador agregue listeners y acceda a la tabla de pendientes
    public javax.swing.JTable getTablaPendientes() {
        return tablaPendientes;
    }

    // Permite que el controlador agregue listeners al botón Volver
    public javax.swing.JButton getBtnVolver() {
        return btnVolver;
    }

    private void rebuildLayout() {
        getContentPane().removeAll();
        add(new JScrollPane(tablaHorario), BorderLayout.CENTER);
        add(new JScrollPane(tablaPendientes), BorderLayout.SOUTH);
        add(btnVolver, BorderLayout.NORTH);
        revalidate();
        repaint();
    }



}

