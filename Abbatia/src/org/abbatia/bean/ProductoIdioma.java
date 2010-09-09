package org.abbatia.bean;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 30-nov-2008
 * Time: 23:06:14
 */
public class ProductoIdioma {
    private int abadiaId;
    private int productoId;
    private int idiomaId;

    public ProductoIdioma(int abadiaId, int productoId, int idiomaId) {
        this.abadiaId = abadiaId;
        this.productoId = productoId;
        this.idiomaId = idiomaId;
    }

    public int getAbadiaId() {
        return abadiaId;
    }

    public void setAbadiaId(int abadiaId) {
        this.abadiaId = abadiaId;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public int getIdiomaId() {
        return idiomaId;
    }

    public void setIdiomaId(int idiomaId) {
        this.idiomaId = idiomaId;
    }
}
