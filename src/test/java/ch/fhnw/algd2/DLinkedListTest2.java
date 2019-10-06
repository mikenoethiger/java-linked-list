package ch.fhnw.algd2;

import static org.junit.Assert.*;

import ch.fhnw.algd2.DLinkedList;
import ch.fhnw.algd2.IList;
import ch.fhnw.algd2.DLinkedList.ListItem;
import org.junit.Before;
import org.junit.Test;

public class DLinkedListTest2 {
	private IList<Integer> list1, list2;

	@Before
	public void setUp() throws Exception {
		list1 = new DLinkedList<>();
		list2 = new DLinkedList<>();
		for(int i=1; i <= 3; i++) {
			list1.add(2*i);
			list2.add(2*i);
		}
		int x = 1;
	}

	@Test
	public void testDelete() {
		for(ListItem it=list1.head(); it != null; it = list1.delete(it, true));
		assertTrue(list1.isEmpty());
		
		for(ListItem it=list2.head(); it != null; it = list2.delete(it, true));
		assertTrue(list2.isEmpty());
	}

	@Test
	public void testDelete2() {
		for(ListItem it=list1.tail(); it != null; it = list1.delete(it, false));
		assertTrue(list1.isEmpty());
		
		for(ListItem it=list2.tail(); it != null; it = list2.delete(it, false));
		assertTrue(list2.isEmpty());
	}

	@Test
	public void testCyclicDelete() {
		for(ListItem it=list1.next(list1.head()); it != null; it = list1.cyclicDelete(it, true)) {
			int x = 0; 
		}
		assertTrue(list1.isEmpty());
		
		for(ListItem it=list2.next(list2.head()); it != null; it = list2.cyclicDelete(it, true));
		assertTrue(list2.isEmpty());
	}

	@Test
	public void testCyclicDelete2() {
		for(ListItem it=list1.next(list1.head()); it != null; it = list1.cyclicDelete(it, false));
		assertTrue(list1.isEmpty());
		
		for(ListItem it=list2.next(list2.head()); it != null; it = list2.cyclicDelete(it, false));
		assertTrue(list2.isEmpty());
	}

	@Test
	public void testAddAfterListItem() {
		ListItem it3 = list1.addAfter(list1.head(), 3);
		list1.addAfter(null, 1);
		list1.addAfter(list1.tail(), 7);
		list1.addAfter(list1.next(it3), 5);
		int k = 1;
		for(int i: list1) {
			assertEquals(i,k++);
		}
		list1.clear();
		list1.addAfter(null, 1);
		assertEquals(list1.size(), 1);
		assertEquals((int)list1.get(list1.head()), 1);
		
		it3 = list2.addAfter(list2.head(), 3);
		list2.addAfter(null, 1);
		list2.addAfter(list2.tail(), 7);
		list2.addAfter(list2.next(it3), 5);
		k = 1;
		for(int i: list2) {
			assertEquals(i,k++);
		}
		list2.clear();
		list2.addAfter(null, 1);
		assertEquals(list2.size(), 1);
		assertEquals((int)list2.get(list2.head()), 1);
	}

	@Test
	public void testAddBeforeListItem() {
		ListItem it5 = list1.addBefore(list1.tail(), 5);
		list1.addBefore(null, 7);
		list1.addBefore(list1.head(), 1);
		list1.addBefore(list1.previous(it5), 3);
		int k = 1;
		for(int i: list1) {
			assertEquals(i,k++);
		}
		list1.clear();
		list1.addBefore(null, 1);
		assertEquals(list1.size(), 1);
		assertEquals((int)list1.get(list1.head()), 1);
		
		it5 = list2.addBefore(list2.tail(), 5);
		list2.addBefore(null, 7);
		list2.addBefore(list2.head(), 1);
		list2.addBefore(list2.previous(it5), 3);
		k = 1;
		for(int i: list2) {
			assertEquals(i,k++);
		}
		list2.clear();
		list2.addBefore(null, 1);
		assertEquals(list2.size(), 1);
		assertEquals((int)list2.get(list2.head()), 1);
	}

