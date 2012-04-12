package org.abbatia.utils;

public class Constantes {
    public static String TABLA_IDIOMA = "idioma";
    public static String TABLA_REGION = "region";
    public static String TABLA_ORDEN = "orden_eclesiastica";
    public static String TABLA_ACTIVIDAD = "ACTIVI";
    public static String TABLA_PAIS = "PAIS";
    public static String TABLA_EDAD = "EDAD";
    public static String TABLA_SEXO = "SEXO";

    public static String USER_KEY = "usuario";
    public static short USUARIO_ADMINISTRADOR = 1;
    public static short USUARIO_PENDIENTE_ALTA = 99;
    public static short USUARIO_COLABORADOR = 2;
    public static short USUARIO_COLABORADOR_PLUS = 3;
    public static short USUARIO_BASICO = 0;
    public static String ABADIA = "abadia";
    public static String PAGE_SIZE = "pagesize";
    public static String MENSAJES = "mensajes";
    public static String WELCOME = "success";
    public static int DEBUG = 1;
    public static int NICK_YA_EXISTE = -1;
    public static int EMAIL_YA_EXISTE = -2;
    public static int REGISTRO_USUARIO_OK = 0;
    public static final String ERROR = "1";
    public static final String MISMA_FECHA = "00";
    public static final String FECHA_MAYOR = "02";
    public static final String FECHA_MENOR = "03";
    public static final String FORMATO_FECHA_PANTALLA = "dd-MM-yyyy";
    public static final String FORMATO_FECHA_BD = "yyyy-MM-dd";
    public static final String FEC_DEFECTO = "9999-01-01";

    // DB
    public static int DB_TIMEOUT = 200;
    public static int DB_TIMEOUT_PROCESS = 1000;
    public static String DB_CONEXION_PROCESS = "Procesos";
    public static String DB_CONEXION_ABADIAS = "Abadias";
    public static boolean AUTOCOMIT_ON = true;
    public static boolean AUTOCOMIT_OF = false;

    //Constantes para su uso en los procesos


    //DEFINICION DE CONSTANTES PARA HABILIDADES
    public static final int HABILIDAD_IDIOMA = 20;
    public static final int HABILIDAD_FE = 21;
    public static final int HABILIDAD_FUERZA = 22;
    public static final int HABILIDAD_DESTREZA = 23;
    public static final int HABILIDAD_TALENTO = 24;
    public static final int HABILIDAD_SABIDURIA = 25;
    public static final int HABILIDAD_CARISMA = 26;
    public static final int HABILIDAD_POPULARIDAD = 27;

    public static final short TAREA_NINGUNA = 0;
    public static final short TAREA_REZAR = 1;
    public static final short TAREA_AGRICULTURA = 2;
    public static final short TAREA_TALAR = 3;
    public static final short TAREA_PESCAR = 4;
    public static final short TAREA_CURAR = 6;
    public static final short TAREA_COPIAR = 8;
    public static final short TAREA_ENSENAR = 9;
    public static final short TAREA_ABAD = 10;
    public static final short TAREA_GANADERIA = 11;
    public static final short TAREA_COMER = 12;
    public static final short TAREA_MOLINERO = 13;
    public static final short TAREA_ELEBORAR_ALIMENTO = 14;
    public static final short TAREA_ELABORAR_RECURSO = 15;
    public static final short TAREA_ELABORAR_COSTURA = 16;
    public static final short TAREA_ELABORAR_ARTESANIA = 17;
    public static final short TAREA_APRENDER = 18;
    public static final short TAREA_RESTAURAR_LIBROS = 19;

    //DEFINICION DE CONSTANTES PARA PROPIEDADES
    public static final int PROPIEDAD_NIVEL = 5;
    public static final int PROPIEDAD_PROTEINAS = 6;
    public static final int PROPIEDAD_LIPIDOS = 7;
    public static final int PROPIEDAD_HIDRATOS = 8;
    public static final int PROPIEDAD_VITAMINAS = 9;

