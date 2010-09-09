package org.abbatia.dbms.model.vo;

import java.util.ArrayList;

/**
 * <p> Título: LoadVO</p>
 * <p> Descripción: </p>
 * <p> Copyright: Copyright (c) 2007 </p>
 * <p> Compañía: Olympus </p>
 *
 * @author Talarn Consulting
  */
public class LoadVO {
  private String                  m_szTipoCarga;
  private String                  m_szTabla;
  private int                     m_iTramo;
  private ArrayList<LoadFieldVO>  m_alModeloSet;
  private ArrayList<LoadFieldVO>  m_alModeloWhere;
  private ArrayList<ArrayList>    m_alDatosSet;
  private ArrayList<ArrayList>    m_alDatosWhere;


  public LoadVO() {
    m_iTramo = 0;
    
  }

  public String getTipoCarga() {
    return m_szTipoCarga;
  }


  public void setTipoCarga(String p_szTipoCarga) {
    this.m_szTipoCarga = p_szTipoCarga;
  }


  public String getTabla() {
    return m_szTabla;
  }


  public void setTabla(String p_szTabla) {
    this.m_szTabla = p_szTabla;
  }


  public int getTramo() {
    return m_iTramo;

  }


  public void setTramo(int p_iTramo) {
    this.m_iTramo = p_iTramo;
  }


  public ArrayList<LoadFieldVO> getModeloSet() {
    return m_alModeloSet;
  }


  public void setModeloSet(ArrayList<LoadFieldVO> p_alModeloSet) {
    this.m_alModeloSet = p_alModeloSet;
  }


  public ArrayList<LoadFieldVO> getModeloWhere() {
    return m_alModeloWhere;
  }


  public void setModeloWhere(ArrayList<LoadFieldVO> p_alModeloWhere) {
    this.m_alModeloWhere = p_alModeloWhere;
  }


  public ArrayList getDatosSet() {
    return m_alDatosSet;
  }


  public void setDatosSet(ArrayList<ArrayList> p_alDatosSet) {
    this.m_alDatosSet = p_alDatosSet;
  }


  public ArrayList<ArrayList> getDatosWhere() {
    return m_alDatosWhere;
  }


  public void setDatosWhere(ArrayList<ArrayList> p_alDatosWhere) {
    this.m_alDatosWhere = p_alDatosWhere;
  }


  // METODOS PUBLICOS
  public void anyadirCampoModeloSet(LoadFieldVO p_oLoadFieldVO) {
    if (m_alModeloSet == null) {
      m_alModeloSet = new ArrayList<LoadFieldVO>();
    }

    m_alModeloSet.add(p_oLoadFieldVO);

  } // END OF public void anyadirCampoModeloSet(LoadFieldVO)


  public void anyadirCampoModeloWhere(LoadFieldVO p_oLoadFieldVO) {
    if (m_alModeloWhere == null) {
      m_alModeloWhere = new ArrayList<LoadFieldVO>();
    }

    m_alModeloWhere.add(p_oLoadFieldVO);

  } // END OF public void anyadirCampoModeloWhere(LoadFieldVO)


  public void anyadirALCamposDatosSet(ArrayList p_alLoaderFieldVO) {
    if (m_alDatosSet == null) {
      m_alDatosSet = new ArrayList<ArrayList>();
    }

    m_alDatosSet.add(p_alLoaderFieldVO);

  } // END OF public void anyadirALCamposDatosSet(LoadFieldVO)


  public void anyadirALCamposDatosWhere(ArrayList p_alLoaderFieldVO) {
    if (m_alDatosWhere == null) {
      m_alDatosWhere = new ArrayList<ArrayList>();
    }

    m_alDatosWhere.add(p_alLoaderFieldVO);

  } // END OF public void anyadirALCamposDatosWhere(LoadFieldVO)


  public void vaciarDatos() {
    if (m_alDatosSet != null) {
      m_alDatosSet.clear();

    } // END OF if


    if (m_alDatosWhere != null) {
      m_alDatosWhere.clear();

    } // END OF if

  } // END OF public void vaciarDatos()
  
} // END OF public class LoadVO
