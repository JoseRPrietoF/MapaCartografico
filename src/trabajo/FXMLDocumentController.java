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

	private final boolean DEBUG = false;

    private ArrayList<Cruz> listaLineas = new ArrayList<Cruz>();
    @FXML
    private Pane pane;

    private final Rectangle2D rec = Screen.getPrimary().getBounds();
    private final double ALTURA = rec.getWidth();
    private final double LONGITUD = rec.getHeight();

    private int rango = 20;

    double orgSceneX;

    double orgSceneY;

    double orgTranslateX;
    double orgTranslateY;
    @FXML
    private Group angulos;
    @FXML
    private Circle circleSrc;


    //private UndecoratorScene undecorator;
    @FXML
    private Pane zonaDibujo;

    //Stage stage = (Stage) pane.getScene().getWindow();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //zonaDibujo.setOnMousePressed(escogerNodo);


        //zonaDibujo.setOnMouseDragged(OnMouseDraggedEventHandler);
        // zonaDibujo.setOnMousePressed(eventoDibujar);

        zonaDibujo.setOnMousePressed(e -> {
        	zonaDibujo.toFront();
            if (e.getButton() == MouseButton.PRIMARY) {
            	dibujarLinea(e.getX(), e.getY());
            } //else if (e.getButton() == MouseButton.SECONDARY) {
            	//borrarLinea(buscarCentro(e.getX(), e.getY()));

            //}
        });

        if(DEBUG)
	        zonaDibujo.setOnMouseMoved(e -> {
	        	zonaDibujo.toFront();
	        	double newMouseX = e.getX();
                double newMouseY = e.getY();
                System.out.println("x : " + newMouseX);
                System.out.println("y : " + newMouseY);
	        });

        zonaDibujo.setOnMouseDragged(e -> {
        	zonaDibujo.toFront();
            if (e.getButton() == MouseButton.SECONDARY) {
            	int cruz = buscarCentro(e.getX(), e.getY());
            	System.out.println(cruz);
            	if (cruz == -1) return;
            	double newMouseX = e.getX();
                double newMouseY = e.getY();
                Cruz c = listaLineas.get(cruz);

                Line h = c.getLineaHorizontal();
                Line v = c.getLineaVertical();
                Line hc = c.getLineaHorizontalCentral();
                Line vc = c.getLineaVerticalCentral();


                double deltaX = newMouseX - hc.getStartX();
                double deltaY = newMouseY - hc.getStartY(); // esta bien

                Centro centro = c.getCentro();

                // lineas grandes
                h.setTranslateY(deltaY);
                v.setTranslateX(deltaX);

                // cruz
                hc.setTranslateX(deltaX);
                hc.setTranslateY(deltaY);
                vc.setTranslateX(deltaX);
                vc.setTranslateY(deltaY);

                c.getCentro().setX(centro.getX() + deltaX);
                c.getCentro().setY(centro.getY() + deltaY);
                //System.out.println(c.getCentro());
            }
        });

    }

    public void setUndecorator(UndecoratorScene undecorator) {

    }

    private void dibujarLinea(double x, double y) {
        //Scene scene = pane.getScene();
        Cruz cruz = new Cruz(x, y, ALTURA, LONGITUD);
        ObservableList<Node> hijos = pane.getChildren();

        hijos.add(cruz.getLineaHorizontal());
        hijos.add(cruz.getLineaVertical());
        hijos.add(cruz.getLineaHorizontalCentral());
        hijos.add(cruz.getLineaVerticalCentral());
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
        Centro centro;
        double centroX, centroY;
        for (int i = 0; i < listaLineas.size(); i++) {
            centro = listaLineas.get(i).getCentro();
            centroX = centro.getX();

            if (Math.abs(centroX - x) <= rango) {
                centroY = centro.getY();
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
