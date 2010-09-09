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
        <link REL="STYLESHEET" HREF="theme/styles.css" TYPE="text/css">
    </head>

    <body topmargin="0" leftmargin="5" bgcolor="#E1C08B">

    <!--- Ver todos -->

    <div align="center">
        <bean:write name="Navega" filter="false"/>
        <table border="1" cellspacing="0" bordercolor="#000000" width="90%" bordercolorlight="#000000"
               bordercolordark="#000000">
            <tr height="20">
                <td align="center" bgcolor="#996633">
                    <b>
                        <font color="#FFFFFF" size="2">
                            <bean:message key="obispado.mostrar.comisiones.titulo"/>
                        </font>
                    </b>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E4CFA2">
                    <table border="1" width="100%" cellspacing="0" bordercolorlight="#808080" bordercolor="#808080"
                           bordercolordark="#808080">
                        <logic:iterate id="contenido" name="MostrarComisiones" property="contents">
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
</html:html>
