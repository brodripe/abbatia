package org.abbatia.dbms.model.vo;

import java.io.Serializable;

/**
 * <p> Título: LoadFieldVO </p>
 * <p> Descripción: </p>
 * <p> Copyright: Copyright (c) 2007 </p>
 * <p> Compañía: Olympus </p>
 *
 * @author Talarn Consulting
  */
public class FieldVO
  implements Serializable {
  private String  m_szNombre;
  private Object  m_oValor;
  private int     m_iTipo;

  /**
   * Crea un nuevo objeto LoadFieldVO.
   */
  public FieldVO() {}


  /**
   * Crea un nuevo objeto LoadFieldVO.
   *
   * @param p_szNombre
   * @param p_oValor
   * @param p_iTipo
   */
  public FieldVO(String p_szNombre,
                  Object p_oValor,
                  int p_iTipo) {
    this.m_szNombre = p_szNombre;
    this.m_oValor   = p_oValor;
    this.m_iTipo    = p_iTipo;

  }


  public String getNombre() {
    return m_szNombre;
  }


  public void setNombre(String p_szNombre) {
    this.m_szNombre = p_szNombre;
  }


  public Object getValor() {
    return m_oValor;
  }


  public void setValor(Object p_oValor) {
    this.m_oValor = p_oValor;
  }


  public int getTipo() {
    return m_iTipo;
  }


  public void setTipo(int p_iTipo) {
    this.m_iTipo = p_iTipo;
  }

}
