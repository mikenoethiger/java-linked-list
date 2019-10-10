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
		private DLinkedList<T> m_owner;
		private T m_data; // @marco removed "final" für method set()
		private ListItem<T> m_next;
		private ListItem<T> m_previous;

		private ListItem(DLinkedList<T> owner, T data) {
			assert owner != null;
			this.m_data = data;
			this.m_owner = owner;
		}

		public T getData() {
			return m_data;
		}
	}

	public DLinkedList() {
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		if (c == null || c.isEmpty()) {
			return false;
		}
		for (T data : c) {
			add(data);
		}
		modCount++;
		return true;
	}

	@Override
	public T get(ListItem<T> item) {
		assert item != null;
		checkMembershipPrecondition(item);
		return item.m_data;
	}

	@Override
	public int size() {
		return m_size;
	}

	@Override
	public boolean checkMembership(ListItem<T> item) {
		return item != null && item.m_owner == this;
	}

	/**
	 * Asserts the precondition "items in this list"
	 */
	private void checkMembershipPrecondition(ListItem<T> item) {
		assert item.m_owner == this;
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
		linkInBack(new ListItem<T>(this, e));
		return true;
	}

	@Override
	public ListItem<T> next(ListItem<T> item) {
		assert item != null;
		checkMembershipPrecondition(item);
		return item.m_next;
	}

	@Override
	public ListItem<T> previous(ListItem<T> item) {
		assert item != null;
		checkMembershipPrecondition(item);
		return item.m_previous;
	}

	@Override
	public ListItem<T> cyclicNext(ListItem<T> item) {
		assert item != null;
		checkMembershipPrecondition(item);
		if (item == m_tail) {
			return m_head;
		}
		return item.m_next;
	}

	@Override
	public ListItem<T> cyclicPrevious(ListItem<T> item) {
		assert item != null;
		checkMembershipPrecondition(item);
		if (item == m_head) {
			return m_tail;
		}
		return item.m_previous;
	}

	@Override
	public ListItem<T> delete(ListItem<T> item, boolean next) {
		return deleteImpl(item, next);
	}

	private ListItem<T> deleteImpl(ListItem<T> item, boolean next) {
		assert item != null;
		checkMembershipPrecondition(item);
		ListItem<T> returnValue = null;
		if (next) {
			returnValue = item.m_next;
		} else {
			returnValue = item.m_previous;
		}
		unlink(item);
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
		assert item != null;
		checkMembershipPrecondition(item); // TODO discuss: throw exception ok?
		// no modCount++ seems to be correct
		item.m_data = data;
	}

	@Override
	public T remove(ListItem<T> item) {
		assert item != null;
		checkMembershipPrecondition(item);
		unlink(item);
		return item.m_data;
	}

	@Override
	public ListItem<T> addHead(T data) {
		ListItem<T> newItem = new ListItem<>(this, data);
		linkInFront(newItem);
		return newItem;
	}

	@Override
	public ListItem<T> addTail(T data) {
		ListItem<T> newItem = new ListItem<>(this, data);
		linkInBack(newItem);
		return newItem;
	}

	@Override
	public ListItem<T> addAfter(ListItem<T> item, T data) {
		if (item == null) {
			// TODO Is it correct to prepend the new item to head if item is null?
			return addHead(data);
		}
		return addBefore(next(item), data);
	}

	@Override
	public ListItem<T> addBefore(ListItem<T> item, T data) {
		if (item == null) {
			// TODO Is it correct to append the new item to tail if item is null?
			return addTail(data);
		}
		checkMembershipPrecondition(item);
		if (item == m_head) {
			return addHead(data);
		}
		ListItem<T> newItem = new ListItem<>(this, data);
		linkInAfter(previous(item), newItem);
		return newItem;
	}

	@Override
	public void moveToHead(ListItem<T> item) {
		assert item != null;
		checkMembershipPrecondition(item);
		unlink(item);
		linkInFront(item);
		modCount++;
	}

	@Override
	public void moveToTail(ListItem<T> item) {
		assert item != null;
		checkMembershipPrecondition(item);
		unlink(item);
		linkInBack(item);
		modCount++;
	}

	@Override
	public void rotate(ListItem<T> item) {
		assert item != null;
		checkMembershipPrecondition(item); // added by s?mi
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
		assert item1 != null && item2 != null;
		// SWAP WAS A PAIN IN THE ASS -.-
		// Edge Cases:
		// * item1 = item2 (nothing to rewire)
		// * item2 is left neighbour of item1 (6 rewires)
		// * item2 is right neighbour of item1 (6 rewires, other way around)
		// * item1 and item2 are not neighbours (8 rewires, due to the intermediate
		// element(s))
		// * item1 was head|tail
		// * item2 was head|tail
		// If item1 and item2 are not neighbours, predecessor/successor of item1 will
		// become
		// the pred/succ of item2 and vice versa.
		// But if they are neighbours, doing the same "stupid" relation swap will end in
		// an endless linkage.
		// Consider the following linked list:
		// item0 <> item1 <> item2 <> item3
		// If the succ of item1 becomes the succ of item2, item2 would link to itself.

		checkMembershipPrecondition(item1);
		checkMembershipPrecondition(item2);

		if (item1 == item2)
			return;

		if (item1.m_previous == item2) { // item2 is left neighbour of item1
			// swap references, so that item2 is now right neighbour of item1
			// which will make it obsolete to distinct between left and right neighbour,
			// thus reduce code redundancy, complexity and increase readability.
			ListItem<T> tmp = item1;
			item1 = item2;
			item2 = tmp;
		}

		// snapshot of relations
		ListItem<T> item1Prev = item1.m_previous;
		ListItem<T> item1Next = item1.m_next;
		ListItem<T> item2Prev = item2.m_previous;
		ListItem<T> item2Next = item2.m_next;

		item1.m_next = item2Next;
		item2.m_previous = item1Prev;

		if (item1Next == item2) { // item2 is right neighbour of item1 (6 assignments)
			item1.m_previous = item2;
			item2.m_next = item1;
		} else { // item2 is not neighbour of item1 (8 assignments, 2 more for intermediate
					// element(s))
			item1.m_previous = item2Prev;
			item2.m_next = item1Next;

			if (item2Prev != null)
				item2Prev.m_next = item1;
			if (item1Next != null)
				item1Next.m_previous = item2;
		}

		// item1 was not head
		if (item1Prev != null)
			item1Prev.m_next = item2;
		// item2 was not tail
		if (item2Next != null)
			item2Next.m_previous = item1;

		// refresh heads/tails
		if (m_head == item1)
			m_head = item2;
		else if (m_head == item2)
			m_head = item1;
		if (m_tail == item1)
			m_tail = item2;
		else if (m_tail == item2)
			m_tail = item1;
	}

	@Override
	public void reverse() {
		ListItem<T> left = head();
		ListItem<T> right = tail();
		for (int i = 0; i < size() / 2; i++) {
			swap(left, right);
			// notice that left and right have been swapped
			ListItem<T> leftBefore = left;
			left = next(right);
			right = previous(leftBefore);
		}
	}

	@Override
	public void addAfter(ListItem<T> item, List<T> list) {
		assert list != null && list != this;
		if (list.isEmpty()) {
			return;
		}

		if (list instanceof DLinkedList) {
			addAfterDLinkedList(item, (DLinkedList<T>) list);
		} else {
			for (T data : list) {
				addAfter(item, data);
			}
			list.clear();
		}
	}

	/**
	 * Inserts all elements of list after item in O(1) time.
	 *
	 * @param item
	 * @param list
	 */
	protected void addAfterDLinkedList(ListItem<T> item, DLinkedList<T> list) {
		// TODO O(n) but very secure, decide?
		DLinkedListIterator iter = list.listIterator();
		while (iter.hasNext()) {
			iter.nextItem().m_owner = this;
		}
		if (item == null) {
			ListItem<T> oldHead = m_head;
			m_head = list.m_head;
			if (oldHead != null) {
				linkTogether(list.m_tail, oldHead);
			} else {
				m_tail = list.m_tail;
			}
		} else {
			if (item.m_next == null) {
				m_tail = list.m_tail;
			} else {
				linkTogether(list.m_tail, item.m_next);
			}
			linkTogether(item, list.m_head);
		}

		m_size += list.size();
		modCount++;
		list.clear();
	}

	@Override
	public void addBefore(ListItem<T> item, List<T> list) {
		assert list != null && list != this;
		if (list.isEmpty()) {
			return;
		}
		if (list instanceof DLinkedList) {
			addBeforeDLinkedList(item, (DLinkedList<T>) list);
		} else {
			for (T data : list) {
				addBefore(item, data);
			}
			list.clear();
		}
	}

	/**
	 * Inserts all elements of list after item in O(1) time.
	 *
	 * @param item
	 * @param list
	 */
	protected void addBeforeDLinkedList(ListItem<T> item, DLinkedList<T> list) {
		// TODO O(n) bu very secure, decide?
		DLinkedListIterator iter = list.listIterator();
		while (iter.hasNext()) {
			iter.nextItem().m_owner = this;
		}

		// item is null -> insert list AFTER list (against intuition)
		if (item == null) {
			ListItem<T> oldTail = m_tail;
			m_tail = list.m_tail;
			if (oldTail != null) {
				linkTogether(oldTail, list.m_head);
			} else {
				m_head = list.m_head;
			}
			// item.previous -> insert list BEFORE existing list
		} else if (item.m_previous == null) {
			ListItem<T> oldHead = m_head;
			m_head = list.m_head;
			if (oldHead != null) {
				linkTogether(list.m_tail, oldHead);
			} else {
				m_tail = list.m_tail;
			}
			// insert list BEFORE item
		} else {
			linkTogether(item.m_previous, list.m_head);
			linkTogether(list.m_tail, item);
		}

		m_size += list.m_size;
		modCount++;
		list.clear();
	}

	/**
	 * Connects firs to second, without modifying first.m_next
	 */
	private void linkTogether(ListItem<T> first, ListItem<T> second) {
		first.m_next = second;
		second.m_previous = first;
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
		checkMembershipPrecondition(startInclusive);
		checkMembershipPrecondition(endExclusive);

		DLinkedList<T> removedElements = new DLinkedList<T>();
		ListItem<T> current = startInclusive;

		do {
			ListItem<T> newOne = cyclicNext(current);
			unlink(current);
			current.m_owner = removedElements;
			removedElements.linkInBack(current);
			current = newOne;
		} while (current != endExclusive && m_size != 0);
		// check size because when removing all items (startInclusive
		// == endExclusive) the last cyclicNext call removes itself.
		return removedElements;
	}

	/**
	 * Make item new head
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
		m_size++;
	}

	/**
	 * Make item new tail
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
		m_size++;
		modCount++;
	}

	/**
	 * Links item after prev.
	 */
	private void linkInAfter(ListItem<T> prev, ListItem<T> item) {
		assert prev != null;
		assert item != null;

		// TODO cache prev.m_next, because first linkTogether will override it.
		ListItem<T> next = prev.m_next;
		linkTogether(prev, item);
		linkTogether(item, next);
		m_size++;
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
		m_size--;
		modCount++;
	}

	public DLinkedListIterator listIterator(ListItem<T> firstItem) {
		return new DLinkedListIterator(firstItem);
	}

	@Override
	public DLinkedListIterator listIterator() {
		return new DLinkedListIterator(m_head);
	}

	@Override
	public Iterator<T> iterator() {
		return new DLinkedListIterator(m_head);
	}

	// TODO maybe add option to enable cycling, needed by remove
	public class DLinkedListIterator implements IListIterator<T> {
		/**
		 * The current modCount known to this iterator. Will be compared with modCount
		 * of list and throw exception
		 */
		private long m_curModCount;
		/**
		 * The last returned element by next or previous. Or null, if next or previous
		 * have not been used
		 */
		private ListItem<T> m_returned;
		/**
		 * The next element of this iterator.
		 */
		private ListItem<T> m_next;
		/**
		 * The current index of the
		 */
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
			// TODO test
			if (m_returned == null) {
				throw new IllegalStateException();
			} else {
				return m_returned;
			}
		}

		@Override
		public void add(T e) {
			// TODO test
			checkModCount();
			if (m_returned == null) {
				throw new IllegalStateException();
			} else {
				DLinkedList.this.addAfter(m_returned, e);
				m_curModCount++;
				m_index++;
			}
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
			checkModCount();
			if (m_returned == null) {
				throw new IllegalStateException();
			} else {
				m_returned.m_data = data;
			}
		}
	}

	@Override
	public <T1> T1[] toArray(T1[] a) {
		return super.toArray(a);
	}

	@Override
	public T get(int index) {
		throw new UnsupportedOperationException("cannot be implemented efficiently");
	}

	@Override
	public IListIterator<T> listIterator(int index) {
		throw new UnsupportedOperationException("cannot be implemented efficiently");
	}

	@Override
	public boolean removeIf(Predicate<? super T> filter) {
		throw new UnsupportedOperationException("cannot be implemented efficiently");
	}

	@Override
	public void sort(Comparator<? super T> c) {
		super.sort(c);
	}

	@Override
	public Spliterator<T> spliterator() {
		return super.spliterator(); // TODO discuss for efficiency
	}

	@Override
	public void replaceAll(UnaryOperator<T> operator) {
		super.replaceAll(operator);
	}

	@Override
	public Stream<T> stream() {
		return super.stream();
	}

	@Override
	public Stream<T> parallelStream() {
		return super.parallelStream();
	}

	@Override
	public void forEach(Consumer<? super T> action) {
	}

}