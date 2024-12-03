public class Node {
    Key[] data;
    //    int hr, min, sec;
    //    boolean isAM;

    //Still figuring out how to structure this
    Node parent;
    Node[] child;

    public Node(Node parent, int leafLength) {
        this.parent = parent;
        this.data = new Key[leafLength];
        this.child = new Node[leafLength+1];
    }

    public boolean isLeaf() {
        return child[0] == null;
    }

    public boolean isFull() {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == null) {     //nodes cannot hold 0 values
                return false;
            }
        }
        return true;
    }
    
    public Key split() {
        int medianIndex = data.length / 2;
        if (data.length % 2 == 0) {
            medianIndex--;       //ensures actual median in case of odd data length
        }
        Key median = data[medianIndex];

        //send median to parent
        if (parent == null) {       //TODO--ensure child arrays are saved when parent changes
            parent = new Node(null, data.length);
        }

        parent.insert(median);


        parent.child[parent.indexOf(median)] = new Node(parent, data.length);
        parent.child[parent.indexOf(median)+1] = new Node(parent, data.length);
        Node leftSplit = parent.child[parent.indexOf(median)];
        Node rightSplit = parent.child[parent.indexOf(median)+1];

        for (int i = 0; i < medianIndex; i++) {
            leftSplit.data[i] = data[i];
        }
        int index = 0;  //to be able to track rightsplit index while copying from this' index
        for (int i = medianIndex+1; i < data.length; i++) {
            rightSplit.data[index] = data[i];
            index++;
        }
        return median;      //to compare inserted value with in BTree
    }
    
    public void insert(Key data) {
        for (int i = 0; i < this.data.length; i++) {
            if (data.compareTo(this.data[i]) > 0 && this.data[i] != null) {
                continue;
            }
            for (int j = this.data.length-1; j > i; j--) {
                this.data[j] = this.data[j-1];
            }
            this.data[i] = data;
            break;
        }
    }

    public int indexOf(Key n) {
        for (int i = 0; i < data.length; i++) {
            if (n.equals(data[i])) {
                return i;
            }
        }
        return -1;  //n not in node
    }

    public Key getLast() {
        int i = data.length-1;
        while (i >= 0 && data[i] == null) {
            i--;
        }
        if (i < 0) {
            return null;
        } else {
            return data[i];
        }
    }
}
