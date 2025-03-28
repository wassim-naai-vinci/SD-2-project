import java.util.Map;
import java.util.Objects;

public class Artist {

    private final int id;
    private final String name;
    private final String category;
    private final Map<Artist,Double> mentions;

    public Artist(int id, String name, String category, Map<Artist, Double> mentions) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.mentions = mentions;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public Map<Artist, Double> getMentions() {
        return mentions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return id == artist.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", mentions=" + mentions +
                '}';
    }
}
