package jorge.arff2BOW;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
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
	
	public void generateBOW(File objetiveFil, File[] arffs, boolean idft, boolean tft){
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
			allInst = this.filtrarConBOW(allInst,idft, tft);
			instList = this.separarInst(allInst);
			crearFicheros(instList, objetiveFil);
		} catch(FileNotFoundException e) {
			System.out.println("ERROR, fichero no encontrado generateBOW()");
			e.printStackTrace();
		} catch(IOException e) {
			System.out.println("ERROR desconocido");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void crearFicheros(Instances[] instList, File objetiveFil) throws IOException {
		System.out.println("Se crear\u00E1n los ficheros:");
		File fil;
		String filName;
		FileWriter fw;
		int i = 0;
		for(Instances inst : instList){
			filName = inst.relationName();
			fil = new File(objetiveFil.getPath()+"\\BOW_"+filName);
			System.out.println(objetiveFil.getPath());
			System.out.println(fil.getAbsolutePath());
			System.out.println(filName);
			inst.deleteAttributeAt(inst.attribute("File-Name").index());
			fil.createNewFile();
			fw  = new FileWriter(fil);
			fw.write(inst.toString());
			fw.close();
			System.out.println(filName+"creado");
			i++;
		}
	}

	private Instances[] separarInst(Instances allInst) {
		System.out.println("Separando instancias:");
		HashMap<String, Instances> hm = new HashMap<String, Instances>();
		FastVector attInfo = new FastVector();
		for(int i=0; i<allInst.numAttributes(); i++){
			attInfo.addElement(allInst.attribute(i));
		}
		Instances insts;
		Instance inst;
		String key;
		for(int i = 0; i<allInst.numInstances();i++){
			inst = allInst.instance(i);
			key = inst.stringValue(allInst.attribute("File-Name").index());
			if(!hm.containsKey(key)){
				insts = new Instances(key, attInfo, 0);
				hm.put(key, insts);
			}
			hm.get(key).add(inst);
		}
		Set<String> keys = hm.keySet();
		Instances[] instList = new Instances[keys.size()];
		int index=0;
		for (String s : keys){
			instList[index] = hm.get(s);
			index++;
		}
		return instList;
	}

	private Instances filtrarConBOW(Instances allInst,boolean idft, boolean tft) throws Exception {
		System.out.println("Filtro StringToWordVector:");
		StringToWordVector stwvFilter = new StringToWordVector();
		stwvFilter.setIDFTransform(idft);
		stwvFilter.setTFTransform(tft);
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
		stwvFilter.setInputFormat(allInst);
		return Filter.useFilter(allInst, stwvFilter);
	}

	private Instances getAllInstances(Instances[] instList) {
		System.out.println("Juntando instancias:");
		Instances allInst = new Instances(instList[0]);
		for(int i = 1; i<instList.length;i++){
			for(int j = 0; j<instList[i].numInstances();j++){
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
