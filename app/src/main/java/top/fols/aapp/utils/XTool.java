package top.fols.aapp.utils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import top.fols.box.io.XStream;
import top.fols.box.io.base.ByteArrayOutputStreamUtils;
import top.fols.box.io.os.XRandomAccessFileInputStream;
import top.fols.box.io.os.XRandomAccessFileOutputStream;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.HashMapUtils9;
import top.fols.box.util.XArraysUtils;
import android.content.Intent;
import android.net.Uri;
import android.app.Activity;
import android.widget.Toast;
import top.fols.box.util.XExceptionTool;
public class XTool {
	public static boolean toBoolean(String str) {
		try {return Boolean.parseBoolean(str.trim());} catch (Throwable e) {return false;}
	}
	public static long toLong(String str) {
		try {return Long.parseLong(str.trim());} catch (Throwable e) {return 0;}
	}
	public static Object[] objArr(Object...str) {
		if (str == null)
			return XStaticFixedValue.nullObjectArray;
		return str;
	}
	public static String[] strArr(String...str) {
		if (str == null)
			return XStaticFixedValue.nullStringArray;
		return str;
	}

	public static class config {
		private static final Object Sync = new Object();
		public static Map<String,Object> wrapMap(String[] str, Object[] str2) {
			Map<String,Object> m = new HashMapUtils9<>();
			if (str == null)
				return m;
			for (int i = 0;i < str.length;i++)
				m.put(str[i], i >= str2.length ?null: str2[i] == null ?null: str2[i].toString());
			return m;
		}
		public static void clearConfig(File file) {
			synchronized (Sync) {
				try {
					file.delete();
				} catch (Exception e) {
					return;//throw new RuntimeException(e);
				}
			}
		}
		public static boolean putConfigString(Map<String,Object> m, File file) {
			synchronized (Sync) {
				if (m == null)
					return false;
				try {
					Map<String,Object> origin = new HashMapUtils9<>();
					origin.putAll(m);

					Properties PropertiesConfig = new Properties();
					if (file.exists()) {
						InputStream i = new XRandomAccessFileInputStream(file);
						PropertiesConfig.load(i);
						i.close();
					}
					XRandomAccessFileOutputStream out = new XRandomAccessFileOutputStream(file);
					out.setLength(0);
					Set<String> keys = m.keySet();
					for (String Key:keys) {
						Object value = m.get(Key);
						if (value == null)
							continue;
						PropertiesConfig.put(Key, value);
					}
					PropertiesConfig.save(out, "#");
					out.flush();
					out.close();
					PropertiesConfig.clear();
					return true;
				} catch (Exception e) {
					return false;
				}
			}
		}
		public static boolean setConfigString(Map<String,Object> m, File file) {
			synchronized (Sync) {
				if (m == null)
					return false;
				try {
					Properties PropertiesConfig = new Properties();
					XRandomAccessFileOutputStream out = new XRandomAccessFileOutputStream(file);
					out.setLength(0);
					Set<String> keys = m.keySet();
					for (String Key:keys) {
						Object value = m.get(Key);
						if (value == null)
							continue;
						PropertiesConfig.put(Key, value instanceof String ?(String)value: value.toString());
					}
					PropertiesConfig.save(out, "#");
					out.flush();
					out.close();
					PropertiesConfig.clear();
					return true;
				} catch (Exception e) {
					return false;
				}
			}
		}
		public static Map<String,String> getConfig(File file) {
			synchronized (Sync) {
				Map<String,String> m = new HashMapUtils9<>();
				try {
					if (!file.exists() || !file.isFile())
						return m;
					Properties PropertiesConfig = new Properties();
					InputStream in = new XRandomAccessFileInputStream(file);
					PropertiesConfig.load(in);
					Set keys = PropertiesConfig.keySet();
					in.close();
					for (Object key:keys) {
						String k = key == null ?null: key.toString();
						if (k == null)
							continue;
						Object value = PropertiesConfig.get(key);
						if (value == null)
							continue;
						m.put(k, value.toString());
					}
					PropertiesConfig.clear();
					return m;
				} catch (Exception e) {
					return m;//throw new RuntimeException(e);
				}
			}
		}

	}
	public static String getDirLength(File Dir) {
		if (Dir == null || !Dir.exists())
			return "0";
		File[] fs = Dir.listFiles();
		BigDecimal length = BigDecimal.ZERO;
		if (fs != null)
			for (File f:fs)
				length = length.add(new BigDecimal(f.length()));
		return length.toString();

	}
	public static Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        float targetX, targetY;
		targetX = bm.getHeight();
        if (orientationDegree == 90)
			targetY = 0;
		else
			targetY = bm.getWidth();

		final float[] values = new float[9];
		m.getValues(values);

