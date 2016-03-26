package jorge.modelGenerator;

public class ModelConfig {
	private boolean useNaive;
	private boolean flagS;
	private String trainPath;
	private String devPath;
	
	public ModelConfig(){
		useNaive = false;
		flagS = false;
		trainPath = null;
		devPath= null;
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

}
