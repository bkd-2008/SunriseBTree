public class Driver {
    public static void main(String[] args) {
        BTree demo = new BTree();

//        demo.insert(5);
//        demo.insert(9);
//        demo.insert(7);
//        demo.insert(3);
//        demo.insert(20);
//        demo.insert(12);
//        demo.insert(15);
//        demo.insert(100);
//        demo.insert(14);
//        demo.insert(2);
//        demo.insert(50);
//        demo.insert(25);
//        demo.insert(66);
//        demo.insert(70);

        for (int i = 1; i < 101; i++) {
            demo.insert(new Key(i));
        }
        System.out.println();
    }
}
