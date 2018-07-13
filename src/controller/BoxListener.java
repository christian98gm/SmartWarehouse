package controller;

import model.Point3D;
import model.Product;
import model.Shelf;
import model.ShelvesColumn;
import view.WarehouseView;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Material pràctica 1 Programació Avançada i Estructura de Dades
 * Enginyeria Informàtica
 * © La Salle Campus Barcelona - Departament d'Enginyeria
 *
 * Aquesta classe permet l'actualització de la GUI amb les dades del
 * magatzem donat que escolta els clics realitzats sobre la interfície
 * gràfica. El mètode 'mouseClicked' es crida cada cop que una casella
 * del magatzem que sigui d'una prestatgeria s'hagi clicat.
 *
 * Aquesta classe és modificable, en cap cas ha de suposar una limitació
 * en com ha estat implementada. És a dir, si necessiteu algun paràmetre
 * extra o atribut (o bé us sobren) teniu tot el dret a canviar-ho. L'únic
 * que s'ha de mantenir i completar és el mètode 'mouseClicked'.
 *
 * @author Albert Pernía Vázquez
 */
public class BoxListener implements MouseListener {

    private WarehouseView view;
    private ArrayList<ShelvesColumn> shelvesColumns;

    public BoxListener(WarehouseView view, ArrayList<ShelvesColumn> shelvesColumns) {
        this.view = view;
        this.shelvesColumns = shelvesColumns;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        Point point = e.getPoint();
        Point p = view.getBoxClickedPosition(point);

        if(p == null) {
            System.out.println(System.lineSeparator() + "null point");
        } else {
            //Check if is occupied
            for(int i = 0; i < shelvesColumns.size(); i++) {
                ShelvesColumn shelvesColumn = shelvesColumns.get(i);
                if(shelvesColumn.getShelvesPoint().equals(p)) {
                    Shelf[] shelves = shelvesColumn.getShelves();
                    String[] boxInfo = new String[shelves.length];
                    for(int j = 0; j < shelves.length; j++) {
                        Point3D point3D = shelves[j].getPoint();
                        if(shelves[j].getProduct() != null) {
                            Product product = shelves[j].getProduct();
                            boxInfo[j] = String.format("(x,y,z)=(%d,%d,%d) %s - ID: %d", point3D.getX(), point3D.getY(),
                                    point3D.getZ(), product.getName(), product.getId());
                        } else {
                            boxInfo[j] = String.format("(x,y,z)=(%d,%d,%d) %s", point3D.getX(), point3D.getY(),
                                    point3D.getZ(), "Empty");
                        }
                    }
                    view.setBoxInfo(boxInfo);
                }
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

}
