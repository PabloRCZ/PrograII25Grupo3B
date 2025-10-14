/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.GranjaManager;

import gt.edu.miumg.db.*;
import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Scanner;

/**
 * Clase especializada para gestionar compras
 */
public class CompraManager {
    
    private EntityManagerFactory emf;
    private Scanner scanner;
    
    private CompraJpaController compraCtrl;
    private DetalleCompraJpaController detalleCompraCtrl;
    private ProveedorJpaController proveedorCtrl;
    private UsuarioJpaController usuarioCtrl;
    private InventarioJpaController inventarioCtrl;
    private InventarioAnimalesJpaController animalesCtrl;
    private FacturaJpaController facturaCtrl;
    
    public CompraManager(EntityManagerFactory emf, Scanner scanner) {
        this.emf = emf;
        this.scanner = scanner;
        
        this.compraCtrl = new CompraJpaController(emf);
        this.detalleCompraCtrl = new DetalleCompraJpaController(emf);
        this.proveedorCtrl = new ProveedorJpaController(emf);
        this.usuarioCtrl = new UsuarioJpaController(emf);
        this.inventarioCtrl = new InventarioJpaController(emf);
        this.animalesCtrl = new InventarioAnimalesJpaController(emf);
        this.facturaCtrl = new FacturaJpaController(emf);
    }
    
