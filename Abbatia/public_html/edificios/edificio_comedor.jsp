<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<table border="1" width="80%" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000"
       bordercolordark="#000000">
    <tr>
        <td align="center" style="border-bottom: none thin white" bgcolor="#E4CFA2">
            <b><bean:message key="edificios.abadia.monjes"/></b>
        </td>
    </tr>
    <tr>

        <td bgcolor="#E4CFA2" style="border-top: none thin white;">
            <table cellspacing="0" border="0" width="100%" align="center">

                <logic:iterate id="familia" name="familias">
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
                                        <br>
                                    </font>
                                </td>
                            </tr>
                            <tr>
                                <td bgcolor="#E1C08B" align="center">
                                    <font color="#000000" size="1">
                                        <html:link action="/mostrarMonje" paramId="clave" paramName="Monje"
                                                   paramProperty="idDeMonje">
                                            <bean:message key="monjes.abadia.masinfo"/>
                                        </html:link><br>
                                        <html:link action="/mostrar_produccion" paramId="clave" paramName="Monje"
                                                   paramProperty="idDeMonje">
                                            <html:img border="0" page="/images/iconos/16/ojo.gif"
                                                      altKey="tooltip.produccion"/>
                                        </html:link>
                                        <font>
                                </td>
                            </tr>

                        </table>

                    </td>
                    <%
                        if (n > 2) {
                            out.println("<tr></tr>");
                            n = 0;
                        } else n++;
                    %>
                </logic:iterate>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                </tr>
            </table>
        </td>

    </tr>
</table>
