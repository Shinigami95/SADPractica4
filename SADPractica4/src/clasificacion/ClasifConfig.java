package clasificacion;

/**
 * 
 * @author Mikel Alb&oacute;niga, Unai Garc&iacute;a y Jorge P&eacute;rez.
 */
public class ClasifConfig {
	private String blindPath;
	private String modelPath;
	private String resultPath;
	private boolean naive;
	
	public ClasifConfig() {
		this.blindPath = null;
		this.modelPath = null;
		this.resultPath = null;
		this.naive = false;
	}
	
	/**
	 * Devuelve en un String el path del fichero blind.arff. 
	 * @return blindPath
	 */
	public String getBlindPath() {
		return blindPath;
	}
	
	/**
	 * Guarda en la variable global blindPath el string que se pasa como par&aacute;metro. 
	 * @param blindPath - String que indica el path en el que se encuentra el fichero blind.arff.
	 */
	public void setBlindPath(String blindPath) {
		this.blindPath = blindPath;
	}
	
	/**
	 * Devuelve en un String el path del binario que guarda el modelo. 
	 * @return modelPath
	 */
	public String getModelPath() {
		return modelPath;
	}
	
	/**
	 * Guarda en la variable global modelPath el string que se pasa como par&aacute;metro. 
	 * @param modelPath - String que indica el path en el que se encuentra el binario que guarda el modelo.
	 */
	public void setModelPath(String modelPath) {
		this.modelPath = modelPath;
	}
	
	/**
	 * Devuelve en un String el path del fichero que se guardar&aacute;n los resultados predecidos.
	 * @return resultPath
	 */
	public String getResultPath() {
		return resultPath;
	}
	
	/**
	 * Guarda en la variable global resulPath el string que se pasa como par&aacute;metro. 
	 * @param resulPath - String que indica el path en el que se guardar&aacute; el fichero con los resultados de las predicciones.
	 */
	public void setResultPath(String resultPath) {
		this.resultPath = resultPath;
	}
	
	/**
	 * Devuelve un booleano que indica si se ha utilizado NaiveBayes o BayesNetwork.
	 * @return naive
	 */
	public boolean isNaive() {
		return naive;
	}
	
	/**
	 * Guarda en la variable global resulPath el booleano que se pasa como par&aacute;metro. 
	 * @param resulPath - String que indica el path en el que se guardar&aacute; el fichero con los resultados de las predicciones.
	 */
	public void setNaive(boolean naive) {
		this.naive = naive;
	}
	
	
}
