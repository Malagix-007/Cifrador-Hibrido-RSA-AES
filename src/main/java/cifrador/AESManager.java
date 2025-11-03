package cifrador; 
 
import javax.crypto.*; 
import javax.crypto.spec.GCMParameterSpec; 
import javax.crypto.spec.SecretKeySpec; 
import java.security.SecureRandom; 
import java.util.Base64; 
 
/** 
 * Gestor para operaciones con algoritmo AES 
 * Responsable: Angel de Jesus Fiscal Malaga 
 */ 
public class AESManager { 
    private SecretKey claveAES; 
    private static final String ALGORITMO = "AES"; 
    private static final String TRANSFORMACION = "AES/GCM/NoPadding"; 
    private static final int TAMANIO_TAG = 128; 
    private static final int TAMANIO_IV = 12; 
 
    public AESManager() { 
        // Constructor - inicializar gestor AES 
    } 
 
    public SecretKey generarClave() { 
        try { 
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITMO); 
            keyGen.init(256); 
            this.claveAES = keyGen.generateKey(); 
            System.out.println("Clave AES generada exitosamente"); 
            return this.claveAES; 
        } catch (Exception e) { 
            System.err.println("Error al generar clave AES: " + e.getMessage()); 
            return null; 
        } 
    } 
 
    // Para texto 
    public String cifrarTexto(String texto) { 
        try { 
            byte[] iv = new byte[TAMANIO_IV]; 
            SecureRandom random = new SecureRandom(); 
            random.nextBytes(iv); 
 
            Cipher cipher = Cipher.getInstance(TRANSFORMACION); 
            GCMParameterSpec spec = new GCMParameterSpec(TAMANIO_TAG, iv); 
            cipher.init(Cipher.ENCRYPT_MODE, claveAES, spec); 
 
            byte[] datosCifrados = cipher.doFinal(texto.getBytes()); 
 
            byte[] resultado = new byte[iv.length + datosCifrados.length]; 
            System.arraycopy(iv, 0, resultado, 0, iv.length); 
            System.arraycopy(datosCifrados, 0, resultado, iv.length, datosCifrados.length); 
 
            return Base64.getEncoder().encodeToString(resultado); 
        } catch (Exception e) { 
            System.err.println("Error al cifrar texto con AES: " + e.getMessage()); 
            return null; 
        } 
    } 
 
    // Para archivos (bytes) 
    public byte[] cifrarArchivo(byte[] datosArchivo) { 
        try { 
            byte[] iv = new byte[TAMANIO_IV]; 
            SecureRandom random = new SecureRandom(); 
            random.nextBytes(iv); 
 
            Cipher cipher = Cipher.getInstance(TRANSFORMACION); 
            GCMParameterSpec spec = new GCMParameterSpec(TAMANIO_TAG, iv); 
            cipher.init(Cipher.ENCRYPT_MODE, claveAES, spec); 
 
            byte[] datosCifrados = cipher.doFinal(datosArchivo); 
 
            byte[] resultado = new byte[iv.length + datosCifrados.length]; 
            System.arraycopy(iv, 0, resultado, 0, iv.length); 
            System.arraycopy(datosCifrados, 0, resultado, iv.length, datosCifrados.length); 
 
            return resultado; 
        } catch (Exception e) { 
            System.err.println("Error al cifrar archivo con AES: " + e.getMessage()); 
            return null; 
        } 
    } 
 
    // Para texto 
    public String descifrarTexto(String textoCifrado) { 
        try { 
            byte[] datosCombinados = Base64.getDecoder().decode(textoCifrado); 
 
            byte[] iv = new byte[TAMANIO_IV]; 
            System.arraycopy(datosCombinados, 0, iv, 0, iv.length); 
 
            byte[] datosCifradosBytes = new byte[datosCombinados.length - TAMANIO_IV]; 
            System.arraycopy(datosCombinados, TAMANIO_IV, datosCifradosBytes, 0, datosCifradosBytes.length); 
 
            Cipher cipher = Cipher.getInstance(TRANSFORMACION); 
            GCMParameterSpec spec = new GCMParameterSpec(TAMANIO_TAG, iv); 
            cipher.init(Cipher.DECRYPT_MODE, claveAES, spec); 
 
            byte[] datosOriginales = cipher.doFinal(datosCifradosBytes); 
            return new String(datosOriginales); 
        } catch (Exception e) { 
            System.err.println("Error al descifrar texto con AES: " + e.getMessage()); 
            return null; 
        } 
    } 
 
    // Para archivos (bytes) 
    public byte[] descifrarArchivo(byte[] archivoCifrado) { 
        try { 
            byte[] iv = new byte[TAMANIO_IV]; 
            System.arraycopy(archivoCifrado, 0, iv, 0, iv.length); 
 
            byte[] datosCifradosBytes = new byte[archivoCifrado.length - TAMANIO_IV]; 
            System.arraycopy(archivoCifrado, TAMANIO_IV, datosCifradosBytes, 0, datosCifradosBytes.length); 
 
            Cipher cipher = Cipher.getInstance(TRANSFORMACION); 
            GCMParameterSpec spec = new GCMParameterSpec(TAMANIO_TAG, iv); 
            cipher.init(Cipher.DECRYPT_MODE, claveAES, spec); 
 
            return cipher.doFinal(datosCifradosBytes); 
        } catch (Exception e) { 
            System.err.println("Error al descifrar archivo con AES: " + e.getMessage()); 
            return null; 
        } 
    } 
 
    public String claveAESToString() { 
        return Base64.getEncoder().encodeToString(claveAES.getEncoded()); 
    } 
 
    public void claveAESFromString(String claveBase64) { 
        byte[] decodedKey = Base64.getDecoder().decode(claveBase64); 
        this.claveAES = new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITMO); 
    } 
 
    public SecretKey getClaveAES() { 
        return claveAES; 
    } 
} 
