<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>


<table border="1" width="90%" cellspacing="0">
    <tr>
        <td align="center" bgcolor="#780A00">
            <html:link action="/mostrarEdificio?Tab=velatorio" paramId="clave" paramName="Edificio"
                       paramProperty="idDeEdificio">
                <font color="#FFFFFF">
                    <b><bean:message key="edificios.abadia.cementerio.tab.velatorio"/> ( <bean:write name="Edificio"
                                                                                                     property="numMonjesVelatorio"/>
                        ) </b>
                </font>
            </html:link>
        </td>
        <td align="center" bgcolor="#780A00">
            <html:link action="/mostrarEdificio?Tab=cementerio" paramId="clave" paramName="Edificio"
                       paramProperty="idDeEdificio">
                <font color="#FFFFFF">
                    <b><bean:message key="edificios.abadia.cementerio.tab.cementerio"/> ( <bean:write name="Edificio"
                                                                                                      property="numMonjesCementerio"/>
                        ) </b>
                </font>
            </html:link>
        </td>
        <td align="center" style="border-bottom: none thin white;border-right: none thin white;" bgcolor="#E4CFA2">
            <font size="2">
                <b><bean:message key="edificios.abadia.cementerio.tab.osario"/> ( <bean:write name="Edificio"
                                                                                              property="numMonjesOsario"/>
                    ) </b>
            </font>
        </td>

    </tr>
    <tr>

        <td bgcolor="#E4CFA2" colspan="3">
            <%
                int n = 0;
            %>

            <!-- CEMENTERIO -->
            <logic:notEmpty name="Edificio" property="contenido">

                <table borderColor="#000000" width="90%" cellSpacing="0" align="center" borderColorDark="#000000"
                       borderColorLight="#000000" border="0">
                    <tr>
                        <%
                            n = 0;
                        %>
                        <logic:iterate id="Monje1" name="Edificio" property="contenido">
                            <td>
                                <table border="1" width="130" align="center" cellspacing="0" bordercolor="#000000"
                                       bordercolorlight="#000000" bordercolordark="#000000">
                                    <tr>
                                        <td align="center" bgcolor="#F0AF1C">
                                            <font color="#FFFFFF" size="1">
                                                <bean:write name="Monje1" property="nombre"/>&nbsp;<bean:message
                                                    key="monjes.abadia.nomciudad"/>&nbsp;<bean:write name="Monje1"
                                                                                                     property="primerApellido"/>
                                            </font>
                                        </td>
                                        <td>
                                            <DIV align="center">
                                                <html:img border="0" page="/images/iconos/16/hueso.gif"/>
                                            </DIV>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td bgcolor="#E4CFA2" align="center" colspan="2">
                                            <font color="#000000" size="1">
                                                <bean:message key="monjes.abadia.nacimiento"/>
                                                <bean:write name="Monje1" property="fechaDeNacimiento"/>
                                                <br>
                                                <bean:message key="monjes.abadia.fecha_defuncion"/>
                                                <bean:write name="Monje1" property="fechaDeFallecimiento"/>
                                            </font>
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
                </table>
            </logic:notEmpty></td>
    </tr>
</table>
