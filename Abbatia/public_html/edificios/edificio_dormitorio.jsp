<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<table border="1" width="80%" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000"
       bordercolordark="#000000">
    <tr>
        <td align="center" style="border-bottom: none thin white;border-right: none thin white;" bgcolor="#E4CFA2">
            <b><font size="2">
                <bean:message key="edificios.abadia.contenido"/>
            </font>
            </b>
        </td>
        <td align="center" bgcolor="#780A00">
            <html:link action="/mostrarEdificio.do?Tab=monjes" paramId="clave" paramName="Edificio"
                       paramProperty="idDeEdificio">
                <font color="#FFFFFF"><b><bean:message key="edificios.abadia.monjes"/></b></font>
            </html:link>
        </td>
    </tr>
    <tr>

        <td bgcolor="#E4CFA2" colspan=2 style="border-top: none thin white;">
            &nbsp;
        </td>

    </tr>
</table>
