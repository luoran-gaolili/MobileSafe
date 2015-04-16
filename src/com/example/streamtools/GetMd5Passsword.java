package com.example.streamtools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GetMd5Passsword {
	public static String getPassword(String password) {

		MessageDigest md;
		try {
			md = MessageDigest.getInstance("md5");
			StringBuilder builder = new StringBuilder();
			byte[] bt = md.digest(password.getBytes());
			for (byte b : bt) {
				int result = b & 0xff;
				String str = Integer.toHexString(result);
				if (str.length() == 1) {
					builder.append("0");
				}
				builder.append(str);
			}

			return builder.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
}
