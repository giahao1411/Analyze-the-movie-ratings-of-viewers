public class Rating {
    private int userID;
    private int movieID;
    private int ratingStar;
    private long timeStampOfTheMovie;

    // constructor with full parameters
    public Rating(int userID, int movieID, int ratingStar, long timeStampOfTheMovie) {
        this.userID = userID;
        this.movieID = movieID;
        this.ratingStar = ratingStar;
        this.timeStampOfTheMovie = timeStampOfTheMovie;
    }

    // getter and setter of each parameter
    public int getuserID() {
        return userID;
    }

    public void setuserID(int userID) {
        this.userID = userID;
    }

    public int getmovieID() {
        return movieID;
    }

    public void setmovieID(int movieID) {
        this.movieID = movieID;
    }

    public int getRatingStar() {
        return ratingStar;
    }

    public void setRatingStar(int ratingStar) {
        this.ratingStar = ratingStar;
    }

    public long getTimeStampOfTheMovie() {
        return timeStampOfTheMovie;
    }

    public void setTimeStampOfTheMovie(long timeStampOfTheMovie) {
        this.timeStampOfTheMovie = timeStampOfTheMovie;
    }

    @Override
    public String toString() {
        return String.format("Rating[%d, %d, %d, %d]", userID, movieID, ratingStar, timeStampOfTheMovie);
    }
}
