/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;


import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author dibosjor
 */
public class Cruz {

    private Line lineaVertical;
	private Line lineaHorizontal;
    private Line lineaVerticalCentral;
    private Line lineaHorizontalCentral;

    private Centro centro;

    public Cruz(){}

    public Cruz(double x, double y, double screenWidth, double screenHeight) {
        centro = new Centro(x,y);


        lineaVertical = construirVertical(x,screenHeight);
        lineaHorizontalCentral = construirVerticalCentral(x,y);
        lineaVerticalCentral = construirHorizontalCentral(x,y);
        lineaHorizontal = contruirHorizontal(screenWidth,y);
    }

    public Line construirVertical(double x, double altura) {

        Line lineaV = new Line(0,0,0,altura);
        lineaV.setStroke(Color.RED);
        lineaV.setStrokeWidth(0.5);

        lineaV.setTranslateX(x);
        return lineaV;
    }

    public Line construirVerticalCentral(double x,double y) {
        Line lineaVC = new Line (0,y-5,0,y+5);
        lineaVC.setStroke(Color.RED);
        lineaVC.setStrokeWidth(0.8);
        lineaVC.setTranslateX(x);
        return lineaVC;
    }
    public Line construirHorizontalCentral(double x,double y) {
        Line lineaHC = new Line (x-5,0,x+5,0);
        lineaHC.setStroke(Color.RED);
        lineaHC.setStrokeWidth(0.8);
        lineaHC.setTranslateX(x);
        return lineaHC;
    }

    private Line contruirHorizontal(double longitud, double y) {
        Line lineaH;

        lineaH = new Line(0,0,longitud,0);
        lineaH.setStroke(Color.RED);
        lineaH.setStrokeWidth(0.5);
        lineaH.setTranslateY(y);
        return lineaH;
    }

    public void dibujarVertical(AnchorPane anchor) {
        anchor.getChildren().add(lineaVertical);
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

	@Override
	public String toString() {
		return "Cruz [lineaVertical=" + lineaVertical.getTranslateX() + ", lineaHorizontal=" + lineaHorizontal.getTranslateY()
				+ ", lineaVerticalCentral=" + lineaVerticalCentral.getTranslateX() + ", lineaHorizontalCentral="
				+ lineaHorizontalCentral.getTranslateY() + "]";
	}



}


