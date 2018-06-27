package Snake.Items;

import Snake.Snake.Snake;

public enum Direction {

    NORTH(){
        @Override
        public int[] getDirection (){
            return getArray(1);
        }
    },
    EAST(){
        @Override
        public int[] getDirection (){
            return getArray(2);
        }
    },
    SOUTH(){
        @Override
        public int[] getDirection (){
            return getArray(3);
        }
    },
    WEST(){
        @Override
        public int[] getDirection (){
            return getArray(0);
        }
    };

    private static int[][] direction =
            {{-Snake.getSize(), 0},
            {0, -Snake.getSize()},
            {Snake.getSize(), 0},
            {0, Snake.getSize()}};

    /**
     * get current direction of snake
     * @return int-array with vector
     */
    public abstract int[] getDirection();

    private static int[] getArray(int index) {
        return direction[index];
    }
}