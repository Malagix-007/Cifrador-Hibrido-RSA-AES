package cifrador; 
 
import java.security.*; 
import javax.crypto.Cipher; 
 
/** 
 * Gestor para operaciones con algoritmo RSA 
 * Responsable: µngel de Jes£s Fiscal M laga 
 */ 
public class RSAManager { 
    private KeyPair clavesRSA; 
 
    public RSAManager() { 
        // Constructor - inicializar gestor RSA 
    } 
 
    /** 
     * Genera un nuevo par de claves RSA 
     * @return KeyPair con claves p£blica y privada 
     */ 
    public KeyPair generarParClaves() { 
        try { 
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA"); 
            keyGen.initialize(2048); // Clave de 2048 bits 
            this.clavesRSA = keyGen.generateKeyPair(); 
            return this.clavesRSA; 
        } catch (NoSuchAlgorithmException e) { 
            System.err.println("Error al generar claves RSA: " + e.getMessage()); 
            return null; 
        } 
    } 
} 
