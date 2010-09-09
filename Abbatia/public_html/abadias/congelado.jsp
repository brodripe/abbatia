<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<logic:present name="usuario" scope="session">
    <logic:equal name="usuario" scope="session" property="congelado" value="1">
        <div style="position:absolute;padding:0px;top:0;left:0;width:100%;height:100%;color:black;background:#E1C08B;margin-right:0px;filter:alpha(opacity=70);-moz-opacity:.50;opacity:.50;font-size:normal;font-weight:bolder;">
            <p>
            <center>
                <div style="position:relative;top:40%">
                    <table border="1" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000"
                           bordercolordark="#000000">
                        <tr>
                            <td width="150" align="center" valign="bottom" bgcolor="#F7FEEA">
                                <bean:message key="supporters.bloqueado.mensaje"/><br/>
                                <html:link action="/bloquear_abadia">
                                    <html:img border="0" page="/images/iconos/abadia_descongelar.gif"/> <br/>
                                    <bean:message key="usuario.opcion.descongelar"/>
                                    &nbsp;<img src="images/iconos/16/registrado.gif" border="0">
                                </html:link>
                            </td>
                        </tr>
                    </table>
                </div>
            </center>
            </p>
        </div>
    </logic:equal>
</logic:present>
