import java.util.*;
import java.io.*;

public class RatingManagement {
    private ArrayList<Rating> ratings;
    private ArrayList<Movie> movies;
    private ArrayList<User> users;
    private BufferedReader read;

    // @Requirement 1
    public RatingManagement(String moviePath, String ratingPath, String userPath) {
        this.movies = loadMovies(moviePath);
        this.users = loadUsers(userPath);
        this.ratings = loadEdgeList(ratingPath);
    }

    private ArrayList<Rating> loadEdgeList(String ratingPath) {
        ratings = new ArrayList<>();
        // try - catch when reading ratings file
        try {
            // initiate the BufferedReader with the path is the userPath given
            read = new BufferedReader(new FileReader(ratingPath));

            // read the fisrt line to skip the header line
            String lineOfData = read.readLine();

            // reading all the line of data in ratingPath
            while ((lineOfData = read.readLine()) != null) {
                String[] ratingData = lineOfData.split(",");

                // add each line of ratingPath to the ratings ArrayList
                ratings.add(new Rating(Integer.parseInt(ratingData[0]), Integer.parseInt(ratingData[1]),
                        Integer.parseInt(ratingData[2]), Long.parseLong(ratingData[3])));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ratings;
    }

    private ArrayList<Movie> loadMovies(String moviePath) {
        movies = new ArrayList<>();
        // try - catch when reading movies file
        try {
            // initiate the BufferedReader with the path is the moviePath given
            read = new BufferedReader(new FileReader(moviePath));

            String lineOfData = read.readLine();

            // reading all the line of data in moviePath
            while ((lineOfData = read.readLine()) != null) {
                String[] moviesData = lineOfData.split(",");

                // initiate and add the the genres ArrayList
                String[] genresArray = moviesData[2].split("-");
                ArrayList<String> genres = new ArrayList<>();
                for (String genre : genresArray) {
                    genres.add(genre);
                }

                // add each line of moviePath to the movies ArrayList
                movies.add(new Movie(Integer.parseInt(moviesData[0]), moviesData[1], genres));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movies;
    }

    private ArrayList<User> loadUsers(String userPath) {
        users = new ArrayList<>();
        // try - catch when reading users file
        try {
            // initiate the BufferedReader with the path is the userPath given
            read = new BufferedReader(new FileReader(userPath));

            String lineOfData = read.readLine();

            while ((lineOfData = read.readLine()) != null) {
                String[] userData = lineOfData.split(",");

                // add each line of userPath to users ArrayList
                users.add(new User(Integer.parseInt(userData[0]), userData[1], Integer.parseInt(userData[2]),
                        userData[3], userData[4]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Rating> getRating() {
        return ratings;
    }

    // @Requirement 2
    public ArrayList<Movie> findMoviesByNameAndMatchRating(int userId, int rating) {
        ArrayList<Movie> moviesRatedByUser = new ArrayList<>();

        // loop in ratings ArrayList to find the userID matched the given one
        for (Rating ratingLine : ratings) {
            // check if matched the userID and greater than or equal the rating
            if ((ratingLine.getuserID() == userId) && (ratingLine.getRatingStar() >= rating)) {
                // find the movie that matched the movie was taken then add it to ArrayList
                for (Movie movie : movies) {
                    if (movie.getId() == ratingLine.getmovieID()) {
                        moviesRatedByUser.add(movie);
                        break;
                    }
                }
            }
        }

        // sort the movies in alphabetical order with anonymous inner class
        Collections.sort(moviesRatedByUser, new Comparator<Movie>() {
            public int compare(Movie movie1, Movie movie2) {
                return movie1.getName().compareTo(movie2.getName());
            }
        });
        return moviesRatedByUser;
    }

    // Requirement 3
    public ArrayList<User> findUsersHavingSameRatingWithUser(int userId, int movieId) {
        ArrayList<User> usersRatedTheSame = new ArrayList<>();
        int ratingStar = getUserRatingStar(userId, movieId);

        // find the rating that matched movieId and rated the same star
        for (Rating rating : ratings) {
            // check ignore the given userId
            if (rating.getmovieID() == movieId && rating.getRatingStar() == ratingStar
                    && rating.getuserID() != userId) {
                // get the userID index
                int userIdx = rating.getuserID() - 1;
                // check if valid then add to the usersRatedTheSame ArrayList
                if (userIdx >= 0 && userIdx < users.size()) {
                    usersRatedTheSame.add(users.get(userIdx));
                }
            }
        }
        return usersRatedTheSame;
    }

    // get ratingStar of the userID rated the movieID
    private int getUserRatingStar(int userID, int movieID) {
        int ratingStar = 0;
        for (Rating rating : ratings) {
            if (rating.getuserID() == userID && rating.getmovieID() == movieID) {
                ratingStar = rating.getRatingStar();
                break;
            }
        }
        return ratingStar;
    }

    // Requirement 4
    public ArrayList<String> findMoviesNameHavingSameReputation() {
        ArrayList<String> moviesName = new ArrayList<>();

        // HashMap with key: movieID - value: NumOfRating
        HashMap<Integer, Integer> movieRatingCount = new HashMap<>();

        // add to movieRatingCount if the ratingStar is greater than 3
        for (Rating rating : ratings) {
            if (rating.getRatingStar() > 3) {
                int movieID = rating.getmovieID();
                movieRatingCount.put(movieID, movieRatingCount.getOrDefault(movieID, 0) + 1);
            }
        }

        // add to moviesName if numOfRating(count) greater than or equal to 2
        for (Movie movie : movies) {
            int numOfRating = movieRatingCount.getOrDefault(movie.getId(), 0);
            if (numOfRating >= 2) {
                moviesName.add(movie.getName());
            }
        }

        // sort the movies in alphabetical order
        Collections.sort(moviesName);
        return moviesName;
    }

    // @Requirement 5
    public ArrayList<String> findMoviesMatchOccupationAndGender(String occupation, String gender, int k, int rating) {
        // store all the movies matched the requirement
        HashSet<String> moviesNameMatchedReq = new HashSet<>();

        // adding to the moviesNameMatchedReq
        for (User user : users) {
            // find matched occupation and gender
            if (user.getOccupation().equals(occupation) && (user.getGender().equals(gender))) {
                for (Rating ratingLine : ratings) {
                    // find matched rating
                    if ((ratingLine.getuserID() == user.getId()) && (ratingLine.getRatingStar() == rating)) {
                        if (isMatchedMovieID(ratingLine.getmovieID())) {
                            moviesNameMatchedReq.add(getMoviesName(ratingLine.getmovieID()));
                            break;
                        }
                    }
                }
            }
        }

        return addToArrayList(moviesNameMatchedReq, k);
    }

    private boolean isMatchedMovieID(int movieID) {
        for (Movie movie : movies) {
            if (movie.getId() == movieID)
                return true;
        }
        return false;
    }

    // get HashSet then add k moviesName to ArrayList for Req5, Req6 and Req7
    private ArrayList<String> addToArrayList(HashSet<String> movies, int k) {
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>(movies);

        // sort by alphabetical order
        Collections.sort(temp);

        int countMovies = 0;
        for (String movieName : temp) {
            result.add(movieName);
            countMovies++;
            if (countMovies == k)
                break;
        }
        return result;
    }

    // @Requirement 6
    public ArrayList<String> findMoviesByOccupationAndLessThanRating(String occupation, int k, int rating) {
        HashSet<String> moviesNameMatchedReq = new HashSet<>();

        // adding the movieName to moviesNameMatchedReq
        for (User user : users) {
            // find matched occupation
            if (user.getOccupation().equals(occupation)) {
                for (Rating ratingLine : ratings) {
                    // find movie's rating less than the given rating
                    if ((ratingLine.getuserID() == user.getId()) && (ratingLine.getRatingStar() < rating)) {
                        if (isMatchedMovieID(ratingLine.getmovieID())) {
                            moviesNameMatchedReq.add(getMoviesName(ratingLine.getmovieID()));
                            break;
                        }
                    }
                }
            }
        }

        return addToArrayList(moviesNameMatchedReq, k);
    }

    // @Requirement 7
    public ArrayList<String> findMoviesMatchLatestMovieOf(int userId, int rating, int k) {
        HashSet<String> moviesNameMatchedReq = new HashSet<>();
        ArrayList<String> genresOfTheMovie = getLastestGenres(userId);
        User reqUser = getUser(userId);

        // add to moviesNameMatchedReq is matched the requirements
        for (Rating ratingLine : ratings) {
            // find the movie that greater than or equal to rating
            if (ratingLine.getRatingStar() >= rating) {
                User user = getUser(ratingLine.getuserID());
                // find the user that have the same gender as the given one
                if ((user != null) && (user.getGender().equals(reqUser.getGender()))) {
                    ArrayList<String> checkGenres = getGenre(ratingLine.getmovieID());
                    if (isContain(genresOfTheMovie, checkGenres)) {
                        moviesNameMatchedReq.add(getMoviesName(ratingLine.getmovieID()));
                    }
                }
            }
        }

        return addToArrayList(moviesNameMatchedReq, k);
    }

    // check if checkGenres having one of reqGenres or not
    private boolean isContain(ArrayList<String> reqGenres, ArrayList<String> checkGenres) {
        HashSet<String> reqGenresHS = new HashSet<>(reqGenres);
        HashSet<String> checkGenresHS = new HashSet<>(checkGenres);
        reqGenresHS.retainAll(checkGenresHS);
        // if not having at least one, the reqGenresHS will empty
        return !reqGenresHS.isEmpty();
    }

    private String getMoviesName(int movieID) {
        for (Movie movie : movies) {
            if (movie.getId() == movieID)
                return movie.getName();
        }
        return "";
    }

    private ArrayList<String> getGenre(int movieID) {
        for (Movie movie : movies) {
            if (movie.getId() == movieID)
                return movie.getGenres();
        }
        return new ArrayList<>();
    }

    private User getUser(int userID) {
        if (userID >= 1 && userID <= users.size()) {
            return users.get(userID - 1);
        }
        return null;
    }

    private ArrayList<String> getLastestGenres(int userID) {
        long timestampMax = Long.MIN_VALUE;
        int movieID = -1;

        // loop to find the movieID of the lasted rating(timestamp max)
        for (Rating rating : ratings) {
            if ((rating.getuserID() == userID) && (rating.getTimeStampOfTheMovie() > timestampMax)) {
                timestampMax = rating.getTimeStampOfTheMovie();
                movieID = rating.getmovieID();
            }
        }

        return getGenre(movieID);
    }
}