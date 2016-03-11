package jorge;

import java.io.FileWriter;

import weka.core.Instances;

public class ArffFromDirs {
	public static void main(String[] args){
		String[] args2 = {"C:/Users/Txema/Desktop/movies_reviews/train","C:/Users/Txema/Desktop/suerte.arff"};
		if(args2.length!=2){
			System.out.println("ERROR, par\u00E1metros inv\u00E1lidos");
			return;
		}
		try {
			System.out.println("Se crear\u00E1 el fichero ARFF");
			String dir = args2[0];
			String arff = args2[1];
			TextDirectoryToArff text = new TextDirectoryToArff();
			Instances inst = text.createDataset(dir);
			FileWriter fw = new FileWriter(arff);
			String arffText = inst.toString();
			fw.write(arffText);
			fw.close();
			System.out.println("Fichero ARFF creado");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
