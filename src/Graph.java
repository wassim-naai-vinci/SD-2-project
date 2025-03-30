import java.io.*;
import java.util.*;

public class Graph {
    private Map<Integer, Artist> artistById = new HashMap<>();

    private Map<String, Artist> artistsByName = new HashMap<>();

    public Graph(String artistsFile, String mentionsFile) {
        loadArtists(artistsFile);
        loadMentions(mentionsFile);
    }


    private void loadArtists(String filename) {
        try (BufferedReader bufferedReader
                     = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] partss = line.split(",", 3);
                if (partss.length < 3) continue;

                int id = Integer.parseInt(partss[0]);
                String name = partss[1].replace("\"", "").trim();
                String category = partss[2].split(";")[0]; // Prend la première catégorie

                Artist artist = new Artist(id, name, category, new HashMap<>());
                artistById.put(id, artist);
                artistsByName.put(name, artist);
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecteur de : " + filename);
            e.printStackTrace();
        }
    }

    private void loadMentions(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 3) continue;

                int sourceId = Integer.parseInt(parts[0]);
                int targetId = Integer.parseInt(parts[1]);
                int mentionCount = Integer.parseInt(parts[2]);
                double weight = 1.0 / mentionCount;

                Artist source = artistById.get(sourceId);
                Artist target = artistById.get(targetId);

                if (source != null && target != null) {
                    source.getMentions().put(target, weight);
                }

            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture de  " + filename + ": " + e);
            e.printStackTrace();
        }
    }

    public void trouverCheminLePlusCourt(String startName, String endName) {
        Artist start = artistsByName.get(startName);
        Artist end = artistsByName.get(endName);

        if (start == null || end == null) {
            throw new RuntimeException("Aucun chemin entre " + startName + " et " + endName);
        }

        Map<Artist, Artist> predecessors = new HashMap<>();
        Map<Artist, Integer> distances = new HashMap<>();

        Queue<Artist> queueArtist = new LinkedList<>();

        queueArtist.add(start);
        distances.put(start, 0);

        while (!queueArtist.isEmpty()) {
            Artist current = queueArtist.poll();

            if (current.equals(end)) {
                printOutpout(start, end, predecessors, distances.get(end), false);
                return;
            }

            for (Artist neighbor : current.getMentions().keySet()) {
                if (!distances.containsKey(neighbor)) {
                    distances.put(neighbor, distances.get(current) + 1);
                    predecessors.put(neighbor, current);
                    queueArtist.add(neighbor);
                }
            }
        }

        throw new RuntimeException("Aucun chemin entre " + startName + " et " + endName);
    }

    public void trouverCheminMaxMentions(String startName, String endName) {
        Artist start = artistsByName.get(startName);

        Artist end = artistsByName.get(endName);

        if (start == null || end == null) {
            throw new RuntimeException("Aucun chemin entre " + startName + " et " + endName);
        }

        Map<Artist, Artist> predecessors = new HashMap<>();
        Map<Artist, Double> totalWeights = new HashMap<>();
        PriorityQueue<Artist> queue = new PriorityQueue<>(
                Comparator.comparingDouble(a -> totalWeights.getOrDefault(a, Double.POSITIVE_INFINITY))
        );

        queue.add(start);
        totalWeights.put(start, 0.0);

        while (!queue.isEmpty()) {
            Artist current = queue.poll();

            if (current.equals(end)) {
                printOutpout(start, end, predecessors, totalWeights.get(end), true);
                return;
            }

            for (Map.Entry<Artist, Double> entry : current.getMentions().entrySet()) {
                Artist neighbor = entry.getKey();
                double newWeight = totalWeights.get(current) + entry.getValue();

                if (newWeight < totalWeights.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                    totalWeights.put(neighbor, newWeight);
                    predecessors.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        throw new RuntimeException("Aucun chemin entre " + startName + " et " + endName);
    }

    private void printOutpout(Artist start, Artist end,
                              Map<Artist, Artist> predecessors,
                              double totalCost,
                              boolean weighted) {
        List<Artist> path = new ArrayList<>();
        Artist current = end;

        while (!current.equals(start)) {
            path.add(current);
            current = predecessors.get(current);
        }
        path.add(start);
        Collections.reverse(path);

        System.out.println("Longueur du chemin : " + (path.size() - 1));
        System.out.printf("Coût total du chemin : %.6f\n", totalCost);
        System.out.println("Chemin :");
        for (Artist artist : path) {
            System.out.println(artist.getName() + " (" + artist.getCategory() + ")");
        }
        System.out.println("-------------------------");
    }
}