package at.fhv.itm3.s2.roundabout.ui.util;

public class Link<T> {
    private final T value;
    private final double lengthPassed;
    private Link<T> nextLink;

    public Link(T value, double lengthPassed) {
        this(value, lengthPassed,null);
    }

    public Link(T value, double lengthPassed, Link<T> nextLink) {
        this.value = value;
        this.lengthPassed = lengthPassed;
        this.nextLink = nextLink;
    }

    public T getValue() {
        return value;
    }

    public double getLengthPassed() {
        return lengthPassed;
    }

    public Link<T> getNextLink() {
        return nextLink;
    }

    public void setNextLink(Link<T> nextLink) {
        this.nextLink = nextLink;
    }
}