package ch.fhnw.algd2;

import java.util.List;
import ch.fhnw.algd2.DLinkedList.ListItem;

/**
 * Very helpful linked list operations for lists with public list items.
 * 
 * @author Christoph Stamm
 *
 */
public interface IList<E> extends List<E> {
	/**
	 * @param item
	 * @return true if item is a member of this list
	 */
	boolean checkMembership(ListItem<E> item);
	
	/**
	 * @return the first item or null if the list is empty
	 */
	ListItem<E> head();
	
	/**
	 * @return the last item or null if the list is empty
	 */
	ListItem<E> tail();
	
	/**
	 * Precondition: item is in this list
	 * @param item
	 * @return the successor of item or null if item is the last
	 */
	ListItem<E> next(ListItem<E> item);
	
	/**
	 * Precondition: item is in this list
	 * @param item
	 * @return the predecessor of item or null if item is the first
	 */
	ListItem<E> previous(ListItem<E> item);
	
	/**
	 * Precondition: item is in this list
	 * @param item
	 * @return the cyclic successor of item, if item is the last then the first
	 */
	ListItem<E> cyclicNext(ListItem<E> item);
	
	/**
	 * Precondition: item is in this list
	 * @param item
	 * @return the cyclic predecessor of item, if item is the first then the last
	 */
	ListItem<E> cyclicPrevious(ListItem<E> item);
	
	/**
	 * Deletes item while iterating.
	 * If next = true: returns successor of item or null if item is the last.
	 * If next = false: returns predecessor of item or null if item is the first.
	 * Precondition: item is in this list
	 * @param item to be deleted
	 * @param next controls direction of iteration
	 * @return successor of item while iterating or null
	 */
	ListItem<E> delete(ListItem<E> item, boolean next);
	
	/**
	 * Deletes item while iterating.
	 * If next = true: returns cyclic successor of item or null if item is the only list item.
	 * If next = false: returns cyclic predecessor of item or null if item is the only list item.
	 * Precondition: item is in this list
	 * @param item to be deleted
	 * @param next controls direction of cyclic iteration
	 * @return successor of item while cyclic iterating or null if list becomes empty
	 */
	ListItem<E> cyclicDelete(ListItem<E> item, boolean next);
	
	/**
	 * Precondition: item is in this list
	 * @param item
	 * @return the contents of item
	 */
	E get(ListItem<E> item);
	
	/**
	 * Makes data the contents of item.
	 * Precondition: item is in this list
	 * @param item
	 * @param data
	 */
	void set(ListItem<E> item, E data);
	
	/**
	 * Deletes the item and returns its contents.
	 * Precondition: item is in this list
	 * @param item
	 * @return contents of the removed item
	 */
	E remove(ListItem<E> item);
	
	/**
	 * Adds a new list item with given data at the front and returns it.
	 * @param data
	 * @return new item
	 */
	ListItem<E> addHead(E data);
	
	/**
	 * Appends a new list item with given data and returns it.
	 * @param data
	 * @return appended item
	 */
	ListItem<E> addTail(E data);

	/**
	 * Inserts a new list item with given data after item and returns the new item.
	 * Precondition: item is in this list or null
	 * @param item can be null
	 * @param data
	 * @return inserted item
	 */
	ListItem<E> addAfter(ListItem<E> item, E data);
	
	/**
	 * Inserts a new list item with given data before item and returns the new item.
	 * Precondition: item is in this list or null
	 * @param item can be null
	 * @param data
	 * @return inserted item
	 */
	ListItem<E> addBefore(ListItem<E> item, E data);
	
	/**
	 * Moves item to front. item becomes new head.
	 * Precondition: item is in this list
	 * @param item
	 */
	void moveToHead(ListItem<E> item);
	
	/**
	 * Moves item to back. item becomes new tail.
	 * Precondition: item is in this list
	 * @param item
	 */
	void moveToTail(ListItem<E> item);
	
	/**
	 * Rotates the list until item is the new head in O(1) time.
	 * Precondition: item is in this list
	 * @param item
	 */
	void rotate(ListItem<E> item);

	/**
	 * Swaps two list items. The content of the list items doesn't change.
	 * Precondition: item1 and item2 are in this list
	 * @param item1
	 * @param item2
	 */
	void swap(ListItem<E> item1, ListItem<E> item2);
	
	/**
	 * Reverses the order of all list items. Head becomes tail and vice versa.
	 * Replaces Collections.reverse(), because that method needs either random access
	 * or it just replaces swap data objects and changes data of list items.
	 * Takes O(n) time, where n is the size of the this list. 
	 */
	void reverse();
	
	/**
	 * Inserts list after item in O(n) time, where n is the size of list.
	 * Makes list the empty list;
	 * Precondition: item is in this list or null and list != null and list != this
	 * @param item can be null
	 */
	void addAfter(ListItem<E> item, List<E> list);

	/**
	 * Inserts list before item in O(n) time, where n is the size of list.
	 * Makes list the empty list;
	 * Precondition: item is in this list or null and list != null and list != this
	 * @param item can be null
	 */
	void addBefore(ListItem<E> item, List<E> list);

	/**
	 * Appends (after = true) or prepends (after = false) list to this list in O(n) time 
	 * and makes list the empty list. n is the size of list.
	 * Precondition: list != null and list != this
	 * @param list
	 * @param after
	 */
	void conc(List<E> list, boolean after);
	
	/**
	 * Removes a (cyclic extended) sublist in O(n) time, where n is the size of the returned list.
	 * If startInclusive = endExclusive then this list becomes the empty list.
	 * Precondition: startInclusive and endExclusive are in this list
	 * @param startInclusive first item to be removed
	 * @param endExclusive the item cyclic succeeding the last item to be removed
	 * @return list with removed items with head = startInclusive and tail = last removed
	 */
	IList<E> remove(ListItem<E> startInclusive, ListItem<E> endExclusive);
	
	/**
	 * @return a list-iterator of the elements in this list (in proper sequence).
	 */
	IListIterator<E> listIterator();

	/**
	 * @return a list-iterator of the elements in this list (in proper sequence), starting at the specified position in the list.
	 */
	IListIterator<E> listIterator(int index);	
}
