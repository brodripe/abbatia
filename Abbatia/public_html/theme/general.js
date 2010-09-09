var bEstadoOK=0;
var bEstadoERROR=1;

var anchoNoDatos='100%';
var altoNoDatos=0;
var xNoDatos=0;
var yNoDatos=0;

var nombreVentanaPopUp="ventanaPopUp";

var varControlTPL = "null";
var varControlTPL1 = 0;
var varControlTPL2 = 0;
var minutosRestantes = 0;
var segundosRestantes = 0;

var mensaje="";





function quitarFoco(){
	window.focus();
}

function habilitar(quien){

	eval ("document.all." + quien + ".className='boton'");

}

function ampliar(cadena,max){
	str=cadena.replace(/&#39;/gi, "'");
//	str=unescape(cadena);
	str=Trim(str);
	if (str.length>max){
		document.all.ampliarTxt.innerText=str;
	}
}

function ocultarTxt(){
	document.all.ampliarTxt.innerText="";
}

function Trim(cadena){
	if(cadena.length < 1){
		return"";
	}
	cadena = RTrim(cadena);
	cadena = LTrim(cadena);
	if(cadena==""){
		return "";
	}else{
	return cadena;
	}
}

function RTrim(VALUE){
	var w_space = String.fromCharCode(32);
	var v_length = VALUE.length;
	var strTemp = "";
	if(v_length < 0){
		return"";
	}
	var iTemp = v_length -1;

	while(iTemp > -1){
		if(VALUE.charAt(iTemp) == w_space){
	}else{
		strTemp = VALUE.substring(0,iTemp +1);
		break;
	}
	iTemp = iTemp-1;
	}
	return strTemp;
}

function LTrim(VALUE){
	var w_space = String.fromCharCode(32);
	if(v_length < 1){
		return"";
	}
	var v_length = VALUE.length;
	var strTemp = "";

	var iTemp = 0;

	while(iTemp < v_length){
		if(VALUE.charAt(iTemp) == w_space){
		}else{
			strTemp = VALUE.substring(iTemp,v_length);
			break;
		}
	iTemp = iTemp + 1;
	}
	return strTemp;
}

function recortar (cadena, max){
//	str=unescape(cadena);
	str=cadena.replace(/&#39;/gi, "'");
	str=Trim(str);
	if (str.length>max){
		document.write(str.substring(0,max-3) + "...");
	}else{
		if (str==""){
			document.write("&nbsp;");
		}else{
			document.write(str);
		}
	}	
}
function recortarScript (cadena, max){

	str=cadena.replace(/&#39;/gi, "'");
	str=Trim(str);
	if (str.length>max){
		return str.substring(0,max-3) + "...";
	}else{
		if (str==""){
			return "&nbsp;";
		}else{
			return str;
		}
	}	
}

function controlTPL(){
	
	cadena="<div align=\"justify\" style=\"border:solid 1pt #336600;position:absolute;left:110;top:120;width:400;height:100;background-color:#FFFFFF;padding-right:20pt;padding-top:5pt;padding-left:40pt\"><font class=\"textoERRORPagina\">" + controlTPL1 + "<br><BR></font><font class=\"textoNoDatosGris\">" + controlTPL2 + "</font></div>";
	cadena+="<div style=\"position:absolute;left:80;top:80;\"><img src=\"img/aviso_grande.gif\"></div>";
	cadena+="<div class=\"capaEspera\" align=center onClick=\"cerrarEspera('espera');\">" + continuarTPL + "</div>";
	
	  div1 = document.createElement("div");
  	  div1.setAttribute("id","espera");
  	  div1.innerHTML = cadena;
  	  div1.className = "capaBloqueoTPL";
  	  document.body.appendChild(div1);
  	  
	//Down();
	cmin2=minutosRestantes;
        csec2=segundosRestantes;
	document.all.tiempo.innerHTML=Display(cmin2,csec2);
}


function informeActTarjetas(){
	cadena ="<div class=\"textoCargando\" align=center style=\"position:absolute;left:0;top:10;width:630;Z-index:20\">INFORME DE ACTUALIZACIÓN DE TARJETAS</div>";
	cadena+="<div align=left style=\"position:absolute; left:605; top:40;height:18; width:17; z-index:100;background-color:#46891F\"><img src=\"img/dummi.gif\" heigth=18></div>";
	cadena+="<div style=\"position:absolute; left:10; top:40; height:18\">";
	cadena+="   <table border=0 cellspacing=0 cellpadding=0 width=595 bordercolor=red>";
	cadena+="      <tr>";
	cadena+="         <td class=\"columnaCabecera\" width=130 align=center>Tarjeta</td>";
	cadena+="         <td class=\"columnaCabecera\" width=90 align=center>Fecha asig.</td>";
	cadena+="         <td class=\"columnaCabecera\" width=90 align=center>Fecha actu.</td>";
	cadena+="         <td class=\"columnaCabecera\" width=80 align=center>Hora actu.</td>";
	cadena+="         <td class=\"columnaCabecera\" width=205 align=center>Descripción</td>";
	cadena+="      </tr>";   
	cadena+="   </table>";   
	cadena+="</div>";   
	cadena+="<div align=left class=\"scrollTabla\" style=\"border:solid 1pt #336600;position:absolute;left:10;top:60;width:612;height:260;background-color:#FFFFFF;padding-right:0pt;padding-top:0pt;padding-bottom:0pt;padding-left:0pt;overflow-y:scroll;\">";
	cadena+="   <table border=0 width=594 cellspacing=2 cellpadding=0 class=\"textoGris\" bordercolor=yellow>"; 
	cadena+=       informeTarjetas;
	cadena+="   </table>";
	cadena+="</div>";
	cadena+="<div class=\"capaEspera\" align=center onClick=\"cerrarEspera('informeTarjetas');\">";
	cadena+=   continuarTPL;
	cadena+="</div>";

  	div1 = document.createElement("div");
  	div1.setAttribute("id","informeTarjetas");
  	div1.innerHTML = cadena;
  	div1.className = "capaBloqueoTPL";
  	document.body.appendChild(div1);
}


function cerrarEspera(quien){

	eval("document.all." + quien + ".style.visibility='hidden'");
	//window.clearTimeout(down);
	lanzarMensaje();
	
}
function controlarNoDatos(){

	if (sinDatos){
	
		cadena="<TABLE border=0 cellspacing=0 cellpadding=0 width=100% heigth=100%>";
		cadena+="<TR><TD align=center height=" + altoNoDatos + " valign=middle class=\"textoNoDatos\">NO SE HAN ENCONTRADO DATOS</TD></TR></TABLE>";
		
		document.all.ocultarVacio.innerHTML=cadena;
		document.all.ocultarVacio.style.width=anchoNoDatos;
		document.all.ocultarVacio.style.height=altoNoDatos;
		document.all.ocultarVacio.style.left=xNoDatos;
		document.all.ocultarVacio.style.top=yNoDatos;
		document.all.ocultarVacio.style.visibility='visible';
		if (botones.length>0){
			for (i=0;i<=botones.length-1;i++){
				eval("document.all." + botones[i] + ".className='botonDisabled'");
			}
		}
	}

}
function controlarNoDatos2(){

	if (sinDatos){
	
		cadena="<TABLE border=0 cellspacing=0 cellpadding=0 width=100% heigth=100%>";
		cadena+="<TR><TD align=center height=" + altoNoDatos + " valign=middle class=\"textoNoDatos\">NO  QUEDAN  TARJETAS  DISPONIBLES  PARA  LA  FECHA  ASIGNADA  DE  LECTURA</TD></TR></TABLE>";
		
		document.all.ocultarVacio.innerHTML=cadena;
		document.all.ocultarVacio.style.width=anchoNoDatos;
		document.all.ocultarVacio.style.height=altoNoDatos;
		document.all.ocultarVacio.style.left=xNoDatos;
		document.all.ocultarVacio.style.top=yNoDatos;
		document.all.ocultarVacio.style.visibility='visible';
		if (botones.length>0){
			for (i=0;i<=botones.length-1;i++){
				eval("document.all." + botones[i] + ".className='botonDisabled'");
			}
		}
	}

}
function controlarError(msg1,msg2){
	anchoNoDatos=630;
	altoNoDatos=310;
	xNoDatos=5;
	yNoDatos=20;
    
    
	cadena="<TABLE border=0 cellspacing=0 cellpadding=0 width=100% heigth=100%>";
	cadena+="<TR><TD align=center height=" + altoNoDatos + " valign=middle><font class=\"textoERRORPagina\">" + msg1 + "<br><BR></font><font class=\"textoNoDatosGris\">" + msg2 + "</font></TD></TR></TABLE>";

		document.all.ocultarError.innerHTML=cadena;
		document.all.ocultarError.style.width=anchoNoDatos;
		document.all.ocultarError.style.height=altoNoDatos;
		document.all.ocultarError.style.left=xNoDatos;
		document.all.ocultarError.style.top=yNoDatos;
		document.all.ocultarError.style.visibility='visible';
		for (i=0;i<=botones.length-1;i++){
			eval("document.all." + botones[i] + ".className='botonDisabled'");
		}

}

function controlPagina(id,m1,m2){
	if (id==bEstadoERROR) controlarError(m1,m2);
	if (id==bEstadoOK) controlarNoDatos();	

	if (varControlTPL != "null" ){
		controlTPL();
	}else{
		lanzarMensaje();
	}
	if (informeTarjetas != "null" ){
		
		informeActTarjetas();
	}
	if (estadoAplicacion != "null" ){
		top.frames.location.href="FueraDeServicio.jsp";
	}
}

function controlPagina2(id,m1,m2){
	if (id==bEstadoERROR) controlarError(m1,m2);
	if (id==bEstadoOK) controlarNoDatos2();	
	
	if (varControlTPL != "null" ){
		controlTPL();
	}else{
		lanzarMensaje();
	}
	if (informeTarjetas != "null" ){
		informeActTarjetas();
	}
	if (estadoAplicacion != "null" ){
		top.frames.location.href="FueraDeServicio.jsp";
	}
}
function marcarPrimero(){

	if (!sinDatos){
		document.all.btn_1_1.click();
	}
}
function escribirTituloPopUp(titulo,codigo,anchoBarra){

		//escribe el titulo dentro de la página 
		cadena="<TABLE border=0 cellspacing=0 cellpadding=0 width="+anchoBarra+" heigth=17 style=\"position:absolute; left:10; top:5;\">";
		cadena+="<TR><TD align=left height=17 valign=middle class=\"txtTitulo\">";
		cadena+=titulo + "</TD>";
		cadena+="<TD align=right height=17 valign=middle class=\"txtTitulo\" width=100>";
		cadena+=codigo + "</TD></TR></TABLE>";
		document.write(cadena);
	
		//titulo de la ventana
		cadena="<TITLE>";			 	
		cadena+=codigo +" "+titulo + "</TITLE>";
		document.write(cadena);	
}	
function escribirTitulo(titulo,codigo){

		cadena="<TABLE border=0 cellspacing=0 cellpadding=0 width=630 heigth=17 style=\"position:absolute; left:5; top:0;\">";
		cadena+="<TR><TD align=left height=17 valign=middle class=\"txtTitulo\" id=\"idTituloPagina\">";
		cadena+=titulo + "</TD>";
		cadena+="<TD align=right height=17 valign=middle class=\"txtTitulo\" width=100>";
		cadena+=codigo + "</TD></TR></TABLE>";
		document.write(cadena);
}

function deshabilitarMenu(){
	anchoNoDatos=630;
	altoNoDatos=355;
	xNoDatos=5;
	yNoDatos=0;

	if (typeof(parent.frames.menu.ocultarOpciones).toString()!='undefined'){
		parent.frames.menu.ocultarOpciones.style.visibility='';
		parent.frames.menu.ocultarMarcas.style.visibility='';
		
		cadena="<TABLE border=0 cellspacing=0 cellpadding=0 width=100% heigth=100%>";
		cadena+="<TR><TD align=center height=" + altoNoDatos + " valign=middle><font class=\"textoCargando\">C A R G A N D O . . .<br></font><font class=\"textoNoDatosGris\">Espere un momento, por favor</font></TD></TR></TABLE>";

		document.all.cargando.innerHTML=cadena;
		document.all.cargando.style.width=anchoNoDatos;
		document.all.cargando.style.height=altoNoDatos;
		document.all.cargando.style.left=xNoDatos;
		document.all.cargando.style.top=yNoDatos;
		document.all.cargando.style.visibility='visible';		
	}
		
}

function escribirLogicEmpty(alto){

			document.write("<div id=\"ocultarVacio\" class=\"capaNoDatos\"></div>");
			sinDatos=true;
			if (bEstado==bEstadoOK) {
				altoNoDatos=alto;
			}
}

function lanzarMensaje() {
	if ((mensaje != "null") && (mensaje != "")) {
		alert(mensaje);
	}
}
function moverCabecera(){
	document.all.cabecera.scrollLeft=document.all.datos.scrollLeft;
}

function finContador(){

	document.location.href="\UsuConectAction.do";

}

//if (typeof(parent.frames.menu).toString()!='undefined'){
if (parent.frames.length>2){
	if (typeof(parent.frames.menu.ocultarOpciones).toString()!='undefined'){
		parent.frames.menu.ocultarOpciones.style.visibility='hidden';
		parent.frames.menu.ocultarMarcas.style.visibility='hidden';
	}
}                         