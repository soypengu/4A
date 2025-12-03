/***************************************************************************
 * Programa 5a: Simpson Integration of t Distribution
 * Clase:    SimpsonIntegration
 * Autor:   Jared Fernandez Gonzalez
 * Fecha:     29-11-2025
 *
 * Descrpción:
 *   Encapsula el cálculo del valor de la integral de la distribución t
 *   usando la regla de Simpson, para un DOF y un límite x dados.
 *
 * 
 *   - Usa arreglos para almacenar:
 *       dblTotXi          : puntos Xi
 *       dblFirstBaseTerms : términos base (1 + Xi^2 / dof)
 *       dblFxi            : valores de f(Xi)
 *       dblFinalTerms     : términos finales con multiplicadores 1,4,2,...
 *   - dblFinalValue guarda el valor de la integral para un num_seg dado.
 ***************************************************************************/

/**
 * Clase SimpsonIntegration.
 * <p>
 * Implementa la integración numérica de la distribución t mediante
 * la regla de Simpson, para un número de segmentos dado, un valor x 
 * y unos grados de libertad (DOF).
 * <p>
 * La secuencia de cálculo típica es:
 * <ol>
 *   <li>Calcular el ancho de subintervalo w.</li>
 *   <li>Generar los puntos Xi.</li>
 *   <li>Calcular términos base (1 + Xi² / dof).</li>
 *   <li>Calcular exponente y coeficiente de la función t.</li>
 *   <li>Evaluar f(Xi) en cada punto.</li>
 *   <li>Aplicar los multiplicadores de Simpson (1,4,2,...,4,1).</li>
 *   <li>Obtener la suma y el valor final de la integral.</li>
 * </ol>
 */
/*
 * Contenido del archivo:
 *   - Atributos principales:
 *       + intNumSeg        : número de segmentos de Simpson
 *       + dblW             : ancho de subintervalo
 *       + dblE             : error aceptable
 *       + intDOF           : grados de libertad
 *       + dblX             : límite superior de integración
 *       + dblTotXi         : arreglo de puntos Xi
 *       + dblFirstBaseTerms: términos base (1 + Xi^2 / dof)
 *       + dblFxi           : valores de f(Xi)
 *       + dblFinalTerms    : términos finales con multiplicadores 1,4,2,...
 *       + dblFinalValue    : valor final de la integral
 *   - Dependencias:
 *       + GammaFunction gammaFunction
 *   - Constructor:
 *       + SimpsonIntegration(int intDOFIn, double dblXIn)
 *   - Métodos públicos principales:
 *       + void   computeW(int intNumSegIn, double dblXIn)
 *       + void   computeXi(int intNumSegIn)
 *       + void   computeFirstBaseTerms(int intNumSegIn, double[] dblXi, int intDOFIn)
 *       + void   computeExponent(int intDOFIn)
 *       + void   computeCoefficient(int intDOFIn)
 *       + void   computeFxi(int intNumSegIn)
 *       + void   computeFinalTerms(int intNumSegIn)
 *       + double computeFinalValue(int intNumSegIn)
 *       + double getFinalValue()
 */

public class SimpsonIntegration
{

    /*------------------------------------------------------------------*/
    /*  Attributes                                                      */
    /*------------------------------------------------------------------*/

    /** Número de segmentos usados en la integración de Simpson. */
    private int intNumSeg;

    /** Ancho de cada subintervalo: w = x / num_seg. */
    private double dblW;

    /** Error aceptable (se declara por consistencia con el diseño PSP). */
    private double dblE;              

    /** Grados de libertad de la distribución t. */
    private int intDOF;

    /** Límite superior de integración (desde 0 hasta dblX). */
    private double dblX;

    /** Arreglo de puntos Xi sobre el intervalo [0, x]. */
    private double[] dblTotXi;

    /** Términos base: 1 + (Xi² / dof). */
    private double[] dblFirstBaseTerms;

    /** Exponente para la función de densidad de t. */
    private double dblExponent;

    /** Coeficiente multiplicador de la función de densidad de t. */
    private double dblCoeff;

    /** Valores evaluados de la función f(Xi) para cada Xi. */
    private double[] dblFxi;

    /** Términos finales f(Xi) con los multiplicadores 1,4,2,...,4,1. */
    private double[] dblFinalTerms;

    /** Valor final de la integral para un número de segmentos dado. */
    private double dblFinalValue;

    /** Objeto auxiliar para cálculo de funciones Gamma. */
    private GammaFunction objGamma;


    /*------------------------------------------------------------------*/
    /*  Constructors                                                    */
    /*------------------------------------------------------------------*/

    /**
     * Constructor principal para un DOF y un valor X dados.
     *
     * @param intDOFIn grados de libertad de la distribución t.
     * @param dblXIn   límite superior de integración.
     */
    public SimpsonIntegration(int intDOFIn, double dblXIn) 
    {
        this.intDOF   = intDOFIn;
        this.dblX     = dblXIn;
        this.dblE     = 0.00001; /* valor por defecto, coherente con Logic */
        this.objGamma = new GammaFunction();
    }


    /*------------------------------------------------------------------*/
    /*  Public Simpson Helper Methods                                   */
    /*------------------------------------------------------------------*/

    /**
     * Calcula el ancho de subintervalo w en función del número de segmentos
     * y del límite superior x.
     *
     * @param intNumSegIn número de segmentos (debe ser par para Simpson).
     * @param dblXIn      límite superior de integración.
     */
    public void computeW(int intNumSegIn, double dblXIn) 
    {
        this.intNumSeg = intNumSegIn;
        this.dblX      = dblXIn;
        this.dblW      = dblXIn / (double) intNumSegIn;
    }

