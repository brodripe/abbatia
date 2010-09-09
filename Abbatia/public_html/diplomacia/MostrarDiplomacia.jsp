<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <title></title>
    <link REL="STYLESHEET" HREF="theme/styles.css" TYPE="text/css">
    <script language="JavaScript1.2">
        function over_effect(e, state) {
            if (document.all)
                source4 = event.srcElement
            else if (document.getElementById)
                source4 = e.target
            if (source4.className != "menu")
            {
                while (source4.tagName != "TABLE") {
                    source4 = document.getElementById ? source4.parentNode : source4.parentElement
                    if (source4.className == "menu") {
                        if (state == "solid")
                            source4.style.background = "#e4cfa2"
                        else
                            source4.style.background = "#B6BDD2"
                    }
                }
            }
        }
        function confirmarDimisionObispo()
        {
            return confirm("<bean:message key="prompt.confirmacion.dimision.obispo" />");
        }
        function confirmarDimisionCardenal()
        {
            return confirm("<bean:message key="prompt.confirmacion.dimision.cardenal" />");
        }

    </script>
</head>
<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<br/>

<div align="center">
<table border="1" cellspacing="0" bordercolor="#000000" width="90%" bordercolorlight="#000000"
       bordercolordark="#000000">
<tr height="20">
    <td align="center" style="border-bottom: none thin white;border-right: none thin white;" bgcolor="#E4CFA2">
        <b><font size="2">
            <bean:message key="diplomacia.abadia.titulo"/>
        </font>
        </b>
    </td>
    <td align="center" bgcolor="#780A00">
        <html:link action="/mostrarDiplomacia?tab=1">
            <b><font color="#FFFFFF" size="2">
                <bean:message key="diplomacia.jerarquia.titulo"/>
            </font>
            </b>
        </html:link>
    </td>
    <td align="center" bgcolor="#780A00">
        <html:link action="/mostrarDiplomacia?tab=2">
            <font color="#FFFFFF"><b>
                <bean:message key="diplomacia.jerarquia.abbatia.titulo"/>
            </b></font>
        </html:link>
    </td>
