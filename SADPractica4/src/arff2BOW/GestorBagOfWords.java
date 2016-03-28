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

/**
 * 
 * @author Mikel Alb&oacute;niga, Unai Garc&iacute;a y Jorge P&eacute;rez.
 */
public class GestorBagOfWords {

	private static GestorBagOfWords mGest;
	
	private GestorBagOfWords(){}
	
	/**
	 * Devuelve una unica &uacute;nica instancia de la clase.
	 * @return mGest
	 */
	public static GestorBagOfWords getGestor() {
		if(mGest==null){
			mGest = new GestorBagOfWords();
		}
		return mGest;
	}
	
	/**
	 * Fusiona todos los arff que se le pasan como par&aacute;metro y los filtra con StringToWordVector (BOW). 
	 * Por &uacute;ltimo vuelve a separalos y los crea en el path especificado en los par&aacute;metros.
	 * @param objetiveFil - Fichero en el que se guardar&aacute;n los .arff obtenidos.
	 * @param arffs - Array que se compone de los ficheros arff a los que se quiere aplicar el StringToWordVector (BOW).
	 * @param idft - Booleano que activa el par&aacute;metro IDFTransform en el filtro StringToWordVector (BOW).
	 * @param tft - Booleano que activa el par&aacute;metro TFTransform en el filtro StringToWordVector (BOW).
	 */
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
	
	/**
	 * Crea arffs con las instancias del array instList, les da el nombre del arff del que se sacaron  
	 * y los guarda en la carpeta especificada en los par&aacute;metros.
	 * @param instList - Array con la lista de instancias de un arff.
	 * @param fileNames - Array de ficheros arff.
	 * @param objetiveFil - Carpeta en la que se guardar&aacute;n todos los arff generados.
	 * @throws IOException
	 */
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
	
	/**
	 * Prepara el filtro StringToWordVector (BOW) para poder utilizarlo. 
	 * Para ello se le pasan al m&eacute;todo los parametros que indican algunas de sus especificaciones.  
	 * @param trainInst - Instancias que dan formato al filtro StringToWordVector (BOW).
	 * @param idft - Booleano que activa o desactiva el par&aacute;metro IDFTransform en el filtro StringToWordVector (BOW).
	 * @param tft - Booleano que activa o desactiva el par&aacute;metro TFTransform en el filtro StringToWordVector (BOW).
	 * @return stwvFilter - Filtro StringToWordVector (BOW) con las preparaciones necesaris para utilizarlo.
	 * @throws Exception
	 */
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
	
	/**
	 * Se fusionan todas las instancias que son pasadas como par&aacutemetro. 
	 * @param instList - Array de instancias a fusionar.
	 * @return allInst - Fusi&oacute;n de todas las instancias. 
	 */
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
