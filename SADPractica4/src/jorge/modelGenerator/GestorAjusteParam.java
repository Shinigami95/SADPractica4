package jorge.modelGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.net.estimate.BMAEstimator;
import weka.classifiers.bayes.net.estimate.BayesNetEstimator;
import weka.classifiers.bayes.net.estimate.MultiNomialBMAEstimator;
import weka.classifiers.bayes.net.estimate.SimpleEstimator;
import weka.classifiers.bayes.net.search.SearchAlgorithm;
import weka.classifiers.bayes.net.search.local.HillClimber;
import weka.classifiers.bayes.net.search.local.K2;
import weka.classifiers.bayes.net.search.local.LAGDHillClimber;
import weka.classifiers.bayes.net.search.local.RepeatedHillClimber;
import weka.classifiers.bayes.net.search.local.TAN;
import weka.classifiers.bayes.net.search.local.TabuSearch;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

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
		} catch (Exception e) {
			System.out.println("ERROR desconocido.");
			e.printStackTrace();
		}
	}

	private void bayesNetInference(Instances trainInst, Instances devInst, boolean flagS) throws Exception {
		BayesNet bn = new BayesNet();
		int iMax=0, jMax=0;
		double fmeasure = -1.0;
		double fmeasureAux = 0.0;
		Evaluation eval, evalMax=null;
		for(int i = 0; i<=2; i++){
			bn.setEstimator(getEstimator(i));
			for(int j = 0; j<=4; j++){
				bn.setSearchAlgorithm(getSearch(j));
				bn.buildClassifier(trainInst);
				eval = bayesNetEval(bn,trainInst, devInst, flagS);
				if(eval !=null){
					int index = getMinimunClassIndex(trainInst);
					fmeasureAux = eval.fMeasure(index);
					if(fmeasureAux>fmeasure){
						fmeasure = fmeasureAux;
						iMax = i;
						jMax = j;
						evalMax = eval;
					}
				}
			}
		}
		System.out.println("---- RESULTADOS \u00D3PTIMOS ----");
		System.out.println("Los mejores par\00E3metros son:");
		System.out.println("\tEstimator: "+getEstimator(iMax));
		System.out.println("\tSearchAlg: "+getSearch(jMax));
		System.out.println(evalMax.toSummaryString());
		System.out.println(evalMax.toClassDetailsString());
		System.out.println(evalMax.toMatrixString());
		System.out.println("---------------------------------");
	}
	
	private Evaluation bayesNetEval(BayesNet bn, Instances trainInst, Instances devInst, boolean flagS) {
		try {
			double fmeasure = 0.0;
			Evaluation eval = new Evaluation(trainInst);
			eval.evaluateModel(bn, devInst);
			int index = getMinimunClassIndex(trainInst);
			fmeasure = eval.fMeasure(index);
			if(flagS){	
				System.out.println(eval.toSummaryString());
				System.out.println(eval.toClassDetailsString());
				System.out.println(eval.toMatrixString());
			}
			System.out.println("Bayes Net: "+bn.getSearchAlgorithm().getClass()+" / "+bn.getEstimator().getClass()+" -> f-measure:"+fmeasure);
			return eval;
		} catch (Exception e) {
			System.out.println("ERROR -> "+bn.getSearchAlgorithm().getClass()+" / "+bn.getEstimator().getClass());
			return null;
		}
	}

	/*
		0 - SimpleEstimator
		1 - MultiNomialBMAEstimator
		2 - BMAEstimator
	 */
	private BayesNetEstimator getEstimator(int i){
		BayesNetEstimator bne;
		switch(i){
			case 0:
				bne = new SimpleEstimator();
				break;
			case 1:
				bne = new MultiNomialBMAEstimator();
				break;
			case 2:
				bne = new BMAEstimator();
				break;
			default:
				bne = null;
				break;
		}
		System.out.println("ESTIMATOR "+i+": " +bne.getClass());
		return bne;
	}
	
	/*
		0 - HillClimber
		1 - K2
		2 - LAGDHillClimber
		3 - RepeatedHillClimber
		4 - TabuSearch
	 */
	private SearchAlgorithm getSearch(int i){
		SearchAlgorithm sa;
		switch(i){
			case 0:
				sa = new HillClimber();
				break;
			case 1:
				sa = new K2();
				break;
			case 2:
				sa = new LAGDHillClimber();
				break;
			case 3:
				sa = new RepeatedHillClimber();
				break;
			case 4:
				sa = new TabuSearch();
				break;
			default:
				sa = null;
				break;
		}
		System.out.println("SEARCH "+i+": " +sa.getClass());
		return sa;
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
			if(hm.get(str)<valOfIndex){
				valOfIndex = hm.get(str);
				index = attribClass.indexOfValue(str);
			}
		}
		return index;
	}
}
