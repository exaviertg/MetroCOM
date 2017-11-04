package com.services.metrocom.android;

import com.bddmanager.metrocom.*;

public class solicitudesAndroid {
	String nombreClase = "solicitudesAndroid";

		//Consultar listado clientes 
		public String consultarClientesMetroCOM(String filtro )
		{
			try
			{
			Rastro.EscribirEnConsola(nombreClase, "M", "INICIO EJECUCION","consultarClientesMetroCOM");
			conectorBddJTDS objConector = new conectorBddJTDS();

 			 if (objConector.ConsultarBdd("select rtrim(cast(codigo as varchar(10))) +  ' - ' + primer_nombre + ' '  + segundo_nombre + ' ' + primer_apellido + ' ' + segundo_apellido as descripcion_cliente from car_cliente  where primer_apellido like '%" + filtro.trim() + "%' order by codigo"))
			 {
 				StringBuilder objStrBuider = new StringBuilder();
				objStrBuider.append("<?xml version='1.0' encoding='UTF-8' ?>");
				objStrBuider.append("<raiz>");
 				while (objConector.rsDatos.next())
 	            {
 	                String nombre = objConector.rsDatos.getString("descripcion_cliente");

 	                objStrBuider.append("<cliente>");
						objStrBuider.append("<nombre>"+ nombre  + "</nombre>");
					objStrBuider.append("</cliente>");
 	            }
				objStrBuider.append("</raiz>"); 
				
				Rastro.EscribirEnConsola(nombreClase, "X", "[" + objStrBuider.toString() + "]" ,"consultarClientesMetroCOM");
				
				return objStrBuider.toString();
				
			}
			else
			{
				return null;
			}
			}
			catch (Exception ex)
			{
				Rastro.EscribirEnConsola(nombreClase, "E", ex,"consultarClientesMetroCOM");
				return null;
			}
			
		}	
		
		//Logueo enviando usuario y password
		public String logueoUsuarioMetroCOM(String usuario, String password)
		{			
			try
			{
				Rastro.EscribirEnConsola(nombreClase, "M", "INICIO EJECUCION","logueoUsuarioMetroCOM");
				conectorBddJTDS objConector = new conectorBddJTDS();

	 			 if (objConector.ConsultarBdd(" select usuario as login, nmb_completo as nombre, cdg_usuario as codigoInterno from usuario where usuario = '" + usuario.trim() + "' and clave = '" + password.trim() + "' "))
				 {
	 				StringBuilder objStrBuider = new StringBuilder();
					objStrBuider.append("<?xml version='1.0' encoding='UTF-8' ?>");
					objStrBuider.append("<raiz>");
	 				if (objConector.rsDatos.next())
	 	            {
	 	               String login = objConector.rsDatos.getString("login");
	 	               String nombre = objConector.rsDatos.getString("nombre");
	 	               String codigoInterno = ((Integer)objConector.rsDatos.getInt("codigoInterno")).toString();
	
	 	                objStrBuider.append("<infoLogin>");
							objStrBuider.append("<respuesta>"+ "S"  + "</respuesta>");
							objStrBuider.append("<login>"+ login.trim()  + "</login>");
							objStrBuider.append("<nombre>"+ nombre.trim()  + "</nombre>");
							objStrBuider.append("<codigoInterno>"+ codigoInterno  + "</codigoInterno>");						
						objStrBuider.append("</infoLogin>");
	 	            } 				
	 				else
	 	            {
	
	  	                objStrBuider.append("<infoLogin>");
	 						objStrBuider.append("<respuesta>"+ "N"  + "</respuesta>");
	 						objStrBuider.append("<login>"+ ""  + "</login>");
	 						objStrBuider.append("<nombre>"+ ""  + "</nombre>");
	 						objStrBuider.append("<codigoInterno>"+ ""  + "</codigoInterno>");						
	 					objStrBuider.append("</infoLogin>");
	  	            }
					objStrBuider.append("</raiz>"); 				

					Rastro.EscribirEnConsola(nombreClase, "X", "[" + objStrBuider.toString() + "]" ,"logueoUsuarioMetroCOM");
					
					return objStrBuider.toString();				
				}
				else
				{
					return null;
				}
			}
			catch (Exception ex)
			{
				Rastro.EscribirEnConsola(nombreClase, "E", ex,"logueoUsuarioMetroCOM");				
				return null;
			}

		}
		
