public class BTree {
    final int MAX_LEAF_SIZE = 7;    //TODO--figure out the actual size I want the leaves to be
    Node root = null;

    public BTree() {
        root = new Node(null, MAX_LEAF_SIZE);
    }

    public BTree(int data) {        //TODO      change int data to string data
        root = new Node(null, MAX_LEAF_SIZE);
        insert(data);
//
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

            if (data < current.data[0]) {
                current = current.child[0];
            } else if (data > current.getLast()) {
                current = current.child[current.indexOf(current.getLast())+1];
            } else {
                for (int i = 0; i < current.data.length; i++) {
                    if (data > current.data[i] && data < current.data[i+1]) {
                        current = current.child[i+1];   //right child of lesser key
                    }
                }
            }
            break;
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
        return root.toString();
    }
}
