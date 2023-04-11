package byow.Core;

import byow.TileEngine.TERenderer;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

 /**
 *      A class to streamline the menu drawing process
 */
public class Menu {
    private TERenderer ter;
    private int x;
    private int y;
    public Menu(TERenderer terIn, int x, int y) {
        this.ter = terIn;
        this.x = x;
        this.y = y;
    }
    public void drawMenu() {
        Font title = new Font("Arial", Font.PLAIN, 40);
        Font subtitle = new Font("Arial", Font.PLAIN, 18);
        StdDraw.setCanvasSize(x * 10, y * 16);
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(title);
        StdDraw.text(.5, .88, "CS61B: THE GAME");
        StdDraw.setFont(subtitle);
        StdDraw.text(.5, .55, "New Game (N)");
        StdDraw.text(.5, .5, "Load Game (L)");
        StdDraw.text(.5, .45, "Quit (Q)");


    }
    public static void main(String[] args) {
        StdDraw.setPenRadius(0.05);
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.point(0.5, 0.5);
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.line(0.2, 0.2, 0.8, 0.2);
    }
}
