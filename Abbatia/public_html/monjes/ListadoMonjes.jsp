<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title><bean:message key="monjes.abadia.titulo"/></title>
    <link REL="STYLESHEET" HREF="/theme/style-global.css" TYPE="text/css"/>
    <link REL="STYLESHEET" HREF="/theme/style-revised.css" TYPE="text/css"/>
</head>

<body>

<!-- Monjes en la abadia -->
<div id="content">
<div class="container">
<h4><bean:message key="monjes.abadia.titulo"/></h4>
<logic:iterate id="Monje" name="Monjes">
    <c:choose>
        <c:when test="${Monje.idDeJerarquia==6}">
            <c:set var="cssjerarquia" value="pope"/>
        </c:when>
        <c:when test="${Monje.idDeJerarquia==5}">
            <c:set var="cssjerarquia" value="cardinal"/>
        </c:when>
        <c:when test="${Monje.idDeJerarquia==3}">
            <c:set var="cssjerarquia" value="bishop"/>
        </c:when>
        <c:when test="${Monje.idDeJerarquia==2}">
            <c:set var="cssjerarquia" value="abad"/>
        </c:when>
        <c:when test="${Monje.idDeJerarquia==1}">
            <c:set var="cssjerarquia" value="monje"/>
        </c:when>
        <c:when test="${Monje.idDeJerarquia==0}">
            <c:set var="cssjerarquia" value="novicio"/>
        </c:when>
    </c:choose>

    <div class="dataDiplomacy">
        <table>
            <thead>
            <tr>
                <th class="${cssjerarquia}">
                        ${Monje.nombre} <bean:message key="monjes.abadia.nomciudad"/> ${Monje.primerApellido}
                        ${Monje.HTMLEnfermedad}
                </th>
            </tr>
            </thead>
            <tbody>
            <tr class="content">
                <td>
                    <dl>
                        <dt><bean:message key="monjes.abadia.edad"/></dt>
                        <dd>${Monje.edad}</dd>
                    </dl>
                    <dl>
                        <dt><bean:message key="monjes.abadia.salud"/> ${Monje.barra_salud}</dt>
                        <dd><bean:message key="monjes.abadia.fe"/> ${Monje.barra_fe}</dd>
                    </dl>
                </td>
            </tr>
            <tr class="content">
                <td>
                    <dl>
                        <dt>I: ${Monje.barra_idioma}</dt>
                        <dd>T: ${Monje.barra_talento}</dd>
                        <dt>C: ${Monje.barra_carisma}</dt>
                        <dd>S: ${Monje.barra_sabiduria}</dd>
                        <dt>F: ${Monje.barra_fuerza}</dt>
                        <dd>D: ${Monje.barra_destreza}</dd>
                    </dl>
                </td>
            </tr>
            </tbody>
            <tfoot>
            <tr>
                <td>
                    <html:link action="/mostrarMonje" paramId="clave" paramName="Monje"
                               paramProperty="idDeMonje">
                        <bean:message key="monjes.abadia.masinfo"/>
                    </html:link>
                    <br>
                    <html:link action="/mostrar_produccion" paramId="clave" paramName="Monje"
                               paramProperty="idDeMonje">
                        <html:img border="0" page="/images/iconos/16/ojo.gif"
                                  altKey="tooltip.produccion"/>
                    </html:link>
                </td>
            </tr>
            </tfoot>
        </table>
    </div>
</logic:iterate>

