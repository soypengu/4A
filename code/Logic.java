/***************************************************************************
 * Programa 5a: Simpson Integration of t Distribution
 * Clase:    Logic
 * Autor:   Jared Fernandez Gonzalez
 * Fecha:     28-11-2025
 *
 * Descripción:
 *   Coordina el flujo general:
 *     - Lee datos de entrada desde test.txt
 *     - Para cada (x, dof), aplica la integración de Simpson sobre la
 *       distribución t hasta que el error esté por debajo de E
 *     - Escribe los resultados en result.txt
 *
 *
 *   - Variables miembro según diagrama de clases:
 *       intNumSeg : número de segmentos (par)
 *       dblE      : error aceptable
 *       intDOF    : grados de libertad
 *       dblX      : límite superior de integración
 *   - Usa SimpsonIntegration, GammaFunction y Output.
 ***************************************************************************/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Clase Logic.
 * <p>
 * Encargada de coordinar el flujo principal del programa:
 * <ul>
 *   <li>Lee cada línea de entrada desde <b>test.txt</b>.</li>
 *   <li>Interpreta cada línea como un par (x, dof).</li>
 *   <li>Utiliza la clase {@code SimpsonIntegration} para aproximar
 *       la integral de la distribución t.</li>
 *   <li>Refina la aproximación aumentando el número de segmentos
 *       hasta que el error sea menor o igual a {@code dblE}.</li>
 *   <li>Escribe los resultados finales en el archivo result.txt
 *       por medio de la clase {@code Output}.</li>
 * </ul>
 */
public class Logic
{

    /*------------------------------------------------------------------*/
    /*  Attributes                                                      */
    /*------------------------------------------------------------------*/

    /** Número de segmentos a usar en la integración (debe ser par). */
    private int intNumSeg;   

    /** Error máximo aceptable para la aproximación numérica. */
    private double dblE;     

    /** Grados de libertad de la distribución t. */
    private int intDOF;      

    /** Límite superior de integración (valor de x). */
    private double dblX;     


    /*------------------------------------------------------------------*/
    /*  Public Methods                                                  */
    /*------------------------------------------------------------------*/

    /**
     * Método principal de lógica del programa.
     * <p>
     * Realiza las siguientes tareas:
     * <ol>
     *   <li>Define los parámetros base (E y número de segmentos).</li>
     *   <li>Abre el archivo de entrada <b>test.txt</b>.</li>
     *   <li>Lee línea por línea, ignorando comentarios y líneas vacías.</li>
     *   <li>Parsea cada línea válida como (x, dof).</li>
     *   <li>Crea un objeto {@code SimpsonIntegration} y calcula la
     *       aproximación de la integral.</li>
     *   <li>Refina el número de segmentos hasta que el error entre
     *       dos aproximaciones consecutivas sea menor o igual a {@code dblE}.</li>
     *   <li>Acumula los resultados en un buffer y los escribe a
     *       {@code result.txt} mediante la clase {@code Output}.</li>
     * </ol>
     */
    public void logic1a() 
    {
        // Nombre del archivo de entrada con los parámetros (x, dof)
        final String strInFile  = "test.txt";
        // Nombre del archivo de salida donde se escribirá el resultado final
        final String strOutFile = "result.txt";

        // Buffer para ir acumulando todas las líneas de salida
        StringBuilder objOutBuffer;
        // Lector para recorrer el archivo de entrada línea por línea
        BufferedReader objReader;
        // Variable para almacenar cada línea leída de test.txt
        String strLine;

        /* Parámetros base de Program 5 */
        dblE      = 0.00001;      /* error aceptable */
        intNumSeg = 10;           /* número inicial de segmentos (par) */

        // Inicializamos el buffer de salida
        objOutBuffer = new StringBuilder();

        try 
        {
            // Abrir archivo de entrada
            objReader = new BufferedReader(new FileReader(strInFile));

            // Leer cada línea del archivo test.txt
            while ((strLine = objReader.readLine()) != null) 
            {
                strLine = strLine.trim();

                /* Ignorar líneas vacías o comentarios (que empiezan con '#') */
                if (strLine.length() == 0 || strLine.startsWith("#")) 
                {
                    continue;
                }

                // Separar la línea en tokens usando espacios como separadores
                String[] arrTokens = strLine.split("\\s+");
                if (arrTokens.length < 2) 
                {
                    /* Línea mal formada; en PSP lo ideal es registrar el defecto */
                    continue;
                }

                /*---- Parseo de entrada -------------------------------------*/
                // Primer token es x
                dblX  = Double.parseDouble(arrTokens[0]); /* x */
                // Segundo token es dof
                intDOF = Integer.parseInt(arrTokens[1]);  /* dof */

                /*---- Cálculo numérico -------------------------------------*/
                // Objeto que realiza la integración numérica de Simpson
                SimpsonIntegration objSimpson;
                // Valor anterior de la aproximación
                double dblPrevValue;
                // Valor actual de la aproximación
                double dblCurrValue;
                // Número actual de segmentos en la iteración
                int    intCurrSeg;

                // Inicializamos SimpsonIntegration con (dof, x)
                objSimpson = new SimpsonIntegration(intDOF, dblX);

                /* primer cálculo */
                intCurrSeg   = intNumSeg;
                dblPrevValue = objSimpson.computeFinalValue(intCurrSeg);

                /* refinar doblando segmentos hasta que el error <= E */
                while (true) 
                {
                    // Doblamos el número de segmentos
                    intCurrSeg = intCurrSeg * 2;
                    // Calculamos de nuevo con mayor resolución
                    dblCurrValue = objSimpson.computeFinalValue(intCurrSeg);

                    // Condición de paro: diferencia entre aproximaciones
                    if (Math.abs(dblCurrValue - dblPrevValue) <= dblE) 
                    {
                        break;
                    }

                    // Actualizamos el valor previo para la siguiente iteración
                    dblPrevValue = dblCurrValue;
                }

                // Al finalizar el refinamiento, el valor actual es la mejor aproximación de p
                double dblP = dblCurrValue;

                /*---- Acumular salida --------------------------------------*/
                // Formato de salida para cada línea: x, dof y p calculado
                String strOutputLine =
                    String.format("x = %.4f   dof = %d   p = %.5f%n",
                                  dblX, intDOF, dblP);

                // Agregar línea formateada al buffer de salida
                objOutBuffer.append(strOutputLine);
                System.out.print(strOutputLine);

            }

            // Cerrar archivo de entrada
            objReader.close();

            /* Escribir todo al archivo de salida */
            Output objOutput = new Output();
            objOutput.writeData(strOutFile, objOutBuffer.toString());

        } 
        catch (IOException objEx) 
        {
            /* En PSP registrar defecto; aquí solo mostramos mensaje */
            System.err.println("Error al leer archivo de entrada: "
                               + objEx.getMessage());
        } 
        catch (NumberFormatException objEx) 
        {
            System.err.println("Error en formato numérico de test.txt: "
                               + objEx.getMessage());
        }
    }
}
