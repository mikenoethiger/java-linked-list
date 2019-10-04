package ch.fhnw.algd2;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import ch.fhnw.algd2.DLinkedList;
import ch.fhnw.algd2.IList;
import org.junit.Before;
import org.junit.Test;

import ch.fhnw.algd2.DLinkedList.ListItem;

public class DLinkedListTest1 {
	private final int Size = 10000000;
	private IList<Integer> list1;
	private List<Integer> list2;
	
	@Before
	public void setUp() throws Exception {
		list1 = new DLinkedList<>();
		list2 = new LinkedList<>();
	}

	@Test
	public void testAdd() {
		System.out.println("Adding");
		Clock clock = new Clock();
		addElements(list1, Size);
		clock.stop();
		System.out.println(clock);

		clock.start();
		addElements(list2, Size);
		clock.stop();
		System.out.println(clock);
		
		equals();
		
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testNext() {
		System.out.println("Iterating with Next");
		addElements(list1, Size);
		addElements(list2, Size);
		
		Clock clock = new Clock();
		ListItem it = list1.head();
		while(it != null) {
			int i = list1.get(it);
			it = list1.next(it);
		}
		clock.stop();
		System.out.println(clock);

		clock.start();
		ListIterator<Integer> itr = list2.listIterator();
		while(itr.hasNext()) {
			int i = itr.next();
		}
		clock.stop();
		System.out.println(clock);
		
		equals();
	}

	@SuppressWarnings("unused")
	@Test
	public void testCyclicNext() {
		System.out.println("Iterating with CyclicNext");
		addElements(list1, Size);
		addElements(list2, Size);
		
		Clock clock = new Clock();
		final ListItem tail = list1.tail();
		ListItem it = list1.head();
		while(it != tail) {
			int i = list1.get(it);
			it = list1.cyclicNext(it);
		}
		list1.get(it);
		clock.stop();
		System.out.println(clock);

		clock.start();
		ListIterator<Integer> itr = list2.listIterator();
		while(itr.hasNext()) {
			int i = itr.next();
		}
		clock.stop();
		System.out.println(clock);
		
		equals();
	}

	@Test
	public void testRemoveListItem() {
		System.out.println("Removing");
		addElements(list1, Size);
		addElements(list2, Size);
		
		Clock clock = new Clock();
		ListItem it = list1.head();
		while(it != null) {
			ListItem t = list1.next(it);
			list1.remove(it);
			it = t;
		}
		clock.stop();
		System.out.println(clock);

		clock.start();
		ListIterator<Integer> itr = list2.listIterator();
		while(itr.hasNext()) {
			itr.next();
			itr.remove();
		}
		clock.stop();
		System.out.println(clock);
		
		equals();
	}

	@Test
	public void testAddBefore() {
		System.out.println("Inserting");
		addElements(list1, 2);
		addElements(list2, 2);
		
		Clock clock = new Clock();
		final ListItem tail = list1.tail();
		for(int i=0; i < Size; i++) {
			list1.addBefore(tail, i);
		}
		clock.stop();
		System.out.println(clock);
		
		clock.start();
		ListIterator<Integer> itr = list2.listIterator();
		itr.next();
		for(int i=0; i < Size; i++) {
			itr.add(i);
		}
		clock.stop();
		System.out.println(clock);
		
		equals();
	}

	private void addElements(List<Integer> list, int size) {
		for(int i=0; i < size; i++) {
			list.add(i);
		}
	}
	
	private void equals() {
		assertEquals(list2.size(), list1.size());
		assertArrayEquals(list2.toArray(), list1.toArray());
	}
}
