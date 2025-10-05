/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package gt.edu.umg.empresa;

import gt.edu.umg.empresa.ENTITY.Cliente;
import gt.edu.umg.empresa.exceptions.IllegalOrphanException;
import gt.edu.umg.empresa.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author caste
 */
public class EMPRESA {


 public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gt.edu.umg.CRUD_jar_1.0");
        EntityManager em = emf.createEntityManager();
        ClienteJpaController clienteController = new ClienteJpaController(emf);

        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n===== MENU CLIENTE =====");
            System.out.println("1. Crear cliente");
            System.out.println("2. Mostrar todos los clientes");
            System.out.println("3. Actualizar cliente");
            System.out.println("4. Eliminar cliente");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine(); // Limpiar buffer

            switch (opcion) {
                case 1:
                    Cliente nuevoCliente = new Cliente();

                    System.out.print("Ingrese nombre: ");
                    nuevoCliente.setNombre(sc.nextLine());

                    System.out.print("Ingrese apellido: ");
                    nuevoCliente.setApellido(sc.nextLine());

                    System.out.print("Ingrese dirección: ");
                    nuevoCliente.setDireccion(sc.nextLine());

                    System.out.print("Ingrese teléfono: ");
                    nuevoCliente.setTelefono(sc.nextLine());

                    System.out.print("Ingrese email: ");
                    nuevoCliente.setEmail(sc.nextLine());

                    try {
                        clienteController.create(nuevoCliente);
                        System.out.println("Cliente creado correctamente.");
                    } catch (Exception e) {
                        System.out.println("Error al crear cliente: " + e.getMessage());
                    }
                    break;

                case 2:
                    List<Cliente> clientes = clienteController.findClienteEntities();
                    System.out.println("\n--- Lista de Clientes ---");
                    for (Cliente c : clientes) {
                        System.out.println("ID: " + c.getId());
                        System.out.println("Nombre: " + c.getNombre());
                        System.out.println("Apellido: " + c.getApellido());
                        System.out.println("Dirección: " + c.getDireccion());
                        System.out.println("Teléfono: " + c.getTelefono());
                        System.out.println("Email: " + c.getEmail());
                        System.out.println("------------------------");
                    }
                    break;

                case 3:
                    System.out.print("Ingrese ID del cliente a actualizar: ");
                    int idActualizar = sc.nextInt();
                    sc.nextLine(); // Limpiar buffer

                    Cliente clienteExistente = clienteController.findCliente(idActualizar);
                    if (clienteExistente != null) {
                        System.out.print("Nuevo nombre (" + clienteExistente.getNombre() + "): ");
                        clienteExistente.setNombre(sc.nextLine());

                        System.out.print("Nuevo apellido (" + clienteExistente.getApellido() + "): ");
                        clienteExistente.setApellido(sc.nextLine());

                        System.out.print("Nueva dirección (" + clienteExistente.getDireccion() + "): ");
                        clienteExistente.setDireccion(sc.nextLine());

                        System.out.print("Nuevo teléfono (" + clienteExistente.getTelefono() + "): ");
                        clienteExistente.setTelefono(sc.nextLine());

                        System.out.print("Nuevo email (" + clienteExistente.getEmail() + "): ");
                        clienteExistente.setEmail(sc.nextLine());

                        try {
                            clienteController.edit(clienteExistente);
                            System.out.println("Cliente actualizado correctamente.");
                        } catch (Exception e) {
                            System.out.println("Error al actualizar cliente: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Cliente no encontrado.");
                    }
                    break;

                case 4:
                    System.out.print("Ingrese ID del cliente a eliminar: ");
                    int idEliminar = sc.nextInt();
                    sc.nextLine(); // Limpiar buffer

                    try {
                        clienteController.destroy(idEliminar);
                        System.out.println("Cliente eliminado correctamente.");
                    } catch (IllegalOrphanException | NonexistentEntityException e) {
                        System.out.println("Error al eliminar cliente: " + e.getMessage());
                    }
                    break;

                case 5:
                    System.out.println("Saliendo...");
                    break;

                default:
                    System.out.println("Opción inválida.");
                    break;
            }

        } while (opcion != 5);

        sc.close();
        em.close();
        emf.close();
    }
}
