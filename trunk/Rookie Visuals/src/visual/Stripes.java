package visual;

import parameter.DefaultParameter;
import parameter.ModuloParameter;
import parameter.Parameter;
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
     * @param sizeX  the width of the stripe block
     * @param sizeY  the height of the stripe block
     * @param stepX  the horizontal difference between each stripe
     * @param stepY  the vertical difference between each stripe
     * @param stroke the thickness of each stripe
     */
    public Stripes(float sizeX, float sizeY, float stepX, float stepY, float stroke)
    {
        super("Stripes");
        
        this.parmSizeX  = new DefaultParameter("sizeX", sizeX);     parameters.add(this.parmSizeX);
        this.parmSizeY  = new DefaultParameter("sizeY", sizeY);     parameters.add(this.parmSizeY);
        this.parmStepX  = new DefaultParameter("stepX", stepX);     parameters.add(this.parmStepX);
        this.parmStepY  = new DefaultParameter("stepY", stepY);     parameters.add(this.parmStepY);
        this.parmStroke = new DefaultParameter("stroke", stroke);   parameters.add(this.parmStroke);
        this.parmOffset = new ModuloParameter( "offset", 0, -1, 1); parameters.add(this.parmOffset);
    }
  
  
    @Override
    public void render(PGraphics g)
    {
        preRender(g);
        
        g.noStroke();
        g.fill(0);

        final float dX  = parmStepX.get();
        final float dY  = parmStepY.get();
        final float sY  = parmStroke.get() / 2.0f; 
        final float sX  = dX * sY / dY;
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
        while ( y <= y2 )
        {
            g.beginShape();
                g.vertex(x1-sX, y-sY); // TL
                g.vertex(x2-sX, y-sY); // TR
                g.vertex(x2+sX, y+sY); // BR
                g.vertex(x1+sX, y+sY); // BL
            g.endShape();
            y  += dY;
            x1 += dX;
            x2 += dX;
        }
    }


    private final Parameter parmSizeX, parmSizeY;
    private final Parameter parmStepX, parmStepY;
    private final Parameter parmStroke, parmOffset;
}
