function bloqueoModal(){
	
	window.opener.parent.frames.menu.ocultarOpciones.style.visibility='';
	window.opener.parent.frames.menu.ocultarMarcas.style.visibility='';
	
	  div1 = window.opener.parent.frames.contenido.document.createElement("div");
  	  div1.setAttribute("id","espera");
  	  div1.className = "capaBloqueoModal";
  	  window.opener.parent.frames.contenido.document.body.appendChild(div1);
}

function desbloquearModal(){

	var node = window.opener.parent.frames.contenido.document.getElementById("espera");
	if (node!=null)	node.parentNode.removeChild(node);
	window.opener.parent.frames.menu.ocultarOpciones.style.visibility='hidden';
	window.opener.parent.frames.menu.ocultarMarcas.style.visibility='hidden';
	
}

varControlTPL ="null";
informeTarjetas = "null";
estadoAplicacion = "null";