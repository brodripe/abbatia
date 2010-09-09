<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <title>untitled</title>
    <link REL="STYLESHEET" HREF="theme/styles.css" TYPE="text/css"/>
</head>

<SCRIPT LANGUAGE="JavaScript">

    function checkOrUncheck()
    {
        if (document.BuscarAbadiasForm[1].CheckAll.checked)
            checkAll(document.BuscarAbadiasForm[1].seleccion);
        else
            uncheckAll(document.BuscarAbadiasForm[1].seleccion);
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


<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<center>
    <html:form action="/buscar_abadias">
        <html:hidden name="BuscarAbadiasForm" property="accion" value="buscar"/>
        <table border="1" cellspacing="0" bordercolor="#000000" width="60%" bordercolorlight="#000000"
               bordercolordark="#000000">
            <tr height="20">
                <td align="center" bgcolor="#996633" colspan="4">
                    <b>
                        <font color="#FFFFFF" size="2">
                            <bean:message key="buscar.abadia.titulo"/>
                        </font>
                    </b>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E4CFA2">
                    <table border="0" width="100%">
                        <tr align="center">
                            <td>
                                <bean:message key="registro.abadia.region"/>
                            </td>
                            <td>
                                <html:select name="BuscarAbadiasForm" property="region">
                                    <html:options collection="regiones" property="id" labelProperty="description"/>
                                </html:select>
                            </td>
                            <td>
                                <bean:message key="registro.abadia.orden"/>
                            </td>
                            <td>
                                <html:select name="BuscarAbadiasForm" property="orden">
                                    <html:options collection="ordenes" property="id" labelProperty="description"/>
                                </html:select>
                            </td>
                        </tr>
                        <tr align="center">
                            <td>
                                <bean:message key="buscar.abadia.nombreabadia"/>
                            </td>
                            <td>
                                <html:text name="BuscarAbadiasForm" property="nombre"/>
                            </td>
                            <td colspan="2" align="left">
                                <bean:message key="buscar.abadia.administradores"/>
                                <html:checkbox name="BuscarAbadiasForm" property="administradores"/>
                            </td>
                        </tr>
                    </table>
                    <center>
                        <hr size="1" width="80%"/>
                        <html:submit>
                            <bean:message key="buscar.abadia.boton.buscar"/>
                        </html:submit>
                    </center>
                </td>
            </tr>
        </table>
    </html:form>
    <br/>

    <div align="center">
        <table border="1" cellspacing="0" bordercolor="#000000" width="50%" bordercolorlight="#000000"
               bordercolordark="#000000">
            <tr height="20">
                <td align="center" bgcolor="#996633">
                    <b>
                        <font color="#FFFFFF" size="2">
                            <bean:message key="buscar.abadia.listado"/>
                        </font>
                    </b>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E4CFA2" width="100%">
                    <logic:notEmpty name="BuscarAbadiasForm" property="listado">
                        <html:form action="/buscar_abadias">
                            <html:hidden name="BuscarAbadiasForm" property="accion" value="aceptar"/>
                            <table border="1" width="100%" cellspacing="0" bordercolorlight="#808080"
                                   bordercolor="#808080"
                                   bordercolordark="#808080">
                                <tr>
                                    <td bgcolor="#E1C08B" align="center" width="10%">
                                        <input type="checkbox" name="CheckAll" value="Check All"
                                               onClick="checkOrUncheck()">
                                    </td>
                                    <td bgcolor="#E1C08B" align="center" width="30%"><b><font color="#FFFFFF">
                                        <bean:message key="buscar.abadia.nombreabadia"/>
                                    </font>
                                    </b>
                                    </td>
                                    <td bgcolor="#E1C08B" align="center" width="30%"><b><font color="#FFFFFF">
                                        <bean:message key="buscar.abadia.nombreregion"/>
                                    </font>
                                    </b>
                                    </td>
                                    <td bgcolor="#E1C08B" align="center" width="30%"><b><font color="#FFFFFF">
                                        <bean:message key="buscar.abadia.nombreorden"/>
                                    </font>
                                    </b>
                                    </td>
                                </tr>
                                <logic:iterate id="lista" name="BuscarAbadiasForm" property="listado">
                                    <tr>
                                        <td align="center" width="10%">
                                            <html:multibox name="BuscarAbadiasForm" property="seleccion">
                                                <bean:write name="lista" property="idAbadia"/>
                                            </html:multibox>
                                            <br/>
                                        </td>
                                        <td width="30%">
                                            <bean:write property="nombre_abadia" name="lista"/>
                                            <br/>
                                        </td>
                                        <td width="30%">
                                            <bean:write property="nombre_region" name="lista"/>
                                            <br/>
                                        </td>
                                        <td width="30%">
                                            <bean:write property="nombre_orden" name="lista"/>
                                            <br/>
                                        </td>
                                    </tr>
                                </logic:iterate>
                                <tr>
                                    <td align="center" bgcolor="#E1C08B" colspan="4">
                                        <input type="button" onclick="javascript:history.back();" value="Atrás"/>
                                        <html:submit>
                                            <bean:message key="buscar.abadia.boton.aceptar"/>
                                        </html:submit>
                                    </td>
                                </tr>
                            </table>
                        </html:form>
                    </logic:notEmpty>
                </td>
            </tr>
        </table>
    </div>
</center>

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
</body>
</html>