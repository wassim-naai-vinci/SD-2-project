import java.io.*;
import java.util.*;

public class Graph {
    private Map<String, List<String>> artistsGraph = new HashMap<>();
    private Map<String, List<Integer>> mentionsGraph = new HashMap<>();
    private Map<String, Double> artistCost = new HashMap<>();

    // Constructeur qui charge les fichiers artists.txt et mentions.txt
    public Graph(String artistsFile, String mentionsFile) {
        try {
            // Charger le graphe des artistes
            BufferedReader br = new BufferedReader(new FileReader(artistsFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String artist = parts[1].replaceAll("\"", "").trim();
                String[] categories = parts[2].split(";");
                artistsGraph.computeIfAbsent(artist, k -> new ArrayList<>());
                for (String category : categories) {
                    artistsGraph.get(artist).add(category);
                }
                artistCost.put(artist, Math.random() * 10); // Exemple de coût aléatoire
            }
            br.close();

            // Charger le graphe des mentions
            br = new BufferedReader(new FileReader(mentionsFile));
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String artist = parts[0];
                int mentionId = Integer.parseInt(parts[1]);
                mentionsGraph.computeIfAbsent(artist, k -> new ArrayList<>()).add(mentionId);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Trouver le chemin le plus court avec le coût total et la longueur du chemin
    public void trouverCheminLePlusCourt(String start, String end) {
        Map<String, String> parentMap = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, Double> totalCost = new HashMap<>();
        totalCost.put(start, 0.0);

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(end)) {
                List<String> path = new ArrayList<>();
                double totalPathCost = totalCost.get(end);
                while (parentMap.containsKey(current)) {
                    path.add(current);
                    current = parentMap.get(current);
                }
                path.add(start);
                Collections.reverse(path);

                // Afficher le résultat
                System.out.println("Longueur du chemin : " + path.size());
                System.out.println("Coût total du chemin : " + totalPathCost);
                System.out.println("Chemin :");
                for (String artist : path) {
                    // Vérification si l'artiste existe dans le Map artistCost
                    Double cost = artistCost.get(artist);
                    if (cost == null) {
                        cost = 0.0;  // Attribuer un coût par défaut si l'artiste n'a pas de coût
                    }
                    System.out.println(artist + " (" + String.join(";", artistsGraph.get(artist)) + ")");
                }
                return;
            }

            for (String neighbor : artistsGraph.getOrDefault(current, new ArrayList<>())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parentMap.put(neighbor, current);

                    // Récupérer le coût de l'artiste voisin
                    Double currentCost = artistCost.getOrDefault(current, 0.0);
                    Double neighborCost = artistCost.getOrDefault(neighbor, 0.0);
                    totalCost.put(neighbor, totalCost.get(current) + neighborCost);
                    queue.add(neighbor);
                }
            }
        }

        System.out.println("Aucun chemin trouvé.");
    }


    // Trouver le chemin avec le plus de mentions
    public void trouverCheminMaxMentions(String start, String end) {
        Map<String, String> parentMap = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, Integer> mentionCount = new HashMap<>();
        mentionCount.put(start, 0);

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(end)) {
                List<String> path = new ArrayList<>();
                while (parentMap.containsKey(current)) {
                    path.add(current);
                    current = parentMap.get(current);
                }
                path.add(start);
                Collections.reverse(path);

                // Afficher le résultat
                System.out.println("Longueur du chemin : " + path.size());
                System.out.println("Coût total du chemin : " + mentionCount.get(end) / 10.0); // Exemple de coût basé sur le nombre de mentions
                System.out.println("Chemin :");
                for (String artist : path) {
                    System.out.println(artist + " (" + String.join(";", artistsGraph.get(artist)) + ")");
                }
                return;
            }

            for (String neighbor : artistsGraph.getOrDefault(current, new ArrayList<>())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parentMap.put(neighbor, current);
                    mentionCount.put(neighbor, mentionCount.get(current) + 1);
                    queue.add(neighbor);
                }
            }
        }

        System.out.println("Aucun chemin trouvé.");
    }
}
