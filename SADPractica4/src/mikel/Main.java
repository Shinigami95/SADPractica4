package mikel;

import java.io.File;
import java.io.FileWriter;

import weka.core.Instances;

public class Main {

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Error en los parametros.");
		}
		//String pathtxt="C:/Users/shind/Desktop/movies_reviews/train";
		//String patharff="C:/Users/shind/Desktop/movies_reviews/train/lista.arff";
		String pathtxt="C:/Users/Txema/Desktop/movies_reviews/train";
		String patharff="C:/Users/Txema/Desktop/suerte.arff";
		TextDirectoryToArff tdta = new TextDirectoryToArff();
		try {
			Instances dataset = tdta.createDataset(pathtxt);
			File arff=new File(patharff);
			FileWriter fw=new FileWriter(arff);
			fw.write(dataset.toString());
			fw.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

	}

}
