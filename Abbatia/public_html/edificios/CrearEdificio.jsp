<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title><bean:message key="edificios.abadia.construir.titulo"/></title>
    <link REL="STYLESHEET" HREF="/theme/style-global.css" TYPE="text/css"/>
    <link REL="STYLESHEET" HREF="/theme/style-revised.css" TYPE="text/css"/>
</head>
<body>
<div id="container">
    <logic:notEmpty name="Nombre">
        <p align="center">
            <bean:message key="mercado.historico.abadia"/>:<br/>
            <strong><bean:write name="Nombre"/></strong>
        </p>
    </logic:notEmpty>
    <div class="break"></div>
    <logic:iterate id="Edificio" name="Edificios">
        <div class="dataBuilding">
            <table>
                <thead>
                <tr>
                    <th><bean:write name="Edificio" property="nombre_edificio"/></th>
                </tr>
                </thead>
                <tbody>
                <tr class="image">
                    <td>
                        <p class="textCenter">
                            <img border="0"
                                 src="images/mapas/<bean:write name='Edificio' property='grafico_construccion'/>_1.gif"
                                 alt="<bean:write name='Edificio' property='nombre_edificio'/>"/> <br/> <br/>
                            <bean:write name="Edificio" property="descripcion_edificio"/>
                        </p>
                    </td>
                </tr>
                <tr class="content">
                    <td>
                        <dl>
                            <dt><bean:message key="edificios.abadia.tiempocontruccion"/></dt>
                            <dd><bean:write name="Edificio" property="dias_costruccion"/> <bean:message
                                    key="edificios.abadia.dias"/></dd>
                            <dt><bean:message key="edificios.abadia.costeoro"/></dt>
                            <dd><bean:write name="Edificio" property="coste_oro"/></dd>
                            <dt><bean:message key="edificios.abadia.costemadera"/></dt>
                            <dd><bean:write name="Edificio" property="coste_madera"/></dd>
                            <dt><bean:message key="edificios.abadia.costepiedra"/></dt>
                            <dd><bean:write name="Edificio" property="coste_piedra"/></dd>
                            <dt><bean:message key="edificios.abadia.costehierro"/></dt>
                            <dd><bean:write name="Edificio" property="coste_hierro"/></dd>
                        </dl>
                    </td>
                </tr>
                </tbody>
                <tfoot>
                <tr>
                    <td>
                        <logic:empty name="Abadiaid_Obispado">
                            <logic:equal name="Edificio" property="existe" value="0">
                                <html:link action="/crear_edificio" paramId="clave" paramName="Edificio"
                                           paramProperty="tipo_edificio">
                                    <b><bean:message key="edificios.abadia.construir"/></b>
                                </html:link>
                            </logic:equal>
                            <logic:equal name="Edificio" property="existe" value="1">
                                <bean:message key="edificios.abadia.construir.yalotienes"/>
                                <logic:notEqual name="Edificio" property="siguienteNivel" value="0">
                                    <br/>
                                    <html:link action="/SubirNivel" paramId="clave" paramName="Edificio"
                                               paramProperty="edificioid">
                                        <b><bean:message key="edificios.abadia.subirnivel"/>&nbsp;<bean:write
                                                name="Edificio" property="siguienteNivel"/></b>
                                    </html:link>
                                </logic:notEqual>
                            </logic:equal>
                        </logic:empty>
                        <logic:notEmpty name="Abadiaid_Obispado">
                            <logic:equal name="Edificio" property="existe" value="0">
                                <a href="crear_edificio.do?clave=<bean:write name="Edificio" property="tipo_edificio" />&abadiaid_obispado=<bean:write name="Abadiaid_Obispado" />">
                                    <b><bean:message key="edificios.abadia.construir"/></b>
                                </a>
                            </logic:equal>
                            <logic:equal name="Edificio" property="existe" value="1">
                                <bean:message key="edificios.abadia.construir.yalotienes"/>
                                <logic:notEqual name="Edificio" property="siguienteNivel" value="0">
                                    <br/>
                                    <a href="SubirNivel.do?clave=<bean:write name="Edificio" property="edificioid" />&abadiaid_obispado=<bean:write name="Abadiaid_Obispado" />">
                                        <b><bean:message key="edificios.abadia.subirnivel"/>&nbsp;<bean:write
                                                name="Edificio" property="siguienteNivel"/></b>
                                    </a>
                                </logic:notEqual>
                            </logic:equal>
                        </logic:notEmpty>
                    </td>
                </tr>
                </tfoot>

            </table>
        </div>
    </logic:iterate>
</div>
<div id="container">
    <!-- Publicidad -->
    <logic:present name="usuario" scope="session">
        <logic:notEqual property="registrado" name="usuario" scope="session" value="1">
            <jsp:include page="/varios/publicidad.jsp"/>
        </logic:notEqual>
    </logic:present>
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
<jsp:include page="/abadias/congelado.jsp"/>
</html>

