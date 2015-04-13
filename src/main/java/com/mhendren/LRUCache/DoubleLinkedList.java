package com.mhendren.LRUCache;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/*
 * Created by mhendren on 4/8/2015.
 *
 * While I have since realized that the java.util.LinkedList is a DoubleLinkedList, I am re-inventing this
 * wheel because I need more direct access to the list node. The List nodes will be available for direct implementation
 * which will be useful in the LRU cache as it will manage its own list, as the specific nodes are mapped to a
 * map, which will allow an O(1) access to items in the cache, and list maintenance.
 */

/**
 * DoubleLinkListNode<E> This is a copy of the data of type E with a pointer to the next Node and the previous Node.
 * This is a data structure class mostly only relevant to items in this package.
 *
 * @param <E> This is the type of data that will be stored in the node.
 */

class DoubleLinkedListNode<E> implements Serializable {
    DoubleLinkedListNode(E data) { this.data = data; }
    E data;
    DoubleLinkedListNode<E> prev;
    DoubleLinkedListNode<E> next;

    private static final long serialVersionUID = -2697033429346313531L;
}

/**
 * This is the List. It contains a head (the first DoubleLinkedListNode) and the tail (the final DoubleLinkedList node)
 * and the methods for operating on the list.
 *
 * @param <E>
 */
public class DoubleLinkedList<E> extends AbstractSequentialList<E>  implements List<E>, Deque<E>, Cloneable, Serializable {
    DoubleLinkedListNode<E> head;
    DoubleLinkedListNode<E> tail;
    int nodeCount = 0;
    private int adjustCount = 0;

    /**
     * O(1)
     * adjust() - called to keep track of adjustments made to the list. This is to keep track that the list is not being
     * modified both by an iterator and directly simultaneously. While this method is synchronized to prevent a miscount
     * in the number of adjustments made, the other methods are not.
     */
    private synchronized void adjust() {
        adjustCount++;
    }

    /**
     * O(1)
     * @return The number of elements in the list right now
     */
    @Override
    public int size() {
        return this.nodeCount;
    }

    /**
     * O(1)
     * @return true if there are no elements in the list, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return this.nodeCount == 0;
    }

    /**
     * O(n)
     * @param o - the object to look for in the list
     * @return - true if the object is located in the list, false otherwise
     */
    @Override
    public boolean contains(Object o) {
        if (o == null) {
            for (DoubleLinkedListNode cur = head; cur != null; cur = cur.next) {
                if (cur.data == null) return true;
            }
        } else {
            for (DoubleLinkedListNode cur = head; cur != null; cur = cur.next) {
                if (cur.data.equals(o)) return true;
            }
        }
        return false;
    }

    /**
     * O(1)
     * @return A general Iterator starting at the first item in the list
     */
    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public Iterator iterator() {
        return (Iterator)(new LstIter(0));
    }

    /**
     * The class specific version of a descending iterator. It is just a regular Iterator that starts at the
     * end and has next defined to be previous.
     */
    private class DescIter implements Iterator<E> {
        private LstIter iter = new LstIter(nodeCount);

        /**
         * O(1)
         * @return true if there is a previous element in the list
         */
        @Override
        public boolean hasNext() {
            return iter.hasPrevious();
        }

        /**
         * O(1)
         * @return The previous element
         */
        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            return (E)iter.previous();
        }

