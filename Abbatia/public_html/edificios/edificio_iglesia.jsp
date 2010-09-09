<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>


<table border="1" width="80%" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000"
       bordercolordark="#000000">
    <tr>
        <td align="center" bgcolor="#996633">
            <b><font color="#FFFFFF" size="2">
                <bean:message key="edificios.abadia.contenido"/>
            </font>
            </b>
        </td>
    </tr>
    <tr>

        <td bgcolor="#E4CFA2">
            <logic:notEmpty name="Edificio" property="contenido">
                <table border="1" width="100%" cellspacing="0" bordercolorlight="#808080" bordercolor="#808080"
                       bordercolordark="#808080">
                    <tr>
                        <td colspan="5" bgcolor="#E1C08B" align="center">
                            <b><font color="#FFFFFF">
                                <bean:message key="edificios.abadia.iglesia.reliquias"/>
                            </font>
                            </b>
                        </td>
                    </tr>
                    <tr>
                        <td bgcolor="#E1C08B" align="center">
                            <b><font color="#FFFFFF">
                                <bean:message key="edificios.abadia.iglesia.nombre"/>
                            </font>
                            </b>
                        </td>
                        <td bgcolor="#E1C08B" align="center">
                            <font color="#FFFFFF"><b>
                                <bean:message key="edificios.abadia.iglesia.descripcion"/>
                            </b>
                            </font>
                        </td>
                        <td bgcolor="#E1C08B" align="center">
                            <b><font color="#FFFFFF">
                                <bean:message key="edificios.abadia.iglesia.estado"/>
                            </font>
                            </b>
                        </td>
                        <td bgcolor="#E1C08B" align="center">
                            <b><font color="#FFFFFF">
                                <bean:message key="edificios.abadia.iglesia.fecha_creacion"/>
                            </font>
                            </b>
                        </td>
                        <td bgcolor="#E1C08B" align="center">
                            <b><font color="#FFFFFF">
                                <bean:message key="edificios.abadia.iglesia.fecha_adquisicion"/>
                            </font>
                            </b>
                        </td>
                    </tr>
                    <logic:iterate id="lista" name="Reliquias" property="contenido">
                        <tr>
                            <td>
                                <bean:write property="nombre" name="lista"/>
                            </td>
                            <td>
                                <bean:write property="nombre" name="lista"/>
                            </td>
                            <td>
                                <bean:write property="estado" name="lista"/>
                            </td>
                            <td>
                                <bean:write property="fecha_creacion" name="lista"/>
                            </td>
                            <td>
                                <bean:write property="fecha_adquisicion" name="lista"/>
                            </td>
                        </tr>
                    </logic:iterate>
                </table>
            </logic:notEmpty>
        </td>


    </tr>
</table>
