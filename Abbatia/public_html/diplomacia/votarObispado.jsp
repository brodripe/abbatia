<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html:html>
    <head>
        <title></title>
        <link REL="STYLESHEET" HREF="theme/styles.css" TYPE="text/css">
    </head>
    <body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
    <div style="text-align: center;">

        <table border="1" width="80%" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000"
               bordercolordark="#000000">
            <tr>
                <td align="center" bgcolor="#996633">
                    <strong>
              <span style="color: #FFFFFF; font-size: x-small; ">
                <bean:message key="principal.elecciones.arzobispo"/> <bean:write name="DatosVotacion"
                                                                                 property="nombreRegion"/>
              </span>
                    </strong>
                </td>
            </tr>
            <tr>
                <td>
                    <table border="0" width="100%" cellspacing="2">
                        <tr>
                            <td align="right">
        	        <span style="color: #FFFFFF; font-size: x-small; ">
	                    <bean:message key="elecciones.candidatos.inicio"/>
        	        </span>
                            </td>
                            <td align="left">
	                <span style="color: #FFFFFF; font-size: x-small; ">
                	    <bean:write name="DatosVotacion" property="fechaInicio"/>
        	        </span>
                            </td>
                            <td align="right">
	                <span style="color: #FFFFFF; font-size: x-small; ">
        		            <bean:message key="elecciones.candidatos.fin"/>
	                </span>
                            </td>
                            <td align="left">
	                <span style="color: #FFFFFF; font-size: x-small; ">
                	    <bean:write name="DatosVotacion" property="fechaFin"/>
        	        </span>
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
        	        <span style="color: #FFFFFF; font-size: x-small; ">
	                    <bean:message key="elecciones.candidatos.restantes"/>
        	        </span>
                            </td>
                            <td align="left">
	                <span style="color: #FFFFFF; font-size: x-small; ">
                	    <bean:write name="DatosVotacion" property="pendientesVoto"/>
        	        </span>
                            </td>
                            <td align="right">
	                <span style="color: #FFFFFF; font-size: x-small; ">
        		            <bean:message key="elecciones.candidatos.total"/>
	                </span>
                            </td>
                            <td align="left">
	                <span style="color: #FFFFFF; font-size: x-small; ">
                	    <bean:write name="DatosVotacion" property="totalVotantes"/>
        	        </span>
                            </td>
                        </tr>

                    </table>
                </td>
            </tr>
            <tr>

                <td bgcolor="#E4CFA2" colspan="4">
                    <table border="1" width="100%" cellspacing="0" bordercolorlight="#808080" bordercolor="#808080"
                           bordercolordark="#808080">
                        <tr>
                            <td colspan="5" bgcolor="#E1C08B" align="center">
                                <strong>
                     <span style="color: #FFFFFF; ">
                       <bean:message key="elecciones.candidatos.lista"/>
                     </span>
                                </strong>
                            </td>
                        </tr>
                        <tr>
                            <td bgcolor="#E1C08B" align="center">
                                <strong>
                     <span style="color: #FFFFFF; ">
                       <bean:message key="elecciones.candidatos.tabla.candidato"/>
                     </span>
                                </strong>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <strong>
                     <span style="color: #FFFFFF; ">
                       <bean:message key="elecciones.candidatos.tabla.abadia"/>
                     </span>
                                </strong>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                   <span style="color: #FFFFFF; ">
                     <strong>
                         <bean:message key="elecciones.candidatos.tabla.orden"/>
                     </strong>
                   </span>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <strong>
                     <span style="color: #FFFFFF; ">
                       <bean:message key="elecciones.candidatos.tabla.votos"/>
                     </span>
                                </strong>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <logic:equal name="DatosVotacion" property="puedeVotar" value="1">
                                    <strong>
                         <span style="color: #FFFFFF; ">
                           <bean:message key="edificios.abadia.tabla.opciones"/>
                         </span>
                                    </strong>
                                </logic:equal>
                            </td>
                        </tr>
                        <logic:iterate id="lista" name="DatosVotacion" property="candidatos">
                            <tr>
                                <td>
                                    <span style="font-weight: bold;"><bean:write property="nombreMonje"
                                                                                 name="lista"/></span><br/>
                                    <bean:write property="descripcion" name="lista"/>
                                    <logic:equal name="lista" property="votable" value="0">
                                        <html:form action="/votar_obispado">
                                            <html:hidden property="seleccion" name="DatosVotacion" value="-1"/>
                                            <html:text property="descripcion" name="DatosVotacion" size="30"
                                                       maxlength="100"/>
                                            <html:submit value="Modificar"/>
                                        </html:form>
                                    </logic:equal>
                                </td>
                                <td align="center">
                                    <bean:write property="nombreAbadia" name="lista"/>
                                    <br/>
                                </td>
                                <td align="center">
                                    <bean:write property="nombreOrden" name="lista"/>
                                    <br/>
                                </td>
                                <td align="center">
                                    <bean:write property="votos" name="lista"/>
                                    <br/>
                                </td>
                                <td align="center">
                                    <logic:equal name="DatosVotacion" property="puedeVotar" value="1">
                                        <logic:equal name="lista" property="votable" value="1">
                                            <html:link action="/votar_obispado" paramId="seleccion" paramName="lista"
                                                       paramProperty="idAbadia">
                                                <html:img border="0" page="/images/iconos/16/votar.gif"
                                                          altKey="tooltip.votar"/>
                                            </html:link>
                                        </logic:equal>
                                    </logic:equal>
                                </td>
                            </tr>
                        </logic:iterate>
                    </table>
                </td>
            </tr>
        </table>
        <br/>

        <p>
            <logic:equal name="DatosVotacion" property="puedeVotar" value="0">
                       <span style="font-weight: bold;"><bean:message key="principal.elecciones.nopuedesvotar"/>
                       </span>
            </logic:equal>
        </p>
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
</html:html>
