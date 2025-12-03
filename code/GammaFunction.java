/***************************************************************************
 * Programa 5a: Simpson Integration of t Distribution
 * Clase:    GammaFunction
 * Autor:   Jared Fernandez Gonzalez
 * Fecha:     29-11-2025
 *
 * Descripción:
 *   Proporciona el cálculo de la función Gamma para:
 *     - Valores enteros positivos: Gamma(n) = (n-1)!
 *     - Valores semi-enteros: n + 0.5, usando la recurrencia basada en
 *       Gamma(0.5) = sqrt(pi) y:
 *          Gamma(n + 0.5) = (n - 0.5)(n - 1.5)...(0.5) * sqrt(pi)
 *
 * 
 *   - La variable gammaValue almacena el último resultado calculado
 *     para trazabilidad y depuración.
 ***************************************************************************/

/**
 * Clase GammaFunction.
 * <p>
 * Esta clase implementa la función Gamma únicamente para valores:
 * <ul>
 *     <li>Enteros positivos</li>
 *     <li>Semi-enteros (n + 0.5)</li>
 * </ul>
 * Estos son los únicos casos necesarios para Program 5
 * (integración de la distribución t).
 */
public class GammaFunction
{

    /*------------------------------------------------------------------*/
    /*  Attributes                                                      */
    /*------------------------------------------------------------------*/

    /** Último valor de Gamma calculado, almacenado para trazabilidad. */
    private double gammaValue;


    /*------------------------------------------------------------------*/
    /*  Public Methods                                                  */
    /*------------------------------------------------------------------*/

    /**
     * Calcula Gamma(n) para un entero positivo n.
     *
     * <p>
     * Definición:
     *     Gamma(n) = (n - 1)!
     *
     * <p>
     * Ejemplos:
     *     Gamma(1) = 1  
     *     Gamma(2) = 1  
     *     Gamma(3) = 2  
     *     Gamma(4) = 6
     *
     * @param intValue entero n (> 0)
     * @return Gamma(n)
     * @throws IllegalArgumentException si n &le; 0 (Gamma no está definida)
     */
    public double computeIntGamma(int intValue) 
    {
        double dblResult = 1.0;

        if (intValue <= 0) 
        {
            throw new IllegalArgumentException(
                    "Gamma no está definida para enteros <= 0");
        }

        // Multiplica desde 1 hasta (n-1)
        for (int intI = 1; intI < intValue; intI++) 
        {
            dblResult = dblResult * (double) intI;
        }

        gammaValue = dblResult;
        return gammaValue;
    }

    /**
     * Calcula Gamma(x) para valores double.
     *
     * <p>La implementación cubre únicamente:</p>
     * <ul>
     *     <li>Enteros positivos</li>
     *     <li>Semi-enteros (n + 0.5)</li>
     * </ul>
     *
     * Esto es suficiente para calcular:
     *   Gamma((ν+1)/2)  
     *   Gamma(ν/2)
     * requeridos en la distribución t.
     *
     * @param doubleValue valor double x para el cual se desea Gamma(x)
     * @return Gamma(doubleValue)
     *
     * @throws IllegalArgumentException 
     *         si el valor no es entero ni semi-entero
     */
    public double computeDblGamma(double doubleValue) 
    {
        double dblResult;
        double dblFractionalPart;
        int    intPart;

        // Parte entera aproximada de doubleValue
        intPart = (int) Math.round(doubleValue);

        // Parte fraccionaria = x - entero aproximado
        dblFractionalPart = doubleValue - (double) intPart;

        /* ==========================================================
           CASO 1: Entero (fracción ≈ 0)
           ========================================================== */
        if (Math.abs(dblFractionalPart) < 1.0e-10) 
        {
            dblResult = computeIntGamma(intPart);
            gammaValue = dblResult;
            return gammaValue;
        }

        /* ==========================================================
           CASO 2: Semi-entero: x = n + 0.5
           ========================================================== */
        double dblN = doubleValue - 0.5;    // x - 0.5
        int intN = (int) Math.round(dblN);

        // Verificar si dblN es entero y >= 0
        if (Math.abs(dblN - (double) intN) < 1.0e-10 && intN >= 0) 
        {
            // Gamma(0.5) = sqrt(pi)
            dblResult = Math.sqrt(Math.PI);

            // Recurrencia: Gamma(n + 0.5) = (n - 0.5)(n - 1.5)...(0.5)*sqrt(pi)
            for (int intI = 1; intI <= intN; intI++) 
            {
                dblResult = dblResult * (intI - 0.5);
            }

            gammaValue = dblResult;
            return gammaValue;
        }

        /* ==========================================================
           CASO 3: No soportado en Program 5
           ========================================================== */
        throw new IllegalArgumentException(
                "GammaFunction solo implementada para enteros y semi-enteros.");
    }

    /**
     * Regresa el último valor de Gamma calculado.
     *
     * @return gammaValue
     */
    public double getGammaValue() 
    {
        return gammaValue;
    }
}

