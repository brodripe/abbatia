<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <title>
        <bean:message key="edificios.abadia.mostrar.titulo"/>
    </title>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css"/>
    <link rel="stylesheet" href="/theme/style-global.css" type="text/css"/>
    <link rel="stylesheet" href="/theme/style-revised.css" type="text/css"/>

</head>
<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<center>
    <div id="resources" class="alignCenter column" align="center">
        <jsp:include page="/varios/mostrar_recursos.jsp"/>
    </div>

    <logic:notEmpty name="Nombre">
        <p>
            <bean:message key="mercado.historico.abadia"/>:<br/>
            <b><bean:write name="Nombre"/></b></p>
    </logic:notEmpty>
    <table border="1" width="50%" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000"
           bordercolordark="#000000">
        <tr>
            <td align="center" bgcolor="#996633" colspan="2">
                <b><font color="#FFFFFF" size="2">
                    <bean:message key="edificios.abadia.coste"/>
                    <logic:notEmpty name="Nombre">
                        <br/><bean:message key="edificios.construccion.confirmar.cabecera2"/>&nbsp;<bean:write
                            name="Nombre"/>
                    </logic:notEmpty>
                </font>
                </b>
            </td>
        </tr>
        <tr>
            <td align="center" bgcolor="#E4CFA2">
                <img border="0" alt="<bean:write name="Edificio" property="descripcion"/>"
                     SRC="images/mapas/<bean:write name="Edificio" property="grafico_construccion"/>_<bean:write name="Edificio" property="nivel"/>.gif"/><br/><br>
                <b><bean:write name="Edificio" property="nombre" filter="false"/></b>

            </td>
            <td bgcolor="#E4CFA2">
                <logic:equal value="S" name="Edificio" property="enConstruccion">
                    <table border="0" width="100%">
                        <tr align="center">
                            <td colspan="2">
                                <b>
                                    <bean:message key="edificios.abadia.ampliando"/>
                                </b>
                            </td>
                        </tr>
                        <tr align="center">
                            <td>
                                <b>
                                    <bean:message key="edificios.abadia.fechaprevista"/>
                                </b>
                            </td>
                            <td>
                                <bean:write name="Edificio" property="fechaFinPrevista" filter="false"/>
                            </td>
                        </tr>
                    </table>
                </logic:equal>
                <logic:equal value="N" name="Edificio" property="enConstruccion">
                    <logic:present name="DatosNivel">
                        <table border="0" width="100%">
                            <tr>
                                <td>
                                    <b>
                                        <bean:message key="edificios.abadia.mostrar.nivel"/>
                                    </b>
                                </td>
                                <td>
                                    <bean:write name="DatosNivel" property="nivel" filter="false"/>
                                </td>
                                <td>
                                    <b>
                                        <bean:message key="edificios.abadia.mostrar.tiempo"/>
                                    </b>
                                </td>
                                <td>
                                    <bean:write name="DatosNivel" property="tiempo" filter="false"/>
                                    <bean:message key="edificios.abadia.dias"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <b>
                                        <bean:message key="edificios.abadia.mostrar.almacenamiento"/>
                                    </b>
                                </td>
                                <td>
                                    <bean:write name="DatosNivel" property="almacenamiento" filter="false"/>
                                </td>
                                <td colspan="2">
                                </td>
                            </tr>
                            <tr>
                                <td colspan="4" align="center">
                                    <hr size="1" width="80%">
                                </td>
                            </tr>
                            <logic:empty name="TotalOro">
                                <tr>
                                    <td>
                                        <b>
                                            <bean:message key="edificios.abadia.costeoro"/>
                                        </b>
                                    </td>
                                    <td>
                                        <bean:write name="DatosNivel" property="oro" filter="false"/>
                                    </td>
                                    <td>
                                        <b>
                                            <bean:message key="edificios.abadia.costemadera"/>
                                        </b>
                                    </td>
                                    <td>
                                        <bean:write name="DatosNivel" property="madera" filter="false"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <b>
                                            <bean:message key="edificios.abadia.costepiedra"/>
                                        </b>
                                    </td>
                                    <td>
                                        <bean:write name="DatosNivel" property="piedra" filter="false"/>
                                    </td>
                                    <td>
                                        <b>
                                            <bean:message key="edificios.abadia.costehierro"/>
                                        </b>
                                    </td>
                                    <td>
                                        <bean:write name="DatosNivel" property="hierro" filter="false"/>
                                    </td>
                                </tr>
                            </logic:empty>
                            <logic:notEmpty name="TotalOro">
                                <tr>
                                    <td colspan="4" align="center">
                                        <b>
                                            <bean:message key="edificios.abadia.costeoro"/>
                                        </b>&nbsp;
                                        <bean:write name="TotalOro" filter="false"/>
                                    </td>
                                </tr>
                            </logic:notEmpty>
                        </table>
                    </logic:present>
                </logic:equal>
            </TD>
        </TR>
    </table>

    <p>
        <logic:equal value="N" name="Edificio" property="enConstruccion">
            <logic:empty name="Abadiaid_Obispado">
                <html:link action="/SubirNivel?confirma=Y" paramId="clave" paramName="Edificio"
                           paramProperty="idDeEdificio">
                    <bean:message key="edificios.abadia.subirnivel"/>
                </html:link>
            </logic:empty>
            <logic:notEmpty name="Abadiaid_Obispado">
                <a href="SubirNivel.do?confirma=Y&clave=<bean:write name="Edificio" property="idDeEdificio" />&abadiaid_obispado=<bean:write name="Abadiaid_Obispado" />">
                    <b><bean:message key="edificios.abadia.subirnivel"/></b>
                </a>
            </logic:notEmpty>
        </logic:equal>
    </p>
</center>
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
