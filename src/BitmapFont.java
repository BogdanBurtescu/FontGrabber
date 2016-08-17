import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class BitmapFont {

    @JsonIgnore
    private BufferedImage bitmap;

    @JsonProperty("width")
    private int width;

    @JsonProperty("height")
    private int height;

    @JsonProperty("size")
    private int size;

    @JsonProperty("imageFileName")
    private String imageFileName;

    @JsonProperty("type")
    private String type;

    @JsonProperty("isItalic")
    private boolean isItalic;

    @JsonProperty("isBold")
    private boolean isBold;

    @JsonProperty("monoLogicalWidth")
    private double monoLogicalWidth;

    @JsonProperty("charMaxHeight")
    private double charMaxHeight;

    @JsonProperty("familyName")
    private String familyName;

    @JsonProperty("chars")
    private ArrayList<Glyph> chars = new ArrayList<>();

    public BitmapFont(int width,
                      int height,
                      String imageFileName,
                      String familyName,
                      BufferedImage bitmap,
                      int size,
                      String type,
                      boolean isItalic,
                      boolean isBold,
                      double monoLogicalWidth,
                      double charMaxHeight,
                      ArrayList<Glyph> chars) {

        this.width = width;
        this.height = height;
        this.imageFileName = imageFileName;
        this.bitmap = bitmap;
        this.size = size;
        this.type = type;
        this.chars = chars;
        this.charMaxHeight = charMaxHeight;
        this.isBold = isBold;
        this.isItalic = isItalic;
        this.monoLogicalWidth = monoLogicalWidth;
        this.familyName = familyName;
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
