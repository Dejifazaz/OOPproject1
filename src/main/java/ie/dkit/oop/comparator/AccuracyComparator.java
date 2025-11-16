package ie.dkit.oop.comparator;

import ie.dkit.oop.model.GameScore;
import java.util.Comparator;

public class AccuracyComparator implements Comparator<GameScore> {
    
    @Override
    public int compare(GameScore o1, GameScore o2) {
        if (o1 == null || o2 == null) {
            throw new NullPointerException("Cannot compare null GameScore objects");
        }
        
        int accuracyCompare = Double.compare(o2.getAccuracy(), o1.getAccuracy());
        if (accuracyCompare != 0) {
            return accuracyCompare;
        }
        
        return Integer.compare(o2.getScore(), o1.getScore());
    }
}
