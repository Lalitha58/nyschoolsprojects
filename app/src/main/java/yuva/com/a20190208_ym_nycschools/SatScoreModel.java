package yuva.com.a20190208_ym_nycschools;

public class SatScoreModel {
    String reading_avg_score;
    String math_avg_score;
    String writing_avg_score;
    String noSatScoreAvailble;
    public String getNoSatScoreAvailble() {
        return noSatScoreAvailble;
    }

    public void setNoSatScoreAvailble(String noSatScoreAvailble) {
        this.noSatScoreAvailble = noSatScoreAvailble;
    }


    public SatScoreModel(){

    }
    public String getReading_avg_score() {
        return reading_avg_score;
    }

    public void setReading_avg_score(String reading_avg_score) {
        this.reading_avg_score = reading_avg_score;
    }

    public String getMath_avg_score() {
        return math_avg_score;
    }

    public void setMath_avg_score(String math_avg_score) {
        this.math_avg_score = math_avg_score;
    }

    public String getWriting_avg_score() {
        return writing_avg_score;
    }

    public void setWriting_avg_score(String writing_avg_score) {
        this.writing_avg_score = writing_avg_score;
    }

}
