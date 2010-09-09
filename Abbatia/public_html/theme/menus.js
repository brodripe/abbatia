
function over_effect(e,state){
	if (document.all)
		source4=event.srcElement
	else if (document.getElementById)
		source4=e.target
	if (source4.className=="menu")
		source4.style.borderStyle=state
	else
	{
		while(source4.tagName!="TABLE"){
			source4=document.getElementById? source4.parentNode : source4.parentElement
			if (source4.className=="menu") {
				source4.style.borderStyle=state
			if (state=="solid")
				source4.style.background="#e4cfa2"
			else
			    source4.style.background="#B6BDD2"
			}
		}
	}
}
