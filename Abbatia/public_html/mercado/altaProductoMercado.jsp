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
        <bean:message key="index.header"/>
    </title>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css"/>
</head>
<body bgcolor="#E1C08B">
<html:form action="/altaMercadoAdmin">
    <html:hidden property="accion" value="alta"/>
    <center>
        <table border="1" width="50%" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000"
               bordercolordark="#000000">
            <tr>
                <td colspan="2" align="center" bgcolor="#996633">
                    <b>
                        <font color="#FFFFFF" size="2">
                            <bean:message key="formulario.alta.mercado.titulo"/>
                        </font>
                    </b>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E4CFA2">
                    <table border="0" align="center">
                        <tr>
                            <td colspan="2" align="center">
                                <bean:message key="formulario.alta.mercado.cabecera"/>:
                            </td>
                        </tr>
                        <br>
                        <tr>
                            <th>
                                <div align="right">
                                    <bean:message key="formulario.alta.mercado.alimentos"/>
                                </div>
                            </th>
                            <td>
                                <html:select name="datosMercadoAdmin" property="claveAlimento">
                                    <html:options collection="lista_alimentos" property="codigo"
                                                  labelProperty="descripcion"/>
                                </html:select>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <div align="right">
                                    <bean:message key="formulario.alta.mercado.animales"/>
                                </div>
                            </th>
                            <td>
                                <html:select name="datosMercadoAdmin" property="claveAnimal">
                                    <html:options collection="lista_animales" property="codigo"
                                                  labelProperty="descripcion"/>
                                </html:select>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <div align="right">
                                    <bean:message key="formulario.alta.mercado.recursos"/>
                                </div>
                            </th>
                            <td>
                                <html:select name="datosMercadoAdmin" property="claveRecurso">
                                    <html:options collection="lista_recursos" property="codigo"
                                                  labelProperty="descripcion"/>
                                </html:select>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <div align="right">
                                    <bean:message key="formulario.alta.mercado.reliquias"/>
                                </div>
                            </th>
                            <td>
                                <html:select name="datosMercadoAdmin" property="claveReliquia">
                                    <html:options collection="lista_reliquias" property="codigo"
                                                  labelProperty="descripcion"/>
                                </html:select>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <div align="right">
                                    <bean:message key="formulario.alta.mercado.libros"/>
                                </div>
                            </th>
                            <td>
                                <html:select name="datosMercadoAdmin" property="claveLibro">
                                    <html:options collection="lista_libros" property="codigo"
                                                  labelProperty="descripcion"/>
                                </html:select>
                            </td>
                        </tr>
                        <tr>
                            <td align="center" colspan="2">
                                <hr width="100%" align="center" size="1"/>
                                <BR/>
                                <input type="button" onClick="javascript:history.back();" value="Atrás"/>
                                <html:submit>
                                    <bean:message key="formulario.alta.mercado.submit"/>
                                </html:submit>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </center>
</html:form>
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
