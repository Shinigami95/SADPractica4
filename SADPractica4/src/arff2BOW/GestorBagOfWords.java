package arff2BOW;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.filters.Filter;
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
			for(int i = 0;i<instList.length;i++){
				rd = new FileReader(arffs[i]);
				instList[i] = new Instances(rd);
			}
			Instances allInst = this.getAllInstances(instList);
			StringToWordVector stwv = this.filtrarConBOW(allInst,idft, tft);
			for(int i = 0;i<instList.length;i++){
				instList[i] = Filter.useFilter(instList[i], stwv);
			}
			crearFicheros(instList, arffs, objetiveFil);
		} catch(FileNotFoundException e) {
			System.out.println("ERROR, fichero no encontrado generateBOW()");
			e.printStackTrace();
		} catch(IOException e) {
			System.out.println("ERROR desconocido");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void crearFicheros(Instances[] instList, File[] fileNames,File objetiveFil) throws IOException {
		System.out.println("Se crear\u00E1n los ficheros:");
		File fil;
		String filName;
		FileWriter fw;
		Instances inst;
		for(int i=0; i<instList.length; i++){
			inst = instList[i];
			filName = fileNames[i].getName();
			fil = new File(objetiveFil.getPath()+"\\BOW_"+filName);
			System.out.println(objetiveFil.getPath());
			System.out.println(fil.getAbsolutePath());
			System.out.println(filName);
			fil.createNewFile();
			fw  = new FileWriter(fil);
			fw.write(inst.toString());
			fw.close();
			System.out.println(filName+"creado");
		}
	}

	private StringToWordVector filtrarConBOW(Instances trainInst,boolean idft, boolean tft) throws Exception {
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
		stwvFilter.setInputFormat(trainInst);
		return stwvFilter;
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
}
