public class BTree {
    final int DEGREE = 4;
    final int MAX_LEAF_SIZE = (2 * DEGREE) - 1;    //TODO--figure out the actual size I want the leaves to be
    final int MIN_LEAF_SIZE = DEGREE-1;
    Node root = null;

    public BTree() {
        root = new Node(null, MAX_LEAF_SIZE);
    }

    public BTree(int data) {        //TODO      change int data to string data
        root = new Node(null, MAX_LEAF_SIZE);
        insert(data);
//
    }

    public Node search(int data) {
        Node current = root;
        while (current != null) {
            if (current.contains(data)) {
                return current;
            }

            if (data < current.keys[0]) {       //checks if in leftmost child first
                current = current.child[0];
            }

            for (int i = 0; i <= current.indexOf(current.getLast()); i++) {
                if (data < current.keys[i+1] || current.keys[i+1] == 0) {         //then checks if in right children
                    current = current.child[i+1];
                    break;
                }
            }
        }

        return null;
    }

    public void insert(int data) {
        Node current = root;
        while (!current.isLeaf()) {
            if (current.isFull()) {
                current.split();
                if (current == root) {
                    root = current.parent;
                }
            }

            if (data < current.keys[0]) {
                current = current.child[0];
            } else if (data > current.getLast()) {
                current = current.child[current.indexOf(current.getLast())+1];
            } else {
                for (int i = 0; i < current.keys.length; i++) {
                    if (data > current.keys[i] && data < current.keys[i+1]) {
                        current = current.child[i+1];   //right child of lesser key
                    }
                }
            }
//            break;
        }

        if (current.isFull()) {
            int median = current.split();
            if (current == root) {
                root = current.parent;
            }
            Node parent = current.parent;
            if (data < median) {
                current = parent.child[parent.indexOf(median)];
                current.insert(data);
            } else {
                current = parent.child[parent.indexOf(median)+1];
                current.insert(data);
            }
        } else {
            current.insert(data);
        }
    }

    @Override
    public String toString() {
        //TODO--modify to recursively call Node toString
        String ret = "";


        return root.toString();
    }
}