		//guardar/actualizar información cliente
		public String guardarClienteMetroCOM(String idCliente, 
											 String descripcion,
											 String ruc,
											 String primerNombre,
											 String segundoNombre,
											 String primerApellido,
											 String segundoApellido,
											 String telefono,
											 String celular,										
											 String direccion)
		{
			
			try
			{
				Rastro.EscribirEnConsola(nombreClase, "M", "INICIO EJECUCION","guardarClienteMetroCOM");
				conectorBddJTDS objConector = new conectorBddJTDS();
				
				StringBuilder objStrSql = new StringBuilder();
				
				String tipo_documento = "";
				
				if (ruc.trim().length() > 10)
					tipo_documento = "2";
				else
					tipo_documento = "1";	
				
				if (idCliente.equals(""))
				{
					/*objConector.ConsultarBdd("select isnull(max(codigo),0) + 1 as idNuevo from car_cliente");
					Integer nuevoCodigoCliente = 0;
					if (objConector.rsDatos.next())
						nuevoCodigoCliente = objConector.rsDatos.getInt("idNuevo");
					else 
						return null;*/									
					 
					
					objStrSql.append(" insert into car_cliente(documento,fact_ruc, descripcion,primer_nombre,segundo_nombre,primer_apellido,segundo_apellido,telefono,fact_direccion,tipo_documento) "); 
					objStrSql.append(" values ");
					objStrSql.append("(") ;
					objStrSql.append("'" + ruc + "'" + ",");
					objStrSql.append("'" + ruc + "'" + ",");
					objStrSql.append("'" + descripcion + "'" + ",");					
					objStrSql.append("'" + primerNombre + "'" + ",");
					objStrSql.append("'" + segundoNombre + "'" + ",");
					objStrSql.append("'" + primerApellido + "'" + ",");
					objStrSql.append("'" + segundoApellido + "'" + ",");
					objStrSql.append("'" + telefono + "'" + ",");
					objStrSql.append("'" + direccion + "',");
					objStrSql.append("" + tipo_documento + "");
					objStrSql.append(")") ;
					

				}
				else
				{
					objStrSql.append(" update car_cliente "); 
					objStrSql.append(" set ");
					objStrSql.append(" documento = '" + ruc + "'" + ",");
					objStrSql.append(" fact_ruc = '" + ruc + "'" + ",");
					objStrSql.append(" primer_nombre = '" + primerNombre + "'" + ",");
					objStrSql.append(" segundo_nombre = '" + segundoNombre + "'" + ",");
					objStrSql.append(" primer_apellido = '" + primerApellido + "'" + ",");
					objStrSql.append(" segundo_apellido =  '" + segundoApellido + "'" + ",");
					objStrSql.append(" telefono = '" + telefono + "'" + ",");
					objStrSql.append(" tipo_documento = " + tipo_documento + "" + ",");					
					objStrSql.append(" fact_direccion =  '" + direccion + "'");
					objStrSql.append(" where codigo = " + idCliente) ;
				}				
				//System.out.println(objStrSql.toString());
				
				
				
 				StringBuilder objStrBuiderXML = new StringBuilder();
 				objStrBuiderXML.append("<?xml version='1.0' encoding='UTF-8' ?>");
 				objStrBuiderXML.append("<raiz>");
				
	 			 if (  objConector.EjecutarQL( objStrSql.toString())  )  
				 {
					if ("".equals(idCliente.trim()))
					{
		 				objConector.ConsultarBdd("select codigo from CAR_CLIENTE where FACT_RUC = '" + ruc +  "'");
						if (objConector.rsDatos.next())
							idCliente = objConector.rsDatos.getString("codigo");	
						else
							idCliente = "-1";
					}
					
					if (idCliente != "-1")
					{
		 				objStrBuiderXML.append("<infoEjecucion>");
		 					objStrBuiderXML.append("<respuesta>"+ "OK" + "</respuesta>");
		 					objStrBuiderXML.append("<idcliente>"+ idCliente.trim() + "</idcliente>");
		 				objStrBuiderXML.append("</infoEjecucion>");
					}
					else
					{	
		 				objStrBuiderXML.append("<infoEjecucion>");
		 					objStrBuiderXML.append("<respuesta>"+ "ERROR" + "</respuesta>");
		 					objStrBuiderXML.append("<idcliente>"+ "" + "</idcliente>");
		 				objStrBuiderXML.append("</infoEjecucion>");
		  	         }
	 	         } 				
	 			else
	 	         {	
	 				objStrBuiderXML.append("<infoEjecucion>");
	 					objStrBuiderXML.append("<respuesta>"+ "ERROR" + "</respuesta>");
	 					objStrBuiderXML.append("<idcliente>"+ "" + "</idcliente>");
	 				objStrBuiderXML.append("</infoEjecucion>");
	  	         }		 					
	 			objStrBuiderXML.append("</raiz>"); 				
	 			Rastro.EscribirEnConsola(nombreClase, "X", "[" + objStrBuiderXML.toString() + "]" ,"guardarClienteMetroCOM");
					
				return objStrBuiderXML.toString();
			}
			catch (Exception ex)
			{
				Rastro.EscribirEnConsola(nombreClase, "E", ex,"guardarClienteMetroCOM");
				return null;
			}

		} //fin guardarClienteMetroCOM
		
