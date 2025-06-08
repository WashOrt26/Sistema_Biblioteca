import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import excepciones.*;
import java.util.List;

public class SistemaBibliotecaGUI extends JFrame {
    private Biblioteca biblioteca;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Colores bonitos
    private final Color AZUL_BONITO = new Color(52, 152, 219);
    private final Color VERDE_BONITO = new Color(46, 204, 113);
    private final Color ROJO_BONITO = new Color(231, 76, 60);
    private final Color GRIS_FONDO = new Color(248, 249, 250);

    public SistemaBibliotecaGUI() {
        biblioteca = new Biblioteca();
        
        // Cargar datos desde la base de datos
        try {
            biblioteca.cargarLibrosDesdeBD();
            biblioteca.cargarUsuariosDesdeBD();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al conectar con la base de datos.\n" +
                    "Asegúrese de que MySQL esté ejecutándose y las tablas existan.\n\nError: " + e.getMessage(),
                    "Error de Conexión",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        setTitle("Sistema de Biblioteca"); // Quitar emoji del título
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(GRIS_FONDO); // Color de fondo

        // Panel principal con CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(GRIS_FONDO); // Color de fondo

        // Panel del título
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Sistema de Biblioteca", SwingConstants.CENTER); // Quitar emoji
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(AZUL_BONITO); // Color azul bonito
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        titlePanel.setBackground(Color.WHITE); // Fondo blanco

        // Panel del menú
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(6, 1, 10, 10)); // Cambiar a 6 para agregar devolver libro
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        menuPanel.setBackground(GRIS_FONDO); // Color de fondo

        JButton btnConsultar = new JButton("Consultar disponibilidad de libros"); // Quitar emoji
        JButton btnReservar = new JButton("Reservar libro"); // Quitar emoji
        JButton btnDevolver = new JButton("Devolver libro"); // Nueva opción
        JButton btnEstado = new JButton("Consultar estado del usuario"); // Quitar emoji
        JButton btnMulta = new JButton("Pagar multa"); // Quitar emoji
        JButton btnSalir = new JButton("Salir"); // Quitar emoji

        // Estilo de los botones
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

        // Botón Consultar - Azul
        btnConsultar.setFont(buttonFont);
        btnConsultar.setBackground(AZUL_BONITO);
        btnConsultar.setForeground(Color.WHITE);
        btnConsultar.setBorderPainted(false);
        btnConsultar.setFocusPainted(false);

        // Botón Reservar - Azul
        btnReservar.setFont(buttonFont);
        btnReservar.setBackground(AZUL_BONITO);
        btnReservar.setForeground(Color.WHITE);
        btnReservar.setBorderPainted(false);
        btnReservar.setFocusPainted(false);

        // Botón Devolver - Verde
        btnDevolver.setFont(buttonFont);
        btnDevolver.setBackground(VERDE_BONITO);
        btnDevolver.setForeground(Color.WHITE);
        btnDevolver.setBorderPainted(false);
        btnDevolver.setFocusPainted(false);

        // Botón Estado - Verde
        btnEstado.setFont(buttonFont);
        btnEstado.setBackground(VERDE_BONITO);
        btnEstado.setForeground(Color.WHITE);
        btnEstado.setBorderPainted(false);
        btnEstado.setFocusPainted(false);

        // Botón Multa - Naranja
        btnMulta.setFont(buttonFont);
        btnMulta.setBackground(new Color(230, 126, 34));
        btnMulta.setForeground(Color.WHITE);
        btnMulta.setBorderPainted(false);
        btnMulta.setFocusPainted(false);

        // Botón Salir - Rojo
        btnSalir.setFont(buttonFont);
        btnSalir.setBackground(ROJO_BONITO);
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setBorderPainted(false);
        btnSalir.setFocusPainted(false);

        menuPanel.add(btnConsultar);
        menuPanel.add(btnReservar);
        menuPanel.add(btnDevolver);
        menuPanel.add(btnEstado);
        menuPanel.add(btnMulta);
        menuPanel.add(btnSalir);

        // Agregar los paneles al frame
        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // Agregar el menú al CardLayout
        mainPanel.add(menuPanel, "menu");
        cardLayout.show(mainPanel, "menu");

        // Action Listeners
        btnSalir.addActionListener(e -> System.exit(0));
        btnConsultar.addActionListener(e -> mostrarLibrosDisponibles());
        btnReservar.addActionListener(e -> mostrarPanelReserva());
        btnDevolver.addActionListener(e -> mostrarPanelDevolver());
        btnEstado.addActionListener(e -> mostrarPanelEstado());
        btnMulta.addActionListener(e -> mostrarPanelMulta());
    }

