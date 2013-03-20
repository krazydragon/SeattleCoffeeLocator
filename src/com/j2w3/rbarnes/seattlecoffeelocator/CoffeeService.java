/*
 * project	SeattleCoffeeLocator
 * 
 * package	com.j2w3.rbarnes.seattlecoffeelocator
 * 
 * @author	Ronaldo Barnes
 * 
 * date		Mar 20, 2013
 */
package com.j2w3.rbarnes.seattlecoffeelocator;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;



import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class CoffeeService extends Service{

	Context _context;
	static Boolean _connected = false;
	static String _connectionType = "Unavailable";
	Toast _toast;
	
	
	
	 @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
		 _context = this;
		 Boolean connected = getConnectionStatus(_context);
		 Toast.makeText(this,"This service works :)", Toast.LENGTH_LONG).show();
		 if (connected){
				Log.i("SERVICE", "CONNECTED");
			}
		 getLocations("Coffee", "98101");
		 this.stopSelf();
		 
	    return Service.START_NOT_STICKY;
	  }

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("service", "Service started");
		this.stopSelf();
	}
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.d("service", "Service destroyed");
    }
  
  	public static String getConnectionType(Context context){
  		netInfo(context);
  		return _connectionType;
  	}
  	
  	public static Boolean getConnectionStatus(Context context) {
  		netInfo(context);
  		return _connected;
  	}
  	
  	private static void netInfo(Context context){
  		ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
  		NetworkInfo nInfo = cManager.getActiveNetworkInfo();
  		if(nInfo != null){
  			
  			if(nInfo.isConnected()){
  				_connectionType = nInfo.getTypeName();
  				_connected = true;
  				
  			}else{
  				_connected = false;
  			}
  			
  		}
  	}
  	
  	
  	public static String getUrlStringResponse(URL url){
  		String response = "";
  		
  		try{
  			URLConnection connection = url.openConnection();
  			BufferedInputStream bin = new BufferedInputStream(connection.getInputStream());
  			
  			byte[] contentBytes = new byte[1024];
  			int bytesRead = 0;
  			StringBuffer responseBuffer = new StringBuffer();
  			
  			while((bytesRead = bin.read(contentBytes)) != -1){
  				
  				response = new String(contentBytes,0,bytesRead);
  				
  				responseBuffer.append(response);
  				
  			}
  			return responseBuffer.toString();
  		}catch (Exception e){
  			Log.e("URL RESPONSE ERROR","getURLStringResponse");
  		}
  		
  		return response;
  	}
  	
  	private void getLocations(String dessert, String zipCode){
		String baseUrl = "http://local.yahooapis.com/LocalSearchService/V3/localSearch?appid=qJIjRlbV34GJZfg2AwqSWVV03eeg8SpTQKy5PZqSfjlRrItt5hS2n3PIysdPU_CCIQlCGXIGjoTDESp3l42Ueic3O1EaYXU-&query="+dessert+"&zip="+zipCode+"&results=10&output=xml";
		URL finalURL;
		try{
			finalURL = new URL(baseUrl);
			LocationRequest lr = new LocationRequest();
			lr.execute(finalURL);
			Log.i("URL ", baseUrl);
			
		}catch(MalformedURLException e){
			Log.e("BAD URL","MALFORMED URL");
			finalURL = null;
		}
	}
	
	//get results
	private class LocationRequest extends AsyncTask<URL, Void, Boolean>{
		@Override
		protected Boolean doInBackground(URL... params){
			boolean response = false;
			URL result = params[0];
			if(result != null){
				
				xmlParse(result);
			}
			return response;
		}
		
		
		private void xmlParse(URL result){
			
			XmlPullParser coffeeShops = null;
			try {
				coffeeShops = XmlPullParserFactory .newInstance().newPullParser();
			} catch (XmlPullParserException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try {
				coffeeShops.setInput(result.openStream(), null);
			} catch (XmlPullParserException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int eventType = 0;
			try {
				eventType = coffeeShops.getEventType();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			while (eventType != XmlPullParser.END_DOCUMENT) {
			    if (eventType == XmlPullParser.START_TAG) {
			        String tagName = coffeeShops.getName();
			        String tagText = coffeeShops.getText();
			        if (tagName.equals("ResultSet")) {
			        	
			            // inner loop looking for link and title
			            while (eventType != XmlPullParser.END_DOCUMENT) {
			                if (eventType == XmlPullParser.START_TAG) {
			                    if (tagName.equals("Result")) {
			                    	
			                    }
			                } else if (eventType == XmlPullParser.END_TAG) {
			                    if (tagName.equals("Categories")) {
			                        // save the data, and then continue with
			                        // the outer loop
			                        break;
			                    }
			                }
			                try {
								eventType = coffeeShops.next();
							} catch (XmlPullParserException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			            }
			        }else  if(eventType == XmlPullParser.TEXT){
                		Log.i("tag", tagText);
                	}
			    }
			    try {
					eventType = coffeeShops.next();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
	}
}
