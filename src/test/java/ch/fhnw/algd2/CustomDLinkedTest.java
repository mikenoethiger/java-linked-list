package ch.fhnw.algd2;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import ch.fhnw.algd2.DLinkedList.ListItem;

public class CustomDLinkedTest {
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

		ListItem it = list1.head();
		while (it != null) {
			ListItem t = list1.next(it);
			list1.remove(it);
			it = t;
		}
		assertEquals(0, list1.size());
	}

	@Test(expected = NoSuchElementException.class)
	public void testRemoveForeignItem() {
		addElements(list1, 10);

		DLinkedList<Integer> foreignList = new DLinkedList<Integer>();
		foreignList.add(100);

		list1.remove(foreignList.head());
	}

	@Test(expected = NoSuchElementException.class)
	public void testGetForeignItem() {
		addElements(list1, 10);

		DLinkedList<Integer> foreignList = new DLinkedList<Integer>();
		foreignList.add(100);

		list1.get(foreignList.head());
	}

	@Test(expected = NoSuchElementException.class)
	public void testPreviousForeignItem() {
		addElements(list1, 10);

		DLinkedList<Integer> foreignList = new DLinkedList<Integer>();
		foreignList.add(100);

		list1.previous(foreignList.head());
	}

	@Test(expected = NoSuchElementException.class)
	public void testNextForeignItem() {
		addElements(list1, 10);

		DLinkedList<Integer> foreignList = new DLinkedList<Integer>();
		foreignList.add(100);

		list1.next(foreignList.head());
	}

	@Test(expected = NoSuchElementException.class)
	public void testRotateForeignItem() {
		addElements(list1, 10);

		DLinkedList<Integer> foreignList = new DLinkedList<Integer>();
		foreignList.add(100);

		list1.rotate(foreignList.head());
	}

	@Test(expected = NoSuchElementException.class)
	public void testMoveToTailForeignItem() {
		addElements(list1, 10);

		DLinkedList<Integer> foreignList = new DLinkedList<Integer>();
		foreignList.add(100);

		list1.moveToTail(foreignList.head());
	}

	@Test(expected = NoSuchElementException.class)
	public void testMoveToHeadForeignItem() {
		addElements(list1, 10);

		DLinkedList<Integer> foreignList = new DLinkedList<Integer>();
		foreignList.add(100);

		list1.moveToHead(foreignList.head());
	}

	@Test(expected = ConcurrentModificationException.class)
	public void testRemoveWhileIterating() {
		addElements(list1, 1_000);

		Iterator<Integer> iter = list1.iterator();
		iter.next();
		iter.next();
		iter.next();
		list1.remove(list1.head());
		iter.next();
	}

	@Test(expected = ConcurrentModificationException.class)
	public void testAddWhileIterating() {
		addElements(list1, 1_000);

		Iterator<Integer> iter = list1.iterator();
		iter.next();
		iter.next();
		iter.next();
		list1.add(34);
		iter.next();
	}

	@Test(expected = ConcurrentModificationException.class)
	public void testMoveToHeadWhileIterating() {
		addElements(list1, 1_000);

		Iterator<Integer> iter = list1.iterator();
		iter.next();
		iter.next();
		iter.next();
		list1.moveToHead(list1.tail());
		iter.next();
	}

	@Test(expected = ConcurrentModificationException.class)
	public void testMoveToTailWhileIterating() {
		addElements(list1, 1_000);

		Iterator<Integer> iter = list1.iterator();
		iter.next();
		iter.next();
		iter.next();
		list1.moveToTail(list1.head());
		iter.next();
	}

	@Test(expected = ConcurrentModificationException.class)
	public void testRotateWhileIterating() {
		addElements(list1, 1_000);

		Iterator<Integer> iter = list1.iterator();
		iter.next();
		iter.next();
		iter.next();
		list1.rotate(list1.next(list1.next(list1.head())));
		iter.next();
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
