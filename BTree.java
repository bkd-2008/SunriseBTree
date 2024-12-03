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
            //TODO
            break;
        }

        if (current.isFull()) {
            //TODO
        } else {
            for (int i = 0; i < current.data.length; i++) {
                if (data > current.data[i] && current.data[i] != 0) {
                    continue;
                }
                for (int j = current.data.length-1; j > i; j--) {
                    current.data[j] = current.data[j-1];
                }
                current.data[i] = data;
                break;
            }
        }
    }
}
