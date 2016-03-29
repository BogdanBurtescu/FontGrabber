

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeBuild;
import com.badlogic.gdx.utils.SharedLibraryLoader;

import java.awt.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, FontFormatException {

        BitmapFontCreator bitmapFontCreator = new BitmapFontCreator();

        try {
            bitmapFontCreator.writeFonts(args);
        }
        catch (Exception e) {
            System.err.println("Unable to create bitmap font.");
            System.err.println(e.getMessage());
        }
    }
}
