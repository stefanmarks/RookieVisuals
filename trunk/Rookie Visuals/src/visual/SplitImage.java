package visual;

import static java.awt.Frame.NORMAL;
import parameter.DefaultParameter;
import parameter.Parameter;
import static processing.core.PConstants.TRIANGLES;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Class for a simple image that can be split diagonally
 * 
 * @author  Stefan Marks
 */
public class SplitImage extends AbstractVisual
{
    public SplitImage(String name, PImage image)
    {
        super(name);
        this.image  = image;
        this.aspect = (float) this.image.width / (float) this.image.height; 
        
        splitSize = new DefaultParameter("split", 0); parameters.add(splitSize);
    }  


    @Override
    public void render(PGraphics g)
    {
        preRender(g);
        g.textureMode(NORMAL);
        g.noStroke();
        final float dX = aspect * splitSize.get();
        final float dY =          splitSize.get();
        g.beginShape(TRIANGLES);
            g.texture(image);
            g.vertex(-aspect-dX, -1+dY, 0, 0, 0);
            g.vertex( aspect-dX, -1+dY, 0, 1, 0);
            g.vertex(-aspect-dX,  1+dY, 0, 0, 1);

            g.vertex( aspect+dX, -1-dY, 0, 1, 0);
            g.vertex( aspect+dX,  1-dY, 0, 1, 1);
            g.vertex(-aspect+dX,  1-dY, 0, 0, 1);
        g.endShape();
    }
  
  
    private final PImage    image;
    private final float     aspect; 
    
    private final Parameter splitSize; 
}
