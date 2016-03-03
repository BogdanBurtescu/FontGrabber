import com.fasterxml.jackson.databind.ObjectMapper;
import org.kohsuke.args4j.Argument;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.Buffer;
import java.util.*;
import java.util.List;

public class BitmapFontCreator
{


    private char fontType;

    private int MAX_CHARS_PER_LINE = 15;

    private int HORIZONTAL_CHAR_SEPARATOR_DIMENSION = 2;

    private int BUFFER = 5;

    public void setFontType(char fontType) {
        if(fontType == 'B' || fontType == 'I' || fontType == 'R'){
            this.fontType = fontType;

        }
    }

    public void setTtf(String ttf) {

        this.ttf = ttf;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setGlyphFile(String glyphFile) {
        this.glyphFile = glyphFile;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setAntiAlias(boolean antiAlias) {
        this.antiAlias = antiAlias;
    }

    public void setOutDir(String outDir) {
        this.outDir = outDir;
    }

    private String ttf;
    private int size = -1;
    private String glyphFile;
    private String color = "FFFFFFFF";
    private boolean antiAlias = false;
    private String outDir = ".";

    @Argument
    private List<String> arguments = new ArrayList<String>();

    public BitmapFontCreator()
    {
    }

    public BitmapFont writeFonts() throws FontFormatException, IOException
    {
        final String glyphs = getFileAsString("glyphs.txt");

        BitmapFont font;
        try {
            font = createFonts(ttf, size, glyphs, (int)Long.parseLong(color, 16), antiAlias);
        }catch (Exception e){
            System.out.println("Unable to create font");
            return null;
        }

        File file = new File(ttf);
        String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
        System.out.println("Writing " + outDir + File.separator + fileName + ".png");


        ImageIO.write(font.getImage(), "png", new FileOutputStream(new File(outDir + File.separator + fileName + ".png")));





        System.out.println("Writing " + outDir + File.separator + fileName + ".json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(outDir + File.separator + fileName + ".json"), font);
        return font;

    }


    private BitmapFont createFonts(String fontFile, int size, String glyphs, int argb, boolean antiAlias) throws FontFormatException, IOException {
        InputStream is = new FileInputStream(fontFile);
        Font font = Font.createFont(Font.TRUETYPE_FONT, is);

        if(this.fontType == 'I')
        {
            font = font.deriveFont(Font.ITALIC, size);
        }
        if(this.fontType == 'B')
        {
            font = font.deriveFont(Font.BOLD, size);
        }
        if(this.fontType == 'R')
        {
            font = font.deriveFont(Font.PLAIN, size);
        }

        return createFontMetrics(font, size, glyphs, argb, antiAlias);
    }

    private BitmapFont createFontMetrics(Font font, int size, String glyphs, int argb, boolean antiAlias){
        final int verticalSpacing = 1;

        FontMetrics fm = new Canvas().getFontMetrics(font);
        final int ascent = (int)fm.getAscent();
        final int descent = (int)fm.getDescent();
        int totalNecessaryWidth = this.HORIZONTAL_CHAR_SEPARATOR_DIMENSION * this.MAX_CHARS_PER_LINE;
        for (int i = 0; i<glyphs.length(); i++)
        {
            int charWidth = fm.charWidth(glyphs.codePointAt(i));
            totalNecessaryWidth += charWidth;
        }

        final int area = fm.stringWidth(glyphs) * (ascent + descent + verticalSpacing);
        final int width = Integer.highestOneBit((int)Math.ceil(Math.sqrt(area))) << 1;
        final int height = (ascent + descent + verticalSpacing) * glyphs.length()/ this.MAX_CHARS_PER_LINE;


        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage image = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);

        Graphics2D graphics = (Graphics2D)image.getGraphics();
        graphics.setColor(new Color(argb, true));
        graphics.setFont(font);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAlias ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        ArrayList<Glyph> chars = new ArrayList<Glyph>();
        final int glyphCount = glyphs.length();
        int x = 0;
        int y = ascent;
        String glyphList = "";
        int charNum = 0;
        for(int i = 0; i < glyphCount; i++) {
            char glyph = glyphs.charAt(i);
            String glyphString = Character.toString(glyph);
            Rectangle2D r2d = fm.getStringBounds(glyphString, graphics);
            glyphList += glyph;


            int charIndex = graphics.getFont().createGlyphVector(fm.getFontRenderContext(), glyphList).getGlyphCharIndex(i);


            double xPos = graphics.getFont().createGlyphVector(fm.getFontRenderContext(), glyphList).getGlyphPosition(charIndex).getX();
            double yPos = graphics.getFont().createGlyphVector(fm.getFontRenderContext(), glyphList).getGlyphPosition(charIndex).getY();

            int glyphWidth = r2d.getBounds().width;
            System.out.println(glyphWidth);
            if (charNum > this.MAX_CHARS_PER_LINE) {
                charNum = 0;
                x = 0;
                y = y + ascent + descent + verticalSpacing;
            }

            charNum++;
            int charLeftBearing = (int)graphics.getFont().createGlyphVector(fm.getFontRenderContext(), glyphList).getGlyphMetrics(charIndex).getLSB();
            int charRightBearing = (int)graphics.getFont().createGlyphVector(fm.getFontRenderContext(), glyphList).getGlyphMetrics(charIndex).getRSB();

            graphics.drawString(glyphString, x * this.HORIZONTAL_CHAR_SEPARATOR_DIMENSION, y);
            int nameASCII = (int) glyph;
            String nameHex = String.format("%04x", (int) glyph);
            String ASCIICode = Integer.toString(nameASCII);
            int charWidth = (int)(graphics.getFont().createGlyphVector(fm.getFontRenderContext(), glyphList).getGlyphMetrics(charIndex).getBounds2D().getBounds().getMaxX() - graphics.getFont().createGlyphVector(fm.getFontRenderContext(), glyphList).getGlyphMetrics(charIndex).getBounds2D().getBounds().getMinX());
            int charHeight = (int)(graphics.getFont().createGlyphVector(fm.getFontRenderContext(), glyphList).getGlyphMetrics(charIndex).getBounds2D().getBounds().getMaxY() - graphics.getFont().createGlyphVector(fm.getFontRenderContext(), glyphList).getGlyphMetrics(charIndex).getBounds2D().getBounds().getMinY());
            int[] charPos = new int[2];
            charPos[0] = (int)xPos;
            charPos[1] = y;
            int charAscent = 0;
            int charDescent = 0;
            Glyph arrayGlyph = new Glyph(nameHex, ASCIICode, charWidth, charHeight, charPos, charAscent ,charDescent ,charLeftBearing, charRightBearing);
            chars.add(arrayGlyph);

            x += glyphWidth;
        }

        String fontType = null;
        if(font.isBold())
        {
            fontType = "Bold";
        }

        if(font.isPlain())
        {
            fontType = "Regular";
        }

        if(font.isItalic())
        {
            fontType = "Italic";
        }


        BitmapFont bitmapFont = new BitmapFont(font.getName(), image, size, fontType, chars);
        return bitmapFont;
    }


    private String getFileAsString(String filename) throws FileNotFoundException {
        Scanner s = new Scanner(new FileInputStream(filename)).useDelimiter("\\A");
        return s.hasNext() ? s.next() : null;
    }

}