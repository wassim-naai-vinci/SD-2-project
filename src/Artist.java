import java.util.Map;
import java.util.Objects;

public class Artist {

    private  int id;
    private  String name;
    private  String category;
    private  Map<Artist,Double> mentions;

    //Wassim c'est le constructeur
    public Artist(int id, String name, String category, Map<Artist, Double> mentions) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.mentions = mentions;
    }

    //Tous les getters
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

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return id == artist.id;
    }

    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        return "Artist{" + "id=" + id + ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", mentions=" + mentions +
                '}';
    }
}
