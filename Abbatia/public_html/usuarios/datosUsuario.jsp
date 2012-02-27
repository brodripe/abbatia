<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <title>
        <bean:message key="index.header"/>
    </title>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css"/>
    <link REL="STYLESHEET" HREF="/theme/style-global.css" TYPE="text/css"/>
    <link REL="STYLESHEET" HREF="/theme/style-revised.css" TYPE="text/css"/>
    <script type="text/javascript">
        function confirmarCongelacion() {
            return confirm('<bean:message key="usuario.opcion.congelar.confirmacion"/>');
        }
    </script>
</head>
<body bgcolor="#E1C08B">

<center>
<table border="1" cellspacing="0" bordercolor="#000000" width="90%" bordercolorlight="#000000"
       bordercolordark="#000000">
    <tr height="20">
        <td align="center" bgcolor="#E4CFA2" style="border-bottom: none thin white">
            <b><font size="2">
                <bean:message key="usuario.opciones"/>
            </font>
            </b>
        </td>
    </tr>
    <tr>
        <td bgcolor="#E4CFA2" style="border-top: none thin white;">
            <!-- Opciones del usuario -->
            <br/>
            <center>
                <table border="1" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000"
                       bordercolordark="#000000">
                    <tr>
                        <td width="90" align="center" valign="bottom">
                            <html:link action="/eliminar_abadia">
                                <html:img border="0" page="/images/iconos/borrar_abadia.gif"/>
                                <br/>
                                <bean:message key="outlook.menu.usuario.elimina"/>
                            </html:link>
                        </td>
                        <td width="90" align="center" valign="bottom">
                            <html:link action="/bloquear_abadia" onclick="return confirmarCongelacion()">
                                <html:img border="0" page="/images/iconos/abadia_congelar.gif"/> <br/>
                                <bean:message key="usuario.opcion.congelar"/>
                                &nbsp;<img src="/images/iconos/16/registrado.gif" border="0">
                            </html:link>
                        </td>
                    </tr>
                </table>
            </center>
        </td>
    </tr>
</table>
&nbsp;

<html:form action="/actRegistro">
    <html:hidden property="email"/>
    <html:hidden property="nick"/>
<table width="64%" border="1" align="center" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000"
       bordercolordark="#000000">
    <tr>
        <td colspan="2" align="center" bgcolor="#996633">
            <b>
                <font color="#FFFFFF" size="2">
                    <bean:message key="usuario.titulo"/>
                </font>
            </b>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <table border="0" align="center" bgcolor="#E4CFA2" width="100%">
                <tr>
                    <td width="39%">
                        <div align="right">
                            <bean:message key="usuario.nombre"/>
                        </div>
                    </td>
                    <td width="61%">
                        <html:text property="nombre" altKey="usuario.nombrealt" maxlength="10" name="RegistroForm"
                                   size="15"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div align="right">
                            <bean:message key="usuario.apellido1"/>
                        </div>
                    </td>
                    <td>
                        <html:text maxlength="15" property="apellido1" alt="<bean:message key='usuario.apellido1alt'/>"
                                   name="RegistroForm" size="20"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div align="right">
                            <bean:message key="usuario.apellido2"/>
                        </div>
                    </td>
                    <td>
                        <html:text maxlength="15" property="apellido2" alt="<bean:message key='usuario.apellido2alt'/>"
                                   name="RegistroForm" size="20"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div align="right">
                            <bean:message key="usuario.edad"/>
                        </div>
                    </td>
                    <td>
                        <html:select name="RegistroForm" property="edad">
                            <html:options collection="edades" property="id" labelProperty="description"/>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div align="right">
                            <bean:message key="usuario.sexo"/>
                        </div>
                    </td>
                    <td>
                        <html:select name="RegistroForm" property="sexo">
                            <html:options collection="sexo" property="id" labelProperty="description"/>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div align="right">
                            <bean:message key="usuario.pais"/>
                        </div>
                    </td>
                    <td>
                        <html:select name="RegistroForm" property="pais">
                            <html:options collection="paises" property="id" labelProperty="description"/>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div align="right">
                            <bean:message key="usuario.alias"/>
                        </div>
                    </td>
                    <td>
                        <bean:write property="nick" name="RegistroForm"/>
                        <!--html:text maxlength="10" property="nick" alt="Alias para el juego" name="RegistroForm" size="10"/-->
                    </td>
                </tr>
                <tr>
                    <td>
                        <div align="right">
                            <bean:message key="usuario.email"/>
                        </div>
                    </td>
                    <td>
                        <bean:write property="email" name="RegistroForm"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div align="right">
                            <bean:message key="usuario.pwd"/>
                        </div>
                    </td>
                    <td>
                        <html:password property="pwd" maxlength="15" name="RegistroForm" redisplay="true" size="15"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div align="right">
                            <bean:message key="usuario.pwd2"/>
                        </div>
                    </td>
                    <td>
                        <html:password property="pwd2" maxlength="15" name="RegistroForm" redisplay="true" size="15"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div align="right">
                            <bean:message key="usuario.idioma"/>
                        </div>
                    </td>
                    <td>
                        <html:select property="idioma" name="RegistroForm">
                            <html:options collection="languages" property="id" labelProperty="description"/>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <hr size="1" width="80%"/>
                    </td>
                </tr>
                <tr>
                    <TD align="right">
                        <html:submit>
                            <bean:message key="usuario.actualizar"/>
                        </html:submit>
                    </TD>
                    <td>
                        <html:reset>
                            <bean:message key="usuario.reset"/>
                        </html:reset>
                    </td>
                </tr>
                <tr align="center">
                    <td colspan="2">
                        <html:errors/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<center>
    </html:form>
    <div id="container">
        <!-- Publicidad -->
        <logic:present name="usuario" scope="session">
            <logic:notEqual property="registrado" name="usuario" scope="session" value="1">
                <jsp:include page="/varios/publicidad.jsp"/>
            </logic:notEqual>
        </logic:present>
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