    //definición de los nombres de las propiedades
    public static final String PROPIEDAD_NOMBRE_VITAMINAS = "VITAMINAS";
    public static final String PROPIEDAD_NOMBRE_PROTEINAS = "PROTEINAS";
    public static final String PROPIEDAD_NOMBRE_HIDRATOS = "HIDRATOS_CARBONO";
    public static final String PROPIEDAD_NOMBRE_LIPIDOS = "LIPIDOS";

    //parametros de la tabla 'propiedad_valor'
    public static short PROPIEDAD_TIPO_PARAMETROS = 10001;
    public static short PROPIEDAD_PUNTOS_SICARIO = 1;


    //DEFINICION DE CONSTANTES PARA ACTIVIDADES
    public static final int ACTIVIDAD_ESCRIBAS = 3;
    public static final int ACTIVIDAD_GANADERIA = 2;
    public static final int ACTIVIDAD_AGRICULTURA = 1;

    //DEFINICION DE CONSTANTES PARA PERIODOS
    public static final int PERIODO_NADA = 0;
    public static final int PERIODO_MAITINES = 1;
    public static final int PERIODO_LAUDES = 2;
    public static final int PERIODO_PRIMA = 3;
    public static final int PERIODO_TERCIA = 4;
    public static final int PERIODO_SEXTA = 5;
    public static final int PERIODO_NONA = 6;
    public static final int PERIODO_VISPERAS = 7;
    public static final int PERIODO_ANGELUS = 8;

    public static final String PERIODO_PRIMA_DESC = "Prima";
    public static final String PERIODO_MAITINES_DESC = "Maitines";
    public static final String PERIODO_LAUDES_DESC = "Laudes";
    public static final String PERIODO_TERCIA_DESC = "Tercia";
    public static final String PERIODO_SEXTA_DESC = "Sexta";
    public static final String PERIODO_NONA_DESC = "Nona";
    public static final String PERIODO_VISPERAS_DESC = "Visperas";
    public static final String PERIODO_ANGELUS_DESC = "Angelus";

    //EDIFICIOS_tipo
    public static final int EDIFICIO_MUROS = 0;
    public static final int EDIFICIO_ORATORIO = 1;
    public static final int EDIFICIO_CLAUSTRO = 2;
    public static final int EDIFICIO_DORMITORIO = 3;
    public static final int EDIFICIO_COMEDOR = 4;
    public static final int EDIFICIO_COCINA = 5;
    public static final int EDIFICIO_BIBLIOTECA = 6;
    public static final int EDIFICIO_GRANJA = 7;
    public static final int EDIFICIO_GRANERO = 8;
    public static final int EDIFICIO_MOLINO = 9;
    public static final int EDIFICIO_TALLER_COSTURA = 10;
    public static final int EDIFICIO_TALLER_ARTESANIA = 11;
    public static final int EDIFICIO_ALMACEN = 12;
    public static final int EDIFICIO_ENFERMERIA = 13;
    public static final int EDIFICIO_NOVICIADO = 14;
    public static final int EDIFICIO_ESCUELA = 15;
    public static final int EDIFICIO_HOSPEDERIA = 16;
    public static final int EDIFICIO_CAPITULAR = 17;
    public static final int EDIFICIO_RETRETE = 18;
    public static final int EDIFICIO_CORO = 19;
    public static final int EDIFICIO_CALEFACTORIO = 20;
    public static final int EDIFICIO_ESTABLO = 22;
    public static final int EDIFICIO_CAMPO = 23;
    public static final int EDIFICIO_IGLESIA = 24;
    public static final int EDIFICIO_MERCADO = 25;
    public static final int EDIFICIO_CEMENTERIO = 26;
    public static final int EDIFICIO_ABADIA = 99;

    //tipos de mantenimiento por edificio
    public static final int EDIFICIO_MANTENIMIENTO_INEXISTENTE = 0;
    public static final int EDIFICIO_MANTENIMIENTO_BASICO = 1;
    public static final int EDIFICIO_MANTENIMIENTO_MODERADO = 2;
    public static final int EDIFICIO_MANTENIMIENTO_INTENSIVO = 3;

