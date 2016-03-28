package arffGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * 
 * @author Mikel Alb&oacute;niga, Unai Garc&iacute;a y Jorge P&eacute;rez.
 */
public class TextCsvToArff implements TextPlainToArff{
	
	@Override
	public Instances createDatasetSupervised(String filePath) throws Exception {
		return createDataset(filePath);
	}

	@Override
	public Instances createDatasetUnsupervised(String filePath) throws Exception {
		return createDataset(filePath);
	}
	
	/**
	 * Extrae las instancias del archivo csv que se le pasa como par&aacute;metro.
	 * @param filePath - String que indica el path donde se encuentra el csv que contiene las instancias.
	 * @return data - Instancias que se encontr&aacute;ban en el csv indicado por el par&aacute;metro filePath.
	 * @throws Exception
	 */
	private Instances createDataset(String filePath) throws Exception {
		File file = new File(filePath);
		FileReader read = new FileReader(file);
		String aux;
		String[] tokens;
		BufferedReader buff = new BufferedReader(read);
		String line;
		line = buff.readLine();
		aux = "";
		for(int i=0; i<line.length(); i++){
			if(!isFakeChar(line.charAt(i))) aux += line.charAt(i);
		}
		tokens = aux.split(",");
		int numAtrib = tokens.length;
		FastVector atts = new FastVector(tokens.length);
		for(int i = 0; i<tokens.length;i++){
			atts.addElement(new Attribute(tokens[i], (FastVector) null));
		}
		
		Instances data = new Instances("text_files_in_" + filePath, atts, 0);
		double[] newInst;
		
		for(String l = buff.readLine();l!=null;l=buff.readLine()){
			aux = "";
			for(int i=0; i<l.length(); i++){
				if(!isFakeChar(l.charAt(i))) aux += l.charAt(i);
			}
			tokens = aux.split(",");
			newInst = new double[numAtrib];
			for(int i = 0; i<tokens.length && i<numAtrib; i++){
				if(i==numAtrib-1){
					line = "";
					for(;i<tokens.length;i++){
						line+=tokens[i];
					}
					newInst[numAtrib-1] = (double)data.attribute(numAtrib-1).addStringValue(line);
				} else {
					newInst[i] = (double)data.attribute(i).addStringValue(tokens[i]);
				}
			}
			data.add(new Instance(1.0, newInst));
		}
		
		buff.close();
		read.close();
		return data;
	}
	
	/**
	 * Clasifica el par&aacute;metro que se le pasa como car&aacute;cter problem&aacute;tico o car&aacute;cter normal.
	 * @param c - Car&aacute;cter a clasificar.
	 * @return Boolean
	 */
	private boolean isFakeChar(char c){
		if((c>='A' && c<='Z') || (c>='a' && c<='z') || (c>='0' && c<='9') || c==','|| c==' '){
			return false;
		}
		return true;
	}

}