<div class="break"></div>
<!-- ENFERMOS -->
<logic:notEmpty name="Monjes_enfermos">
    <h4><bean:message key="monjes.abadia.enfermos"/></h4>
    <logic:iterate id="Monje" name="Monjes_enfermos">
        <c:choose>
            <c:when test="${Monje.idDeJerarquia==6}">
                <c:set var="cssjerarquia" value="pope"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==5}">
                <c:set var="cssjerarquia" value="cardinal"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==3}">
                <c:set var="cssjerarquia" value="bishop"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==2}">
                <c:set var="cssjerarquia" value="abad"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==1}">
                <c:set var="cssjerarquia" value="monje"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==0}">
                <c:set var="cssjerarquia" value="novicio"/>
            </c:when>
        </c:choose>
        <div class="dataDiplomacy">
            <table>
                <thead>
                <tr>
                    <th class="${cssjerarquia}">
                            ${Monje.nombre} <bean:message key="monjes.abadia.nomciudad"/> ${Monje.primerApellido}
                            ${Monje.HTMLEnfermedad}
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr class="content">
                    <td>
                        <dl>
                            <dt><bean:message key="monjes.abadia.edad"/></dt>
                            <dd>${Monje.edad}</dd>
                        </dl>
                        <dl>
                            <dt><bean:message key="monjes.abadia.salud"/> ${Monje.barra_salud}</dt>
                            <dd><bean:message key="monjes.abadia.fe"/> ${Monje.barra_fe}</dd>
                        </dl>
                    </td>
                </tr>
                <tr class="content">
                    <td>
                        <dl>
                            <dt>I: ${Monje.barra_idioma}</dt>
                            <dd>T: ${Monje.barra_talento}</dd>
                            <dt>C: ${Monje.barra_carisma}</dt>
                            <dd>S: ${Monje.barra_sabiduria}</dd>
                            <dt>F: ${Monje.barra_fuerza}</dt>
                            <dd>D: ${Monje.barra_destreza}</dd>
                        </dl>
                    </td>
                </tr>

                </tbody>
                <tfoot>
                <tr>
                    <td>
                        <html:link action="/mostrarMonje" paramId="clave" paramName="Monje"
                                   paramProperty="idDeMonje">
                            <bean:message key="monjes.abadia.masinfo"/>
                        </html:link>
                        <br>
                        <html:link action="/mostrar_produccion" paramId="clave" paramName="Monje"
                                   paramProperty="idDeMonje">
                            <html:img border="0" page="/images/iconos/16/ojo.gif"
                                      altKey="tooltip.produccion"/>
                        </html:link>
                    </td>
                </tr>
                </tfoot>
            </table>
        </div>
    </logic:iterate>
</logic:notEmpty>
<div class="break"></div>
<!-- VIAJANDO -->
<logic:notEmpty name="Monjes_viaje">
    <h4><bean:message key="monjes.abadia.viaje"/></h4>
    <logic:iterate id="Monje" name="Monjes_viaje">
        <c:choose>
            <c:when test="${Monje.idDeJerarquia==6}">
                <c:set var="cssjerarquia" value="pope"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==5}">
                <c:set var="cssjerarquia" value="cardinal"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==3}">
                <c:set var="cssjerarquia" value="bishop"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==2}">
                <c:set var="cssjerarquia" value="abad"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==1}">
                <c:set var="cssjerarquia" value="monje"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==0}">
                <c:set var="cssjerarquia" value="novicio"/>
            </c:when>
        </c:choose>
        <div class="dataDiplomacy">
            <table>
                <thead>
                <tr>
                    <th class="${cssjerarquia}">
                            ${Monje.nombre} <bean:message key="monjes.abadia.nomciudad"/> ${Monje.primerApellido}
                            ${Monje.HTMLEnfermedad}
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr class="content">
                    <td>
                        <dl>
                            <dt><bean:message key="monjes.abadia.edad"/></dt>
                            <dd>${Monje.edad}</dd>
                        </dl>
                        <dl>
                            <dt><bean:message key="monjes.abadia.salud"/> ${Monje.barra_salud}</dt>
                            <dd><bean:message key="monjes.abadia.fe"/> ${Monje.barra_fe}</dd>
                        </dl>
                    </td>
                </tr>
                <tr class="content">
                    <td>
                        <dl>
                            <dt><bean:message key="monjes.abadia.destino"/></dt>
                            <dd><bean:write name="Monje" property="abadia" filter="false"/> (<bean:write name="Monje"
                                                                                                         property="region"
                                                                                                         filter="false"/>)
                            </dd>
                            <dt><bean:message key="monjes.abadia.llegada"/></dt>
                            <dd><bean:write name="Monje" property="fechallegada" filter="false"/></dd>
                        </dl>
                    </td>
                </tr>
                </tbody>
                <tfoot>
                <tr>
                    <td>
                        <html:link action="/mostrarMonje" paramId="clave" paramName="Monje"
                                   paramProperty="idDeMonje">
                            <bean:message key="monjes.abadia.masinfo"/>
                        </html:link>
                        <br>
                        <html:link action="/mostrar_produccion" paramId="clave" paramName="Monje"
                                   paramProperty="idDeMonje">
                            <html:img border="0" page="/images/iconos/16/ojo.gif"
                                      altKey="tooltip.produccion"/>
                        </html:link>
                    </td>
                </tr>
                </tfoot>
            </table>
        </div>
    </logic:iterate>
