package cifrador; 
 
/** 
 * Contenedor para el resultado del cifrado hibrido 
 */ 
public class ResultadoCifrado { 
    private String datosCifrados; 
    private String claveAESCifrada; 
 
    public ResultadoCifrado(String datosCifrados, String claveAESCifrada) { 
        this.datosCifrados = datosCifrados; 
        this.claveAESCifrada = claveAESCifrada; 
    } 
 
    public String getDatosCifrados() { return datosCifrados; } 
    public String getClaveAESCifrada() { return claveAESCifrada; } 
 
    public void setDatosCifrados(String datosCifrados) { this.datosCifrados = datosCifrados; } 
    public void setClaveAESCifrada(String claveAESCifrada) { this.claveAESCifrada = claveAESCifrada; } 
 
    public String toString() { 
        return "Datos cifrados: " + datosCifrados.substring(0, Math.min(50, datosCifrados.length())) + "...\n" + 
               "Clave AES cifrada: " + claveAESCifrada.substring(0, Math.min(50, claveAESCifrada.length())) + "..."; 
    } 
} 
