package jorge.arff2BOW;

import java.io.File;
import java.io.FileNotFoundException;

public class ArffToBowWithIdfTf{
	
	public static void main(String[] args){
	if(args.length<2){
		System.out.println("ERROR, par\u00E1metros incorrectos");
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
}