</logic:notEmpty>
<div class="break"></div>
<!-- VISITA -->
<logic:notEmpty name="Monjes_visita">
    <h4><bean:message key="monjes.abadia.visita"/></h4>
    <logic:iterate id="Monje" name="Monjes_visita">
        <c:choose>
            <c:when test="${Monje.idDeJerarquia==6}">
                <c:set var="cssjerarquia" value="pope"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==5}">
                <c:set var="cssjerarquia" value="cardinal"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==3}">
                <c:set var="cssjerarquia" value="bishop"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==2}">
                <c:set var="cssjerarquia" value="abad"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==1}">
                <c:set var="cssjerarquia" value="monje"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==0}">
                <c:set var="cssjerarquia" value="novicio"/>
            </c:when>
        </c:choose>
        <div class="dataDiplomacy">
            <table>
                <thead>
                <tr>
                    <th class="${cssjerarquia}">
                            ${Monje.nombre} <bean:message key="monjes.abadia.nomciudad"/> ${Monje.primerApellido}
                            ${Monje.HTMLEnfermedad}
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr class="content">
                    <td>
                        <dl>
                            <dt><bean:message key="monjes.abadia.edad"/></dt>
                            <dd>${Monje.edad}</dd>
                        </dl>
                        <dl>
                            <dt><bean:message key="monjes.abadia.salud"/> ${Monje.barra_salud}</dt>
                            <dd><bean:message key="monjes.abadia.fe"/> ${Monje.barra_fe}</dd>
                        </dl>
                    </td>
                </tr>
                <tr class="content">
                    <td>
                        <dl>
                            <dt>I: ${Monje.barra_idioma}</dt>
                            <dd>T: ${Monje.barra_talento}</dd>
                            <dt>C: ${Monje.barra_carisma}</dt>
                            <dd>S: ${Monje.barra_sabiduria}</dd>
                            <dt>F: ${Monje.barra_fuerza}</dt>
                            <dd>D: ${Monje.barra_destreza}</dd>
                        </dl>
                    </td>
                <tr class="content">
                    <td>
                        <dl>
                            <dt><bean:message key="monjes.abadia.en"/></dt>
                            <dd><bean:write name="Monje" property="abadia" filter="false"/> (<bean:write name="Monje"
                                                                                                         property="region"
                                                                                                         filter="false"/>)
                            </dd>
                        </dl>
                    </td>
                </tr>
                </tbody>
                <tfoot>
                <tr>
                    <td>
                        <html:link action="/mostrarMonje" paramId="clave" paramName="Monje"
                                   paramProperty="idDeMonje">
                            <bean:message key="monjes.abadia.masinfo"/>
                        </html:link>
                        <br>
                        <html:link action="/mostrar_produccion" paramId="clave" paramName="Monje"
                                   paramProperty="idDeMonje">
                            <html:img border="0" page="/images/iconos/16/ojo.gif"
                                      altKey="tooltip.produccion"/>
                        </html:link>
                    </td>
                </tr>
                <tr>
                    <td>
                        <html:link action="/visita?action=marchar" paramId="clave" paramName="Monje"
                                   paramProperty="idDeMonje">
                            <bean:message key="monjes.abadia.visita.marchar"/>
                        </html:link><br>
                        <logic:greaterThan name="Monje" property="idDeJerarquia" value="2">
                            <html:link action="/visita?action=enfadado" paramId="clave"
                                       paramName="Monje" paramProperty="idDeMonje">
                                <bean:message key="monjes.abadia.visita.marchar.enfadado"/>
                            </html:link><br>
                        </logic:greaterThan>
                    </td>
                </tr>
                </tfoot>
            </table>
        </div>
    </logic:iterate>