		//Consultar Informacion del cliente por su ID
		public String consultarInformacionClienteMetroCOM(String idCliente)
		{			
			try
			{
				Rastro.EscribirEnConsola(nombreClase, "M", "INICIO EJECUCION","consultarInformacionClienteMetroCOM");
				conectorBddJTDS objConector = new conectorBddJTDS();

				StringBuilder objStrBuiderSQL = new StringBuilder();
				objStrBuiderSQL.append(" select isnull(codigo,'') as codigo,isnull(descripcion,'') as descripcion,isnull(fact_ruc,'') as fact_ruc," +
						"isnull(primer_nombre,'') as primer_nombre,isnull(segundo_nombre,'') as segundo_nombre," +
						"isnull(primer_apellido,'') as primer_apellido,isnull(segundo_apellido,'') as segundo_apellido," +
						"isnull(telefono,'') as telefono, isnull('','') as celular_claro,isnull(fact_direccion,'') as fact_direccion ");
				objStrBuiderSQL.append(" from car_cliente ");
				objStrBuiderSQL.append(" where codigo = " + idCliente);
				
	 			 if ( objConector.ConsultarBdd( objStrBuiderSQL.toString() ) )
				 {
	 				StringBuilder objStrBuider = new StringBuilder();
					objStrBuider.append("<?xml version='1.0' encoding='UTF-8' ?>");
					objStrBuider.append("<raiz>");
	 				if (objConector.rsDatos.next())
	 	            {
	 	               
	    	             String descripcion = objConector.rsDatos.getString("descripcion");
		 				 String ruc = objConector.rsDatos.getString("fact_ruc");
		 				 String primerNombre  = objConector.rsDatos.getString("primer_nombre");
		 				 String segundoNombre = objConector.rsDatos.getString("segundo_nombre");
		 				 String primerApellido = objConector.rsDatos.getString("primer_apellido");
		 				 String segundoApellido = objConector.rsDatos.getString("segundo_apellido");
		 				 String telefono = objConector.rsDatos.getString("telefono");
		 				 String celular = objConector.rsDatos.getString("celular_claro");										
		 				 String direccion = objConector.rsDatos.getString("fact_direccion");
	 	               
	
	 	                objStrBuider.append("<infoCliente>");
							objStrBuider.append("<descripcion>"+ descripcion  + "</descripcion>");
							objStrBuider.append("<fact_ruc>"+ ruc  + "</fact_ruc>");
							objStrBuider.append("<primer_nombre>"+ primerNombre  + "</primer_nombre>");
							objStrBuider.append("<segundo_nombre>"+ segundoNombre  + "</segundo_nombre>");							
							objStrBuider.append("<primer_apellido>"+ primerApellido  + "</primer_apellido>");
							objStrBuider.append("<segundo_apellido>"+ segundoApellido  + "</segundo_apellido>");
							objStrBuider.append("<telefono>"+ telefono  + "</telefono>");
							objStrBuider.append("<celular_claro>"+ celular  + "</celular_claro>");
							objStrBuider.append("<fact_direccion>"+ direccion  + "</fact_direccion>");							
						objStrBuider.append("</infoCliente>");
	 	            } 				
	 					
					objStrBuider.append("</raiz>"); 
					System.out.println(objStrBuider.toString());
		 			Rastro.EscribirEnConsola(nombreClase, "X", "[" + objStrBuider.toString() + "]" ,"consultarInformacionClienteMetroCOM");
					
					return objStrBuider.toString();				
				}
				else
				{
					return null;
				}
			}
			catch (Exception ex)
			{
				Rastro.EscribirEnConsola(nombreClase, "E", ex,"consultarInformacionClienteMetroCOM");
				return null;
			}

		} //fin consultar cliente metrocom
		
