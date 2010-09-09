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