</logic:notEmpty>
<div class="break"></div>
<!-- VISITA LA ABADIA -->
<logic:notEmpty name="Monjes_visita_miabadia">
    <h4><bean:message key="monjes.abadia.visita_miabadia"/></h4>
    <logic:iterate id="Monje" name="Monjes_visita_miabadia">
        <c:choose>
            <c:when test="${Monje.idDeJerarquia==6}">
                <c:set var="cssjerarquia" value="pope"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==5}">
                <c:set var="cssjerarquia" value="cardinal"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==3}">
                <c:set var="cssjerarquia" value="bishop"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==2}">
                <c:set var="cssjerarquia" value="abad"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==1}">
                <c:set var="cssjerarquia" value="monje"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==0}">
                <c:set var="cssjerarquia" value="novicio"/>
            </c:when>
        </c:choose>
        <div class="dataDiplomacy">
            <table>
                <thead>
                <tr>
                    <th class="${cssjerarquia}">
                            ${Monje.nombre} <bean:message key="monjes.abadia.nomciudad"/> ${Monje.primerApellido}
                            ${Monje.HTMLEnfermedad}
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr class="content">
                    <td>
                        <dl>
                            <dt><bean:message key="monjes.abadia.edad"/></dt>
                            <dd>${Monje.edad}</dd>
                        </dl>
                        <dl>
                            <dt><bean:message key="monjes.abadia.salud"/> ${Monje.barra_salud}</dt>
                            <dd><bean:message key="monjes.abadia.fe"/> ${Monje.barra_fe}</dd>
                        </dl>
                    </td>
                </tr>
                <tr class="content">
                    <td>
                        <dl>
                            <dt>I: ${Monje.barra_idioma}</dt>
                            <dd>T: ${Monje.barra_talento}</dd>
                            <dt>C: ${Monje.barra_carisma}</dt>
                            <dd>S: ${Monje.barra_sabiduria}</dd>
                            <dt>F: ${Monje.barra_fuerza}</dt>
                            <dd>D: ${Monje.barra_destreza}</dd>
                        </dl>
                    </td>
                </tr>
                <tr class="content">
                    <td>
                        <dl>
                            <dt><bean:message key="monjes.abadia.de"/>:</dt>
                            <dd><bean:write name="Monje" property="abadia" filter="false"/> (<bean:write name="Monje"
                                                                                                         property="region"
                                                                                                         filter="false"/>)
                            </dd>
                        </dl>
                    </td>
                </tr>
                </tbody>
                <tfoot>
                <tr>
                    <td>
                        <html:link action="/mostrarMonje" paramId="clave" paramName="Monje"
                                   paramProperty="idDeMonje">
                            <bean:message key="monjes.abadia.masinfo"/>
                        </html:link>
                        <br>
                        <html:link action="/mostrar_produccion" paramId="clave" paramName="Monje"
                                   paramProperty="idDeMonje">
                            <html:img border="0" page="/images/iconos/16/ojo.gif"
                                      altKey="tooltip.produccion"/>
                        </html:link>
                    </td>
                </tr>
                <tr>
                    <td>
                        <html:link action="/visita?action=expulsar&" paramId="clave"
                                   paramName="Monje" paramProperty="idDeMonje">
                            <bean:message key="monjes.abadia.visita.expulsar"/>
                        </html:link><br>
                    </td>
                </tr>
                </tfoot>
            </table>
        </div>
    </logic:iterate>
</logic:notEmpty>
<div class="break"></div>
<!-- VELATORIO -->
<logic:notEmpty name="Monjes_muertos">
    <h4><bean:message key="monjes.abadia.velatorio"/></h4>
    <logic:iterate id="Monje" name="Monjes_muertos">
        <c:choose>
            <c:when test="${Monje.idDeJerarquia==6}">
                <c:set var="cssjerarquia" value="pope"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==5}">
                <c:set var="cssjerarquia" value="cardinal"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==3}">
                <c:set var="cssjerarquia" value="bishop"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==2}">
                <c:set var="cssjerarquia" value="abad"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==1}">
                <c:set var="cssjerarquia" value="monje"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==0}">
                <c:set var="cssjerarquia" value="novicio"/>
            </c:when>
        </c:choose>
        <div class="dataDiplomacy">
            <table>
                <thead>
                <tr>
                    <th class="${cssjerarquia}">
                            ${Monje.nombre} <bean:message key="monjes.abadia.nomciudad"/> ${Monje.primerApellido}
                            ${Monje.HTMLEnfermedad}
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr class="content">
                    <td>
                        <dl>
                            <dt><bean:message key="monjes.abadia.edad"/></dt>
                            <dd>${Monje.edad}</dd>
                            <dt><bean:message key="monjes.abadia.nacimiento"/></dt>
                            <dd>${Monje.fechaDeNacimiento}</dd>
                            <dt><bean:message key="monjes.abadia.fecha_defuncion"/></dt>
                            <dd>${Monje.fechaDeFallecimiento}</dd>
                        </dl>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </logic:iterate>
