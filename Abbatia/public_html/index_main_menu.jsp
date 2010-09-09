<%@ taglib uri="/WEB-INF/tld/struts-nested.tld" prefix="nested" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<html:html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <link REL="STYLESHEET" HREF="theme/styles.css" TYPE="text/css">
        <html:base target="_self"/>
        <title></title>
    </head>


    <body topmargin="0" leftmargin="0" bgcolor="#E4CFA2">

    <center>
        <table border=0>
            <tr>
                <td><html:link action="/idioma?param=idioma&id=es" target="_top"><html:img src="images/iconos/16/es.gif"
                                                                                           border="0"
                                                                                           altKey="tooltip.idioma_es"/></html:link></td>
                <td><html:link action="/idioma?param=idioma&id=ct" target="_top"><html:img src="images/iconos/16/ct.gif"
                                                                                           border="0"
                                                                                           altKey="tooltip.idioma_ct"/></html:link></td>
                <td><html:link action="/idioma?param=idioma&id=gl" target="_top"><html:img src="images/iconos/16/gl.gif"
                                                                                           border="0"
                                                                                           altKey="tooltip.idioma_gl"/></html:link></td>
                <td><html:link action="/idioma?param=idioma&id=ek" target="_top"><html:img src="images/iconos/16/ek.gif"
                                                                                           border="0"
                                                                                           altKey="tooltip.idioma_ek"/></html:link></td>
                <td><html:link action="/idioma?param=idioma&id=lt" target="_top"><html:img src="images/iconos/16/vt.gif"
                                                                                           border="0"
                                                                                           altKey="tooltip.idioma_lt"/></html:link></td>
            </tr>
            <!--/table>
           <table border=0-->
            <tr>
                <td><html:link action="/idioma?param=idioma&id=us" target="_top"><html:img src="images/iconos/16/us.gif"
                                                                                           border="0"
                                                                                           altKey="tooltip.idioma_us"/></html:link></td>
                <td><html:link action="/idioma?param=idioma&id=fr" target="_top"><html:img src="images/iconos/16/fr.gif"
                                                                                           border="0"
                                                                                           altKey="tooltip.idioma_fr"/></html:link></td>
                <td><html:link action="/idioma?param=idioma&id=de" target="_top"><html:img src="images/iconos/16/de.gif"
                                                                                           border="0"
                                                                                           altKey="tooltip.idioma_de"/></html:link></td>
                <td><html:link action="/idioma?param=idioma&id=it" target="_top"><html:img src="images/iconos/16/it.gif"
                                                                                           border="0"
                                                                                           altKey="tooltip.idioma_it"/></html:link></td>
                <td><html:link action="/idioma?param=idioma&id=pt" target="_top"><html:img src="images/iconos/16/pt.gif"
                                                                                           border="0"
                                                                                           altKey="tooltip.idioma_pt"/></html:link></td>
            </tr>
        </table>
    </center>

    <p align="center">
        <font size="2">
            <html:link action="/index_main" target="principal">
                <html:img src="images/monjesentado.gif" border="0"/><br>
                <bean:message key="outlook.menu.novedades"/>
            </html:link>
        </font>
    </p>

    <hr size=1>

    <p align="center">
        <html:link action="/loginFrm" target="principal">
            <html:img src="images/monjepuerta.gif" border="0"/><br>
            <bean:message key="outlook.menu.login"/>
        </html:link>
    </p>

    <p align="center">
        <html:link action="/registro" target="principal">
            <html:img width="55" height="68" src="images/iconos/registro.gif" border="0"/><br>
            <bean:message key="outlook.menu.registrar"/>
        </html:link>
    </p>

    <p align="center">
        <html:link href="http://foros.abbatia.net" target="principal">
            <html:img src="images/foros.gif" border="0"/><br>
            <bean:message key="outlook.menu.foros"/>
        </html:link>
    </p>

    <!--
    <p align="center" >
     <form name=svoto action="http://www.juegaenred.com/svoto.php" method=post target="_blank" >
     <p class="v10px">Ayudanos a ser Nº1 en www.juegaenred.com<p class="v10px"><p><input type="hidden" name="codiusr" size="20" value=167165><input type="hidden" name="votoper" value="434">
     <p><input type="Submit" name="Votar" value="Ayudanos a ser Nº1 en www.juegaenred.com">
     </form>
    </p-->
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

</html:html>
