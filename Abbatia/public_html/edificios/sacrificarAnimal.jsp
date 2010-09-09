<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>


<html>
<head>
    <title>Pantalla mercado venta</title>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css">
</head>

<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">

<p>&nbsp;</p>
<html:form action="/sacrificar_animal_confirmacion">
    <html:hidden name="SacrificioForm" property="id"/>

    <DIV align="center">

        <table border="1" cellspacing="0" bordercolor="#000000" width="80%" bordercolorlight="#000000"
               bordercolordark="#000000">

            <tr height="20">

                <td align="center" bgcolor="#996633">

                    <DIV align="center">

                        <b>
                            <font color="#FFFFFF" size="2">
                                <bean:message key="edificio.abadia.sacrificar.tabla"/>
                            </font>
                        </b>

                    </DIV>

                </td>

            </tr>

            <tr>

                <td bgcolor="#E4CFA2">

                    <DIV align="center">

                        <table border="0" width="100%">

                            <tr>

                                <td width="100%">

                                    <p align="center">

                                        <b>
                                            <font size="2">
                                                <bean:message key="edificio.abadia.sacrificar.texto1"/>
                                                <bean:message key="edificio.abadia.sacrificar.texto2"/>
                                                <bean:write property="alimento_min" name="SacrificioForm"/>
                                                <bean:message key="edificio.abadia.sacrificar.texto3"/>
                                                <bean:write property="alimento_max" name="SacrificioForm"/>
                                                <bean:write property="unidad_alimento" name="SacrificioForm"/>
                                                <bean:message key="edificio.abadia.sacrificar.texto4"/>
                                                <bean:write property="alimento_desc" name="SacrificioForm"/>
                                                <bean:message key="edificio.abadia.sacrificar.texto5"/>
                                                <bean:message key="edificio.abadia.sacrificar.texto6"/><br/>
                                                <bean:message key="edificio.abadia.sacrificar.texto7"/>
                                                <html:text property="precio" name="SacrificioForm" size="3"/><br/>
                                                <bean:message key="edificio.abadia.sacrificar.texto8"/>
                                                <html:text property="numAnimales" name="SacrificioForm" size="3"/>
                                                <logic:notEqual value="0" property="recurso_id" name="SacrificioForm">
                                                    <br/>
                                                    <bean:message key="edificio.abadia.sacrificar.texto9"/> <bean:write
                                                        name="SacrificioForm" property="recurso_min"/>
                                                    <bean:message key="edificio.abadia.sacrificar.texto10"/> <bean:write
                                                        name="SacrificioForm" property="recurso_max"/> <bean:write
                                                        name="SacrificioForm" property="unidad_recurso"/>
                                                    <bean:message key="edificio.abadia.sacrificar.texto11"/> <bean:write
                                                        name="SacrificioForm" property="recurso_desc"/>
                                                </logic:notEqual>
                                                <br/><bean:message key="edificio.abadia.sacrificar.texto12"/>
                                            </font>
                                        </b>

                                    </p>

                                </td>

                            </tr>

                        </table>

                    </DIV>

                </td>

            </tr>

            <tr>

                <td>

                    <p align="center">

                        <input type="button" onClick="window.parent.cClick();" value="Atrás"/>&nbsp;

                        <html:submit value="Sacrificar"/>

                    </p>

                </td>

            </tr>

        </table>

    </DIV>
</html:form>
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
