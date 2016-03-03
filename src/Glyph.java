import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ats on 3/3/16.
 */
public class Glyph {


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