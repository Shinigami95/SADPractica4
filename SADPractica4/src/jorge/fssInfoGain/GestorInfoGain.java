package jorge.fssInfoGain;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

public class GestorInfoGain {

	private static GestorInfoGain mGest;
	
	private GestorInfoGain(){}
	
	public static GestorInfoGain getGestor() {
		if(mGest == null){
			mGest = new GestorInfoGain();
		}
		return mGest;
	}
	
	public void generateInfoGainedAttributes(File filTrain, File[] filArray, File filObj) {
		try {
			Instances trainInst = new Instances(new FileReader(filTrain));
			trainInst.setClass(trainInst.attribute("class"));
			AttributeSelection attribSel = new AttributeSelection();
			InfoGainAttributeEval iga = new InfoGainAttributeEval();
			Ranker rank = new Ranker();
			rank.setThreshold(0.0);
			attribSel.setEvaluator(iga);
			attribSel.setSearch(rank);
			attribSel.setInputFormat(trainInst);
			trainInst = Filter.useFilter(trainInst, attribSel);
			FileWriter fw = new FileWriter(filObj+"/FSSIG_"+filTrain.getName());
			fw.write(trainInst.toString());
			Instances inst;
			for(File fil : filArray){
				inst = new Instances(new FileReader(fil));
				inst = Filter.useFilter(inst, attribSel);
				fw = new FileWriter(filObj+"/FSSIG_"+fil.getName());
				fw.write(inst.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/*	public void generateInfoGainedAttributes(File filTrain, File[] filArray, File filObj) {
		try {
			AttributeSelection attribSel = new AttributeSelection();
			InfoGainAttributeEval iga = new InfoGainAttributeEval();
			Ranker rank = new Ranker();
			rank.setThreshold(0.0);
			attribSel.setEvaluator(iga);
			attribSel.setSearch(rank);
			Instances trainInst;
			trainInst = new Instances(new FileReader(filTrain));
			trainInst.setClass(trainInst.attribute("class"));
			attribSel.SelectAttributes(trainInst);
			attribSel.setRanking(true);
			attribSel.setXval(false);
			System.out.println(attribSel.toResultsString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
*/
}
