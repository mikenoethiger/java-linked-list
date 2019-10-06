package ch.fhnw.algd2;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.fhnw.algd2.DLinkedList.ListItem;

public class CustomerDLinkedTest {
	private final int Size = 10000000;
	private IList<Integer> list1;
	private List<Integer> list2;

	@Before
	public void setUp() throws Exception {
		list1 = new DLinkedList<>();
		list2 = new LinkedList<>();
	}

	@Test
	public void testOneElementList() {
		list1.add(1);

		assertEquals(Integer.valueOf(1), list1.head().getData());
		assertEquals(Integer.valueOf(1), list1.tail().getData());
	}

	@Test
	public void testMoveToTail() {
		list1.add(1);
		list1.add(2);

		assertEquals(Integer.valueOf(1), list1.head().getData());
		list1.moveToHead(list1.tail());
		assertEquals(Integer.valueOf(2), list1.head().getData());
		assertEquals(Integer.valueOf(1), list1.tail().getData());
	}

	@Test
	public void testBackAndForthListIterator() {
		addElements(list1, Size);

		IListIterator<Integer> iter = list1.listIterator();

		assertEquals(0, iter.nextIndex());
		assertEquals(list1.head().getData(), iter.next());
		assertEquals(0, iter.previousIndex());
		assertEquals(list1.head().getData(), iter.previous());

		// and again
		assertEquals(0, iter.nextIndex());
		assertEquals(list1.head().getData(), iter.next());
		assertEquals(0, iter.previousIndex());
		assertEquals(list1.head().getData(), iter.previous());
	}
	
	@Test
	public void testRemoveSize2() {
		addElements(list1, 2);
		
		list1.remove(list1.head());
		assertEquals(list1.tail(), list1.head());
		assertEquals(null, list1.next(list1.head()));
		assertEquals(null, list1.next(list1.tail()));
		assertEquals(null, list1.previous(list1.head()));
		assertEquals(null, list1.previous(list1.tail()));
	}

	@Test
	public void testRemove() {
		addElements(list1, 1000);

		ListItem<Integer> it = list1.head();
		while (it != null) {
			ListItem<Integer> t = list1.next(it);
			list1.remove(it);
			it = t;
		}
		assertEquals(0, list1.size());
	}

	private void addElements(List<Integer> list, int size) {
		for (int i = 0; i < size; i++) {
			list.add(i);
		}
	}

	private void equals() {
		assertEquals(list2.size(), list1.size());
		assertArrayEquals(list2.toArray(), list1.toArray());
	}
}