</tr>
<tr>
<td bgcolor="#E4CFA2" colspan=3 style="border-top: none thin white;">
<br/>
<!-- ABAD -->
<logic:present name="Abad">
<table border="0" width="100%" cellspacing="4">
    <tr>
    </tr>
    <tr>
        <td colspan=2 bgcolor="#F0AF1C">
            <b>
                <bean:message key="diplomacia.abadia.Abad"/>
                :</b>
        </td>
    </tr>
    <tr>
        <td width="200" align="center" valign="top">
            <table border="1" width="180" align="center" cellspacing="0" bordercolor="#000000"
                   bordercolorlight="#000000"
                   bordercolordark="#000000">
                <tr>
                    <td align="center" bgcolor="#F0AF1C" colspan="2">
                        <font color="#000000" size="1">
                            <bean:write name="Abad" property="nombre"/>
                            &nbsp;
                            <bean:message key="monjes.abadia.nomciudad"/>
                            &nbsp;
                            <bean:write name="Abad" property="primerApellido"/>
                        </font>
                    </td>
                </tr>
                <tr>
                    <td rowspan="2" align="center">
                        <img src="/images/iconos/monje_2_${sessionScope.abadia.idDeOrden}.png">
                    </td>
                    <td bgcolor="#E4CFA2" align="center">
                        <font color="#000000" size="1">
                                <bean:message key="monjes.abadia.edad"/>
                                <bean:write name="Abad" property="edad"/>
                            <br>
                                <bean:message key="monjes.abadia.salud"/>
                                <bean:write name="Abad" property="barra_salud" filter="false"/>
                                <bean:message key="monjes.abadia.fe"/>
                                <bean:write name="Abad" property="barra_fe" filter="false"/>
                    </td>
                </tr>
                <tr>
                    <td bgcolor="#E4CFA2" align="center">
                        <table width="100%" align="center" border=0>
                            <tr>
                                <td align="center"><font size=1>I:</font>
                                    <bean:write name="Abad" property="barra_idioma" filter="false"/>
                                </td>
                                <td align="center"><font size=1>T:</font>
                                    <bean:write name="Abad" property="barra_talento" filter="false"/>
                                </td>
                            </tr>
                            <tr>
                                <td align="center"><font size=1>C:</font>
                                    <bean:write name="Abad" property="barra_carisma" filter="false"/>
                                </td>
                                <td align="center"><font size=1>S:</font>
                                    <bean:write name="Abad" property="barra_sabiduria" filter="false"/>
                                </td>
                            </tr>
                            <tr>
                                <td align="center"><font size=1>F:</font>
                                    <bean:write name="Abad" property="barra_fuerza" filter="false"/>
                                </td>
                                <td align="center"><font size=1>D:</font>
                                    <bean:write name="Abad" property="barra_destreza" filter="false"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td bgcolor="#E1C08B" align="center" colspan="2">
                        <font color="#000000" size="1">
                            <html:link action="/mostrarMonje" paramId="clave" paramName="Abad"
                                       paramProperty="idDeMonje">
                                <bean:message key="monjes.abadia.masinfo"/>
                            </html:link>
                            <br>
                            <html:link action="/mostrar_produccion" paramId="clave" paramName="Abad"
                                       paramProperty="idDeMonje">
                                <html:img border="0" page="/images/iconos/16/ojo.gif" altKey="tooltip.produccion"/>
                            </html:link>
                        </font>
                    </td>
                </tr>

            </table>
        </td>
        <td align="center">
            <!-- Opciones del Abad -->
            <table border="1" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000" bordercolordark="#000000"
                   onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')"
                   onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
                <tr>
                    <td class="menu" width="90" align="center" valign="bottom">
                        <html:link action="/nombrarAbad?revocar=1" paramId="clave" paramName="Abad"
                                   paramProperty="idDeMonje">
                            <html:img border="0" page="/images/iconos/desconectar.gif"/>
                            <br/>
                            <bean:message key="diplomacia.abadia.abad.revocar"/>
                        </html:link>
                        <br>
                    </td>
                    <td class="menu" width="90" align="center" valign="bottom">
                        <logic:greaterThan value="-1" name="Elecciones">
                            <html:link action="/votar_obispado">
                                <html:img border="0" page="/images/iconos/votar.gif"/>
                                <br/>
                                <bean:message key="principal.elecciones.votar"/>
                            </html:link>
                            <br>
                        </logic:greaterThan>
                        <logic:lessEqual value="-1" name="Elecciones">
                            <html:img border="0" page="/images/iconos/votar_disabled.gif"/>
                            <br/>
                            <bean:message key="principal.elecciones.votar"/>
                        </logic:lessEqual>
                    </td>
                    <td class="menu" width="90" align="center" valign="bottom">
                        <html:link action="/buscar_abadia?opcion=2&region=0">
                            <html:img border="0" page="/images/iconos/monje_viajar.gif"/>
                            <bean:message key="diplomacia.jerarquia.viajar"/>
                        </html:link>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<br/>
</logic:present>

<!-- si la abadía no tiene obispo ofrecemos la posibilidad de no presentarse a las elecciones -->
<!-- Ocurre lo mismo con el Cardenal -->

<html:form action="/cargo_seleccionable">
<table border="1" width="680" align="center" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000"
       bordercolordark="#000000">
    <tr>
        <td bgcolor="#E1C08B" align="left">
            <logic:notPresent name="Obispo">
                <html:checkbox name="DiplomaciaForm" property="flagObispo">
                    <bean:message key="diplomacia.candidatura.obispo"/>
                </html:checkbox>
            </logic:notPresent>
            <logic:present name="Obispo">
                <html:checkbox name="DiplomaciaForm" property="flagObispo" disabled="true">
                    <bean:message key="diplomacia.candidatura.obispo"/>
                </html:checkbox>
            </logic:present>
        </td>
        <td td bgcolor="#E1C08B" align="left">
            <logic:notPresent name="Cardenal">
                <html:checkbox name="DiplomaciaForm" property="flagCardenal">
                    <bean:message key="diplomacia.candidatura.cardenal"/>
                </html:checkbox>
            </logic:notPresent>
            <logic:present name="Cardenal">
                <html:checkbox name="DiplomaciaForm" property="flagCardenal" disabled="true">
                    <bean:message key="diplomacia.candidatura.cardenal"/>
                </html:checkbox>
            </logic:present>
        </td>
    </tr>
    <tr>
        <td colspan="2" bgcolor="#E1C08B" align="center">
            <html:submit>
                <bean:message key="usuario.actualizar"/>
            </html:submit>
        </td>
    </tr>
