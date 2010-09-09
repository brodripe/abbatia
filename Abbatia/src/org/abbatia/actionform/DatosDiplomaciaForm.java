package org.abbatia.actionform;

import org.apache.struts.action.ActionForm;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 12-oct-2005
 * Time: 21:17:30
 * To change this template use File | Settings | File Templates.
 */
public class DatosDiplomaciaForm extends ActionForm {
//    <form-bean name="DiplomaciaForm" type="org.apache.struts.action.DynaActionForm">
//      <form-property name="flagObispo" type="java.lang.Boolean" initial="false"/>
//      <form-property name="flagCardenal" type="java.lang.Boolean" initial="false"/>
//    </form-bean>

    private boolean flagObispo;
    private boolean flagCardenal;

    public boolean isFlagObispo() {
        return flagObispo;
    }

    public void setFlagObispo(boolean flagObispo) {
        this.flagObispo = flagObispo;
    }

    public boolean isFlagCardenal() {
        return flagCardenal;
    }

    public void setFlagCardenal(boolean flagCardenal) {
        this.flagCardenal = flagCardenal;
    }
}
