package visual;

import parameter.DefaultParameter;
import parameter.ModuloParameter;
import parameter.Parameter;
import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Visual that renders animated stripes.
 * 
 * @author  Stefan Marks
 */
public class Stripes extends AbstractVisual
{
    /**
     * Creates a new horizontal line pattern 
     * with a specific distance between the line ends.
     * 
     * @param name   the name of the visual
     * @param independentStripes    number of stripes with independent stroke widths
     * @param sizeX  the width of the stripe block
     * @param sizeY  the height of the stripe block
     * @param stepX  the horizontal difference between each stripe
     * @param stepY  the vertical difference between each stripe
     * @param stroke the thickness of each stripe
     */
    public Stripes(String name, int independentStripes, float sizeX, float sizeY, float stepX, float stepY, float stroke)
    {
        super(name);
        
        this.parmSizeX  = new DefaultParameter("sizeX", sizeX);     parameters.add(this.parmSizeX);
        this.parmSizeY  = new DefaultParameter("sizeY", sizeY);     parameters.add(this.parmSizeY);
        this.parmStepX  = new DefaultParameter("stepX", stepX);     parameters.add(this.parmStepX);
        this.parmStepY  = new DefaultParameter("stepY", stepY);     parameters.add(this.parmStepY);
        this.parmOffset = new ModuloParameter( "offset", 0, -1, 1); parameters.add(this.parmOffset);

        this.parmStroke = new Parameter[independentStripes];
        for (int i = 0; i < parmStroke.length; i++)
        {
            parmStroke[i] = new DefaultParameter("stroke[" + i + "]", stroke);   parameters.add(this.parmStroke[i]);
        }
    }
  
  
    @Override
    public void render(PGraphics g)
    {
        preRender(g);
        g.noStroke();
        g.fill(255);
        g.blendMode(PApplet.EXCLUSION);

        final float dX  = parmStepX.get();
        final float dY  = parmStepY.get();
        float x1 = 0;                    // left
        float x2 = x1 + parmSizeX.get(); // right
        float y1 = 0;                    // top
        float y2 = y1 + parmSizeY.get(); // bottom
        float y  = y1;

        // apply offset
        final float off = parmOffset.get();
        x1 += off * dX;
        x2 += off * dX;
        y  += off * dY;
        
        // draw the bars
        int idx  = 0;
        int dIdx = (parmStroke.length > 1) ? 1 : 0;
        while ( y <= y2 )
        {
            final float sY  = parmStroke[idx].get() / 2.0f; 
            final float sX  = dX * sY / dY;

            g.beginShape();
                g.vertex(x1-sX, y-sY); // TL
                g.vertex(x2-sX, y-sY); // TR
                g.vertex(x2+sX, y+sY); // BR
                g.vertex(x1+sX, y+sY); // BL
            g.endShape();
            y  += dY;
            x1 += dX;
            x2 += dX;
            idx += dIdx;
            if ( (idx == parmStroke.length - 1) || (idx == 0) ) { dIdx *= -1; }
        }
        
        g.blendMode(PApplet.BLEND);
    }


    private final Parameter   parmSizeX, parmSizeY;
    private final Parameter   parmStepX, parmStepY;
    private final Parameter[] parmStroke;
    private final Parameter   parmOffset;
}