    private void mostrarLibrosDisponibles() {
        // Crear un nuevo panel para la tabla
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(GRIS_FONDO);

        // Panel superior con título y barra de búsqueda
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(GRIS_FONDO);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Título
        JLabel titulo = new JLabel("Libros Disponibles", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(AZUL_BONITO);
        topPanel.add(titulo, BorderLayout.NORTH);

        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(GRIS_FONDO);
        
        JLabel searchLabel = new JLabel("Buscar libro:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchPanel.add(searchLabel);
        
        JTextField searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchPanel.add(searchField);
        
        topPanel.add(searchPanel, BorderLayout.CENTER);
        panelTabla.add(topPanel, BorderLayout.NORTH);

        // Crear el modelo de la tabla
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        modelo.addColumn("ID");
        modelo.addColumn("Título");
        modelo.addColumn("Autor");
        modelo.addColumn("Ejemplares Disponibles");

        // Obtener todos los libros
        List<Libro> todosLosLibros = biblioteca.getLibros();
        
        // Función para agregar libros al modelo
        Runnable actualizarTabla = () -> {
            modelo.setRowCount(0); // Limpiar tabla
            String busqueda = searchField.getText().toLowerCase().trim();
            
            for (Libro libro : todosLosLibros) {
                // Filtrar por título o autor
                if (busqueda.isEmpty() || 
                    libro.getTitulo().toLowerCase().contains(busqueda) ||
                    libro.getAutor().toLowerCase().contains(busqueda)) {
                    
                    Object[] fila = {
                        libro.getId(),
                        libro.getTitulo(),
                        libro.getAutor(),
                        libro.getCantidadDisponible()
                    };
                    modelo.addRow(fila);
                }
            }
        };

        // Agregar todos los libros inicialmente
        actualizarTabla.run();

        // Crear la tabla
        JTable tabla = new JTable(modelo);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Colores de la tabla
        tabla.getTableHeader().setBackground(AZUL_BONITO);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setRowHeight(25);
        tabla.setGridColor(new Color(200, 200, 200));

        // Ajustar el ancho de las columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(150);

        // Agregar la tabla a un JScrollPane
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        panelTabla.add(scrollPane, BorderLayout.CENTER);

        // Agregar listener para la búsqueda en tiempo real
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizarTabla.run(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizarTabla.run(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizarTabla.run(); }
        });

        // Botón para volver al menú principal
        JButton btnVolver = new JButton("Volver al Menú");
        btnVolver.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnVolver.setBackground(new Color(108, 117, 125));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setBorderPainted(false);
        btnVolver.setFocusPainted(false);
        btnVolver.addActionListener(e -> {
            cardLayout.show(mainPanel, "menu");
        });
        panelTabla.add(btnVolver, BorderLayout.SOUTH);

        // Agregar el panel al CardLayout
        mainPanel.add(panelTabla, "libros");
        cardLayout.show(mainPanel, "libros");
    }

    private void mostrarPanelReserva() {
        // Crear panel principal para la reserva
        JPanel panelReserva = new JPanel(new BorderLayout());
        panelReserva.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelReserva.setBackground(GRIS_FONDO); // Color de fondo

        // Panel del título
        JLabel titulo = new JLabel("Reservar Libro", SwingConstants.CENTER); // Quitar emoji
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(AZUL_BONITO); // Color azul
        panelReserva.add(titulo, BorderLayout.NORTH);

        // Panel del formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(GRIS_FONDO); // Color de fondo
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo ID Usuario
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblUsuario = new JLabel("ID de Usuario:");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblUsuario, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        JTextField idUsuarioField = new JTextField(20);
        idUsuarioField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(idUsuarioField, gbc);

        // Lista desplegable de libros
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblSeleccionarLibro = new JLabel("Seleccionar libro:"); // Quitar emoji
        lblSeleccionarLibro.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblSeleccionarLibro, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JComboBox<String> libroComboBox = new JComboBox<>();
        libroComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        libroComboBox.setPreferredSize(new Dimension(300, 25));
        formPanel.add(libroComboBox, gbc);

        // Cargar todos los libros en el desplegable
        List<Libro> todosLosLibros = biblioteca.getLibros();
        for (Libro libro : todosLosLibros) {
            String item = libro.getTitulo() + " - " + libro.getAutor() + 
                         " (" + libro.getCantidadDisponible() + " disponibles)";
            libroComboBox.addItem(item);
        }

        // Botón de reserva
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnReservar = new JButton("Reservar"); // Quitar emoji
        btnReservar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnReservar.setBackground(VERDE_BONITO); // Color verde
        btnReservar.setForeground(Color.WHITE); // Texto blanco
        btnReservar.setBorderPainted(false); // Sin borde
        btnReservar.setFocusPainted(false); // Sin focus
        formPanel.add(btnReservar, gbc);

        panelReserva.add(formPanel, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(GRIS_FONDO); // Color de fondo
        JButton btnVolver = new JButton("Volver al Menú"); // Quitar emoji
        btnVolver.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnVolver.setBackground(new Color(108, 117, 125)); // Color gris
        btnVolver.setForeground(Color.WHITE); // Texto blanco
        btnVolver.setBorderPainted(false); // Sin borde
        btnVolver.setFocusPainted(false); // Sin focus
        buttonPanel.add(btnVolver);

        panelReserva.add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnReservar.addActionListener(e -> {
            try {
                int idUsuario = Integer.parseInt(idUsuarioField.getText().trim());
                
                if (libroComboBox.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this,
                            "Por favor seleccione un libro",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String libroSeleccionado = libroComboBox.getSelectedItem().toString();
                // Extraer el título del libro del formato "Título - Autor (X disponibles)"
                String tituloLibro = libroSeleccionado.split(" - ")[0];

                Usuario usuario = biblioteca.buscarUsuarioPorId(idUsuario);
                Libro libro = biblioteca.buscarLibroPorTitulo(tituloLibro);
                biblioteca.reservarLibro(usuario, libro);

                JOptionPane.showMessageDialog(this,
                        "Reserva exitosa!\nLibro: " + libro.getTitulo() + "\nEjemplares disponibles: "
                                + libro.getCantidadDisponible(), // Quitar emoji
                        "Exito",
                        JOptionPane.INFORMATION_MESSAGE);

                // Limpiar campos
                idUsuarioField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Por favor ingrese un ID de usuario válido",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (UsuarioNoEncontradoException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (LibroNoDisponibleException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (MultaPendienteException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnVolver.addActionListener(e -> {
            cardLayout.show(mainPanel, "menu");
        });

        // Agregar el panel al CardLayout
        mainPanel.add(panelReserva, "reserva");
        cardLayout.show(mainPanel, "reserva");
    }

    private void mostrarPanelEstado() {
        // Crear panel principal para consultar estado
        JPanel panelEstado = new JPanel(new BorderLayout());
        panelEstado.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelEstado.setBackground(GRIS_FONDO); // Color de fondo

        // Panel del título
        JLabel titulo = new JLabel("Consultar Estado del Usuario", SwingConstants.CENTER); // Quitar emoji
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(AZUL_BONITO); // Color azul
        panelEstado.add(titulo, BorderLayout.NORTH);

        // Panel del formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(GRIS_FONDO); // Color de fondo
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo ID Usuario
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblUsuario = new JLabel("ID de Usuario:");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblUsuario, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        JTextField idUsuarioField = new JTextField(20);
        idUsuarioField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(idUsuarioField, gbc);

        // Botón de consulta
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnConsultar = new JButton("Consultar Estado"); // Quitar emoji
        btnConsultar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConsultar.setBackground(AZUL_BONITO); // Color azul
        btnConsultar.setForeground(Color.WHITE); // Texto blanco
        btnConsultar.setBorderPainted(false); // Sin borde
        btnConsultar.setFocusPainted(false); // Sin focus
        formPanel.add(btnConsultar, gbc);

        panelEstado.add(formPanel, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(GRIS_FONDO); // Color de fondo
        JButton btnVolver = new JButton("Volver al Menú"); // Quitar emoji
        btnVolver.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnVolver.setBackground(new Color(108, 117, 125)); // Color gris
        btnVolver.setForeground(Color.WHITE); // Texto blanco
        btnVolver.setBorderPainted(false); // Sin borde
        btnVolver.setFocusPainted(false); // Sin focus
        buttonPanel.add(btnVolver);

        panelEstado.add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnConsultar.addActionListener(e -> {
            try {
                int idUsuario = Integer.parseInt(idUsuarioField.getText().trim());
                Usuario usuario = biblioteca.buscarUsuarioPorId(idUsuario);

                // Construir el mensaje de estado con información completa
                StringBuilder estado = new StringBuilder();
                estado.append("Estado de ").append(usuario.nombre).append(":\n");
                estado.append("Sancionado: ").append(usuario.sancionado).append("\n");
                estado.append("Multa pendiente: $").append(usuario.multaPendiente).append("\n\n");
                
                // Agregar información de libros reservados
                if (usuario.tieneLibrosReservados()) {
                    estado.append("Libros reservados:\n");
                    List<Libro> librosReservados = usuario.getLibrosReservados();
                    for (int i = 0; i < librosReservados.size(); i++) {
                        Libro libro = librosReservados.get(i);
                        estado.append(i + 1).append(". ").append(libro.getTitulo())
                              .append(" - ").append(libro.getAutor()).append("\n");
                    }
                } else {
                    estado.append("No tiene libros reservados.");
                }

                // Mostrar en una ventana emergente
                JOptionPane.showMessageDialog(this,
                        estado.toString(),
                        "Estado del Usuario",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Por favor ingrese un ID de usuario válido",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (UsuarioNoEncontradoException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnVolver.addActionListener(e -> {
            cardLayout.show(mainPanel, "menu");
        });

        // Agregar el panel al CardLayout
        mainPanel.add(panelEstado, "estado");
        cardLayout.show(mainPanel, "estado");
    }

    private void mostrarPanelMulta() {
        // Crear panel principal para pagar multa
        JPanel panelMulta = new JPanel(new BorderLayout());
        panelMulta.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelMulta.setBackground(GRIS_FONDO); // Color de fondo

        // Panel del título
        JLabel titulo = new JLabel("Pagar Multa", SwingConstants.CENTER); // Quitar emoji
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(AZUL_BONITO); // Color azul
        panelMulta.add(titulo, BorderLayout.NORTH);

        // Panel del formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(GRIS_FONDO); // Color de fondo
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo ID Usuario
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblUsuario = new JLabel("ID de Usuario:");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblUsuario, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        JTextField idUsuarioField = new JTextField(20);
        idUsuarioField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(idUsuarioField, gbc);

        // Panel para mostrar la información de la multa
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        infoPanel.setBackground(GRIS_FONDO); // Color de fondo
        JTextArea infoArea = new JTextArea(3, 30);
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoArea.setBackground(Color.WHITE); // Fondo blanco
        infoPanel.add(new JScrollPane(infoArea), BorderLayout.CENTER);

        // Botón de consulta
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnConsultar = new JButton("Consultar Multa"); // Quitar emoji
        btnConsultar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConsultar.setBackground(AZUL_BONITO); // Color azul
        btnConsultar.setForeground(Color.WHITE); // Texto blanco
        btnConsultar.setBorderPainted(false); // Sin borde
        btnConsultar.setFocusPainted(false); // Sin focus
        formPanel.add(btnConsultar, gbc);

        // Botón de pago
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnPagar = new JButton("Pagar Multa"); // Quitar emoji
        btnPagar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPagar.setBackground(VERDE_BONITO); // Color verde
        btnPagar.setForeground(Color.WHITE); // Texto blanco
        btnPagar.setBorderPainted(false); // Sin borde
        btnPagar.setFocusPainted(false); // Sin focus
        btnPagar.setEnabled(false);
        formPanel.add(btnPagar, gbc);

        panelMulta.add(formPanel, BorderLayout.CENTER);
        panelMulta.add(infoPanel, BorderLayout.SOUTH);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(GRIS_FONDO); // Color de fondo
        JButton btnVolver = new JButton("Volver al Menú"); // Quitar emoji
        btnVolver.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnVolver.setBackground(new Color(108, 117, 125)); // Color gris
        btnVolver.setForeground(Color.WHITE); // Texto blanco
        btnVolver.setBorderPainted(false); // Sin borde
        btnVolver.setFocusPainted(false); // Sin focus
        buttonPanel.add(btnVolver);

        panelMulta.add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnConsultar.addActionListener(e -> {
            try {
                int idUsuario = Integer.parseInt(idUsuarioField.getText().trim());
                Usuario usuario = biblioteca.buscarUsuarioPorId(idUsuario);

                if (usuario.sancionado) {
                    StringBuilder info = new StringBuilder();
                    info.append("Informacion de la Multa:\n\n"); // Quitar emoji
                    info.append("El usuario tiene una multa pendiente.\n");
                    info.append("Monto de la Multa: $").append(usuario.multaPendiente).append("\n"); // Quitar emoji
                    infoArea.setText(info.toString());
                    btnPagar.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "El usuario no tiene multas pendientes.\nPuede realizar reservas normalmente.", // Quitar emoji
                            "Sin Multas",
                            JOptionPane.INFORMATION_MESSAGE);
                    infoArea.setText("");
                    btnPagar.setEnabled(false);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Por favor ingrese un ID de usuario válido",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (UsuarioNoEncontradoException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnPagar.addActionListener(e -> {
            try {
                int idUsuario = Integer.parseInt(idUsuarioField.getText().trim());
                Usuario usuario = biblioteca.buscarUsuarioPorId(idUsuario);

                if (usuario.sancionado) {
                    usuario.pagarMulta();
                    
                    // Actualizar en la base de datos
                    UsuarioDAO.actualizarUsuario(usuario);
                    
                    JOptionPane.showMessageDialog(this,
                            "Multa pagada exitosamente",
                            "Exito",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Limpiar y actualizar la interfaz
                    infoArea.setText("El usuario no tiene multas pendientes.\nPuede realizar reservas normalmente.");
                    btnPagar.setEnabled(false);
                    idUsuarioField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "El usuario no tiene multas pendientes",
                            "Informacion",
                            JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (UsuarioNoEncontradoException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnVolver.addActionListener(e -> {
            cardLayout.show(mainPanel, "menu");
        });

        // Agregar el panel al CardLayout
        mainPanel.add(panelMulta, "multa");
        cardLayout.show(mainPanel, "multa");
    }

    private void mostrarPanelDevolver() {
        // Crear panel principal para devolver libro
        JPanel panelDevolver = new JPanel(new BorderLayout());
        panelDevolver.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelDevolver.setBackground(GRIS_FONDO); // Color de fondo

        // Panel del título
        JLabel titulo = new JLabel("Devolver Libro", SwingConstants.CENTER); // Quitar emoji
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(AZUL_BONITO); // Color azul
        panelDevolver.add(titulo, BorderLayout.NORTH);

        // Panel del formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(GRIS_FONDO); // Color de fondo
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo ID Usuario
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblUsuario = new JLabel("ID de Usuario:");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblUsuario, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        JTextField idUsuarioField = new JTextField(20);
        idUsuarioField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(idUsuarioField, gbc);

        // Lista desplegable de libros reservados
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblSeleccionarLibro = new JLabel("Seleccionar libro a devolver:");
        lblSeleccionarLibro.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblSeleccionarLibro, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JComboBox<String> libroComboBox = new JComboBox<>();
        libroComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        libroComboBox.setPreferredSize(new Dimension(300, 25));
        formPanel.add(libroComboBox, gbc);

        // Botón para consultar libros reservados
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnConsultar = new JButton("Consultar Libros Reservados");
        btnConsultar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConsultar.setBackground(AZUL_BONITO);
        btnConsultar.setForeground(Color.WHITE);
        btnConsultar.setBorderPainted(false);
        btnConsultar.setFocusPainted(false);
        formPanel.add(btnConsultar, gbc);

        // Botón de devolver
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnDevolver = new JButton("Devolver Libro"); // Quitar emoji
        btnDevolver.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDevolver.setBackground(VERDE_BONITO); // Color verde
        btnDevolver.setForeground(Color.WHITE); // Texto blanco
        btnDevolver.setBorderPainted(false); // Sin borde
        btnDevolver.setFocusPainted(false); // Sin focus
        btnDevolver.setEnabled(false); // Deshabilitado hasta consultar
        formPanel.add(btnDevolver, gbc);

        panelDevolver.add(formPanel, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(GRIS_FONDO); // Color de fondo
        JButton btnVolver = new JButton("Volver al Menú"); // Quitar emoji
        btnVolver.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnVolver.setBackground(new Color(108, 117, 125)); // Color gris
        btnVolver.setForeground(Color.WHITE); // Texto blanco
        btnVolver.setBorderPainted(false); // Sin borde
        btnVolver.setFocusPainted(false); // Sin focus
        buttonPanel.add(btnVolver);

        panelDevolver.add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnConsultar.addActionListener(e -> {
            try {
                int idUsuario = Integer.parseInt(idUsuarioField.getText().trim());
                Usuario usuario = biblioteca.buscarUsuarioPorId(idUsuario);
                
                // Limpiar el combo box
                libroComboBox.removeAllItems();
                
                // Verificar si el usuario tiene libros reservados
                if (!usuario.tieneLibrosReservados()) {
                    JOptionPane.showMessageDialog(this,
                            "El usuario no tiene libros reservados para devolver.",
                            "Sin Libros Reservados",
                            JOptionPane.INFORMATION_MESSAGE);
                    btnDevolver.setEnabled(false);
                    return;
                }
                
                // Cargar solo los libros que tiene reservados
                List<Libro> librosReservados = usuario.getLibrosReservados();
                for (Libro libro : librosReservados) {
                    String item = libro.getTitulo() + " - " + libro.getAutor();
                    libroComboBox.addItem(item);
                }
                
                btnDevolver.setEnabled(true);
                JOptionPane.showMessageDialog(this,
                        "Se encontraron " + librosReservados.size() + " libro(s) reservado(s).",
                        "Libros Encontrados",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Por favor ingrese un ID de usuario válido",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (UsuarioNoEncontradoException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDevolver.addActionListener(e -> {
            try {
                int idUsuario = Integer.parseInt(idUsuarioField.getText().trim());
                
                if (libroComboBox.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this,
                            "Por favor seleccione un libro",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Usuario usuario = biblioteca.buscarUsuarioPorId(idUsuario);
                
                // Verificar si el usuario tiene multa pendiente
                if (usuario.sancionado) {
                    JOptionPane.showMessageDialog(this,
                            "No puede devolver libros hasta pagar la multa pendiente de $" + usuario.multaPendiente,
                            "Multa Pendiente",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String libroSeleccionado = libroComboBox.getSelectedItem().toString();
                // Extraer el título del libro del formato "Título - Autor"
                String tituloLibro = libroSeleccionado.split(" - ")[0];

                Libro libro = biblioteca.buscarLibroPorTitulo(tituloLibro);
                
                // Usar el método devolverLibro de la biblioteca
                biblioteca.devolverLibro(usuario, libro);

                JOptionPane.showMessageDialog(this,
                        "Libro devuelto exitosamente!\nLibro: " + libro.getTitulo() + "\nEjemplares disponibles: "
                                + libro.getCantidadDisponible(),
                        "Exito",
                        JOptionPane.INFORMATION_MESSAGE);

                // Limpiar campos y deshabilitar botón
                idUsuarioField.setText("");
                libroComboBox.removeAllItems();
                btnDevolver.setEnabled(false);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Por favor ingrese un ID de usuario válido",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (UsuarioNoEncontradoException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al devolver el libro: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnVolver.addActionListener(e -> {
            cardLayout.show(mainPanel, "menu");
        });

        // Agregar el panel al CardLayout
        mainPanel.add(panelDevolver, "devolver");
        cardLayout.show(mainPanel, "devolver");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SistemaBibliotecaGUI gui = new SistemaBibliotecaGUI();
            gui.setVisible(true);
        });
    }
}