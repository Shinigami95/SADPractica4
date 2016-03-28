package arff2BOW;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 
 * @author Mikel Alb&oacute;niga, Unai Garc&iacute;a y Jorge P&eacute;rez.
 */
public class ArffToBowWithIdfTf{
	
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
				File[] filArray = new File[args.length-1];
				for(int i = 0;i<args.length-1;i++){
					filArray[i] = new File(args[i]);
					if(!filArray[i].exists()) throw new FileNotFoundException("alguno de los ficheros no es correcto");
				}
				GestorBagOfWords.getGestor().generateBOW(filObj,filArray,true,true);
			} catch(FileNotFoundException e){
				System.out.println("ERROR, "+e.getMessage());
			}
		}
	}
	
	/**
	 * Imprime las instrucciones. 
	 */
	private static void printInst(){
		String s= "----------------------------------- INSTRUCCIONES -----------------------------------\n"
				+ "java -jar fssTFIDF.jar {fich.arff} objetive_dir\n"
				+ "Primero se indican todos los ficheros arff (train, dev, blind).\n"
				+ "El \u00FAltimo es el directorio donde se guardar\u00E1n los arff resultantes.\n"
				+ "Si el directorio objetivo no existe se crear\u00E1.\n"
				+ "-------------------------------------------------------------------------------------";
		System.out.println(s);
	}
}