	@Test
	public void testMoveToHead() {
		list1.moveToHead(list1.head());
		equals();
		list1.moveToHead(list1.tail());
		list1.moveToHead(list1.tail());
		list1.moveToHead(list1.tail());
		equals();
		list1.moveToHead(list1.next(list1.head()));
		list1.moveToHead(list1.next(list1.head()));
		equals();

		list2.moveToHead(list2.head());
		equals();
		list2.moveToHead(list2.tail());
		list2.moveToHead(list2.tail());
		list2.moveToHead(list2.tail());
		equals();
		list2.moveToHead(list2.next(list2.head()));
		list2.moveToHead(list2.next(list2.head()));
		equals();
	}

	@Test
	public void testMoveToTail() {
		list1.moveToTail(list1.tail());
		equals();
		list1.moveToTail(list1.head());
		list1.moveToTail(list1.head());
		list1.moveToTail(list1.head());
		equals();
		list1.moveToTail(list1.previous(list1.tail()));
		list1.moveToTail(list1.previous(list1.tail()));
		equals();

		list2.moveToTail(list2.tail());
		equals();
		list2.moveToTail(list2.head());
		list2.moveToTail(list2.head());
		list2.moveToTail(list2.head());
		equals();
		list2.moveToTail(list2.previous(list2.tail()));
		list2.moveToTail(list2.previous(list2.tail()));
		equals();
	}

	@Test
	public void testRotate() {
		list1.rotate(list1.head());
		equals();
		list1.rotate(list1.next(list1.head()));
		list1.rotate(list1.tail());
		equals();

		list2.rotate(list2.head());
		equals();
		list2.rotate(list2.next(list2.head()));
		list2.rotate(list2.tail());
		equals();
	}

	@Test
	public void testSwapAndReverse() {
		list1.swap(list1.head(), list1.head());
		equals();
		
		list1.swap(list1.head(), list1.tail());
		list1.swap(list1.tail(), list1.head());
		equals();

		list1.swap(list1.head(), list1.next(list1.head()));
		list1.swap(list1.tail(), list1.next(list1.head()));
		list1.swap(list1.head(), list1.next(list1.head()));
		list1.reverse();
		equals();
		
		list2.swap(list2.head(), list2.head());
		equals();
		
		list2.swap(list2.head(), list2.tail());
		list2.swap(list2.tail(), list2.head());
		equals();

		list2.swap(list2.head(), list2.head());
		equals();
		list2.swap(list2.head(), list2.tail());
		list2.swap(list2.tail(), list2.head());
		equals();
		list2.swap(list2.head(), list2.next(list2.head()));
		list2.swap(list2.tail(), list2.next(list2.head()));
		list2.swap(list2.head(), list2.next(list2.head()));
		list2.reverse();
		equals();
		
	}

	@Test
	public void testAddListAfterListItem() {
		final int s = list1.size() + list2.size();
		
		ListItem head = list1.head();
		list1.addAfter(null, list2);
		assertEquals(list1.size(), s);
		assertTrue(list2.isEmpty());
		list2 = list1.remove(list1.head(), head);
		equals();
		
		ListItem tail = list1.tail();
		list1.addAfter(tail, list2);
		assertEquals(list1.size(), s);
		assertTrue(list2.isEmpty());
		list2 = list1.remove(list1.next(tail), list1.head());
		equals();
		
		ListItem prev = list1.previous(list1.tail());
		list1.addAfter(list1.head(), list2);
		assertEquals(list1.size(), s);
		assertTrue(list2.isEmpty());
		list2 = list1.remove(list1.next(list1.head()), prev);
		equals();
		
		try {
			list1.addAfter(null, (IList<Integer>)null);
			fail();
		} catch(Throwable ex) {
		}
		
		head = list2.head();
		list2.addAfter(null, list1);
		assertEquals(list2.size(), s);
		assertTrue(list1.isEmpty());
		list1 = list2.remove(list2.head(), head);
		equals();
		
		tail = list2.tail();
		list2.addAfter(tail, list1);
		assertEquals(list2.size(), s);
		assertTrue(list1.isEmpty());
		list1 = list2.remove(list2.next(tail), list2.head());
		equals();
		
		prev = list2.previous(list2.tail());
		list2.addAfter(list2.head(), list1);
		assertEquals(list2.size(), s);
		assertTrue(list1.isEmpty());
		list1 = list2.remove(list2.next(list2.head()), prev);
		equals();
		
		try {
			list2.addAfter(null, (IList<Integer>)null);
			fail();
		} catch(Throwable ex) {
		}
	}

