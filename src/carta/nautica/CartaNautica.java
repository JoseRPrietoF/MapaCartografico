/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carta.nautica;

import insidefx.undecorator.Undecorator;
import insidefx.undecorator.UndecoratorScene;
import java.awt.Robot;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import modelo.Centro;
import modelo.Cruz;
import modelo.Dibujo;

/**
 *
 * @author dibosjor
 */
public class CartaNautica extends Application {

    Stage stage;
    Robot robot = null;

    private final boolean DEBUG = false;
    private int rango = 200;

    private ArrayList<Cruz> listaLineas = new ArrayList<Cruz>();
    
    private ArrayList<Centro> listaCentros = new ArrayList<Centro>();
    private Dibujo dibujo = new Dibujo();
    @FXML
    private Pane pane;

    private final Rectangle2D rec = Screen.getPrimary().getBounds();
    private final double ALTURA = rec.getWidth();
    private final double LONGITUD = rec.getHeight();
    private Button boton;

    double orgSceneX;

    double orgSceneY;

    double orgTranslateX;
    double orgTranslateY;
    @FXML
    private Group angulos;
    @FXML
    private Circle circuloMover;

    private Undecorator undecorator;

    @FXML
    private Pane zonaDibujo;

    boolean canBeDragged = false;
    boolean dragging = false;
    int cruzSeleccionada = 0;
    boolean draw = true;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        primaryStage.setTitle("CARTA");

        // UI
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/carta/nautica/FXMLDocument.fxml"));
        fxmlLoader.setController(this);
        Region root = (Region) fxmlLoader.load();

        // undecorator como escena
        final UndecoratorScene undecoratorScene = new UndecoratorScene(primaryStage, root);
        // css
        undecoratorScene.addStylesheet("/carta/nautica/demoapp.css");
        // fade al abrir
        undecoratorScene.setFadeInTransition();