    public void registrarCompra() {
        try {
            System.out.println("\n=== REGISTRAR NUEVA COMPRA ===");
            
            // Seleccionar proveedor
            listarProveedores();
            System.out.print("\nID del proveedor: ");
            int proveedorId = Integer.parseInt(scanner.nextLine());
            Proveedor proveedor = proveedorCtrl.findProveedor(proveedorId);
            
            if (proveedor == null) {
                System.out.println("Proveedor no encontrado!");
                return;
            }
            
            // Seleccionar usuario
            System.out.print("ID del usuario que realiza la compra: ");
            int usuarioId = Integer.parseInt(scanner.nextLine());
            Usuario usuario = usuarioCtrl.findUsuario(usuarioId);
            
            if (usuario == null) {
                System.out.println("Usuario no encontrado!");
                return;
            }
            
            // Crear compra
            Compra compra = new Compra();
            compra.setProveedorId(proveedor);
            compra.setUsuarioId(usuario);
            compra.setFecha(new Date());
            compra.setTotal(BigDecimal.ZERO);
            compra.setCreatedAt(new Date());
            
            compraCtrl.create(compra);
            
            // Agregar detalles
            BigDecimal totalCompra = BigDecimal.ZERO;
            boolean agregarMas = true;
            
            while (agregarMas) {
                System.out.println("\n¿Qué está comprando?");
                System.out.println("1. Producto para inventario");
                System.out.println("2. Animal");
                System.out.print("Opción: ");
                int tipo = Integer.parseInt(scanner.nextLine());
                
                DetalleCompra detalle = new DetalleCompra();
                detalle.setCompraId(compra);
                
                if (tipo == 1) {
                    detalle = procesarCompraProducto(detalle);
                } else if (tipo == 2) {
                    detalle = procesarCompraAnimal(detalle);
                } else {
                    System.out.println("Opción no válida!");
                    continue;
                }
                
                if (detalle != null) {
                    detalleCompraCtrl.create(detalle);
                    totalCompra = totalCompra.add(detalle.getSubtotal());
                    
                    System.out.printf("Subtotal: Q%.2f%n", detalle.getSubtotal());
                }
                
                System.out.print("\n¿Agregar más items? (S/N): ");
                agregarMas = scanner.nextLine().equalsIgnoreCase("S");
            }
            
            // Actualizar total de compra
            compra.setTotal(totalCompra);
            compraCtrl.edit(compra);
            
            // Preguntar si desea generar factura
            System.out.print("\n¿Desea registrar la factura? (S/N): ");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                registrarFactura(compra, totalCompra);
            }
            
            System.out.println("\n✓ Compra registrada exitosamente!");
            System.out.printf("Total: Q%.2f%n", totalCompra);
            
        } catch (Exception e) {
            System.err.println("Error al registrar compra: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private DetalleCompra procesarCompraProducto(DetalleCompra detalle) {
        try {
            System.out.println("\n--- COMPRA DE PRODUCTO ---");
            System.out.println("¿El producto ya existe en inventario?");
            System.out.println("1. Sí (agregar stock)");
            System.out.println("2. No (crear nuevo)");
            System.out.print("Opción: ");
            int opcion = Integer.parseInt(scanner.nextLine());
            
            Inventario producto = null;
            
            if (opcion == 1) {
                // Producto existente
                listarInventario();
                System.out.print("\nID del producto: ");
                int prodId = Integer.parseInt(scanner.nextLine());
                producto = inventarioCtrl.findInventario(prodId);
                
                if (producto == null) {
                    System.out.println("Producto no encontrado!");
                    return null;
                }
                
            } else {
                // Nuevo producto
                System.out.print("Nombre del producto: ");
                String nombre = scanner.nextLine();
                
                System.out.print("Descripción: ");
                String descripcion = scanner.nextLine();
                
                System.out.print("Unidad (kg, litros, sacos, etc.): ");
                String unidad = scanner.nextLine();
                
                System.out.print("Precio de venta sugerido: ");
                BigDecimal precioVenta = new BigDecimal(scanner.nextLine());
                
                System.out.print("Mínimo en stock: ");
                int minimoStock = Integer.parseInt(scanner.nextLine());
                
                producto = new Inventario();
                producto.setNombre(nombre);
                producto.setDescripcion(descripcion);
                producto.setCantidad(0);
                producto.setUnidad(unidad);
                producto.setPrecioCosto(BigDecimal.ZERO);
                producto.setPrecioVenta(precioVenta);
                producto.setMinimoStock(minimoStock);
                producto.setCreatedAt(new Date());
                
                inventarioCtrl.create(producto);
                System.out.println("✓ Nuevo producto creado en inventario");
            }
            
            // Datos de la compra
            System.out.print("Cantidad comprada: ");
            int cantidad = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Precio unitario de compra: ");
            BigDecimal precioUnitario = new BigDecimal(scanner.nextLine());
            
            BigDecimal subtotal = precioUnitario.multiply(new BigDecimal(cantidad));
            
            // Actualizar inventario
            producto.setCantidad(producto.getCantidad() + cantidad);
            
            // Actualizar precio de costo promedio
            BigDecimal costoTotalAnterior = producto.getPrecioCosto()
                    .multiply(new BigDecimal(producto.getCantidad() - cantidad));
            BigDecimal costoTotalNuevo = precioUnitario.multiply(new BigDecimal(cantidad));
            BigDecimal costoTotal = costoTotalAnterior.add(costoTotalNuevo);
            BigDecimal nuevoPrecioCosto = costoTotal.divide(
                    new BigDecimal(producto.getCantidad()), 2, BigDecimal.ROUND_HALF_UP);
            
            producto.setPrecioCosto(nuevoPrecioCosto);
            producto.setUpdatedAt(new Date());
            
            inventarioCtrl.edit(producto);
            
            // Crear detalle
            detalle.setProductoId(producto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setSubtotal(subtotal);
            
            return detalle;
            
        } catch (Exception e) {
            System.err.println("Error al procesar compra de producto: " + e.getMessage());
            return null;
        }
    }
    
    private DetalleCompra procesarCompraAnimal(DetalleCompra detalle) {
        try {
            System.out.println("\n--- COMPRA DE ANIMAL ---");
            
            System.out.print("Especie: ");
            String especie = scanner.nextLine();
            
            System.out.print("Raza: ");
            String raza = scanner.nextLine();
            
            System.out.print("Peso (kg): ");
            BigDecimal peso = new BigDecimal(scanner.nextLine());
            
            System.out.print("Edad (meses): ");
            int edad = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Estado (Sano/En cuarentena/etc.): ");
            String estado = scanner.nextLine();
            
            System.out.print("Identificador único: ");
            String identificador = scanner.nextLine();
            
            System.out.print("Observaciones: ");
            String observaciones = scanner.nextLine();
            
            // Crear animal en inventario
            InventarioAnimales animal = new InventarioAnimales();
            animal.setEspecie(especie);
            animal.setRaza(raza);
            animal.setPeso(peso);
            animal.setEdad(edad);
            animal.setEstado(estado);
            animal.setIdentificadorUnico(identificador);
            animal.setObservaciones(observaciones);
            animal.setFechaIngreso(new Date());
            animal.setCreatedAt(new Date());
            
            animalesCtrl.create(animal);
            System.out.println("✓ Animal registrado en inventario");
            
            // Datos de la compra
            System.out.print("Precio de compra: ");
            BigDecimal precio = new BigDecimal(scanner.nextLine());
            
            // Crear detalle
            detalle.setAnimalId(animal);
            detalle.setCantidad(1);
            detalle.setPrecioUnitario(precio);
            detalle.setSubtotal(precio);
            
            return detalle;
            
        } catch (Exception e) {
            System.err.println("Error al procesar compra de animal: " + e.getMessage());
            return null;
        }
    }
    
    private void registrarFactura(Compra compra, BigDecimal total) {
        try {
            System.out.print("Número de factura: ");
            String numero = scanner.nextLine();
            
            System.out.print("Estado (Pagada/Pendiente/Crédito): ");
            String estado = scanner.nextLine();
            
            Factura factura = new Factura();
            factura.setNumero(numero);
            factura.setFecha(new Date());
            factura.setTipo("Compra");
            factura.setTotal(total);
            factura.setEstado(estado);
            factura.setCreatedAt(new Date());
            
            facturaCtrl.create(factura);
            
            // Asociar factura con compra
            compra.setFacturaId(factura);
            compraCtrl.edit(compra);
            
            System.out.println("✓ Factura registrada");
            
        } catch (Exception e) {
            System.err.println("Error al registrar factura: " + e.getMessage());
        }
    }
    
    public void listarCompras() {
        System.out.println("\n=== LISTA DE COMPRAS ===");
        var compras = compraCtrl.findCompraEntities();
        
        for (Compra c : compras) {
            System.out.printf("ID: %d | Proveedor: %s | Total: Q%.2f | Fecha: %s%n",
                    c.getId(), c.getProveedorId().getNombre(), 
                    c.getTotal(), c.getFecha());
        }
    }
    
    public void verDetalleCompra() {
        try {
            System.out.print("ID de la compra: ");
            int id = Integer.parseInt(scanner.nextLine());
            
            Compra compra = compraCtrl.findCompra(id);
            if (compra == null) {
                System.out.println("Compra no encontrada!");
                return;
            }
            
            System.out.println("\n=== DETALLE DE COMPRA ===");
            System.out.println("ID: " + compra.getId());
            System.out.println("Proveedor: " + compra.getProveedorId().getNombre());
            System.out.println("Usuario: " + compra.getUsuarioId().getNombre());
            System.out.println("Fecha: " + compra.getFecha());
            System.out.printf("Total: Q%.2f%n", compra.getTotal());
            
            if (compra.getFacturaId() != null) {
                System.out.println("Factura: " + compra.getFacturaId().getNumero());
            }
            
            System.out.println("\n--- Items ---");
            for (DetalleCompra detalle : compra.getDetalleCompraList()) {
                if (detalle.getProductoId() != null) {
                    System.out.printf("Producto: %s | Cantidad: %d | Precio: Q%.2f | Subtotal: Q%.2f%n",
                            detalle.getProductoId().getNombre(),
                            detalle.getCantidad(),
                            detalle.getPrecioUnitario(),
                            detalle.getSubtotal());
                } else if (detalle.getAnimalId() != null) {
                    System.out.printf("Animal: %s (%s) | Precio: Q%.2f%n",
                            detalle.getAnimalId().getEspecie(),
                            detalle.getAnimalId().getIdentificadorUnico(),
                            detalle.getSubtotal());
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void listarProveedores() {
        System.out.println("\n--- Proveedores ---");
        for (Proveedor p : proveedorCtrl.findProveedorEntities()) {
            System.out.printf("%d. %s - %s%n", 
                    p.getId(), p.getNombre(), p.getProducto());
        }
    }
    
    private void listarInventario() {
        System.out.println("\n--- Inventario ---");
        for (Inventario i : inventarioCtrl.findInventarioEntities()) {
            System.out.printf("%d. %s - %d %s%n",
                    i.getId(), i.getNombre(), i.getCantidad(), i.getUnidad());
        }
    }
}