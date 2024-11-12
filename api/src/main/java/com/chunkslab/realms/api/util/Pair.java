package com.chunkslab.realms.api.util;

/**
 * A generic class representing a pair of values.
 * This class provides methods to create and access pairs of values.
 *
 * @param <L> the type of the left value
 * @param <R> the type of the right value
 */
public class Pair<L, R> {
    private L left;
    private R right;

    /**
     * Constructs a new {@link Pair} with the specified left and right values.
     *
     * @param left  the left value
     * @param right the right value
     */
    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Returns the left value.
     *
     * @return the left value
     */
    public L left() {
        return left;
    }

    /**
     * Sets the left value.
     *
     * @param left the new left value
     */
    public void left(L left) {
        this.left = left;
    }

    /**
     * Returns the right value.
     *
     * @return the right value
     */
    public R right() {
        return right;
    }

    /**
     * Sets the right value.
     *
     * @param right the new right value
     */
    public void right(R right) {
        this.right = right;
    }

    /**
     * Creates a new {@link Pair} with the specified left and right values.
     *
     * @param left  the left value
     * @param right the right value
     * @param <L>   the type of the left value
     * @param <R>   the type of the right value
     * @return a new {@link Pair} with the specified values
     */
    public static <L, R> Pair<L, R> of(final L left, final R right) {
        return new Pair<>(left, right);
    }
}