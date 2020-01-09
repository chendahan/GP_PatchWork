import java.util.ArrayList;
import java.util.List;

public class PieceAndCoord {
    Piece piece;
    Dot coord;
    List<Dot> shape;

    public PieceAndCoord(Piece piece, Dot coord, List<Dot> shape) {
        this.piece = piece;
        this.coord = coord;
        this.shape = shape;
    }

    public PieceAndCoord() {
        piece = new Piece();
        coord = new Dot();
        shape = new ArrayList<Dot>();
    }
}
