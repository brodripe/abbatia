document.onmouseover = domouseover;
document.onmouseout = domouseout;

function domouseover() {

  if(document.all){
  	srcElement = window.event.srcElement;    	
	if (srcElement.className.indexOf("boton") > -1) {  
		if (srcElement.className!="botonDisabled"){
      			var linkName = srcElement.name;        	
			eval("document.all." + linkName + ".className='botonOver'");
		}
	}
  }
}

function domouseout() {
  if (document.all){
  	srcElement = window.event.srcElement;  	  	
	if (srcElement.className.indexOf("boton") > -1) {
		if (srcElement.className!="botonDisabled"){
       			var linkName = srcElement.name;
			eval("document.all." + linkName + ".className='boton'");
 		}
      	}
  }
}