		//Consultar Informacion del cliente por su ID
		public String consultarPDVPorcliente(String idCliente)
		{			
			
			conectorBddJTDS objConector = new conectorBddJTDS();
			try
			{
				Rastro.EscribirEnConsola(nombreClase, "M", "INICIO EJECUCION","consultarPDVPorcliente");
				StringBuilder objStrBuiderSQL = new StringBuilder();
				//objStrBuiderSQL.append(" select distinct numero_pdv as codigo, 50 as saldo from chips where codigo_cliente = " + idCliente) ;
				
				/*idCliente = "22295";
				objStrBuiderSQL.append(" SELECT CDG_PDV as numero_pdv, CDG_CLIENTE as codigo, SUM(ISNULL(CUPO_DISPONIBLE_OPERADORA,0)) as saldo "); 
				objStrBuiderSQL.append(" FROM CAR_SALDO_CUPO_PRODUCTO ");
				objStrBuiderSQL.append(" WHERE CDG_CLIENTE = " + idCliente + " ");
				objStrBuiderSQL.append(" GROUP BY CDG_CLIENTE, CDG_PDV ");*/
				
				objStrBuiderSQL.append(" select  pdv as numero_pdv, a.cliente as codigo, ");
				objStrBuiderSQL.append(" (	SELECT isnull(SUM(b.CUPO_DISPONIBLE_OPERADORA),0) ");
				objStrBuiderSQL.append(" FROM CAR_SALDO_CUPO_PRODUCTO b  ");
				objStrBuiderSQL.append(" WHERE b.CDG_CLIENTE = a.CLIENTE ");
				objStrBuiderSQL.append(" and b.CDG_PDV = a.PDV ");
				objStrBuiderSQL.append(" ) as saldo ");
				objStrBuiderSQL.append(" from CAR_LOCAL a ");
				objStrBuiderSQL.append(" where a.CLIENTE = "+ idCliente +" ");
				objStrBuiderSQL.append(" and a.PDV is not null ");
				objStrBuiderSQL.append(" and a.PDV != '' ");

				
	 			if ( objConector.ConsultarBdd( objStrBuiderSQL.toString() ) )
				{
	 				StringBuilder objStrBuider = new StringBuilder();
					objStrBuider.append("<?xml version='1.0' encoding='UTF-8' ?>");
					objStrBuider.append("<raiz>");
	 				while (objConector.rsDatos.next())
	 	            {
	 	               
	    	             String codigo = objConector.rsDatos.getString("numero_pdv");
		 				 String saldo = objConector.rsDatos.getString("saldo");	 	               
	
	 	                objStrBuider.append("<infoPDV>");
							objStrBuider.append("<codigo>"+ codigo  + "</codigo>");
							objStrBuider.append("<saldo>"+ saldo  + "</saldo>");
						objStrBuider.append("</infoPDV>");
	 	             } 				
	 					
					objStrBuider.append("</raiz>"); 				
		 			Rastro.EscribirEnConsola(nombreClase, "X", "[" + objStrBuider.toString() + "]" ,"consultarPDVPorcliente");
					
					return objStrBuider.toString();				
				}
				else
				{
					return null;
				}
			}
			catch (Exception ex)
			{
				Rastro.EscribirEnConsola(nombreClase, "E", ex,"consultarPDVPorcliente");
				return null;
			}

		} //fin consultar cliente metrocom		

