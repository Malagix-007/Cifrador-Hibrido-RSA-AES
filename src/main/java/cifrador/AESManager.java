package cifrador; 
 
import javax.crypto.*; 
import javax.crypto.spec.SecretKeySpec; 
import java.security.SecureRandom; 
 
/** 
 * Gestor para operaciones con algoritmo AES 
 * Responsable: µngel de Jes£s Fiscal M laga 
 */ 
public class AESManager { 
    private SecretKey claveAES; 
 
    public AESManager() { 
        // Constructor - inicializar gestor AES 
    } 
 
    /** 
     * Genera una nueva clave AES de 256 bits 
     * @return SecretKey con la clave AES generada 
     */ 
    public SecretKey generarClave() { 
        try { 
            KeyGenerator keyGen = KeyGenerator.getInstance("AES"); 
            keyGen.init(256); // Clave de 256 bits 
            this.claveAES = keyGen.generateKey(); 
            return this.claveAES; 
        } catch (NoSuchAlgorithmException e) { 
            System.err.println("Error al generar clave AES: " + e.getMessage()); 
            return null; 
        } 
    } 
} 
