/***************************************************************************
 * Programa 5a :  Simpson Integration of t Distribution
 * Clase:    App
 * Autor:   Jared Fernandez Gonzalez
 * Fecha:    28-11-2025
 *
 * Descripción:Punto de entrada del programa. 
 * Crea el objeto Logic y dispara el procesamiento principal.
 * 
 *
 * PSP Notes:
 *   - Sigue estándar PSP: una clase por archivo, cabecera, nombres
 *     con prefijos de tipo (int, dbl, str, etc.).
 ***************************************************************************/

public class App
{

    /**
     * Método principal (main) del programa.
     *
     * Este método actúa como punto de entrada de la aplicación. Su única
     * responsabilidad es:
     *
     * <ol>
     *   <li>Declarar una instancia de la clase Logic.</li>
     *   <li>Inicializar el objeto Logic.</li>
     *   <li>Invocar el método logic1a(), que contiene el flujo principal
     *       del programa.</li>
     * </ol>
     *
     * Los argumentos de línea de comandos no se utilizan en esta versión.
     *
     * @param args argumentos opcionales pasados desde la terminal.
     */
    public static void main(String[] args)
    {
        /** Objeto que controla la lógica principal del programa. */
        Logic objLogic;

        // Crear instancia de la lógica
        objLogic = new Logic();

        // Ejecutar el flujo principal definido en Logic
        objLogic.logic1a();
    }
}
