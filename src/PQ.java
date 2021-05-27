import java.util.ArrayList;

public class PQ {
    private ArrayList<Node> list;

    public PQ() {
        this.list = new ArrayList<>();
    }

    public void add(Node n) {
        list.add(n);
    }

    public Node remove() {
        if (list.isEmpty())
            return null;

        Node removedObject = list.get(0);
        list.set(0, list.get(list.size() - 1));
        list.remove(list.size() - 1);

        int currentIndex = 0;

        while (currentIndex < list.size()) {
            int leftChildIndex = 2 * currentIndex + 1;
            int rightChildIndex = 2 * currentIndex + 2;

            if (leftChildIndex >= list.size())
                break;

            int maxIndex = leftChildIndex;
            if (rightChildIndex < list.size()) {
                if (list.get(maxIndex).compareTo(list.get(rightChildIndex)) < 0)
                    maxIndex = rightChildIndex;
            }

            if (list.get(currentIndex).compareTo(list.get(maxIndex)) < 0) {
                Node temp = list.get(maxIndex);
                list.set(maxIndex, list.get(currentIndex));
                list.set(currentIndex, temp);
                currentIndex = maxIndex;
            } else
                break;
        }

        return removedObject;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}