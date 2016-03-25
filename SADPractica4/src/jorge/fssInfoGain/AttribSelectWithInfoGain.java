package jorge.fssInfoGain;

import java.io.File;
import java.io.FileNotFoundException;

public class AttribSelectWithInfoGain {
	public static void main(String[] args){
		if(args.length<2){
			printInst();
		} else {
			try {
				File filObj = new File(args[args.length-1]);
				if(filObj.exists()){
					if(!filObj.isDirectory()){
						throw new FileNotFoundException("el objetivo no es un directorio");
					}
				} else {
					filObj.mkdir();
				}
				File[] filArray = new File[args.length-2];
				File filTrain = new File(args[0]);
				for(int i = 0;i<args.length-2;i++){
					filArray[i] = new File(args[i+1]);
					if(!filArray[i].exists()) throw new FileNotFoundException("alguno de los ficheros no es correcto");
				}
				GestorInfoGain.getGestor().generateInfoGainedAttributes(filTrain,filArray,filObj);
			} catch(FileNotFoundException e){
				System.out.println("ERROR, "+e.getMessage());
				printInst();
			}
		}
	}
	
	private static void printInst(){
		String s= "------------------------------- INSTRUCCIONES -------------------------------\n"
				+ "java -jar fssInfoGain.jar train.arff {fich.arff} objetive_dir\n"
				+ "Primero se indica el fichero train.\n"
				+ "A continuaci\u00F3n todos los ficheros arff (dev, blind).\n"
				+ "El \u00FAltimo es el directorio donde se guardar\u00E1n los arff resultantes.\n"
				+ "Si el directorio objetivo no existe se crear\u00E1.\n"
				+ "-----------------------------------------------------------------------------";
		System.out.println(s);
	}
}
