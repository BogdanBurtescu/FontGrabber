
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;

import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontMetrics;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, FontFormatException {

        BitmapFontCreator bitmapFontCreator = new BitmapFontCreator();


        System.out.println("TTF Font Grabber");

        System.out.println("Insert path of the .ttf file: ");
        Scanner userInput = new Scanner(System.in);

        String ttfFilePath;
        ttfFilePath = userInput.next();
        bitmapFontCreator.setTtf(ttfFilePath);



        System.out.println("Antialiasing? (Y/N) ");
        String antialias;
        antialias = userInput.next();
        if(antialias.equals("Y"))
        {
            bitmapFontCreator.setAntiAlias(true);
        }else{
            bitmapFontCreator.setAntiAlias(false);
        }

        System.out.println("Font size (e.g. 11): ");
        int fontSize;

        do {
            try{
                String s = userInput.next();
                fontSize = Integer.parseInt(s);
                bitmapFontCreator.setSize(fontSize);
                break;

            }
            catch (Exception e)
            {
                System.out.println("Couldn't parse input, please try again");
            }
        } while (true);

        System.out.println("PATH OF OUTPUT DIRECTORY: ");
        String outputDirectory;
        outputDirectory = userInput.next();
        bitmapFontCreator.setOutDir(outputDirectory);

        bitmapFontCreator.writeFonts();

    }
}
