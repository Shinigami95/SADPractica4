package jorge.clasificacion;

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

	public String getBlindPath() {
		return blindPath;
	}

	public void setBlindPath(String blindPath) {
		this.blindPath = blindPath;
	}

	public String getModelPath() {
		return modelPath;
	}

	public void setModelPath(String modelPath) {
		this.modelPath = modelPath;
	}
	
	public String getResultPath() {
		return resultPath;
	}

	public void setResultPath(String resultPath) {
		this.resultPath = resultPath;
	}

	public boolean isNaive() {
		return naive;
	}

	public void setNaive(boolean naive) {
		this.naive = naive;
	}
	
	
}
