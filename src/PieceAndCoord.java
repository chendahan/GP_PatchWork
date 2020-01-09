import java.util.ArrayList;
import java.util.List;

public class PieceAndCoord {
    Piece piece;
    Dot coord;
    List<Dot> shape;
    int index = 0;

    public PieceAndCoord(Piece piece, Dot coord, List<Dot> shape, int index) {
        this.piece = piece;
        this.coord = coord;
        this.shape = shape;
        this.index = index;
    }

    public PieceAndCoord() {
        piece = new Piece();
        coord = new Dot();
        shape = new ArrayList<Dot>();
    }
}
