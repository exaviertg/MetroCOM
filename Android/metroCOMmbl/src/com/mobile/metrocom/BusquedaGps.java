package com.mobile.metrocom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BusquedaGps extends Activity implements Runnable  {
	
    private ProgressDialog pd;
    private static final String nombreClase = "Activity - BusquedaGPS";
	LocationManager mLocationManager;
	Location mLocation;
	MyLocationListener mLocationListener;
	
	Location currentLocation = null;
	
	TextView outlat;
	TextView outlong;
	
	String str_lat_grados = "";
	String str_lat_minutos = "";
	String str_lat_segundos = "";
	
	String str_lon_grados = "";
	String str_lon_minutos = "";
	String str_lon_segundos = "";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        
        outlat = (TextView) findViewById(R.id.outlat);
        outlong = (TextView) findViewById(R.id.outlong);
        
		Button btsearch = (Button) findViewById(R.id.btsearch);
		btsearch.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                writeSignalGPS();
            }
          
        });
        
    }
    
    private void setCurrentLocation(Location loc) {
    	currentLocation = loc;
    }
    
    
    private void writeSignalGPS() {
    	
    	DialogInterface.OnCancelListener dialogCancel = new DialogInterface.OnCancelListener() {

            public void onCancel(DialogInterface dialog) {
                Toast.makeText(getBaseContext(), 
                        getResources().getString(R.string.gps_signal_not_found), 
                        Toast.LENGTH_LONG).show();
                handler.sendEmptyMessage(0);
            }
          
        };
    	
		pd = ProgressDialog.show(this, this.getResources().getString(R.string.search), 
				this.getResources().getString(R.string.search_signal_gps), true, true, dialogCancel);
		
		Thread thread = new Thread(this);
		thread.start();

    }

	public void run() {
    	
		mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			
			Looper.prepare();
			
			mLocationListener = new MyLocationListener();
			
			mLocationManager.requestLocationUpdates(
	                LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
			Looper.loop(); 
			Looper.myLooper().quit(); 
			
		} else {
			
            Toast.makeText(getBaseContext(), 
                    getResources().getString(R.string.gps_signal_not_found), 
                    Toast.LENGTH_LONG).show();
            
		}
	}
    
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			mLocationManager.removeUpdates(mLocationListener);
	    	if (currentLocation!=null) {
	    		
	    		
	    		Double lat_decimales = currentLocation.getLatitude();
	    		Math.abs(lat_decimales);
	    		Integer lat_grados = lat_decimales.intValue();	    		
	    		
	    		int posicion1 = lat_decimales.toString().indexOf(".") + 1;
	    		int posicion2 = lat_decimales.toString().length();
	    		String str_decimales = lat_decimales.toString().substring(posicion1, posicion2);
	    		str_decimales = "0." + str_decimales.trim();
	    		lat_decimales = Double.parseDouble(str_decimales);	    				
	    		
	    		Double lat_aux = (lat_decimales*60);	    		
	    		Integer lat_minutos = lat_aux.intValue();
	    		
	    		posicion1 = lat_aux.toString().indexOf(".") + 1;
	    		posicion2 = lat_aux.toString().length();
	    		str_decimales = lat_aux.toString().substring(posicion1, posicion2);
	    		str_decimales = "0." + str_decimales.trim();
	    		lat_decimales = Double.parseDouble(str_decimales);	    				
	    		
	    		lat_aux = (lat_decimales*60);	    		
	    		Integer lat_segundos = lat_aux.intValue();
	    		
	    		String latitud =  "\n GRADOS: " + lat_grados.toString() + "\n MINUTOS: " + lat_minutos.toString() + "\n SEGUNDOS: " + lat_segundos.toString();

	    		str_lat_grados = lat_grados.toString() ;
	    		str_lat_minutos = lat_minutos.toString();
	    		str_lat_segundos = lat_segundos.toString();
	    		
	    		//LONGITUD
	    		
	    		Double lon_decimales = currentLocation.getLongitude();

	    		Math.abs(lon_decimales);
	    		Integer lon_grados = lon_decimales.intValue();	    		
	    		
	    		 posicion1 = lon_decimales.toString().indexOf(".") + 1;
	    		 posicion2 = lon_decimales.toString().length();
	    		 str_decimales = lon_decimales.toString().substring(posicion1, posicion2);
	    		str_decimales = "0." + str_decimales.trim();
	    		lon_decimales = Double.parseDouble(str_decimales);	    				
	    		
	    		Double lon_aux = (lon_decimales*60);	    		
	    		Integer lon_minutos = lon_aux.intValue();
	    		
	    		posicion1 = lon_aux.toString().indexOf(".") + 1;
	    		posicion2 = lon_aux.toString().length();
	    		str_decimales = lon_aux.toString().substring(posicion1, posicion2);
	    		str_decimales = "0." + str_decimales.trim();
	    		lon_decimales = Double.parseDouble(str_decimales);	    				
	    		
	    		lon_aux = (lon_decimales*60);	    		
	    		Integer lon_segundos = lon_aux.intValue();

	    		
	    		String longitud =  "\n GRADOS: " + lon_grados.toString() + "\n MINUTOS: " + lon_minutos.toString() + "\n SEGUNDOS: " + lon_segundos.toString();
	    		
	    		outlat.setText("LATITUD: " + currentLocation.getLatitude() + latitud );
	    		outlong.setText("LONGITUD: " + currentLocation.getLongitude()  + longitud);
	    		
	    		str_lon_grados = lon_grados.toString();
	    		str_lon_minutos = lon_minutos.toString();
	    		str_lon_segundos = lon_segundos.toString();

	    	}
		}
	};
	
    private class MyLocationListener implements LocationListener 
    {
        public void onLocationChanged(Location loc) {
            if (loc != null) {
                Toast.makeText(getBaseContext(), 
                    getResources().getString(R.string.gps_signal_found), 
                    Toast.LENGTH_LONG).show();
                setCurrentLocation(loc);
                handler.sendEmptyMessage(0);
            }
        }

        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onStatusChanged(String provider, int status, 
            Bundle extras) {
            // TODO Auto-generated method stub
        }
    }
    
    public void cmd_aceptar_OnClick(View Botton)
	  {
	  	try
	  	{
	  		Rastro.EscribirEnConsola(nombreClase, "M", "INICIO EJECUCION","cmd_aceptar_OnClick");
	  		
	  		//validar si se hizo la búsqueda de gps
	  		if (this.str_lat_grados.equals(""))
	  		{
	  			Toast.makeText(getApplicationContext(), "Primero realice la búsqueda.",Toast.LENGTH_SHORT).show();
	  			return;
	  		}
	  		
	  		//retornar valores a la activity anterior
			Intent resultData = new Intent();
            resultData.putExtra("p_lat_grados", this.str_lat_grados);
            resultData.putExtra("p_lat_minutos", this.str_lat_minutos);
            resultData.putExtra("p_lat_segundos", this.str_lat_segundos);
            resultData.putExtra("p_lon_grados", this.str_lon_grados);
            resultData.putExtra("p_lon_minutos", this.str_lon_minutos);
            resultData.putExtra("p_lon_segundos", this.str_lon_segundos);
            setResult(Activity.RESULT_OK, resultData);
            finish();
	    	
	    	Rastro.EscribirEnConsola(nombreClase, "M", "FIN EJECUCION EJECUCION","cmd_aceptar_OnClick");
	  	}
	  	catch(Exception ex)
	  	{
	  		Rastro.EscribirEnConsola(this.nombreClase, "E", ex.getMessage(),"cmd_aceptar_OnClick");
	  	}
	  }
    
    public void cmd_cancelar_OnClick(View Botton)
	  {
	  	try
	  	{
	  		Rastro.EscribirEnConsola(nombreClase, "M", "INICIO EJECUCION","cmd_cancelar_OnClick");
	  		
	  		finish();
	    	
	    	Rastro.EscribirEnConsola(nombreClase, "M", "FIN EJECUCION EJECUCION","cmd_cancelar_OnClick");
	  	}
	  	catch(Exception ex)
	  	{
	  		Rastro.EscribirEnConsola(this.nombreClase, "E", ex.getMessage(),"cmd_cancelar_OnClick");
	  	}
	  }
    
}