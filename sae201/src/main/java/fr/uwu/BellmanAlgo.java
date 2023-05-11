package fr.uwu;
public class BellmanAlgo
{
 
// La fonction principale qui trouve les distances les plus courtes
// de la source à tous les autres sommets
// en utilisant l'algorithme de Bellman-Ford. La fonction
// détecte également les cycles de poids négatifs
// La ligne graph[i] représente le i-ème arc avec
// trois valeurs u, v et w.
static void BellmanFord(int graph[][], int V, int E,
                int src)
{
    // Initialise la distance de tous les sommets à l'infini.
    int []dis = new int[V];
    for (int i = 0; i < V; i++)
        dis[i] = Integer.MAX_VALUE;
 
    // initialise la distance de la source à 0
    dis[src] = 0;
 
    // Relaxe toutes les arêtes |V| - 1 fois. Un chemin le plus court
    // simple de la source à n'importe quel autre
    // sommet peut avoir au plus |V| - 1 arêtes
    for (int i = 0; i < V - 1; i++)
    {
 
        for (int j = 0; j < E; j++)
        {
            if (dis[graph[j][0]] != Integer.MAX_VALUE && dis[graph[j][0]] + graph[j][2] <
                            dis[graph[j][1]])
                dis[graph[j][1]] =
                dis[graph[j][0]] + graph[j][2];
        }
    }
 
    // Vérifie les cycles de poids négatifs.
    // L'étape ci-dessus garantit les distances les plus courtes si le graphe ne contient pas
    // de cycle de poids négatif. Si on obtient un
    // chemin plus court, alors il y a un cycle.
    for (int i = 0; i < E; i++)
    {
        int x = graph[i][0];
        int y = graph[i][1];
        int weight = graph[i][2];
        if (dis[x] != Integer.MAX_VALUE &&
                dis[x] + weight < dis[y])
            System.out.println("Le graphe contient "
                    +" un cycle de poids négatif");
    }
 
    System.out.println("Distance du sommet depuis la source");
    for (int i = 0; i < V; i++)
        System.out.println(i + "\t\t" + dis[i]);
}
 
// Exemple de programme d'execution de l'algo:
public static void main(String[] args)
{
    int V = 5; // Nombre de sommets dans le graphe
    int E = 8; // Nombre d'arêtes dans le graphe
 
    // Chaque arête a trois valeurs (u, v, w) où
    // l'arête est du sommet u au sommet v. Et le poids
    // de l'arête est w.
    int graph[][] = { { 0, 1, -1 }, { 0, 2, 4 },
                    { 1, 2, 3 }, { 1, 3, 2 },
                    { 1, 4, 2 }, { 3, 2, 5 },
                    { 3, 1, 1 }, { 4, 3, -3 } };
 
    BellmanFord(graph, V, E, 0);
    }
}