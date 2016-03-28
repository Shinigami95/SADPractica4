package modelGenerator;

/**
 * 
 * @author Mikel Alb&oacute;niga, Unai Garc&iacute;a y Jorge P&eacute;rez.
 */
public class ModelConfig {
	private boolean useNaive;
	private boolean flagS;
	private String trainPath;
	private String devPath;
	private String modelPath;
	private int evaluationSchema; // 1) Not Honest, 2) 10-Fold CrossVal, 3) Hold-Out
	
	public ModelConfig(){
		useNaive = false;
		flagS = false;
		trainPath = null;
		devPath= null;
		modelPath= null;
		evaluationSchema= 0;
	}
	
	/**
	 * Devuelve un booleano que indica si se utiliza NaiveBayes o BayesNetwork. 
	 * @return useNaive
	 */
	public boolean isUseNaive() {
		return useNaive;
	}
	
	/**
	 * Guarda en la variable global useNaive el booleano que se pasa como par&aacute;metro. 
	 * @param blindPath Booleano que indica si se utiliza NaiveBayes o BayesNetwork.
	 */
	public void setUseNaive(boolean useNaive) {
		this.useNaive = useNaive;
	}
	
	/**
	 * Devuelve un booleano que indica si se quiere que aparezca m&aacute;s unformaci&oacute;n de la necesaria.
	 * @return flagS
	 */
	public boolean isFlagS() {
		return flagS;
	}
	
	/**
	 * Guarda en flagS el booleano que se le pasa por par&aacute;metro.
	 * @param flagS Booleano que indica si se quiere recibir mayor cantidad de informaci&oacute;n.
	 */
	public void setFlagS(boolean flagS) {
		this.flagS = flagS;
	}
	
	/**
	 * Devuelve un String con el path en el que est&aacute; guargado train.arff.
	 * @return trinpath 
	 */
	public String getTrainPath() {
		return trainPath;
	}
	
	/**
	 * Guarda en la variable trainPath el String que se le pasa como par&aacute;metro.
	 * @param trainPath String que indica el path en el que se guarda train.arff.
	 */
	public void setTrainPath(String trainPath) {
		this.trainPath = trainPath;
	}
	
	/**
	 Devuelve un String con el path en el que est&aacute; guargado dev.arff.
	 * @return devPath
	 */
	public String getDevPath() {
		return devPath;
	}
	
	/**
	 * Guarda en la variable devPath el String que se le pasa como par&aacute;metro.
	 * @param devPath String que indica el path en el que se guarda dev.arff.
	 */
	public void setDevPath(String devPath) {
		this.devPath = devPath;
	}
	
	/**
	 * Devuelve en un String el path del fichero blind.arff. 
	 * @return modelPath
	 */
	public String getModelPath() {
		return modelPath;
	}
	
	/**
	 * Guarda en la variable global modelPath el string que se pasa como par&aacute;metro. 
	 * @param modelPath String que indica el path se guardar&aacute; el binario con el modelo.
	 */
	public void setModelPath(String modelPath) {
		this.modelPath = modelPath;
	}
	
	/**
	 * Devuelve un int que indica el esquema de evaluaci&oacute;n a utilizar.
	 * @return evaluationSchema
	 */
	public int getEvaluationSchema() {
		return evaluationSchema;
	}
	
	/**
	 * Guarda en la variable evaluationSchema el int que se le pasa como par&aacute;metro.
	 * @param evaluationSchema Int que indica el esquema de evaluaci&oacute;n a utilizar:
	 * <br><br>
	 * <table align="center" border=1><tr><td align="center">Valor</td><td align="center">Tipo de esquema</td></tr>
	 * <tr><td align="center">1</td><td align="center">No Honesto</td></tr>
	 * <tr><td align="center">2</td><td align="center">Hold Out</td></tr>
	 * <tr><td align="center">3</td><td align="center">10-Fold Crossvalidation</td></tr>
	 * </table>
	 */
	public void setEvaluationSchema(int evaluationSchema) {
		this.evaluationSchema = evaluationSchema;
	}

}
