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
     * @return False if length of node is less than length of key array, true otherwise.
     */
    public boolean isFull() {
        return (getLength() == keys.length);
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
        parent.child[parent.indexOfKey(median)] = new Node(parent, keys.length);
        parent.child[parent.indexOfKey(median)+1] = new Node(parent, keys.length);
        Node leftSplit = parent.child[parent.indexOfKey(median)];
        Node rightSplit = parent.child[parent.indexOfKey(median)+1];

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

    public void delete(int delKey) {
        final int MIN_LENGTH = (keys.length-1)/2;
        //((x+1) /2) -1) = ((x+1)-2)/2 = (x-1)/2
        if ((this.isLeaf() && this.getLength() > MIN_LENGTH) || (this.parent == null && this.isLeaf())) {       //second condition covers edge case where root is leaf
            int delIndex = indexOfKey(delKey);
            keys[delIndex] = 0;
            for (int i = delIndex; i < indexOfKey(getLast()); i++) {
                keys[i] = keys[i+1];
            }
            keys[indexOfKey(getLast())+1] = 0;     //needs to be +1 since getLast now returns one index too early
        } else if (this.isLeaf()) {     //ie, deletion would make the node too small
            int locInParent = parent.indexOfChild(this);
            int leftParentKey = locInParent-1;
            int rightParentKey = locInParent;

            Node leftSibling = locInParent == 0 ? null : parent.child[parent.indexOfChild(this) - 1];
            Node rightSibling = locInParent == parent.indexOfKey(parent.getLast()) ? null : parent.child[parent.indexOfChild(this) + 1];

            //TODO--handle cases where predecessor/successor don't come from immediate siblings, ie, left/right sibling is null
            if ((leftSibling != null) && (leftSibling.getLength() > MIN_LENGTH)) {      //predecessor case
                int predecessor = leftSibling.getLast();
                this.insert(parent.keys[leftParentKey]);
                parent.keys[leftParentKey] = predecessor;
                leftSibling.delete(predecessor);
                delete(delKey);
            } else if ((rightSibling != null) && (rightSibling.getLength() > MIN_LENGTH)) {     //successor case
                int successor = rightSibling.keys[0];
                this.insert(parent.keys[rightParentKey]);
                parent.keys[rightParentKey] = successor;
                rightSibling.delete(successor);
                delete(delKey);
            } else if (parent.getLength() > MIN_LENGTH) {       //merge with sibling case
                int medianIndex = leftSibling == null ? rightParentKey : leftParentKey;
                int newMedian = parent.keys[medianIndex];
                Node mergingSibling = leftSibling == null ? rightSibling : leftSibling;
                insert(newMedian);
                for (int i = 0; i < mergingSibling.getLength(); i++) {      //getLength is never null b/c all leaves are at the same level
                    insert(mergingSibling.keys[i]);
                }

                parent.child[parent.indexOfChild(mergingSibling)] = parent.child[parent.indexOfChild(this)];
                for (int i = medianIndex; i < parent.indexOfKey(parent.getLast()); i++) {
                    parent.keys[i] = parent.keys[i+1];
                    parent.child[i+1] = parent.child[i+2];
                }
                parent.child[parent.getLength()] = null;
                parent.keys[parent.getLength()-1] = 0;

                delete(delKey);
            } else {        //merge with parent case
                //TODO
            }
        }
    }

    /**
     * Finds the first index in this node of a given key.
     *
     * @param n The key whose index is being sought.
     * @return The index of the first instance in this node of n, or -1 if n is not in this node.
     */
    public int indexOfKey(int n) {
        for (int i = 0; i < keys.length; i++) {
            if (n == keys[i]) {
                return i;
            }
        }
        return -1;  //n not in node
    }

    /**
     * Finds where in this node a given node is stored.
     *
     * @param n The node being queried.
     * @return The index in this node's child array that contains n, -1 if n is not found.
     */
    public int indexOfChild(Node n) {
        for (int i = 0; i < this.child.length; i++) {
            if (this.child[i] == n) {
                return i;
            }
        }
        return -1;
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

    /**
     * Finds the length of this node.
     *
     * @return The number of non-zero values stored in the keys array
     */
    public int getLength() {
        int length = 0;
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] != 0) {
                length++;
            } else {
                break;
            }
        }

        return length;
    }

    /**
     * Method to get the depth of this node in the larger tree.
     *
     * @return A count of the nodes between this and root, inclusive.
     */
    public int getDepth() {
        int depth = 1;
        Node current = this;
        while (current.parent != null) {
            current = current.parent;
            depth++;
        }
        return depth;
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
