package com.fast.van.utils.file;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONObject;
import android.content.Context;
import android.content.res.AssetManager;

public class AppFileUtils {

	private Context _context;
	
	public AppFileUtils(Context cxt) {
		_context = cxt;
	}
	
	public String GetJSONString (String filename) {
		return jsonToStringFromAssetFolder(filename);
	}
	
	public JSONObject GetJSONObject (String filename) {
		JSONObject jsonObj = null;
		try	{
			//get json string
			String jsonStr = jsonToStringFromAssetFolder(filename);
			//convert to json object
			jsonObj = new JSONObject(jsonStr); 
			
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		return jsonObj;
	}
	
	private String jsonToStringFromAssetFolder(String fileName)  {
        try {
			AssetManager manager = _context.getAssets();
			InputStream file = manager.open(fileName);

			byte[] data = new byte[file.available()];
			file.read(data);
			file.close();
			return new String(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
    }
	
	public static final String saveDataFile(String fileName, String jsonData, Context context)  {
		BufferedOutputStream bos = null;
        try {
        	FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        	bos = new BufferedOutputStream(fos);
        	bos.write(jsonData.getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
        	try {
        		if (bos != null) bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return null;
    }
	
	public static final String readDataFile(String fileName, Context context)  {
		BufferedReader bufferedReader = null;
		try {
			FileInputStream fin = context.openFileInput(fileName);
			InputStreamReader inputStreamReader = new InputStreamReader(fin);
            bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            if (sb != null && sb.toString().length() > 0) { 
            	return sb.toString(); 
            }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return null;
    }
	
	public static final Boolean deleteDataFile(String filename, Context context) {
		boolean fileDeleted = false;
		boolean fileExists = false;
		for (int i = 0; i < context.fileList().length; i++) {
			fileExists = context.fileList()[i].equals(filename);
			if (fileExists) {
				break;
			}
		}
		if (fileExists) {
			fileDeleted = context.deleteFile(filename);
		}
		return fileDeleted;
	}
}
