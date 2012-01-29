<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css"/>
    <script src="/theme/popup/prototype.js" type="text/javascript"></script>
    <script src="/theme/popup/effects.js" type="text/javascript"></script>
    <script src="/theme/popup/dragdrop.js" type="text/javascript"></script>
    <script src="/theme/popup/popup.js" type="text/javascript"></script>
    <link rel="stylesheet" href="/theme/popup/popup.css" type="text/css"/>
    <title>Literales</title>
</head>
<body bgcolor="#E1C08B">

<center>
    <form name="formulario" action="AdminLiterales.do">
        <input type="hidden" name="change" value="1"/>
        <table border="0" width="100%">
            <tr>
                <td width="80%" align="center">
                    <table style="BORDER-RIGHT: #000000 1px solid; BORDER-TOP: #000000 1px solid; BORDER-LEFT: #000000 1px solid; BORDER-BOTTOM: #000000 1px solid"
                           borderColor="#000000" cellSpacing="0" width="40%" border="1">
                        <tbody>
                        <tr>
                            <td bgColor="#e4cfa2">
                                <table border="0" width="100%">
                                    <tbody>
                                    <tr>
                                        <td align="right"><font size="2">Idioma 1</font></td>
                                        <td><select size="1" name="Idioma1">
                                            <option value="1">Castellano</option>
                                            <option value="2">Catalán</option>
                                            <option value="3">Inglés</option>
                                            <option value="4">Francés</option>
                                            <option value="5">Alemán</option>
                                            <option value="6">Italiano</option>
                                            <option value="7">Portugues</option>
                                            <option value="8">Gallego</option>
                                            <option value="9">Euskera</option>
                                            <option value="10">Latín</option>
                                        </select></td>
                                    </tr>
                                    <font size="2">
                                        <tr>
                                            <td align="right"><font size="2">Idioma 2</font></td>
                                            <td><select size="1" name="Idioma2">
                                                <option value="1">Castellano</option>
                                                <option value="2">Catalán</option>
                                                <option value="3">Inglés</option>
                                                <option value="4">Francés</option>
                                                <option value="5">Alemán</option>
                                                <option value="6">Italiano</option>
                                                <option value="7">Portugues</option>
                                                <option value="8">Gallego</option>
                                                <option value="9">Euskera</option>
                                                <option value="10">Latín</option>
                                            </select></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <input type="checkbox" name="Nuevos" value="S"> ¿Nuevos?
                                            </td>
                                            <td>
                                                <p align="center"><input type="submit" value="Submit" name="B1"></p>
                                            </td>
                                            <td>
                                                <p><a id="popup_nuevo_literal" href="#">Nuevo Literal</a></p>
                                            </td>
                                        </tr>
                                    </font>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </td>

                <td width="20%" valign="middle" align="center">
                    <a href="/ayudas/docs/abadia_properties.zip">Descargar constantes ( 26.10.2004 )</a>
                    <br>Al finalizar enviar al administrador
                </td>
            </tr>
        </table>
    </form>
    &nbsp;
    <bean:write name="Navega" filter="false"/>
    <br/>

    <form name="literales" action="AdminLiterales.do">
        <input type="hidden" name="post" value="1"/>
        <input type="hidden" name="pagina" value="<bean:write name="Pagina" filter="false"/>"/>
        <input type="hidden" name="Idioma1" value="<bean:write name="Idioma1" filter="false"/>"/>
        <input type="hidden" name="Idioma2" value="<bean:write name="Idioma2" filter="false"/>"/>
        <input type="hidden" name="Nuevos" value="<bean:write name="Nuevos" filter="false"/>"/>
        <table cellSpacing="0" width="90%" bgColor="#996633" border="0">
            <tbody>
            <tr>
                <td width="20"></td>
                <td>
                    <p align="center"><b><font color="#ffffff" size="2">Literales</font></b></p>
                </td>
            </tr>
            </tbody>
        </table>
        <table style="BORDER-RIGHT: #000000 1px solid; BORDER-TOP: #000000 1px solid; BORDER-LEFT: #000000 1px solid; BORDER-BOTTOM: #000000 1px solid"
               borderColor="#000000" cellSpacing="0" width="90%" border="1">
            <tbody>
            <tr>
                <td bgColor="#e4cfa2">
                    <table width="100%">
                        <tr>
                            <td><strong>ID</strong></td>
                            <td><strong>Idioma 1 - <bean:write name="sIdioma1" filter="false"/></strong></td>
                            <td><strong>Idioma 2 - <bean:write name="sIdioma2" filter="false"/></strong></td>
                            <td><strong>Modificado</strong></td>
                        </tr>
                        <logic:iterate id="lista" name="literales">
                            <font size="2">
                                <bean:write property="description" name="lista" filter="false"/>
                            </font>
                        </logic:iterate>
                    </table>
                </td>
            </tr>
            </tbody>
        </table>
        <p align="center"><input type="submit" value="Modificar" name="Modifica"></p>
    </form>
    &nbsp;
    <div id="nuevo_literal" class="popup">
        <h3>Introduce el texto correspondiente al literal en Castellano</h3>
        <html:form action="/nuevo_literal">
            <html:text property="literal"/> <html:submit>crear</html:submit>
        </html:form>
    </div>
    <script type="text/javascript">
        //<![CDATA[
        new Popup('nuevo_literal', 'popup_nuevo_literal', {modal:true})
        //]]>
    </script>
</body>
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
</html>
