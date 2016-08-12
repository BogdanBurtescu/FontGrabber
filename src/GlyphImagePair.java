import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class GlyphImagePair {

    private BufferedImage pngImage;
    private ArrayList<Glyph> glyphs;



    public GlyphImagePair(BufferedImage pngImage, ArrayList<Glyph> glyphs) {
        this.pngImage = pngImage;
        this.glyphs = glyphs;
    }

    public BufferedImage getPngImage() {
        return pngImage;
    }

    public void setPngImage(BufferedImage pngImage) {
        this.pngImage = pngImage;
    }

    public ArrayList<Glyph> getGlyphs() {
        return glyphs;
    }

    public void setGlyphs(ArrayList<Glyph> glyphs) {
        this.glyphs = glyphs;
    }
}