    //estado cultivo
    public static final short ESTADO_CULTIVO_INACTIVO = 0;
    public static final short ESTADO_CULTIVO_ARANDO = 1;
    public static final short ESTADO_CULTIVO_ARADO = 2;
    public static final short ESTADO_CULTIVO_SEMBRANDO = 3;
    public static final short ESTADO_CULTIVO_CULTIVANDO = 4;
    public static final short ESTADO_CULTIVO_RECOGIENDO = 5;

    //varios cultivos
    public static final int CULTIVO_TOTAL_ARADO = 24000;
    public static final double CULTIVO_PUNTOS_ARADO_CABALLO = 2;
    public static final double CULTIVO_PUNTOS_ARADO_BUEY = 1.85;
    public static final double CULTIVO_PUNTOS_ARADO_AZADA = 1.5;


    //estado de elaboración de productos
    public static final int ESTADO_ELABORACION_ELABORANDO = 1;
    public static final int ESTADO_ELABORACION_DETENIDA_R = 2;
    public static final int ESTADO_ELABORACION_DETENIDA_M = 3;
    public static final int ESTADO_ELABORACION_FINALIZADO = 4;
    public static final int ESTADO_ELABORACION_REPOSANDO = 5;
    public static final int ESTADO_ELABORACION_PENDIENTE = 6;
    public static final int ESTADO_ELABORACION_PAUSADO = 7;

    //estado de las tareas-libro
    public static final short ESTADO_TAREA_COPIA_PENDIENTE = 0;
    public static final short ESTADO_TAREA_COPIA_ACTIVO = 1;

    //estados de los libros
    public static final short ESTADO_LIBRO_PENDIENTE = 0;
    public static final short ESTADO_LIBRO_INCOMPLETO = 1;
    public static final short ESTADO_LIBRO_COMPLETO = 2;
    public static final short ESTADO_LIBRO_COPIANDOSE = 3;
    public static final short ESTADO_LIBRO_VIAJANDO = 4;
    public static final short ESTADO_LIBRO_SIN_ENCUADERNAR = 5;
    public static final short ESTADO_LIBRO_ENCUADERNANDOSE = 6;
    public static final short ESTADO_LIBRO_DETERIORADO = 7;
    public static final short ESTADO_LIBRO_RESTAURANDO = 8;

    //parámetro de valor por deterioro
    public static final short VALOR_LIBRO_DETERIORADO = 50;

    //tipos de elaboraciones
    public static final String ELABORACION_TIPO_ALIMENTO = "A";
    public static final String ELABORACION_TIPO_RECURSO = "R";
    public static final String ELABORACION_TIPO_ARTESANIA = "T";
    public static final String ELABORACION_TIPO_COSTURA = "C";

    //Recursos
    public static final Integer RECURSOS_ORO = 0;
    public static final Integer RECURSOS_AGUA = 1;
    public static final Integer RECURSOS_SAL = 5;
    public static final Integer RECURSOS_ARADO = 51;
    public static final Integer RECURSOS_AZADA = 50;
    public static final Integer RECURSOS_MADERA = 2;
    public static final Integer RECURSOS_PIEDRA = 3;
    public static final Integer RECURSOS_HIERRO = 4;
    public static final Integer RECURSOS_GUARDIA = 10;
    public static final Integer RECURSOS_ALDEANOS = 20;
    public static final Integer RECURSOS_DEUDAS = 1000;
    public static final Integer RECURSOS_PLUMAS = 703;
    public static final Integer RECURSOS_PERGAMINOS = 700;
    public static final Integer RECURSOS_PERGAMINO_SUCIO = 711;
    public static final Integer RECURSOS_TINTA = 702;
    public static final Integer RECURSOS_PIEL_DE_ANIMAL = 701;
    public static final Integer RECURSOS_PIEL_VACUNA = 704;
    public static final Integer RECURSOS_SUCIEDAD = 14;
    public static final Integer RECURSOS_RATAS = 15;

