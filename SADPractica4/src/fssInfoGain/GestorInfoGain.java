package fssInfoGain;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

/**
 * 
 * @author Mikel Alb&oacute;niga, Unai Garc&iacute;a y Jorge P&eacute;rez.
 */
public class GestorInfoGain {

	private static GestorInfoGain mGest;
	
	private GestorInfoGain(){}
	
	/**
	 * Devuelve una unica &uacute;nica instancia de la clase.
	 * @return mGest 
	 */
	public static GestorInfoGain getGestor() {
		if(mGest == null){
			mGest = new GestorInfoGain();
		}
		return mGest;
	}
	
	/**
	 * Filtra con AtributeSelection (InfoGain) las instancias del fichero pasado por par&aacute;metros, 
	 * las separa en varios arff y los crea en en la carpeta determinada por el par&ametro filObj.
	 * @param filTrain - Fichero con todas las instancias a filtran con AtributeSelection (InfoGain).
	 * @param filArray - Array de ficheros en los que se separar&aacute;n las instancias ya filtradas.
	 * @param filObj - Fichero/directorio en el que se almacenar&aacute;n los arff generados.
	 */
	public void generateInfoGainedAttributes(File filTrain, File[] filArray, File filObj) {
		try {
			System.out.println("Aplicando InfoGain...");
			Instances trainInst = new Instances(new FileReader(filTrain));
			trainInst.setClass(trainInst.attribute("class"));
			AttributeSelection attribSel = new AttributeSelection();
			InfoGainAttributeEval iga = new InfoGainAttributeEval();
			Ranker rank = new Ranker();
			rank.setThreshold(0.0);
			rank.setNumToSelect(-1);
			attribSel.setEvaluator(iga);
			attribSel.setSearch(rank);
			attribSel.setInputFormat(trainInst);
			System.out.println("Fich: "+filTrain.getName());
			trainInst = Filter.useFilter(trainInst, attribSel);
			FileWriter fw = new FileWriter(filObj+"/FSSIG_"+filTrain.getName());
			fw.write(trainInst.toString());
			fw.close();
			System.out.println("--> FSSIG_"+filTrain.getName()+" creado correctamente.");
			Instances inst;
			for(File fil : filArray){
				System.out.println("Fich: "+fil.getName());
				inst = new Instances(new FileReader(fil));
				inst = Filter.useFilter(inst, attribSel);
				fw = new FileWriter(filObj+"/FSSIG_"+fil.getName());
				fw.write(inst.toString());
				fw.close();
				System.out.println("--> FSSIG_"+fil.getName()+" creado correctamente.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
