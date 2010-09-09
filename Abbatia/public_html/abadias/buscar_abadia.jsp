<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html:html>
    <head>
        <title>Pantalla muestra Impuestos</title>
        <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css">
        <link rel="stylesheet" href="/theme/popup.css" type="text/css">
    </head>

    <body topmargin="0" leftmargin="5" bgcolor="#E1C08B">

    <!-- Buscar abadia -->
    <html:form action="/buscar_abadia">
        <html:hidden property="busqueda" value="1" name="BuscarAbadiaForm"/>
        <input type="hidden" name="opcion" value="<bean:write name="BuscarAbadiaForm" property="opcion"/>"/>
        <table border="1" cellspacing="0" bordercolor="#000000" ordercolorlight="#000000" bordercolordark="#000000"
               cellpadding="2">
            <tr height="20">
                <td align="center" bgcolor="#996633" colspan="4">
                    <h2><span style="color: #FFFFFF; font-size: x-small; ">
                  <bean:message key="elecciones.candidatos.tabla.abadia"/>
                </span>
                    </h2>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E4CFA2">
                    <table border="0" width="100%">
                        <tr>
                            <td align="center">
                                <html:text property="search" name="BuscarAbadiaForm"/>
                            </td>
                        </tr>
                    </table>
                    <hr size="1" width="80%"/>
                    <logic:notEqual name="BuscarAbadiaForm" property="region" value="-1">
                        <bean:message key="registro.abadia.region"/> : <br>
                        <html:select name="BuscarAbadiaForm" property="region">
                            <html:options collection="regiones" property="id" labelProperty="description"/>
                        </html:select>
                        <hr size="1" width="80%"/>
                    </logic:notEqual>
                    <div style="text-align: center;">
                        <html:submit>
                            <bean:message key="mercado.compra.buscar"/>
                        </html:submit>
                    </div>
                </td>
            </tr>
        </table>
    </html:form>
    <br/>

    <!--- Ver todos -->
    <bean:write name="Navega" filter="false"/>

    <div align="center">
        <table border="1" cellspacing="0" bordercolor="#000000" width="90%" bordercolorlight="#000000"
               bordercolordark="#000000">
            <tr height="20">
                <td align="center" bgcolor="#996633">
                <span style="color: #FFFFFF; font-size: x-small; font-weight: bold;">
                  <bean:message key="diplomacia.abadia.obispo.selectAbadia"/>
                </span>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E4CFA2">
                    <table border="1" width="100%" cellspacing="0" bordercolorlight="#808080" bordercolor="#808080"
                           bordercolordark="#808080">
                        <logic:iterate id="contenido" name="BuscarAbadiaForm" property="contents">
                            <bean:write property="description" name="contenido" filter="false"/>
                        </logic:iterate>
                    </table>
                </td>
            </tr>
        </table>
    </div>


    <!-- Publicidad -->
    <logic:present name="usuario">
        <logic:notEqual property="registrado" name="usuario" scope="session" value="1">
            <jsp:include page="/varios/publicidad.jsp"/>
        </logic:notEqual>
    </logic:present>

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
