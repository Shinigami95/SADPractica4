package principal;

import java.io.File;
import java.io.FileWriter;

import weka.core.Instances;

public class Main {

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Error en los parametros.");
		}
		String pathtxt=args[0];
		String patharff=args[1];
		TextDirectoryToArff tdta = new TextDirectoryToArff();
		try {
			Instances dataset = tdta.createDataset(pathtxt);
			File arff=new File(patharff);
			FileWriter fw=new FileWriter(arff);
			fw.write(dataset.toString());
			fw.close();
			System.out.println(dataset);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

	}

}
