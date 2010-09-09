package org.abbatia.bean;

public class Publicidad {
    private int id;
    private String imagen;
    private String url;
    private String hint;


    public int getId() {
        return id;
    }

    public String getImagen() {
        return imagen;
    }

    public String getUrl() {
        return url;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHint() {
        return hint;
    }
}
