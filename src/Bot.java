public abstract class Bot {
    // public int[] move() {
    //     // create random move
    //     int randomX = (int) (Math.random()*8);
    //     int randomY = (int) (Math.random()*8);
    //     System.out.println("Ini X: " + randomX + " Ini Y: " + randomY);
    //     return new int[]{randomY, randomX};
    // }
    public abstract int[] move(Board board);
}
