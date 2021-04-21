import java.util.Objects;

public class Node {

   private final int _id;
   private boolean _visited;
   private final GroupEnum _group;
   private static int idCounter = 0;

   public Node(GroupEnum group) {
      _id = idCounter++;
      _visited = false;
      _group = group;
   }

   public int getKey() {
      return _id;
   }

   public boolean isVisited() {
      return _visited;
   }

   public void setVisited(boolean v) {
      _visited = v;
   }

   public GroupEnum getGroup() {
      return _group;
   }

   @Override
   public String toString() {
      return String.valueOf(_id);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Node node = (Node) o;
      return _id == node._id && _visited == node._visited && _group == node._group;
   }

   @Override
   public int hashCode() {
      return Objects.hash(_id, _visited, _group);
   }

   public enum GroupEnum {
      GROUP_A,
      GROUP_B
   }
}
