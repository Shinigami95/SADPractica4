package arffGenerator;

import weka.core.Instances;

/**
 * 
 * @author Mikel Alb&oacute;niga, Unai Garc&iacute;a y Jorge P&eacute;rez.
 */
public interface TextPlainToArff {
	
	/**
	 * Crea el conjunto de datos supervisado.
	 * @param filePath - Path del fichero/directorio que almacena las instancias.
	 * @return Instances - Instancias generadas a partir del texto plano.
	 * @throws Exception
	 */
	public abstract Instances createDatasetSupervised(String filePath) throws Exception;
	
	/**
	 * Crea el conjunto de datos no supervisado.
	 * @param filePath - Path del fichero/directorio que almacena las instancias.
	 * @return Instances - Instancias generadas a partir del texto plano.
	 * @throws Exception
	 */
	public Instances createDatasetUnsupervised(String filePath) throws Exception;
}
