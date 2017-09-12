
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user
 */
public  class RoundedBorderCellRenderer extends CellRenderer {
    public RoundedBorderCellRenderer(Cell modelElement) {
        super(modelElement);
    }

    @Override
    public void draw(DrawContext drawContext) {
        drawContext.getCanvas().roundRectangle(getOccupiedAreaBBox().getX() + 1.5f, getOccupiedAreaBBox().getY() + 1.5f,
                getOccupiedAreaBBox().getWidth() - 3, getOccupiedAreaBBox().getHeight() - 3, 4);
        drawContext.getCanvas().stroke();
        super.draw(drawContext);
    }
}