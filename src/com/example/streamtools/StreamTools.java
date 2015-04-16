package com.example.streamtools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamTools {
	public static String getString(InputStream is) throws IOException
	{
		ByteArrayOutputStream bao=new ByteArrayOutputStream();
		int len=0;
		byte[] bt=new byte[1024];
		while((len=is.read(bt))!=-1){
			bao.write(bt,0,len);
		}
		is.close();
		String str=bao.toString();
		bao.close();
		return str;
		
	}

}
