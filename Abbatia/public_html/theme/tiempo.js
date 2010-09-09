	var fecha
	var CronoID = null

	function InicializarCrono () {

		if (fecha == null) {
			fecha = new Date();
			fecha.setYear(<bean:write name="Tiempo" property="year" />);
			fecha.setMonth(<bean:write name="Tiempo" property="mes" />);
			fecha.setDate(<bean:write name="Tiempo" property="dia" />);
			fecha.setHours(<bean:write name="Tiempo" property="hora" />);
			fecha.setMinutes(<bean:write name="Tiempo" property="minuto" />);
			fecha.setSeconds(0);
		}
		MostrarCrono ();
	}

	function MostrarCrono () {


		//configura la salida
		minutos = fecha.getMinutes();

		anyo = fecha.getYear();
		mes = fecha.getMonth(); 
		dia = fecha.getDate();
		hora = fecha.getHours();
		minutos = fecha.getMinutes();

		if (hora < 10) {
			hora = "0" + hora;
		}

		if (minutos < 10) {
			minutos = "0" + minutos;
		}


         var ValorCrono = dia + "-" + mes + "-" + anyo + " " + hora + ":" + minutos;

	  document.getElementById("reloj").innerHTML= ValorCrono;

	  fecha.setMinutes(minutos + 1);

	  CronoID = setTimeout("InicializarCrono()", 5000);
          return true
	}