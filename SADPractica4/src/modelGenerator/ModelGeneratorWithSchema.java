package modelGenerator;

/**
 * 
 * @author Mikel Alb&oacute;niga, Unai Garc&iacute;a y Jorge P&eacute;rez.
 */
public class ModelGeneratorWithSchema {

	public static void main(String[] args){
		try{
			if(args.length==0){
				printInst();
			} else {
				ModelConfig mc = new ModelConfig();
				int i = 0;
				while (i < args.length) {
					switch (args[i]) {
					case "-s": // Flag que permite mostrar informacion completa de la evaluacion
						mc.setFlagS(true);
						i+=1;
						break;
					case "-t": // Fichero train
						mc.setTrainPath(args[i+1]);
						i+=2;
						break;
					case "-d": // Fichero dev
						mc.setDevPath(args[i+1]);
						i+=2;
						break;
					case "-n": // Flag para indicar que se utilizara naive bayes
						mc.setUseNaive(true);
						i+=1;
						break;
					case "-m": // Path del fichero donde se crea el binario del modelo
						mc.setModelPath(args[i+1]);
						i+=2;
						break;
					case "-e": // 1) Not Honest, 2) Hold-Out, 3) 10-Fold CrossVal
						mc.setEvaluationSchema(Integer.parseInt(args[i+1]));
						i+=2;
						break;
					default:
						throw new Exception();
					}
				}
				GestorAjusteParam.getGestor().infiereConEsquema(mc);
			}
		} catch (Exception e){
			System.out.println("ERROR, par\u00E1metros incorrectos");
			printInst();
		}
	}
	
	/**
	 * Imprime las instrucciones. 
	 */
	public static void printInst(){
		String s= "---------------------------------------- INSTRUCCIONES ----------------------------------------\n"
				+ "java -jar GetModelConEsquema.jar -t train.arff -d dev.arff -e {1,2,3} [-s] [-n] [-m fich.model]\n"
				+ "Las opciones son:\n"
				+ "\t-t path\n"
				+ "\t\tSe elige en path el fichero train.\n"
				+ "\t-d path\n"
				+ "\t\tSe elige en path el fichero dev.\n"
				+ "\t-s\n"
				+ "\t\tMostrar\u00E1 informaci\u00F3n extra.\n"
				+ "\t-n\n"
				+ "\t\tPara utilizar NaiveBayes, sin \u00E9l BayesNet.\n"
				+ "\t-m path\n"
				+ "\t\tPara crear el binario del modelo se indica la ruta en path.\n"
				+ "\t-e i\n"
				+ "\t\tSe indica en i el esquema de evaluaci\u00F3n:\n"
				+ "\t\t\t1 -> No honesta.\n"
				+ "\t\t\t2 -> Hold Out.\n"
				+ "\t\t\t3 -> 10-Fold Crossvalidation.\n"
				+ "\t\t\totro -> Da error.\n"
				+ "-----------------------------------------------------------------------------------------------";
		System.out.println(s);
	}
}