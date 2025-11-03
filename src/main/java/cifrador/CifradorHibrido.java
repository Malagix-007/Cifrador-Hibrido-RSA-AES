package cifrador; 
 
import java.security.PublicKey; 
import java.util.Base64; 
 
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
 
            String datosCifrados = aesManager.cifrarTexto(datos); 
 
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
 
            String datosOriginales = aesManager.descifrarTexto(resultado.getDatosCifrados()); 
 
            System.out.println("Descifrado hibrido completado"); 
            return datosOriginales; 
 
        } catch (Exception e) { 
            System.err.println("Error en descifrado hibrido: " + e.getMessage()); 
            return null; 
        } 
    } 
 
    public boolean cifrarArchivo(String rutaArchivo, String rutaSalida, PublicKey clavePublica) { 
        try { 
            System.out.println("Cifrando archivo: " + rutaArchivo); 
 
            byte[] datosArchivo = FileManager.leerArchivo(rutaArchivo); 
            if (datosArchivo == null) return false; 
 
            aesManager.generarClave(); 
            byte[] archivoCifrado = aesManager.cifrarArchivo(datosArchivo); 
 
            String claveAES = aesManager.claveAESToString(); 
            String claveAESCifrada = rsaManager.cifrar(claveAES, clavePublica); 
 
            String contenidoFinal = claveAESCifrada + "\n" + Base64.getEncoder().encodeToString(archivoCifrado); 
            boolean exito = FileManager.escribirArchivo(rutaSalida, contenidoFinal.getBytes()); 
 
            if (exito) { 
                System.out.println("Archivo cifrado guardado: " + rutaSalida); 
            } 
 
            return exito; 
 
        } catch (Exception e) { 
            System.err.println("Error cifrando archivo: " + e.getMessage()); 
            return false; 
        } 
    } 
 
    public boolean descifrarArchivo(String rutaArchivoCifrado, String rutaSalida) { 
        try { 
            System.out.println("Descifrando archivo: " + rutaArchivoCifrado); 
 
            byte[] datos = FileManager.leerArchivo(rutaArchivoCifrado); 
            if (datos == null) return false; 
 
            String contenido = new String(datos); 
            String[] partes = contenido.split("\n", 2); 
 
            if (partes.length != 2) { 
                System.err.println("Formato de archivo cifrado invalido"); 
                return false; 
            } 
 
            String claveAESCifrada = partes[0]; 
            byte[] archivoCifrado = Base64.getDecoder().decode(partes[1]); 
 
            String claveAES = rsaManager.descifrar(claveAESCifrada, rsaManager.getClavePrivada()); 
            aesManager.claveAESFromString(claveAES); 
 
            byte[] archivoOriginal = aesManager.descifrarArchivo(archivoCifrado); 
 
            boolean exito = FileManager.escribirArchivo(rutaSalida, archivoOriginal); 
 
            if (exito) { 
                System.out.println("Archivo descifrado guardado: " + rutaSalida); 
            } 
 
            return exito; 
 
        } catch (Exception e) { 
            System.err.println("Error descifrando archivo: " + e.getMessage()); 
            return false; 
        } 
    } 
 
    public RSAManager getRsaManager() { return rsaManager; } 
    public AESManager getAesManager() { return aesManager; } 
} 
