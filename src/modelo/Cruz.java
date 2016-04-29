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


        construirVertical(x,screenHeight);
        construirVerticalCentral(x,y);
        construirHorizontalCentral(x,y);
        contruirHorizontal(screenWidth,y);
    }

    public void construirVertical(double x, double altura) {
        lineaVertical = new Line(0,0,0,altura);
        lineaVertical.setStroke(Color.RED);
        lineaVertical.setStrokeWidth(0.5);
        lineaVertical.setTranslateX(x);
    }

    public void construirVerticalCentral(double x,double y) {
        lineaVerticalCentral = new Line (0,y-5,0,y+5);
        lineaVerticalCentral.setStroke(Color.DARKBLUE);
        lineaVerticalCentral.setStrokeWidth(0.8);
        lineaVerticalCentral.setTranslateX(x);
    }
    public void construirHorizontalCentral(double x,double y) {
        lineaHorizontalCentral = new Line (x-5,0,x+5,0);
        lineaHorizontalCentral.setStroke(Color.DARKBLUE);
        lineaHorizontalCentral.setStrokeWidth(0.8);
        lineaHorizontalCentral.setTranslateY(y);
    }

    private void contruirHorizontal(double longitud, double y) {
        lineaHorizontal = new Line(0,0,longitud,0);
        lineaHorizontal.setStroke(Color.RED);
        lineaHorizontal.setStrokeWidth(0.5);
        lineaHorizontal.setTranslateY(y);
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


