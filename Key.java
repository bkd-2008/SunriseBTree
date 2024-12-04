public class Key {
    int data;
    Node leftChild, rightChild;

    public Key(int n) {
        data = n;
        leftChild = null;
        rightChild = null;
    }

    /**
     * Method to compare two keys.
     *
     * @param o Other key being compared.
     * @return >0 if this is greater, <0 if this is less, 0 else
     */
    public int compareTo(Key o) {
        if (o == null) {
            return 1;
        }
        return this.data - o.data;
    }

    public boolean isEquals(Key o) {
        return this.data == o.data;
    }
}
