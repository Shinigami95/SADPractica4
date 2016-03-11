package jorge;

import weka.core.Instances;

public interface TextPlainToArff {
	public abstract Instances createDatasetSupervised(String filePath) throws Exception;
	public Instances createDatasetUnsupervised(String filePath) throws Exception;
}
