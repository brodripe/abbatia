<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page pageEncoding="ISO-8859-1" contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html:html xhtml="http://www.w3.org/1999/xhtml">
    <head>
        <title><bean:message key="mensajes.nuevo.buscarabadia"/></title>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
        <link REL="STYLESHEET" HREF="/theme/style-global.css" TYPE="text/css"/>
        <link REL="STYLESHEET" HREF="/theme/style-revised.css" TYPE="text/css"/>
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

    <body>
    <div id="container">
        <div id="mercadoFiltro" class="alignCenter">
            <h4><bean:message key="mercado.venta.filtro"/></h4>
            <html:form action="/buscar_abadias">
                <html:hidden name="BuscarAbadiasForm" property="accion" value="buscar"/>
                <div class="floatLeft downSpace">
                    <ul>
                        <li><bean:message key="registro.abadia.region"/></li>
                        <li>
                            <html:select name="BuscarAbadiasForm" property="region">
                                <html:options collection="regiones" property="id" labelProperty="description"/>
                            </html:select>
                        </li>
                        <li><bean:message key="registro.abadia.orden"/></li>
                        <li>
                            <html:select name="BuscarAbadiasForm" property="orden">
                                <html:options collection="ordenes" property="id" labelProperty="description"/>
                            </html:select>
                        </li>
                        <li><bean:message key="buscar.abadia.nombreabadia"/></li>
                        <li><html:text name="BuscarAbadiasForm" property="nombre"/></li>
                    </ul>
                </div>
                <div class="alignCenter downSpace">
                    <ul>
                        <li>
                            <html:checkbox name="BuscarAbadiasForm" property="administradores">
                                <bean:message key="buscar.abadia.administradores"/>
                            </html:checkbox>
                        </li>
                        <li>
                            <html:checkbox name="BuscarAbadiasForm" property="cardenales">
                                <bean:message key="buscar.abadia.cardenales"/>
                            </html:checkbox>
                        </li>
                        <li>
                            <html:checkbox name="BuscarAbadiasForm" property="obispos">
                                <bean:message key="buscar.abadia.obispos"/>
                            </html:checkbox>
                        </li>
                    </ul>
                </div>
                <div class="alignCenter">
                    <ul>
                        <li>
                            <html:submit onclick="this.disabled=true; this.form.submit();">
                                <bean:message key="buscar.abadia.boton.buscar"/>
                            </html:submit>
                        </li>
                    </ul>
                </div>
            </html:form>
        </div>
        <div class="break"></div>
        <html:form action="/buscar_abadias">
            <html:hidden name="BuscarAbadiasForm" property="accion" value="aceptar"/>
            <display:table name="requestScope.BuscarAbadiasForm.listado" uid="lista_abadias" pagesize="30"
                           export="true" requestURI="/buscar_abadias.do"
                           sort="list" class="contenidoedificio alignCenter">
                <display:setProperty name="export.xsl.filename" value="lista_abadias.xsl"/>
                <display:setProperty name="export.pdf.filename" value="lista_abadias.pdf"/>
                <display:setProperty name="export.csv.filename" value="lista_abadias.csv"/>
                <display:caption><bean:message key="buscar.abadia.listado"/></display:caption>
                <display:column title="<input type='checkbox' name='CheckAll' onClick='checkOrUncheck()' />">
                    <html:multibox name="BuscarAbadiasForm" property="seleccion">
                        ${lista_abadias.idAbadia}
                    </html:multibox>
                </display:column>
                <display:column property="nombre_abadia" titleKey="buscar.abadia.nombreabadia" class="textLeft"/>
                <display:column property="nombre_region" titleKey="buscar.abadia.nombreregion" class="textLeft"/>
                <display:column property="nombre_orden" titleKey="buscar.abadia.nombreorden" class="textLeft"/>
            </display:table>
            <div class="alignCenter">
                <ul>
                    <li>
                        <html:submit onclick="this.disabled=true; this.form.submit();">
                            <bean:message key="buscar.abadia.boton.aceptar"/>
                        </html:submit>
                    </li>
                </ul>
            </div>
        </html:form>
        <div class="break"></div>
            <%-- Publicidad --%>
        <logic:notEqual property="registrado" name="usuario" scope="session" value="1">
            <jsp:include page="/varios/publicidad.jsp"/>
        </logic:notEqual>

    </div>
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
</html:html>


<%--

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
                                <html:checkbox name="BuscarAbadiasForm" property="administradores">
                                    <bean:message key="buscar.abadia.administradores"/></html:checkbox>
                                <html:checkbox name="BuscarAbadiasForm" property="cardenales">
                                    <bean:message key="buscar.abadia.cardenales"/></html:checkbox>
                                <html:checkbox name="BuscarAbadiasForm" property="obispos">
                                    <bean:message key="buscar.abadia.obispos"/></html:checkbox>
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
</center>--%>
<%--

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
</html>--%>
