package cifrador; 
 
import javax.crypto.Cipher; 
import java.security.*; 
import java.util.Base64; 
 
/** 
 * Gestor para operaciones con algoritmo RSA 
 * Responsable: Angel de Jesus Fiscal Malaga 
 */ 
public class RSAManager { 
    private KeyPair clavesRSA; 
    private static final String ALGORITMO = "RSA"; 
    private static final int TAMANIO_CLAVE = 2048; 
 
    public RSAManager() { 
        // Constructor - inicializar gestor RSA 
    } 
 
    public KeyPair generarParClaves() { 
        try { 
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITMO); 
            keyGen.initialize(TAMANIO_CLAVE); 
            this.clavesRSA = keyGen.generateKeyPair(); 
            System.out.println("Par de claves RSA generado exitosamente"); 
            return this.clavesRSA; 
        } catch (NoSuchAlgorithmException e) { 
            System.err.println("Error al generar claves RSA: " + e.getMessage()); 
            return null; 
        } 
    } 
 
    public String cifrar(String datos, PublicKey clavePublica) { 
        try { 
            Cipher cipher = Cipher.getInstance(ALGORITMO); 
            cipher.init(Cipher.ENCRYPT_MODE, clavePublica); 
            byte[] datosCifrados = cipher.doFinal(datos.getBytes()); 
            return Base64.getEncoder().encodeToString(datosCifrados); 
        } catch (Exception e) { 
            System.err.println("Error al cifrar con RSA: " + e.getMessage()); 
            return null; 
        } 
    } 
 
    public String descifrar(String datosCifrados, PrivateKey clavePrivada) { 
        try { 
            Cipher cipher = Cipher.getInstance(ALGORITMO); 
            cipher.init(Cipher.DECRYPT_MODE, clavePrivada); 
            byte[] datosBytes = Base64.getDecoder().decode(datosCifrados); 
            byte[] datosOriginales = cipher.doFinal(datosBytes); 
            return new String(datosOriginales); 
        } catch (Exception e) { 
            System.err.println("Error al descifrar con RSA: " + e.getMessage()); 
            return null; 
        } 
    } 
 
    public PublicKey getClavePublica() { 
        return (clavesRSA != null) ? clavesRSA.getPublic() : null; 
    } 
 
    public PrivateKey getClavePrivada() { 
        return (clavesRSA != null) ? clavesRSA.getPrivate() : null; 
    } 
} 
