/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author dibosjor
 */

@XmlRootElement
public class Dibujo {
    private ArrayList<Centro> listaCentros = new ArrayList<Centro>();
    
    public Dibujo() {
        
    }
    
    public void setListaLineas(ArrayList<Centro> lista) {
        listaCentros = lista;
    }
    
    public void addCentro(Centro centro) {
        listaCentros.add(centro);
    }
    
    public void removeCentro(int pos) {
        listaCentros.remove(pos);
    }
    
    public ArrayList<Centro> getListaLineas() {
        return listaCentros;
    }
    
    public void imprimirDibujo()  {
        for(int i = 0; i<listaCentros.size(); i++) {
            System.out.println("" + listaCentros.get(i).getX() + "   " + listaCentros.get(i).getY());
        }
    }
    
}