    //Animales
    public static final int ANIMALES_BUEY = 1;
    public static final int ANIMALES_VACA = 2;
    public static final int ANIMALES_GALLINA = 4;
    public static final int ANIMALES_CONEJO = 5;
    public static final int ANIMALES_GALLO = 6;
    public static final int ANIMALES_CERDO = 8;
    public static final int ANIMALES_CERDA = 9;
    public static final int ANIMALES_CONEJA = 10;
    public static final int ANIMALES_TORO = 11;
    public static final int ANIMALES_CARNERO = 12;
    public static final int ANIMALES_CABRA = 13;
    public static final int ANIMALES_OVEJA = 14;
    public static final int ANIMALES_OVEJA_MACHO = 15;
    public static final int ANIMALES_CABALLO = 16;
    public static final int ANIMALES_YEGUA = 17;

    //Mercancías tipo

    public static final int MERCANCIA_ALIMENTOS = 1;
    public static final int MERCANCIA_ANIMALES = 2;
    public static final int MERCANCIA_LIBROS = 3;
    public static final int MERCANCIA_RECURSOS = 4;
    public static final int MERCANCIA_RELIQUIAS = 5;

    //Mercancías tipo String

    public static final String MERCANCIA_ALIMENTOS_STR = "A";
    public static final String MERCANCIA_ANIMALES_STR = "N";
    public static final String MERCANCIA_LIBROS_STR = "L";
    public static final String MERCANCIA_RECURSOS_STR = "R";
    public static final String MERCANCIA_RELIQUIAS_STR = "O";

    public static final short IDIOMA_CASTELLANO = 1;
    public static final short IDIOMA_CATALAN = 2;
    public static final short IDIOMA_INGLES = 3;
    public static final short IDIOMA_FRANCES = 4;
    public static final short IDIOMA_ALEMAN = 5;
    public static final short IDIOMA_ITALIANO = 6;
    public static final short IDIOMA_PORTUGUES = 7;
    public static final short IDIOMA_GALLEGO = 8;
    public static final short IDIOMA_EUSKERA = 9;
    public static final short IDIOMA_LATIN = 10;
    public static final short IDIOMAS = 10;   // Total de idiomas
    public static final short IDIOMA_DEFECTO = 1;
    public static final short REGIONES = 57;   // Total de regiones en Abbatia

    //tipos de compra/venta
    public static final String TABLA_MERCANCIA = "MERCANCIA";
    public static final String TABLA_TIPO_VENTA = "TIPO_VENTA";
    public static final String TABLA_MERCADO = "MERCADO";
    public static final String TABLA_FILTRO = "FILTRO";

    //mercados
    public static final int MERCADO_TODOS = 0;
    public static final int MERCADO_REGIONAL = 1;
    public static final int MERCADO_REGIONES = 2;
    public static final int MERCADO_CIUDAD = 3;

    //filtros para compra
    public static final int FILTRO_TODOS = 0;
    public static final int FILTRO_PRECIO_MENOR = 1;
    public static final int FILTRO_PRECIO_MAYOR = 2;
    public static final int FILTRO_NOMBRE_ABADIA = 3;
    public static final int FILTRO_NOMBRE_REGION = 4;
    public static final int FILTRO_NOMBRE_PRODUCTO = 5;

    // Registros por pagina
    public static int REGISTROS_PAGINA = 25;

    // Comisiones
    public static String COMISION_TIPO_CANCELACION = "cancelacion";
    public static String COMISION_TIPO_VENTA5 = "venta5";
    public static String COMISION_TIPO_VENTA10 = "venta10";
    public static String COMISION_TIPO_VENTA15 = "venta15";

    //jerarquia eclesiastica
    public static int JERARQUIA_NOVICIO = 0;
    public static int JERARQUIA_MONJE = 1;
    public static int JERARQUIA_ABAD = 2;
    public static int JERARQUIA_OBISPO = 3;
    public static int JERARQUIA_CARDENAL = 5;
    public static int JERARQUIA_PAPA = 6;

    // Estado de los monjes
    public static int MONJE_VIVO = 0;
    public static int MONJE_MUERTO = 1;       // Velatorio, cuando pasa el proceso lo pasa al cementerio
    public static int MONJE_ENFERMO = 2;
    public static int MONJE_VIAJE = 3;
    public static int MONJE_VISITA = 4;
    public static int MONJE_OSARIO = 5;

