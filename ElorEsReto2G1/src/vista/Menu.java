package vista;

import javax.swing.*;
import javax.swing.border.*;

import controlador.Controlador;
import modelo.Users;

import java.awt.*;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class Menu extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    @SuppressWarnings("unused")
    private Controlador controlador;
    @SuppressWarnings("unused")
    private int idUsuario;

    public Menu(Controlador controlador, int idUsuario) {
    	this.controlador = controlador;
    	this.idUsuario = idUsuario;
    	
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 780, 541);
        setTitle("Menú Principal");

        contentPane = new JPanel();
        contentPane.setBackground(new Color(245, 245, 245));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // ===== PANEL IZQUIERDO =====
        JPanel panelIzq = new JPanel();
        panelIzq.setBounds(30, 28, 220, 438);
        panelIzq.setLayout(null);
        panelIzq.setBackground(Color.WHITE);
        panelIzq.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        contentPane.add(panelIzq);

        JButton btnPerfil = new JButton("⚙  Consultar Perfil");
        btnPerfil.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

	                Users user = controlador.obtenerPerfil(idUsuario);
	              
	                if (user != null) {
	                    Perfil perfil = new Perfil(user,Menu.this);
	                    perfil.setVisible(true);
	                    setVisible(false);
	                } else {
	                    JOptionPane.showMessageDialog(null, 
	                        "No se pudo obtener el perfil del usuario.",
	                        "Error",
	                        JOptionPane.ERROR_MESSAGE);
	                }
	            }
	        });

        btnPerfil.setBounds(20, 114, 180, 76);
        btnPerfil.setFocusPainted(false);
        panelIzq.add(btnPerfil);

        JButton btnAlumnos = new JButton("👤  Consultar Alumnos");
        btnAlumnos.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Alumnos vntnAlumn = new Alumnos(controlador, idUsuario,Menu.this);
        		vntnAlumn.setVisible(true); 
        		setVisible(false);
        	}
        });
        btnAlumnos.setBounds(20, 201, 180, 145);
        btnAlumnos.setFocusPainted(false);
        panelIzq.add(btnAlumnos);
        
        JLabel lblelo = new JLabel("");
        
        lblelo.setBounds(0, 0, 220, 96);
        panelIzq.add(lblelo);
        lblelo.setIcon(new ImageIcon(getClass().getClassLoader().getResource("logoelo.png")));
        
                JLabel lblPerfil = new JLabel("Perfil");
                lblPerfil.setBounds(10, 80, 180, 25);
                panelIzq.add(lblPerfil);
                lblPerfil.setFont(new Font("Segoe UI", Font.BOLD, 16));
                
                JButton btnDesc = new JButton("Desconectar");
                btnDesc.setFocusPainted(false);
                btnDesc.setBounds(20, 371, 180, 56);
                panelIzq.add(btnDesc);
                btnDesc.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                        int opcion = JOptionPane.showConfirmDialog(
                                null,
                                "¿Deseas cerrar la sesión?",
                                "Confirmar desconexión",
                                JOptionPane.YES_NO_OPTION
                        );

                        if (opcion == JOptionPane.YES_OPTION) {
                            // Desconectar y cerrar socket
                            controlador.desconectar();

                            // Crear nueva conexión al servidor
                            try {
                                Socket nuevoSocket = new Socket("localhost", 5000);
                                Login login = new Login(nuevoSocket);
                                login.setVisible(true);
                                dispose();
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, "No se pudo conectar al servidor", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                                // Si no puede conectar, volver a inicio
                                InicioCliente inicio = new InicioCliente();
                                inicio.setVisible(true);
                                dispose();
                            }
                        }
                    }
                });



        // ===== PANEL DERECHO =====
        JPanel panelDer = new JPanel();
        panelDer.setBounds(260, 107, 480, 384);
        panelDer.setLayout(null);
        panelDer.setBackground(new Color(245, 245, 245));
        contentPane.add(panelDer);

        // ===== HORARIO =====
        JPanel panelHorario = new JPanel();
        panelHorario.setBounds(0, 0, 480, 180);
        panelHorario.setLayout(null);
        panelHorario.setBackground(Color.WHITE);
        panelHorario.setBorder(new TitledBorder(
                new LineBorder(new Color(180, 180, 180)),
                "📅 Horario",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14)
        ));
        panelDer.add(panelHorario);

        JButton btnConsultarHorario = new JButton("Consultar horario");
        btnConsultarHorario.setBounds(40, 31, 180, 138);
        panelHorario.add(btnConsultarHorario);
        btnConsultarHorario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	Horario panelHorario = new Horario(controlador,idUsuario,Menu.this);
            	panelHorario.setVisible(true);
            	setVisible(false);
            	
            	
            }
        });
        

        JButton btnOtrosHorarios = new JButton("Consultar otros horarios");
        btnOtrosHorarios.setBounds(243, 31, 202, 138);
        panelHorario.add(btnOtrosHorarios);
        btnOtrosHorarios.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	OtrsHorarios panelHorarios = new OtrsHorarios();
            	panelHorarios.setVisible(true);
            	setVisible(false);
            	
            	
            }
        });

        // ===== REUNIONES =====
        JPanel panelReuniones = new JPanel();
        panelReuniones.setBounds(0, 204, 480, 180);
        panelReuniones.setLayout(null);
        panelReuniones.setBackground(Color.WHITE);
        panelReuniones.setBorder(new TitledBorder(
                new LineBorder(new Color(180, 180, 180)),
                "👥 Reuniones",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14)
        ));
        panelDer.add(panelReuniones);

        JButton btnCrearReunion = new JButton("Crear reunión");
        btnCrearReunion.setBounds(40, 29, 180, 140);
        panelReuniones.add(btnCrearReunion);

        JButton btnVerReuniones = new JButton("Ver reuniones");
        btnVerReuniones.setBounds(242, 29, 209, 140);
        panelReuniones.add(btnVerReuniones);
        btnVerReuniones.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	VerReuniones panelReuniones = new VerReuniones();
            	panelReuniones.setVisible(true);
            	setVisible(false);
            	
            	
            }
        });
    
            
        
        JLabel lblbanner = new JLabel("");
        lblbanner.setBounds(260, 28, 480, 68);
        contentPane.add(lblbanner);
        lblbanner.setIcon(new ImageIcon(getClass().getClassLoader().getResource("logoelorrieta.jpg")));
    }
}