        /*
         * Fade out al cerrar
         */
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {
                we.consume();
                undecoratorScene.setFadeOutTransition();
            }
        });
        String deco = "-fx-background-color: white;\n"
                + "-fx-opacity: 0.45;";

        Pane canvas = new Pane();
        canvas.setStyle(deco);//\n-fx-opacity: 0.2;");
        canvas.setPrefSize(900, 900);
        pane.getChildren().add(canvas);
        angulos.toFront();
        zonaDibujo.toFront();
        primaryStage.setScene(undecoratorScene);
        primaryStage.sizeToScene();
        primaryStage.toFront();
        primaryStage.setAlwaysOnTop(true);

        undecorator = undecoratorScene.getUndecorator();
        primaryStage.setMinWidth(undecorator.getMinWidth());
        primaryStage.setMinHeight(undecorator.getMinHeight());

        Image image = new Image("/skin/timon.png");
        primaryStage.getIcons().addAll(image);

        initCarta();

        primaryStage.show();
    }

    void initCarta() {

        //   zonaDibujo.setOnMousePressed(escogerNodo);
        //    zonaDibujo.setOnMouseDragged(OnMouseDraggedEventHandler);
        undecorator.removeMenuItems();
        MenuItem itemGuardar = new MenuItem("Guardar Mapa");
        itemGuardar.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                guardarXML();
            }
        });
        MenuItem itemAbrir = new MenuItem("Abrir Mapa");
        itemAbrir.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                abrirXML();
            }
        });
        MenuItem itemDibujar = new MenuItem("Dibujar 45");
        itemDibujar.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Bounds limites = angulos.getBoundsInParent();

                double centroX = limites.getMinX() + 80;
                double centroY = limites.getMinY() + 80;
                dibujarLinea(centroX, centroY, 90);
                
            }
        });
        undecorator.setMenuItem(itemDibujar);
        
        undecorator.setMenuItem(itemGuardar);
        undecorator.setMenuItem(itemAbrir);

        zonaDibujo.setOnMousePressed(e -> {
            if (clicMedio(e)) {

                if (dentroAngulos(e)) {

                    moverAngulos(e);
                } else {

                    canBeDragged = false;
                }

                //y mover
            } else {
                zonaDibujo.toFront();
                if (clicIzquierdo(e)) {
                    draw = true;
                } else {
                    borrarLinea(buscarCentro(e.getX(), e.getY()));
                }
            }

            /*
            zonaDibujo.toFront();
            if (e.getButton() == MouseButton.PRIMARY) {
                dibujarLinea(e.getX(), e.getY());
            } //else if (e.getButton() == MouseButton.SECONDARY) {
            //borrarLinea(buscarCentro(e.getX(), e.getY()));
            //}
            else if (e.getButton() == MouseButton.MIDDLE) {
                int cruz = buscarCentro(e.getX(), e.getY());
                if (cruz == -1) {
                    return;
                }
                Cruz c = listaLineas.get(cruz);

                Centro centro = c.getCentro();

                if (DEBUG) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Situacion");
                    alert.setHeaderText("X: " + e.getX() + " Y: " + e.getY());
                    alert.setContentText(centro.toString());

                    alert.showAndWait();
                }
                System.out.println(cruz);
            }*/
        });

        zonaDibujo.setOnMouseDragged(e -> {
            zonaDibujo.toFront();

            if (e.getButton() == MouseButton.MIDDLE) {
                if (dentroAngulos(e)) {
                    double offsetX = e.getSceneX() - orgSceneX;
                    double offsetY = e.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;

                    angulos.setTranslateX(newTranslateX);
                    angulos.setTranslateY(newTranslateY);
                } else {
                    System.out.println(dragging);
                    if (!dragging) {
                        cruzSeleccionada = buscarCentro(e.getX(), e.getY());
                        if (cruzSeleccionada == -1) {
                            return;
                        }
                        dragging = true;
                    }

                    double newMouseX = e.getX();
                    double newMouseY = e.getY();
                    Cruz c = listaLineas.get(cruzSeleccionada);

                    Line h = c.getLineaHorizontal();
                    Line v = c.getLineaVertical();
                    Line hc = c.getLineaHorizontalCentral();
                    Line vc = c.getLineaVerticalCentral();

                    double deltaX = newMouseX - v.getStartX();
                    double deltaY = newMouseY - h.getStartY();

                    double deltaXh = newMouseX - ((hc.getStartX() + hc.getEndX()) / 2);
                    double deltaYh = newMouseY - ((hc.getStartY() + hc.getEndY()) / 2);

                    double deltaXv = newMouseX - ((vc.getStartX() + vc.getEndX()) / 2);
                    double deltaYv = newMouseY - ((vc.getStartY() + vc.getEndY()) / 2);
                    // lineas grandes
                    h.setTranslateY(deltaY);
                  //  h.setTranslateX(deltaX);
                //    v.setTranslateY(deltaY);
                    v.setTranslateX(deltaX);
                    // cruz
                    hc.setTranslateX(deltaXh);
                    hc.setTranslateY(deltaYh);
                    vc.setTranslateX(deltaXv);
                    vc.setTranslateY(deltaYv);

                    c.setCentro(new Centro(e.getX(), e.getY())); // si no se actualiza el centro no se seleccionan como es debido

                    //    listaLineas.add(new Cruz(v,h,vc,hc));
                    //   listaLineas.remove(c);
                    if (DEBUG) {
                        System.out.println("-----Translated------");
                        System.out.println("hc.getTranslateX() " + hc.getTranslateX() + " hc.getTranslateY() " + hc.getTranslateY());
                        System.out.println("X: " + deltaXh + " Y: " + deltaYh);
                        System.out.println("Estoy en hc " + hc.getStartX() + "   " + hc.getStartY());
                        System.out.println("Estoy en vc " + vc.getStartX() + "   " + vc.getStartY());
                        System.out.println("-------------------------");
                    }
                }
            } else if (e.getButton() == MouseButton.PRIMARY) { //pone la guarda de dibujar a false
                draw = false;
            }
        });

        zonaDibujo.setOnMouseReleased(e -> {
            dragging = false;
            if (draw && e.getButton() == MouseButton.PRIMARY) { //solo se dibuja si no se ha hecho drag
                dibujarLinea(e.getX(), e.getY(),0);
            }
            
        });

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void moverAngulos(MouseEvent t) {
        if (!clicMedio(t)) {
            return;
        }

        orgSceneX = t.getSceneX();
        orgSceneY = t.getSceneY();
        orgTranslateX = angulos.getTranslateX();
        orgTranslateY = angulos.getTranslateY();
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

    EventHandler<MouseEvent> OnMouseDraggedEventHandler
            = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent t) {
            if (!clicMedio(t) || !canBeDragged) {
                return;
            }

            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;
            double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;

            angulos.setTranslateX(newTranslateX);
            angulos.setTranslateY(newTranslateY);
        }
    };

    private void dibujarLinea(double x, double y, int angulo) {
        System.out.println("AAAA");
        Cruz cruz = new Cruz(x, y, ALTURA, LONGITUD, angulo);
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
        dibujo.addCentro(cruz.getCentro());
        dibujo.imprimirDibujo();
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
        dibujo.removeCentro(indice);
    }

    EventHandler<MouseEvent> escogerNodo
            = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (clicMedio(t)) {

                if (dentroAngulos(t)) {

                    moverAngulos(t);
                } else {

                    canBeDragged = false;
                }

                //y mover
            } else {
                zonaDibujo.toFront();
                if (clicIzquierdo(t)) {
                    dibujarLinea(t.getX(), t.getY(), 60);
                } else {
                    borrarLinea(buscarCentro(t.getX(), t.getY()));
                }
            }

        }
    };
    // aquí hay que escoger el centro más cercano, no el primero que encuentre

    private int buscarCentro(double x, double y) {
        Centro centro;
        double centroX, centroY;
        double distanciaMinima = rango + 1;
        double aux;
        int indice = -1;

        for (int i = 0; i < listaLineas.size(); i++) {
            centro = listaLineas.get(i).getCentro();
            centroX = centro.getX();
            aux = 0;
            System.out.println("Cruz " + i);
            if (Math.abs(centroX - x) <= rango) {
                centroY = centro.getY();
                aux += Math.abs(centroX - x);
                if (Math.abs(centroY - y) <= rango) {
                    aux += Math.abs(centroY - y);
                    System.out.println("Ditancia entre clic y cruz" + aux);
                    if (aux < distanciaMinima) {
                        indice = i;
                        distanciaMinima = aux;
                    }

                }
            }
        }

        return indice;
    }

    private boolean dentroAngulos(MouseEvent t) {

        Bounds limites = angulos.getBoundsInParent();

        double centroX = limites.getMinX() + 80;
        double centroY = limites.getMinY() + 80;

        if (t.getX() < centroX + 24 && t.getX() > centroX - 24) {
            if (t.getY() < centroY + 24 && t.getY() > centroY - 24) {
                canBeDragged = true;

                return true;
            }
        }

        return false;
    }

    public void sop(String r) {
        System.out.println(r);
    }

    private void dibujarArrayLineas(ArrayList<Cruz> listaLineas) {

    }

    private void guardarXML() {
        Dibujo dib = new Dibujo();

        try {

            File file = new File("Carta.xml");
            // file name

            JAXBContext jaxbContext = JAXBContext.newInstance(Dibujo.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(dibujo, file); // save to a file
            //  jaxbMarshaller.marshal(dibujo, System.out); // eco a consola
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void abrirXML() {
        Dibujo aux = new Dibujo();
        try {
            File file = new File("Carta.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Dibujo.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            aux = (Dibujo) jaxbUnmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        ArrayList<Centro> listaAux = aux.getListaLineas();
        int size = listaAux.size();
        double x, y;

        for (int i = 0; i < size; i++) {
            x = listaAux.get(i).getX();
            y = listaAux.get(i).getY();
            dibujarLinea(x, y, 0);
        }

    }

    public void centrar(){
        Bounds limites = angulos.getBoundsInParent();
        
        double centroX = limites.getMinX() + 80;
        double centroY = limites.getMinY() + 80;
        dibujarLinea(centroX, centroY, 60);
    }
}