    public static int MONJES_TODOS = 0;
    public static int MONJES_VIVOS = 1;
    public static int MONJES_MUERTOS = 2;  // Velatorio
    public static int MONJES_CEMENTERIO = 3;
    public static int MONJES_ENFERMOS = 4;  // Enfermos
    public static int MONJES_VIAJANDO = 5;
    public static int MONJES_VISITA = 6;
    public static int MONJES_VISITA_MIABADIA = 7;  // Monjes que están de visita en mi abbatia
    public static int MONJES_VISITA_ENFERMOS = 8;  // Monjes de visita que estan enfermos
    public static int MONJES_OSARIO = 9;


    //Definicion de familias
    public static int ALIMENTO_FAMILIA_PAN = 1;
    public static int ALIMENTO_FAMILIA_CEREALES = 2;
    public static int ALIMENTO_FAMILIA_PESCADO = 3;
    public static int ALIMENTO_FAMILIA_CARNE = 4;
    public static int ALIMENTO_FAMILIA_FRUTA = 5;
    public static int ALIMENTO_FAMILIA_LEGUMBRES = 6;
    public static int ALIMENTO_FAMILIA_VERDURA = 7;
    public static int ALIMENTO_FAMILIA_LECHE = 8;
    public static int ALIMENTO_FAMILIA_QUESO = 9;
    public static int ALIMENTO_FAMILIA_HUEVO = 10;

    //Funciones definidas para los cardenales
    public static int FUNCION_CARDENAL_MERCADO = 1;

    //tipos de empleado..
    public static int EMPLEADO_GUARDIA = 1;
    public static int EMPLEADO_MINERO = 2;
    public static int EMPLEADO_PICAPEDRERO = 3;

    //votaciones
    public static short VOTACIONES_OK = 1;
    public static short VOTACIONES_NOOK = 2;
    public static short VOTACIONES_PENDIENTE = 0;

    //tipos de viaje
    public static short VIAJE_TIPO_COPIA = 1;
    public static short VIAJE_TIPO_SIMPLE = 0;

    //estados de las solicitudes
    public static short SOLICITUD_ESTADO_PENDIENTE = 0;
    public static short SOLICITUD_ESTADO_APROBADA = 1;
    public static short SOLICITUD_ESTADO_CANCELADA = 2;

    //estados de los animales
    public static short ESTADO_ANIMAL_VIVO = 0;
    public static short ESTADO_ANIMAL_MUERTO = 1;
    public static short ESTADO_ANIMAL_VIAJANDO = 2;

    //tipos de solicitud
    public static short SOLICITUD_TIPO_VIAJE = 1;
    public static short SOLICITUD_TIPO_VIAJE_COPIA = 2;

    //objetos cargados en sesion
    public static String DATOS_SESSION_INFO_VIAJE_COPIA = "InfoViajeCopia";
    public static String DATOS_SESSION_LAST_URI_REQUESTED = "UltimaPeticion";
    public static String DATOS_SESSION_MILIS_REQUESTED = "MiliSegundos";
    public static String DATOS_SESSION_PARAMETROS = "Atributos";

    //keys de mensajes
    public static String MENSAJES_ADMINISTRACION_MERCADO = "ADMIN_MERCADO";

    //Flag para Idiomas asignables y no asignables
    public static short IDIOMA_TIPO_ASIGNABLE = 1;
    public static short IDIOMA_TIPO_NO_ASIGNABLE = 0;

    //tipos de libro
    public static short LIBRO_TIPO_BIBLIA = 1;
    public static short LIBRO_TIPO_CAPACITATE = 300;
    public static short LIBRO_TIPO_CONTRUCCION = 100;
    public static short LIBRO_TIPO_CONSERVATOR = 324;
    public static short LIBRO_TIPO_RENOVO = 323;
    public static short LIBRO_TIPO_CARTA_CARITATIS = 2;


    //Constantes para la gestion de enfermedades
    public static short ENFERMEDAD_DEFECTO = 1;
    public static short ENFERMEDAD_EXCESO = 2;
    public static int ENFERMEDAD_MINIMO_ACEPTABLE = 20;
    public static int ENFERMEDAD_MAXIMO_ACEPTABLE = 50;

