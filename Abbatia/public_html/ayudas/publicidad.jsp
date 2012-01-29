<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <meta name="GENERATOR" content="Microsoft FrontPage 4.0"/>
    <meta name="ProgId" content="FrontPage.Editor.Document"/>
    <link REL="STYLESHEET" HREF="../theme/styles.css" TYPE="text/css">
    <title>Pagina nueva 2</title>
    <base target="_self"/>
</head>
<body topmargin="2" leftmargin="4" bgcolor="#E1C08B" text="#000000">

&nbsp;
<div align="center">
    <center>
        <table borderColor="#000000" cellSpacing="0" borderColorDark="#000000" width="90%" borderColorLight="#000000"
               border="1">
            <tr>
                <td align="middle" bgColor="#996633">
                    <p align="center"><b><font color="#ffffff" size="2"><bean:message
                            key="publicidad.titulo"/></font></b></p>
                </td>
            </tr>
            <tr>
                <td bgColor="#e4cfa2">
                    <p align="left"><font face="Verdana, Tahoma, Arial" size="2"><br>
                        Para sufragar los gastos de mantenimiento de este juego ofrecemos
                        diferentes tipos de publicidad, por favor escoja la opción que más le
                        convenga para su negocio:</font></p>
                    <center>
                        <img src="/ayudas/images/publicidad_posiciones.jpg">
                    </center>
                    <p>Tráfico mensual: 20.000 acesos.</td>
            </tr>
            <tr>
                <td bgColor="#e4cfa2">
                    <u><b>Anuncio de 125px:<br>
                    </b></u>Localizado en la parte derecha de la página de inicio ofrece
                    una visión inmejorable y a primera vista para su anuncio.
                    <p><u>Especificaciones:<br>
                    </u>125x125 Pixels<br>
                        Tamaño máximo de 7K<br>
                        GIF estático, animado o JPG</p>

                    <p><u>Precio:<br>
                    </u>100 € + 16% IVA</p>
                </td>
            </tr>
            <tr>
                <td bgColor="#e4cfa2">
                    <u><b>Anuncio de 468px:<br>
                    </b></u>Localizado en la parte inferior de cada página de la web o en
                    la del inicio.
                    <p><u>Especificaciones:<br>
                    </u>468x60 Pixels<br>
                        Tamaño máximo de 20K<br>
                        GIF estático, animado, JPG o Flash</p>

                    <p><u>Precio:<br>
                    </u>200 € + 16% IVA&nbsp; ( todas las páginas del juego )<br>
                        150 € + 16% IVA&nbsp; ( sólo página de inicio )</p>
                </td>
            </tr>
        </table>
    </center>
</div>
&nbsp;
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
