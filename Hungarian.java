package algo2.The_Hungarian_Method;

import javax.swing.*;
import java.util.*;

public class Hungarian {

   private final BipartiteGraph _graph;
   private final ArrayList<Edge> _M;
   private Set<Integer> _Am;
   private Set<Integer> _Bm;
   private final GraphPanel _gp;

   public Hungarian(BipartiteGraph graph, GraphPanel gp) {
      _graph = graph;
      _gp = gp;
      _M = new ArrayList<>();
      _Am = graph.getGroupA();
      _Bm = graph.getGroupB();
   }

   public ArrayList<Edge> getM() {
      return _M;
   }

   public void theHungarianMethod() {
      new SwingWorker() {
         @Override
         protected Object doInBackground() throws Exception {
            ArrayList<Edge> newMatch = mAugmentingPath();
            while (newMatch != null) {
               _gp.repaint();
               Thread.sleep(2000);
               addMatchToM(newMatch);
               _Am = setAm();
               _Bm = setBm();
               newMatch = mAugmentingPath();
            }
            System.out.println(_M);
            _gp.repaint();
            System.out.println("end of algorithm");
            return null;
         }
      }.execute();
   }

   private Set<Integer> setAm() {
      Set<Integer> Am = new HashSet<>();
      for (Integer node : _graph.getGroupA()) {
         if (!isSaturated(node)) {
            Am.add(node);
         }
      }
      return Am;
   }

   private Set<Integer> setBm() {
      Set<Integer> Bm = new HashSet<>();
      for (Integer node : _graph.getGroupB()) {
         if (!isSaturated(node)) {
            Bm.add(node);
         }
      }
      return Bm;
   }

   private boolean isSaturated(int node) {
      for (Edge edge : _M) {
         if (node == edge.getNode1().getKey() || node == edge.getNode2().getKey()) return true;
      }
      return false;
   }


   private void addMatchToM(ArrayList<Edge> newMatch) {
      for (Edge edge : newMatch) {
         if (_M.contains(edge)) {
            _M.remove(edge);
         } else {
            _M.add(edge);
         }
      }
   }

   private ArrayList<Edge> mAugmentingPath() {
      BipartiteDiGraph diGraph = toDiGraph();
      for (Integer src : _Am) {
         for (Integer dest : _Bm) {
            ArrayList<Edge> path = BFS(diGraph, _graph.getNode(src), _graph.getNode(dest));
            if (path != null) {
               return path;
            }
         }
      }
      return null;
   }

   private BipartiteDiGraph toDiGraph() {
      BipartiteDiGraph diGraph = new BipartiteDiGraph();
      for (Node node : _graph.getV()) {
         diGraph.addNode(node);
      }
      for (Node node : _graph.getV()) {
         for (Edge edge : _graph.getE(node.getKey())) {
            if (_M.contains(edge)) {
               diGraph.connect(edge.getNode2().getKey(), edge.getNode1().getKey());
            } else {
               diGraph.connect(edge.getNode1().getKey(), edge.getNode2().getKey());
            }
         }
      }
      return diGraph;
   }

   private ArrayList<Edge> BFS(BipartiteDiGraph diGraph, Node src, Node dest) {
      resetVisited(diGraph);
      HashMap<Node, Node> path = new HashMap<>();
//      temp queue
      Queue<Node> queue = new LinkedList<>();
      queue.add(src);

//      set src to visited
      src.setVisited(true);

//      looping while there is nodes in the queue
      while (!queue.isEmpty()) {
         Node node = queue.remove();
         if (node.equals(dest))
            break;
         for (Edge edge : diGraph.getE(node.getKey())) {
            if (!edge.getNode2().isVisited()) {
               /*
                checking if we visited this neighbor
                if not we adding him to the queue and setting to visited
                */
               queue.add(edge.getNode2());
               edge.getNode2().setVisited(true);
               path.put(edge.getNode2(), edge.getNode1());
            }
         }
      }
      return shortestPath(path, src, dest);
   }

   private ArrayList<Edge> shortestPath(HashMap<Node, Node> _path, Node src, Node dest) {
//      checking that the dest is connected to src
      if (_path.get(dest) == null) return null;
//      setting n to the parent of dest

      Node n = _path.get(dest);
      ArrayList<Edge> shortestPath = new ArrayList<>();

//      adding the edge to the set
      Edge e = new Edge(n, dest);
      shortestPath.add(e);

//      looping while the parent isn't the src
      while (!n.equals(src)) {
         e = new Edge(_path.get(n), n);
         shortestPath.add(e);
//         setting n to the parent of parent
         n = _path.get(n);
      }
      return shortestPath;
   }

   private void resetVisited(BipartiteDiGraph graph) {
      for (Node node : graph.getV()) {
         node.setVisited(false);
      }
   }
}
