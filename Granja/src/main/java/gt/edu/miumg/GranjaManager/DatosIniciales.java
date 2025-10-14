/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.GranjaManager;

import gt.edu.miumg.db.*;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Clase para insertar datos iniciales en la base de datos
 */
public class DatosIniciales {
    
    private EntityManagerFactory emf;
    
    public DatosIniciales() {
        emf = Persistence.createEntityManagerFactory("gt.edu.umg_Granja_jar_1.0-SNAPSHOTPU");
    }
    
    public void crearRolesIniciales() {
        RolJpaController rolCtrl = new RolJpaController(emf);
        
        try {
            // Verificar si ya existen roles
            if (rolCtrl.getRolCount() > 0) {
                System.out.println("Los roles ya existen en la base de datos.");
                return;
            }
            
            // Crear roles básicos
            Rol admin = new Rol();
            admin.setNombre("Administrador");
            rolCtrl.create(admin);
            
            Rol vendedor = new Rol();
            vendedor.setNombre("Vendedor");
            rolCtrl.create(vendedor);
            
            Rol comprador = new Rol();
            comprador.setNombre("Comprador");
            rolCtrl.create(comprador);
            
            Rol operador = new Rol();
            operador.setNombre("Operador");
            rolCtrl.create(operador);
            
            System.out.println("✓ Roles creados exitosamente!");
            
        } catch (Exception e) {
            System.err.println("Error al crear roles: " + e.getMessage());
        }
    }
    
    public void crearUsuarioAdmin() {
        UsuarioJpaController usuarioCtrl = new UsuarioJpaController(emf);
        RolJpaController rolCtrl = new RolJpaController(emf);
        
        try {
            // Buscar el rol de administrador
            Rol rolAdmin = null;
            for (Rol rol : rolCtrl.findRolEntities()) {
                if (rol.getNombre().equals("Administrador")) {
                    rolAdmin = rol;
                    break;
                }
            }
            
            if (rolAdmin == null) {
                System.out.println("Error: No se encontró el rol de Administrador");
                return;
            }
            
            // Verificar si ya existe un admin
            for (Usuario u : usuarioCtrl.findUsuarioEntities()) {
                if (u.getEmail().equals("admin@granja.com")) {
                    System.out.println("El usuario administrador ya existe.");
                    return;
                }
            }
            
            // Crear usuario administrador
            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setEmail("admin@granja.com");
            admin.setContraseña("admin123"); // En producción usar hash
            admin.setRolId(rolAdmin);
            admin.setFechaRegistro(new Date());
            
            usuarioCtrl.create(admin);
            
            System.out.println("✓ Usuario administrador creado!");
            System.out.println("  Email: admin@granja.com");
            System.out.println("  Contraseña: admin123");
            
        } catch (Exception e) {
            System.err.println("Error al crear usuario admin: " + e.getMessage());
        }
    }
    
    public void crearDatosPrueba() {
        try {
            System.out.println("\n=== CREANDO DATOS DE PRUEBA ===\n");
            
            // Clientes
            crearClientesPrueba();
            
            // Proveedores
            crearProveedoresPrueba();
            
            // Productos en inventario
            crearProductosPrueba();
            
            // Animales
            crearAnimalesPrueba();
            
            System.out.println("\n✓ Datos de prueba creados exitosamente!");
            
        } catch (Exception e) {
            System.err.println("Error al crear datos de prueba: " + e.getMessage());
        }
    }
    
    private void crearClientesPrueba() {
        ClienteJpaController clienteCtrl = new ClienteJpaController(emf);
        
        if (clienteCtrl.getClienteCount() > 0) {
            System.out.println("Los clientes ya existen.");
            return;
        }
        
        String[] nombres = {
            "Juan Pérez", "María García", "Carlos López", 
            "Ana Martínez", "Pedro Rodríguez"
        };
        String[] telefonos = {
            "5551-1234", "5552-5678", "5553-9012",
            "5554-3456", "5555-7890"
        };
        String[] direcciones = {
            "Zona 1, Guatemala", "Zona 10, Guatemala", "Antigua Guatemala",
            "Villa Nueva", "Mixco"
        };
        
        for (int i = 0; i < nombres.length; i++) {
            Cliente cliente = new Cliente();
            cliente.setNombre(nombres[i]);
            cliente.setTelefono(telefonos[i]);
            cliente.setDireccion(direcciones[i]);
            cliente.setEmail(nombres[i].toLowerCase().replace(" ", ".") + "@email.com");
            cliente.setFechaRegistro(new Date());
            
            clienteCtrl.create(cliente);
        }
        
        System.out.println("✓ Clientes de prueba creados: " + nombres.length);
    }
    
    private void crearProveedoresPrueba() {
        ProveedorJpaController proveedorCtrl = new ProveedorJpaController(emf);
        
        if (proveedorCtrl.getProveedorCount() > 0) {
            System.out.println("Los proveedores ya existen.");
            return;
        }
        
        String[][] proveedores = {
            {"Alimentos SA", "Concentrado para animales", "2234-5678"},
            {"Veterinaria El Campo", "Medicamentos veterinarios", "2345-6789"},
            {"Semillas del Valle", "Semillas y granos", "2456-7890"},
            {"Insumos Agrícolas", "Fertilizantes y herbicidas", "2567-8901"}
        };
        
        for (String[] datos : proveedores) {
            Proveedor proveedor = new Proveedor();
            proveedor.setNombre(datos[0]);
            proveedor.setProducto(datos[1]);
            proveedor.setTelefono(datos[2]);
            proveedor.setDireccion("Ciudad de Guatemala");
            proveedor.setEmail(datos[0].toLowerCase().replace(" ", "") + "@proveedor.com");
            
            proveedorCtrl.create(proveedor);
        }
        
        System.out.println("✓ Proveedores de prueba creados: " + proveedores.length);
    }
    
