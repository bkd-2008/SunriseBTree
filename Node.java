public class Node {
    int[] data;
    //    int hr, min, sec;
    //    boolean isAM;

    //Still figuring out how to structure this
    Node parent;
    Node[] child;

    public Node(Node parent, int leafLength) {
        this.parent = parent;
        this.data = new int[leafLength];
    }

    public boolean isLeaf() {
        return child == null;
    }

    public boolean isFull() {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == 0) {     //nodes cannot hold 0 values
                return false;
            }
        }
        return true;
    }
}
