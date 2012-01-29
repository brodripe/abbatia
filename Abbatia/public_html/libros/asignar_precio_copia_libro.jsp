<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <title>
        <bean:message key="edificios.abadia.biblioteca.asignarprecio"/> <bean:write name="libroDetalle"
                                                                                    property="nombreLibro"/>
    </title>
    <link REL="STYLESHEET" HREF="/theme/style-global.css" TYPE="text/css"/>
    <link REL="STYLESHEET" HREF="/theme/style-revised.css" TYPE="text/css"/>
</head>
<body>
<div id="container">
    <div id="edificioHeader" style="width: 100%;">
        <html:form action="/actualizarPrecioCopia.do" styleClass="alignCenter">
            <html:hidden property="accion" value="actualizar"/>
            <html:hidden property="idLibro"/>

            <div class="alignCenter">
                <ul>
                    <li>
                        <h4><bean:message key="edificios.abadia.biblioteca.asignarprecio"/>
                            <strong><bean:write name="libroDetalle" property="nombreLibro"/></strong></h4>
                    </li>
                </ul>
                <ul>
                    <li><bean:message key="edificios.abadia.biblioteca.precio"/>
                        <bean:write name="libroDetalle" property="precioMin"/>
                        <bean:message key="edificio.abadia.sacrificar.texto3"/>
                        <bean:write name="libroDetalle" property="precioMax"/>
                        <bean:message key="sicario.confirmar.precio.monedas"/>
                    </li>
                </ul>
            </div>
            <div class="alignCenter topLineBorder">
                <ul>
                    <li>
                        <html:text property="precioCopiaS" name="libroDetalle" size="5" styleClass="textRight"/>
                    </li>
                </ul>
            </div>
            <div class="alignCenter topLineBorder">
                <ul>
                    <li><input type="button" onClick="window.parent.cClick();" value="Atrás"/>
                        <html:submit>
                            <bean:message key="edificios.abadia.biblioteca.actualizarprecio.aceptar"/>
                        </html:submit>
                    </li>
                </ul>
            </div>
        </html:form>
        <div class="row">
            <P class="rowRed"><html:errors/></P>
        </div>
    </div>
</div>


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
