package jorge.modelGenerator;

public class ModelGeneratorWithSchema {

	public static void main(String[] args){
		try{
			if(args.length==0){ // Flag que permite mostrar las instrucciones
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
					case "-b": // Path del fichero donde se crea el binario del modelo
						mc.setModelPath(args[i+1]);
						i+=2;
						break;
					case "-n": // Flag para indicar que se utilizara naive bayes
						mc.setUseNaive(true);
						i+=1;
						break;
					case "-e": // 1) Not Honest, 2) 10-Fold CrossVal, 3) Hold-Out
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
	
	public static void printInst(){
		//TODO write instructions
		System.out.println("INST Schema");
	}
}