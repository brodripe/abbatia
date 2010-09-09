<!-- 

// Mira navegador del cliente
var isNav4 = false, isNav5 = false, isIE4 = false
var strSeperator = "-"; 

var vDateType = 3; 
//                1 = mm-dd-yyyy
//                2 = yyyy-dd-mm  
//                3 = dd-mm-yyyy

var vYearType = 4; //Set to 2 or 4 for number of digits in the year for Netscape
var vYearLength = 2; // Set to 4 if you want to force the user to enter 4 digits for the year before validating.

var err = 0; // Set the error code to a default of zero

if(navigator.appName == "Netscape") 
{
   if (navigator.appVersion < "5")  
   {
      isNav4 = true;
      isNav5 = false;
	}
   else
   if (navigator.appVersion > "4") 
   {
      isNav4 = false;
      isNav5 = true;
	}
}
else  
{
   isIE4 = true;
}

function DateFormat(vDateName, vDateValue, e, dateCheck, dateType)  {

vDateType = dateType;
   
   if (vDateValue == "~")
   {
      alert("AppVersion = "+navigator.appVersion+" \nNav. 4 Version = "+isNav4+" \nNav. 5 Version = "+isNav5+" \nIE Version = "+isIE4+" \nYear Type = "+vYearType+" \nDate Type = "+vDateType+" \nSeparator = "+strSeperator);
      vDateName.value = "";
      vDateName.focus();
      return true;
   }
      
   var whichCode = (window.Event) ? e.which : e.keyCode;
 
   if (vDateValue.length > 8 && isNav4)
   {
      if ((vDateValue.indexOf("-") >= 1) || (vDateValue.indexOf("/") >= 1))
         return true;
   }
   
   var alphaCheck = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ/-";
   if (alphaCheck.indexOf(vDateValue) >= 1)  
   {
      if (isNav4)
      {
         vDateName.value = "";
         vDateName.focus();
         vDateName.select();
         return false;
      }
      else
      {
         vDateName.value = vDateName.value.substr(0, (vDateValue.length-1));
         return false;
      } 
   }
   if (whichCode == 8) //Ignore the Netscape value for backspace. IE has no value
      return false;
   else 
   {
      var strCheck = '13,37,39,47,48,49,50,51,52,53,54,55,56,57,58,59,95,96,97,98,99,100,101,102,103,104,105';
      if (strCheck.indexOf(whichCode) != -1)  
      {
         if (isNav4)  
         {
            if (((vDateValue.length < 6 && dateCheck) || (vDateValue.length == 7 && dateCheck)) && (vDateValue.length >=1))
            {
               alert("Fecha Invalida\nPor Favor, introduzca de nuevo...");
               vDateName.value = "";
               vDateName.focus();
               vDateName.select();
               return false;
            }
            if (vDateValue.length == 6 && dateCheck)  
            {
               var mDay = vDateName.value.substr(2,2);
               var mMonth = vDateName.value.substr(0,2);
               var mYear = vDateName.value.substr(4,4)
               
               if (mYear.length == 2 && vYearType == 4) 
               {
                  var mToday = new Date();
                  
                  var checkYear = mToday.getFullYear() + 30; 
                  var mCheckYear = '20' + mYear;
                  if (mCheckYear >= checkYear)
                     mYear = '19' + mYear;
                  else
                     mYear = '20' + mYear;
               }
               var vDateValueCheck = mMonth+strSeperator+mDay+strSeperator+mYear;
               
               if (!dateValid(vDateValueCheck))  
               {
                  alert("Fecha Invalida\nPor Favor, introduzca de nuevo...");
                  vDateName.value = "";
                  vDateName.focus();
                  vDateName.select();
                  return false;
		         }
               return true;
            
            }
            else
            {
               if (vDateValue.length >= 8  && dateCheck)  
               {
                  if (vDateType == 1) // mmddyyyy
                  {
                     var mDay = vDateName.value.substr(2,2);
                     var mMonth = vDateName.value.substr(0,2);
                     var mYear = vDateName.value.substr(4,4)
                     vDateName.value = mMonth+strSeperator+mDay+strSeperator+mYear;
                  }
                  if (vDateType == 2) // yyyymmdd
                  {
                     var mYear = vDateName.value.substr(0,4)
                     var mMonth = vDateName.value.substr(4,2);
                     var mDay = vDateName.value.substr(6,2);
                     vDateName.value = mYear+strSeperator+mMonth+strSeperator+mDay;
                  }
                  if (vDateType == 3) // ddmmyyyy
                  {
                     var mMonth = vDateName.value.substr(2,2);
                     var mDay = vDateName.value.substr(0,2);
                     var mYear = vDateName.value.substr(4,4)
                     vDateName.value = mDay+strSeperator+mMonth+strSeperator+mYear;
                  }
                  
                  
                  var vDateTypeTemp = vDateType;
                  vDateType = 1;
                  var vDateValueCheck = mMonth+strSeperator+mDay+strSeperator+mYear;
                  
                  if (!dateValid(vDateValueCheck))  
                  {
                     alert("Fecha Invalida\nPor Favor, introduzca de nuevo...");
                     vDateType = vDateTypeTemp;
                     vDateName.value = "";
                     vDateName.focus();
                     vDateName.select();
                     return false;
		            }
                     vDateType = vDateTypeTemp;
                     return true;
	            }
               else
               {
                  if (((vDateValue.length < 8 && dateCheck) || (vDateValue.length == 9 && dateCheck)) && (vDateValue.length >=1))
                  {
                     alert("Fecha Invalida\nPor Favor, introduzca de nuevo...");
                     vDateName.value = "";
                     vDateName.focus();
                     vDateName.select();
                     return false;
                  }
               }
            }
         }
         else  
         {
         // Non isNav Check
            if (((vDateValue.length < 8 && dateCheck) || (vDateValue.length == 9 && dateCheck)) && (vDateValue.length >=1))
            {
               alert("Fecha Invalida\nPor Favor, introduzca de nuevo...");
               vDateName.value = "";
               vDateName.focus();
               return true;
            }
            
            
            if (vDateValue.length >= 8 && dateCheck)  
            {
               
               if (vDateType == 1) // mm/dd/yyyy
               {
                  var mMonth = vDateName.value.substr(0,2);
                  var mDay = vDateName.value.substr(3,2);
                  var mYear = vDateName.value.substr(6,4)
               }
               if (vDateType == 2) // yyyy/mm/dd
               {
                  var mYear = vDateName.value.substr(0,4)
                  var mMonth = vDateName.value.substr(5,2);
                  var mDay = vDateName.value.substr(8,2);
               }
               if (vDateType == 3) // dd/mm/yyyy
               {
                  var mDay = vDateName.value.substr(0,2);
                  var mMonth = vDateName.value.substr(3,2);
                  var mYear = vDateName.value.substr(6,4)
               }
               if (vYearLength == 4)
               {
                  if (mYear.length < 4)
                  {
                     alert("Fecha Invalida\nPor Favor, introduzca de nuevo...");
                     vDateName.value = "";
                     vDateName.focus();
                     return true;
                  }
               }
               
               var vDateTypeTemp = vDateType;
               

               vDateType = 1;
               

               var vDateValueCheck = mMonth+strSeperator+mDay+strSeperator+mYear;
               
               if (mYear.length == 2 && vYearType == 4 && dateCheck)  
               {
                  
                  var mToday = new Date();
                  
                  var checkYear = mToday.getFullYear() + 30; 
                  var mCheckYear = '20' + mYear;
                  if (mCheckYear >= checkYear)
                     mYear = '19' + mYear;
                  else
                     mYear = '20' + mYear;
                  vDateValueCheck = mMonth+strSeperator+mDay+strSeperator+mYear;
                  
                  
                  if (vDateTypeTemp == 1) // mm/dd/yyyy
                     vDateName.value = mMonth+strSeperator+mDay+strSeperator+mYear;
                  if (vDateTypeTemp == 3) // dd/mm/yyyy
                     vDateName.value = mDay+strSeperator+mMonth+strSeperator+mYear;

               } 
               
               
               if (!dateValid(vDateValueCheck))  
               {
                  alert("Fecha Invalida\nPor Favor, introduzca de nuevo...");
                  vDateType = vDateTypeTemp;
                  vDateName.value = "";
                  vDateName.focus();
                  return true;
		         }
               vDateType = vDateTypeTemp;
               return true;
            
            }
            else
            {
               
               if (vDateType == 1)
               {
                  if (vDateValue.length == 2)  
                  {
                     vDateName.value = vDateValue+strSeperator;
                  }
                  if (vDateValue.length == 5)  
                  {
                     vDateName.value = vDateValue+strSeperator;
                  }
               }
               if (vDateType == 2)
               {
                  if (vDateValue.length == 4)  
                  {
                     vDateName.value = vDateValue+strSeperator;
                  }
                  if (vDateValue.length == 7)  
                  {
                     vDateName.value = vDateValue+strSeperator;
                  }
               } 
               if (vDateType == 3)
               {
                  if (vDateValue.length == 2)  
                  {
                     vDateName.value = vDateValue+strSeperator;
                  }
                  if (vDateValue.length == 5)  
                  {
                     vDateName.value = vDateValue+strSeperator;
                  }
               }
               return true;
            }
         }
         if (vDateValue.length == 10   && dateCheck)  
         {
            if (!dateValid(vDateName))  
            {
//               alert(err);  
               alert("Fecha Invalida\nPor Favor, introduzca de nuevo...");
               vDateName.focus();
               vDateName.select();
	         }
         }
         return false;
      }
      else  
      {
         if (isNav4)
         {
            vDateName.value = "";
            vDateName.focus();
            vDateName.select();
            return false;
         }
         else
         {
       	    if (whichCode==46){
            	vDateName.value = vDateName.value.substr(0, (vDateValue.length-1));
            }
            return false;
         }
		}
	}
}



   function dateValid(objName) {
      var strDate;
      var strDateArray;
      var strDay;
      var strMonth;
      var strYear;
      var intday;
      var intMonth;
      var intYear;
      var booFound = false;
      var datefield = objName;
      var strSeparatorArray = new Array("-"," ","/",".");
      var intElementNr;
      // var err = 0;
      var strMonthArray = new Array(12);
      strMonthArray[0] = "Ene";
      strMonthArray[1] = "Feb";
      strMonthArray[2] = "Mar";
      strMonthArray[3] = "Abr";
      strMonthArray[4] = "May";
      strMonthArray[5] = "Jun";
      strMonthArray[6] = "Jul";
      strMonthArray[7] = "Ago";
      strMonthArray[8] = "Sep";
      strMonthArray[9] = "Oct";
      strMonthArray[10] = "Nov";
      strMonthArray[11] = "Dic";
      
      //strDate = datefield.value;
      strDate = objName;
      
      if (strDate.length < 1) {
         return true;
      }
      for (intElementNr = 0; intElementNr < strSeparatorArray.length; intElementNr++) {
         if (strDate.indexOf(strSeparatorArray[intElementNr]) != -1) 
         {
            strDateArray = strDate.split(strSeparatorArray[intElementNr]);
            if (strDateArray.length != 3) 
            {
               err = 1;
               return false;
            }
            else 
            {
               strDay = strDateArray[0];
               strMonth = strDateArray[1];
               strYear = strDateArray[2];
            }
            booFound = true;
         }
      }
      if (booFound == false) {
         if (strDate.length>5) {
            strDay = strDate.substr(0, 2);
            strMonth = strDate.substr(2, 2);
            strYear = strDate.substr(4);
         }
      }
      //Adjustment for short years entered
      if (strYear.length == 2) {
         strYear = '20' + strYear;
      }
      strTemp = strDay;
      strDay = strMonth;
      strMonth = strTemp;
      intday = parseInt(strDay, 10);
      if (isNaN(intday)) {
         err = 2;
         return false;
      }
      
      intMonth = parseInt(strMonth, 10);
      if (isNaN(intMonth)) {
         for (i = 0;i<12;i++) {
            if (strMonth.toUpperCase() == strMonthArray[i].toUpperCase()) {
               intMonth = i+1;
               strMonth = strMonthArray[i];
               i = 12;
            }
         }
         if (isNaN(intMonth)) {
            err = 3;
            return false;
         }
      }
      intYear = parseInt(strYear, 10);
      if (isNaN(intYear)) {
         err = 4;
         return false;
      }
      if (intMonth>12 || intMonth<1) {
         err = 5;
         return false;
      }
      if ((intMonth == 1 || intMonth == 3 || intMonth == 5 || intMonth == 7 || intMonth == 8 || intMonth == 10 || intMonth == 12) && (intday > 31 || intday < 1)) {
         err = 6;
         return false;
      }
      if ((intMonth == 4 || intMonth == 6 || intMonth == 9 || intMonth == 11) && (intday > 30 || intday < 1)) {
         err = 7;
         return false;
      }
      if (intMonth == 2) {
         if (intday < 1) {
            err = 8;
            return false;
         }
         if (LeapYear(intYear) == true) {
            if (intday > 29) {
               err = 9;
               return false;
            }
         }
         else {
            if (intday > 28) {
               err = 10;
               return false;
            }
         }
      }
         return true;
      }

   function LeapYear(intYear) {
      if (intYear % 100 == 0) {
         if (intYear % 400 == 0) { return true; }
      }
      else {
         if ((intYear % 4) == 0) { return true; }
      }
         return false;
      }
      
      
      
      
      
      //funciones nuevas para la gestión de la fecha de los Informes
      
      
      //métodos nuevos



	//función que verifica si el año pasado como parámetro está dentro del rango de años válidos
	//Está establecido los límites entre 1900 y 2000
	function obtenerAnno(d) {
	  if (d < 100)
	     return (d+1900);
	  else if (d < 1900 || d > 2100) {
	    alert ("El año no puede ser superior a 2100 ni inferior a 1900");
	    return 0;
	  }
	  else 
	    return d;
	}
	
	
	//función que verifica si el año, mes y día pasados como parámetros forman una fecha válida
	function esFechaValida (dia, mes, anno) {
	  // el mes debe estar en un rango de 1-12
	  mes = mes - 1;  // javascript rango del mes : 0- 11
	  var fechaTemporal = new Date(anno,mes,dia);
	 
	  if ( (obtenerAnno(fechaTemporal.getYear()) == anno) && (mes == fechaTemporal.getMonth()) && (dia == fechaTemporal.getDate()) )
	      return true;
	  else
	      return false
	}
	
	
	//función que valida si un string con el formato "dd-mm-aaaa" es una fecha válida
	function validarFecha(valor) {
	
		//si la fecha tiene una longitud menor que 10 caracteres no es válida
		//ya que debe seguir el formato dd-mm-aaaa
		if (valor.length < 10)
		  return false;
		//si no aparece un guión tampoco es una fecha válida
		else if (valor.indexOf("-")==-1)
		 return false;
		else {
		  //obtenemos los valores para el día, mes y año
		  var dia = valor.substring(0,2);
		  var mes = valor.substring(3,5);
		  var anno = valor.substring(6,10);
		   //puede que aún así la fecha no sea correcta, ya que se puede haber introducido
		   //una fecha del estilo 2002-09-09
		   //así que llamamos al método esFechaValida que nos dirá si en efecto la fecha
		   //que nos ha llegado por parámetro era una fecha válida
		  if (esFechaValida(dia,mes,anno))
		      return true;
		  else
		      return false;	
		}
	
	}
	
	//Función que actualiza el texfield pasado como parámetro siguiendo una serie de reglas
	function actualizarCampo (campo) {
	 
	 //obtenemos el valor del textfield
	 var valor = campo.value;
	 
	 //si su longitud es 2, quiere decir que se ha introducido el día
	 if (valor.length == 2) {
	   if (!isNaN(valor) && valor>0 && valor<32) {
	   		//si el día era un número y entraba dentro del rango de los días [1-31]..
	   		//entonces añadimos al textfield un guión (-)
			valor = valor + "-";
			campo.value = valor;
			return true;
	   }
	   else {
	        //si no era un número o el rango no era el correcto, se borra el valor del textfield
			limpiarCampo(campo);
			return false;
	   }
	 }
	
	 if (valor.length == 5) {
	   //si la longitud es 5, ya tenemos un día, un guión y un mes
	   //obtenemos los valores de los tres elementos
	   var dia = valor.substring(0,2);
	   var mes = valor.substring(3,5);
	   var guion = valor.substring(2,3);
	
	   //verificamos que el guión es correcto, que día y mes son números y que están dentro
	   //de los rangos establecidos para los días y los meses
	   if (!isNaN(dia) && dia>0 && dia<32 && mes>0 && mes<13 && !isNaN(mes) && guion=="-") {
			//si todo ha ido bien, añadimos el segundo guión
			valor = valor + "-";
			campo.value = valor;
			return true;
	   }
	   else {
	    	//si alguna condición no se ha cumplido, borramos el valor del textfield
			limpiarCampo(campo);
			return false;
	   }
	 }
	 if (valor.length == 10) {
	   //si la longitud es 10, ya deberíamos tener la fecha correcta
	   //obtenemos los valores para cada elemento
	   var dia = valor.substring(0,2);
	   var mes = valor.substring(3,5);
	   var guion = valor.substring(2,3);
	   var anno = valor.substring(6,10);
	   var guion2 = valor.substring(5,6);
	   //y verificamos que se cumplen todas las condiciones
	   if (isNaN(dia) || dia>31 || dia<1 || isNaN(mes) || mes>12 || mes<1  || guion!="-" || isNaN(anno) || anno>2100 || anno<1900 || guion2!="-") {
	        
			//si no se cumple alguna de las condiciones, borramos el valor del textfield
			limpiarCampo(campo);
			return false
	   }
	   else
	       return true;
	 }
	
	}
	
/* no se usa
	
	function validarFechaTecleada (evento, campo) {
	
	 if (evento.keyCode == 13) {
	   //alert('Se ha pulsado Return!');
	   
	   if (campo.value =="") {
			alert(campoObligatorio);
			return false;
		}
	   
	   
	   if (!actualizarCampo(campo))
	      return false;
	
	   if (validarFecha(campo.value)) {
	      alert("fecha correcta");
	      return true;
	   }
	   else {
	      alert ("fecha incorrecta");
	      limpiarCampo(campo);
	      return false;
	   }
	   
	 }
	 else {
	   actualizarCampo(campo);
	   return false;
	 }
		
	}
	
*/	
	
	//función que borra el contenido del textfield pasado como parámetro
	function limpiarCampo (elCampo) {
		
		elCampo.value="";
		elCampo.focus();
	
	}

      
      
      
      
      
      
 -->