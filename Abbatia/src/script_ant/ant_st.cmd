@ECHO OFF
SETLOCAL

@rem Parametrización del entorno

ant -f build_release_mod.xml -Dentorno=dv deploy-tomcat-desplegado