</table>
</html:form>


<br/>
<!--
****************************************************************************************************
OBISPO
****************************************************************************************************
-->
<logic:present name="Obispo">
<table border="0" width="100%" cellspacing="4">
    <tr>
    </tr>
    <tr>
        <td colspan=2 bgcolor="#F0AF1C">
            <b>
                <bean:message key="diplomacia.abadia.obispo"/>
                :</b>
        </td>
    </tr>
    <tr>
        <td width="200" align="center" valign="top">

            <table border="1" width="180" align="center" cellspacing="0" bordercolor="#000000"
                   bordercolorlight="#000000"
                   bordercolordark="#000000">
                <tr>
                    <td align="center" bgcolor="#F0AF1C" colspan="2">
                        <font color="#000000" size="1">
                            <bean:write name="Obispo" property="nombre"/>
                            &nbsp;
                            <bean:message key="monjes.abadia.nomciudad"/>
                            &nbsp;
                            <bean:write name="Obispo" property="primerApellido"/>
                        </font>
                    </td>
                </tr>
                <tr>
                    <td rowspan="2" align="center">
                        <img src="images/iconos/monje_obispo.gif">
                    </td>
                    <td bgcolor="#E4CFA2" align="center">
                        <font color="#000000" size="1">
                            <bean:message key="monjes.abadia.edad"/>
                            <bean:write name="Obispo" property="edad"/>
                            <br>
                            <bean:message key="monjes.abadia.salud"/>
                            <bean:write name="Obispo" property="barra_salud" filter="false"/>
                            <bean:message key="monjes.abadia.fe"/>
                            <bean:write name="Obispo" property="barra_fe" filter="false"/>
                            <br>
                        </font>
                    </td>
                </tr>
                <tr>
                    <td bgcolor="#E4CFA2" align="center">
                        <table width="100%" align="center" border=0>
                            <tr>
                                <td align="center"><font size=1>I:</font>
                                    <bean:write name="Obispo" property="barra_idioma" filter="false"/>
                                </td>
                                <td align="center"><font size=1>T:</font>
                                    <bean:write name="Obispo" property="barra_talento" filter="false"/>
                                </td>
                            </tr>
                            <tr>
                                <td align="center"><font size=1>C:</font>
                                    <bean:write name="Obispo" property="barra_carisma" filter="false"/>
                                </td>
                                <td align="center"><font size=1>S:</font>
                                    <bean:write name="Obispo" property="barra_sabiduria" filter="false"/>
                                </td>
                            </tr>
                            <tr>
                                <td align="center"><font size=1>F:</font>
                                    <bean:write name="Obispo" property="barra_fuerza" filter="false"/>
                                </td>
                                <td align="center"><font size=1>D:</font>
                                    <bean:write name="Obispo" property="barra_destreza" filter="false"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" bgcolor="#E1C08B" align="center">
                        <font color="#000000" size="1">
                            <html:link action="/mostrarMonje" paramId="clave" paramName="Obispo"
                                       paramProperty="idDeMonje">
                                <bean:message key="monjes.abadia.masinfo"/>
                            </html:link>
                            <br>
                            <html:link action="/mostrar_produccion" paramId="clave" paramName="Obispo"
                                       paramProperty="idDeMonje">
                                <html:img border="0" page="/images/iconos/16/ojo.gif" altKey="tooltip.produccion"/>
                            </html:link>
                        </font>
                    </td>
                </tr>

            </table>
        </td>
        <td align="center">
            <!-- Opciones del Obispo -->
            <table border="1" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000" bordercolordark="#000000"
                   onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')"
                   onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
                <tr>
                    <td class="menu" width="90" align="center" valign="bottom">
                        <!-- Esta en la abadia? -->
                        <logic:equal value="0" name="Obispo" property="estado">
                            <html:link action="/obispadoImpuestos">
                                <html:img border="0" page="/images/iconos/monje_impuestos.gif"/>
                                <br/>
                                <bean:message key="diplomacia.abadia.obispo.impuestos"/>
                            </html:link>
                            <br>
                        </logic:equal>
                        <logic:notEqual value="0" name="Obispo" property="estado">
                            <html:img border="0" page="/images/iconos/monje_impuestos_disabled.gif"/>
                            <br/>
                            <bean:message key="diplomacia.abadia.obispo.impuestos"/>
                        </logic:notEqual>
                    </td>
                    <td class="menu" width="90" align="center" valign="bottom">
                        <!-- Esta en la abadia? -->
                        <logic:equal value="0" name="Obispo" property="estado">
                            <html:link action="/buscar_abadia?opcion=1">
                                <html:img border="0" page="/images/iconos/lista_edificios.gif"/>
                                <br/>
                                <bean:message key="diplomacia.abadia.obispo.mapa"/>
                            </html:link>
                            <br>
                        </logic:equal>
                        <logic:notEqual value="0" name="Obispo" property="estado">
                            <html:img border="0" page="/images/iconos/lista_edificios_disabled.gif"/>
                            <br/>
                            <bean:message key="diplomacia.abadia.obispo.mapa"/>
                        </logic:notEqual>
                    </td>
                    <!-- dimisiones Obispo-->
                    <td class="menu" width="90" align="center" valign="bottom">
                        <logic:equal value="0" name="Obispo" property="estado">
                            <html:link action="/obispo_dimitir" onclick="return confirmarDimisionObispo();">
                                <html:img border="0" page="/images/iconos/dimitir_obispo.gif"/>
                                <br/>
                                <bean:message key="diplomacia.abadia.obispo.dimitir"/>
                            </html:link>
                            <br>
                        </logic:equal>
                        <logic:notEqual value="0" name="Obispo" property="estado">
                            <html:img border="0" page="/images/iconos/dimitir_obispo.gif"/>
                            <br/>
                            <bean:message key="diplomacia.abadia.obispo.dimitir"/>
                        </logic:notEqual>
                    </td>

                    <td class="menu" width="90" align="center" valign="bottom">
                        <!-- Esta en la abadia? -->
                        <logic:equal value="0" name="Obispo" property="estado">
                            <html:link action="/buscar_abadia?opcion=3">
                                <html:img border="0" page="/images/iconos/construir_edificios.gif"/>
                                <br/>
                                <bean:message key="diplomacia.abadia.obispo.construir"/>
                            </html:link>
                            <br>
                        </logic:equal>
                        <logic:notEqual value="0" name="Obispo" property="estado">
                            <html:img border="0" page="/images/iconos/construir_edificios_disabled.gif"/>
                            <br/>
                            <bean:message key="diplomacia.abadia.obispo.construir"/>
                        </logic:notEqual>
                    </td>
                    <td class="menu" width="90" align="center" valign="bottom">
                        <!-- Esta en la abadia? -->
                        <logic:equal value="0" name="Obispo" property="estado">
                            <html:link action="/buscar_abadia?opcion=2&region=0">
                                <html:img border="0" page="/images/iconos/monje_viajar.gif"/>
                                <bean:message key="diplomacia.jerarquia.viajar"/>
                            </html:link>
                        </logic:equal>
                        <logic:notEqual value="0" name="Obispo" property="estado">
                            <html:img border="0" page="/images/iconos/monje_viajar_disabled.gif"/>
                            <bean:message key="diplomacia.jerarquia.viajar"/>
                        </logic:notEqual>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<br/>
