package ch.fhnw.algd2;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class DLinkedList<T> extends AbstractList<T> implements IList<T> {

    private ListItem<T> m_head;
    private ListItem<T> m_tail;
    private int m_size;

    public static class ListItem<T> {

        private final T m_data;
        private ListItem<T> m_next;
        private ListItem<T> m_previous;

        private ListItem(T data) {
            this.m_data = data;
        }
    }

    public DLinkedList() {
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return super.toArray(a);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c == null || c.isEmpty()) return false;
        for (T item : c) {
            ListItem<T> lItem = new ListItem<>(item);
            linkInBack(lItem);
        }
        return true;
    }

    @Override
    public T get(ListItem item) {
        // TODO find solution for type check on item
        if (item == null || !checkMembership(item)) {
            throw new NoSuchElementException();
        }

        return (T) item.m_data;
    }

    @Override
    public T get(int index) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public int size() {
        return m_size;
    }

    @Override
    public boolean checkMembership(ListItem item) {
        // TODO in order to check the membership of an item, the item needs a reference to the LinkedList it belongs to. This reference has to be removed (or set to null), as the item is deleted from the list.
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public ListItem head() {
        return m_head;
    }

    @Override
    public ListItem tail() {
        return m_tail;
    }

    @Override
    public ListItem next(ListItem item) {
        if (!checkMembership(item)) {
            throw new NoSuchElementException(item.toString());
        }
        return item.m_next;
    }

    @Override
    public ListItem previous(ListItem item) {
        if (!checkMembership(item)) {
            throw new NoSuchElementException(item.toString());
        }
        return item.m_previous;
    }

    @Override
    public ListItem cyclicNext(ListItem item) {
        if (!checkMembership(item)) {
            throw new NoSuchElementException(item.toString());
        }
        if (item == m_tail) {
            return m_head;
        }
        return item.m_next;
    }

    @Override
    public ListItem cyclicPrevious(ListItem item) {
        if (!checkMembership(item)) {
            throw new NoSuchElementException(item.toString());
        }
        if (item == m_head) {
            return m_tail;
        }
        return item.m_previous;
    }

    @Override
    public ListItem delete(ListItem item, boolean next) {
        // TODO @sämi
        return null;
    }

    @Override
    public ListItem cyclicDelete(ListItem item, boolean next) {
        // TODO @sämi
        return null;
    }

    @Override
    public void set(ListItem item, T data) {
        // TODO @marco
    }

    @Override
    public T remove(ListItem item) {
        // TODO @sämi
        return null;
    }

    @Override
    public ListItem addHead(T data) {
        // TODO @mike
        return null;
    }

    @Override
    public ListItem addTail(T data) {
        // TODO @mike
        return null;
    }

    @Override
    public ListItem addAfter(ListItem item, T data) {
        // TODO @mike
        return null;
    }

    @Override
    public ListItem addBefore(ListItem item, T data) {
        // TODO @mike
        return null;
    }

    @Override
    public void moveToHead(ListItem item) {
        // TODO @sämi

    }

    @Override
    public void moveToTail(ListItem item) {
        // TODO @sämi
    }

    @Override
    public void rotate(ListItem item) {
        // TODO @marco
    }

    @Override
    public void swap(ListItem item1, ListItem item2) {
        // TODO @mike
    }

    @Override
    public void reverse() {
        // TODO @mike
    }

    @Override
    public void addAfter(ListItem item, List<T> list) {
        // TODO @sämi list has to be copied into ListItem's
    }

    @Override
    public void addBefore(ListItem item, List<T> list) {
        // TODO @sämi list has to be copied into ListItem's
    }

    @Override
    public void conc(List<T> list, boolean after) {
        // TODO @sämi
    }

    @Override
    public IList<T> remove(ListItem startInclusive, ListItem endExclusive) {

        return null;
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {

    }

    @Override
    public void sort(Comparator<? super T> c) {

    }

    @Override
    public Spliterator<T> spliterator() {
        return null;
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        return false;
    }

    @Override
    public Stream<T> stream() {
        return null;
    }

    @Override
    public Stream<T> parallelStream() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super T> action) {

    }

    @Override
    public IListIterator<T> listIterator() {
        return null;
    }

    @Override
    public IListIterator<T> listIterator(int index) {
        return null;
    }

    private void linkInFront(ListItem item) {

    }

    private void linkInBack(ListItem item) {

    }

    private void linkInAfter(ListItem prev, ListItem item) {

    }

    private void unlink(ListItem item) {

    }
}