    /**
     * Calcula los puntos Xi en el intervalo [0, x], usando el ancho w.
     *
     * @param intNumSegIn número de segmentos de integración.
     */
    public void computeXi(int intNumSegIn) 
    {
        int intSize = intNumSegIn + 1;
        dblTotXi = new double[intSize];

        for (int intI = 0; intI <= intNumSegIn; intI++) 
        {
            dblTotXi[intI] = dblW * (double) intI;
        }
    }

    /**
     * Calcula los términos base (1 + Xi² / dof) para cada Xi.
     *
     * @param intNumSegIn   número de segmentos.
     * @param dblTotXiIn    arreglo de puntos Xi.
     * @param intDOFIn      grados de libertad.
     */
    public void computeFirstBaseTerms(int intNumSegIn,
                                      double[] dblTotXiIn,
                                      int intDOFIn) 
    {
        int intSize = intNumSegIn + 1;
        dblFirstBaseTerms = new double[intSize];

        for (int intI = 0; intI <= intNumSegIn; intI++) 
        {
            double dblXi = dblTotXiIn[intI];
            dblFirstBaseTerms[intI] =
                    1.0 + (dblXi * dblXi) / (double) intDOFIn;
        }
    }

    /**
     * Calcula el exponente de la función t:
     * exponent = - (dof + 1) / 2
     *
     * @param intDOFIn grados de libertad.
     */
    public void computeExponent(int intDOFIn) 
    {
        dblExponent = -((double) intDOFIn + 1.0) / 2.0;
    }

    /**
     * Calcula el coeficiente de la función de densidad t:
     *
     * coeff = Γ((ν+1)/2) / [ sqrt(νπ) * Γ(ν/2) ]
     *
     * donde ν = dof.
     *
     * @param intDOFIn grados de libertad.
     */
    public void computeCoefficient(int intDOFIn) 
    {
        double dblNu1;
        double dblNu2;
        double dblTop;
        double dblBottom;

        dblNu1 = ((double) intDOFIn + 1.0) / 2.0;
        dblNu2 = ((double) intDOFIn) / 2.0;

        // Numerador: Γ((ν+1)/2)
        dblTop = objGamma.computeDblGamma(dblNu1);

        // Denominador: sqrt(νπ) * Γ(ν/2)
        dblBottom = Math.sqrt(intDOFIn * Math.PI)
                  * objGamma.computeDblGamma(dblNu2);

        dblCoeff = dblTop / dblBottom;
    }

    /**
     * Calcula los valores de la función f(Xi) = coeff * (baseTerm^exponent)
     * para cada Xi, donde baseTerm = 1 + Xi² / dof.
     *
     * @param intNumSegIn número de segmentos.
     */
    public void computeFxi(int intNumSegIn) 
    {
        int intSize = intNumSegIn + 1;
        dblFxi = new double[intSize];

        for (int intI = 0; intI <= intNumSegIn; intI++) 
        {
            dblFxi[intI] = dblCoeff
                         * Math.pow(dblFirstBaseTerms[intI], dblExponent);
        }
    }

    /**
     * Aplica los multiplicadores de Simpson (1,4,2,4,...,4,1) a cada
     * valor f(Xi), generando los términos finales a sumar.
     *
     * @param intNumSegIn número de segmentos.
     */
    public void computeFinalTerms(int intNumSegIn) 
    {
        int intSize = intNumSegIn + 1;
        dblFinalTerms = new double[intSize];

        for (int intI = 0; intI <= intNumSegIn; intI++) 
        {
            int intMultiplier;

            if (intI == 0 || intI == intNumSegIn) 
            {
                intMultiplier = 1;
            } 
            else if (intI % 2 == 1) 
            {
                intMultiplier = 4;
            } 
            else 
            {
                intMultiplier = 2;
            }

            dblFinalTerms[intI] = (double) intMultiplier * dblFxi[intI];
        }
    }

    /**
     * Método principal de esta clase: calcula el valor final de la integral
     * con la regla de Simpson para un número de segmentos dado.
     * <p>
     * Sigue la secuencia:
     * <ol>
     *   <li>computeW</li>
     *   <li>computeXi</li>
     *   <li>computeFirstBaseTerms</li>
     *   <li>computeExponent</li>
     *   <li>computeCoefficient</li>
     *   <li>computeFxi</li>
     *   <li>computeFinalTerms</li>
     *   <li>Suma y multiplicación final por (w / 3)</li>
     * </ol>
     *
     * @param intNumSegIn número de segmentos (debe ser par).
     * @return valor aproximado de la integral desde 0 hasta x.
     */
    public double computeFinalValue(int intNumSegIn) 
    {
        double dblSum = 0.0;

        /* Secuencia lógica de cálculo siguiendo el diagrama */
        computeW(intNumSegIn, dblX);
        computeXi(intNumSegIn);
        computeFirstBaseTerms(intNumSegIn, dblTotXi, intDOF);
        computeExponent(intDOF);
        computeCoefficient(intDOF);
        computeFxi(intNumSegIn);
        computeFinalTerms(intNumSegIn);

        // Sumar todos los términos finales
        for (int intI = 0; intI <= intNumSegIn; intI++) 
        {
            dblSum += dblFinalTerms[intI];
        }

        // Aplicar el factor final de Simpson: (w / 3) * suma
        dblFinalValue = (dblW / 3.0) * dblSum;

        return dblFinalValue;
    }

    /**
     * Retorna el último valor de integral calculado.
     *
     * @return valor final de la integral almacenado en dblFinalValue.
     */
    public double getFinalValue() 
    {
        return dblFinalValue;
    }
}