</logic:notEmpty>
<div class="break"></div>
<!-- CEMENTERIO -->
<logic:notEmpty name="Monjes_cementerio">
    <h4><bean:message key="monjes.abadia.cementerio"/></h4>
    <logic:iterate id="Monje" name="Monjes_cementerio">
        <c:choose>
            <c:when test="${Monje.idDeJerarquia==6}">
                <c:set var="cssjerarquia" value="pope"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==5}">
                <c:set var="cssjerarquia" value="cardinal"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==3}">
                <c:set var="cssjerarquia" value="bishop"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==2}">
                <c:set var="cssjerarquia" value="abad"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==1}">
                <c:set var="cssjerarquia" value="monje"/>
            </c:when>
            <c:when test="${Monje.idDeJerarquia==0}">
                <c:set var="cssjerarquia" value="novicio"/>
            </c:when>
        </c:choose>
        <div class="dataDiplomacy">
            <table>
                <thead>
                <tr>
                    <th class="${cssjerarquia}">
                            ${Monje.nombre} <bean:message key="monjes.abadia.nomciudad"/> ${Monje.primerApellido}
                            ${Monje.HTMLEnfermedad}
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr class="content">
                    <td>
                        <dl>
                            <dt><bean:message key="monjes.abadia.edad"/></dt>
                            <dd>${Monje.edad}</dd>
                            <dt><bean:message key="monjes.abadia.nacimiento"/></dt>
                            <dd>${Monje.fechaDeNacimiento}</dd>
                            <dt><bean:message key="monjes.abadia.fecha_defuncion"/></dt>
                            <dd>${Monje.fechaDeFallecimiento}</dd>
                        </dl>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </logic:iterate>
</logic:notEmpty>
</div>
</div>
<%--
<!-- Monjes en la abadia -->
<table borderColor="#000000" width="90%" cellSpacing="0" align="center" borderColorDark="#000000"
       borderColorLight="#000000" border="1">
<tr>
    <td bgColor="#996633">
        <p align="center"><font color="#FFFFFF"><b><bean:message key="monjes.abadia.titulo"/></b></font></p>
    </td>
</tr>
<tr>
    <td>
        <table cellspacing="0" border="0" background="images/backgrounds/papel.jpg" width="100%" align="center">

            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <%
                    int n = 0;
                %>
                <logic:iterate id="Monje" name="Monjes">
                    <td>

                        <table border="1" width="130" align="center" cellspacing="0" bordercolor="#000000"
                               bordercolorlight="#000000" bordercolordark="#000000">
                            <tr>
                                <td align="center" bgcolor="#F0AF1C">
                                    <font color="#000000" size="1">
                                        <bean:write name="Monje" property="nombre"/>&nbsp;<bean:message
                                            key="monjes.abadia.nomciudad"/>&nbsp;<bean:write name="Monje"
                                                                                             property="primerApellido"/>
                                        <bean:write name="Monje" property="HTMLEnfermedad" filter="false"/>
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
                                    <table width="100%" align="center" border=0>
                                        <tr>
                                            <td align="center"><font size=1>I:</font><bean:write name="Monje"
                                                                                                 property="barra_idioma"
                                                                                                 filter="false"/></td>
                                            <td align="center"><font size=1>T:</font><bean:write name="Monje"
                                                                                                 property="barra_talento"
                                                                                                 filter="false"/></td>
                                        </tr>
                                        <tr>
                                            <td align="center"><font size=1>C:</font><bean:write name="Monje"
                                                                                                 property="barra_carisma"
                                                                                                 filter="false"/></td>
                                            <td align="center"><font size=1>S:</font><bean:write name="Monje"
                                                                                                 property="barra_sabiduria"
                                                                                                 filter="false"/></td>
                                        </tr>
                                        <tr>
                                            <td align="center"><font size=1>F:</font><bean:write name="Monje"
                                                                                                 property="barra_fuerza"
                                                                                                 filter="false"/></td>
                                            <td align="center"><font size=1>D:</font><bean:write name="Monje"
                                                                                                 property="barra_destreza"
                                                                                                 filter="false"/></td>
                                        </tr>
                                    </table>
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
            <tr>
                <td>&nbsp;</td>
            </tr>
        </table>
    </td>
</tr>--%>

