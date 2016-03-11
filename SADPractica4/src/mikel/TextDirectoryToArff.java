package mikel;

/*
 *    TextDirectoryToArff.java
 *    Copyright (C) 2002 Richard Kirkby
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
 
import java.io.*;
import weka.core.*;
 
/**
 * Builds an arff dataset from the documents in a given directory.
 * Assumes that the file names for the documents end with ".txt".
 *
 * Usage:<p/>
 *
 * TextDirectoryToArff <directory path> <p/>
 *
 * @author Richard Kirkby (rkirkby at cs.waikato.ac.nz)
 * @version 1.0
 */
public class TextDirectoryToArff {
 
  public Instances createDataset(String directoryPath) throws Exception {
 
    FastVector atts = new FastVector(2);
    atts.addElement(new Attribute("class", (FastVector) null));
    atts.addElement(new Attribute("contents", (FastVector) null));
    Instances data = new Instances("text_files_in_" + directoryPath, atts, 0);
 
    File dir = new File(directoryPath);
    String[] files = dir.list();
    File dir2;
    String[] files2;
    for (int i = 0; i < files.length; i++) {
    	dir2=new File(directoryPath+ File.separator +files[i]);
    	files2=dir2.list();
    	for(int j = 0; j < files2.length; j++){
      if (files2[j].endsWith(".txt")) {
    try {
      double[] newInst = new double[3];
      newInst[0] = (double)data.attribute(0).addStringValue(files[i]);
      newInst[1] = (double)data.attribute(1).addStringValue(files2[j]);
      File txt = new File(directoryPath + File.separator+files[i]+ File.separator + files2[j]);
      InputStreamReader is;
      is = new InputStreamReader(new FileInputStream(txt));
      StringBuffer txtStr = new StringBuffer();
      int c;
      while ((c = is.read()) != -1) {
        txtStr.append((char)c);
      }
      newInst[3] = (double)data.attribute(3).addStringValue(txtStr.toString());
      data.add(new Instance(1.0, newInst));
    } catch (Exception e) {
      e.printStackTrace();
    }
      }}
    }
    return data;
  }
  
}