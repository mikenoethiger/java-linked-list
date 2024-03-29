package ch.fhnw.algd2;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.fhnw.algd2.DLinkedList.ListItem;

/**
 * 
 * @author Samuel Keusch, Marco Waldmeier, Mike N�thiger
 *
 */
public class CustomDLinkedListTest {
	private final int TEST_DEFAULT_SIZE = 10000000;
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
	public void testIteratorAtRequestedPosition() {
		addElements(list1, 100);

		assertEquals(Integer.valueOf(0), list1.listIterator(0).next());
		assertEquals(Integer.valueOf(99), list1.listIterator(list1.size()).previous());
		assertEquals(Integer.valueOf(99), list1.listIterator(list1.size() - 1).next());
		assertEquals(Integer.valueOf(11), list1.listIterator(11).next());
		assertEquals(Integer.valueOf(79), list1.listIterator(79).next());
	}

	@Test
	public void testIteratorSet() {
		addElements(list1, 100);

		IListIterator<Integer> iter = list1.listIterator();
		iter.next();
		iter.next();
		iter.set(10);

		assertEquals(Integer.valueOf(0), list1.head().getData());
		assertEquals(Integer.valueOf(10), list1.next(list1.head()).getData());
	}

	@Test
	public void testIteratorRemove() {
		addElements(list1, 100);

		IListIterator<Integer> iter = list1.listIterator();
		iter.next();
		iter.next();
		iter.remove();

		assertEquals(Integer.valueOf(0), list1.head().getData());
		assertEquals(Integer.valueOf(2), list1.next(list1.head()).getData());
	}

	@Test
	public void testIterRemoveEverything() {
		addElements(list1, 100);

		IListIterator<Integer> iter = list1.listIterator();
		while (iter.hasNext()) {
			iter.next();
			iter.remove();
		}

		assertTrue(list1.isEmpty());
	}

	@Test
	public void testRemoveRangeEverything() {
		addElements(list1, 10);

		ListItem middle = list1.next(list1.next(list1.next(list1.head())));
		list1.remove(middle, middle);

		assertTrue(list1.isEmpty());
	}

	@Test(expected = ConcurrentModificationException.class)
	public void testNextModCountIterator() {
		addElements(list1, 100);

		IListIterator<Integer> iter = list1.listIterator();
		iter.next();
		list1.add(100);
		iter.next();
	}