	@Test
	public void testAddListBeforeListItem() {
		final int s = list1.size() + list2.size();
		
		// expect addBefore(null) to insert at end
		ListItem tail = list1.tail();
		list1.addBefore(null, list2);
		assertEquals(list1.size(), s);
		assertTrue(list2.isEmpty());
		list2 = list1.remove(list1.next(tail), list1.head());
		equals();
		
		ListItem head = list1.head();
		list1.addBefore(head, list2);
		assertEquals(list1.size(), s);
		assertTrue(list2.isEmpty());
		list2 = list1.remove(list1.head(), head);
		equals();
		
		ListItem prev = list1.previous(list1.tail());
		list1.addBefore(prev, list2);
		assertEquals(list1.size(), s);
		assertTrue(list2.isEmpty());
		list2 = list1.remove(list1.next(list1.head()), prev);
		equals();
		
		try {
			list1.addAfter(null, (IList<Integer>)null);
			fail();
		} catch(Throwable ex) {
		}
		
		tail = list2.tail();
		list2.addBefore(null, list1);
		assertEquals(list2.size(), s);
		assertTrue(list1.isEmpty());
		list1 = list2.remove(list2.next(tail), list2.head());
		equals();
		
		head = list2.head();
		list2.addBefore(head, list1);
		assertEquals(list2.size(), s);
		assertTrue(list1.isEmpty());
		list1 = list2.remove(list2.head(), head);
		equals();
		
		prev = list2.previous(list2.tail());
		list2.addBefore(prev, list1);
		assertEquals(list2.size(), s);
		assertTrue(list1.isEmpty());
		list1 = list2.remove(list2.next(list2.head()), prev);
		equals();
		
		try {
			list2.addAfter(null, (IList<Integer>)null);
			fail();
		} catch(Throwable ex) {
		}
	}

	@Test
	public void testConcAndRemove() {
		final int s = list1.size() + list2.size();
		
		ListItem tail = list1.tail();
		list1.conc(list2, true);
		assertEquals(list1.size(), s);
		assertTrue(list2.isEmpty());
		list2 = list1.remove(list1.next(tail), list1.head());
		equals();
		
		ListItem head = list1.head();
		list1.conc(list2, false);
		assertEquals(list1.size(), s);
		assertTrue(list2.isEmpty());
		list2 = list1.remove(list1.head(), head);
		equals();

		try {
			list1.addAfter(null, (IList<Integer>)null);
			fail();
		} catch(Throwable ex) {
		}
		
		tail = list2.tail();
		list2.conc(list1, true);
		assertEquals(list2.size(), s);
		assertTrue(list1.isEmpty());
		list1 = list2.remove(list2.next(tail), list2.head());
		equals();
		
		head = list2.head();
		list2.conc(list1, false);
		assertEquals(list2.size(), s);
		assertTrue(list1.isEmpty());
		list1 = list2.remove(list2.head(), head);
		equals();
		
		try {
			list2.addAfter(null, (IList<Integer>)null);
			fail();
		} catch(Throwable ex) {
		}
	}

	private void equals() {
		assertEquals(list2.size(), list1.size());
		assertArrayEquals(list2.toArray(), list1.toArray());
	}
}
