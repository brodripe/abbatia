<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<%
    int n = 0;
%>

<html>
<head>
    <title></title>
    <link REL="STYLESHEET" HREF="theme/styles.css" TYPE="text/css">
</head>
<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<br/>

<div align="center">
<table border="1" cellspacing="0" bordercolor="#000000" width="90%" bordercolorlight="#000000"
       bordercolordark="#000000">
<tr>
    <td align="center" bgcolor="#780A00">
        <html:link action="/mostrarDiplomacia?tab=0">
            <b><font color="#FFFFFF" size="2">
                <bean:message key="diplomacia.abadia.titulo"/>
            </font>
            </b>
        </html:link>
    </td>
    <td align="center" bgcolor="#780A00">
        <html:link action="/mostrarDiplomacia?tab=1">
            <b><font color="#FFFFFF" size="2">
                <bean:message key="diplomacia.jerarquia.titulo"/>
            </font>
            </b>
        </html:link>
    </td>
    <td align="center" style="border-bottom: none thin white;" bgcolor="#E4CFA2">
        <b><bean:message key="diplomacia.jerarquia.abbatia.titulo"/></b>
    </td>
</tr>
<tr>
    <td bgcolor="#E4CFA2" colspan=3 style="border-top: none thin white;">
        <!-- Papa -->
        <table border="0" width="100%" cellspacing="4">
            <tr>
            </tr>
            <tr>
                <td colspan=2 bgcolor="#F0AF1C">
                    <b><bean:message key="diplomacia.jerarquia.papa"/>:</b>
                </td>
            </tr>
            <td align="center" valign="top">
                <logic:present name="Papa">
                    <table border="1" width="130" align="center" cellspacing="0" bordercolor="#000000"
                           bordercolorlight="#000000" bordercolordark="#000000">
                        <tr>
                            <td align="center" bgcolor="#F0AF1C">
                                <font color="#000000" size="1">
                                    <bean:write name="Papa" property="nombre"/>&nbsp;<bean:message
                                        key="monjes.abadia.nomciudad"/>&nbsp;<bean:write name="Papa"
                                                                                         property="primerApellido"/>
                                </font>
                            </td>
                        </tr>
                        <tr>
                            <td bgcolor="#E4CFA2" align="center">
                                <font color="#000000" size="1">
                                    <bean:message key="monjes.abadia.edad"/>
                                    <bean:write name="Papa" property="edad"/>
                                    <br>
                                    <bean:message key="monjes.abadia.salud"/>
                                    <bean:write name="Papa" property="barra_salud" filter="false"/>
                                    <bean:message key="monjes.abadia.fe"/>
                                    <bean:write name="Papa" property="barra_fe" filter="false"/>
                                    <br>
                                    <bean:message key="monjes.abadia.popularidad"/>
                                    <bean:write name="Papa" property="barra_popularidad" filter="false"/>
                                </font>
                            </td>
                        <tr>
                            <td bgcolor="#E4CFA2" align="center">
                                <font color="#000000" size="1">
                                    <bean:write name="Papa" property="abadia"/>
                                    <br>
                                    <bean:write name="Papa" property="region"/>
                                </font>
                            </td>
                        </tr>
                    </table>
                </logic:present>
                <logic:notPresent name="Papa">
                    <b><bean:message key="diplomacia.jerarquia.sinpapa"/></b>
                </logic:notPresent>
            </td>
            <td>
                <!-- Opciones -->
                <logic:present name="Papa">
                    <table width="100%" border=0>
                        <tr>
                            <td width="" align="center">
                            </td>
                        </tr>
                    </table>
                </logic:present>
            </td>
            </tr>
        </table>
        <br/>

        <!-- Cardenales -->
        <table border="0" width="100%" cellspacing="4">
            <tr>
                <td bgcolor="#F0AF1C" colspan=5>
                    <b><bean:message key="diplomacia.jerarquia.cardenales"/>:</b>
                </td>
            </tr>
            <tr>
                <%
                    n = 0;
                %>
                <logic:iterate id="Monje" name="Cardenales">
                    <td>

                        <table border="1" width="130" align="center" cellspacing="0" bordercolor="#000000"
                               bordercolorlight="#000000" bordercolordark="#000000">
                            <tr>
                                <td align="center" bgcolor="#F0AF1C">
                                    <font color="#000000" size="1">
                                        <bean:write name="Monje" property="nombre"/>&nbsp;<bean:message
                                            key="monjes.abadia.nomciudad"/>&nbsp;<bean:write name="Monje"
                                                                                             property="primerApellido"/>
                                    </font>
                                </td>
                            </tr>
                            <tr>
                                <td bgcolor="#E4CFA2" align="center">
                                    <font color="#000000" size="1">
                                        <bean:message key="monjes.abadia.edad"/>
                                        <bean:write name="Monje" property="edad"/>
                                        <br>
                                        <bean:message key="monjes.abadia.salud"/>
                                        <bean:write name="Monje" property="barra_salud" filter="false"/>
                                        <bean:message key="monjes.abadia.fe"/>
                                        <bean:write name="Monje" property="barra_fe" filter="false"/>
                                        <br>
                                    </font>
                                </td>
                            </tr>
                            <tr>
                                <td bgcolor="#E4CFA2" align="center">
                                    <font color="#000000" size="1">
                                        <bean:write name="Monje" property="abadia"/>
                                        <br>
                                        <bean:write name="Monje" property="region"/>
                                    </font>
                                </td>
                            </tr>
                        </table>

                    </td>
                    <%
                        if (n > 3) {
                            out.println("<tr></tr>");
                            n = 0;
                        } else n++;
                    %>
                </logic:iterate>

            </tr>
        </table>
        <br/>

        <!-- Obispos -->
        <table border="0" width="100%" cellspacing="4">
            <tr>
            </tr>
            <tr>
                <td bgcolor="#F0AF1C" colspan=5>
                    <b><bean:message key="diplomacia.jerarquia.obispos"/>:</b>
                </td>
            </tr>
            <tr>
                <%
                    n = 0;
                %>
                <logic:iterate id="Monje" name="Obispos">
                    <td>

                        <table border="1" width="130" align="center" cellspacing="0" bordercolor="#000000"
                               bordercolorlight="#000000" bordercolordark="#000000">
                            <tr>
                                <td align="center" bgcolor="#F0AF1C">
                                    <font color="#000000" size="1">
                                        <bean:write name="Monje" property="nombre"/>&nbsp;<bean:message
                                            key="monjes.abadia.nomciudad"/>&nbsp;<bean:write name="Monje"
                                                                                             property="primerApellido"/>
                                    </font>
                                </td>
                            </tr>
                            <tr>
                                <td bgcolor="#E4CFA2" align="center">
                                    <font color="#000000" size="1">
                                        <bean:message key="monjes.abadia.edad"/>
                                        <bean:write name="Monje" property="edad"/>
                                        <br>
                                        <bean:message key="monjes.abadia.salud"/>
                                        <bean:write name="Monje" property="barra_salud" filter="false"/>
                                        <bean:message key="monjes.abadia.fe"/>
                                        <bean:write name="Monje" property="barra_fe" filter="false"/>
                                        <br>
                                        <bean:message key="monjes.abadia.popularidad"/>
                                        <bean:write name="Monje" property="barra_popularidad" filter="false"/>
                                    </font>
                                </td>
                            </tr>
                            <tr>
                                <td bgcolor="#E4CFA2" align="center">
                                    <font color="#000000" size="1">
                                        <bean:write name="Monje" property="abadia"/>
                                        <br>
                                        <bean:write name="Monje" property="region"/>
                                    </font>
                                </td>
                            </tr>
                        </table>

                    </td>
                    <%
                        if (n > 3) {
                            out.println("<tr></tr>");
                            n = 0;
                        } else n++;
                    %>
                </logic:iterate>
            </tr>
        </table>
        <br/>
        <br/>

    </td>
</tr>
</table>
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
