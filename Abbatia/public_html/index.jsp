<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<html>

<head>
    <title>ABBATIA</title>
    <meta name="ROBOTS" content="index, follow">
    <meta name="description"
          content="Abbatia es un juego on-line gratuito (actualmente en construcción) ambientado en la época de los monasterios medievales.
Los jugadores se hacen cargo de la gestión y desarrollo de una Abadía en un condado, ducado o reino de la Europa del 1000. El objetivo es intentar llegar a lo más alto del escalafón eclesiástico convirtiendo tu Abadía en un referente cultural y/o económico de la época.
Para ello, tendrás que gestionar de forma adecuada los recursos de tu monasterio, elaborar productos, comerciar, formar monjes, copiar libros y mucho más... así poco a poco irás ampliando el poder de tu Abadía. ">
    <meta name="KEYWORDS"
          content="Abbatia, abadias, abadia, juego, on-line, gratuito, monasterios, monjes, circenses, año del señor, juegos">
    <LINK REL="SHORTCUT ICON" HREF="images/iconito.gif"/>
</head>

<frameset framespacing="0" border="0" rows="100,*" frameborder="0">
    <frame name="titulo" scrolling="no" noresize target="contenido" src="cabecera.do">
    <frameset cols="135,1,*">
        <logic:notEmpty name="usuario" scope="session">
            <frame name='contenido' target='principal' src='index_menu.do' scrolling='no' >
            <frame name='contenido' target='principal' src='index_border.html' scrolling='no' noresize>
            <frame name='principal' target='principal' src='main.do' marginwidth='0' marginheight='0' scrolling='auto'
                   noresize target='_self'>
        </logic:notEmpty>

        <logic:empty name="usuario" scope="session">
            <frame name='contenido' target='principal' src='index_main_menu.do' scrolling='no' >
            <frame name='contenido' target='principal' src='index_border.html' scrolling='no' noresize>
            <frame name='principal' target='principal' src='index_main.do' marginwidth='0' marginheight='0'
                   scrolling='auto' noresize target='_self'>
        </logic:empty>
    </frameset>
</frameset>

<script type="text/javascript" src="http://stats.abbatia.net/php-stats.js.php"></script>
<noscript><img src="http://stats.abbatia.net/php-stats.php" border="0" alt=""></noscript>

</html>