<!-- ENFERMOS -->
<%--<logic:notEmpty name="Monjes_enfermos">
    <tr>
        <td bgColor="#996633">
            <p align="center"><font color="#FFFFFF"><b><bean:message key="monjes.abadia.enfermos"/></b></font></p>
        </td>
    </tr>
    <tr>
        <td>
            <table cellspacing="0" border="0" background="images/backgrounds/papel.jpg" width="100%" align="center">

                <tr>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <%
                        n = 0;
                    %>
                    <logic:iterate id="Monje" name="Monjes_enfermos">
                        <td>

                            <table border="1" width="130" align="center" cellspacing="0" bordercolor="#000000"
                                   bordercolorlight="#000000" bordercolordark="#000000">
                                <tr>
                                    <td align="center" bgcolor="#F0AF1C">
                                        <font color="#000000" size="1">
                                            <bean:write name="Monje" property="nombre"/>&nbsp;<bean:message
                                                key="monjes.abadia.nomciudad"/>&nbsp;<bean:write name="Monje"
                                                                                                 property="primerApellido"/>
                                            <bean:write name="Monje" property="HTMLEnfermedad" filter="false"/>
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
                                        <table width="100%" align="center" border=0>
                                            <tr>
                                                <td align="center"><font size=1>I:</font><bean:write name="Monje"
                                                                                                     property="barra_idioma"
                                                                                                     filter="false"/>
                                                </td>
                                                <td align="center"><font size=1>T:</font><bean:write name="Monje"
                                                                                                     property="barra_talento"
                                                                                                     filter="false"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td align="center"><font size=1>C:</font><bean:write name="Monje"
                                                                                                     property="barra_carisma"
                                                                                                     filter="false"/>
                                                </td>
                                                <td align="center"><font size=1>S:</font><bean:write name="Monje"
                                                                                                     property="barra_sabiduria"
                                                                                                     filter="false"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td align="center"><font size=1>F:</font><bean:write name="Monje"
                                                                                                     property="barra_fuerza"
                                                                                                     filter="false"/>
                                                </td>
                                                <td align="center"><font size=1>D:</font><bean:write name="Monje"
                                                                                                     property="barra_destreza"
                                                                                                     filter="false"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td bgcolor="#E1C08B" align="center">
                                        <font color="#000000" size="1">
                                            <html:link action="/mostrarMonje" paramId="clave" paramName="Monje"
                                                       paramProperty="idDeMonje">
                                                <bean:message key="monjes.abadia.masinfo"/>
                                            </html:link><br>
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
                <tr>
                    <td>&nbsp;</td>
                </tr>
            </table>
        </td>
    </tr>
</logic:notEmpty>--%>


<!-- VIAJANDO -->
<%--<logic:notEmpty name="Monjes_viaje">
    <tr>
        <td bgColor="#996633">
            <p align="center"><font color="#FFFFFF"><b><bean:message key="monjes.abadia.viaje"/></b></font></p>
        </td>
    </tr>
    <tr>
        <td>
            <table cellspacing="0" border="0" background="images/backgrounds/papel.jpg" width="100%" align="center">

                <tr>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <%
                        n = 0;
                    %>
                    <logic:iterate id="Monje" name="Monjes_viaje">
                        <td>
                            <table border="1" width="130" align="center" cellspacing="0" bordercolor="#000000"
                                   bordercolorlight="#000000" bordercolordark="#000000">
                                <tr>
                                    <td align="center" bgcolor="#F0AF1C">
                                        <font color="#000000" size="1">
                                            <bean:write name="Monje" property="nombre"/>&nbsp;<bean:message
                                                key="monjes.abadia.nomciudad"/>&nbsp;<bean:write name="Monje"
                                                                                                 property="primerApellido"/>
                                            <bean:write name="Monje" property="HTMLEnfermedad" filter="false"/>
                                        </font>
                                    </td>
                                </tr>
                                <tr>
                                    <td bgcolor="#E4CFA2" align="center">
                                        <font color="#000000" size="1">
                                            <bean:message key="monjes.abadia.edad"/>
                                            <bean:write name="Monje" property="edad"/>
                                        </font>
                                        <br>
                                        <bean:message key="monjes.abadia.salud"/>
                                        <bean:write name="Monje" property="barra_salud" filter="false"/>
                                        <br>
                                    </td>
                                </tr>
                                <tr>
                                    <td bgcolor="#E4CFA2" align="center">
                                        <bean:message key="monjes.abadia.destino"/><br>
                                        <bean:write name="Monje" property="abadia" filter="false"/><br>
                                        <bean:write name="Monje" property="region" filter="false"/><br>
                                    </td>
                                </tr>
                                <tr>
                                    <td bgcolor="#E4CFA2" align="center">
                                        <bean:message key="monjes.abadia.llegada"/><br>
                                        <bean:write name="Monje" property="fechallegada" filter="false"/>
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
</logic:notEmpty>--%>

