package jorge.arff2BOW;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class GestorBagOfWords {

	private static GestorBagOfWords mGest;
	
	private GestorBagOfWords(){}
	
	public static GestorBagOfWords getGestor() {
		if(mGest==null){
			mGest = new GestorBagOfWords();
		}
		return mGest;
	}
	
	public void generateBOW(File objetiveFil, File[] arffs){
		Instances[] instList = new Instances[arffs.length];
		FileReader rd;
		try{
			Add addFilter = new Add();
			addFilter.setAttributeIndex("last");
			addFilter.setNominalLabels(this.getNomLabels(arffs));
			addFilter.setAttributeName("File-Name");
			int attInd;
			for(int i = 0;i<instList.length;i++){
				rd = new FileReader(arffs[i]);
				instList[i] = new Instances(rd);
				addFilter.setInputFormat(instList[i]);
				instList[i] = Filter.useFilter(instList[i], addFilter);
				attInd = instList[i].numAttributes()-1;
				for(int j = 0; j<instList[i].numInstances();j++){
					instList[i].instance(j).setValue(attInd, arffs[i].getName());
				}
			}
			Instances allInst = this.getAllInstances(instList);
			allInst = this.filtrarConBOW(allInst);
			instList = this.separarInst(allInst);
		} catch(FileNotFoundException e) {
			System.out.println("ERROR, fichero no encontrado");
		} catch(IOException e) {
			System.out.println("ERROR desconocido");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Instances[] separarInst(Instances allInst) {
		HashMap<String, Instances> hm = new HashMap<String, Instances>();
		Instance inst;
		String key;
		for(int i = 0; i<allInst.numInstances();i++){
			inst = allInst.instance(i);
			key = inst.stringValue(inst.numAttributes()-1);
		}
		return null;
	}

	private Instances filtrarConBOW(Instances allInst) throws Exception {
		StringToWordVector stwvFilter = new StringToWordVector();
		stwvFilter.setIDFTransform(false);
		stwvFilter.setTFTransform(false);
		stwvFilter.setAttributeIndices("1");
		stwvFilter.setAttributeNamePrefix("count-");
		stwvFilter.setDoNotOperateOnPerClassBasis(false);
		stwvFilter.setInvertSelection(false);
		stwvFilter.setLowerCaseTokens(true);
		stwvFilter.setMinTermFreq(1);
		stwvFilter.setNormalizeDocLength(new SelectedTag(StringToWordVector.FILTER_NONE,StringToWordVector.TAGS_FILTER));
		stwvFilter.setOutputWordCounts(true);
		stwvFilter.setPeriodicPruning(-1.0);
		stwvFilter.setStemmer(null);
		stwvFilter.setUseStoplist(false);
		stwvFilter.setWordsToKeep(2000);
		return Filter.useFilter(allInst, stwvFilter);
	}

	private Instances getAllInstances(Instances[] instList) {
		Instances allInst = new Instances(instList[0]);
		for(int i = 1; i<instList.length;i++){
			for(int j = 0; j<instList[i].numInstances();i++){
				allInst.add(instList[i].instance(j));
			}
		}
		return allInst;
	}

	private String getNomLabels(File[] arffs){
		String str = "";
		for(File arff : arffs){
			str += arff.getName()+",";
		}
		return str.substring(0, str.length()-1);
	}
}
