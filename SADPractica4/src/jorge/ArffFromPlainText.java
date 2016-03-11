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
					if(t==0 && flagU) getArffFrom(new TextDirectoryToArff(),file, arff, false);
					else if(t==0 && !flagU) getArffFrom(new TextDirectoryToArff(), file, arff, true);
					else if(t==1 && flagU) getArffFrom(new TextFileToArff(),file, arff, false);
					else if(t==1 && !flagU)getArffFrom(new TextFileToArff(),file, arff, true);
					else if(t==2 && flagU)getArffFrom(new TextCsvToArff(),file, arff, true);
					else if(t==2 && !flagU)getArffFrom(new TextCsvToArff(),file, arff, true);
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
	
	private static void getArffFrom(TextPlainToArff tpta, String dir, String arff, boolean superv){
		try {
			System.out.println("Se crear\u00E1 el fichero ARFF");
			Instances inst;
			if(superv){
				inst = tpta.createDatasetSupervised(dir);
			}
			else{
				inst = tpta.createDatasetUnsupervised(dir);
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
		String s= "----------------------------------- INSTRUCCIONES -----------------------------------\n"
				+ "java -jar getArff.jar -f plain_data -a arff_file -t {0,1,2} [-u]\n"
				+ "Las opciones son:\n"
				+ "\t-h\n"
				+ "\t\tMuestra las instrucciones del programa, ignora el resto de opciones.\n"
				+ "\t-f path\n"
				+ "\t\tSe elige en path el fichero o directorio de los datos en bruto.\n"
				+ "\t-a path\n"
				+ "\t\tSe elige en path el fichero donde se guardar\u00E1 el arff resultante.\n"
				+ "\t-u\n"
				+ "\t\tSirve para indicar que sea sin supervisar (por defecto supervisado)\n"
				+ "\t-t i\n"
				+ "\t\tSe indica en i el tipo de fichero en bruto:\n"
				+ "\t\t\t0 -> Directorio (pelis)\n"
				+ "\t\t\t1 -> Texto plano (sms)\n"
				+ "\t\t\t2 -> Csv (tweets)\n"
				+ "\t\t\totro -> Da error\n"
				+ "-------------------------------------------------------------------------------------";
		System.out.println(s);
	}
}
