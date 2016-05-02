/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author dibosjor
 */
@XmlRootElement
public class Cruz {

    private Line lineaVertical;
    private Line lineaHorizontal;
    private Line lineaVerticalCentral;
    private Line lineaHorizontalCentral;

    private Centro centro;

    public Cruz() {
    }

    public Cruz(double x, double y, double screenWidth, double screenHeight) {
        centro = new Centro(x, y);

        lineaVertical = construirVertical(x, screenHeight);
        lineaHorizontalCentral = construirVerticalCentral(x, y);
        lineaVerticalCentral = construirHorizontalCentral(x, y);
        lineaHorizontal = contruirHorizontal(screenWidth, y);
    }

    public Cruz(Line lineaVertical, Line lineaHorizontal, Line lineaVerticalCentral, Line lineaHorizontalCentral) {
        this.lineaVertical = lineaVertical;
        this.lineaHorizontal = lineaHorizontal;
        this.lineaVerticalCentral = lineaVerticalCentral;
        this.lineaHorizontalCentral = lineaHorizontalCentral;
    }

    public Cruz(double x, double y, double screenWidth, double screenHeight, int angulo) {
        centro = new Centro(x, y);
        if (angulo == 0) {
            lineaVertical = construirVertical(x, screenHeight);
            lineaHorizontalCentral = construirVerticalCentral(x, y);
            lineaVerticalCentral = construirHorizontalCentral(x, y);
            lineaHorizontal = contruirHorizontal(screenWidth, y);
        } else {
            System.out.println("ANGULADO");
            double Cy = Math.sin(angulo);
            double Cyy = Math.sin(angulo + 90);
            double Cx = Math.cos(angulo);
            double Cxx = Math.cos(angulo + 90);
            lineaVertical = construirDiagonal(x-100*Cx,y-100*Cy,x+100*Cx,y+100*Cy);
            lineaHorizontalCentral = construirVerticalCentral(x, y);
            lineaVerticalCentral = construirHorizontalCentral(x, y);
            lineaHorizontal = construirDiagonal(x,y,x+100*Cxx,y+100*Cyy);
            
        }

    }

    
    public Line construirVertical(double x, double altura) {

        Line lineaV = new Line(x, 0, x, altura);
        lineaV.setStroke(Color.RED);
        lineaV.setStrokeWidth(0.5);
        return lineaV;
    }

    public Line construirVerticalCentral(double x, double y) {
        Line lineaVC = new Line(x, y - 5, x, y + 5);
        lineaVC.setStroke(Color.RED);
        lineaVC.setStrokeWidth(0.8);
        return lineaVC;
    }

    public Line construirHorizontalCentral(double x, double y) {
        Line lineaHC = new Line(x - 5, y, x + 5, y);
        lineaHC.setStroke(Color.RED);
        lineaHC.setStrokeWidth(0.8);
        return lineaHC;
    }

   

    private Line contruirHorizontal(double longitud, double y) {
        Line lineaH;

        lineaH = new Line(0, y, longitud, y);
        lineaH.setStroke(Color.RED);
        lineaH.setStrokeWidth(0.5);

        return lineaH;
    }
   
    private Line construirDiagonal(double x, double y, double xx, double yy) {
        Line linea = new Line(0,0,0,0);
        linea = new Line(x, y, xx, yy);
        linea.setStroke(Color.RED);
        linea.setStrokeWidth(0.5);
        return linea;
    }

    public void dibujarHorizontal(AnchorPane anchor) {
        anchor.getChildren().add(lineaHorizontal);
    }

    public Line getLineaVertical() {
        return lineaVertical;
    }

    public void setLineaVertical(Line lineaVertical) {
        this.lineaVertical = lineaVertical;
    }

    public Line getLineaHorizontal() {
        return lineaHorizontal;
    }

    public void setLineaHorizontal(Line lineaHorizontal) {
        this.lineaHorizontal = lineaHorizontal;
    }

    public Line getLineaVerticalCentral() {
        return lineaVerticalCentral;
    }

    public void setLineaVerticalCentral(Line lineaVerticalCentral) {
        this.lineaVerticalCentral = lineaVerticalCentral;
    }

    public Line getLineaHorizontalCentral() {
        return lineaHorizontalCentral;
    }

    public void setLineaHorizontalCentral(Line lineaHorizontalCentral) {
        this.lineaHorizontalCentral = lineaHorizontalCentral;
    }

    public Centro getCentro() {
        return centro;
    }

    public void setCentro(Centro centro) {
        this.centro = centro;
    }
}
