/*
 * ARCHIVO JS QUE AÑADE FUNCIONALIDAD A LAS TABLAS GENERADAS POR DISPLAYTAG.
 */

/**
 * Añade "handlers" para las filas de la tabla.
 * Permite ejecutar una URL al hacer click sobre la fila.
 *
 * tableId:       Identificador de la tabla a la que se añadirán los handlers.
 * rowClassName:  Clase que se aplicará a la fila en el evento onmouseover.
 * url:           URL que se ejecutarán en el evento onclick.
 * paramName:     Nombre del parámetro que se añadirá a URL
 * columnIndex:   (opcional) Indice de la columna de la cual se recuperará el valor para el parámetro.
 *
 */
function addRowHandlersURL(tableId, url, paramName, columnIndex, rowClassName) {
    var previousClass = null;
    var table = document.getElementById(tableId);
    var rows = table.getElementsByTagName("tr");

    for (var i = 1; i < rows.length; i++) {
        if (rowClassName != undefined) {
            rows[i].onmouseover = function () {
                previousClass = this.className;
                this.className = this.className + " " + rowClassName;
            };

            rows[i].onmouseout = function () {
                this.className = previousClass;
            };

        }

        rows[i].onclick = function () {
            var cell = this.getElementsByTagName("td")[columnIndex];
            var paramValue = cell.innerHTML;
            location.href = url + "?" + paramName + "=" + paramValue;
        };

    } // END OF for

} // END OF function addRowHandlersURL(tableId, url, paramName, columnIndex, rowClassName)


/**
 * Añade "handlers" para las filas de la tabla.
 * Permite ejecutar una función javascript al hacer click sobre la fila.
 *
 * tableId:       Identificador de la tabla a la que se añadirán los handlers.
 * funcion:       Función javascript que se ejecutará en el evento onclick.
 * paramName:     Nombre del parámetro que se añadirá a URL
 * columnIndex:   Indice de la columna de la cual se recuperará el valor para el parámetro.
 * rowClassName:  (opcional) Clase que se aplicará a la fila en el evento onmouseover.
 *
 */
function addRowHandlersJS(tableId, funcion, columnIndex, rowClassName) {
    var previousClass = null;
    var table = document.getElementById(tableId);
    var rows = table.getElementsByTagName("tr");

    for (var i = 1; i < rows.length; i++) {
        if (rowClassName != undefined) {
            rows[i].onmouseover = function () {
                previousClass = this.className;
                this.className = this.className + " " + rowClassName;

            };

            rows[i].onmouseout = function () {
                this.className = previousClass;

            };

        } // END OF if

        rows[i].onclick = function () {
            var cell = this.getElementsByTagName("td")[columnIndex];
            var paramValue = cell.innerHTML;
            location.href = "javascript:" + funcion + "('" + paramValue + "')";

            return false;

        };

    } // END OF for

} // END OF function addRowHandlersJS(tableId, funcion, columnIndex, rowClassName)


/**
 * Añade "handlers" de highlighting para las filas de la tabla.
 *
 * tableId:       Identificador de la tabla a la que se añadirán los handlers.
 * rowClassName:  Clase que se aplicará a la fila en el evento onmouseover.
 *
 */
function addRowHandlersHL(tableId, rowClassName) {
    var previousClass = null;
    var table = document.getElementById(tableId);
    var rows = table.getElementsByTagName("tr");

    for (var i = 1; i < rows.length; i++) {
        rows[i].onmouseover = function () {
            previousClass = this.className;
            this.className = this.className + " " + rowClassName;

        };

        rows[i].onmouseout = function () {
            this.className = previousClass;

        };

    } // END OF for

} // END OF function addRowHandlersHL(tableId, rowClassName)


/**
 * Añade "handlers" para las columnas de la tabla.
 * Permite ejecutar una función javascript al hacer click sobre las columnas de una tabla.
 *
 * tableId:       Identificador de la tabla a la que se añadirán los handlers.
 * funcion:       Función javascript que se ejecutará en el evento onclick.
 * columnIndex:   Indice de la columna de la cual se recuperará el valor para el parámetro.
 * rowClassName:  (opcional) Clase que se aplicará a la fila en el evento onmouseover.
 * avoidColumnIndex:  (opcional) Columna a la que no se asignará el evento onclick.
 * withTotals:    (opcional) Indica si existe una última fila de la tabla con totales
 *
 */
function addColumnsHandlersJS(tableId, funcion, columnIndex, rowClassName, avoidColumnIndex, withTotals) {
    var previousClass = null;
    var table = document.getElementById(tableId);
    var rows = table.getElementsByTagName("tr");
    var avoidColumnIndexLocal = -1;
    var limiteSuperior = rows.length;

    // Tratamiento de parámetros opcionales
    if (avoidColumnIndex != undefined) {
        avoidColumnIndexLocal = avoidColumnIndex;

    } // END OF if

    if (withTotals != undefined) {
        limiteSuperior -= 1;

    } // END OF if

    // Se evita la primera fila (i = 1)
    for (var i = 1; i < limiteSuperior; i++) {
        if (rowClassName != undefined) {
            rows[i].onmouseover = function () {
                previousClass = this.className;
                this.className = this.className + " " + rowClassName;

            };

            rows[i].onmouseout = function () {
                this.className = previousClass;

            };

        } // END OF if

        // Recuperamos las celdas de la fila y colocamos el evento onclick
        var cells = rows[i].getElementsByTagName("td");
        for (var j = 0; j < cells.length; j++) {
            if (avoidColumnIndexLocal != j) {
                eval("cells[j].onclick = function () {location.href = \"javascript:" + funcion + "('" + cells[columnIndex].innerHTML + "')\";return false;}");
            }
        }

    } // END OF for

} // END OF function addColumnsHandlersJS(tableId, funcion, columnIndex, rowClassName, avoidColumnIndex)


/**
 * Añade "handlers" para la columna de una tabla.
 * Permite ejecutar una función javascript al hacer click sobre una columna en concreto.
 *
 * tableId:       Identificador de la tabla a la que se añadirán los handlers.
 * funcion:       Función javascript que se ejecutará en el evento onclick.
 * paramName:     Nombre del parámetro que se añadirá a URL
 * columnIndex:   Indice de la columna donde se añadira el evento onclick.
 * columnIndex:   Indice de la columna de la cual se recuperará el valor para el parámetro.
 * rowClassName:  (opcional) Clase que se aplicará a la fila en el evento onmouseover.
 *
 */
function addColumnHandlersJS(tableId, funcion, columnIndex, columnIndexParam, rowClassName) {
    var previousClass = null;
    var table = document.getElementById(tableId);
    var rows = table.getElementsByTagName("tr");

    // Se evita la primera fila (i = 1)
    for (var i = 1; i < rows.length; i++) {
        if (rowClassName != undefined) {
            eval("rows[i].onmouseover = function () { previousClass = this.className; this.className = this.className + \" \" + rowClassName;}");
            eval("rows[i].onmouseout = function () {this.className = previousClass; }");

        } // END OF if

        // Recuperamos las celdas de la fila y colocamos el evento onclick
        var cells = rows[i].getElementsByTagName("td");

        eval("cells[columnIndex].onclick=function(){location.href=\"javascript:" + funcion + "('" + cells[columnIndexParam].innerHTML + "')\";return false;}");
        eval("cells[columnIndex].onmouseover = function () {previousClass = this.className; this.className = this.className + \" cellMouseOverPointer\";}");


    } // END OF for

}

