/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
/*
 * Sistema de Login y Control de Acceso
 */
package gt.edu.miumg.GranjaManager;

import gt.edu.miumg.db.*;
import javax.persistence.EntityManagerFactory;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Clase para manejar el login y control de acceso basado en roles
 */
public class LoginManager {
    
    private EntityManagerFactory emf;
    private Scanner scanner;
    private UsuarioJpaController usuarioCtrl;
    private BitacoraAccesoJpaController bitacoraCtrl;
    private Usuario usuarioActual;
    
    public LoginManager(EntityManagerFactory emf, Scanner scanner) {
        this.emf = emf;
        this.scanner = scanner;
        this.usuarioCtrl = new UsuarioJpaController(emf);
        this.bitacoraCtrl = new BitacoraAccesoJpaController(emf);
    }
    
    /**
     * Realiza el proceso de login
     * @return Usuario autenticado o null si falla
     */
    public Usuario login() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║     SISTEMA DE GESTIÓN GRANJA     ║");
        System.out.println("║          INICIO DE SESIÓN          ║");
        System.out.println("╚════════════════════════════════════╝");
        
        int intentos = 0;
        final int MAX_INTENTOS = 3;
        
        while (intentos < MAX_INTENTOS) {
            System.out.print("\nEmail: ");
            String email = scanner.nextLine().trim();
            
            System.out.print("Contraseña: ");
            String password = scanner.nextLine();
            
            // Buscar usuario
            Usuario usuario = buscarUsuarioPorEmail(email);
            
            if (usuario != null && usuario.getContraseña().equals(password)) {
                // Login exitoso
                usuarioActual = usuario;
                registrarAcceso(usuario, true);
                
                System.out.println("\n✓ ¡Bienvenido " + usuario.getNombre() + "!");
                System.out.println("Rol: " + usuario.getRolId().getNombre());
                System.out.println("Fecha: " + new Date());
                
                return usuario;
            } else {
                // Login fallido
                intentos++;
                if (usuario != null) {
                    registrarAcceso(usuario, false);
                }
                
                System.out.println("\n✗ Credenciales incorrectas!");
                System.out.println("Intentos restantes: " + (MAX_INTENTOS - intentos));
                
                if (intentos >= MAX_INTENTOS) {
                    System.out.println("\n✗ Máximo de intentos alcanzado. Acceso bloqueado.");
                    return null;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Busca un usuario por su email
     */
    private Usuario buscarUsuarioPorEmail(String email) {
        List<Usuario> usuarios = usuarioCtrl.findUsuarioEntities();
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }
    
    /**
     * Registra el intento de acceso en la bitácora
     */
    private void registrarAcceso(Usuario usuario, boolean exito) {
        try {
            BitacoraAcceso bitacora = new BitacoraAcceso();
            bitacora.setUsuarioId(usuario);
            bitacora.setFecha(new Date());
            bitacora.setIpAcceso("127.0.0.1"); // En producción, obtener IP real
            bitacora.setExito(exito);
            
            bitacoraCtrl.create(bitacora);
        } catch (Exception e) {
            System.err.println("Error al registrar acceso: " + e.getMessage());
        }
    }
    
    /**
     * Verifica si el usuario tiene permiso para una operación
     */
    public boolean tienePermiso(String operacion) {
        if (usuarioActual == null) {
            return false;
        }
        
        String rol = usuarioActual.getRolId().getNombre();
        
        switch (rol) {
            case "Administrador":
                return true; // Administrador tiene todos los permisos
                
            case "Vendedor":
                return operacion.equals("VENTAS") || 
                       operacion.equals("CLIENTES") ||
                       operacion.equals("VER_INVENTARIO") ||
                       operacion.equals("VER_ANIMALES");
                
            case "Comprador":
                return operacion.equals("COMPRAS") || 
                       operacion.equals("PROVEEDORES") ||
                       operacion.equals("INVENTARIO") ||
                       operacion.equals("ANIMALES");
                
            case "Operador":
                return operacion.equals("VER_INVENTARIO") ||
                       operacion.equals("ANIMALES") ||
                       operacion.equals("EVENTOS_ANIMALES");
                
            default:
                return false;
        }
    }
    
    /**
     * Muestra el menú según el rol del usuario
     */
    public void mostrarMenuPorRol(GranjaManager manager) {
        if (usuarioActual == null) {
            System.out.println("Error: No hay usuario logueado");
            return;
        }
        
        String rol = usuarioActual.getRolId().getNombre();
        
        while (true) {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║   Usuario: " + String.format("%-22s", usuarioActual.getNombre()) + "║");
            System.out.println("║   Rol: " + String.format("%-26s", rol) + "║");
            System.out.println("╚════════════════════════════════════╝");
            
            switch (rol) {
                case "Administrador":
                    mostrarMenuAdministrador(manager);
                    break;
                case "Vendedor":
                    mostrarMenuVendedor(manager);
                    break;
                case "Comprador":
                    mostrarMenuComprador(manager);
                    break;
                case "Operador":
                    mostrarMenuOperador(manager);
                    break;
                default:
                    System.out.println("Rol no reconocido");
                    return;
            }
            
            System.out.print("\nSeleccione opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                
                if (opcion == 0) {
                    System.out.println("\n✓ Sesión cerrada. ¡Hasta luego!");
                    usuarioActual = null;
                    return;
                }
                
                procesarOpcion(opcion, rol, manager);
                
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido!");
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
    
    /**
     * Menú para Administrador (acceso total)
     */
    private void mostrarMenuAdministrador(GranjaManager manager) {
        System.out.println("\n=== MENÚ ADMINISTRADOR ===");
        System.out.println("1.  Gestión de Usuarios");
        System.out.println("2.  Gestión de Clientes");
        System.out.println("3.  Gestión de Proveedores");
        System.out.println("4.  Gestión de Inventario");
        System.out.println("5.  Gestión de Animales");
        System.out.println("6.  Registrar Venta");
        System.out.println("7.  Listar Ventas");
        System.out.println("8.  Registrar Compra");
        System.out.println("9.  Listar Compras");
        System.out.println("10. Ver Reportes");
        System.out.println("11. Ver Bitácora de Accesos");
        System.out.println("0.  Cerrar Sesión");
    }
    
    /**
     * Menú para Vendedor
     */
    private void mostrarMenuVendedor(GranjaManager manager) {
        System.out.println("\n=== MENÚ VENDEDOR ===");
        System.out.println("1. Registrar Venta");
        System.out.println("2. Listar Ventas");
        System.out.println("3. Gestión de Clientes");
        System.out.println("4. Ver Inventario");
        System.out.println("5. Ver Animales");
        System.out.println("0. Cerrar Sesión");
    }
    
    /**
     * Menú para Comprador
     */
    private void mostrarMenuComprador(GranjaManager manager) {
        System.out.println("\n=== MENÚ COMPRADOR ===");
        System.out.println("1. Registrar Compra");
        System.out.println("2. Listar Compras");
        System.out.println("3. Gestión de Proveedores");
        System.out.println("4. Gestión de Inventario");
        System.out.println("5. Gestión de Animales");
        System.out.println("0. Cerrar Sesión");
    }
    
    /**
     * Menú para Operador
     */
    private void mostrarMenuOperador(GranjaManager manager) {
        System.out.println("\n=== MENÚ OPERADOR ===");
        System.out.println("1. Ver Inventario");
        System.out.println("2. Gestión de Animales");
        System.out.println("3. Registrar Eventos de Animales");
        System.out.println("0. Cerrar Sesión");
    }
    
    /**
     * Procesa la opción seleccionada según el rol
     */
    private void procesarOpcion(int opcion, String rol, GranjaManager manager) {
        CompraManager compraManager = new CompraManager(emf, scanner);
        
        switch (rol) {
            case "Administrador":
                procesarOpcionAdmin(opcion, manager, compraManager);
                break;
            case "Vendedor":
                procesarOpcionVendedor(opcion, manager);
                break;
            case "Comprador":
                procesarOpcionComprador(opcion, manager, compraManager);
                break;
            case "Operador":
                procesarOpcionOperador(opcion, manager);
                break;
        }
    }
    
    private void procesarOpcionAdmin(int opcion, GranjaManager manager, CompraManager compraManager) {
        switch (opcion) {
            case 1: menuUsuarios(manager); break;
            case 2: menuClientes(manager); break;
            case 3: menuProveedores(manager); break;
            case 4: menuInventario(manager); break;
            case 5: menuAnimales(manager); break;
            case 6: manager.registrarVenta(); break;
            case 7: manager.listarVentas(); break;
            case 8: compraManager.registrarCompra(); break;
            case 9: compraManager.listarCompras(); break;
            case 10: System.out.println("Reportes en desarrollo..."); break;
            case 11: verBitacora(); break;
            default: System.out.println("Opción no válida!");
        }
    }
    
    private void procesarOpcionVendedor(int opcion, GranjaManager manager) {
        switch (opcion) {
            case 1: manager.registrarVenta(); break;
            case 2: manager.listarVentas(); break;
            case 3: menuClientes(manager); break;
            case 4: manager.listarInventario(); break;
            case 5: manager.listarAnimales(); break;
            default: System.out.println("Opción no válida!");
        }
    }
    
    private void procesarOpcionComprador(int opcion, GranjaManager manager, CompraManager compraManager) {
        switch (opcion) {
            case 1: compraManager.registrarCompra(); break;
            case 2: compraManager.listarCompras(); break;
            case 3: menuProveedores(manager); break;
            case 4: menuInventario(manager); break;
            case 5: menuAnimales(manager); break;
            default: System.out.println("Opción no válida!");
        }
    }
    
    private void procesarOpcionOperador(int opcion, GranjaManager manager) {
        switch (opcion) {
            case 1: manager.listarInventario(); break;
            case 2: menuAnimales(manager); break;
            case 3: manager.registrarEventoAnimal(); break;
            default: System.out.println("Opción no válida!");
        }
    }
    
    // Métodos de menú auxiliares
    private void menuUsuarios(GranjaManager manager) {
        System.out.println("\n--- GESTIÓN DE USUARIOS ---");
        System.out.println("1. Crear usuario");
        System.out.println("2. Listar usuarios");
        System.out.println("3. Actualizar usuario");
        System.out.print("Opción: ");
        
        int op = Integer.parseInt(scanner.nextLine());
        switch (op) {
            case 1: manager.crearUsuario(); break;
            case 2: manager.listarUsuarios(); break;
            case 3: manager.actualizarUsuario(); break;
        }
    }
    
    private void menuClientes(GranjaManager manager) {
        System.out.println("\n--- GESTIÓN DE CLIENTES ---");
        System.out.println("1. Crear cliente");
        System.out.println("2. Listar clientes");
        System.out.print("Opción: ");
        
        int op = Integer.parseInt(scanner.nextLine());
        switch (op) {
            case 1: manager.crearCliente(); break;
            case 2: manager.listarClientes(); break;
        }
    }
    
    private void menuProveedores(GranjaManager manager) {
        System.out.println("\n--- GESTIÓN DE PROVEEDORES ---");
        System.out.println("1. Crear proveedor");
        System.out.println("2. Listar proveedores");
        System.out.print("Opción: ");
        
        int op = Integer.parseInt(scanner.nextLine());
        switch (op) {
            case 1: manager.crearProveedor(); break;
            case 2: manager.listarProveedores(); break;
        }
    }
    
    private void menuInventario(GranjaManager manager) {
        System.out.println("\n--- GESTIÓN DE INVENTARIO ---");
        System.out.println("1. Agregar producto");
        System.out.println("2. Listar productos");
        System.out.print("Opción: ");
        
        int op = Integer.parseInt(scanner.nextLine());
        switch (op) {
            case 1: manager.crearProductoInventario(); break;
            case 2: manager.listarInventario(); break;
        }
    }
    
    private void menuAnimales(GranjaManager manager) {
        System.out.println("\n--- GESTIÓN DE ANIMALES ---");
        System.out.println("1. Registrar animal");
        System.out.println("2. Listar animales");
        System.out.println("3. Registrar evento");
        System.out.print("Opción: ");
        
        int op = Integer.parseInt(scanner.nextLine());
        switch (op) {
            case 1: manager.registrarAnimal(); break;
            case 2: manager.listarAnimales(); break;
            case 3: manager.registrarEventoAnimal(); break;
        }
    }
    
    private void verBitacora() {
        System.out.println("\n=== BITÁCORA DE ACCESOS ===");
        List<BitacoraAcceso> registros = bitacoraCtrl.findBitacoraAccesoEntities();
        
        for (BitacoraAcceso b : registros) {
            String estado = b.getExito() ? "✓ EXITOSO" : "✗ FALLIDO";
            System.out.printf("%s | Usuario: %s | IP: %s | Fecha: %s%n",
                    estado, 
                    b.getUsuarioId().getNombre(),
                    b.getIpAcceso(),
                    b.getFecha());
        }
    }
    
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
}