		float x1 = values[Matrix.MTRANS_X];
		float y1 = values[Matrix.MTRANS_Y];

		m.postTranslate(targetX - x1, targetY - y1);

		Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.ARGB_8888);

		Paint paint = new Paint();
		Canvas canvas = new Canvas(bm1);
		canvas.drawBitmap(bm, m, paint);
		return bm1;
	}
	public static void refreshDrawableState(ImageView ivLeftAvatar) {
		if (ivLeftAvatar == null)
			return;
		try {
			Bitmap leftBitmap;
			try {
				leftBitmap = ((BitmapDrawable)ivLeftAvatar.getDrawable()).getBitmap();
			} catch (Throwable e) {
				leftBitmap = null;
			}
			ivLeftAvatar.setImageDrawable(null);  
			if (leftBitmap != null && !leftBitmap.isRecycled()) {  
				leftBitmap.recycle();
				leftBitmap = null;
			} 
		} catch (Throwable e) {
			return;
		}
	}



	public static void delFolder(File folderPath) {
		delFolder(folderPath.getAbsolutePath());
	}
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); //删除完里面所有内容
			new File(folderPath).delete(); //删除空文件夹
		} catch (Exception e) {
			return; 
		}
	}
	//删除指定文件夹下所有文件
	//param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists())
			return flag;
		if (!file.isDirectory())
			return flag;
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator))
				temp = new File(path + tempList[i]);
			else
				temp = new File(path + File.separator + tempList[i]);
			if (temp.isFile())
				temp.delete();
			if (temp.isDirectory()) {
				delAllFile(path + File.separator + tempList[i]);//先删除文件夹里面的文件
				delFolder(path + File.separator + tempList[i]);//再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}







	private static final int[] LocationResponseCode = new int[]{301,302};
	private static HttpURLConnection location(Map<String,String> UA, HttpURLConnection oldHttpURLConnection, int... responseCode) throws IOException {
		HttpURLConnection HttpURLConnection = oldHttpURLConnection;
		if (responseCode == null || responseCode.length == 0)
			responseCode = LocationResponseCode;
		int code = HttpURLConnection.getResponseCode();
		if (XArraysUtils.indexOf(responseCode, code, 0, responseCode.length) > -1) {
			String Location = HttpURLConnection.getHeaderField("Location");
			if (Location == null)
				return HttpURLConnection;
			HttpURLConnection = (HttpURLConnection) new URL(Location).openConnection();
			HttpURLConnection.setConnectTimeout(60 * 1000);
			HttpURLConnection.setReadTimeout(60 * 1000);

			putRequestPropertyMap(UA, HttpURLConnection);
			HttpURLConnection.setInstanceFollowRedirects(false);

			code = HttpURLConnection.getResponseCode();
			if (XArraysUtils.indexOf(responseCode, code, 0, responseCode.length) > -1)
				return location(UA, HttpURLConnection);
		}
		return HttpURLConnection;
	}
	public static byte[] getImageNetWork(String url) throws IOException {
		HttpURLConnection HttpURLConnection = (HttpURLConnection) new URL(url).openConnection(); 
		HttpURLConnection.setConnectTimeout(60 * 1000);
		HttpURLConnection.setReadTimeout(60 * 1000);

		HttpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		HttpURLConnection.setRequestProperty("Accept", "text/html, application/xhtml+xml, */*");
		HttpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31");
		HttpURLConnection.setInstanceFollowRedirects(false);
		Map<String,String> ua = getRequestPropertyMap(HttpURLConnection);
		HttpURLConnection = location(ua, HttpURLConnection);

		ByteArrayOutputStreamUtils bytes = new ByteArrayOutputStreamUtils();
		XStream.copy(HttpURLConnection.getInputStream(), bytes);
		return bytes.toByteArray();
	}
	public static Bitmap loadImageFromNetwork(String imageUrl) throws IOException {  
		byte[] bytes = getImageNetWork(imageUrl);
		Bitmap drawable = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return drawable ;  
	}

	private static Map<String,String> getRequestPropertyMap(HttpURLConnection HttpURLConnection) {
		Map<String,String> map = new HashMapUtils9<>();
		Map<String,?> mapr = HttpURLConnection.getRequestProperties();
		for (String key:mapr.keySet())
			map.put(key, HttpURLConnection.getRequestProperty(key));
		return map;
	}
	private static void putRequestPropertyMap(Map<String,String> UA, HttpURLConnection HttpURLConnection) {
		for (String key:UA.keySet())
			if (key == null)
				HttpURLConnection.addRequestProperty(null, UA.get(null));
			else if (key.startsWith("Set-"))
				HttpURLConnection.addRequestProperty(key.substring(4, key.length()), UA.get(key));
			else
				HttpURLConnection.addRequestProperty(key, UA.get(key));
	}






	public static Drawable zoomDrawable(Bitmap oldbmp , int w, int h) {    
		if (oldbmp == null)
			return null;
		int width = oldbmp.getWidth();    
		int height = oldbmp.getHeight();    
		Matrix matrix = new Matrix();    
		float scaleWidth = ((float) w / width);    
		float scaleHeight = ((float) h / height);    
		matrix.postScale(scaleWidth, scaleHeight);    
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,    matrix, true);   
		Drawable b = new BitmapDrawable(null, newbmp);  
		newbmp = null;
		return b;
	}
	public static Drawable zoomDrawable(Drawable drawable, int w, int h) {    
		if (drawable == null)
			return null;
		int width = drawable.getIntrinsicWidth();    
		int height = drawable.getIntrinsicHeight();    
		Bitmap oldbmp = drawableToBitmap(drawable);    
		Matrix matrix = new Matrix();    
		float scaleWidth = ((float) w / width);    
		float scaleHeight = ((float) h / height);    
		matrix.postScale(scaleWidth, scaleHeight);    
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,    matrix, true);    
		Drawable b = new BitmapDrawable(null, newbmp);    
		newbmp = null;
		return b;    
	}    
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		if (bitmap == null)
			return null;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		bitmap = null;
		return newbmp;
	}




	public static Bitmap drawableToBitmap(Drawable drawable) {    
		if (drawable == null)
			return null;
		return ((BitmapDrawable)drawable).getBitmap();
	}    
	public static Bitmap drawableToBitmap2(Drawable drawable) {    
		if (drawable == null)
			return null;
		int width = drawable.getIntrinsicWidth();    
		int height = drawable.getIntrinsicHeight();    
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888: Bitmap.Config.RGB_565;    
		Bitmap bitmap = Bitmap.createBitmap(width, height, config);    
		Canvas canvas = new Canvas(bitmap);    
		drawable.setBounds(0, 0, width, height);    
		drawable.draw(canvas);    
		return bitmap;    
	}
	public static byte[] bitmap2Bytes(Bitmap bitmap) {
		if (bitmap == null)
			return null;
        ByteArrayOutputStreamUtils byteArrayOutputStream = new ByteArrayOutputStreamUtils();
        bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
		byteArrayOutputStream.releaseCache();
		return bytes;
    }
    public static Bitmap bytes2Bitmap(byte[] bArr) {
		if (bArr == null)
			return null;
        return BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
    }
	public static Bitmap file2Bitmap(String f) {
		try {
			FileInputStream in = new FileInputStream(f);
			BitmapFactory.Options options = new BitmapFactory.Options();

			Bitmap bitmap = BitmapFactory.decodeStream(in, null, null);
			in.close();
			return bitmap;
		} catch (IOException e) {
			return null;
		}
	}
	public static Bitmap file2Bitmap(File f) {
		try {
			FileInputStream in = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(in);
			in.close();
			return bitmap;
		} catch (IOException e) {
			return null;
		}
	}

	public static Bitmap cloneBitmap(Bitmap bitmap) {
		if (bitmap == null)
			return null;
		Bitmap.Config config = bitmap.getConfig();
		Bitmap newbmp = bitmap.copy(config, false);
		bitmap = null;
		return newbmp;
	}



	public static void save(File file, Map<String,String> map) {
		try {
			XRandomAccessFileOutputStream input = new XRandomAccessFileOutputStream(file);
			Properties properties = new Properties();
			for (String key:map.keySet())
				properties.setProperty(key, map.get(key));
			properties.save(input, "");
			properties.clear();
			input.close();
		} catch (Exception e) {
			return;
		}
	}
	public static Map<String,String> load(File file) {
		HashMapUtils9<String,String> map = new HashMapUtils9<>();
		try {
			XRandomAccessFileInputStream input = new XRandomAccessFileInputStream(file);
			Properties properties = new Properties();
			properties.load(input);
			for (Object key:properties.keySet()) {
				String stringKey = key == null ?null: key instanceof String ?(String)key: key.toString();
				map.put(stringKey, properties.getProperty(stringKey, null));
			}
			input.close();
		} catch (Exception e) {
			;
		}
		return map;
	}






	public static void close(URLConnection con) {
		try {
			con.getInputStream().close();
		} catch (Exception e) {
			;
		}
		try {
			con.getInputStream().close();
		} catch (Exception e) {
			;
		}
	}



	public static void openLink(Activity ac, String link) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
		try {
			ac.startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(ac.getApplicationContext(), XExceptionTool.StackTraceToString(e), Toast.LENGTH_SHORT).show();
		}
	}
}

