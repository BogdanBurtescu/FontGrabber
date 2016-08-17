import com.fasterxml.jackson.annotation.JsonProperty;

public class Glyph {

    @JsonProperty("nameASCII")
    public String nameASCII;

    @JsonProperty("glyphWidth")
    public int glyphWidth;

    @JsonProperty("glyphHeight")
    public int glyphHeight;

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

    @JsonProperty("charLogicalWidth")
    public double charLogicalWidth;

    @JsonProperty("glyphPosX")
    public int glyphPosX;

    @JsonProperty("glyphPosY")
    public int glyphPosY;

    @JsonProperty("character")
    public String character;


    public Glyph(String character,
                 String nameASCII,
                 int glyphWidth,
                 int glyphHeight,
                 int glyphPosX,
                 int glyphPosY,
                 int charAscent,
                 int charDescent,
                 double charLeftBearing,
                 double charRightBearing,
                 int charMaxHeight,
                 double charLogicalWidth) {

        this.nameASCII = nameASCII;
        this.glyphWidth = glyphWidth;
        this.glyphHeight = glyphHeight;
        this.charAscent = charAscent;
        this.charDescent = charDescent;
        this.charLeftBearing = charLeftBearing;
        this.charRightBearing = charRightBearing;
        this.charMaxHeight = charMaxHeight;
        this.charLogicalWidth = charLogicalWidth;
        this.glyphPosX = glyphPosX;
        this.glyphPosY = glyphPosY;
        this.character = character;
    }
}