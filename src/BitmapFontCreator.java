import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import sun.font.CoreMetrics;
import sun.font.FontLineMetrics;
import sun.font.StandardGlyphVector;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.GlyphMetrics;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BitmapFontCreator
{

    @Option(name="-f", aliases={"--font","--ttf"}, usage="TTF-file to create bitmap font from", required=true)
    private String ttf;

    @Option(name="-fN", aliases={"--familyName"}, usage="New family name for font", required=true)
    private String familyName;

    @Option(name="-s", aliases={"--size"}, usage="Size to render font in (pixels)", required=true)
    private int size = -1;

    @Option(name="-sF", aliases={"--sizemetrics"}, usage="Size to render font metrics in (pixels)", required=true)
    private int sizeForMetrics = -1;

    @Option(name="-g", aliases={"--glyphs"}, usage="path to a single line text file containing all glyphs/characters to render", required=false)
    private String glyphFile;

    @Option(name="-b", aliases={"--bold"}, usage="boolean to set bold font", required=false)
    private boolean bold;

    @Option(name="-i", aliases={"--italic"}, usage="boolean to set italic font in png", required=false)
    private boolean italic;

    @Option(name="-r", aliases={"--regular"}, usage="boolean to set regular font in png", required=false)
    private boolean regular;

    @Option(name="-bId", aliases={"--boldId"}, usage="boolean to set bold font identification in json metrics", required=false)
    private boolean boldId;

    @Option(name="-iId", aliases={"--italicId"}, usage="boolean to set italic font identification in json metrics", required=false)
    private boolean italicId;

    @Option(name="-rId", aliases={"--regularId"}, usage="boolean to set regular font identification in json metrics", required=false)
    private boolean regularId;

    @Option(name="-a", aliases={"--antialias"}, usage="Render font with anti alias enabled", required=false)
    private boolean antiAlias = false;

    @Option(name="-o", aliases={"--out"}, usage="Output directory to write PNG and JSON file to", required=false)
    private String outDir = ".";

    private int MAX_CHARS_PER_LINE = 16;

    private int HORIZ_CHAR_SEPARATOR = 2;

    private String color = "FFFFFFFF";

    private String exportNameComponent;


    @Argument
    private List<String> arguments = new ArrayList<String>();

    public BitmapFontCreator()
    {
    }

    public BitmapFont writeFonts(String[] args) throws FontFormatException, IOException
    {

        CmdLineParser argsParser = new CmdLineParser(this);
        try {
            argsParser.parseArgument(args);
        }
        catch (CmdLineException e) {
            System.err.println("Usage: java -jar bitmapfontcreator.jar [options...]");
            argsParser.printUsage(System.err);
            return null;
        }
        String glyphs;
        if(glyphFile == null){
             glyphs = getFileAsString("glyphs.txt");
        }else{
            glyphs = getFileAsString(glyphFile);
        }

        if(boldId){
            this.exportNameComponent = "Bold";
        }
        if(italicId){
            this.exportNameComponent = "Italic";
        }
        if(boldId && italicId){
            this.exportNameComponent = "BoldItalic";
        }
        if(!boldId && !italicId){
            this.exportNameComponent = "Regular";
        }


        BitmapFont font;
        try {
            font = createFonts(ttf, size, sizeForMetrics, glyphs, (int)Long.parseLong(color, 16), antiAlias);
        }catch (Exception e){
            System.out.println("Unable to create font");
            return null;
        }




        File resultDirectory = new File(outDir + File.separator + familyName + size + this.exportNameComponent);
        if(!resultDirectory.exists())
        {
            boolean result = false;
            System.out.println("Creating directory " + resultDirectory.getName());
            try{
                resultDirectory.mkdir();
                result = true;
            }catch (SecurityException se){
            }
            if(result){
                System.out.println("Directory " + resultDirectory.getName() + " has been created.");
            }
        }



        System.out.println("Generating " + outDir + File.separator + resultDirectory.getName() + File.separator + familyName + size + this.exportNameComponent + ".png");

        ImageIO.write(font.getImage(), "png", new FileOutputStream(new File(outDir + File.separator + resultDirectory.getName() + File.separator+ familyName + size + this.exportNameComponent + ".png")));

        System.out.println("Generating " + outDir + File.separator + resultDirectory.getName() + File.separator + familyName + sizeForMetrics  + this.exportNameComponent + ".json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File(outDir + File.separator + resultDirectory.getName() + File.separator + familyName + sizeForMetrics + this.exportNameComponent + ".json"), font);
        return font;
    }

    private BitmapFont createFonts(String fontFile, int size, int sizeForMetrics, String glyphs, int argb, boolean antiAlias) throws FontFormatException, IOException {
        InputStream is = new FileInputStream(fontFile);
        InputStream isFontForMEtrics = new FileInputStream(fontFile);

        Font font = Font.createFont(Font.TRUETYPE_FONT, is);
        Font fontForMetricsCalculations = Font.createFont(Font.TRUETYPE_FONT, isFontForMEtrics);
        if(bold){
            font = font.deriveFont(Font.BOLD, size);
            fontForMetricsCalculations = font.deriveFont(Font.BOLD, sizeForMetrics);
        }
        if(italic){
            font = font.deriveFont(Font.ITALIC, size);
            fontForMetricsCalculations = font.deriveFont(Font.ITALIC, sizeForMetrics);

        }
        if(italic && bold){
            font = font.deriveFont(Font.BOLD + Font.ITALIC, size);
            fontForMetricsCalculations = font.deriveFont(Font.BOLD + Font.ITALIC, sizeForMetrics);

        }

        if(!italic && !bold){
            font = font.deriveFont(Font.PLAIN, size);
            fontForMetricsCalculations = font.deriveFont(Font.PLAIN, sizeForMetrics);

        }

        return createFontMetrics(font, fontForMetricsCalculations, size, glyphs, argb, antiAlias);
    }

    private BitmapFont createFontMetrics(Font font, Font fontForMetricsCalculations, int size, String glyphs, int argb, boolean antiAlias){


        GlyphImagePair glyphImagePair = this._obtainGlyphImagePair(glyphs, font);

        String fontType = null;
        if(boldId)
        {
            fontType = "Bold";
        }

        if(!boldId && !italicId)
        {
            fontType = "Regular";
        }

        if(italicId)
        {
            fontType = "Italic";
        }



        int jsonWidth = 256;
        int jsonHeight = 256;

        BitmapFont bitmapFont = new BitmapFont(jsonWidth,
                jsonHeight,
                familyName + sizeForMetrics + this.exportNameComponent,
                glyphImagePair.getPngImage(),
                sizeForMetrics,
                fontType,
                glyphImagePair.getGlyphs());

        return bitmapFont;
    }


    private String getFileAsString(String filename) throws FileNotFoundException {
        Scanner s = new Scanner(new FileInputStream(filename)).useDelimiter("\\A");
        return s.hasNext() ? s.next() : null;
    }


    private BufferedImage _createPNG(String glyphs, Font font)
    {
        final int verticalSpacing = 1;

        FontMetrics fontMetricsPng = new Canvas().getFontMetrics(font);

        final int ascent = fontMetricsPng.getAscent();
        final int descent = fontMetricsPng.getDescent();

        final int area = fontMetricsPng.stringWidth(glyphs) * (ascent + descent + verticalSpacing);
        final int width = (int)Math.ceil(Math.sqrt(area)/2.5) << 2;
        final int height = (ascent + descent + verticalSpacing) * glyphs.length()/ this.MAX_CHARS_PER_LINE;


        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage image = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);

        Graphics2D graphics = (Graphics2D)image.getGraphics();
        graphics.setColor(new Color((int)Long.parseLong(color, 16), true));
        graphics.setFont(font);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAlias ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);


        final int glyphCount = glyphs.length();
        int x = 0;
        int y = fontMetricsPng.getAscent();

        String glyphList = "";
        int charNum = 0;
        for(int i = 0; i < glyphCount; i++) {
            char glyph = glyphs.charAt(i);
            String glyphString = Character.toString(glyph);
            Rectangle2D r2d = fontMetricsPng.getStringBounds(glyphString, graphics);

            glyphList += glyph;

            int glyphWidth = r2d.getBounds().width;

            if (charNum > this.MAX_CHARS_PER_LINE) {
                charNum = 0;
                x = 0;
                y = y + fontMetricsPng.getAscent() + fontMetricsPng.getDescent() + verticalSpacing;
            }
            charNum++;
            graphics.drawString(glyphString, x * this.HORIZ_CHAR_SEPARATOR, y);
            x += glyphWidth;

        }
        return image;

    }




    private GlyphImagePair _obtainGlyphImagePair(String glyphs, Font font)
    {
        final int verticalSpacing = 1;

        FontMetrics fontMetricsPng = new Canvas().getFontMetrics(font);

        final int ascent = fontMetricsPng.getAscent();
        final int descent = fontMetricsPng.getDescent();

        final int area = fontMetricsPng.stringWidth(glyphs) * (ascent + descent + verticalSpacing);
        final int width = (int)Math.ceil(Math.sqrt(area)/2.5) << 2;
        final int height = (ascent + descent + verticalSpacing) * glyphs.length()/ this.MAX_CHARS_PER_LINE;


        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage image = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);

        Graphics2D graphics = (Graphics2D)image.getGraphics();
        graphics.setColor(new Color((int)Long.parseLong(color, 16), true));
        graphics.setFont(font);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAlias ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);


        final int glyphCount = glyphs.length();
        int x = 0;
        int y = fontMetricsPng.getAscent();

        String glyphList = "";
        int charNum = 0;


        ArrayList<Glyph> chars = new ArrayList<>();


        for(int i = 0; i < glyphCount; i++) {
            char glyph = glyphs.charAt(i);
            String glyphString = Character.toString(glyph);
            Rectangle2D r2d = fontMetricsPng.getStringBounds(glyphString, graphics);

            glyphList += glyph;

            int glyphWidth = r2d.getBounds().width;
            int glyphHeight = r2d.getBounds().height;

            if (charNum > this.MAX_CHARS_PER_LINE) {
                charNum = 0;
                x = 0;
                y = y + fontMetricsPng.getAscent() + fontMetricsPng.getDescent() + verticalSpacing;
            }
            charNum++;
            graphics.drawString(glyphString, x * this.HORIZ_CHAR_SEPARATOR, y);


            GlyphMetrics individualGlyphMetrics =
                    graphics.getFont().createGlyphVector(fontMetricsPng.getFontRenderContext(), glyphList).getGlyphMetrics(i);




            int[] charPos = new int[2];

            charNum++;

            int nameASCII = (int) glyph;
            String nameHex = String.format("%02x", (int) glyph);
            String ASCIICode = Integer.toString(nameASCII);

            double charLeftBearing = individualGlyphMetrics.getLSB();
            double charRightBearing = individualGlyphMetrics.getRSB();

            charPos[0] = x * this.HORIZ_CHAR_SEPARATOR;
            charPos[1] = y - ascent;

            int charWidth = (int)(individualGlyphMetrics.getBounds2D().getFrame().getWidth() + individualGlyphMetrics.getAdvance());
            int charHeight = (int)(individualGlyphMetrics.getBounds2D().getFrame().getHeight() + individualGlyphMetrics.getAdvance());


            double maxY = individualGlyphMetrics.getBounds2D().getMaxY();
            double minY = individualGlyphMetrics.getBounds2D().getMinY();

            int glyphLogicalWidth = (int)individualGlyphMetrics.getAdvance();

            int glyphAscent = (int)minY;
            int glyphDescent = (int)maxY;
            Glyph arrayGlyph =
                    new Glyph(nameHex, ASCIICode, charWidth, charHeight, charPos,
                            Math.abs(glyphAscent), glyphDescent, charLeftBearing, charRightBearing, (int) r2d.getHeight(), glyphLogicalWidth);
            chars.add(arrayGlyph);
            x += glyphWidth;

        }

        return new GlyphImagePair(image, chars);
    }

}