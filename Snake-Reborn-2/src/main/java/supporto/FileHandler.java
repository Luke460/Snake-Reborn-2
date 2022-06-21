package supporto;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class FileHandler {

    public static String readFile(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        String data = IOUtils.toString(fis, "UTF-8");
        return data;
    }

    public static void writeFile(String filePath, String content) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(filePath, false);
        byte[] strToBytes = content.getBytes();
        outputStream.write(strToBytes);
        outputStream.close();
    }

    public static void addToFile(String filePath, String content) throws IOException {
        FileOutputStream fos = new FileOutputStream(filePath, true);
        byte[] strToBytes = content.getBytes();
        fos.write(strToBytes);
        fos.close();
    }

    public static ArrayList<String> getFileList(String folderPath, String fileExtension) throws FileNotFoundException {
    	File f = new File(folderPath);
    	if(!f.exists()) {
    		throw new FileNotFoundException("folder not found: " + folderPath);
    	}
    	FilenameFilter filter = new FilenameFilter() {
	        @Override
	        public boolean accept(File f, String name) {
	            return name.endsWith(fileExtension);
	        }
	    };
		ArrayList<String> fileList = new ArrayList<String>(Arrays.asList(f.list(filter)));
		return fileList;
    }
    
    public static ArrayList<String> getFolderList(String folderPath) throws FileNotFoundException {
    	File f = new File(folderPath);
    	if(!f.exists()) {
    		throw new FileNotFoundException("folder not found: " + folderPath);
    	}
    	FilenameFilter filter = new FilenameFilter() {
	        @Override
	        public boolean accept(File f, String name) {
	            return !name.contains(".");
	        }
	    };
		ArrayList<String> fileList = new ArrayList<String>(Arrays.asList(f.list(filter)));
		return fileList;
    }
    
}
