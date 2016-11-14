
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Slaush
 */
public class GeneradorDeGraphicas 
{
   static public String SAWTOOTH = "Diente sierra";
   static public String TRIANGULAR = "Triangular";
   static public String SQUARE = "Cuadrada";
   static public String SIN = "Senoidal";
   static public String COS = "Cosenoidal";
   
  public static JFreeChart createChart(double amplitud, double frecuencia, double fase, 
                                  int desde, int hasta,boolean ruido, String tipo)
  {
    int frames = hasta - desde;
    double [][]matrizGrafica = new double[2][frames] ;        
    
    llenarTiempo(matrizGrafica,desde,frames);
    llenarY(matrizGrafica,frames,amplitud,frecuencia,fase,tipo);
    if(ruido)
        agregarRuido(matrizGrafica,frames);
    DefaultXYDataset dataset = new DefaultXYDataset();
    dataset.addSeries(tipo, matrizGrafica);
      
 
      // Create the chart
      return ChartFactory.createXYLineChart
      (
         "", // The chart title
         "", // x axis label
         "", // y axis label
         dataset, // The dataset for the chart
         PlotOrientation.VERTICAL,
         true, // Is a legend required?
         false, // Use tooltips
         true // Configure chart to generate URLs?
      );
  } 
  
  public static double [][] retornarMatriz(double amplitud, double frecuencia, double fase, 
                                  int desde, int hasta,boolean ruido, String tipo)
  {
    int frames = hasta - desde;
    double [][]matrizGrafica = new double[2][frames] ;        
    
    llenarTiempo(matrizGrafica,desde,frames);
    llenarY(matrizGrafica,frames,amplitud,frecuencia,fase,tipo);
    if(ruido)
        agregarRuido(matrizGrafica,frames);
    return matrizGrafica;
  } 
  
  public static double [][] sumarMatrices(double[][] m1, double [][]m2, int l1, int l2)
  { 
    int lr;
    if(l1 > l2)
        lr = l1;
    else
        lr = l2;
    double [][] resultado = new double[2][lr];
    
    for(int i = 0; i<lr; i++)
    {
        if(l1 > l2)
            resultado[0][i] = m1[0][i];    
        else
            resultado[0][i] = m2[0][i];
        if(l1>= i && l2 >=i)
            resultado[1][i] = m1[1][i] + m2[1][i];
        else if(l2< i)
            resultado[1][i] = m1[1][i];
        else
            resultado[1][i] = m2[1][i];
    }
    return resultado;
  }
  
  public static JFreeChart pintarMatriz(double[][] m)
  {
       DefaultXYDataset dataset = new DefaultXYDataset();
    dataset.addSeries("", m);
      
 
      // Create the chart
      return ChartFactory.createXYLineChart
      (
         "", // The chart title
         "", // x axis label
         "", // y axis label
         dataset, // The dataset for the chart
         PlotOrientation.VERTICAL,
         false, // Is a legend required?
         false, // Use tooltips
         true // Configure chart to generate URLs?
      );
  }
    private static double square(double x)
   {
       double s = Math.sin(x);
       if(s>=0)
           return 1;
       else
           return 0;
   }
   
   private static double sawTooth(double n)
   {   
       
       double tn;

       tn = ceil((n+Math.PI)/(2*Math.PI));
           //System.out.println(tn);
       return ((n - tn*2*Math.PI) + 2*Math.PI)/Math.PI;
           //System.out.println(n[1][i]);
       
   }
   
   private static double triangle(double x)
   {
      return Math.abs( 1 - x % (2*1) );
   }
   
   private static double ceil(double d)
   {
       long iPart = (long) d;
       double fPart = d - iPart;
       if(fPart >= 0.5d)
           return iPart+1;
       else 
           return iPart;
   }

    private static void llenarTiempo(double[][] matrizGrafica, int desde, int frames) 
    {
        for(int i = 0; i<frames; i++)
        {
            matrizGrafica[0][i] = (double)(desde+i)/1000;
        }
    }

    private static void llenarY(double[][] matrizGrafica, int frames, double amplitud, 
                                double frecuencia, double fase, String tipo) 
    {
      for(int i = 0; i<frames; i++)
      {
          matrizGrafica[1][i] = seleccionarFuncion(matrizGrafica[0][i],amplitud,frecuencia,fase,tipo);
      }  
    }

    private static double seleccionarFuncion(double t, double amplitud, double frecuencia,double fase, String tipo) 
    {   
        
        if(amplitud == 0)
            amplitud = 1;
        if(frecuencia == 0)
            frecuencia = 1;
        
            if(tipo.equals(SAWTOOTH))
                return amplitud*sawTooth(t*frecuencia + fase);
            if(tipo.equals(SQUARE))
                return amplitud*square(t*frecuencia + fase);
            if(tipo.equals(TRIANGULAR))
                return amplitud*triangle(t*frecuencia + fase);
            if(tipo.equals(SIN))
                return amplitud*Math.sin(t*frecuencia + fase);
            if(tipo.equals(COS))
                return amplitud*Math.cos(t*frecuencia + fase);
            return 0;

    }

    private static void agregarRuido(double[][] matrizGrafica, int frames) 
    {
        for(int i = 0; i<frames; i++)
            matrizGrafica[1][i] = matrizGrafica[1][i]+(Math.random()%frames);
    }
    
    
}
