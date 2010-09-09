//function controlCaracteres(maximo,nombreArea){
	
//	var teclaPulsada = String.fromCharCode(event.keyCode);
//		temp=teclaPulsada.charCodeAt();
//		x=eval("document.all." + nombreArea + ".value.length");
//		if ((x>=maximo) && (temp!=8) && (temp!=35) && (temp!=36) && (temp!=17) && (temp!=20) && (temp!=9)){
//			event.keyCode=0; event.returnCode=false;
//		}
//}

/**
 * DHTML script para el control de caracteres de un textbox (IE4+) script
 */

function taLimit() {
	var taObj=event.srcElement;
	if (taObj.value.length==taObj.maxLength*1) return false;
}

function taCount(visCnt) { 
	var taObj=event.srcElement;
	if (taObj.value.length>taObj.maxLength*1) taObj.value=taObj.value.substring(0,taObj.maxLength*1);
	if (visCnt) visCnt.innerText=taObj.maxLength-taObj.value.length;
}
