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
        return super.addAll(c);
    }

    @Override
    public T get(ListItem item) {
        return null;
    }

    @Override
    public T get(int index) {
        return null;
    }

    @Override
    public int size() {
        return m_size;
    }

    @Override
    public boolean checkMembership(ListItem item) {
        return false;
    }

    @Override
    public ListItem head() {
        return null;
    }

    @Override
    public ListItem tail() {
        return null;
    }

    @Override
    public ListItem next(ListItem item) {
        return null;
    }

    @Override
    public ListItem previous(ListItem item) {
        return null;
    }

    @Override
    public ListItem cyclicNext(ListItem item) {
        return null;
    }

    @Override
    public ListItem cyclicPrevious(ListItem item) {
        return null;
    }

    @Override
    public ListItem delete(ListItem item, boolean next) {
        return null;
    }

    @Override
    public ListItem cyclicDelete(ListItem item, boolean next) {
        return null;
    }

    @Override
    public void set(ListItem item, T data) {

    }

    @Override
    public T remove(ListItem item) {
        return null;
    }

    @Override
    public ListItem addHead(T data) {
        return null;
    }

    @Override
    public ListItem addTail(T data) {
        return null;
    }

    @Override
    public ListItem addAfter(ListItem item, T data) {
        return null;
    }

    @Override
    public ListItem addBefore(ListItem item, T data) {
        return null;
    }

    @Override
    public void moveToHead(ListItem item) {

    }

    @Override
    public void moveToTail(ListItem item) {

    }

    @Override
    public void rotate(ListItem item) {

    }

    @Override
    public void swap(ListItem item1, ListItem item2) {

    }

    @Override
    public void reverse() {

    }

    @Override
    public void addAfter(ListItem item, List<T> list) {
        // TODO list has to be copied into ListItem's
    }

    @Override
    public void addBefore(ListItem item, List<T> list) {
        // TODO list has to be copied into ListItem's
    }

    @Override
    public void conc(List<T> list, boolean after) {

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
