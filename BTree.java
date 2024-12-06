public class BTree {
    final int DEGREE = 4;       //TODO--figure out the actual size I want the leaves to be
    final int MAX_LEAF_SIZE = (2 * DEGREE) - 1;
    final int MIN_LEAF_SIZE = DEGREE-1;
    Node root = null;

    public BTree() {
        root = new Node(null, MAX_LEAF_SIZE);
    }

    public BTree(Node n) {
        root = n;
        root.parent = null;     //TODO--keep this from affecting n's actual parent
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

            for (int i = 0; i <= current.indexOfKey(current.getLast()); i++) {
                if (data < current.keys[i+1] || current.keys[i+1] == 0) {         //then checks if in right children
                    current = current.child[i+1];
                    break;
                }
            }
        }

        return null;
    }

    public void insert(int key) {
        Node current = root;
        while (!current.isLeaf()) {
            if (current.isFull()) {
                current.split();
                if (current == root) {
                    root = current.parent;
                }
            }

            if (key < current.keys[0]) {
                current = current.child[0];
            } else if (key > current.getLast()) {
                current = current.child[current.indexOfKey(current.getLast())+1];
            } else {
                for (int i = 0; i < current.keys.length; i++) {
                    if (key > current.keys[i] && key < current.keys[i+1]) {
                        current = current.child[i+1];   //right child of lesser key
                    }
                }
            }
        }

        if (current.isFull()) {
            int median = current.split();
            if (current == root) {
                root = current.parent;
            }
            Node parent = current.parent;
            if (key < median) {
                current = parent.child[parent.indexOfKey(median)];
                current.insert(key);
            } else {
                current = parent.child[parent.indexOfKey(median)+1];
                current.insert(key);
            }
        } else {
            current.insert(key);
        }
    }

    public void delete(int key) {
        Node delNode = search(key);

        delNode.delete(key);
    }

    @Override
    public String toString() {
        //TODO--modify to recursively call Node toString
        String ret = "";


        return root.toString();
    }
}
