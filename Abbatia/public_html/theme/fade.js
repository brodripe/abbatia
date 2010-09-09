document.onmouseover = domouseover;
document.onmouseout = domouseout;

function domouseover() {
  if(document.all){
  srcElement = window.event.srcElement;
  if (srcElement.className.indexOf("txtMenuNivel2") > -1) {
        var linkName = srcElement.name;
      fadein(linkName);
      }
      }
}

function domouseout() {
  if (document.all){
  srcElement = window.event.srcElement;
  if (srcElement.className.indexOf("txtMenuNivel2") > -1) {
        var linkName = srcElement.name;
      fadeout(linkName);
      }
      }
}

function makearray(n) {
    this.length = n;
    for(var i = 1; i <= n; i++)
        this[i] = 0;
    return this;
}

hexa = new makearray(16);
for(var i = 0; i < 10; i++)
    hexa[i] = i;
hexa[10]="a"; hexa[11]="b"; hexa[12]="c";
hexa[13]="d"; hexa[14]="e"; hexa[15]="f";

function hex(i) {
    if (i < 0)
        return "00";
    else if (i > 255)
        return "ff";
    else
       return "" + hexa[Math.floor(i/16)] + hexa[i%16];}

function setbgColor(r, g, b, element) {
      var hr = hex(r); var hg = hex(g); var hb = hex(b);
      element.style.color = "#"+hr+hg+hb;
}

function fade(sr, sg, sb, er, eg, eb, step, direction, element){
    for(var i = 0; i <= step; i++) {
setTimeout("setbgColor(Math.floor(" +sr+ " *(( " +step+ " - " +i+ " )/ " +step+ " ) + " +er+ " * (" +i+ "/" +step+ ")),Math.floor(" +sg+ " * (( " +step+ " - " +i+ " )/ " +step+ " ) + " +eg+ " * (" +i+ "/" +step+ ")),Math.floor(" +sb+ " * ((" +step+ "-" +i+ ")/" +step+ ") + " +eb+ " * (" +i+ "/" +step+ ")),"+element+");",i*step);
    }
}


/*
fadein  -- fadeout
first three nrs fade to 2nd three nrs, then back again
colors are in RGB
*/

function fadein(element) {
	if (seleccionado2!=element){
		fade(70,137,31, 255,153,0, 10, 1, element);  
	}
}

function fadeout(element) {
	if (seleccionado2!=element){
     		fade(255,153,0, 70,137,31, 20, 1, element);
     	}
}
