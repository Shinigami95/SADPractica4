package jorge;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class TextFileToArff implements TextPlainToArff{
	
	@Override
	public Instances createDatasetSupervised(String filePath) throws Exception {
		 
		FileReader fileRead = new FileReader(filePath);
		HashMap<String, LinkedList<String>> instOfFile = new HashMap<String, LinkedList<String>>();
		LinkedList<String> list;
		BufferedReader buff = new BufferedReader(fileRead);
		String[] tokens;
		String classValue;
		String sms, smsAux;
		String key;
		
		for(String l = buff.readLine();l!=null;l=buff.readLine()){
			tokens = l.split("\t");
			classValue = tokens[0];
			sms = tokens[1];
			if(instOfFile.containsKey(classValue)){
				instOfFile.get(classValue).add(sms);
			} else {
				list = new LinkedList<String>();
				list.add(sms);
				instOfFile.put(classValue, list);
			}
		}
		buff.close();
		fileRead.close();
		
		Set<String> keySet = instOfFile.keySet();
		Iterator<String> itr;
		Iterator<String> itr2;
		
		FastVector atts = new FastVector(2);
		FastVector classValues = new FastVector(keySet.size());
		itr = keySet.iterator();
		while(itr.hasNext()){
			classValues.addElement(itr.next());
		}
		
		atts.addElement(new Attribute("contents", (FastVector) null));
		atts.addElement(new Attribute("class", classValues));
		Instances data = new Instances("text_files_in_" + filePath, atts, 0);
		
		itr = keySet.iterator();
		while(itr.hasNext()){
			key = itr.next();
			list = instOfFile.get(key);
			itr2 = list.iterator();
			while(itr2.hasNext()){
				smsAux = itr2.next();
				sms = "";
				for(int i = 0; i<smsAux.length(); i++){
					if(!isFakeChar(smsAux.charAt(i))) sms += smsAux.charAt(i);
				}
				double[] newInst = new double[2];
				newInst[0] = (double)data.attribute(0).addStringValue(sms);
				newInst[1] = (double)data.attribute(1).indexOfValue(key);
				data.add(new Instance(1.0, newInst));
			}
		}
		return data;
	}

	@Override
	public Instances createDatasetUnsupervised(String filePath) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private boolean isFakeChar(char c){
		if(c=='@'||c==','||c=='%'||c=='#'||c=='/'||c=='\''||c=='\"'){
			return true;
		}
		return false;
	}
}
