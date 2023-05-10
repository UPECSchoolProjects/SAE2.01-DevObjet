import math

class Graph:
    def __init__(self, directed=False):
        self._edges = dict()
        self._isDirected = directed

    def __len__(self):
        return len(self._edges)

    def __iter__(self):
        return iter(self._edges.keys())

    def __getitem__(self, node):
        return self._edges[node]

    def add_node(self, s):
        if s not in self._edges:
            self._edges[s] = []

    def add_edge(self, source, target, weight=None):
        self.add_node(source)
        self.add_node(target)
        self._edges[source].append((target, weight))
        # si le graphe est non orienté
        if not self._isDirected:
            self._edges[target].append((source, weight))

    def __str__(self):
        s = ""
        for (n, out) in self._edges.items():
            s += n.__str__() + " -> " + out.__str__() + "\n"
        return s

    def bellman_ford(self, start, destination):
        dist = dict()
        pred = dict()

        for n in self:
            dist[n] = math.inf
            pred[n] = None
        dist[start] = 0

        for k in range(0, len(self) - 1):
            for u in self._edges:
                for (v, w) in self[u]:
                    if dist[v] > dist[u] + w:
                        dist[v] = dist[u] + w
                        pred[v] = u

        # Vérification de la présence d'un cycle négatif
        for u in self._edges:
            for (v, w) in self[u]:
                if dist[v] > dist[u] + w:
                    return None

        # Retourner le chemin et la distance
        path = [destination]
        node = destination
        while pred[node] is not None:
            path.append(pred[node])
            node = pred[node]
        path.reverse()
        return path, dist[destination]


if __name__ == "__main__":

    G = Graph()
    G.add_edge("s", "a", 10)
    G.add_edge("s", "e", 8)
    G.add_edge("e", "d", 1)
    G.add_edge("d", "a", 4)
    G.add_edge("d", "c", 1)
    G.add_edge("c", "b", 2)
    G.add_edge("b", "a", 1)
    G.add_edge("a", "c", 2)

    print(G)
    print(G.bellman_ford("a", "e"))