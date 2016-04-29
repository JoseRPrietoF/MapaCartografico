/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabajo;

import insidefx.undecorator.UndecoratorScene;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import modelo.Centro;
import modelo.Cruz;

/**
 *
 * @author dibosjor
 */
public class FXMLDocumentController implements Initializable {

	private final boolean DEBUG = true;

    private ArrayList<Cruz> listaLineas = new ArrayList<Cruz>();
    @FXML
    private Pane pane;

    private final Rectangle2D rec = Screen.getPrimary().getBounds();
    private final double ALTURA = rec.getWidth();
    private final double LONGITUD = rec.getHeight();

    private int rango = 200;

    double orgSceneX;

    double orgSceneY;

    double orgTranslateX;
    double orgTranslateY;
    @FXML
    private Group angulos;
    @FXML
    private Circle circleSrc;

    @FXML
    private Pane zonaDibujo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        zonaDibujo.setOnMousePressed(e -> {
        	zonaDibujo.toFront();
            if (e.getButton() == MouseButton.PRIMARY) {
            	dibujarLinea(e.getX(), e.getY());
            } //else if (e.getButton() == MouseButton.SECONDARY) {
            	//borrarLinea(buscarCentro(e.getX(), e.getY()));

            //}
            else if(e.getButton() == MouseButton.MIDDLE){
            	int cruz = buscarCentro(e.getX(), e.getY());
            	if (cruz == -1) return;
                Cruz c = listaLineas.get(cruz);

                Centro centro = c.getCentro();

                if(DEBUG){
                	Alert alert = new Alert(AlertType.INFORMATION);
                	alert.setTitle("Situacion");
                	alert.setHeaderText("X: " + e.getX() + " Y: " + e.getY());
                	alert.setContentText(centro.toString());

                	alert.showAndWait();
                }
                System.out.println(cruz);
            }
        });

        zonaDibujo.setOnMouseDragged(e -> {
        	zonaDibujo.toFront();
            if (e.getButton() == MouseButton.SECONDARY) {
            	int cruz = buscarCentro(e.getX(), e.getY());
            	if (cruz == -1) return;
            	double newMouseX = e.getX();
                double newMouseY = e.getY();
                Cruz c = listaLineas.get(cruz);

                Line h = c.getLineaHorizontal();
                Line v = c.getLineaVertical();
                Line hc = c.getLineaHorizontalCentral();
                Line vc = c.getLineaVerticalCentral();

                double deltaX = newMouseX - v.getStartX();
                double deltaY = newMouseY - h.getStartY();

                double deltaXh = newMouseX - ((hc.getStartX() + hc.getEndX())/2);
                double deltaYh = newMouseY - ((hc.getStartY() + hc.getEndY())/2);

                double deltaXv = newMouseX - ((vc.getStartX() + vc.getEndX())/2);
                double deltaYv = newMouseY - ((vc.getStartY() + vc.getEndY())/2);
                // lineas grandes
                h.setTranslateY( deltaY);
                v.setTranslateX( deltaX);
                // cruz
                hc.setTranslateX(deltaXh);
                hc.setTranslateY(deltaYh);
                vc.setTranslateX(deltaXv);
                vc.setTranslateY(deltaYv );
                if(DEBUG){
                    System.out.println("-----Translated------");
                    System.out.println("hc.getTranslateX() " + hc.getTranslateX() + " hc.getTranslateY() " + hc.getTranslateY());
                    System.out.println("X: " + deltaXh + " Y: " + deltaYh);
                    System.out.println("Estoy en hc " + hc.getStartX() + "   " + hc.getStartY());
                    System.out.println("Estoy en vc " + vc.getStartX() + "   " + vc.getStartY());
                    System.out.println("-------------------------");
                    System.out.println(c);
                }
            }
        });
        if(DEBUG)
	        zonaDibujo.setOnMouseMoved(e -> {
	        	System.out.println("X: " + e.getX() + " Y: " + e.getY());

	        });

    }

    public void setUndecorator(UndecoratorScene undecorator) {

    }

    private void dibujarLinea(double x, double y) {
        Cruz cruz = new Cruz(x, y, ALTURA, LONGITUD);
        ObservableList<Node> hijos = pane.getChildren();

        Line l = cruz.getLineaHorizontal();
        hijos.add(l);

        l = cruz.getLineaVertical();
        hijos.add(l);

        l = cruz.getLineaHorizontalCentral();
        hijos.add(l);

        l = cruz.getLineaVerticalCentral();
        hijos.add(l);

        listaLineas.add(cruz);
    }

    private void borrarLinea(int indice) {
        if (indice == -1) {
            return;
        }
        ObservableList<Node> hijos = pane.getChildren();

        hijos.remove(listaLineas.get(indice).getLineaHorizontal());
        hijos.remove(listaLineas.get(indice).getLineaVertical());
        hijos.remove(listaLineas.get(indice).getLineaHorizontalCentral());
        hijos.remove(listaLineas.get(indice).getLineaVerticalCentral());


        listaLineas.remove(indice);
    }

    @FXML
    private void eventoClick(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            dibujarLinea(event.getX(), event.getY());
        } else if (event.getButton() == MouseButton.MIDDLE) {

        } else {
            borrarLinea(buscarCentro(event.getX(), event.getY()));

        }

    }

    private int buscarCentro(double x, double y) {
        Cruz c;
        double centroX, centroY;
        for (int i = 0; i < listaLineas.size(); i++) {
            c = listaLineas.get(i);
            centroX = c.getLineaVerticalCentral().getTranslateX();
            System.out.println("busco x en " + centroX + " x vale: " + x);
            if (Math.abs(centroX - x) <= rango) {
                centroY = c.getLineaVerticalCentral().getTranslateY();
                System.out.println("busco y en " + centroY + " y vale: " + y);
                if (Math.abs(centroY - y) <= rango) {
                    return i;
                }
            }
        }

        return -1;
    }

    @FXML
    private void teclaPulsada(KeyEvent event) {

    }



    public boolean clicDerecho(MouseEvent mouse) {
        if (mouse.getButton() == MouseButton.SECONDARY) {
            return true;
        }
        return false;
    }

    public boolean clicIzquierdo(MouseEvent mouse) {
        if (mouse.getButton() == MouseButton.PRIMARY) {
            return true;
        }
        return false;
    }

    public boolean clicMedio(MouseEvent mouse) {
        if (mouse.getButton() == MouseButton.MIDDLE) {
            return true;
        }
        return false;
    }

    EventHandler<MouseEvent> escogerNodo
            = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (clicMedio(t)) {

                // angulos.toFront();
                moverAngulos(t);
                //y mover
            } else {
                zonaDibujo.toFront();
                if (clicIzquierdo(t)) {
                    dibujarLinea(t.getX(), t.getY());
                } else {
                    borrarLinea(buscarCentro(t.getX(), t.getY()));
                }
            }

        }
    };

    private void moverAngulos(MouseEvent t) {
        if (!clicMedio(t)) {
            return;
        }

        orgSceneX = t.getSceneX();
        orgSceneY = t.getSceneY();
        orgTranslateX = angulos.getTranslateX();
        orgTranslateY = angulos.getTranslateY();
    }

}
