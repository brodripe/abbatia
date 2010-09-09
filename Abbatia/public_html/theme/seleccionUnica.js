document.onclick = domouseclick;

function domouseclick() {
  tipoMarcado="columnasDatos";
  
  if (document.all){
  	srcElement = window.event.srcElement;
  	//srcElement = window.event.srcElement.parentElement;
  	if (srcElement.id.indexOf("btn_") > -1) {  		
        	var linkName = srcElement.id;
        	
        	//Se desmarca el anterior
        	if (seleccionado!=""){
        		var fila = seleccionado.substring(seleccionado.indexOf("_")+1,seleccionado.lastIndexOf("_"));        
        		for (columna=1; columna<=columnas;columna++){
				//eval("document.all.btn_" + fila + "_" + columna + ".className='" + tipoMarcado + "'");
				eval("document.all.btn_" + fila + "_" + columna + ".style.backgroundColor='#F6FEED'");
       			}
       		}
       		fila = linkName.substring(linkName.indexOf("_")+1,linkName.lastIndexOf("_"));        
        	if (srcElement.className=="columnasDatos") tipoMarcado="columnasDatosSel";
        
        	for (columna=1; columna<=columnas;columna++){
			//eval("document.all.btn_" + fila + "_" + columna + ".className='" + tipoMarcado + "'");
			eval("document.all.btn_" + fila + "_" + columna + ".style.backgroundColor='#FFEFB1'");
       		}       		
       		seleccionado=srcElement.id;
       		accionesClick(srcElement.id);
      	}
   }
}

function selTodas(){
        for (fila=1; fila<=n_filas;fila++){
        	for (columna=1; columna<=columnas;columna++){
			eval("document.all.btn_" + fila + "_" + columna + ".className='columnasDatosSel'");
       		}   
	}
}

function anularTodas(){
        for (fila=1; fila<=n_filas;fila++){
        	for (columna=1; columna<=columnas;columna++){
			eval("document.all.btn_" + fila + "_" + columna + ".className='columnasDatos'");
       		}   
	}
}