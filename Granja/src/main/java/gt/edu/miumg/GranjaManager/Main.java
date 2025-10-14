/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Punto de entrada principal de la aplicación
 */
package gt.edu.miumg.GranjaManager;

import gt.edu.miumg.db.Usuario;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

/**
 * Clase principal con sistema de login
 */
public class Main {
    
    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        Scanner scanner = new Scanner(System.in);
        
        try {
            // Inicializar EntityManagerFactory
            emf = Persistence.createEntityManagerFactory("gt.edu.umg_Granja_jar_1.0-SNAPSHOTPU");
            
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║   SISTEMA DE GESTIÓN DE GRANJA     ║");
            System.out.println("║          Version 1.0               ║");
            System.out.println("╚════════════════════════════════════╝");
            
            // Crear instancias
            LoginManager loginManager = new LoginManager(emf, scanner);
            GranjaManager granjaManager = new GranjaManager(emf, scanner);
            
            // Proceso de login
            Usuario usuarioLogueado = loginManager.login();
            
            if (usuarioLogueado != null) {
                // Mostrar menú según el rol
                loginManager.mostrarMenuPorRol(granjaManager);
            } else {
                System.out.println("\nNo se pudo iniciar sesión. Adiós.");
            }
            
        } catch (Exception e) {
            System.err.println("Error crítico: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cerrar recursos
            if (scanner != null) {
                scanner.close();
            }
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        }
    }
}