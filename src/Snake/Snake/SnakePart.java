package Snake.Snake;

public class SnakePart {

    private Position position;
    private String direction;

    public SnakePart(int posX, int posY, String direction){
        this.position = new Position(posX, posY);
        this.direction = direction;
    }

    public Position getPosition() {
        return position;
    }

    public String getDirection() {
        return direction;
    }
}
