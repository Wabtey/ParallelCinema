package tools;

/** From [stackoverflow](https://stackoverflow.com/a/521235) but modified. */
public class Pair<L, R> {

    private final L left;
    private final R right;

    public Pair(L left, R right) {
        if (left == null && right == null) {
            throw new IllegalArgumentException("The two pair's members are null.");
        } else if (left == null) {
            throw new IllegalArgumentException("The left member is null.");
        } else if (right == null) {
            throw new IllegalArgumentException("The right member is null.");
        }

        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }

    @Override
    public int hashCode() {
        return left.hashCode() ^ right.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Pair)
            return this.left.equals(((Pair) o).getLeft()) &&
                    this.right.equals(((Pair) o).getRight());
        return false;
    }

}