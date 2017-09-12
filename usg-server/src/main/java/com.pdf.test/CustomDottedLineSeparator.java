
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user
 */
 class CustomDashedLineSeparator extends DottedLineSeparator {
        protected float dash = 5;
        protected float phase = 1.5f;
 
        public float getDash() {
            return dash;
        }
 
        public float getPhase() {
            return phase;
        }
 
        public void setDash(float dash) {
            this.dash = dash;
        }
 
        public void setPhase(float phase) {
            this.phase = phase;
        }
 
        public void draw(PdfContentByte canvas, float llx, float lly, float urx, float ury, float y) {
            canvas.saveState();
            canvas.setLineWidth(lineWidth);
            canvas.setLineDash(dash, gap, phase);
            drawLine(canvas, llx, urx, y);
            canvas.restoreState();
        }
    }