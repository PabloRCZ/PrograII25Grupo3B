/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.GranjaManager;

import gt.edu.miumg.db.*;
import gt.edu.miumg.db.exceptions.*;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Clase principal para gestionar las operaciones de la granja
 */
public class GranjaManager {
    
    private EntityManagerFactory emf;
    private Scanner scanner;
    
    // Controladores JPA
    private UsuarioJpaController usuarioCtrl;
    private RolJpaController rolCtrl;
    private ClienteJpaController clienteCtrl;
    private ProveedorJpaController proveedorCtrl;
    private InventarioJpaController inventarioCtrl;
    private InventarioAnimalesJpaController animalesCtrl;
    private VentaJpaController ventaCtrl;
    private CompraJpaController compraCtrl;
    private FacturaJpaController facturaCtrl;
    private DetalleVentaJpaController detalleVentaCtrl;
    private DetalleCompraJpaController detalleCompraCtrl;
    private EventoAnimalJpaController eventoAnimalCtrl;
    private BitacoraAccesoJpaController bitacoraCtrl;
    
// Agregar este constructor alternativo a la clase GranjaManager existente:

public GranjaManager(EntityManagerFactory emf, Scanner scanner) {
    // Constructor que acepta emf y scanner externos
    this.emf = emf;
    this.scanner = scanner;
    
    // Inicializar controladores
    usuarioCtrl = new UsuarioJpaController(emf);
    rolCtrl = new RolJpaController(emf);
    clienteCtrl = new ClienteJpaController(emf);
    proveedorCtrl = new ProveedorJpaController(emf);
    inventarioCtrl = new InventarioJpaController(emf);
    animalesCtrl = new InventarioAnimalesJpaController(emf);
    ventaCtrl = new VentaJpaController(emf);
    compraCtrl = new CompraJpaController(emf);
    facturaCtrl = new FacturaJpaController(emf);
    detalleVentaCtrl = new DetalleVentaJpaController(emf);
    detalleCompraCtrl = new DetalleCompraJpaController(emf);
    eventoAnimalCtrl = new EventoAnimalJpaController(emf);
    bitacoraCtrl = new BitacoraAccesoJpaController(emf);
}

// Modificar el método cerrar para NO cerrar scanner ni emf (serán manejados por Main):
public void cerrar() {
    // No cerrar recursos aquí, serán manejados externamente
}

// Mantener el constructor original y método main para compatibilidad:
public GranjaManager() {
    // Inicializar EntityManagerFactory
    emf = Persistence.createEntityManagerFactory("gt.edu.umg_Granja_jar_1.0-SNAPSHOTPU");
    scanner = new Scanner(System.in);
    
    // Inicializar controladores...
    // (código existente)
}

// El método main original puede quedar como alternativa:
public static void main(String[] args) {
    // Este main es la versión SIN login (legacy)
    GranjaManager manager = new GranjaManager();
    manager.mostrarMenu();
}
    // ==================== OPERACIONES DE USUARIO ====================
    
    public void crearUsuario() {
        try {
            System.out.println("\n=== CREAR NUEVO USUARIO ===");
            
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            
            System.out.print("Email: ");
            String email = scanner.nextLine();
            
            System.out.print("Contraseña: ");
            String password = scanner.nextLine();
            
            System.out.println("\nRoles disponibles:");
            List<Rol> roles = rolCtrl.findRolEntities();
            for (Rol rol : roles) {
                System.out.println(rol.getId() + ". " + rol.getNombre());
            }
            
            System.out.print("Seleccione ID del rol: ");
            int rolId = Integer.parseInt(scanner.nextLine());
            
            Rol rol = rolCtrl.findRol(rolId);
            if (rol == null) {
                System.out.println("Rol no encontrado!");
                return;
            }
            
            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setContraseña(password); // En producción, usar hash
            usuario.setRolId(rol);
            usuario.setFechaRegistro(new Date());
            
            usuarioCtrl.create(usuario);
            System.out.println("✓ Usuario creado exitosamente!");
            
        } catch (Exception e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
        }
    }
    
    public void listarUsuarios() {
        System.out.println("\n=== LISTA DE USUARIOS ===");
        List<Usuario> usuarios = usuarioCtrl.findUsuarioEntities();
        
        for (Usuario u : usuarios) {
            System.out.printf("ID: %d | Nombre: %s | Email: %s | Rol: %s%n",
                    u.getId(), u.getNombre(), u.getEmail(), 
                    u.getRolId().getNombre());
        }
    }
    
