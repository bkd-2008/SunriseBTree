public class Node {
    int[] keys;
    //    int hr, min, sec;
    //    boolean isAM;

    //Still figuring out how to structure this
    Node parent;
    Node[] child;

    public Node(Node parent, int leafLength) {
        this.parent = parent;
        this.keys = new int[leafLength];
        this.child = new Node[leafLength+1];
    }

    /**
     * Method to check if this node is a leaf or not.
     *
     * @return True if the first element in the child array is null, false otherwise.
     */
    public boolean isLeaf() {
        return child[0] == null;
    }

    /**
     * Method to check if this node's key array is full.
     * Key array is full if all ints are non-zero values.
     *
     * @return False if any element in the keys array is zero, true otherwise.
     */
    public boolean isFull() {
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] == 0) {     //nodes cannot hold 0 values
                return false;
            }
        }
        return true;
    }
    
    public int split() {
        int medianIndex = keys.length / 2;
        if (keys.length % 2 == 0) {
            medianIndex--;       //ensures actual median in case of odd data length
        }
        int median = keys[medianIndex];

        if (parent == null) {
            parent = new Node(null, keys.length);
        }

        parent.insert(median);


        //TODO--replace with arrays.copyofrange
        parent.child[parent.indexOf(median)] = new Node(parent, keys.length);
        parent.child[parent.indexOf(median)+1] = new Node(parent, keys.length);
        Node leftSplit = parent.child[parent.indexOf(median)];
        Node rightSplit = parent.child[parent.indexOf(median)+1];

        for (int i = 0; i < medianIndex; i++) {
            leftSplit.keys[i] = keys[i];
            leftSplit.child[i] = this.child[i];
            if (leftSplit.child[i] != null) {
                leftSplit.child[i].parent = leftSplit;
            }
        }
        leftSplit.child[medianIndex] = this.child[medianIndex];   //copies edge child of LS
        if (leftSplit.child[medianIndex] != null) {
            leftSplit.child[medianIndex].parent = leftSplit;
        }

        int index = 0;  //to be able to track rightsplit index while copying from this' index
        for (int i = medianIndex+1; i < keys.length; i++) {
            rightSplit.keys[index] = keys[i];
            rightSplit.child[index] = this.child[i];
            if (rightSplit.child[index] != null) {
                rightSplit.child[index].parent = rightSplit;
            }
            index++;
        }
        rightSplit.child[index] = this.child[keys.length];        //copies edge child of RS
        if (rightSplit.child[index] != null) {
            rightSplit.child[index].parent = rightSplit;
        }
        return median;      //to compare inserted value with in BTree
    }

    /**
     * Inserts a new key into the keys array. Moves all greater
     * keys one index right, then inserts the new key.
     *
     * @param data The new key to be inserted.
     */
    public void insert(int data) {
        for (int i = 0; i < this.keys.length; i++) {
            if (data > this.keys[i] && this.keys[i] != 0) {
                continue;
            }
            for (int j = this.keys.length-1; j > i; j--) {
                this.keys[j] = this.keys[j-1];
            }
            this.keys[i] = data;
            break;
        }
    }

    /**
     * Finds the index in this node of a given key.
     *
     * @param n The key whose index is being sought.
     * @return The index of n, or -1 if n is not in this node.
     */
    public int indexOf(int n) {
        for (int i = 0; i < keys.length; i++) {
            if (n == keys[i]) {
                return i;
            }
        }
        return -1;  //n not in node
    }

    /**
     * Method to tell if this node contains a given key.
     *
     * @param n The key being queried.
     * @return True if an element in the keys array equals n, false otherwise.
     */
    public boolean contains(int n) {
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] == n) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the last non-zero value in this keys array.
     *
     * @return The value stored at the last non-zero entry in the keys array.
     */
    public int getLast() {
        int i = keys.length-1;
        while (i >= 0 && keys[i] == 0) {
            i--;
        }
        if (i < 0) {
            return -1;
        } else {
            return keys[i];
        }
    }

    @Override
    public String toString() {
        String ret = "";

        int i = 0;
        while (i < keys.length && keys[i] != 0) {
            ret += keys[i] + ", ";
            i++;
        }
        return ret;
    }
}
