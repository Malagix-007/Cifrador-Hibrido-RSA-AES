# Cifrador Híbrido RSA-AES

![Java](https://img.shields.io/badge/Java-21-orange)
![JavaFX](https://img.shields.io/badge/GUI-JavaFX-blue)
![Database](https://img.shields.io/badge/Database-MySQL-cF3735)
![Security](https://img.shields.io/badge/Security-AES%2BRSA-green)

Bienvenidos a mi repositorio mi del proyecto **Cifrador Híbrido**. Esta aplicación de escritorio permite cifrar y descifrar
cualquier tipo de archivo digital utilizando un esquema de criptografía híbrida (AES para los datos y RSA para proteger las llaves),
asegurando un alto nivel de confidencialidad e integridad, con registro de auditoría en base de datos.

---

## Estructura del Repositorio

Para mantener el orden, este proyecto está organizado en **ramas (branches)** específicas. Por favor, cambia de rama según lo que busques:

| Rama | Descripción | Contenido Principal |
| :--- | :--- | :--- |
| **[`release`](../../tree/release)** | **Código Fuente** | Archivos `.java`, scripts de compilación (`.bat`) y librerías ligeras. Ideal para desarrolladores. |
| **[`docs`](../../tree/docs)** | **Documentación** | Manuales de Usuario, Manual Técnico, Diagramas UML (Clases, Casos de Uso) y PDFs de investigación. |
| **[`source`](../../tree/source)** | **Ejecutables** | Versiones listas para usar (ZIP portable o JAR). Ideal para usuarios finales. |

---

## Características del Sistema

* **Cifrado Híbrido:** Combina la velocidad de **AES-256 (GCM)** para archivos pesados con la seguridad de **RSA-2048** para el intercambio de claves.
* **Interfaz Gráfica (GUI):** Desarrollada en JavaFX para una experiencia de usuario amigable.
* **Gestión de Usuarios:** Generación de pares de llaves (Pública/Privada) personalizada.
* **Auditoría:** Registro automático de todas las operaciones de cifrado en base de datos MySQL.
* **Portabilidad:** Capacidad de ejecutarse en entornos Windows de forma autocontenida (vía versión portable).

---

## Requisitos del Sistema (Para Desarrolladores)

Si deseas clonar la rama `release` y compilar el código tú mismo, necesitarás:

1.  **Java Development Kit (JDK):** Versión 21 o superior.
2.  **JavaFX SDK:** Versión 21 (debes configurar la ruta en los scripts `.bat` o en tu IDE).
3.  **MySQL Server:** Una instancia local o remota corriendo.
O en su defecto ir al link de drive anexado en Sources, descomprimir y ejecutar el archivo "cifradorhibrido"
### Configuración de la Base de Datos
Para que la aplicación funcione, ejecuta este script SQL en tu gestor de base de datos:

```sql
CREATE DATABASE cifrador;
USE cifrador;

CREATE TABLE operaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_archivo VARCHAR(255),
    fecha DATETIME,
    clave_aes_cifrada TEXT,
    usuario VARCHAR(100)
);
