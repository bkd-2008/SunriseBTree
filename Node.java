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

    public boolean isLeaf() {
        return child[0] == null;
    }

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
        leftSplit.child[medianIndex] = this.child[medianIndex];   //should copy edge child of LS?
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
        rightSplit.child[index] = this.child[keys.length];        //should copy edge child of RS?
        if (rightSplit.child[index] != null) {
            rightSplit.child[index].parent = rightSplit;
        }
        return median;      //to compare inserted value with in BTree
    } 

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

    public int indexOf(int n) {
        for (int i = 0; i < keys.length; i++) {
            if (n == keys[i]) {
                return i;
            }
        }
        return -1;  //n not in node
    }

    public boolean contains(int n) {
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] == n) {
                return true;
            }
        }
        return false;
    }

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
//        for (int i = 0; i < data.length; i++) {
//            if (child[i] != null) {
//                ret += child[i].toString();         // + ", ";
//            }
//            if (data[i] != 0) {
//                ret += data[i] + ", ";
//            }
//        }
//        if (child[data.length] != null) {
//            ret += child[data.length];
//        }
        return ret;
    }
}
