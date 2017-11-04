package com.mobile.metrocom;

import android.app.Activity;
import android.os.Bundle;
import com.mobile.metrocom.Rastro;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


public class ClienteContenedor  extends TabActivity{
	private static final String nombreClase = "ClienteContenedor";
	  String loginUsuario = "";
	  String nombreUsuario = "";
	  String codigoUsuario = "";
	  String infoCliente = "";  
	  
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	   try
	   {	
		   Rastro.EscribirEnConsola(nombreClase, "M", "INICIO EJECUCION","onCreate");
		   
		   super.onCreate(savedInstanceState);
		   setContentView(R.layout.activity_cliente_contenedor);   
		   
		   TabHost tabHost = getTabHost(); //creamos el tabhost de esta actividad
		   TabHost.TabSpec spec; //se crea recursos para las propiedades de la pestaña
		   Intent intent; //intent para abrir pestañas
		   Resources res = getResources(); //recuperar los recursos asociados
		   
		   //se abre la primera pestaña por defecto
		   intent = new Intent().setClass(this, ClientePestanaDatos.class);
		   //configurar las propiedades de la pestaña
		   spec = tabHost.newTabSpec("Datos").setIndicator("Datos",res.getDrawable(R.drawable.res_cli_pest_datos)).setContent(intent);
		   tabHost.addTab(spec);
		   
		   intent = new Intent().setClass(this, ClientePestanaChip.class);
		   //configurar las propiedades de la pestaña
		   spec = tabHost.newTabSpec("PDV").setIndicator("PDV",res.getDrawable(R.drawable.res_cli_pest_chip)).setContent(intent);
		   tabHost.addTab(spec);
		   
		   intent = new Intent().setClass(this, ClientePestanaPDV.class);
		   //configurar las propiedades de la pestaña
		   spec = tabHost.newTabSpec("CHIPS").setIndicator("CHIPS",res.getDrawable(R.drawable.res_cli_pest_pdv)).setContent(intent);
		   tabHost.addTab(spec);
		   
		   setTabColor(tabHost);
		   
		   //recuperar parámetros desde activity anterior
		   Bundle bundle = getIntent().getExtras();
	    	if ( bundle.getString("loginUsuario") == null 
	    		 ||	 bundle.getString("nombreUsuario") == null
	    		 ||	 bundle.getString("codigoUsuario") == null
	    		 ||	 bundle.getString("infoCliente") == null)
	        {
	    		Toast.makeText(getApplicationContext(), "No se pudo cargar información de actividad anterior. Imposible continuar",Toast.LENGTH_SHORT).show();
	    		 finish(); //cerrar pantalla
	        }
	        else
	        {
	        	loginUsuario = bundle.getString("loginUsuario");
	        	nombreUsuario = bundle.getString("nombreUsuario");
	        	codigoUsuario = bundle.getString("codigoUsuario"); 
	        	infoCliente = bundle.getString("infoCliente");
	        	
	        	TextView objLblUsuarioConectado = (TextView) findViewById(R.id.lblUsuarioConectado);
	        	objLblUsuarioConectado.setText("Usuario Activo : " + codigoUsuario + "/" + loginUsuario + "/" + nombreUsuario);
	        	
	        }
		       
		   Rastro.EscribirEnConsola(nombreClase, "M", "FIN EJECUCION EJECUCION","onCreate");
	   }
	   catch (Exception ex)
	   {
		   Rastro.EscribirEnConsola(nombreClase, "E", ex.getMessage(),"onCreate");
		   
	   }
	}
	
	private void setTabColor(TabHost tabHost) {
	    try {
	        for (int i=0; i < tabHost.getTabWidget().getChildCount();i++) {
	            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
	            if (tv != null) {
	                tv.setTextColor(Color.RED);
	            }
	            TextView tv2 = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); // Selected Tab
	            if (tv2 != null) {
	                tv2.setTextColor(Color.RED);
	            }
	        }
	    } catch (ClassCastException e) {
	        // A precaution, in case Google changes from a TextView on the tabs.
	    }
	}
}