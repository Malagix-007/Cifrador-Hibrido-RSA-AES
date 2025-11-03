package cifrador; 
 
/** 
 * Clase principal del Cifrador Hibrido RSA-AES 
 * Proyecto de Criptografia - Grupo 910B 
 * Integrantes: Angel Malaga, Luis Aguilar, Roberto Toto 
 */ 
public class Main { 
    public static void main(String[] args) { 
        System.out.println("=== CIFRADOR HIBRIDO RSA-AES ==="); 
        System.out.println("Iniciando pruebas del sistema...\n"); 
 
        probarCifradorHibrido(); 
    } 
 
    private static void probarCifradorHibrido() { 
        try { 
            CifradorHibrido cifrador = new CifradorHibrido(); 
 
            cifrador.getRsaManager().generarParClaves(); 
 
            String mensajeOriginal = "Este es un mensaje secreto para el proyecto de criptografia!"; 
            System.out.println("Mensaje original: " + mensajeOriginal); 
 
            ResultadoCifrado resultado = cifrador.cifrarDatos( 
                mensajeOriginal, 
                cifrador.getRsaManager().getClavePublica() 
            ); 
 
            if (resultado != null) { 
                System.out.println("\nResultado del cifrado:"); 
                System.out.println(resultado); 
 
                String mensajeDescifrado = cifrador.descifrarDatos(resultado); 
 
                if (mensajeDescifrado != null) { 
                    System.out.println("\nMensaje descifrado: " + mensajeDescifrado); 
 
                    if (mensajeOriginal.equals(mensajeDescifrado)) { 
                        System.out.println("\nPRUEBA EXITOSA! El cifrado/descifrado funciona correctamente."); 
                    } else { 
                        System.out.println("\nERROR: El mensaje descifrado no coincide con el original."); 
                    } 
                } 
            } 
 
        } catch (Exception e) { 
            System.err.println("Error en la prueba: " + e.getMessage()); 
            e.printStackTrace(); 
        } 
    } 
} 
