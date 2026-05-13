public class Block implements Collidable {
    private Rectangle rectangle;

    public Block(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    @Override
    public Rectangle getCollisionRectangle() {
        return this.rectangle;
    }

    @Override
    public Velocity hit(Point collisionPoint, Velocity currentVelocity) {
        // placeholder for now need to implent the using closion engine 
        return new Velocity(-currentVelocity.getDx(), -currentVelocity.getDy());
    }
    
}
