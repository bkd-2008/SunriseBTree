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
        this.child = new Node[leafLength+1];
    }

    public boolean isLeaf() {
        return child[0] == null;
    }

    public boolean isFull() {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == 0) {     //nodes cannot hold 0 values
                return false;
            }
        }
        return true;
    }
    
    public int split() {
        int medianIndex = data.length / 2;
        if (data.length % 2 == 0) {
            medianIndex--;       //ensures actual median in case of odd data length
        }
        int median = data[medianIndex];

        if (parent == null) {
            parent = new Node(null, data.length);
        }

        parent.insert(median);


        //TODO--replace with arrays.copyofrange
        parent.child[parent.indexOf(median)] = new Node(parent, data.length);
        parent.child[parent.indexOf(median)+1] = new Node(parent, data.length);
        Node leftSplit = parent.child[parent.indexOf(median)];
        Node rightSplit = parent.child[parent.indexOf(median)+1];

        for (int i = 0; i < medianIndex; i++) {
            leftSplit.data[i] = data[i];
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
        for (int i = medianIndex+1; i < data.length; i++) {
            rightSplit.data[index] = data[i];
            rightSplit.child[index] = this.child[i];
            if (rightSplit.child[index] != null) {
                rightSplit.child[index].parent = rightSplit;
            }
            index++;
        }
        rightSplit.child[index] = this.child[data.length];        //should copy edge child of RS?
        if (rightSplit.child[index] != null) {
            rightSplit.child[index].parent = rightSplit;
        }
        return median;      //to compare inserted value with in BTree
    } 

    public void insert(int data) {
        for (int i = 0; i < this.data.length; i++) {
            if (data > this.data[i] && this.data[i] != 0) {
                continue;
            }
            for (int j = this.data.length-1; j > i; j--) {
                this.data[j] = this.data[j-1];
            }
            this.data[i] = data;
            break;
        }
    }

    public int indexOf(int n) {
        for (int i = 0; i < data.length; i++) {
            if (n == data[i]) {
                return i;
            }
        }
        return -1;  //n not in node
    }

    public int getLast() {
        int i = data.length-1;
        while (i >= 0 && data[i] == 0) {
            i--;
        }
        if (i < 0) {
            return -1;
        } else {
            return data[i];
        }
    }

    @Override
    public String toString() {
        String ret = "";

        int i = 0;
        while (i < data.length && data[i] != 0) {
            ret += data[i] + ", ";
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
