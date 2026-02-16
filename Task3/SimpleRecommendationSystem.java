import java.util.*;

class Movie {
    String title;
    List<String> genres;

    public Movie(String title, List<String> genres) {
        this.title = title;
        this.genres = genres;
    }
}

public class SimpleRecommendationSystem {

    private Map<String, Map<String, Double>> userRatings;
    private Map<String, Movie> movies;

    public SimpleRecommendationSystem() {
        userRatings = new HashMap<>();
        movies = new HashMap<>();
    }

    // Add a movie with its genres
    public void addMovie(String title, List<String> genres) {
        movies.put(title, new Movie(title, genres));
    }

    // Add ratings given by a user
    public void addUserRatings(String user, Map<String, Double> ratings) {
        userRatings.put(user, ratings);
    }

    // ===== User-Based Collaborative Filtering =====
    private double computeUserSimilarity(Map<String, Double> ratings1, Map<String, Double> ratings2) {
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (String movie : ratings1.keySet()) {
            if (ratings2.containsKey(movie)) {
                dotProduct += ratings1.get(movie) * ratings2.get(movie);
            }
            norm1 += ratings1.get(movie) * ratings1.get(movie);
        }

        for (double r : ratings2.values()) {
            norm2 += r * r;
        }

        if (norm1 == 0 || norm2 == 0) {
            return 0.0; // No similarity
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    public List<String> recommendUserBased(String user, int topN) {
        Map<String, Double> targetRatings = userRatings.get(user);
        Map<String, Double> weightedScores = new HashMap<>();
        Map<String, Double> similaritySums = new HashMap<>();

        for (String otherUser : userRatings.keySet()) {
            if (otherUser.equals(user)) continue;

            double similarity = computeUserSimilarity(targetRatings, userRatings.get(otherUser));

            for (Map.Entry<String, Double> entry : userRatings.get(otherUser).entrySet()) {
                String movie = entry.getKey();
                double rating = entry.getValue();

                // Only consider movies the target user has not rated
                if (!targetRatings.containsKey(movie)) {
                    weightedScores.put(movie, weightedScores.getOrDefault(movie, 0.0) + similarity * rating);
                    similaritySums.put(movie, similaritySums.getOrDefault(movie, 0.0) + similarity);
                }
            }
        }

        // Normalize scores
        Map<String, Double> finalScores = new HashMap<>();
        for (String movie : weightedScores.keySet()) {
            finalScores.put(movie, weightedScores.get(movie) / similaritySums.get(movie));
        }

        return getTopRecommendations(finalScores, topN);
    }

    // ===== Item-Based Collaborative Filtering =====
    private double computeItemSimilarity(String item1, String item2) {
        List<Double> ratings1 = new ArrayList<>();
        List<Double> ratings2 = new ArrayList<>();

        for (Map<String, Double> ratings : userRatings.values()) {
            if (ratings.containsKey(item1) && ratings.containsKey(item2)) {
                ratings1.add(ratings.get(item1));
                ratings2.add(ratings.get(item2));
            }
        }

        if (ratings1.isEmpty()) return 0.0;

        double dot = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < ratings1.size(); i++) {
            dot += ratings1.get(i) * ratings2.get(i);
            norm1 += ratings1.get(i) * ratings1.get(i);
            norm2 += ratings2.get(i) * ratings2.get(i);
        }

        return dot / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    public List<String> recommendItemBased(String user, int topN) {
        Map<String, Double> targetRatings = userRatings.get(user);
        Map<String, Double> scores = new HashMap<>();
        Map<String, Double> sumSim = new HashMap<>();

        for (String movie : movies.keySet()) {
            if (targetRatings.containsKey(movie)) continue;

            for (String ratedMovie : targetRatings.keySet()) {
                double sim = computeItemSimilarity(movie, ratedMovie);
                scores.put(movie, scores.getOrDefault(movie, 0.0) + sim * targetRatings.get(ratedMovie));
                sumSim.put(movie, sumSim.getOrDefault(movie, 0.0) + sim);
            }
        }

        Map<String, Double> finalScores = new HashMap<>();
        for (String movie : scores.keySet()) {
            if (sumSim.get(movie) != 0) {
                finalScores.put(movie, scores.get(movie) / sumSim.get(movie));
            }
        }

        return getTopRecommendations(finalScores, topN);
    }

    // ===== Content-Based Filtering =====
    private double computeGenreSimilarity(List<String> g1, List<String> g2) {
        Set<String> common = new HashSet<>(g1);
        common.retainAll(g2);
        int unionSize = g1.size() + g2.size() - common.size();
        if (unionSize == 0) return 0.0;
        return common.size() / (double) unionSize;
    }

    public List<String> recommendContentBased(String user, int topN) {
        Map<String, Double> targetRatings = userRatings.get(user);
        Map<String, Double> scores = new HashMap<>();

        for (String movie : movies.keySet()) {
            if (targetRatings.containsKey(movie)) continue;

            double score = 0.0;
            for (String ratedMovie : targetRatings.keySet()) {
                score += computeGenreSimilarity(movies.get(movie).genres, movies.get(ratedMovie).genres)
                        * targetRatings.get(ratedMovie);
            }
            scores.put(movie, score);
        }

        return getTopRecommendations(scores, topN);
    }

    // Helper: sort map by value and pick top N
    private List<String> getTopRecommendations(Map<String, Double> map, int topN) {
        List<Map.Entry<String, Double>> list = new ArrayList<>(map.entrySet());
        list.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        List<String> recommendations = new ArrayList<>();
        for (int i = 0; i < Math.min(topN, list.size()); i++) {
            recommendations.add(list.get(i).getKey());
        }
        return recommendations;
    }

    public static void main(String[] args) {
        SimpleRecommendationSystem system = new SimpleRecommendationSystem();

        // Adding movies
        system.addMovie("Inception", Arrays.asList("Sci-Fi", "Action"));
        system.addMovie("Avatar", Arrays.asList("Sci-Fi", "Adventure"));
        system.addMovie("Titanic", Arrays.asList("Romance", "Drama"));
        system.addMovie("Matrix", Arrays.asList("Sci-Fi", "Action"));
        system.addMovie("Avengers", Arrays.asList("Action", "Adventure"));

        // Adding user ratings
        system.addUserRatings("Alice", Map.of("Inception", 5.0, "Titanic", 3.0));
        system.addUserRatings("Bob", Map.of("Inception", 4.0, "Avatar", 4.0, "Titanic", 5.0));
        system.addUserRatings("Charlie", Map.of("Avengers", 5.0, "Avatar", 3.0, "Matrix", 4.0));

        // Display recommendations
        System.out.println("User-Based for Alice: " + system.recommendUserBased("Alice", 3));
        System.out.println("Item-Based for Alice: " + system.recommendItemBased("Alice", 3));
        System.out.println("Content-Based for Alice: " + system.recommendContentBased("Alice", 3));
    }
}
