package jorge.modelGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.print.attribute.HashAttributeSet;

import org.xml.sax.HandlerBase;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.neighboursearch.balltrees.TopDownConstructor;

public class GestorAjusteParam {

	private static GestorAjusteParam mGest;
	
	private GestorAjusteParam(){}
	
	public static GestorAjusteParam getGestor() {
		if(mGest==null){
			mGest = new GestorAjusteParam();
		}
		return mGest;
	}

	public void infiere(ModelConfig mConf) {
		try {
			File train = new File(mConf.getTrainPath());
			File dev = new File(mConf.getDevPath());
			Instances trainInst = new Instances(new FileReader(train));
			Instances devInst = new Instances(new FileReader(dev));
			trainInst.setClass(trainInst.attribute("class"));
			devInst.setClass(devInst.attribute("class"));
			boolean flagS = mConf.isFlagS();
			if(mConf.isUseNaive()){
				naiveInference(trainInst,devInst,flagS);
			} else {
				bayesNetInference(trainInst,devInst,flagS);
			}
		} catch (FileNotFoundException e) {
			System.out.println("ERROR, ficheros incorrectos.");
		} catch (IOException e) {
			System.out.println("ERROR desconocido.");
		}
	}

	private void bayesNetInference(Instances trainInst, Instances devInst, boolean flagS) {
		// TODO Auto-generated method stub
		
	}

	private void naiveInference(Instances trainInst, Instances devInst, boolean flagS) {
		try {
			NaiveBayes nb = new NaiveBayes();
			nb.buildClassifier(trainInst);
			Evaluation eval = new Evaluation(trainInst);
			eval.evaluateModel(nb, devInst);
			if(flagS){	
				System.out.println(eval.toSummaryString());
				System.out.println(eval.toClassDetailsString());
				System.out.println(eval.toMatrixString());
			}
			int index = getMinimunClassIndex(trainInst);
			double fmeasure = eval.fMeasure(index);
			System.out.println("El f-measure de la clase minoritaria es: "+fmeasure);
		} catch (Exception e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
	}

	private int getMinimunClassIndex(Instances trainInst) {
		int index = -1;
		int valOfIndex = trainInst.numInstances()+1;
		HashMap<String,Integer> hm = new HashMap<String,Integer>();
		Attribute attribClass = trainInst.classAttribute();
		Instance inst;
		String key;
		for(int i =0; i<trainInst.numInstances();i++){
			inst = trainInst.instance(i);
			key = inst.stringValue(attribClass);
			if(!hm.containsKey(key)){
				hm.put(key, 0);
			}
			hm.put(key, hm.get(key)+1);
		}
		Set<String> setStr = hm.keySet();
		for(String str : setStr){
			System.out.println(str+" "+hm.get(str));
			if(hm.get(str)<valOfIndex){
				valOfIndex = hm.get(str);
				index = attribClass.indexOfValue(str);
			}
		}
		System.out.println("La clase minoritaria es: "+attribClass.value(index));
		return index;
	}
}
