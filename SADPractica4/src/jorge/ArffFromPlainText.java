package jorge;

import java.io.FileWriter;

import weka.core.Instances;

public class ArffFromPlainText {
	public static void main(String[] args){
		try{
			if(args.length>0){
				boolean flagH = false;
				boolean flagU = false;
				String file = null;
				String arff = null;
				int t = -1;
				int i = 0;
				while (i < args.length&&flagH==false) {
					switch (args[i]) {
					case "-h": // Flag que permite mostrar las instrucciones
						flagH=true;
						break;
					case "-u": // Flag que indica que sera sin supervisar
						flagU=true;
						i+=1;
						break;
					case "-f": // Flag para indicar el fichero o directorio (train, def, blind)
						file = args[i+1];
						i+=2;
						break;
					case "-a": // Flag para indicar el fichero arff saliente
						arff = args[i+1];
						i+=2;
						break;
					case "-t": // Flag para indicar el metodo (0=dir, 1=txt, 2=csv)
						t = Integer.parseInt(args[i+1]);
						i+=2;
						break;
					default:
						throw new Exception();
					}
				}
				if(flagH==true){
					printInst();
				}
				else{
					if(t<0||t>2||file==null||arff==null) throw new Exception();
					if(t==0 && flagU) getArffFromDirectorySistem(file, arff, false);
					else if(t==0 && !flagU) getArffFromDirectorySistem(file, arff, true);
					else if(t==1 && flagU);
					else if(t==1 && !flagU);
					else if(t==2 && flagU);
					else if(t==2 && !flagU);
				}
				
			} else {
				System.out.println("ERROR, faltan par\u00E1metros");
				printInst();
			}
		} catch (Exception e){
			System.out.println("ERROR, par\u00E1metros incorrectos");
			printInst();
		}
	}
	
	private static void getArffFromDirectorySistem(String dir, String arff, boolean superv){
		try {
			System.out.println("Se crear\u00E1 el fichero ARFF");
			TextDirectoryToArff text = new TextDirectoryToArff();
			Instances inst;
			if(superv){
				inst = text.createDatasetSupervised(dir);
			}
			else{
				inst = text.createDatasetUnsupervised(dir);
			}
			FileWriter fw = new FileWriter(arff);
			String arffText = inst.toString();
			fw.write(arffText);
			fw.close();
			System.out.println("Fichero ARFF creado");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void printInst(){
		System.out.println("Inst");
	}
}
