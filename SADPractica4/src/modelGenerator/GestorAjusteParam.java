package modelGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.net.estimate.BMAEstimator;
import weka.classifiers.bayes.net.estimate.BayesNetEstimator;
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
import weka.core.SerializationHelper;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemovePercentage;

/**
 * 
 * @author Mikel Alb&oacute;niga, Unai Garc&iacute;a y Jorge P&eacute;rez.
 */
public class GestorAjusteParam {

	private static GestorAjusteParam mGest;
	
	private GestorAjusteParam(){}
	
	/**
	 * Devuelve una &uacute;nica instancia de la clase.
	 * @return mGest 
	 */
	public static GestorAjusteParam getGestor() {
		if(mGest==null){
			mGest = new GestorAjusteParam();
		}
		return mGest;
	}
	
	/**
	 * Hace las llamadas necesarias para clasificar y crear un modelo de aprendizaje seg&uacute;n el par&aacute;metro que se le pasa.
	 * @param mConf - Modelo de configuraci&oacute;n en el que se guardar&aacute;n los siguientes datos: 
	 * booleano que indica si se utiliza NaiveBayes o BayesNetwork, el path donde se encuentra fichero train.arff, 
	 * el path donde se encuentra el fichero dev.arff, el path en el que se desea guardar&aacute; el modelo,
	 *  un booleano que indica si se mostrar&aacute; toda la informaci&oacute;n por pantalla y 
	 *  un int que determina el esquema de evaluaci&oacute;n. Estos son los posibles valores del esquema de evaluaci&oacute;n:
	 * <br><br>
	 * <table align="center" border=1><tr><th align="center">Valor</th><th align="center">Tipo de esquema</th></tr>
	 * <tr><td align="center">1</td><td align="center">No Honesto</td></tr>
	 * <tr><td align="center">2</td><td align="center">Hold Out</td></tr>
	 * <tr><td align="center">3</td><td align="center">10-Fold Crossvalidation</td></tr>
	 * </table>
	 */
	public void infiereConEsquema(ModelConfig mConf) {
		try {
			File train = new File(mConf.getTrainPath());
			File dev = new File(mConf.getDevPath());
			Instances trainInst = new Instances(new FileReader(train));
			Instances devInst = new Instances(new FileReader(dev));
			trainInst.setClass(trainInst.attribute("class"));
			devInst.setClass(devInst.attribute("class"));
			boolean flagS = mConf.isFlagS();
			Instances[] arrayInst = {trainInst,devInst};
			Instances trainDev = this.combine(arrayInst);
			String pathModBin = mConf.getModelPath();
			if(mConf.isUseNaive()){
				inferenceConEsquema(new NaiveBayes(), mConf.getEvaluationSchema(), trainDev, pathModBin);
			} else {
				BayesNet bn = bayesNetInference(trainInst,devInst,flagS);
				inferenceConEsquema(bn, mConf.getEvaluationSchema(), trainDev, pathModBin);
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
	
	/**
	 * Hace la clasificaci&oacute;n de las instacias indicadas en los par&aacute;metros y 
	 * guarda el modelo de aprendizaje en el path determinado por el atributo modelPath.
	 * @param classif -  Es el tipo de clasificado a utilizar. Puede ser NaiveBayes o BayesNetwork.
	 * @param trainDev - Es el conjunto de instancias a clasificar y del que seguarda el modelo de lo que ha aprendido.
	 * @param modelPath - String que indica el path en el que se guardar&aacute; el modelo obtenido.
	 * @param esq - Int que indica el tipo de esquema de evaluaci&oacute;n a utilizar. Sus valores pueden ser los siguientes:
	 * <br><br>
	 * <table align="center" border=1><tr><th align="center">Valor</th><th align="center">Tipo de esquema</th></tr>
	 * <tr><td align="center">1</td><td align="center">No Honesto</td></tr>
	 * <tr><td align="center">2</td><td align="center">Hold Out</td></tr>
	 * <tr><td align="center">3</td><td align="center">10-Fold Crossvalidation</td></tr>
	 * </table>
	 */
	private void inferenceConEsquema(Classifier classif,int esq, Instances trainDev, String modelPath) {
		try {
			trainDev.randomize(new Random(42));
			classif.buildClassifier(trainDev);
			Evaluation eval = null;;
			if(esq == 1){
				System.out.println("ESQUEMA NO HONESTO");
				eval = new Evaluation(trainDev);
				eval.evaluateModel(classif, trainDev);
			} else if(esq == 2){
				System.out.println("ESQUEMA HOLD OUT");
				Instances[] trainTest = this.splitInstances(0.7,trainDev);
				eval = new Evaluation(trainTest[0]);
				eval.evaluateModel(classif, trainTest[1]);
			} else if(esq == 3) {
				System.out.println("ESQUEMA 10-Fold CROSSVALIDATION");
				eval = new Evaluation(trainDev);
				eval.crossValidateModel(classif, trainDev, 10, new Random(42));
			} else throw new Exception("Esquema incorrecto");
			System.out.println("LOS RESULTADOS DE LA EVALUACI\u00D3N SON:");
			System.out.println(eval.toSummaryString());
			System.out.println(eval.toClassDetailsString());
			System.out.println(eval.toMatrixString());
			if(modelPath!=null){
				System.out.println("Guardando modelo en fichero: "+modelPath);
				File modBin = new File(modelPath);
				FileOutputStream out = new FileOutputStream(modBin);
				SerializationHelper.write(out, classif);
				out.close();
				System.out.println("Modelo guardado.");
			}
		} catch (Exception e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
	}
	
	/**
	 * Separa las instancias en dos conjuntos mediante la especificaci&oacute;n de un porcentage con el filtro RemovePercentage.
	 * @param percentageSplit - Double que indica el porcentaje en el que se separan las instancias.
	 * @param trainDev - Instancias a separar con el filtro RemovePercentage.
	 * @return trainTest - Array en el que se guardan las instancias separadas por el filtro RemovePercentage.
	 * @throws Exception
	 */
	private Instances[] splitInstances(double percentageSplit, Instances trainDev) throws Exception {
		Instances[] trainTest = new Instances[2];
		RemovePercentage rp = new RemovePercentage();
		rp.setInputFormat(trainDev);
		rp.setPercentage(percentageSplit);
		rp.setInvertSelection(true);
		trainTest[0] = Filter.useFilter(trainDev, rp);
		rp = new RemovePercentage();
		rp.setInputFormat(trainDev);
		rp.setPercentage(percentageSplit);
		rp.setInvertSelection(false);
		trainTest[1] = Filter.useFilter(trainDev, rp);
		return trainTest;
	}
	
	/**
	 * Hace las llamadas necesarias para clasificar y crear un modelo de aprendizaje seg&uacute;n el par&aacute;metro que se le pasa.
	 * @param mConf - Modelo de configuraci&oacute;n en el que se guardar&aacute;n los siguientes datos: 
	 * booleano que indica si se utiliza NaiveBayes o BayesNetwork, el path donde se encuentra fichero train.arff, 
	 * el path donde se encuentra el fichero dev.arff, el path en el que se desea guardar&aacute; el modelo,
	 *  un booleano que indica si se mostrar&aacute; toda la informaci&oacute;n por pantalla y 
	 *  un int que determina el esquema de evaluaci&oacute;n (en este caso no se utiliza). Estos son los posibles valores del esquema de evaluaci&oacute;n:
	 * <br><br>
	 * <table align="center" border=1><tr><th align="center">Valor</th><th align="center">Tipo de esquema</th></tr>
	 * <tr><td align="center">1</td><td align="center">No Honesto</td></tr>
	 * <tr><td align="center">2</td><td align="center">Hold Out</td></tr>
	 * <tr><td align="center">3</td><td align="center">10-Fold Crossvalidation</td></tr>
	 * </table>
	 */
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
	
	/**
	 * Busca la mejor configuraci&oacute;n del clasificador BayesNetwork.
	 * @param trainInst - Instancias con las que se entrena al clasificador.
	 * @param devInst - Instancias con las que se probar&aacute; lo aprendido con trainInst.
	 * @param flagS - Booleano que indica si se quiere mostrar por pantalla toda la informaci&oacute;n de la clasificaci&oacute;n.
	 * @return trainInst - Clasificador con la configuraci&oacute;n que mayor fmeasure ha obtenido para la clase minoritaria.
	 * @throws Exception
	 */
	private BayesNet bayesNetInference(Instances trainInst, Instances devInst, boolean flagS) throws Exception {
		BayesNet bn, bnMax = null;
		int iMax=0, jMax=0;
		double fmeasure = -1.0;
		double fmeasureAux = 0.0;
		Evaluation eval, evalMax=null;
		for(int i = 0; i<=1; i++){
			for(int j = 0; j<=5; j++){
				try{
					bn = new BayesNet();
					bn.setEstimator(getEstimator(i));
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
							bnMax = bn;
							evalMax = eval;
						}
					}
				}catch (Exception e){
					System.out.println("ERROR -> "+getSearch(j).getClass()+" / "+getEstimator(i).getClass());
				}
			}
		}
		System.out.println("---- RESULTADOS \u00D3PTIMOS ----");
		System.out.println("Los mejores par\u00E1metros son:");
		System.out.println("\tEstimator: "+getEstimator(iMax));
		System.out.println("\tSearchAlg: "+getSearch(jMax));
		System.out.println(evalMax.toSummaryString());
		System.out.println(evalMax.toClassDetailsString());
		System.out.println(evalMax.toMatrixString());
		System.out.println("---------------------------------");
		return bnMax;
	}
	
	/**
	 * Clasifica las instancias que se le pasan con el par&aacute;metro BayesNetwork 
	 * y devuelve la evaluaci&oacute;n obtenida.
	 * @param bn - Clasificador BayesNetwork con una con una configuraci&oacute;n espec&iacute;fica.
	 * @param trainInst - Instancias con las que se entrena al clasificador.
	 * @param devInst - Instancias con las que se probar&aacute; lo aprendido con trainInst.
	 * @param flagS - Booleano que indica si se quiere mostrar por pantalla toda la informaci&oacute;n de la clasificaci&oacute;n.
	 * @return eval - Evaluador con el que se ha clasificado.
	 * @throws Exception
	 */
	private Evaluation bayesNetEval(BayesNet bn, Instances trainInst, Instances devInst, boolean flagS) throws Exception {
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
	}

	/**
	 * Especifica el estimador a utilizar en el clasificador BayesNetwork.
	 * @param i - Int que indica el estimador a utilizar en el clasificador BayesNetwork. Sus posibles valores son:
	 * <br><br>
	 * <table align="center" border=1><tr><th align="center">Valor</th><th align="center">Estimador</th></tr>
	 * <tr><td align="center">0</td><td align="center">SimpleEstimator</td></tr>
	 * <tr><td align="center">1</td><td align="center">BMAEstimator</td></tr>
	 * </table>
	 * @return bne - Clasificador con la configuraci&oacute;n del estimador actualizada.
	 */
	private BayesNetEstimator getEstimator(int i){
		BayesNetEstimator bne;
		switch(i){
			case 0:
				bne = new SimpleEstimator();
				break;
			case 1:
				bne = new BMAEstimator();
				break;
			default:
				bne = null;
				break;
		}
		System.out.println("ESTIMATOR "+i+": " +bne.getClass());
		return bne;
	}

	/**
	 * Especifica el algoritmo de busqueda a utilizar en el clasificador BayesNetwork.
	 * @param i - Int que indica el algoritmo de busqueda a utilizar en el clasificador BayesNetwork. Sus posibles valores son:
	 * <br><br>
	 * <table align="center" border=1><tr><th align="center">Valor</th><th align="center">Algoritmo de busqueda</th></tr>
	 * <tr><td align="center">0</td><td align="center">HillClimber</td></tr>
	 * <tr><td align="center">1</td><td align="center">K2</td></tr>
	 * <tr><td align="center">2</td><td align="center">LAGDHillClimber</td></tr>
	 * <tr><td align="center">3</td><td align="center">RepeatedHillClimber</td></tr>
	 * <tr><td align="center">4</td><td align="center">TabuSearch</td></tr>
	 * <tr><td align="center">5</td><td align="center">TAN</td></tr>
	 * </table>
	 * @return bne - Clasificador con la configuraci&oacute;n del algoritmo de busqueda actualizada.
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
			case 5:
				sa = new TAN();
				break;
			default:
				sa = null;
				break;
		}
		System.out.println("SEARCH "+i+": " +sa.getClass());
		return sa;
	}
	
	/**
	 * Clasifica mediante NaiveBayes las instancias que se le pasan como par&aacute;metro mediante train vs test.
	 * @param trainInst - Instancias con las que se entrena al clasificador.
	 * @param devInst - Instancias con las que se probar&aacute; lo aprendido con trainInst.
	 * @param flagS - Booleano que indica si se quiere mostrar por pantalla toda la informaci&oacute;n de la clasificaci&oacute;n.
	 */
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
	
	/**
	 * Devuelve la clase minoritaria.
	 * @param trainInst - Instancias que ha de recorrer para encontrar la clase minoritaria.
	 * @return index - &Iacute;ndice de la clase minoritaria.
	 */
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
	
	/**
	 * Fusiona el array de instancias que se le pasa por par&aacute;metro.
	 * @param instList - Array de instancias a combinar.
	 * @return allInst - Instancias ya fusionadas.
	 */
	private Instances combine(Instances[] instList) {
		Instances allInst = new Instances(instList[0]);
		for(int i = 1; i<instList.length;i++){
			for(int j = 0; j<instList[i].numInstances();j++){
				allInst.add(instList[i].instance(j));
			}
		}
		return allInst;
	}
}
