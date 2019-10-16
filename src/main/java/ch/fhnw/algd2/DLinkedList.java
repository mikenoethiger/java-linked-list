package ch.fhnw.algd2;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class DLinkedList<T> extends AbstractList<T> implements IList<T> {

	private ListItem m_head;
	private ListItem m_tail;
	private int m_size;

	public static class ListItem {
		private DLinkedList<?> m_owner;
		private Object m_data;
		private ListItem m_next;
		private ListItem m_previous;

		private ListItem(DLinkedList<?> owner, Object data) {
			assert owner != null;
			this.m_data = data;
			this.m_owner = owner;
		}

		public Object getData() {
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
		int tmpModCount = modCount++;
		for (T data : c) {
			add(data);
		}
		modCount = tmpModCount;
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(ListItem item) {
		assert item != null;
		checkMembershipPrecondition(item);
		return (T) item.m_data;
	}

	@Override
	public int size() {
		return m_size;
	}

	@Override
	public boolean checkMembership(ListItem item) {
		return item != null && item.m_owner == this;
	}

	/**
	 * Asserts the precondition "items in this list"
	 */
	private void checkMembershipPrecondition(ListItem item) {
		assert item.m_owner == this;
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
	public void clear() {
		m_tail = null;
		m_head = null;
		m_size = 0;
		modCount++;
	}

	@Override
	public boolean add(T e) {
		linkInBack(new ListItem(this, e));
		return true;
	}

	@Override
	public ListItem next(ListItem item) {
		assert item != null;
		checkMembershipPrecondition(item);
		return item.m_next;
	}

	@Override
	public ListItem previous(ListItem item) {
		assert item != null;
		checkMembershipPrecondition(item);
		return item.m_previous;
	}

	@Override
	public ListItem cyclicNext(ListItem item) {
		assert item != null;
		checkMembershipPrecondition(item);
		if (item == m_tail) {
			return m_head;
		}
		return item.m_next;
	}

	@Override
	public ListItem cyclicPrevious(ListItem item) {
		assert item != null;
		checkMembershipPrecondition(item);
		if (item == m_head) {
			return m_tail;
		}
		return item.m_previous;
	}

	@Override
	public ListItem delete(ListItem item, boolean next) {
		assert item != null;
		checkMembershipPrecondition(item);
		ListItem returnValue = null;
		if (next) {
			returnValue = item.m_next;
		} else {
			returnValue = item.m_previous;
		}
		unlink(item);
		return returnValue;
	}

	@Override
	public ListItem cyclicDelete(ListItem item, boolean next) {
		assert item != null;
		checkMembershipPrecondition(item);
		ListItem nextOrPrevious = delete(item, next);
		if (nextOrPrevious == null) {
			return next ? m_head : m_tail;
		} else {
			return nextOrPrevious;
		}
	}

	@Override
	public void set(ListItem item, T data) {
		assert item != null;
		checkMembershipPrecondition(item);
		// no modCount++ seems to be correct
		item.m_data = data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T remove(ListItem item) {
		assert item != null;
		checkMembershipPrecondition(item);
		unlink(item);
		return (T) item.m_data;
	}

	@Override
	public ListItem addHead(T data) {
		ListItem newItem = new ListItem(this, data);
		linkInFront(newItem);
		return newItem;
	}

	@Override
	public ListItem addTail(T data) {
		ListItem newItem = new ListItem(this, data);
		linkInBack(newItem);
		return newItem;
	}

	@Override
	public ListItem addAfter(ListItem item, T data) {
		if (item == null) {
			return addHead(data);
		}
		return addBefore(next(item), data);
	}

	@Override
	public ListItem addBefore(ListItem item, T data) {
		if (item == null) {
			return addTail(data);
		}
		checkMembershipPrecondition(item);
		if (item == m_head) {
			return addHead(data);
		}
		ListItem newItem = new ListItem(this, data);
		linkInAfter(previous(item), newItem);
		return newItem;
	}

	@Override
	public void moveToHead(ListItem item) {
		assert item != null;
		checkMembershipPrecondition(item);
		unlink(item);
		linkInFront(item);
		modCount++;
	}

	@Override
	public void moveToTail(ListItem item) {
		assert item != null;
		checkMembershipPrecondition(item);
		unlink(item);
		linkInBack(item);
		modCount++;
	}

	@Override
	public void rotate(ListItem item) {
		assert item != null;
		checkMembershipPrecondition(item);
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
	public void swap(ListItem item1, ListItem item2) {
		assert item1 != null && item2 != null;
		checkMembershipPrecondition(item1);
		checkMembershipPrecondition(item2);
		// Edge Cases:
		// * item1 = item2 (nothing to rewire)
		// * item2 is left neighbour of item1 (6 rewires)
		// * item2 is right neighbour of item1 (6 rewires, other way around)
		// * item1 and item2 are not neighbours (8 rewires, due to the intermediate
		// element(s))
		// * item1 was head|tail
		// * item2 was head|tail
		// If item1 and item2 are not neighbours, predecessor/successor of item1 will
		// become the pred/succ of item2 and vice versa.
		// But if they are neighbours, applying the same swap logic will end in
		// a cycle.
		// Consider the following linked list, where you want to swap item1 with item2:
		// item0 <> item1 <> item2 <> item3
		// If we apply the same swap logic as for items that are not neighbours,
		// then the current successor of item1 would become the successor of item2.
		// In this case, item2 would become its own successor, creating a cycle
		// which will lead to a linked list with infinite elements.

		if (item1 == item2)
			return;

		if (item1.m_previous == item2) { // item2 is left neighbour of item1
			// swap references, so that item2 is now right neighbour of item1
			// which will make it obsolete to distinct between left and right neighbour,
			// thus reduce code redundancy, complexity and increase readability.
			ListItem tmp = item1;
			item1 = item2;
			item2 = tmp;
		}

		// snapshot of relations
		ListItem item1Prev = item1.m_previous;
		ListItem item1Next = item1.m_next;
		ListItem item2Prev = item2.m_previous;
		ListItem item2Next = item2.m_next;

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
		ListItem current = head();
		for (int i = 0; i < m_size; i++) {
			ListItem tmp = current.m_next;
			current.m_next = current.m_previous;
			current.m_previous = tmp;
			current = tmp;
		}
		ListItem oldTail = m_tail;
		m_tail = m_head;
		m_head = oldTail;
	}

	@Override
	public void addAfter(ListItem item, List<T> list) {
		assert list != null && list != this;
		assert item == null || checkMembership(item);

		if (list.isEmpty()) {
			return;
		}

		if (list instanceof DLinkedList) {
			addAfterDLinkedList(item, (DLinkedList<T>) list);
		} else {
			// iterate reversly to keep ordering
			ListIterator<T> iter = list.listIterator(list.size());
			while (iter.hasPrevious()) {
				addAfter(item, iter.previous());
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
	protected void addAfterDLinkedList(ListItem item, DLinkedList<T> list) {
		// it's necessary to change owner for all items
		DLinkedListIterator iter = list.listIterator();
		while (iter.hasNext()) {
			iter.nextItem().m_owner = this;
		}

		if (item == null) {
			ListItem oldHead = m_head;
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
	public void addBefore(ListItem item, List<T> list) {
		assert list != null && list != this;
		assert item == null || checkMembership(item);

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
	protected void addBeforeDLinkedList(ListItem item, DLinkedList<T> list) {
		DLinkedListIterator iter = list.listIterator();
		while (iter.hasNext()) {
			iter.nextItem().m_owner = this;
		}

		// item is null -> insert list AFTER list (against intuition)
		if (item == null) {
			ListItem oldTail = m_tail;
			m_tail = list.m_tail;
			if (oldTail != null) {
				linkTogether(oldTail, list.m_head);
			} else {
				m_head = list.m_head;
			}
			// item.previous -> insert list BEFORE existing list
		} else if (item == m_head) {
			ListItem oldHead = m_head;
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
	 * Connects first to second, without modifying first.m_previous or second.m_next
	 */
	private void linkTogether(ListItem first, ListItem second) {
		first.m_next = second;
		second.m_previous = first;
	}

	@Override
	public void conc(List<T> list, boolean after) {
		assert list != null && list != this;
		if (after) {
			addAfter(m_tail, list);
		} else {
			addBefore(m_head, list);
		}
	}

	@Override
	public IList<T> remove(ListItem startInclusive, ListItem endExclusive) {
		checkMembershipPrecondition(startInclusive);
		checkMembershipPrecondition(endExclusive);

		DLinkedList<T> removedElements = new DLinkedList<T>();
		ListItem current = startInclusive;
		do {
			ListItem newOne = cyclicNext(current);
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
	private void linkInFront(ListItem item) {
		assert item != null;
		item.m_next = m_head;
		m_head = item;
		if (item.m_next != null) {
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
	private void linkInBack(ListItem item) {
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
	private void linkInAfter(ListItem prev, ListItem item) {
		assert prev != null;
		assert item != null;

		ListItem next = prev.m_next;
		linkTogether(prev, item);
		linkTogether(item, next);
		m_size++;
		modCount++;
	}

	/**
	 * Removes the item from the list. Removes next and prev from item Connect prev
	 * and next together
	 *
	 * @param item
	 */
	private void unlink(ListItem item) {
		assert item != null;

		ListItem previous = item.m_previous;
		ListItem next = item.m_next;

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

	/**
	 * Create an iterator at position index.<br>
	 * <b>Notice:</b> this method performs in O(n).
	 */
	@Override
	public IListIterator<T> listIterator(int index) {
		return new DLinkedListIterator(index);
	}

	@Override
	public DLinkedListIterator listIterator() {
		return new DLinkedListIterator();
	}

	@Override
	public DLinkedListIterator iterator() {
		return new DLinkedListIterator();
	}

	public class DLinkedListIterator implements IListIterator<T> {
		/**
		 * The latest known modCount of the list being iterated by this iterator.
		 */
		private long m_curModCount;

		/**
		 * The last returned item by next or previous. Or null, if next() or previous()
		 * have not been used
		 */
		private ListItem m_returned;

		/**
		 * The next item of this iterator.
		 */
		private ListItem m_next;

		/**
		 * The index of m_next
		 */
		private int m_index;

		/**
		 * Initializes an iterator at head.
		 */
		private DLinkedListIterator() {
			m_next = m_head;
			m_curModCount = DLinkedList.this.modCount;
		}

		/**
		 * Create a new iterator at position index<br>
		 * Performs in worst-case O(n)
		 * 
		 * @param index
		 */
		private DLinkedListIterator(int index) {
			this();
			assert index >= 0 && index <= m_size; // m_size still allowed

			// decide if its faster to go from the front or the back to the requested
			// position
			if (index <= (m_size / 2)) {
				while (nextIndex() != index) {
					nextItem();
				}
			} else {
				m_next = null; // set next to null, which means, the iterator is at the end of the list
				m_index = m_size;
				while (nextIndex() != index) {
					previousItem();
				}
			}
			m_returned = null; // reset m_returned
		}

		private void checkModCount() {
			if (m_curModCount != DLinkedList.this.modCount) {
				throw new ConcurrentModificationException("list was modified outside iterator");
			}
		}

		@Override
		public boolean hasNext() {
			checkModCount();
			return m_next != null;
		}

		@Override
		public boolean hasPrevious() {
			checkModCount();
			return m_next == null ? m_tail != null : m_next.m_previous != null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T next() {
			return (T) nextItem().m_data;
		}

		/**
		 * @return the next element's ListItem
		 */
		public ListItem nextItem() {
			checkModCount();
			if (m_next == null) {
				throw new NoSuchElementException("iterator reached end of list");
			}
			m_returned = m_next;
			m_next = m_returned.m_next;
			m_index++;
			return m_returned;
		}

		/**
		 * @return the previous element's ListItem
		 */
		public ListItem previousItem() {
			checkModCount();
			if (m_next == null) {
				m_returned = m_tail; // in case we reached end of list with next, previous should returned last
				// element of list
			} else {
				m_returned = m_next.m_previous;
			}
			if (m_returned == null) {
				throw new NoSuchElementException("iterator is at the begin of the list");
			}
			m_next = m_returned;
			m_index--;
			return m_returned;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T previous() {
			return (T) previousItem().m_data;
		}

		@Override
		public int nextIndex() {
			checkModCount();
			return m_index;
		}

		@Override
		public int previousIndex() {
			checkModCount();
			return m_index - 1;
		}

		@Override
		public ListItem getVisited() {
			assert m_returned != null;
			checkModCount();
			return m_returned;
		}

		@Override
		public void add(T e) {
			checkModCount();
			DLinkedList.this.addBefore(m_next, e);
			m_curModCount = DLinkedList.this.modCount;
			m_index++;
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
				m_curModCount = DLinkedList.this.modCount;
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
		// AbstractList.toArray, which's performance is good enough
		return super.toArray(a);
	}

	@Override
	public T get(int index) {
		throw new UnsupportedOperationException("cannot be implemented efficiently");
	}

	@Override
	public boolean removeIf(Predicate<? super T> filter) {
		throw new UnsupportedOperationException("cannot be implemented efficiently");
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
	public void sort(Comparator<? super T> c) {
		super.sort(c);
	}

	@Override
	public Spliterator<T> spliterator() {
		return super.spliterator();
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
		super.forEach(action);
	}

}