    public void actualizarUsuario() {
        try {
            System.out.print("ID del usuario a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine());
            
            Usuario usuario = usuarioCtrl.findUsuario(id);
            if (usuario == null) {
                System.out.println("Usuario no encontrado!");
                return;
            }
            
            System.out.print("Nuevo nombre (actual: " + usuario.getNombre() + "): ");
            String nombre = scanner.nextLine();
            if (!nombre.isEmpty()) {
                usuario.setNombre(nombre);
            }
            
            System.out.print("Nuevo email (actual: " + usuario.getEmail() + "): ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) {
                usuario.setEmail(email);
            }
            
            usuarioCtrl.edit(usuario);
            System.out.println("✓ Usuario actualizado exitosamente!");
            
        } catch (Exception e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
        }
    }
    
    // ==================== OPERACIONES DE CLIENTE ====================
    
    public void crearCliente() {
        try {
            System.out.println("\n=== CREAR NUEVO CLIENTE ===");
            
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            
            System.out.print("Teléfono: ");
            String telefono = scanner.nextLine();
            
            System.out.print("Dirección: ");
            String direccion = scanner.nextLine();
            
            System.out.print("Email: ");
            String email = scanner.nextLine();
            
            Cliente cliente = new Cliente();
            cliente.setNombre(nombre);
            cliente.setTelefono(telefono);
            cliente.setDireccion(direccion);
            cliente.setEmail(email);
            cliente.setFechaRegistro(new Date());
            
            clienteCtrl.create(cliente);
            System.out.println("✓ Cliente creado exitosamente!");
            
        } catch (Exception e) {
            System.err.println("Error al crear cliente: " + e.getMessage());
        }
    }
    
    public void listarClientes() {
        System.out.println("\n=== LISTA DE CLIENTES ===");
        List<Cliente> clientes = clienteCtrl.findClienteEntities();
        
        for (Cliente c : clientes) {
            System.out.printf("ID: %d | Nombre: %s | Tel: %s | Email: %s%n",
                    c.getId(), c.getNombre(), c.getTelefono(), c.getEmail());
        }
    }
    
    // ==================== OPERACIONES DE PROVEEDOR ====================
    
    public void crearProveedor() {
        try {
            System.out.println("\n=== CREAR NUEVO PROVEEDOR ===");
            
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            
            System.out.print("Teléfono: ");
            String telefono = scanner.nextLine();
            
            System.out.print("Dirección: ");
            String direccion = scanner.nextLine();
            
            System.out.print("Producto que suministra: ");
            String producto = scanner.nextLine();
            
            System.out.print("Email: ");
            String email = scanner.nextLine();
            
            Proveedor proveedor = new Proveedor();
            proveedor.setNombre(nombre);
            proveedor.setTelefono(telefono);
            proveedor.setDireccion(direccion);
            proveedor.setProducto(producto);
            proveedor.setEmail(email);
            
            proveedorCtrl.create(proveedor);
            System.out.println("✓ Proveedor creado exitosamente!");
            
        } catch (Exception e) {
            System.err.println("Error al crear proveedor: " + e.getMessage());
        }
    }
    
    public void listarProveedores() {
        System.out.println("\n=== LISTA DE PROVEEDORES ===");
        List<Proveedor> proveedores = proveedorCtrl.findProveedorEntities();
        
        for (Proveedor p : proveedores) {
            System.out.printf("ID: %d | Nombre: %s | Producto: %s | Tel: %s%n",
                    p.getId(), p.getNombre(), p.getProducto(), p.getTelefono());
        }
    }
    
    // ==================== OPERACIONES DE INVENTARIO ====================
    
    public void crearProductoInventario() {
        try {
            System.out.println("\n=== AGREGAR PRODUCTO AL INVENTARIO ===");
            
            System.out.print("Nombre del producto: ");
            String nombre = scanner.nextLine();
            
            System.out.print("Descripción: ");
            String descripcion = scanner.nextLine();
            
            System.out.print("Cantidad: ");
            int cantidad = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Unidad (kg, litros, unidades, etc.): ");
            String unidad = scanner.nextLine();
            
            System.out.print("Precio de costo: ");
            BigDecimal precioCosto = new BigDecimal(scanner.nextLine());
            
            System.out.print("Precio de venta: ");
            BigDecimal precioVenta = new BigDecimal(scanner.nextLine());
            
            System.out.print("Mínimo en stock: ");
            int minimoStock = Integer.parseInt(scanner.nextLine());
            
            Inventario inventario = new Inventario();
            inventario.setNombre(nombre);
            inventario.setDescripcion(descripcion);
            inventario.setCantidad(cantidad);
            inventario.setUnidad(unidad);
            inventario.setPrecioCosto(precioCosto);
            inventario.setPrecioVenta(precioVenta);
            inventario.setMinimoStock(minimoStock);
            inventario.setCreatedAt(new Date());
            
            inventarioCtrl.create(inventario);
            System.out.println("✓ Producto agregado al inventario exitosamente!");
            
        } catch (Exception e) {
            System.err.println("Error al crear producto: " + e.getMessage());
        }
    }
    
    public void listarInventario() {
        System.out.println("\n=== INVENTARIO DE PRODUCTOS ===");
        List<Inventario> productos = inventarioCtrl.findInventarioEntities();
        
        for (Inventario i : productos) {
            System.out.printf("ID: %d | %s | Cantidad: %d %s | Precio: Q%.2f%n",
                    i.getId(), i.getNombre(), i.getCantidad(), 
                    i.getUnidad(), i.getPrecioVenta());
            
            if (i.getCantidad() <= i.getMinimoStock()) {
                System.out.println("  ⚠ ALERTA: Stock bajo!");
            }
        }
    }
    
    // ==================== OPERACIONES DE ANIMALES ====================
    
    public void registrarAnimal() {
        try {
            System.out.println("\n=== REGISTRAR NUEVO ANIMAL ===");
            
            System.out.print("Especie: ");
            String especie = scanner.nextLine();
            
            System.out.print("Raza: ");
            String raza = scanner.nextLine();
            
            System.out.print("Peso (kg): ");
            BigDecimal peso = new BigDecimal(scanner.nextLine());
            
            System.out.print("Edad (meses): ");
            int edad = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Estado (Sano/Enfermo/En tratamiento): ");
            String estado = scanner.nextLine();
            
            System.out.print("Identificador único: ");
            String identificador = scanner.nextLine();
            
            System.out.print("Observaciones: ");
            String observaciones = scanner.nextLine();
            
            InventarioAnimales animal = new InventarioAnimales();
            animal.setEspecie(especie);
            animal.setRaza(raza);
            animal.setPeso(peso);
            animal.setEdad(edad);
            animal.setEstado(estado);
            animal.setFechaIngreso(new Date());
            animal.setIdentificadorUnico(identificador);
            animal.setObservaciones(observaciones);
            animal.setCreatedAt(new Date());
            
            animalesCtrl.create(animal);
            System.out.println("✓ Animal registrado exitosamente!");
            
        } catch (Exception e) {
            System.err.println("Error al registrar animal: " + e.getMessage());
        }
    }
    
    public void listarAnimales() {
        System.out.println("\n=== INVENTARIO DE ANIMALES ===");
        List<InventarioAnimales> animales = animalesCtrl.findInventarioAnimalesEntities();
        
        for (InventarioAnimales a : animales) {
            System.out.printf("ID: %d | %s - %s | Peso: %.2f kg | Estado: %s | ID: %s%n",
                    a.getId(), a.getEspecie(), a.getRaza(), a.getPeso(), 
                    a.getEstado(), a.getIdentificadorUnico());
        }
    }
    
    public void registrarEventoAnimal() {
        try {
            listarAnimales();
            
            System.out.print("\nID del animal: ");
            int animalId = Integer.parseInt(scanner.nextLine());
            
            InventarioAnimales animal = animalesCtrl.findInventarioAnimales(animalId);
            if (animal == null) {
                System.out.println("Animal no encontrado!");
                return;
            }
            
            System.out.println("\nTipos de evento:");
            System.out.println("1. Vacunación");
            System.out.println("2. Enfermedad");
            System.out.println("3. Tratamiento");
            System.out.println("4. Reproducción");
            System.out.println("5. Otro");
            
            System.out.print("Seleccione tipo: ");
            int opcion = Integer.parseInt(scanner.nextLine());
            
            String tipo = "";
            switch(opcion) {
                case 1: tipo = "Vacunación"; break;
                case 2: tipo = "Enfermedad"; break;
                case 3: tipo = "Tratamiento"; break;
                case 4: tipo = "Reproducción"; break;
                case 5: 
                    System.out.print("Especifique tipo: ");
                    tipo = scanner.nextLine();
                    break;
            }
            
            System.out.print("Descripción del evento: ");
            String descripcion = scanner.nextLine();
            
            EventoAnimal evento = new EventoAnimal();
            evento.setAnimalId(animal);
            evento.setTipo(tipo);
            evento.setDescripcion(descripcion);
            evento.setFecha(new Date());
            evento.setCreatedAt(new Date());
            
            eventoAnimalCtrl.create(evento);
            System.out.println("✓ Evento registrado exitosamente!");
            
        } catch (Exception e) {
            System.err.println("Error al registrar evento: " + e.getMessage());
        }
    }
    
    // ==================== OPERACIONES DE VENTA ====================
    
    public void registrarVenta() {
        try {
            System.out.println("\n=== REGISTRAR NUEVA VENTA ===");
            
            // Seleccionar cliente
            listarClientes();
            System.out.print("\nID del cliente: ");
            int clienteId = Integer.parseInt(scanner.nextLine());
            Cliente cliente = clienteCtrl.findCliente(clienteId);
            
            if (cliente == null) {
                System.out.println("Cliente no encontrado!");
                return;
            }
            
            // Seleccionar usuario
            System.out.print("ID del usuario que realiza la venta: ");
            int usuarioId = Integer.parseInt(scanner.nextLine());
            Usuario usuario = usuarioCtrl.findUsuario(usuarioId);
            
            if (usuario == null) {
                System.out.println("Usuario no encontrado!");
                return;
            }
            
            System.out.print("Método de pago (Efectivo/Tarjeta/Transferencia): ");
            String metodoPago = scanner.nextLine();
            
            // Crear venta
            Venta venta = new Venta();
            venta.setClienteId(cliente);
            venta.setUsuarioId(usuario);
            venta.setFecha(new Date());
            venta.setMetodoPago(metodoPago);
            venta.setTotal(BigDecimal.ZERO);
            venta.setCreatedAt(new Date());
            
            ventaCtrl.create(venta);
            
            // Agregar detalles
            BigDecimal totalVenta = BigDecimal.ZERO;
            boolean agregarMas = true;
            
            while (agregarMas) {
                System.out.println("\n¿Agregar producto o animal?");
                System.out.println("1. Producto");
                System.out.println("2. Animal");
                System.out.print("Opción: ");
                int tipo = Integer.parseInt(scanner.nextLine());
                
                DetalleVenta detalle = new DetalleVenta();
                detalle.setVentaId(venta);
                
                if (tipo == 1) {
                    listarInventario();
                    System.out.print("ID del producto: ");
                    int prodId = Integer.parseInt(scanner.nextLine());
                    Inventario producto = inventarioCtrl.findInventario(prodId);
                    
                    if (producto == null) {
                        System.out.println("Producto no encontrado!");
                        continue;
                    }
                    
                    detalle.setProductoId(producto);
                    detalle.setPrecioUnitario(producto.getPrecioVenta());
                    
                } else {
                    listarAnimales();
                    System.out.print("ID del animal: ");
                    int animalId = Integer.parseInt(scanner.nextLine());
                    InventarioAnimales animal = animalesCtrl.findInventarioAnimales(animalId);
                    
                    if (animal == null) {
                        System.out.println("Animal no encontrado!");
                        continue;
                    }
                    
                    detalle.setAnimalId(animal);
                    System.out.print("Precio del animal: ");
                    detalle.setPrecioUnitario(new BigDecimal(scanner.nextLine()));
                }
                
                System.out.print("Cantidad: ");
                int cantidad = Integer.parseInt(scanner.nextLine());
                detalle.setCantidad(cantidad);
                
                BigDecimal subtotal = detalle.getPrecioUnitario()
                        .multiply(new BigDecimal(cantidad));
                detalle.setSubtotal(subtotal);
                
                detalleVentaCtrl.create(detalle);
                totalVenta = totalVenta.add(subtotal);
                
                System.out.print("¿Agregar más items? (S/N): ");
                agregarMas = scanner.nextLine().equalsIgnoreCase("S");
            }
            
            // Actualizar total de venta
            venta.setTotal(totalVenta);
            ventaCtrl.edit(venta);
            
            System.out.println("\n✓ Venta registrada exitosamente!");
            System.out.printf("Total: Q%.2f%n", totalVenta);
            
        } catch (Exception e) {
            System.err.println("Error al registrar venta: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void listarVentas() {
        System.out.println("\n=== LISTA DE VENTAS ===");
        List<Venta> ventas = ventaCtrl.findVentaEntities();
        
        for (Venta v : ventas) {
            System.out.printf("ID: %d | Cliente: %s | Total: Q%.2f | Fecha: %s%n",
                    v.getId(), v.getClienteId().getNombre(), 
                    v.getTotal(), v.getFecha());
        }
    }
    
    // ==================== REGISTRAR BITÁCORA ====================
    
    public void registrarAcceso(int usuarioId, String ip, boolean exito) {
        try {
            Usuario usuario = usuarioCtrl.findUsuario(usuarioId);
            if (usuario == null) return;
            
            BitacoraAcceso bitacora = new BitacoraAcceso();
            bitacora.setUsuarioId(usuario);
            bitacora.setFecha(new Date());
            bitacora.setIpAcceso(ip);
            bitacora.setExito(exito);
            
            bitacoraCtrl.create(bitacora);
            
        } catch (Exception e) {
            System.err.println("Error al registrar acceso: " + e.getMessage());
        }
    }
    
    // ==================== MENÚ PRINCIPAL ====================
    
    public void mostrarMenu() {
        while (true) {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║   SISTEMA DE GESTIÓN DE GRANJA     ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.println("1.  Gestión de Usuarios");
            System.out.println("2.  Gestión de Clientes");
            System.out.println("3.  Gestión de Proveedores");
            System.out.println("4.  Gestión de Inventario");
            System.out.println("5.  Gestión de Animales");
            System.out.println("6.  Registrar Venta");
            System.out.println("7.  Listar Ventas");
            System.out.println("8.  Registrar Compra");
            System.out.println("9.  Reportes");
            System.out.println("0.  Salir");
            System.out.print("\nSeleccione opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1: menuUsuarios(); break;
                    case 2: menuClientes(); break;
                    case 3: menuProveedores(); break;
                    case 4: menuInventario(); break;
                    case 5: menuAnimales(); break;
                    case 6: registrarVenta(); break;
                    case 7: listarVentas(); break;
                    case 0:
                        System.out.println("¡Hasta luego!");
                        cerrar();
                        return;
                    default:
                        System.out.println("Opción no válida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido!");
            }
        }
    }
    
    private void menuUsuarios() {
        System.out.println("\n--- GESTIÓN DE USUARIOS ---");
        System.out.println("1. Crear usuario");
        System.out.println("2. Listar usuarios");
        System.out.println("3. Actualizar usuario");
        System.out.print("Opción: ");
        
        int op = Integer.parseInt(scanner.nextLine());
        switch (op) {
            case 1: crearUsuario(); break;
            case 2: listarUsuarios(); break;
            case 3: actualizarUsuario(); break;
        }
    }
    
    private void menuClientes() {
        System.out.println("\n--- GESTIÓN DE CLIENTES ---");
        System.out.println("1. Crear cliente");
        System.out.println("2. Listar clientes");
        System.out.print("Opción: ");
        
        int op = Integer.parseInt(scanner.nextLine());
        switch (op) {
            case 1: crearCliente(); break;
            case 2: listarClientes(); break;
        }
    }
    
    private void menuProveedores() {
        System.out.println("\n--- GESTIÓN DE PROVEEDORES ---");
        System.out.println("1. Crear proveedor");
        System.out.println("2. Listar proveedores");
        System.out.print("Opción: ");
        
        int op = Integer.parseInt(scanner.nextLine());
        switch (op) {
            case 1: crearProveedor(); break;
            case 2: listarProveedores(); break;
        }
    }
    
    private void menuInventario() {
        System.out.println("\n--- GESTIÓN DE INVENTARIO ---");
        System.out.println("1. Agregar producto");
        System.out.println("2. Listar productos");
        System.out.print("Opción: ");
        
        int op = Integer.parseInt(scanner.nextLine());
        switch (op) {
            case 1: crearProductoInventario(); break;
            case 2: listarInventario(); break;
        }
    }
    
    private void menuAnimales() {
        System.out.println("\n--- GESTIÓN DE ANIMALES ---");
        System.out.println("1. Registrar animal");
        System.out.println("2. Listar animales");
        System.out.println("3. Registrar evento");
        System.out.print("Opción: ");
        
        int op = Integer.parseInt(scanner.nextLine());
        switch (op) {
            case 1: registrarAnimal(); break;
            case 2: listarAnimales(); break;
            case 3: registrarEventoAnimal(); break;
        }
    }
}