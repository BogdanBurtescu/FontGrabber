import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BitmapFont {

    public static class Glyph {


        @JsonProperty("nameHex")
        public String nameHex;

        @JsonProperty("nameASCII")
        public String nameASCII;

        @JsonProperty("charWidth")
        public int charWidth;

        @JsonProperty("charHeight")
        public int charHeight;

        @JsonProperty("charAscent")
        public int charAscent;

        @JsonProperty("charDescent")
        public int charDescent;

        @JsonProperty("charLeftBearing")
        public double charLeftBearing;

        @JsonProperty("charRightBearing")
        public double charRightBearing;

        @JsonProperty("charMaxHeight")
        public int charMaxHeight;

        @JsonProperty("charPosition")
        public int[] charPosition;

        public Glyph(String nameHex, String nameASCII, int charWidth, int charHeight, int[] charPosition, int charAscent, int charDescent,
                     double charLeftBearing, double charRightBearing) {
            this.nameHex = nameHex;
            this.nameASCII = nameASCII;
            this.charWidth = charWidth;
            this.charHeight = charHeight;
            this.charAscent = charAscent;
            this.charDescent = charDescent;
            this.charLeftBearing = charLeftBearing;
            this.charRightBearing = charRightBearing;
            this.charPosition = charPosition;
        }
    }

    @JsonIgnore
    private BufferedImage bitmap;

    @JsonProperty("width")
    private int width;

    @JsonProperty("height")
    private int height;
    @JsonProperty("size")
    private int size;

    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private String type;

    @JsonProperty("chars")
    private ArrayList<Glyph> chars = new ArrayList<>();

    public BitmapFont(String name, BufferedImage bitmap, int size, String type, ArrayList<Glyph> chars) {
        this.name = name;
        this.bitmap = bitmap;
        this.size = size;
        this.type = type;
        this.chars = chars;
    }

    @JsonIgnore
    public BufferedImage getImage() {
        return bitmap;
    }

    @JsonIgnore
    public int getHeight() {
        return size;
    }


//    public int stringWidth(String s) {
//        int width = 0;
//        final int length = s.length();
//        for(int i = 0; i < length; i++) {
//            Glyph g = getGlyph(s.charAt(i));
//            if(g != null) {
//                width += g.w;
//            }
//        }
//        return width;
//    }

}
