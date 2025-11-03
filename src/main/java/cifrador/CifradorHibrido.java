package cifrador; 
 
import java.security.PublicKey; 
 
/** 
 * Coordina el cifrado hibrido RSA + AES 
 * Responsable: Angel de Jesus Fiscal Malaga 
 */ 
public class CifradorHibrido { 
    private RSAManager rsaManager; 
    private AESManager aesManager; 
 
    public CifradorHibrido() { 
        this.rsaManager = new RSAManager(); 
        this.aesManager = new AESManager(); 
    } 
 
    public ResultadoCifrado cifrarDatos(String datos, PublicKey clavePublica) { 
        try { 
            System.out.println("Iniciando cifrado hibrido..."); 
 
            aesManager.generarClave(); 
            String claveAES = aesManager.claveAESToString(); 
 
            String datosCifrados = aesManager.cifrar(datos); 
 
            String claveAESCifrada = rsaManager.cifrar(claveAES, clavePublica); 
 
            System.out.println("Cifrado hibrido completado"); 
            return new ResultadoCifrado(datosCifrados, claveAESCifrada); 
 
        } catch (Exception e) { 
            System.err.println("Error en cifrado hibrido: " + e.getMessage()); 
            return null; 
        } 
    } 
 
    public String descifrarDatos(ResultadoCifrado resultado) { 
        try { 
            System.out.println("Iniciando descifrado hibrido..."); 
 
            String claveAES = rsaManager.descifrar(resultado.getClaveAESCifrada(), rsaManager.getClavePrivada()); 
 
            aesManager.claveAESFromString(claveAES); 
 
            String datosOriginales = aesManager.descifrar(resultado.getDatosCifrados()); 
 
            System.out.println("Descifrado hibrido completado"); 
            return datosOriginales; 
 
        } catch (Exception e) { 
            System.err.println("Error en descifrado hibrido: " + e.getMessage()); 
            return null; 
        } 
    } 
 
    public RSAManager getRsaManager() { return rsaManager; } 
    public AESManager getAesManager() { return aesManager; } 
} 