<!-- VISITA -->
<%--<logic:notEmpty name="Monjes_visita">
    <tr>
        <td bgColor="#996633">
            <p align="center"><font color="#FFFFFF"><b><bean:message key="monjes.abadia.visita"/></b></font></p>
        </td>
    </tr>
    <tr>
        <td>
            <table cellspacing="0" border="0" background="images/backgrounds/papel.jpg" width="100%" align="center">

                <tr>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <%
                        n = 0;
                    %>
                    <logic:iterate id="Monje" name="Monjes_visita">
                        <td>
                            <table border="1" width="130" align="center" cellspacing="0" bordercolor="#000000"
                                   bordercolorlight="#000000" bordercolordark="#000000">
                                <tr>
                                    <td align="center" bgcolor="#F0AF1C">
                                        <font color="#000000" size="1">
                                            <bean:write name="Monje" property="nombre"/>&nbsp;<bean:message
                                                key="monjes.abadia.nomciudad"/>&nbsp;<bean:write name="Monje"
                                                                                                 property="primerApellido"/>
                                            <bean:write name="Monje" property="HTMLEnfermedad" filter="false"/>
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
                                        <table width="100%" align="center" border=0>
                                            <tr>
                                                <td align="center"><font size=1>I:</font><bean:write name="Monje"
                                                                                                     property="barra_idioma"
                                                                                                     filter="false"/>
                                                </td>
                                                <td align="center"><font size=1>T:</font><bean:write name="Monje"
                                                                                                     property="barra_talento"
                                                                                                     filter="false"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td align="center"><font size=1>C:</font><bean:write name="Monje"
                                                                                                     property="barra_carisma"
                                                                                                     filter="false"/>
                                                </td>
                                                <td align="center"><font size=1>S:</font><bean:write name="Monje"
                                                                                                     property="barra_sabiduria"
                                                                                                     filter="false"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td align="center"><font size=1>F:</font><bean:write name="Monje"
                                                                                                     property="barra_fuerza"
                                                                                                     filter="false"/>
                                                </td>
                                                <td align="center"><font size=1>D:</font><bean:write name="Monje"
                                                                                                     property="barra_destreza"
                                                                                                     filter="false"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td bgcolor="#E4CFA2" align="center">
                                        <b><bean:message key="monjes.abadia.en"/>:</b><br>
                                        <bean:write name="Monje" property="abadia" filter="false"/><br>
                                        <bean:write name="Monje" property="region" filter="false"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td bgcolor="#E1C08B" align="center">
                                        <font color="#000000" size="1">
                                            <html:link action="/mostrarMonje" paramId="clave" paramName="Monje"
                                                       paramProperty="idDeMonje">
                                                <bean:message key="monjes.abadia.masinfo"/>
                                            </html:link><br>
                                        </font>
                                    </td>
                                </tr>
                                <tr>
                                    <td bgcolor="#E1C08B" align="center">
                                        <font color="#000000" size="1">
                                            <html:link action="/visita?action=marchar" paramId="clave" paramName="Monje"
                                                       paramProperty="idDeMonje">
                                                <bean:message key="monjes.abadia.visita.marchar"/>
                                            </html:link><br>
                                            <logic:greaterThan name="Monje" property="idDeJerarquia" value="2">
                                                <html:link action="/visita?action=enfadado" paramId="clave"
                                                           paramName="Monje" paramProperty="idDeMonje">
                                                    <bean:message key="monjes.abadia.visita.marchar.enfadado"/>
                                                </html:link><br>
                                            </logic:greaterThan>
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
                <tr>
                    <td>&nbsp;</td>
                </tr>
            </table>
        </td>
    </tr>
</logic:notEmpty>--%>


<!-- VISITA LA ABADIA -->
<%--<logic:notEmpty name="Monjes_visita_miabadia">
    <tr>
        <td bgColor="#996633">
            <p align="center"><font color="#FFFFFF"><b><bean:message key="monjes.abadia.visita_miabadia"/></b></font>
            </p>
        </td>
    </tr>
    <tr>
        <td>
            <table cellspacing="0" border="0" background="images/backgrounds/papel.jpg" width="100%" align="center">

                <tr>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <%
                        n = 0;
                    %>
                    <logic:iterate id="Monje" name="Monjes_visita_miabadia">
                        <td>
                            <table border="1" width="130" align="center" cellspacing="0" bordercolor="#000000"
                                   bordercolorlight="#000000" bordercolordark="#000000">
                                <tr>
                                    <td align="center" bgcolor="#F0AF1C">
                                        <font color="#000000" size="1">
                                            <bean:write name="Monje" property="nombre"/>&nbsp;<bean:message
                                                key="monjes.abadia.nomciudad"/>&nbsp;<bean:write name="Monje"
                                                                                                 property="primerApellido"/>
                                            <bean:write name="Monje" property="HTMLEnfermedad" filter="false"/>
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
                                        <b><bean:message key="monjes.abadia.de"/>:</b><br>
                                        <bean:write name="Monje" property="abadia" filter="false"/><br>
                                        <bean:write name="Monje" property="region" filter="false"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td bgcolor="#E1C08B" align="center">
                                        <font color="#000000" size="1">
                                            <html:link action="/mostrarMonje" paramId="clave" paramName="Monje"
                                                       paramProperty="idDeMonje">
                                                <bean:message key="monjes.abadia.masinfo"/>
                                            </html:link><br>
                                        </font>
                                    </td>
                                </tr>
                                <tr>
                                    <td bgcolor="#E1C08B" align="center">
                                        <font color="#000000" size="1">
                                            <html:link action="/visita?action=expulsar&" paramId="clave"
                                                       paramName="Monje" paramProperty="idDeMonje">
                                                <bean:message key="monjes.abadia.visita.expulsar"/>
                                            </html:link><br>
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
                <tr>
                    <td>&nbsp;</td>
                </tr>
            </table>
        </td>
    </tr>
