package database.handler;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

public class ImageToDatabase {
	
	public static byte[] imgToByteArray(String fileLoc){
		return getBytes(loadFile(fileLoc));
	}
	
	//load image
	public static Bitmap loadFile(String fileLoc) {
		return BitmapFactory.decodeFile(Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/Jujitsu/Gokyu/Cross 1.jpg");
	}

	// convert from bitmap to byte array
	public static byte[] getBytes(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 0, stream);
		return stream.toByteArray();
	}

	// convert from byte array to bitmap
	public static Bitmap getImage(byte[] image) {
		return BitmapFactory.decodeByteArray(image, 0, image.length);
	}

}
