document.onclick = domouseclick;

function domouseclick() {
  tipoMarcado="columnasDatos";
  colorMarcado="#F6FEED";
  
  if (document.all){
  	srcElement = window.event.srcElement;
  	//srcElement = window.event.srcElement.parentElement; 
  	if (srcElement.id.indexOf("btn_") > -1) {
        	var linkName = srcElement.id;
        	var fila = linkName.substring(linkName.indexOf("_")+1,linkName.lastIndexOf("_"));        
        	encontrado=false;
 	        for (x=0; x<=seleccionados.length-1;x++){        		
	        	if (seleccionados[x]==fila){
	        		encontrado=true;
	        	}
		}
        	if (!encontrado){
        		tipoMarcado="columnasDatosSel";
        		colorMarcado="#FFEFB1";
       			seleccionados[seleccionados.length]=fila;
        	}else{
        		aux= new Array();cont=0;
        		for (x=0; x<=seleccionados.length-1;x++){        		
	        		if (seleccionados[x]!=fila){
	        			aux[cont]=seleccionados[x];
	        			cont++;
	        		}
			}
			seleccionados=aux;
		}
			
			
        	for (columna=1; columna<=columnas;columna++){
			//eval("document.all.btn_" + fila + "_" + columna + ".className='" + tipoMarcado + "'");
			eval("document.all.btn_" + fila + "_" + columna + ".style.backgroundColor='" + colorMarcado + "'");
       		}   

       		accionesClick(srcElement.id);
      	}
   }
}

function selTodas(){
	seleccionados=[];
        for (fila=1; fila<=n_filas;fila++){
        	for (columna=1; columna<=columnas;columna++){
			eval("document.all.btn_" + fila + "_" + columna + ".className='columnasDatosSel'");
			seleccionados[fila-1]=fila;
       		}   
	}
}

function anularTodas(){
	seleccionados=[];
        for (fila=1; fila<=n_filas;fila++){
        	for (columna=1; columna<=columnas;columna++){
			eval("document.all.btn_" + fila + "_" + columna + ".className='columnasDatos'");
       		}   
	}
}