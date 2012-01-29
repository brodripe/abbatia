<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<html>
<head>
    <link REL="STYLESHEET" HREF="theme/styles.css" TYPE="text/css"/>
    <script language="JavaScript1.2">
        function over_effect(e, state) {
            if (document.all)
                source4 = event.srcElement
            else if (document.getElementById)
                source4 = e.target
            if (source4.className == "menu")
                source4.style.borderStyle = state
            else {
                while (source4.tagName != "TABLE") {
                    source4 = document.getElementById ? source4.parentNode : source4.parentElement
                    if (source4.className == "menu") {
                        source4.style.borderStyle = state
                        if (state == "solid")
                            source4.style.background = "#e4cfa2"
                        else
                            source4.style.background = "#B6BDD2"
                    }
                }
            }
        }
    </script>
</head>

<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<p align="center">
    <logic:notEmpty name="DatosContents">
<table border="1" width="200" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000"
       bordercolordark="#000000">
    <tr>
        <td align="center" bgcolor="#F0AF1C">
            <font color="#000000" size="1"><bean:message key="principal.poblacion"/></font>
        </td>
    </tr>
    <tr>
        <td bgcolor="#F7FEEA" align="center">
            <table border="0" width="100%">
                <tr>
                    <td width="50%" align="center">
                        <img border="0" src="images/iconos/16/aldeanos.gif"
                             alt="<bean:message key="principal.aldeanos"/>"/>
                    </td>
                    <td width="50%" align="right">
                        <bean:write name="DatosContents" property="contadorAldeanos" filter="false"/> <bean:message
                            key="principal.aldeanos"/>
                        <bean:write name="DatosContents" property="recursoMonedas" filter="false"/>
                    </td>
                <tr>
            </table>
        </td>
    </tr>
</table>
</logic:notEmpty>
</p>
<center>
    <!-- Opciones de la población -->
    <table border="1" cellspacing="0" bordercolor="#000000" width="90%" bordercolorlight="#000000"
           bordercolordark="#000000">
        <tr height="20">
            <td align="center" style="border-bottom: none thin white" bgcolor="#E4CFA2">
                <b><font size="2">
                    <bean:message key="poblacion.opcion.titulo"/>
                </font>
                </b>
            </td>
        </tr>
        <tr>
            <td bgcolor="#E4CFA2" colspan=3 style="border-top: none thin white;" align="center">
                <br/>
                <table border="1" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000"
                       bordercolordark="#000000" onMouseover="over_effect(event,'outset')"
                       onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')"
                       onMouseup="over_effect(event,'outset')">
                    <tr>
                        <td class="menu" width="90" align="center" valign="bottom">
                            <logic:equal value="S" name="puede_contratar">
                                <html:link action="/contratarSicario">
                                    <html:img border="0" page="/images/iconos/asesino.gif"/>
                                    <br/>
                                    <bean:message key="poblacion.opcion.contratar_asesino"/>
                                </html:link>
                            </logic:equal>
                            <logic:equal value="N" name="puede_contratar">
                                <html:img border="0" page="/images/iconos/asesino_disabled.gif"/>
                                <br/>
                                <bean:message key="poblacion.opcion.contratar_asesino"/> <br/>
                            </logic:equal>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <logic:equal value="N" name="puede_contratar">
            <tr>
                <td align="center">
                    <bean:message key="poblacion.opcion.puntiacion_minima"/> <b><bean:write name="minimo_puntos"/></b>
                </td>
            </tr>
        </logic:equal>
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
