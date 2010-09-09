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
public class LoadFieldVO
  extends FieldVO
  implements Serializable {
  private int     m_iIndice;


  public LoadFieldVO(LoadFieldVO p_oLoadFieldVO, Object p_oValor) {
    super(p_oLoadFieldVO.getNombre(), p_oValor, p_oLoadFieldVO.getTipo());

  }

  /**
   * Crea un nuevo objeto LoadFieldVO.
   */
  public LoadFieldVO() {}


  public int getIndice() {
    return m_iIndice;
  }


  public void setIndice(int p_iIndice) {
    this.m_iIndice = p_iIndice;
  }
}
