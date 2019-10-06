package ch.fhnw.algd2;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class DLinkedList<T> extends AbstractList<T> implements IList<T> {

	private ListItem<T> m_head;
	private ListItem<T> m_tail;
	private int m_size;

	public static class ListItem<T> {

		private T m_data; // @marco removed "final" f√ºr method set()
		private ListItem<T> m_next;
		private ListItem<T> m_previous;

		private ListItem(T data) {
			this.m_data = data;
		}

		public T getData() {
			return m_data;
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
		if (c == null || c.isEmpty())
			return false;
		for (T data : c) {
			add(data);
		}
		modCount++;
		return true;
	}

	@Override
	public T get(ListItem<T> item) {
		// TODO find solution for type check on item
		if (item == null || !checkMembership(item)) {
			throw new NoSuchElementException();
		}

		return (T) item.m_data;
	}

	@Override
	public T get(int index) {
		throw new UnsupportedOperationException("cannot be implemented efficiently");
	}

	@Override
	public int size() {
		return m_size;
	}

	@Override
	public boolean checkMembership(ListItem<T> item) {
		// TODO in order to check the membership of an item, the item needs a reference
		// to the LinkedList it belongs to. This reference has to be removed (or set to
		// null), as the item is deleted from the list.
		return true;
	}

	private void checkMembershipOrThrow(ListItem<T> item) {
		if (!checkMembership(item)) {
			throw new NoSuchElementException("item is no element of this list");
		}
	}

	@Override
	public ListItem<T> head() {
		return m_head;
	}

	@Override
	public ListItem<T> tail() {
		return m_tail;
	}

	@Override
	public void clear() {
		m_tail = null;
		m_head = null;
		m_size = 0;
		modCount++;
	}

	@Override
	public void add(int index, T element) {
		throw new UnsupportedOperationException("cannot be implemented efficiently");
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		throw new UnsupportedOperationException("cannot be implemented efficiently");
	}

	@Override
	public boolean add(T e) {
		linkInBack(new ListItem<T>(e));
		m_size++;
		modCount++;
		return true;
	}

	@Override
	public ListItem<T> next(ListItem<T> item) {
		if (!checkMembership(item)) {
			throw new NoSuchElementException(item.toString());
		}
		return item.m_next;
	}

	@Override
	public ListItem<T> previous(ListItem<T> item) {
		if (!checkMembership(item)) {
			throw new NoSuchElementException(item.toString());
		}
		return item.m_previous;
	}

	@Override
	public ListItem<T> cyclicNext(ListItem<T> item) {
		if (!checkMembership(item)) {
			throw new NoSuchElementException(item.toString());
		}
		if (item == m_tail) {
			return m_head;
		}
		return item.m_next;
	}

	@Override
	public ListItem<T> cyclicPrevious(ListItem<T> item) {
		if (!checkMembership(item)) {
			throw new NoSuchElementException(item.toString());
		}
		if (item == m_head) {
			return m_tail;
		}
		return item.m_previous;
	}

	@Override
	public ListItem<T> delete(ListItem<T> item, boolean next) {
		checkMembershipOrThrow(item);
		ListItem<T> returnValue = null;
		if (next) {
			returnValue = item.m_next;
		} else {
			returnValue = item.m_previous;
		}
		unlink(item);
		m_size--;
		modCount++;
		return returnValue;
	}

	@Override
	public ListItem<T> cyclicDelete(ListItem<T> item, boolean next) {
		ListItem<T> nextOrPrevious = delete(item, next);
		if (nextOrPrevious == null) {
			return next ? m_head : m_tail;
		} else {
			return nextOrPrevious;
		}
	}

	@Override
	public void set(ListItem<T> item, T data) {
		checkMembershipOrThrow(item); // TODO discuss: throw exception ok?
		// no modCount++ seems to be correct
		item.m_data = data;
	}

	@Override
	public T remove(ListItem<T> item) {
		if (checkMembership(item)) {
			unlink(item);
			m_size--;
			modCount++;
			return item.m_data;
		}
		return null;
	}

	@Override
	public ListItem<T> addHead(T data) {
		// TODO @mike
		return null;
	}

	@Override
	public ListItem<T> addTail(T data) {
		// TODO @mike
		return null;
	}

	@Override
	public ListItem<T> addAfter(ListItem<T> item, T data) {
		// TODO @mike
		return null;
	}

	@Override
	public ListItem<T> addBefore(ListItem<T> item, T data) {
		// TODO @mike
		return null;
	}

	@Override
	public void moveToHead(ListItem<T> item) {
		checkMembershipOrThrow(item);
		unlink(item);
		linkInFront(item);
		modCount++;
	}

	@Override
	public void moveToTail(ListItem<T> item) {
		checkMembershipOrThrow(item);
		unlink(item);
		linkInBack(item);
		modCount++;
	}

	@Override
	public void rotate(ListItem<T> item) {
		// @marco: check if item is really in list?
		checkMembershipOrThrow(item); // added by s‰mi
		if (item != m_head) {
			m_head.m_previous = m_tail;
			m_tail.m_next = m_head;
			m_head = item;
			m_tail = item.m_previous;
			m_head.m_previous = null;
			m_tail.m_next = null;
			modCount++;
		}
	}

	@Override
	public void swap(ListItem<T> item1, ListItem<T> item2) {
		// TODO @mike
	}

	@Override
	public void reverse() {
		// TODO @mike
	}

	@Override
	public void addAfter(ListItem<T> item, List<T> list) {
		assert list != null && list != this;
		if (list instanceof DLinkedList) {
			addAfterEfficient(item, (DLinkedList<T>) list);
		} else {
			for (T data : list) {
				addAfter(item, data);
			}
			m_size += list.size();
			modCount++;
			list.clear();
		}
	}

	/**
	 * Inserts all elements of list after item in O(1) time.
	 * 
	 * @param item
	 * @param list
	 */
	protected void addAfterEfficient(ListItem<T> item, DLinkedList<T> list) {
		if (list.isEmpty()) {
			return;
		}
		if (item == null) {
			ListItem<T> oldHead = m_head;
			m_head = list.m_head;
			list.m_tail.m_next = oldHead;
			if (oldHead != null) {
				oldHead.m_previous = list.m_tail;
			} else {
				m_tail = list.m_tail;
			}
		} else {
			if (item.m_next == null) {
				m_tail = list.m_tail;
			} else {
				item.m_next.m_previous = list.m_tail;
			}
			list.m_tail.m_next = item.m_next;
			list.m_head.m_previous = item;
			item.m_next = list.m_head;
		}

		m_size += list.size();
		modCount++;
		list.clear();
	}

	@Override
	public void addBefore(ListItem<T> item, List<T> list) {
		assert list != null && list != this;
		if (list instanceof DLinkedList) {
			addBeforeEfficient(item, (DLinkedList<T>) list);
		} else {
			for (T data : list) {
				addBefore(item, data);
			}
			m_size += list.size();
			modCount++;
			list.clear();
		}
	}

	/**
	 * Inserts all elements of list after item in O(1) time.
	 * 
	 * @param item
	 * @param list
	 */
	protected void addBeforeEfficient(ListItem<T> item, DLinkedList<T> list) {
		if (list.isEmpty()) {
			return;
		}

		if (item == null) {
			ListItem<T> oldTail = m_tail;
			m_tail = list.m_head;
			m_tail.m_previous = oldTail;
			if (oldTail != null) {
				oldTail.m_next = list.m_head;
			} else {
				m_tail = list.m_tail;
			}
		} else if (item.m_previous == null) {
			ListItem<T> oldHead = m_head;
			m_head = list.m_head;
			list.m_tail.m_next = oldHead;
			if (oldHead != null) {
				oldHead.m_previous = list.m_tail;
			} else {
				m_tail = list.m_tail;
			}
		} else {
			item.m_previous.m_next = list.m_head;
			list.m_head.m_previous = item.m_previous;
			item.m_previous = list.m_tail;
			list.m_tail.m_next = item;
		}

		m_size += list.m_size;
		modCount++;
		list.clear();
	}

	@Override
	public void conc(List<T> list, boolean after) {
		if (after) {
			addAfter(m_tail, list);
		} else {
			addBefore(m_head, list);
		}
	}

	@Override
	public IList<T> remove(ListItem<T> startInclusive, ListItem<T> endExclusive) {
		checkMembershipOrThrow(startInclusive);
		checkMembershipOrThrow(endExclusive);

		DLinkedList<T> removedElements = new DLinkedList<T>();
		DLinkedListIterator iter = listIterator(startInclusive);
		ListItem<T> current = iter.nextItem();
		do {
			iter.remove();
			removedElements.add(current.getData());
			if (iter.hasNext()) {
				current = iter.nextItem();
			} else {
				iter = listIterator();
				current = iter.nextItem();
			}
			// TODO optimize
		} while (current != endExclusive);
		return removedElements;
	}

	@Override
	public void replaceAll(UnaryOperator<T> operator) {
		// TODO check if efficient
		super.replaceAll(operator);
	}

	@Override
	public void sort(Comparator<? super T> c) {
		// TODO check if efficient
		super.sort(c);
	}

	@Override
	public Spliterator<T> spliterator() {
		return spliterator(); // TODO discuss for efficiency
	}

	@Override
	public boolean removeIf(Predicate<? super T> filter) {
		return removeIf(filter); // TODO discuss for efficiency
	}

	@Override
	public Stream<T> stream() {
		return super.stream(); // TODO discuss for efficiency
	}

	@Override
	public Stream<T> parallelStream() {
		return super.parallelStream(); // TODO discuss for efficiency
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		super.forEach(action); // TODO discuss for efficiency
	}

	public DLinkedListIterator listIterator(ListItem<T> firstItem) {
		return new DLinkedListIterator(firstItem);
	}

	@Override
	public DLinkedListIterator listIterator() {
		return new DLinkedListIterator(m_head);
	}

	@Override
	public IListIterator<T> listIterator(int index) {
		throw new UnsupportedOperationException("cannot be implemented efficiently");
	}

	@Override
	public Iterator<T> iterator() {
		return new DLinkedListIterator(m_head);
	}

	/**
	 * Make item new head Unlinks item Connects old head with item Makes new item
	 * head
	 * 
	 * @param item
	 */
	private void linkInFront(ListItem<T> item) {
		assert item != null;
		item.m_next = m_head;
		m_head = item;
		if (item.m_next != null) { // TODO added to prevent nullpointer, check
			item.m_next.m_previous = item;
		} else {
			m_tail = m_head;
		}
	}

	/**
	 * Make item new tail unlink item Connects item with old tail Sets item as new
	 * tail
	 * 
	 * @param item
	 */
	private void linkInBack(ListItem<T> item) {
		assert item != null;

		item.m_previous = m_tail;
		m_tail = item;
		if (item.m_previous == null) {
			m_head = item;
		} else {
			item.m_previous.m_next = item;
		}
	}

	/**
	 * Links item after prev. Unlinks item Connects prev with item Connets item with
	 * prev.next
	 */
	private void linkInAfter(ListItem<T> prev, ListItem<T> item) {
		assert prev != null;
		assert item != null;

		item.m_previous = prev;
		item.m_next = prev.m_next;

		item.m_previous.m_next = item;
		item.m_next.m_previous = item;
	}

	/**
	 * Removes the item from the list. Removes next and prev from item Connect prev
	 * and next together
	 * 
	 * @param item
	 */
	private void unlink(ListItem<T> item) {
		assert item != null;

		ListItem<T> previous = item.m_previous;
		ListItem<T> next = item.m_next;

		if (previous == null) {
			// item was head, next is new head
			m_head = next;
			// next.m_previous = null; in next if
		} else {
			// item was not head
			previous.m_next = next;
		}

		if (next == null) {
			// item was tail, previous is new tail
			m_tail = previous;
			// next.m_previous = null; already set above in if
		} else {
			// item was not tail
			next.m_previous = previous;
		}

		item.m_next = null;
		item.m_previous = null;
	}

	public class DLinkedListIterator implements IListIterator<T> {

		private long m_curModCount;
		private ListItem<T> m_returned;
		private ListItem<T> m_next;
		private int m_index;

		private DLinkedListIterator(ListItem<T> next) {
			m_next = next;
			m_curModCount = DLinkedList.this.modCount;
		}

		private void checkModCount() {
			if (m_curModCount != DLinkedList.this.modCount) {
				throw new ConcurrentModificationException("list was modified outside iterator");
			}
		}

		@Override
		public boolean hasNext() {
			return m_next != null;
		}

		@Override
		public boolean hasPrevious() {
			return m_next == null ? m_tail != null : m_next.m_previous != null;
		}

		@Override
		public T next() {
			return nextItem().m_data;
		}

		public ListItem<T> nextItem() {
			checkModCount();
			if (m_next == null) {
				throw new NoSuchElementException("iterator reached end of list");
			}
			m_returned = m_next;
			m_next = m_returned.m_next;
			m_index++;
			return m_returned;
		}

		public ListItem<T> previousItem() {
			checkModCount();
			if (m_next == null) {
				throw new NoSuchElementException("iterator reached end of list");
			}
			if (m_next == null) {
				m_returned = m_tail; // in case we reached end of list with next, previous should returned last
										// element of list
			} else {
				m_returned = m_next.m_previous;
			}
			m_next = m_returned;
			m_index--;
			return m_returned;
		}

		@Override
		public T previous() {
			return previousItem().m_data;
		}

		@Override
		public int nextIndex() {
			return m_index;
		}

		@Override
		public int previousIndex() {
			return m_index - 1;
		}

		@Override
		public ListItem<T> getVisited() {
			if (m_returned == null) {
				throw new IllegalStateException();
			} else {
				return m_returned;
			}
		}

		@Override
		public void add(T e) {
			// TODO Auto-generated method stub
			checkModCount();
			DLinkedList.this.addAfter(m_returned, e);
			m_curModCount++;
		}

		public void remove() {
			checkModCount();
			if (m_returned == null) {
				throw new IllegalStateException();
			} else {
				if (m_returned == m_next) {
					m_next = m_returned.m_next;
				} else {
					m_index--;
				}
				DLinkedList.this.remove(m_returned);
				m_returned = null;
				m_curModCount++;
			}
		}

		public void set(T data) {
			if (m_returned == null) {
				throw new IllegalStateException();
			} else {
				m_returned.m_data = data;
			}
		}
	}
}
