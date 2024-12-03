import java.util.Arrays;

public class BTree {
    final int MAX_LEAF_SIZE = 7;    //TODO--figure out the actual size I want the leaves to be
    Node root = null;

    public BTree() {
        root = new Node(null, MAX_LEAF_SIZE);
    }

    public BTree(Key data) {        //TODO      change int data to string data
        root = new Node(null, MAX_LEAF_SIZE);
        insert(data);
//
    }

    public void insert(Key data) {
        Node current = root;
        while (!current.isLeaf()) {
            if (current.isFull()) {
                current.split();
                if (current == root) {
                    root = current.parent;
                }
            }

            if (data.compareTo(current.data[0]) < 0) {
                current = current.child[0];
            } else if (data.compareTo(current.getLast()) > 0) {
                current = current.child[current.indexOf(current.getLast())+1];
            } else {
                for (int i = 0; i < current.data.length; i++) {
                    if (data.compareTo(current.data[i]) > 0 && data.compareTo(current.data[i+1]) < 0) {
                        current = current.child[i+1];   //right child of lesser key
                    }
                }
            }

            break;
        }

        if (current.isFull()) {
            Key median = current.split();
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
}
