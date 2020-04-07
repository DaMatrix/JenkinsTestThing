package net.daporkchop.jenkinstesthing;

/**
 * @author DaPorkchop_
 */
public final class Wrapper {
    private final int data;

    public Wrapper(int data) {
        this.data = data;
    }

    /**
     * Gets this wrapper's value.
     * <p>
     * In accordance with the best principles of data encapsulation, this value cannot be accessed externally in any way.
     */
    private int getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return "Hello World!";
    }
}
