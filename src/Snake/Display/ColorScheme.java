package Snake.Display;

import javafx.scene.paint.Color;

/**
 * change between different color-schemes
 */
public enum ColorScheme {

    BLUE(){
        @Override
        public Color head(){
            return colors[0][0];
        }
        @Override
        public Color body(){
            return colors[0][1];
        }
        @Override
        public Color food(){
            return colors[0][2];
        }
        @Override
        public Color big(){
            return colors[0][3];
        }
    },
    RED(){
        @Override
        public Color head(){
            return colors[1][0];
        }
        @Override
        public Color body(){
            return colors[1][1];
        }
        @Override
        public Color food(){
            return colors[1][2];
        }
        @Override
        public Color big(){
            return colors[1][3];
        }
    },
    ORANGE(){
        @Override
        public Color head(){
            return colors[2][0];
        }
        @Override
        public Color body(){
            return colors[2][1];
        }
        @Override
        public Color food(){
            return colors[2][2];
        }
        @Override
        public Color big(){
            return colors[2][3];
        }
    },
    GREEN() {
        @Override
        public Color head() {
            return colors[3][0];
        }

        @Override
        public Color body() {
            return colors[3][1];
        }

        @Override
        public Color food() {
            return colors[3][2];
        }

        @Override
        public Color big() {
            return colors[3][3];
        }
    },
    PURPLE() {
        @Override
        public Color head() {
            return colors[4][0];
        }

        @Override
        public Color body() {
            return colors[4][1];
        }

        @Override
        public Color food() {
            return colors[4][2];
        }

        @Override
        public Color big() {
            return colors[4][3];
        }
    };

    public abstract Color head();
    public abstract Color body();
    public abstract Color food();
    public abstract Color big();

    private static final Color[][] colors =
            // SnakeHead, SnakeBody, FoodNormal, FoodBig
            {{Color.web("#3e5a78"), Color.web("#112339"), Color.web("#9d9290"), Color.web("#6c2d03")},
            {Color.web("#4e0111"), Color.web("#93001a"), Color.web("#6f645e"), Color.web("#cd9759")},
            {Color.web("#e0331b"), Color.web("#f85d29"), Color.web("#7ad0b8"), Color.web("#3d6b60")},
            {Color.web("#596c16"), Color.web("#a6ac56"), Color.web("#b2844a"), Color.web("#952622")},
            {Color.web("#905bb3"), Color.web("#e192e3"), Color.web("#a7c4fd"), Color.web("#f6bd2d")}};
}
