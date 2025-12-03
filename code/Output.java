/***************************************************************************
 * Program:  Program 5 - Simpson Integration of t Distribution
 * Clase:    Output
 * Autor:   Jared Fernandez Gonzalez
 * Fecha:     29-11-2025
 *
 * Descripción:
 *   Encapsula la escritura de texto a un archivo de salida.
 *
 * 
 *   - Clase simple cuyo propósito es centralizar la operación de escritura.
 *   
 ***************************************************************************/

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Clase Output.
 * <p>
 * Proporciona un método para escribir texto en un archivo externo.
 * Encapsula las operaciones de manejo de flujos de salida, siguiendo
 * una estructura clara y controlada.
 */
public class Output
{

    /**
     * Escribe el texto indicado dentro del archivo especificado.
     * <p>
     * Si el archivo no existe, se crea automáticamente.
     * Si ya existe, su contenido será sobrescrito.
     *
     * @param outFile nombre del archivo de salida (por ejemplo "result.txt").
     * @param outText texto completo que se desea escribir.
     */
    public void writeData(String outFile, String outText) 
    {
        /** Escritor de texto en archivo; se inicializa como null para manejo seguro. */
        PrintWriter objWriter = null;

        try 
        {
            // Abrir archivo en modo escritura
            objWriter = new PrintWriter(new FileWriter(outFile));

            // Escribir texto sin salto automático al final
            objWriter.print(outText);
        } 
        catch (IOException objEx) 
        {
            // Manejo básico de errores (en PSP normalmente se registra el defecto)
            System.err.println("Error al escribir archivo de salida: "
                               + objEx.getMessage());
        } 
        finally 
        {
            // Cerrar el escritor si fue creado correctamente
            if (objWriter != null) 
            {
                objWriter.close();
            }
        }
    }
}