</logic:present>

<br/>
<!--
****************************************************************************************************
CARDENAL
****************************************************************************************************
-->
<logic:present name="Cardenal">
<table border="0" width="100%" cellspacing="4">
    <tr>
    </tr>
    <tr>
        <td colspan=2 bgcolor="#F0AF1C">
            <b>
                <bean:message key="diplomacia.abadia.cardenal"/>
                :</b>
        </td>
    </tr>
    <td width="200" align="center" valign="top">
        <table border="1" width="180" align="center" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000"
               bordercolordark="#000000">
            <tr>
                <td align="center" bgcolor="#F0AF1C" colspan="2">
                    <font color="#000000" size="1">
                        <bean:write name="Cardenal" property="nombre"/>
                        &nbsp;
                        <bean:message key="monjes.abadia.nomciudad"/>
                        &nbsp;
                        <bean:write name="Cardenal" property="primerApellido"/>
                    </font>
                </td>
            </tr>
            <tr>
                <td rowspan="2" align="center">
                    <img src="/images/iconos/monje_cardenal.png">
                </td>
                <td bgcolor="#E4CFA2" align="center">
                    <font color="#000000" size="1">
                        <bean:message key="monjes.abadia.edad"/>
                        <bean:write name="Cardenal" property="edad"/>
                        <br>
                        <bean:message key="monjes.abadia.salud"/>
                        <bean:write name="Cardenal" property="barra_salud" filter="false"/>
                        <bean:message key="monjes.abadia.fe"/>
                        <bean:write name="Cardenal" property="barra_fe" filter="false"/>
                        <br>
                    </font>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E4CFA2" align="center">
                    <table width="100%" align="center" border=0>
                        <tr>
                            <td align="center"><font size=1>I:</font>
                                <bean:write name="Cardenal" property="barra_idioma" filter="false"/>
                            </td>
                            <td align="center"><font size=1>T:</font>
                                <bean:write name="Cardenal" property="barra_talento" filter="false"/>
                            </td>
                        </tr>
                        <tr>
                            <td align="center"><font size=1>C:</font>
                                <bean:write name="Cardenal" property="barra_carisma" filter="false"/>
                            </td>
                            <td align="center"><font size=1>S:</font>
                                <bean:write name="Cardenal" property="barra_sabiduria" filter="false"/>
                            </td>
                        </tr>
                        <tr>
                            <td align="center"><font size=1>F:</font>
                                <bean:write name="Cardenal" property="barra_fuerza" filter="false"/>
                            </td>
                            <td align="center"><font size=1>D:</font>
                                <bean:write name="Cardenal" property="barra_destreza" filter="false"/>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E1C08B" align="center" colspan="2">
                    <font color="#000000" size="1">
                        <html:link action="/mostrarMonje" paramId="clave" paramName="Cardenal"
                                   paramProperty="idDeMonje">
                            <bean:message key="monjes.abadia.masinfo"/>
                        </html:link>
                        <br>
                        <html:link action="/mostrar_produccion" paramId="clave" paramName="Cardenal"
                                   paramProperty="idDeMonje">
                            <html:img border="0" page="/images/iconos/16/ojo.gif" altKey="tooltip.produccion"/>
                        </html:link>
                    </font>
                </td>
            </tr>
        </table>
    </td>
    <td align="center">
        <!-- Opciones del Cardenal -->
        <table border="1" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000" bordercolordark="#000000"
               onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')"
               onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
            <tr>
                <td class="menu" width="90" align="center" valign="bottom">
                    <logic:equal value="0" name="Cardenal" property="estado">
                        <html:link action="/cardenalesImpuestos">
                            <html:img border="0" page="/images/iconos/monje_impuestos.gif"/>
                            <br/>
                            <bean:message key="diplomacia.abadia.obispo.impuestos"/>
                        </html:link>
                        <br>
                    </logic:equal>
                    <logic:notEqual value="0" name="Cardenal" property="estado">
                        <html:img border="0" page="/images/iconos/monje_impuestos_disabled.gif"/>
                        <br/>
                        <bean:message key="diplomacia.abadia.obispo.impuestos"/>
                    </logic:notEqual>
                </td>
                <td class="menu" width="90" align="center" valign="bottom">
                    <!-- Menú Función del cardenal -->
                    <logic:equal value="1" name="Funcion_Cardenal">
                        <logic:equal value="0" name="Cardenal" property="estado">
                            <html:link action="/lista_mercado_admin">
                                <html:img border="0" page="/images/iconos/monje_administrar_mercado.gif"/>
                                <br/>
                                <bean:message key="diplomacia.cardenal.funcion.mercado"/>
                            </html:link>
                            <br>
                        </logic:equal>
                    </logic:equal>
                    <logic:notEqual value="1" name="Funcion_Cardenal">
                        <logic:equal value="0" name="Cardenal" property="estado">
                            <html:img border="0" page="/images/iconos/monje_administrar_mercado_disabled.gif"/>
                            <br/>
                            <bean:message key="diplomacia.cardenal.funcion.mercado"/>
                        </logic:equal>
                    </logic:notEqual>
                </td>
                <!-- dimisiones Cardenal-->
                <td class="menu" width="90" align="center" valign="bottom">
                    <logic:equal value="0" name="Cardenal" property="estado">
                        <html:link action="/cardenal_dimitir" onclick="return confirmarDimisionCardenal();">
                            <html:img border="0" page="/images/iconos/dimitir_obispo.gif"/>
                            <br/>
                            <bean:message key="diplomacia.abadia.obispo.dimitir"/>
                        </html:link>
                        <br>
                    </logic:equal>
                    <logic:notEqual value="0" name="Cardenal" property="estado">
                        <html:img border="0" page="/images/iconos/dimitir_obispo.gif"/>
                        <br/>
                        <bean:message key="diplomacia.abadia.obispo.dimitir"/>
                    </logic:notEqual>
                </td>

                <td class="menu" width="90" align="center" valign="bottom">
                    <logic:equal value="0" name="Cardenal" property="estado">
                        <html:img border="0" page="/images/iconos/monje_dar_dinero_disabled.gif"/>
                        <bean:message key="diplomacia.jerarquia.prestardinero"/>
                    </logic:equal>
                    <logic:notEqual value="0" name="Cardenal" property="estado">
                        <html:img border="0" page="/images/iconos/monje_dar_dinero_disabled.gif"/>
                        <bean:message key="diplomacia.jerarquia.prestardinero"/>
                    </logic:notEqual>
                </td>
                <td class="menu" width="90" align="center" valign="bottom">
                    <logic:equal value="0" name="Cardenal" property="estado">
                        <html:link action="/buscar_abadia?opcion=2&region=0">
                            <html:img border="0" page="/images/iconos/monje_viajar.gif"/>
                            <bean:message key="diplomacia.jerarquia.viajar"/>
                        </html:link>
                    </logic:equal>
                    <logic:notEqual value="0" name="Cardenal" property="estado">
                        <html:img border="0" page="/images/iconos/monje_viajar_disabled.gif"/>
                        <bean:message key="diplomacia.jerarquia.viajar"/>
                    </logic:notEqual>
                </td>
                <!-- Menu del cónclave -->
                <td class="menu" width="90" align="center" valign="bottom">
                    <logic:equal value="2" name="Conclave">
                        <html:link action="/votar_papa">
                            <html:img border="0" page="/images/iconos/votar.gif"/>
                            <br/>
                            <bean:message key="principal.elecciones.votarPapa"/>
                        </html:link>
                        <br>
                    </logic:equal>
                    <logic:equal value="0" name="Conclave">
                        <html:img border="0" page="/images/iconos/votar_disabled.gif"/>
                        <br/>
                        <bean:message key="principal.elecciones.votarPapa"/>
                    </logic:equal>
                </td>
            </tr>
        </table>
    </td>
    </tr>
