<%@page language="java" %>
<%@taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<html>
<head>
    <link REL="STYLESHEET" HREF="../theme/styles.css" TYPE="text/css">
</head>
<body topmargin="2" leftmargin="4" bgcolor="#E1C08B" text="#000000">
<div align="center">
    <table borderColor="#000000" cellSpacing="0" borderColorDark="#000000" width="90%" borderColorLight="#000000"
           border="1">
        <tr>
            <td align="middle" bgColor="#996633">
                <p align="center"><b><font color="#ffffff" size="2">Soporte
                    económico</font></b></p>
            </td>
        </tr>
        <tr>
            <td bgColor="#e4cfa2">
                <p align="left"><font size="2"><b><u>¿Quien
                    soporta económicamente este proyecto?</u></b></font></p>
                <ul>
                    <li>
                        <p align="left"><font size="2">Benjamin
                            Rodriguez ( <font color="#808080">cuota mensual 30€</font> )</font></li>
                    <li>
                        <p align="left"><font size="2">John Lohmeyer ( <font color="#808080">cuota
                            mensual 30€</font> )</font></li>

                </ul>
                <p align="left">
                    <font size="2" color="#800000">
                        <b>Si quieres soportar el proyecto económicamente ya sea con una aportación manual o
                            extraordinaria...</b>
                    </font>
                </p>

                <p align="left">
                    <font size="2">
                        <center>
                            <b>Cuenta bancaria:</b>&nbsp;0128.6001.76.0100058534 ( Indicar "Benjamín Rodríguez", abbatia
                            y nick en el concepto )<br>
                            <b>IBAN para donaciones desde fuera de España:</b>&nbsp;ES6701286001760100058534
                        </center>
                    </font>
                </p>
                <p align="left">
                    <font size="2">
                        <center>
                            <form action="https://www.paypal.com/cgi-bin/webscr" method="post">
                                <input type="hidden" name="cmd" value="_s-xclick">
                                <input type="hidden" name="hosted_button_id" value="9756248">
                                <input type="image" src="http://www.abbatia.net/images/iconos/x-click-but04.gif"
                                       border="0" name="submit"
                                       alt="PayPal. La forma rápida y segura de pagar en Internet.">
                                <img alt="" border="0" src="https://www.paypal.com/es_ES/i/scr/pixel.gif" width="1"
                                     height="1">
                            </form>
                        </center>
                    </font>
                </p>
                <p align="left"><font size="2"><strong>Nota: </strong>A las
                    personas que nos soporten el proyecto económicamente se les quitará
                    la publicidad en el juego y se le añadira en esta página como soporter.</font></p>
            </td>
        </tr>
    </table>
</div>
&nbsp;
<center>
    <bean:write name="Navega" filter="false"/>
    <table borderColor="#000000" cellSpacing="0" borderColorDark="#000000" width="90%" borderColorLight="#000000"
           border="1">
        <tr>
            <td align="middle" bgColor="#996633">
                <p align="center">
                    <b>
                        <font color="#ffffff" size="2">
                            <bean:message key="supporters.titulo"/>
                        </font>
                    </b>
                </p>
            </td>
        </tr>
        <tr>
            <td bgColor="#e4cfa2">
                <table width="100%">
                    <logic:iterate id="lista" name="supporters">
                        <font size="2">
                            <bean:write property="description" name="lista" filter="false"/>
                        </font>
                    </logic:iterate>
                </table>
            </td>
        </tr>
    </table>
</center>
<%--Modulo de estadisticas de google--%>
<script type="text/javascript">
    var _gaq = _gaq || [];
    _gaq.push(['_setAccount', 'UA-302272-1']);
    _gaq.push(['_setDomainName', 'abbatia.net']);
    _gaq.push(['_trackPageview']);
    (function() {
        var ga = document.createElement('script');
        ga.type = 'text/javascript';
        ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(ga, s);
    })();
</script>
<!--Fin Script para google-analytics-->

</body>
</html>