	@Test
	public void testBackAndForthListIterator() {
		addElements(list1, TEST_DEFAULT_SIZE);

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

	@SuppressWarnings("deprecation")
	@Test
	public void testAddBefore() {
		DLinkedList<Integer> list0 = new DLinkedList<>();
		addElements(list0, 10);
		addElements(list1, 10);

		list0.addBefore(null, list1);

		assertEquals(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, list0.toArray());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testAddBeforeArrayList() {
		addElements(list1, 10);
		addElements(list2, 10);

		list1.addBefore(null, list2);

		assertEquals(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, list1.toArray());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testAddAfterArrayList() {
		addElements(list1, 10);
		addElements(list2, 10);

		list1.addAfter(null, list2);

		assertEquals(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, list1.toArray());
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

	@Test(expected = AssertionError.class)
	public void addAfterListForeignListItem() {
		DLinkedList<Integer> list3 = new DLinkedList<>();
		list3.add(424);

		list1.addAfter(list3.head(), list2);
	}

	@Test(expected = AssertionError.class)
	public void addBeforeListForeignListItem() {
		DLinkedList<Integer> list3 = new DLinkedList<>();
		list3.add(424);

		list1.addAfter(list3.head(), list2);
	}

	@Test(expected = AssertionError.class)
	public void testRemoveForeignItem() {
		addElements(list1, 10);

		DLinkedList<Integer> foreignList = new DLinkedList<Integer>();
		foreignList.add(100);

		list1.remove(foreignList.head());
	}

	@Test(expected = AssertionError.class)
	public void testGetForeignItem() {
		addElements(list1, 10);

		DLinkedList<Integer> foreignList = new DLinkedList<Integer>();
		foreignList.add(100);

		list1.get(foreignList.head());
	}

	@Test(expected = AssertionError.class)
	public void testPreviousForeignItem() {
		addElements(list1, 10);

		DLinkedList<Integer> foreignList = new DLinkedList<Integer>();
		foreignList.add(100);

		list1.previous(foreignList.head());
	}

	@Test(expected = AssertionError.class)
	public void testNextForeignItem() {
		addElements(list1, 10);

		DLinkedList<Integer> foreignList = new DLinkedList<Integer>();
		foreignList.add(100);

		list1.next(foreignList.head());
	}

	@Test(expected = AssertionError.class)
	public void testRotateForeignItem() {
		addElements(list1, 10);

		DLinkedList<Integer> foreignList = new DLinkedList<Integer>();
		foreignList.add(100);

		list1.rotate(foreignList.head());
	}

	@Test(expected = AssertionError.class)
	public void testMoveToTailForeignItem() {
		addElements(list1, 10);

		DLinkedList<Integer> foreignList = new DLinkedList<Integer>();
		foreignList.add(100);

		list1.moveToTail(foreignList.head());
	}

	@Test(expected = AssertionError.class)
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

	@Test
	public void testSwapHeadTail() {
		addElements(list1, 2);

		ListItem head = list1.head();
		ListItem tail = list1.tail();
		list1.swap(head, tail);
		assertEquals(tail, list1.head());
		assertEquals(head, list1.tail());
		assertEquals(head, list1.next(list1.head()));
		assertEquals(tail, list1.previous(list1.tail()));
		assertEquals(tail, list1.cyclicNext(list1.tail()));
		assertEquals(head, list1.cyclicPrevious(list1.head()));
	}

	@Test
	public void testSwapInside() {
		addElements(list1, 10);

		ListItem beforeFirst = list1.next(list1.head());
		ListItem first = list1.next(beforeFirst);
		ListItem afterFirst = list1.next(first);
		ListItem afterSecond = list1.previous(list1.tail());
		ListItem second = list1.previous(afterSecond);
		ListItem beforeSecond = list1.previous(second);

		list1.swap(first, second);

		assertEquals(beforeFirst, list1.previous(second));
		assertEquals(afterSecond, list1.next(first));
		assertEquals(afterFirst, list1.next(second));
		assertEquals(beforeSecond, list1.previous(first));
		assertNotEquals(list1.head(), first);
		assertNotEquals(list1.tail(), second);
	}

	@Test
	public void testSwapLeftBorder() {
		addElements(list1, 10);

		ListItem beforeFirst = null;
		ListItem first = list1.head();
		ListItem afterFirst = list1.next(first);
		ListItem afterSecond = list1.previous(list1.tail());
		ListItem second = list1.previous(afterSecond);
		ListItem beforeSecond = list1.previous(second);

		list1.swap(first, second);

		assertEquals(beforeFirst, list1.previous(second));
		assertEquals(afterSecond, list1.next(first));
		assertEquals(afterFirst, list1.next(second));
		assertEquals(beforeSecond, list1.previous(first));
		assertEquals(list1.head(), second);
		assertNotEquals(list1.tail(), second);
		assertNotEquals(list1.tail(), first);
		assertNotEquals(list1.head(), first);
	}

	@Test
	public void testSwapRightBorder() {
		addElements(list1, 10);

		ListItem beforeFirst = list1.next(list1.head());
		ListItem first = list1.next(beforeFirst);
		ListItem afterFirst = list1.next(first);

		ListItem afterSecond = null;
		ListItem second = list1.tail();
		ListItem beforeSecond = list1.previous(second);

		list1.swap(second, first);

		assertEquals(beforeFirst, list1.previous(second));
		assertEquals(afterSecond, list1.next(first));
		assertEquals(afterFirst, list1.next(second));
		assertEquals(beforeSecond, list1.previous(first));
		assertEquals(list1.tail(), first);
		assertNotEquals(list1.head(), first);
		assertNotEquals(list1.head(), second);
		assertNotEquals(list1.tail(), second);
	}

	@Test
	public void testReverseOneElement() {
		addElements(list1, 1);
		addElements(list2, 1);

		list1.reverse();

		equals();
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testReverseStatic() {
		addElements(list1, 5);

		list1.reverse();

		assertEquals(new Integer[] { 4, 3, 2, 1, 0 }, list1.toArray());
	}

	@Test
	public void testReverseReverse() {
		addElements(list1, 100);
		addElements(list2, 100);

		list1.reverse();
		list1.reverse();

		equals();
	}

	@Test
	public void testIteratorIndex() {
		addElements(list1, 3);

		IListIterator<Integer> iter = list1.listIterator();

		assertEquals(0, iter.nextIndex());
		assertEquals(-1, iter.previousIndex());
		iter.next();
		assertEquals(1, iter.nextIndex());
		assertEquals(0, iter.previousIndex());
		iter.next();
		assertEquals(2, iter.nextIndex());
		assertEquals(1, iter.previousIndex());
		iter.previous();
		assertEquals(1, iter.nextIndex());
		assertEquals(0, iter.previousIndex());
		iter.next();
		assertEquals(2, iter.nextIndex());
		assertEquals(1, iter.previousIndex());
		iter.next();
		assertEquals(2, iter.previousIndex());
		assertEquals(list1.size(), iter.nextIndex());
	}

	@Test
	public void testIteratorRemoveIndex() {
		addElements(list1, 10);

		IListIterator<Integer> iter = list1.listIterator();
		iter.next();
		iter.remove();
		assertEquals(0, iter.nextIndex());
	}

	@Test
	public void testIteratorAddIndex() {
		addElements(list1, 10);

		IListIterator<Integer> iter = list1.listIterator();
		iter.next();
		iter.add(235);
		assertEquals(2, iter.nextIndex());
	}

	@Test
	public void testIteratorGetVisited() {
		addElements(list1, 10);

		IListIterator<Integer> iter = list1.listIterator();
		Integer l = iter.next();
		assertEquals(l, iter.getVisited().getData());
		l = iter.next();
		assertEquals(l, iter.getVisited().getData());
		l = iter.previous();
		assertEquals(l, iter.getVisited().getData());
	}

	@Test
	public void testSwapForthAndBack() {
		addElements(list1, 10);

		ListItem first = list1.next(list1.next(list1.head()));
		ListItem second = list1.previous(list1.previous(list1.tail()));

		list1.swap(first, second);
		list1.swap(second, first);

		assertArrayEquals(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, list1.toArray());
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