		//Consultar Informacion del cliente por su ID
		public String consultarChipsPorCliente(String idCliente, String pdv)
		{			
				try
				{
					Rastro.EscribirEnConsola(nombreClase, "M", "INICIO EJECUCION","consultarChipsPorCliente");
					conectorBddJTDS objConector = new conectorBddJTDS();

					StringBuilder objStrBuiderSQL = new StringBuilder();
					//objStrBuiderSQL.append(" select numero_pdv, numero_chip from chips where codigo_cliente = " + idCliente.trim() + "") ;
					objStrBuiderSQL.append(" select pdv as  numero_pdv,NUMERO_COMPLETO as numero_chip from CAR_KARDEX_PRODUCTO where PDV = '" + pdv + "' and ESTADO = 'ACTIVO'") ;
					
				
		 			if ( objConector.ConsultarBdd( objStrBuiderSQL.toString() ) )
					{
		 				StringBuilder objStrBuider = new StringBuilder();
						objStrBuider.append("<?xml version='1.0' encoding='UTF-8' ?>");
						objStrBuider.append("<raiz>");
		 				while (objConector.rsDatos.next())
		 	            {
		 	               
		    	             String numero_pdv = objConector.rsDatos.getString("numero_pdv");
			 				 String numero_chip = objConector.rsDatos.getString("numero_chip");	 	               
		
		 	                objStrBuider.append("<infoChip>");
								objStrBuider.append("<numero_pdv>"+ numero_pdv  + "</numero_pdv>");
								objStrBuider.append("<numero_chip>"+ numero_chip  + "</numero_chip>");
							objStrBuider.append("</infoChip>");
		 	             } 				
		 					
						objStrBuider.append("</raiz>"); 				
			 			Rastro.EscribirEnConsola(nombreClase, "X", "[" + objStrBuider.toString() + "]" ,"consultarChipsPorCliente");
						
						return objStrBuider.toString();				
					}
					else
					{
						return null;
					}
				}
				catch (Exception ex)
				{
					Rastro.EscribirEnConsola(nombreClase, "E", ex,"consultarChipsPorCliente");
					return null;
				}

			} //fin consultar cliente metrocom		
		
		//Ingresar Chip validando q exista en inventario
		public String aniadirChipAPdv(String idCliente, String pdv, String numero, String usuario)
		{		
			try
			{
				Rastro.EscribirEnConsola(nombreClase, "M", "INICIO EJECUCION","aniadirChipAPdv");
				conectorBddJTDS objConector = new conectorBddJTDS();
		
				StringBuilder objStrSql = new StringBuilder();
				
				//if (idCliente.equals(""))
				{

					objStrSql.append(" insert into car_kardex_producto "); 
					objStrSql.append(" (PDV,ESTADO,OBSERVACION, ");
					objStrSql.append(" FCH_APROBACION,FCH_ENTREGA,FCH_INGRESO, ");
					objStrSql.append(" FCH_MODIFICACION,CDG_USUARIO_INGRESA,CDG_USUARIO_MODIFICA, ");
					objStrSql.append(" CDG_LOCAL,NUMERO_IMEI,NUMERO_COMPLETO,CDG_PRODUCTO)   ");
					objStrSql.append(" select '"+ pdv + "','ACTIVO','PRODUCTO INGRESADO DESDE ANDROID', ");
					objStrSql.append(" GETDATE(),GETDATE(),GETDATE(), ");
					objStrSql.append(" GETDATE()," + usuario + "," + usuario + ", ");
					objStrSql.append(" CDG_LOCAL,SERIE1,SERIE3,CDG_PRODUCTO ");
					objStrSql.append(" from GRP_INVENTARIO  ");
					objStrSql.append(" where SERIE3 = '" + numero + "' ");		
					
				}
				//System.out.println(objStrSql.toString());
				
				StringBuilder objStrBuiderXML = new StringBuilder();
				objStrBuiderXML.append("<?xml version='1.0' encoding='UTF-8' ?>");
				objStrBuiderXML.append("<raiz>");
				
				Integer enInventario = 0;				
				objConector.ConsultarBdd("select COUNT(1) as enInventario from GRP_INVENTARIO where SERIE3 = '" + numero + "'");
				if (objConector.rsDatos.next())
					enInventario = objConector.rsDatos.getInt("enInventario"); 	
				
				Integer asignado = 0;
				objConector.ConsultarBdd("select count(1) as asignado from car_kardex_producto where NUMERO_COMPLETO = '" + numero + "' and ESTADO = 'ACTIVO'");
				if (objConector.rsDatos.next())
					asignado = objConector.rsDatos.getInt("asignado"); 	

				if (asignado > 0) //numero ya asignado
				{
					objStrBuiderXML.append("<infoEjecucion>");
							objStrBuiderXML.append("<respuesta>"+ "ERROR1" + "</respuesta>");
					objStrBuiderXML.append("</infoEjecucion>");
				}	
				else if (enInventario == 0 ) //no existe en inventario
				{
					objStrBuiderXML.append("<infoEjecucion>");
							objStrBuiderXML.append("<respuesta>"+ "ERROR0" + "</respuesta>");
					objStrBuiderXML.append("</infoEjecucion>");
				}	
				else 
				{	
					if (  objConector.EjecutarQL( objStrSql.toString())  ) //ok  
					{
						objStrBuiderXML.append("<infoEjecucion>");
							objStrBuiderXML.append("<respuesta>"+ "OK" + "</respuesta>");
						objStrBuiderXML.append("</infoEjecucion>");
					} 				
					else //falla de bdd al insertar
					{	
						objStrBuiderXML.append("<infoEjecucion>");
							objStrBuiderXML.append("<respuesta>"+ "ERROR" + "</respuesta>");
						objStrBuiderXML.append("</infoEjecucion>");
					}		
				}
				objStrBuiderXML.append("</raiz>"); 				
	 			Rastro.EscribirEnConsola(nombreClase, "X", "[" + objStrBuiderXML.toString() + "]" ,"aniadirChipAPdv");
				
				return objStrBuiderXML.toString();
			}
			catch (Exception ex)
			{
				Rastro.EscribirEnConsola(nombreClase, "E", ex,"aniadirChipAPdv");
				return null;
			}		
		} //fin guardarClienteMetroCOM
				
