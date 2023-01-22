package mine_game;

public class Position {
    // field
    private int height ; //縦の大きさ
    private int width ; // 横の大きさ
    // Constructor
    Position(int height,int width) {
        this.height = height;
        this.width = width;
        }
    
    // Getter
    int getHeight() {
        return height;
    }
    int getWidth() {
        return width;
    }
}