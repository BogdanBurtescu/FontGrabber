# FontGrabber
TTF Font Grabber for ATS


TTF Font Grabber is an external tool meant to provide ATS with the possibility of importing and processing any family of fonts in .ttf format.


Steps to successfully run the FontGrabber:

1. Copy the FontGrabber.jar file in the folder where you want to use it.
2. Create an empty folder (e.g. outputFonts)
3. Run the jar file as shown below.



Usage: java -jar FontGrabber.jar [options...]
 -a (--antialias)       : Render font with anti alias enabled
 -b (--bold)            : boolean to set bold font
 -bId (--boldId)        : boolean to set bold font identification in json
                          metrics
 -f (--font, --ttf) VAL : TTF-file to create bitmap font from
 -fN (--familyName) VAL : New family name for font
 -g (--glyphs) VAL      : path to a single line text file containing all
                          glyphs/characters to render
 -i (--italic)          : boolean to set italic font in png
 -iId (--italicId)      : boolean to set italic font identification in json
                          metrics
 -o (--out) VAL         : Output directory to write PNG and JSON file to
 -r (--regular)         : boolean to set regular font in png
 -rId (--regularId)     : boolean to set regular font identification in json
                          metrics
 -s (--size) N          : Size to render font in (pixels)
 -sF (--sizemetrics) N  : Size to render font metrics in (pixels)
 
 FontGrabber runner example:
 
 java -jar FontGrabber.jar --antialias --ttf /home/bogdan/Projects/FontGrabber/LiberationMono-Regular.ttf --out /home/bogdan/Projects/FontGrabber/FontGrabber_jar/outputFonts --size 10 --sizemetrics 10 --regular --familyName Liberation
