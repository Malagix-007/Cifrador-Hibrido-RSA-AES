package cifrador; 
 
import java.io.*; 
import java.nio.file.*; 
 
/** 
 * Gestiona lectura y escritura de archivos 
 */ 
public class FileManager { 
 
    public static byte[] leerArchivo(String ruta) { 
        try { 
            return Files.readAllBytes(Paths.get(ruta)); 
        } catch (IOException e) { 
            System.err.println("Error leyendo archivo: " + e.getMessage()); 
            return null; 
        } 
    } 
 
    public static boolean escribirArchivo(String ruta, byte[] datos) { 
        try { 
            Files.write(Paths.get(ruta), datos); 
            return true; 
        } catch (IOException e) { 
            System.err.println("Error escribiendo archivo: " + e.getMessage()); 
            return false; 
        } 
    } 
 
    public static boolean archivoExiste(String ruta) { 
        return Files.exists(Paths.get(ruta)); 
    } 
} 
