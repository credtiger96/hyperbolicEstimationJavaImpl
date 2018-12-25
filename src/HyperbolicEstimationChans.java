import org.ejml.simple.SimpleMatrix;
import org.ejml.simple.UtilSimpleMatrix;

public class HyperbolicEstimationChans {

    static void show(String str){
        System.out.println(str);
    }
    private static void show(SimpleMatrix matrix) {
        System.out.println(matrix.toString());
    }

    private static SimpleMatrix duplicateRow(SimpleMatrix D, int num){
        int cols = D.numCols();
        SimpleMatrix res = new SimpleMatrix(num, cols);
        for(int i = 0; i < num; i ++){
            res.insertIntoThis(i, 0, D);
        }
        return res;
    }

    private static SimpleMatrix squareEachElement(SimpleMatrix matrix) {
        SimpleMatrix res = new SimpleMatrix(matrix);
        int rows = matrix.numRows();
        int cols = matrix.numCols();

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++){
                res.set(i, j, matrix.get(i, j) * matrix.get(i, j));
            }
        return res;
    }

    private static SimpleMatrix convertToAtoTDoA(SimpleMatrix ToAs){
        int rows = ToAs.numRows();
        int cols = ToAs.numCols();
        double firstToA = ToAs.get(0, cols - 1);

        SimpleMatrix TDoAs = new SimpleMatrix(ToAs);

        for(int i = 0; i < rows; i++){
            TDoAs.set(i, cols - 1, TDoAs.get(i, cols - 1) - firstToA);
        }

        return TDoAs;
    }

    public static SimpleMatrix performForMany (SimpleMatrix ToAs){
        int rows = ToAs.numRows();
        int cols = ToAs.numCols();
        int dim = cols - 1;
        //show(ToAs);
        //
        SimpleMatrix TDoAs = convertToAtoTDoA(ToAs);

        SimpleMatrix M = TDoAs.cols(0,dim);
        SimpleMatrix Ga = TDoAs.rows(1, rows).minus(duplicateRow(TDoAs.rows(0,1), rows-1));
        //show(Ga);
        SimpleMatrix Q = SimpleMatrix.identity(rows - 1).scale(0.5).plus(0.5);

        //show(Q);
        SimpleMatrix E = Ga.transpose().scale(-1).mult(Q.invert());
        //show(E);
        SimpleMatrix Fi = E.mult(Ga.scale(-1)).invert();
        //show(Fi);
        SimpleMatrix R = Ga.cols(cols-1, cols);
        //show(R);
        //SimpleMatrix RSquared
        SimpleMatrix R_squared = squareEachElement(R);
        //show(R_squared);
        SimpleMatrix  K = new SimpleMatrix(rows, 1);

        for (int i = 0; i < rows; i++){
            K.set(i, 0, M.rows(i, i+1).mult(M.rows(i, i+1).transpose()).get(0, 0));
        }
        //show(K);
        SimpleMatrix h = R_squared.minus(K.rows(1, rows)).plus(K.get(0,0)).scale(0.5);
        //show(r);
        show(Fi.mult(E).mult(h));
        double R0 = Fi.mult(E).mult(h).get(rows-2, 0);

        //show(SimpleMatrix.identity(rows - 1));
        //show(R.plus(R0));
        //show(SimpleMatrix.identity(rows - 1).mult(R.plus(R0)));
        //show(R.plus(R0).mult(SimpleMatrix.identity(rows - 1)));

        SimpleMatrix B = SimpleMatrix.identity(rows - 1);

        for (int i = 0; i < rows - 1; i ++){
            B.insertIntoThis(i, 0,B.rows(i, i +1).scale(R.plus(R0).get(i, 0)));
        }
        //show(B);

        SimpleMatrix Y = B.mult(Q).mult(B);
        //show(Y);

        int netc = 1;
        E = Ga.transpose().scale(-1).mult(Y.scale(netc * netc).invert());
        Fi = E.mult(Ga.scale(-1)).invert();

        //second_est = (Fi * E * h).A.squeeze() ;
        SimpleMatrix second_est = Fi.mult(E).mult(h);
        // show(second_est);

        return second_est.rows(0, cols - 1);
    }
}
