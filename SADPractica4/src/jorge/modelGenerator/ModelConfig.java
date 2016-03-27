package jorge.modelGenerator;

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
	
	public boolean isUseNaive() {
		return useNaive;
	}

	public void setUseNaive(boolean useNaive) {
		this.useNaive = useNaive;
	}

	public boolean isFlagS() {
		return flagS;
	}

	public void setFlagS(boolean flagS) {
		this.flagS = flagS;
	}

	public String getTrainPath() {
		return trainPath;
	}

	public void setTrainPath(String trainPath) {
		this.trainPath = trainPath;
	}

	public String getDevPath() {
		return devPath;
	}

	public void setDevPath(String devPath) {
		this.devPath = devPath;
	}
	
	public String getModelPath() {
		return modelPath;
	}

	public void setModelPath(String modelPath) {
		this.modelPath = modelPath;
	}
	
	public int getEvaluationSchema() {
		return evaluationSchema;
	}

	public void setEvaluationSchema(int evaluationSchema) {
		this.evaluationSchema = evaluationSchema;
	}

}