        /**
         * O(1)
         * remove the current element from the list
         */
        @Override
        public void remove() {
            iter.remove();
        }
    }

    /**
     * O(1)
     * Create an iterator that starts and the tail, and proceeds in reverse sequential order.
     * @return An Iterator that operates in reverse from the standard iterator
     */
    @Override
    @NotNull
    public Iterator<E> descendingIterator() {
        return new DescIter();
    }

    /**
     * O(n)
     * Create an array from the elements in the list.
     * @return An array of object which are the elements of the list
     */
    @Override
    @NotNull
    public Object[] toArray() {
        Object[] array = new Object[nodeCount];
        int idx = 0;
        for(DoubleLinkedListNode cur = head; cur != null; cur = cur.next)
            array[idx++] = cur.data;
        return array;
    }

    /**
     * O(n)
     * Create an array from an existing array containing the elements of the list
     * @param a An existing array of elements
     * @param <T> The type of elements in the existing array
     * @return The array of elements filled in with the values from the list. It will add space if there was not
     *   enough space present in the incoming list.
     */
    @Override
    @SuppressWarnings("unchecked")
    @NotNull
    public <T> T[] toArray(@NotNull T[] a) {
        // This implementation pretty much goes directly with java.util.LinkedList
        // This is because I wasn't sure what the parameter did, and How to grow a
        // generic array.
        if (a.length < nodeCount) {
            a = (T[])java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), nodeCount);
        }
        Object[] array = a;
        int idx = 0;
        for(DoubleLinkedListNode cur = head; cur != null; cur = cur.next)
            array[idx++] = cur.data;
        if (a.length > nodeCount)
            array[nodeCount] = null;

        return a;
    }

    /**
     * O(1)
     * Add an element t othe start of the list
     * @param e An element to add to the front of the list.
     */
    @Override
    public void addFirst(E e) {
        add(0, e);
    }

    /**
     * O(1)
     * Add an element to the end of the list
     * @param e An element to add to the end of the list.
     */
    @Override
    public void addLast(E e) {
        add(e);
    }

    /**
     * O(1)
     * Offer to put an element at the start of the list. This is not a managed sized list, so this always succeeds.
     * @param e An element to add to the front of the list.
     * @return true is the list was modified (always true).
     */
    @Override
    public boolean offerFirst(E e) {
        add(0, e);
        return true;
    }

    /**
     * O(1)
     * Offer to put an element at the end of the list. This is not a managed sized list, so this always succeeds.
     * @param e An element to add to the tail of the list
     * @return truw if the list was modified (always true).
     */
    @Override
    public boolean offerLast(E e) {
        add(e);
        return true;
    }

    /**
     * O(1)
     * Remove the first element from the list
     * @return The element that was removed from the list, null if there was nothing in the list.
     */
    @Override
    public E removeFirst() {
        if (head != null) {
            DoubleLinkedListNode<E> node = head;
            if (head.next != null) head.next.prev = null; else tail = null;
            head = head.next;
            nodeCount--;
            adjust();
            return node.data;
        }
        return null;
    }

    /**
     * O(1)
     * Remove the last element from the list.
     * @return The element that was removed from the list, null if there was nothing in the list.
     */
    @Override
    public E removeLast() {
        if (tail != null) {
            DoubleLinkedListNode<E> node = tail;
            if(tail.prev != null) tail.prev.next = null; else head = null;
            tail = tail.prev;
            nodeCount--;
            adjust();
            return node.data;
        }
        return null;
    }

    /**
     * O(1)
     * Return and remove the first element from the list
     * @return The first element in the list, null if the list was empty.
     */
    @Override
    public E pollFirst() {
        if (head == null) {
            return null;
        }
        return removeFirst();
    }

    /**
     * O(1)
     * Return and remove the last element from the list.
     * @return The last element in the list, null if the list was empty.
     */
    @Override
    public E pollLast() {
        if (tail == null) {
            return null;
        }
        return removeLast();
    }

    /**
     * O(1)
     * Return the first element in the list.
     * @return The first element in the list. Throws an exception if the list was empty.
     */
    @Override
    public E getFirst() {
        if (head == null) {
            throw new NoSuchElementException("The list does not have a first element");
        }
        return head.data;
    }

    /**
     * O(1)
     * Return the last element in the list.
     * @return The last element in the list. Throws an exception if the list was empty.
     */
    @Override
    public E getLast() {
        if(tail == null) {
            throw new NoSuchElementException("The list does not have a last element");
        }
        return tail.data;
    }

    /**
     * O(1)
     * Look at the first element in the list, but do not modify the list.
     * @return The first element in the list, null if the list is empty.
     */
    @Override
    public E peekFirst() {
        return head != null ? head.data : null;
    }

    /**
     * O(1)
     * Look at the last element in the list, but do not modify the list.
     * @return The last element in the list, null if the list was empty.
     */
    @Override
    public E peekLast() {
        return tail != null ? tail.data : null;
    }

    /**
     * O(n)
     * Find and remove the first occurrence of the specified element from the list. This starts at the head
     * and proceeds through the list in sequence until a value equal to the specified value is located.
     * @param o The element to search for and remove
     * @return true if the list was modified (the element was found in the list), false otherwise.
     */
    @Override
    public boolean removeFirstOccurrence(Object o) {
        for(DoubleLinkedListNode<E> cur = head; cur != null; cur = cur.next) {
            if ((o == null && cur.data == null) || cur.data.equals(o)) {
                if (cur.prev != null) {cur.prev.next = cur.next;} else {head = cur.next;}
                if (cur.next != null) {cur.next.prev = cur.prev;} else {tail = cur.prev;}
                nodeCount--;
                adjust();
                return true;
            }
        }
        return false;
    }


    /**
     * O(n)
     * Starting from the end of the list, proceed in reverse sequential order until an element with the value
     * specified appears in the list, and remove that value from the list.
     * @param o The value to look for from the end of the list to the start.
     * @return true if the list was modified (the element was found in the list), false otherwise.
     */
    @Override
    public boolean removeLastOccurrence(Object o) {
        for(DoubleLinkedListNode<E> cur = tail; cur != null; cur = cur.prev) {
            if ((o == null && cur.data == null) || cur.data.equals(o)) {
                if (cur.prev != null) {cur.prev.next = cur.next;} else {head = cur.next;}
                if (cur.next != null) {cur.next.prev = cur.prev;} else {tail = cur.prev;}
                nodeCount--;
                adjust();
                return true;
            }
        }
        return false;
    }

    /**
     * O(1)
     * Add an element to the end of the list.
     * @param o The element to add to the list.
     * @return true if the list was modified (always true).
     */
    @Override
    public boolean add(E o) {
        DoubleLinkedListNode<E> newNode = new DoubleLinkedListNode<E>(o);
        if (tail != null) { tail.next = newNode; }
        newNode.next = null;
        newNode.prev = tail;
        tail = newNode;
        if (head == null) head = newNode;
        nodeCount++;
        adjust();
        return true;
    }

    /**
     * O(1)
     * Offer to add an element to the list. This is not a managed sized list, so this will always succeed.
     * @param e The element to add to the list.
     * @return true if the list was modified (always true).
     */
    @Override
    public boolean offer(E e) {
        return add(e);
    }

    /**
     * O(1)
     * Remove the first element from the list.
     * @return The element that was removed from the list.
     */
    @Override
    public E remove() {
        return removeFirst();
    }

    /**
     * O(1)
     * Take the first element from the list. It is removed from the list.
     * @return The first element that was in the list.
     */
    @Override
    public E poll() {
        return removeFirst();
    }

    /**
     * O(1)
     * The first element in the list. The list will be unchanged.
     * @return The first element in the list.
     */
    @Override
    public E element() {
        return head != null ? head.data : null;
    }

    /**
     * O(1)
     * Look at the first element of the list. The list will be unchanged.
     * @return The first element in the list.
     */
    @Override
    public E peek() {
        return head != null ? head.data : null;
    }

    /**
     * O(1)
     * Add an element to the front of the list (push element on the top of the stack).
     * @param e The element to add to the list.
     */
    @Override
    public void push(E e) {
        addFirst(e);
    }

    /**
     * O(1)
     * Take the first element from the list (pop the top element from the stack).
     * @return The first element that was in the list.
     */
    @Override
    public E pop() {
        return removeFirst();
    }

    private void removeNode(DoubleLinkedListNode<E> node) {
        if (node.prev != null) node.prev.next = node.next; else head = node.next;
        if (node.next != null) node.next.prev = node.prev; else tail = node.prev;
        nodeCount--;
        adjust();
    }

    /**
     * O(n)
     * Remove the element in the list with the specified element.
     * @param o The element to look for in the list.
     * @return true if the list was modified, false otherwise
     */
    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (DoubleLinkedListNode<E> cur = head; cur != null; cur = cur.next) {
                if (cur.data == null) {
                    removeNode(cur);
                    return true;
                }
            }
        } else {
            for (DoubleLinkedListNode<E> cur = head; cur != null; cur = cur.next) {
                if (cur.data.equals(o)) {
                    removeNode(cur);
                    return true;
                }
            }
        }
        return false;
    }

    private DoubleLinkedListNode<E> newListFromCollection(Collection<E> c) {
        DoubleLinkedListNode<E> start = null;
        DoubleLinkedListNode<E> cur = null;
        if (c != null) {
            for (E data : c) {
                DoubleLinkedListNode<E> newNode = new DoubleLinkedListNode<E>(data);
                if (cur == null) {
                    start = newNode;
                    cur = newNode;
                } else {
                    cur.next = newNode;
                    newNode.prev = cur;
                    cur = newNode;
                }
            }
        }
        return start;
    }

    private DoubleLinkedListNode<E> findEndOfSubList(DoubleLinkedListNode<E> start) {
        DoubleLinkedListNode<E> cur = start;
        while(cur != null && cur.next != null) {
            cur = cur.next;
        }
        return cur;
    }

    /**
     * O(n(c))
     * Add all of the elements in the specified collection to the list
     * @param c The collection of element to be added to the list.
     * @return true if the list was modified (there were elements in the collection).
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean addAll(@NotNull Collection c) {
        DoubleLinkedListNode<E> newList = newListFromCollection(c);
        if(newList == null) return false;
        newList.prev = tail;
        if(tail == null) { head = newList; } else { tail.next = newList; }
        tail = findEndOfSubList(newList);
        nodeCount += c.size();
        adjust();
        return true;
    }

    /**
     * Add all off the elements in the collection to the list at the specified index
     * O(n/2) + O(n(c))
     * @param index The point in the list to to start adding the elements.
     * @param c The collection of elements to be added to the list.
     * @return true if the list was modified (there were elements in the collection).
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean addAll(int index, @NotNull Collection c) {
        checkIndex(index, 1);
        if(index == nodeCount) return addAll(c);
        DoubleLinkedListNode<E> newList = newListFromCollection((Collection<E>)c);
        if (newList == null) return false;
        DoubleLinkedListNode<E> cur = findIndex(index);
        DoubleLinkedListNode<E> end = findEndOfSubList(newList);
        end.next = cur;
        if(cur.prev != null) {cur.prev.next = newList;} else {head = newList;}
        newList.prev = cur.prev;
        cur.prev = end;
        nodeCount += c.size();
        adjust();
        return true;
    }

    /**
     * O(1)
     * Remove everything (of importance) from the list.
     */
    @Override
    public void clear() {
        head = null;
        tail = null;
        nodeCount = 0;
        adjust();
    }

    private void checkIndex(int index, int adjust) throws IndexOutOfBoundsException {
        if (index >= (nodeCount + adjust) || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + nodeCount);
        }
    }

    private void checkIndex(int index) {
        checkIndex(index, 0);
    }

    private DoubleLinkedListNode<E> findIndex(int index) {
        DoubleLinkedListNode<E> cur;
        if (index >= (nodeCount >> 1)) {
            cur = tail;
            for (int i = nodeCount - 1; i > index; i--) {
                cur = cur.prev;
            }
        } else {
            cur = head;
            for (int i = 0; i < index; i++) {
                cur = cur.next;
            }
        }
        return cur;
    }

    /**
     * O(n/2)
     * Return the element at the specified index in the list.
     * @param index The position in the list to get the element from.
     * @return The element that was at the specified position in the list.
     */
    @Override
    public E get(int index) {
        checkIndex(index);
        DoubleLinkedListNode<E> cur = findIndex(index);
        return cur.data;
    }

    /**
     * O(n/2)
     * Set (Overwrite) the contents at the specfified index in the list.
     * @param index The index position of the list where to make the change.
     * @param element The new element to store at the position.
     * @return The former contents of the list that were replaced by this set.
     */
    @Override
    public E set(int index, E element) {
        checkIndex(index);
        DoubleLinkedListNode<E> cur = findIndex(index);
        E data = cur.data;
        cur.data = element;
        adjust();
        return data;
    }

    /**
     * O(n/2)
     * Add the element at the specified location in the list.
     * @param index The position in the list where the element will be inserted.
     * @param element The element that will be inserted into the list.
     */
    @Override
    public void add(int index, E element) {
        // can actually add at the current nodeCount position
        checkIndex(index, 1);

        if (index == nodeCount) add(element);
        else {
            DoubleLinkedListNode<E> newNode = new DoubleLinkedListNode<E>(element);
            DoubleLinkedListNode<E> cur = findIndex(index);
            if (cur.prev != null) { cur.prev.next = newNode; } else { head = newNode; }
            newNode.next = cur;
            newNode.prev = cur.prev;
            cur.prev = newNode;
            nodeCount++;
            adjust();
        }
    }

    /**
     * O(n/2)
     * Remove the element at the specified index from the list.
     * @param index The position in the list to remove the element from.
     * @return The element that was removed from the list.
     */
    @Override
    public E remove(int index) {
        checkIndex(index);
        DoubleLinkedListNode<E> cur = findIndex(index);
        if (cur.next != null) { cur.next.prev = cur.prev; } else { tail = cur.prev; }
        if (cur.prev != null) { cur.prev.next = cur.next; } else { head = cur.next; }
        nodeCount--;
        adjust();
        return cur.data;
    }

    /**
     * O(n)
     * Find the index position in the list where the specified value is located.
     * @param o The element to look for in the list.
     * @return The position in the list that the element first occurs at, -1 if it is not present.
     */
    @Override
    public int indexOf(Object o) {
        int index = 0;
        if (o == null) {
            for (DoubleLinkedListNode cur = head; cur != null; cur = cur.next) {
                if (cur.data == null) return index;
                index++;
            }
        } else {
            for (DoubleLinkedListNode cur = head; cur != null; cur = cur.next) {
                if (cur.data.equals(o)) return index;
                index++;
            }
        }
        return -1;
    }

    /**
     * O(n)
     * Find the index position in the list where the last occurrence of the specified value is located.
     * @param o The element to look for in the list.
     * @return The position in the list where the final occurrence of the element is located, -1 if it is not present.
     */
    @Override
    public int lastIndexOf(Object o) {
       int index = nodeCount - 1;
        if (o == null) {
            for(DoubleLinkedListNode cur = tail; cur != null; cur = cur.prev) {
                if (cur.data == null) return index;
                index--;
            }
        } else {
            for(DoubleLinkedListNode cur = tail; cur != null; cur = cur.prev) {
                if (cur.data.equals(o)) return index;
                index--;
            }
        }
        return -1;
    }


    private class LstIter implements ListIterator {
        DoubleLinkedListNode<E> nextNode;
        int index = nodeCount;
        int expectedAdjustCount = adjustCount;

        /**
         * O(n/2) - note: O(1) for head, or tail
         * @param index The position in the list to start the iterator (usually head or tail)
         */
        @SuppressWarnings("unchecked")
        LstIter(int index) {
            if (index < 0 || index > nodeCount) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + nodeCount);
            }
            DoubleLinkedListNode<E> cur = null;
            if (index != nodeCount) {
                if (index >= (nodeCount >> 1)) {
                    while(this.index > index) {
                        cur = cur == null ? tail : cur.prev;
                        this.index--;
                    }
                } else {
                    this.index = 0;
                    for (cur = head; this.index < index; cur = cur.next) {
                        this.index++;
                    }
                }
            }
            nextNode = cur;
        }

        // Quick check for valid state, the list shouldn't have changed while the iterator is operational
        void checkValidState() {
            if (adjustCount != expectedAdjustCount) {
                throw new ConcurrentModificationException("Accessing iterator with List state invalid.");
            }
        }

        /**
         * O(1)
         * Determine if the is another element in the iterator after the current one.
         * @return true is there is more in the iterator available.
         */
        @Override
        public boolean hasNext() {
            return index < nodeCount;
        }

        /**
         * O(1)
         * Move the iterator to the next element.
         * @return The next element.
         */
        @Override
        public Object next() {
            checkValidState();
            if (index == nodeCount) {
                throw new NoSuchElementException("Attempting to access element beyond last element in list");
            }
            Object data = nextNode.data;
            nextNode = nextNode.next;
            index++;
            return data;
        }

        /**
         * Determine if the iterator can move backward through the list
         * @return true if this is not the first element of the list, false if it is.
         */
        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        /**
         * Move the iterator to the previous element.
         * @return The previous element.
         */
        @Override
        public Object previous() {
            checkValidState();
            if (index == 0) {
                throw new NoSuchElementException("Attempting to access element before th first element in list");
            }
            if (nextNode == null) {
                nextNode = tail;
            } else {
                nextNode = nextNode.prev;
            }
            index--;
            return nextNode.data;
        }

        /**
         * O(1)
         * Get the position of the iterator in the list
         * @return the index value of the iterators current position.
         */
        @Override
        public int nextIndex() {
            return index;
        }

        /**
         * O(1)
         * Get the position of the previous element to the iterators current position in the list
         * @return The index value of the previous element in the list.
         */
        @Override
        public int previousIndex() {
            return index-1;
        }

        /**
         * O(1)
         * Remove the element at the iterators current position in the list.
         */
        @Override
        public void remove() {
            checkValidState();
            if (nextNode.prev != null) { nextNode.prev.next = nextNode.next; } else { head = nextNode.next; }
            if (nextNode.next != null) { nextNode.next.prev = nextNode.prev; } else { tail = nextNode.prev; }
            nextNode = nextNode.next;
            nodeCount--;
            adjust();
            expectedAdjustCount++;
        }

        /**
         * O(1)
         * Set the element at the iterator current position in the list to the specified value.
         * @param o The value to set the iterator current value to.
         */
        @SuppressWarnings("unchecked")
        public void set(Object o) {
            checkValidState();
            if (index == nodeCount) {
                throw new IndexOutOfBoundsException("Attempted to set value after last element in list");
            }
            adjust();
            expectedAdjustCount++;
            nextNode.data = (E)o;
        }

        /**
         * O(1)
         * Insert an element into the list at the iterators current position
         * @param o The element to add into the list.
         */
        @SuppressWarnings("unchecked")
        @Override
        public void add(Object o) {
            checkValidState();
            DoubleLinkedListNode<E> newNode = new DoubleLinkedListNode<E>((E) o);
            if (nextNode != null) {
                newNode.prev = nextNode.prev;
                nextNode.prev = newNode;
            } else {
                newNode.prev = tail;
                if (tail != null) tail.next = newNode;
                tail = newNode;
            }
            newNode.next = nextNode;
            if (newNode.prev != null) {
                newNode.prev.next = newNode;
            } else {
                head = newNode;
            }
            index++;
            nodeCount++;
            adjust();
            expectedAdjustCount++;
        }
    }

    /**
     * O(1)
     * Get a list iterator pointing at the start of the list.
     * @return a new list iterator starting at the head position of the list.
     */
    @SuppressWarnings("unchecked")
    @Override
    @NotNull
    public ListIterator listIterator() {
        return new LstIter(0);
    }

    /**
     * O(n/2)
     * Get a list iterator pointing at an arbitrary index in the list
     * @param index The index position to start the iterator at.
     * @return A new list iterator.
     */
    @SuppressWarnings("unchecked")
    @Override
    @NotNull
    public ListIterator listIterator(int index) {
        return new LstIter(index);
    }

    private List<E> subListFromTail(int start, int stop) {
        DoubleLinkedListNode<E> cur = tail;
        DoubleLinkedList<E> out = new DoubleLinkedList<E>();
        int index = 0;
        while (cur != null) {
            if (index >= start) {
                out.add(0, cur.data);
            }
            cur = cur.prev;
            index++;
            if (index > stop) break;
        }
        return out;
    }

    /**
     * O(n)
     * Generate a sub list of the elements from the fromIndex to the toIndex
     * @param fromIndex The start point of the sub list
     * @param toIndex The end point of the sub list
     * @return A list containing the elements in the list starting at index fromIndex to toIndex
     * @throws IndexOutOfBoundsException
     */
    @Override
    @SuppressWarnings("Unchecked")
    @NotNull
    public List<E> subList(int fromIndex, int toIndex) throws IndexOutOfBoundsException {
        checkIndex(fromIndex);
        checkIndex(toIndex);
        if (toIndex < fromIndex)
            throw new IllegalArgumentException("fromIndex: " + fromIndex + " > toIndex: " + toIndex);
        if (nodeCount - toIndex <= fromIndex)
            return subListFromTail(nodeCount - toIndex - 1, nodeCount - fromIndex - 1);

        DoubleLinkedListNode<E> cur = head;
        DoubleLinkedList<E> out = new DoubleLinkedList<E>();
        int index = 0;
        while(cur != null) {
            if (index >= fromIndex) {
                out.add(cur.data);
            }
            cur = cur.next;
            index++;
            if (index > toIndex) break;
        }
        return out;
    }

    /**
     * O(n)
     * Retain all of the element in the specified collection in the list
     * @param c The collection of elements to spare.
     * @return true if the list was modified (other elements not in the collection are removed).
     */
    @Override
    public boolean retainAll(@NotNull Collection c) {
        boolean adjust = false;
        for(DoubleLinkedListNode<E> cur = head; cur != null; cur = cur.next) {
            if(!c.contains(cur.data)) {
                if (cur.prev != null) { cur.prev.next = cur.next; } else { head = cur.next; }
                if (cur.next != null) { cur.next.prev = cur.prev; } else { tail = cur.prev; }
                nodeCount--;
                adjust = true;
            }
        }
        if(adjust) adjust();
        return adjust;
    }

    /**
     * O(n)
     * Remove all the elements specified in the collection
     * @param c The collection of elements to remove if they are present.
     * @return true if the list was modified, false if no elements from the collection are in the list.
     */
    @Override
    public boolean removeAll(@NotNull Collection c) {
        boolean adjust = false;
        for(DoubleLinkedListNode<E> cur = head; cur != null; cur = cur.next) {
            if(c.contains(cur.data)) {
                if (cur.prev != null) { cur.prev.next = cur.next; } else { head = cur.next; }
                if (cur.next != null) { cur.next.prev = cur.prev; } else { tail = cur.prev; }
                nodeCount--;
                adjust = true;
            }
        }
        if(adjust) adjust();
        return adjust;
    }

    /**
     * O(n)*O(n(c))
     * Determine if all of the elements in the collection are also present in the list.
     * @param c The collection of elements to look for in the list
     * @return true is all of the elements in the collection are in the list, false if an element in the collection
     * is not in the list.
     */
    @Override
    public boolean containsAll(@NotNull Collection c) {
        for(Object data : c) {
            if(!contains(data)) return false;
        }
        return true;
    }

    /**
     * Make a clone of the list, that still supports all of the linked list functionality. This is a shallow copy.
     * @return A new linked list that contains the same elements as in this list.
     * @throws CloneNotSupportedException
     */
    @SuppressWarnings("unchecked")
    public Object clone() throws CloneNotSupportedException {
        DoubleLinkedList<E> myClone = (DoubleLinkedList<E>)super.clone();
        myClone.clear();
        for(DoubleLinkedListNode<E> cur = this.head; cur != null; cur = cur.next) {
            myClone.add(cur.data);
        }
        return myClone;
    }

    private static final long serialVersionUID = 3965362453216587354L;

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(nodeCount);
        for(DoubleLinkedListNode<E> cur = head; cur != null; cur = cur.next) {
             out.writeObject(cur.data);
        }
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        int nodeCount = in.readInt();
        this.clear();
        for(int i = 0; i < nodeCount; i++) {
            add((E)in.readObject());
        }
    }
}
