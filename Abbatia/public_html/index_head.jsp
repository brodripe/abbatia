<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<html>


<script language="javascript">
    var fecha
    var CronoID = null
    var browserName = navigator.appName;

    function InicializarCrono() {

        if (fecha == null) {
            fecha = new Date();
            fecha.setYear(<bean:write name="Tiempo" property="year" />);
            fecha.setMonth(<bean:write name="Tiempo" property="mes" />);
            fecha.setDate(<bean:write name="Tiempo" property="dia" />);
            fecha.setHours(<bean:write name="Tiempo" property="hora" />);
            fecha.setMinutes(<bean:write name="Tiempo" property="minuto" />);
            fecha.setSeconds(0);
        }
        MostrarCrono();
    }

    function MostrarCrono() {


        //configura la salida
        anyo = fecha.getYear();
        mes = fecha.getMonth() + 1;
        dia = fecha.getDate();

        hora = fecha.getHours();
        minutos = fecha.getMinutes();
        if (browserName == "Microsoft Internet Explorer") {
            anyo = fecha.getYear();
        } else {
            anyo = <bean:write name="Tiempo" property="year" />;
        }


        if (hora < 10) {
            hora = "0" + hora;
        }
        if (minutos < 10) {
            minutos = "0" + minutos;
        }
        if (mes < 10) {
            mes = "0" + mes;
        }
        if (dia < 10) {
            dia = " " + dia;
        }


        var ValorCrono = dia + "-" + mes + "-" + anyo + " " + hora + ":" + minutos;

        document.getElementById("reloj").innerHTML = ValorCrono;

        fecha.setMinutes(fecha.getMinutes() + 1);

        CronoID = setTimeout("InicializarCrono ()", 5000);
    }

</script>


<head>
    <base target="principal"/>
    <link REL="STYLESHEET" HREF="theme/styles.css" TYPE="text/css">
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="InicializarCrono();">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
    <tr>
        <td width="140" height="100">
            <!-- Publicidad -->
            <img STYLE="position:absolute;top:0; left:0;" border="0" src="images/head-frame-left.gif"/>
            <img STYLE="position:absolute;top:0; left:20;" SRC="images/underconstructionchurch.jpg"/>
        </td>
        <td width="300" height="100" background="images/head-frame-bot.gif">
        </td>
        <td background="images/head-frame-bot.gif" align="center">
            <form action="https://www.paypal.com/cgi-bin/webscr" method="post">
                <input type="hidden" name="cmd" value="_s-xclick">
                <input type="hidden" name="hosted_button_id" value="9756248">
                <input type="image" src="http://www.abbatia.net/images/iconos/x-click-but04.gif" border="0"
                       name="submit" alt="PayPal. La forma rápida y segura de pagar en Internet.">
                <img alt="" border="0" src="https://www.paypal.com/es_ES/i/scr/pixel.gif" width="1" height="1">
            </form>
        </td>
        <td width="700" height="100" background="images/head-frame-bot.gif" align="center">
            <a href="index_main.do" target="principal">
                <img border="0" src="images/titulo_mejorado.gif"/>
            </a>
        </td>
        <td align="left" valign="bottom" background="images/head-frame-bot.gif" width="177">
            <br/>
            <table width="100%" border="0" cellspacing="3" bordercolor="#000000" bordercolorlight="#000000"
                   bordercolordark="#000000">
                <tr>
                    <td align="center">
                        <img alt="" src="images/Tiempo/relojarena.gif"/>
                    </td>
                    <td align="center">
                        <font size="2"><B>
                            <bean:message name="Tiempo" property="diaSemana"/>
                        </B>
                            <br/>

                            <div id="reloj">calculando...</div>
                        </font>
                    </td>
                </tr>
                <tr>
                    <td colspan=2>
                        &nbsp;
                    </td>
                </tr>
            </table>
        </td>
        <!-- manual -->
        <td align="center" valign="bottom" background="images/head-frame-bot.gif" width="161">
            <a href="/ayudas.do" target="principal">
                <img border="0" src="images/iconos/manual.gif"><br/><b><bean:message key="index.ayudas"/></b>
            </a><br/>&nbsp;<br/>&nbsp;
        </td>
    </tr>
</table>
<!--Inicio Script para google-analytics-->
<script type="text/javascript">
    var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    document.write(unescape("%3Cscript src='" + gaJsHost +
                            "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
    var pageTracker = _gat._getTracker("UA-302272-1");
    pageTracker._initData();
    pageTracker._trackPageview();
</script>
<!--Fin Script para google-analytics-->

</body>
</html>