</logic:notEmpty>--%>


<!-- VELATORIO -->
<%--<logic:notEmpty name="Monjes_muertos">
    <tr>
        <td bgColor="#996633">
            <p align="center"><font color="#FFFFFF"><b><bean:message key="monjes.abadia.velatorio"/></b></font></p>
        </td>
    </tr>
    <tr>
        <td>
            <table cellspacing="0" border="0" background="images/backgrounds/papel.jpg" width="100%" align="center">

                <tr>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <%
                        n = 0;
                    %>
                    <logic:iterate id="Monje" name="Monjes_muertos">
                        <td>
                            <table border="1" width="130" align="center" cellspacing="0" bordercolor="#000000"
                                   bordercolorlight="#000000" bordercolordark="#000000">
                                <tr>
                                    <td align="center" bgcolor="#F0AF1C">
                                        <font color="#000000" size="1">
                                            <bean:write name="Monje" property="nombre"/>&nbsp;<bean:message
                                                key="monjes.abadia.nomciudad"/>&nbsp;<bean:write name="Monje"
                                                                                                 property="primerApellido"/>
                                            <bean:write name="Monje" property="HTMLEnfermedad" filter="false"/>
                                        </font>
                                    </td>
                                </tr>
                                <tr>
                                    <td bgcolor="#E4CFA2" align="center">
                                        <font color="#000000" size="1">
                                            <bean:message key="monjes.abadia.edad"/>
                                            <bean:write name="Monje" property="edad"/>
                                            <br>
                                            <bean:message key="monjes.abadia.nacimiento"/>
                                            <bean:write name="Monje" property="fechaDeNacimiento"/>
                                            <bean:message key="monjes.abadia.fecha_defuncion"/>
                                            <bean:write name="Monje" property="fechaDeFallecimiento"/>
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
                <tr>
                    <td>&nbsp;</td>
                </tr>
            </table>
        </td>
    </tr>
</logic:notEmpty>
</table>

<!-- CEMENTERIO -->
<p>&nbsp;</p>

<logic:notEmpty name="Monjes_cementerio">
    <table borderColor="#000000" width="90%" cellSpacing="0" align="center" borderColorDark="#000000"
           borderColorLight="#000000" border="1">
        <tr>
            <td bgColor="#996633">
                <p align="center"><font color="#FFFFFF"><b><bean:message key="monjes.abadia.cementerio"/></b></font></p>
            </td>
        </tr>
        <tr>
            <td>
                <table cellspacing="0" border="0" background="images/backgrounds/marmol.gif" width="100%"
                       align="center">
                    <tr>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <%
                            n = 0;
                        %>
                        <logic:iterate id="Monje1" name="Monjes_cementerio">
                            <td>

                                <table border="1" width="130" align="center" cellspacing="0" bordercolor="#000000"
                                       bordercolorlight="#000000" bordercolordark="#000000">
                                    <tr>
                                        <td align="center" bgcolor="#8B1411">
                                            <font color="#FFFFFF" size="1">
                                                <bean:write name="Monje1" property="nombre"/>&nbsp;<bean:message
                                                    key="monjes.abadia.nomciudad"/>&nbsp;<bean:write name="Monje1"
                                                                                                     property="primerApellido"/>
                                            </font>
                                        </td>
                                        <td>
                                            <DIV align="center">
                                                <html:img border="0" page="/images/iconos/16/cementerio.gif"/>
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
                    <tr>
                        <td>&nbsp;</td>
                    </tr>
                </table>
    </table>
</logic:notEmpty>--%>

<!-- Publicidad -->
<logic:notEqual property="registrado" name="usuario" scope="session" value="1">
    <jsp:include page="/varios/publicidad.jsp"/>
</logic:notEqual>

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
<jsp:include page="/abadias/congelado.jsp"/>
</html>