    private void crearProductosPrueba() {
        InventarioJpaController inventarioCtrl = new InventarioJpaController(emf);
        
        if (inventarioCtrl.getInventarioCount() > 0) {
            System.out.println("Los productos ya existen.");
            return;
        }
        
        Object[][] productos = {
            {"Concentrado para pollos", "Alimento balanceado 40kg", 50, "sacos", 
             new BigDecimal("150.00"), new BigDecimal("180.00"), 10},
            {"Concentrado para cerdos", "Alimento balanceado 40kg", 30, "sacos",
             new BigDecimal("145.00"), new BigDecimal("175.00"), 10},
            {"Vacuna Triple", "Vacuna para ganado", 100, "dosis",
             new BigDecimal("15.00"), new BigDecimal("25.00"), 20},
            {"Desparasitante", "Medicamento antiparasitario", 50, "frascos",
             new BigDecimal("35.00"), new BigDecimal("50.00"), 10},
            {"Huevos", "Huevos frescos de gallina", 500, "unidades",
             new BigDecimal("1.00"), new BigDecimal("1.50"), 100},
            {"Leche", "Leche fresca de vaca", 200, "litros",
             new BigDecimal("5.00"), new BigDecimal("7.00"), 50}
        };
        
        for (Object[] datos : productos) {
            Inventario producto = new Inventario();
            producto.setNombre((String) datos[0]);
            producto.setDescripcion((String) datos[1]);
            producto.setCantidad((Integer) datos[2]);
            producto.setUnidad((String) datos[3]);
            producto.setPrecioCosto((BigDecimal) datos[4]);
            producto.setPrecioVenta((BigDecimal) datos[5]);
            producto.setMinimoStock((Integer) datos[6]);
            producto.setCreatedAt(new Date());
            producto.setUpdatedAt(new Date());
            
            inventarioCtrl.create(producto);
        }
        
        System.out.println("✓ Productos de prueba creados: " + productos.length);
    }
    
    private void crearAnimalesPrueba() {
        InventarioAnimalesJpaController animalesCtrl = new InventarioAnimalesJpaController(emf);
        
        if (animalesCtrl.getInventarioAnimalesCount() > 0) {
            System.out.println("Los animales ya existen.");
            return;
        }
        
        Object[][] animales = {
            {"Pollo", "Broiler", new BigDecimal("2.5"), 3, "Sano", "POL-001"},
            {"Pollo", "Broiler", new BigDecimal("2.3"), 3, "Sano", "POL-002"},
            {"Pollo", "Broiler", new BigDecimal("2.7"), 3, "Sano", "POL-003"},
            {"Cerdo", "Yorkshire", new BigDecimal("80.0"), 6, "Sano", "CER-001"},
            {"Cerdo", "Duroc", new BigDecimal("75.5"), 5, "Sano", "CER-002"},
            {"Vaca", "Holstein", new BigDecimal("450.0"), 36, "Sano", "VAC-001"},
            {"Vaca", "Jersey", new BigDecimal("380.0"), 30, "Sano", "VAC-002"},
            {"Cabra", "Alpina", new BigDecimal("35.0"), 12, "Sano", "CAB-001"},
            {"Oveja", "Dorper", new BigDecimal("40.0"), 18, "Sano", "OVE-001"}
        };
        
        for (Object[] datos : animales) {
            InventarioAnimales animal = new InventarioAnimales();
            animal.setEspecie((String) datos[0]);
            animal.setRaza((String) datos[1]);
            animal.setPeso((BigDecimal) datos[2]);
            animal.setEdad((Integer) datos[3]);
            animal.setEstado((String) datos[4]);
            animal.setIdentificadorUnico((String) datos[5]);
            animal.setFechaIngreso(new Date());
            animal.setObservaciones("Animal en buen estado de salud");
            animal.setCreatedAt(new Date());
            animal.setUpdatedAt(new Date());
            
            animalesCtrl.create(animal);
        }
        
        System.out.println("✓ Animales de prueba creados: " + animales.length);
    }
    
    public void cerrar() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== INICIALIZACIÓN DE BASE DE DATOS ===\n");
        
        DatosIniciales datos = new DatosIniciales();
        
        // Crear roles
        datos.crearRolesIniciales();
        
        // Crear usuario administrador
        datos.crearUsuarioAdmin();
        
        // Preguntar si desea crear datos de prueba
        System.out.print("\n¿Desea crear datos de prueba? (S/N): ");
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String respuesta = scanner.nextLine();
        
        if (respuesta.equalsIgnoreCase("S")) {
            datos.crearDatosPrueba();
        }
        
        datos.cerrar();
        scanner.close();
        
        System.out.println("\n=== INICIALIZACIÓN COMPLETADA ===");
    }
}