		//crear nuevo pdv
		public String crearPDV(					
				String idCliente,
				String descripcion,
				String lat_grados,
				String lat_minutos,
				String lat_segundos,
				String lon_grados,
				String lon_minutos,
				String lon_segundos)
		{
			String pdv = "";
			try
			{
				Rastro.EscribirEnConsola(nombreClase, "M", "INICIO EJECUCION","crearPDV");
				conectorBddJTDS objConector = new conectorBddJTDS();
				
				StringBuilder objStrSql = new StringBuilder();
				
				//if (idCliente.equals(""))
				{
					objConector.ConsultarBdd("select right('0000000000' + (ltrim(rtrim( isnull(max(codigo),0) + 1 ))) ,10  ) as idNuevo from car_local");
					String nuevoCodigoLocal = "";
					if (objConector.rsDatos.next())
						nuevoCodigoLocal = objConector.rsDatos.getString("idNuevo");
					else 
						return null; //GENERAR PDV
					pdv = "";
						
					
					objStrSql.append(" insert into car_local(pdv,cliente,latitud_grados,latitud_minutos,latitud_segundos,longitud_grados,longitud_minutos,longitud_segundos,direccion,PARROQUIA,NUMERO_BASE,MES,ANIO,FECHA,TIKET,CDG_USUARIO) "); 
					objStrSql.append(" values ");
					objStrSql.append("(") ;
					objStrSql.append("'" + nuevoCodigoLocal + "',") ;
					objStrSql.append("" + idCliente + "" + ",");
					//objStrSql.append("'" + descripcion + "'" + ",");					
					//objStrSql.append("'" + "" + "'" + ",");
					objStrSql.append("'" + lat_grados + "'" + ",");
					objStrSql.append("'" + lat_minutos + "'" + ",");
					objStrSql.append("'" + lat_segundos + "'" + ",");
					objStrSql.append("'" + lon_grados + "'" + ",");
					objStrSql.append("'" + lon_minutos + "'" + ",");
					objStrSql.append("'" + lon_segundos + "'");
					objStrSql.append(",'','','','','',GETDATE(),'',0)") ;
				}
				//System.out.println(objStrSql.toString());
				
 				StringBuilder objStrBuiderXML = new StringBuilder();
 				objStrBuiderXML.append("<?xml version='1.0' encoding='UTF-8' ?>");
 				objStrBuiderXML.append("<raiz>");
				
	 			 if (  objConector.EjecutarQL( objStrSql.toString())  )  
				 {
	 				objStrBuiderXML.append("<infoEjecucion>");
	 					objStrBuiderXML.append("<respuesta>"+ "OK" + "</respuesta>");
	 				objStrBuiderXML.append("</infoEjecucion>");
	 	         } 				
	 			else
	 	         {	
	 				objStrBuiderXML.append("<infoEjecucion>");
	 					objStrBuiderXML.append("<respuesta>"+ "ERROR" + "</respuesta>");
	 				objStrBuiderXML.append("</infoEjecucion>");
	  	         }		 					
	 			objStrBuiderXML.append("</raiz>"); 				
	 			Rastro.EscribirEnConsola(nombreClase, "X", "[" + objStrBuiderXML.toString() + "]" ,"crearPDV");
					
				return objStrBuiderXML.toString();
			}
			catch (Exception ex)
			{
				Rastro.EscribirEnConsola(nombreClase, "E", ex,"crearPDV");
				return null;
			}

		} //fin crearPDV

}
