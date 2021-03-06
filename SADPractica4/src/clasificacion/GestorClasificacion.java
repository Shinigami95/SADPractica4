package clasificacion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.NominalPrediction;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.SerializationHelper;

/**
 * 
 * @author Mikel Alb&oacute;niga, Unai Garc&iacute;a y Jorge P&eacute;rez.
 */
public class GestorClasificacion {
	private static GestorClasificacion mGest;
	
	private GestorClasificacion(){}
	
	/**
	 * Devuelve una &uacute;nica instancia de la clase.
	 * @return mGest 
	 */
	public static GestorClasificacion getGestor(){
		if(mGest==null){
			mGest = new GestorClasificacion();
		}
		return mGest;
	}
	
	/**
	 * Clasifica las instancias del fichero pasado por uno de los par&aacute;metros. 
	 * La clasificaci&oacute;n se hace mediante la carga del modelo y el resultado puede ser escrito por pantalla o
	 * se guardar&aacute; en el fichero que determina cc en uno de sus atributos.
	 * @param cc -  ClasifConfig que guarda: El path del fichero con instancias a clasificar, el path del modelo, 
	 * un booleano que indica la utilizaci&oacute;n de NaibeBayes o BayesNetwork y el path del fichero(si lo hay) 
	 * donde se guardar&aacute;n los resultados.
	 */
	public void clasificar(ClasifConfig cc){
		try{
			File blind = new File(cc.getBlindPath());
			File model = new File(cc.getModelPath());
			if(!blind.exists()||!model.exists()) throw new FileNotFoundException();
			FileInputStream in = new FileInputStream(model);
			Classifier classif = null;
			if(cc.isNaive()){
				classif = (NaiveBayes) SerializationHelper.read(in);
			} else {
				classif = (BayesNet) SerializationHelper.read(in);
			}
			Instances blindInst = new Instances(new FileReader(blind));
			Attribute classAtrib = blindInst.attribute("class");
			blindInst.setClass(classAtrib);
			Evaluation eval = new Evaluation(blindInst);
			eval.evaluateModel(classif, blindInst);
			FastVector fv = eval.predictions();
			String result = "Inst\tActual\tPredicted\n";
			NominalPrediction np;
			for(int i=0; i<fv.size();i++){
				np = (NominalPrediction) fv.elementAt(i);
				result += (i+1)+"\t\t";
				result += "?\t\t";
				result += classAtrib.value((int)Math.round(np.predicted()))+"\n";
			}
			if(cc.getResultPath()!=null){
				File resFile = new File(cc.getResultPath());
				FileWriter fw = new FileWriter(resFile);
				fw.write(result);
				fw.close();
			} else {
				System.out.println(result);
			}
		} catch (FileNotFoundException e){
			System.out.println("ERROR, ficheros no encontrados.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
