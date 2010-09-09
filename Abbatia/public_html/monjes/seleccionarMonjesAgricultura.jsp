<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<SCRIPT LANGUAGE="JavaScript">

    function checkOrUncheck()
    {
        if (document.DatosCampoForm.CheckAll.checked)
            checkAll(document.DatosCampoForm.seleccion);
        else
            uncheckAll(document.DatosCampoForm.seleccion);
    }

    function checkAll(aCheck)
    {
        for (i = 0; i < aCheck.length; i++)
            aCheck[i].checked = true;
    }

    function uncheckAll(aCheck) {
        for (i = 0; i < aCheck.length; i++)
            aCheck[i].checked = false;
    }
</script>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <title>untitled</title>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css"/>
</head>
<body bgcolor="#E1C08B">
<center>
    <html:form action="/mostrarListaMonjesAgricultura">
        <html:hidden property="accion" value="seleccionado"/>
        <html:hidden property="idCampo"/>
        <table border="1" cellspacing="0" bordercolor="#000000" width="90%" bordercolorlight="#000000"
               bordercolordark="#000000">
            <tr height="20">
                <td align="center" bgcolor="#996633">
                    <b><font color="#FFFFFF" size="2">
                        <bean:message key="edificio.abadia.campo.seleccionmonjes"/>
                    </font>
                    </b>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E4CFA2" style="border-top: none thin white;">
                    <table border="1" width="100%" cellspacing="0" bordercolorlight="#808080" bordercolor="#808080"
                           bordercolordark="#808080">
                        <tr>
                            <td bgcolor="#E1C08B" align="center" colspan="5">
                                <b><font color="#FFFFFF">
                                    <bean:message key="edificio.abadia.campo.listamonjes"/>
                                </font>
                                </b>
                            </td>
                        </tr>
                        <tr>
                            <td align="center" bgcolor="#E1C08B">
                                <input type="checkbox" name="CheckAll" value="Check All" onClick="checkOrUncheck()">
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="edificio.abadia.biblioteca.seleccionmonje.nombre"/>
                                </font>
                                </b>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="edificio.abadia.campo.seleccionmonje.fe"/>
                                </font>
                                </b>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="edificio.abadia.campo.seleccionmonje.destreza"/>
                                </font>
                                </b>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="edificio.abadia.campo.seleccionmonje.fuerza"/>
                                </font>
                                </b>
                            </td>
                        </tr>

                        <logic:notEmpty name="DatosCampoForm" property="monjes">
                            <logic:iterate id="lista" name="DatosCampoForm" property="monjes">
                                <tr>
                                    <td align="center">
                                        <html:multibox name="DatosCampoForm" property="seleccion">
                                            <bean:write name="lista" property="idDeMonje"/>
                                        </html:multibox>
                                    </td>
                                    <td align="left">
                                        <bean:write name="lista" property="nombre"/> <bean:message
                                            key="monjes.abadia.de"/> <bean:write name="lista"
                                                                                 property="primerApellido"/>
                                    </td>
                                    <td align="center">
                                        <bean:write name="lista" property="barra_fe" filter="false"/>
                                    </td>
                                    <td align="center">
                                        <bean:write name="lista" property="barra_destreza" filter="false"/>
                                    </td>
                                    <td align="center">
                                        <bean:write name="lista" property="barra_fuerza" filter="false"/>
                                    </td>
                                </tr>
                            </logic:iterate>
                        </logic:notEmpty>
                        <logic:empty name="DatosCampoForm" property="monjes">
                            <tr>
                                <td align="center" colspan="5">
                                    <bean:message key="mensajes.aviso.notienesmonjes.agricultura"/>
                                </td>
                            </tr>
                        </logic:empty>
                    </table>
                </td>
            </tr>
            <tr>
                <td align="center" bgcolor="#E1C08B">
                    <input type="button" onclick="window.parent.cClick();" value="Atrás"/>
                    <logic:notEmpty name="DatosCampoForm" property="monjes">
                        <html:submit>
                            <bean:message key="edificios.abadia.campo.seleccion.monjes.asignar"/>
                        </html:submit>
                    </logic:notEmpty>
                </td>
            </tr>
            <tr>
                <td align="center">
                    <html:errors/>
                </td>
            </tr>
            <tr>
                <td align="center">
                    <logic:messagesPresent message="true">
                        <html:messages id="msg" message="true">
                            <bean:write name="msg"/><br/>
                        </html:messages>
                    </logic:messagesPresent>
                </td>
            </tr>
        </table>
    </html:form>
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