</table>
<br/>
</logic:present>


<br/>
<!-- PAPA -->
<logic:present name="Papa">
<table border="0" width="100%" cellspacing="4">
    <tr>
        <td colspan=2 bgcolor="#F0AF1C">
            <b>
                <bean:message key="diplomacia.abadia.papa"/>
                :</b>
        </td>
    </tr>
    <tr>
        <td width="200" align="center" valign="top">
            <table border="1" width="180" align="center" cellspacing="0" bordercolor="#000000"
                   bordercolorlight="#000000"
                   bordercolordark="#000000">
                <tr>
                    <td align="center" bgcolor="#F0AF1C" colspan="2">
                        <font color="#000000" size="1">
                            <bean:write name="Papa" property="nombre"/>
                            &nbsp;
                            <bean:message key="monjes.abadia.nomciudad"/>
                            &nbsp;
                            <bean:write name="Papa" property="primerApellido"/>
                        </font>
                    </td>
                </tr>
                <tr>
                    <td rowspan="2" align="center">
                        <img src="/images/iconos/monje_papa.png">
                    </td>
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
                        </font>
                    </td>
                </tr>
                <tr>
                    <td bgcolor="#E4CFA2" align="center">
                        <table width="100%" align="center" border=0>
                            <tr>
                                <td align="center"><font size=1>I:</font>
                                    <bean:write name="Papa" property="barra_idioma" filter="false"/>
                                </td>
                                <td align="center"><font size=1>T:</font>
                                    <bean:write name="Papa" property="barra_talento" filter="false"/>
                                </td>
                            </tr>
                            <tr>
                                <td align="center"><font size=1>C:</font>
                                    <bean:write name="Papa" property="barra_carisma" filter="false"/>
                                </td>
                                <td align="center"><font size=1>S:</font>
                                    <bean:write name="Papa" property="barra_sabiduria" filter="false"/>
                                </td>
                            </tr>
                            <tr>
                                <td align="center"><font size=1>F:</font>
                                    <bean:write name="Papa" property="barra_fuerza" filter="false"/>
                                </td>
                                <td align="center"><font size=1>D:</font>
                                    <bean:write name="Papa" property="barra_destreza" filter="false"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td bgcolor="#E1C08B" align="center" colspan="2">
                        <font color="#000000" size="1">
                            <html:link action="/mostrarMonje" paramId="clave" paramName="Papa"
                                       paramProperty="idDeMonje">
                                <bean:message key="monjes.abadia.masinfo"/>
                            </html:link>
                            <br>
                            <html:link action="/mostrar_produccion" paramId="clave" paramName="Papa"
                                       paramProperty="idDeMonje">
                                <html:img border="0" page="/images/iconos/16/ojo.gif" altKey="tooltip.produccion"/>
                            </html:link>
                        </font>
                    </td>
                </tr>

            </table>
        </td>
        <td align="center">
            <!-- Opciones del Papa -->
            <table border="1" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000" bordercolordark="#000000"
                   onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')"
                   onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
                <tr>
                    <td class="menu" width="90" align="center" valign="bottom">
                        <logic:equal value="0" name="Papa" property="estado">
                            <html:link action="/papadoImpuestos">
                                <html:img border="0" page="/images/iconos/monje_impuestos.gif"/>
                                <br/>
                                <bean:message key="diplomacia.abadia.obispo.impuestos"/>
                            </html:link>
                            <br>
                        </logic:equal>
                        <logic:notEqual value="0" name="Papa" property="estado">
                            <html:img border="0" page="/images/iconos/monje_impuestos_disabled.gif"/>
                            <br/>
                            <bean:message key="diplomacia.abadia.obispo.impuestos"/>
                        </logic:notEqual>
                    </td>
                    <td class="menu" width="90" align="center" valign="bottom">
                        <logic:equal value="0" name="Papa" property="estado">
                            <html:link action="/buscar_eminencias" paramId="monjeid" paramName="Papa"
                                       paramProperty="idDeMonje">
                                <html:img border="0" page="/images/iconos/monje_dar_dinero.gif"/>
                                <bean:message key="diplomacia.jerarquia.prestardinero"/>
                            </html:link>
                        </logic:equal>
                        <logic:notEqual value="0" name="Papa" property="estado">
                            <html:img border="0" page="/images/iconos/monje_dar_dinero_disabled.gif"/>
                            <bean:message key="diplomacia.jerarquia.prestardinero"/>
                        </logic:notEqual>
                    </td>
                    <td class="menu" width="90" align="center" valign="bottom">
                        <logic:equal value="0" name="Papa" property="estado">
                            <html:link action="/buscar_abadia?opcion=2&region=0">
                                <html:img border="0" page="/images/iconos/monje_viajar.gif"/>
                                <bean:message key="diplomacia.jerarquia.viajar"/>
                            </html:link>
                        </logic:equal>
                        <logic:notEqual value="0" name="Papa" property="estado">
                            <html:img border="0" page="/images/iconos/monje_viajar_disabled.gif"/>
                            <bean:message key="diplomacia.jerarquia.viajar"/>
                        </logic:notEqual>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<br/>
</logic:present>

</div>
</tr>
</table>
</div>
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
<!-- Mostrar la abadia congelada -->
<jsp:include page="/abadias/congelado.jsp"/>

</html>