    //varios
    public static int VARIOS_COSTE_ENVIO_MENSAJE = 10;
    public static int VARIOS_MAX_DESTINATARIOS = 50;
    public static int VARIOS_SIN_REGION = 10000;
    public static int VARIOS_REINTENTOS_LOGIN = 3;
    public static int VARIOS_MINIMO_ALIMENTACION = 20;
    public static int VARIOS_MAXIMO_ALIMENTACION = 50;
    public static String VARIOS_PWD_ADMIN = "02ubuntu";
    public static int VARIOS_DIAS_ELIMINACION = 5;

    //clima
    public static int CLIMA_SOLEADO = 0;
    public static int CLIMA_SOL_CON_ALGO_DE_NUBES = 1;
    public static int CLIMA_SOL_CON_NUBES = 2;
    public static int CLIMA_PARCIALMENTE_NUBOSO = 3;
    public static int CLIMA_NUBOSO = 4;
    public static int CLIMA_LLOVIZNA = 5;
    public static int CLIMA_LLUVIA_ESPORADICA = 6;
    public static int CLIMA_LLUVIA_MODERADA = 7;
    public static int CLIMA_LLUVIA = 8;
    public static int CLIMA_LLUVIA_FUERTE = 9;
    public static int CLIMA_TORMENTA = 10;
    public static int CLIMA_GRANIZO = 11;
    public static int CLIMA_SOL = 12;

    //objetos cargados en el InitialContext
    public static String EXCEPCIONES_CONTROLADAS = "tabla_excepciones";
    public static String LITERALES_CARGADOS = "tabla_literales";
    public static String TABLA_ABADIAS = "tabla_abadias";
    public static String TABLA_REGIONES = "tabla_regiones";
    public static String TABLA_LIBROS = "tabla_libros";
    public static String TABLA_IDIOMAS = "tabla_idiomas";
    public static String TABLA_CATEGORIAS = "tabla_categorias";
    //public static String ESTADO_PLANIFICADOR = "estado_planificador";
    public static String PATH_BASE = "base_path";

    //mensajes de bloqueo
    public static String BLOQUEO_REINTENTOS = "Máximo número de reintentos de acceso inconrrecto";
    public static String BLOQUEO_VOTO_POPULARIDAD_INCORRECTO = "Ha intentado votar la popularidad de un obispo de otra región";
    public static String BLOQUEO_ACCESO_ADMINISTRACION = "Ha intentado acceder a la consola de administración";
    public static String BLOQUEO_VENTAS_INEXISTENTES = "Ha tratado de vender varias veces el mismo producto";

    //Estados de petición de bloqueo
    public static short ESTADO_PETICION_BLOQUEO_ABIERTA = 1;
    public static short ESTADO_PETICION_BLOQUEO_CERRADA = 0;

    public static short VOTO_PETICION_BLOQUEO_SI = 1;
    public static short VOTO_PETICION_BLOQUEO_NO = 0;


    public static int MENSAJE_TIPO_WARNING = 1;
    public static int MENSAJE_TIPO_INFO = 1;
    //ficheros
    public static String FICHERO_DATOS_MAPA = "texto.txt";
    public static String FICHERO_DATOS_MAPA_IT = "datos_mapa_it.txt";

    //datos que se cargan en la request
    public static String REQUEST_PARAM_ERROR_STACKTRACE = "msg_stacktrace";
    public static String REQUEST_PARAM_ERROR_MESSAGE = "msg_exception";

    //mensajes para forzar el retorno de los monjes
    // Ha avandonado la abadía %s, el libro que estaba copiando ha desaparecido
    public static int FORZAR_RETORNO_LIBRO_DETERIORADO = 12113;
    // Regresa a casa, sus hermanos le necesitan
    public static int FORZAR_RETORNO_ABADIA_SIN_MONJES = 12114;
    //dejar la abadía, el usuario congela la cuenta
    public static int FORZAR_RETORNO_ABADIA_CONGELADA_PROPIA = 12115;
    public static int FORZAR_RETORNO_ABADIA_CONGELADA_AJENA = 12116;

    public static int NUMERO_REGIONES_SELECCIONABLES_ALTA = 2;

}
