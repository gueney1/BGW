package at.ac.fhcampuswien.fhmdb.api;

import at.ac.fhcampuswien.fhmdb.models.Genre;

public class MovieAPIRequestBuilder {
    private final String baseUrl;
    private String query;
    private Genre genre;
    private String releaseYear;
    private String ratingFrom;

    public MovieAPIRequestBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public MovieAPIRequestBuilder query(String query) {
        this.query = query;
        return this;
    }

    public MovieAPIRequestBuilder genre(Genre genre) {
        this.genre = genre;
        return this;
    }

    public MovieAPIRequestBuilder releaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
        return this;
    }

    public MovieAPIRequestBuilder ratingFrom(String ratingFrom) {
        this.ratingFrom = ratingFrom;
        return this;
    }

    public String build() {
        StringBuilder url = new StringBuilder(baseUrl);

        if ((query != null && !query.isEmpty()) ||
                genre != null || releaseYear != null || ratingFrom != null) {

            url.append("?");

            if (query != null && !query.isEmpty()) {
                url.append("query=").append(query).append("&");
            }
            if (genre != null) {
                url.append("genre=").append(genre).append("&");
            }
            if (releaseYear != null) {
                url.append("releaseYear=").append(releaseYear).append("&");
            }
            if (ratingFrom != null) {
                url.append("ratingFrom=").append(ratingFrom).append("&");
            }

            // Remove the last "&"
            url.setLength(url.length() - 1);
        }

        return url.toString();
    }
}
