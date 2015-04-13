package com.mhendren.LRUCache;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class DoubleLinkedListTest {
    DoubleLinkedList<Integer> stdDblLnkLst = new DoubleLinkedList<Integer>();
    List<Integer> stdList = stdDblLnkLst;
    Deque<Integer> stdDeque = stdDblLnkLst;

    @Before
    public void beforeEach() throws Exception {
        stdDblLnkLst.add(4);
        stdDblLnkLst.add(5);
        stdDblLnkLst.add(6);
    }

    @Test
    public void testSize() throws Exception {
        List<Integer> list = new DoubleLinkedList<Integer>();
        assertThat(list.size(), is(equalTo(0)));
        assertThat(stdList.size(), is(equalTo(3)));
    }

    @Test
    public void testIsEmpty() throws Exception {
        List<Integer> list = new DoubleLinkedList<Integer>();
        assertThat(list.isEmpty(), is(true));
        assertThat(stdList.isEmpty(), is(false));
    }

    @Test
    public void testContains() throws Exception {
        assertThat(stdList.contains(2), is(false));
        assertThat(stdList.contains(6), is(true));
    }

    @Test
    public void testContainsNull() throws Exception {
        stdList.add(null);
        assertThat(stdList.contains(null), is(true));
    }

    @Test
    public void testAdd() throws Exception {
        stdList.add(10);
        assertThat(stdList.contains(10), is(true));
    }

    @Test
    public void testRemove() throws Exception {
        assertThat(stdList.remove((Integer) 5), is(true));
        assertThat(stdList.contains(6), is(true));
        assertThat(stdList.contains(5), is(false));
    }

    @Test
    public void testRemoveObjectNull() throws Exception {
        stdList.add(null);
        stdList.add(7);
        assertThat(stdList.remove(null), is(true));
        assertThat(stdList.size(), is(equalTo(4)));
        assertThat(stdList.get(0), is(equalTo(4)));
        assertThat(stdList.get(1), is(equalTo(5)));
        assertThat(stdList.get(2), is(equalTo(6)));
        assertThat(stdList.get(3), is(equalTo(7)));
    }

    @Test
    public void testRemoveNonExistant() throws Exception {
        assertThat(stdList.remove((Integer) 8), is(false));
    }

    @Test
    public void testClear() throws Exception {
        stdList.clear();
        assertThat(stdList.size(), is(equalTo(0)));
    }

    @Test
    public void testGet() throws Exception {
        assertThat(stdList.get(0), is(equalTo(4)));
        assertThat(stdList.get(1), is(equalTo(5)));
        assertThat(stdList.get(2), is(equalTo(6)));
    }

    @Test
    public void testSet() throws Exception {
        Integer x = stdList.set(0, 19);
        assertThat(stdList.get(0), is(equalTo(19)));
        assertThat(x, is(equalTo(4)));
    }

    @Test
    public void testAdd1() throws Exception {
        stdList.add(1, 17);
        assertThat(stdList.get(1), is(equalTo(17)));
        assertThat(stdList.get(2), is(equalTo(5)));
    }

    @Test
    public void testRemove1() throws Exception {
        assertThat(stdList.remove(stdList.size() - 1), is(equalTo(6)));
        assertThat(stdList.size(), is(equalTo(2)));
        assertThat(stdList.get(0), is(equalTo(4)));
        assertThat(stdList.get(1), is(equalTo(5)));
    }

    @Test
    public void testIndexOf() throws Exception {
        assertThat(stdList.indexOf(6), is(equalTo(2)));
        assertThat(stdList.indexOf(5), is(equalTo(1)));
        assertThat(stdList.indexOf(4), is(equalTo(0)));
        assertThat(stdList.indexOf(100), is(equalTo(-1)));
    }

    @Test
    public void testIndexOfNull() throws Exception {
        stdList.add(null);
        assertThat(stdList.indexOf(null), is(equalTo(3)));
    }

    @Test
    public void testIndexOfNotThere() throws Exception {
        assertThat(stdList.indexOf(8), is(equalTo(-1)));
    }

    @Test
    public void testLastIndexOf() throws Exception {
        stdList.add(5);
        assertThat(stdList.lastIndexOf(5), is(equalTo(3)));
        assertThat(stdList.lastIndexOf(4), is(equalTo(0)));
    }

    @Test
    public void testLastIndexOfNull() throws Exception {
        stdList.add(null);
        stdList.add(8);
        assertThat(stdList.lastIndexOf(null), is(equalTo(3)));
    }

    @Test
    public void testLastIndexOfNotThere() throws Exception {
        assertThat(stdList.lastIndexOf(8), is(equalTo(-1)));
    }

    @Test
    public void testSubList() throws Exception {
        stdList.add(7);
        stdList.add(8);
        stdList.add(9);
        List<Integer> list = stdList.subList(1, 3);
        assertThat(list.size(), is(equalTo(3)));
        assertThat(list.get(0), is(equalTo(5)));
        assertThat(list.get(1), is(equalTo(6)));
        assertThat(list.get(2), is(equalTo(7)));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSubListBadFrom() throws Exception {
        List<Integer> list = stdList.subList(-4, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSubListBadTo() throws Exception {
        List<Integer> list = stdList.subList(0, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubListInvertedFromAndTo() throws Exception {
        List<Integer> list = stdList.subList(2, 1);
    }

    @Test
    public void testSubListTailWeighted() throws Exception {
        stdList.add(7);
        stdList.add(8);
        stdList.add(9);
        List<Integer> list = stdList.subList(3, 5);
        assertThat(list.size(), is(equalTo(3)));
        assertThat(list.get(0), is(equalTo(7)));
        assertThat(list.get(1), is(equalTo(8)));
        assertThat(list.get(2), is(equalTo(9)));
    }

    @Test
    public void testToArray() throws Exception {
        Object[] arr = stdList.toArray();
        assertThat(arr.length, is(equalTo(3)));
        assertThat((Integer) arr[0], is(equalTo(4)));
        assertThat((Integer) arr[1], is(equalTo(5)));
        assertThat((Integer) arr[2], is(equalTo(6)));
    }

    @Test
    public void testToArrayWithParameter() throws Exception {
        Integer[] arr = stdList.toArray(new Integer[0]);
        assertThat(arr.length, is(equalTo(3)));
        assertThat(arr[0], is(equalTo(4)));
        assertThat(arr[1], is(equalTo(5)));
        assertThat(arr[2], is(equalTo(6)));
    }

    @Test
    public void testToArrayWithLargeParameter() throws Exception {
        Integer[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        arr = stdList.toArray(arr);
        assertThat(arr.length, is(equalTo(10)));
        assertThat(arr[0], is(equalTo(4)));
        assertThat(arr[1], is(equalTo(5)));
        assertThat(arr[2], is(equalTo(6)));
        assertThat(arr[3], is(nullValue()));
        assertThat(arr[4], is(equalTo(5)));
    }

    @Test
    public void testListIterator() throws Exception {
        ListIterator<Integer> listIterator = stdList.listIterator();
        int[] values = new int[5];
        int idx = 0;
        while (listIterator.hasNext() && idx < values.length) {
            values[idx++] = listIterator.next();
        }
        assertThat(listIterator.hasNext(), is(false));
        assertThat(listIterator.hasPrevious(), is(true));
        assertThat(listIterator.previous(), is(equalTo(6)));
        assertThat(values[0], is(equalTo(4)));
        assertThat(values[1], is(equalTo(5)));
        assertThat(values[2], is(equalTo(6)));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testListIteratorBadIndex() throws Exception {
        ListIterator<Integer> listIterator = stdList.listIterator(10);
    }

    @Test
    public void testListIteratorHeadWeighted() throws Exception {
        stdList.add(7);
        stdList.add(8);
        stdList.add(9);
        stdList.add(10);
        ListIterator<Integer> listIterator = stdList.listIterator(1);
        assertThat(listIterator.hasNext(), is(true));
        assertThat(listIterator.hasPrevious(), is(true));
        assertThat(listIterator.next(), is(equalTo(5)));
    }

    @Test
    public void testNextIndex() throws Exception {
        ListIterator<Integer> listIterator = stdList.listIterator(1);
        assertThat(listIterator.nextIndex(), is(equalTo(1)));
        assertThat(listIterator.next(), is(equalTo(5)));
        assertThat(stdList.get(1), is(equalTo(5)));
    }

    @Test
    public void testPreviousIndex() throws Exception {
        ListIterator<Integer> listIterator = stdList.listIterator(1);
        assertThat(listIterator.previousIndex(), is(equalTo(0)));
        assertThat(listIterator.previous(), is(equalTo(4)));
        assertThat(stdList.get(0), is(equalTo(4)));
    }

    @Test
    public void testListIteratorIndexNonZero() throws Exception {
        ListIterator<Integer> listIterator = stdList.listIterator(3);
        assertThat(listIterator.hasNext(), is(false));
        assertThat(listIterator.hasPrevious(), is(true));
        assertThat(listIterator.previous(), is(equalTo(6)));
    }

    @Test
    public void testListIteratorAddEmptyList() throws Exception {
        List<Integer> list = new DoubleLinkedList<Integer>();
        ListIterator<Integer> listIterator = list.listIterator();
        assertThat(listIterator.hasPrevious(), is(false));
        assertThat(listIterator.hasNext(), is(false));
        listIterator.add(7);
        assertThat(list.get(0), is(equalTo(7)));
    }

    @Test
    public void testListIteratorInsertBeforeFirst() throws Exception {
        ListIterator<Integer> listIterator = stdList.listIterator();
        listIterator.add(100);
        assertThat(stdList.get(0), is(equalTo(100)));
        assertThat(stdList.size(), is(equalTo(4)));
        assertThat(stdList.get(1), is(equalTo(4)));
    }

    @Test
    public void testListIteratorInsertBeforeEnd() throws Exception {
        ListIterator<Integer> listIterator = stdList.listIterator(stdList.size() - 1);
        listIterator.add(56);
        assertThat(stdList.size(), is(equalTo(4)));
        assertThat(stdList.get(0), is(equalTo(4)));
        assertThat(stdList.get(1), is(equalTo(5)));
        assertThat(stdList.get(2), is(equalTo(56)));
        assertThat(stdList.get(3), is(equalTo(6)));
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testListIteratorConcurrentModificationProblem() throws Exception {
        ListIterator<Integer> listIterator = stdList.listIterator();
        stdList.remove(1);
        listIterator.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void testAttemptedToAccessPastLastElement() throws Exception {
        ListIterator<Integer> listIterator = stdList.listIterator();
        while(listIterator.hasNext()) {
            listIterator.next();
        }
        int q = listIterator.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void testAttemptedToAccessBeforeFirstElement() throws Exception {
        ListIterator<Integer> listIterator = stdList.listIterator();
        int q = listIterator.previous();
    }


    @Test
    public void testListIteratorAppend() throws Exception {
        ListIterator<Integer> listIterator = stdList.listIterator(stdList.size());
        assertThat(listIterator.nextIndex(), is(equalTo(3)));
        listIterator.add(21);
        assertThat(listIterator.nextIndex(), is(equalTo(4)));
        assertThat(listIterator.previous(), is(equalTo(21)));
    }

    @Test
    public void testListIteratorRemoveMiddle() throws Exception {
        ListIterator<Integer> listIterator = stdList.listIterator(1);
        listIterator.remove();
        assertThat(stdList.size(), is(equalTo(2)));
        assertThat(listIterator.nextIndex(), is(equalTo(1)));
        assertThat(listIterator.next(), is(equalTo(6)));
    }

    @Test
    public void testListIteratorSet() throws Exception {
        ListIterator<Integer> listIterator = stdList.listIterator();
        listIterator.next();
        listIterator.set(100);
        assertThat(stdList.size(), is(equalTo(3)));
        assertThat(stdList.get(0), is(equalTo(4)));
        assertThat(stdList.get(1), is(equalTo(100)));
        assertThat(stdList.get(2), is(equalTo(6)));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testListIteratorSetPastEnd() throws Exception {
        ListIterator<Integer> listIterator = stdList.listIterator();
        while(listIterator.hasNext()) listIterator.next();
        listIterator.set(100);
    }

    @Test
    public void testListIteratorRemoveHead() throws Exception {
        ListIterator<Integer> listIterator = stdList.listIterator();
        listIterator.remove();
        assertThat(stdList.size(), is(equalTo(2)));
        assertThat(listIterator.nextIndex(), is(equalTo(0)));
        assertThat(stdList.get(0), is(equalTo(5)));
    }

    @Test
    public void testListIteratorRemoveTail() throws Exception {
        ListIterator<Integer> listIterator = stdList.listIterator(2);
        listIterator.remove();
        assertThat(stdList.size(), is(equalTo(2)));
        assertThat(listIterator.nextIndex(), is(equalTo(2)));
        assertThat(listIterator.previous(), is(equalTo(5)));
    }

    @Test
    public void testAddAll() throws Exception {
        Collection<Integer> c = new ArrayList<Integer>();
        c.add(1);
        c.add(2);
        c.add(3);
        assertThat(stdList.addAll(c), is(true));
        assertThat(stdList.size(), is(equalTo(6)));
        assertThat(stdList.get(3), is(equalTo(1)));
        assertThat(stdList.get(4), is(equalTo(2)));
        assertThat(stdList.get(5), is(equalTo(3)));
    }

    @Test
    public void testAddAllIndexBefore() throws Exception {
        Collection<Integer> c = new ArrayList<Integer>();
        c.add(1);
        c.add(2);
        c.add(3);
        assertThat(stdList.addAll(0, c), is(true));
        assertThat(stdList.size(), is(equalTo(6)));
        assertThat(stdList.get(0), is(equalTo(1)));
        assertThat(stdList.get(1), is(equalTo(2)));
        assertThat(stdList.get(2), is(equalTo(3)));
        assertThat(stdList.get(3), is(equalTo(4)));
        assertThat(stdList.get(4), is(equalTo(5)));
        assertThat(stdList.get(5), is(equalTo(6)));
    }

    @Test
    public void testAddAllIndexAfter() throws Exception {
        Collection<Integer> c = new ArrayList<Integer>();
        c.add(1);
        c.add(2);
        c.add(3);
        assertThat(stdList.addAll(3, c), is(true));
        assertThat(stdList.size(), is(equalTo(6)));
        assertThat(stdList.get(0), is(equalTo(4)));
        assertThat(stdList.get(1), is(equalTo(5)));
        assertThat(stdList.get(2), is(equalTo(6)));
        assertThat(stdList.get(3), is(equalTo(1)));
        assertThat(stdList.get(4), is(equalTo(2)));
        assertThat(stdList.get(5), is(equalTo(3)));
    }

    @Test
    public void testAddAllIndexMiddle() throws Exception {
        Collection<Integer> c = new ArrayList<Integer>();
        c.add(1);
        c.add(2);
        c.add(3);
        assertThat(stdList.addAll(2, c), is(true));
        assertThat(stdList.size(), is(equalTo(6)));
        assertThat(stdList.get(0), is(equalTo(4)));
        assertThat(stdList.get(1), is(equalTo(5)));
        assertThat(stdList.get(2), is(equalTo(1)));
        assertThat(stdList.get(3), is(equalTo(2)));
        assertThat(stdList.get(4), is(equalTo(3)));
        assertThat(stdList.get(5), is(equalTo(6)));
    }

    @Test
    public void testAddAllNothingReturnsFalse() throws Exception {
        Collection<Integer> c = new ArrayList<Integer>();
        assertThat(stdList.addAll(c), is(false));
    }

    @Test
    public void testAddAllIndexedNothingReturnsFalse() throws Exception {
        Collection<Integer> c = new ArrayList<Integer>();
        assertThat(stdList.addAll(1, c), is(false));
    }

    @Test
    public void testIterator() throws Exception {
        Iterator<Integer> iterator = stdList.iterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(equalTo(4)));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(equalTo(5)));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(equalTo(6)));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void testIteratorRemove() throws Exception {
        Iterator<Integer> iterator = stdList.iterator();
        iterator.next();
        iterator.remove();
        assertThat(stdList.size(), is(equalTo(2)));
        assertThat(stdList.get(0), is(equalTo(4)));
        assertThat(stdList.get(1), is(equalTo(6)));
    }

    @Test
    public void testIteratorRemoveFirst() throws Exception {
        Iterator<Integer> iterator = stdList.iterator();
        iterator.remove();
        assertThat(stdList.size(), is(equalTo(2)));
        assertThat(stdList.get(0), is(equalTo(5)));
        assertThat(stdList.get(1), is(equalTo(6)));
    }

    @Test
    public void testIteratorRemoveLast() throws Exception {
        Iterator<Integer> iterator = stdList.iterator();
        iterator.next();
        iterator.next();
        iterator.remove();
        assertThat(stdList.size(), is(equalTo(2)));
        assertThat(stdList.get(0), is(equalTo(4)));
        assertThat(stdList.get(1), is(equalTo(5)));
    }

    @Test
    public void testRetainAllConainsNone() throws Exception {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(7);
        arrayList.add(8);
        arrayList.add(9);
        assertThat(stdList.retainAll(arrayList), is(true));
        assertThat(stdList.size(), is(equalTo(0)));
    }

    @Test
    public void testRetainAllContainsAll() throws Exception {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(5);
        arrayList.add(6);
        arrayList.add(4);
        assertThat(stdList.retainAll(arrayList), is(false));
        assertThat(stdList.size(), is(equalTo(3)));
        assertThat(stdList.get(0), is(equalTo(4)));
        assertThat(stdList.get(1), is(equalTo(5)));
        assertThat(stdList.get(2), is(equalTo(6)));
    }


    @Test
    public void testRetainAllContainsOne() throws Exception {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(2);
        arrayList.add(9);
        arrayList.add(5);
        assertThat(stdList.retainAll(arrayList), is(true));
        assertThat(stdList.size(), is(equalTo(1)));
        assertThat(stdList.get(0), is(equalTo(5)));
    }

    @Test
    public void testRetainAllContainsSome() throws Exception {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(6);
        arrayList.add(4);
        arrayList.add(9);
        assertThat(stdList.retainAll(arrayList), is(true));
        assertThat(stdList.size(), is(equalTo(2)));
        assertThat(stdList.get(0), is(equalTo(4)));
        assertThat(stdList.get(1), is(equalTo(6)));
    }

    @Test
    public void testRemoveAllContainsNone() throws Exception {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(7);
        arrayList.add(8);
        arrayList.add(9);
        assertThat(stdList.removeAll(arrayList), is(false));
        assertThat(stdList.size(), is(equalTo(3)));
        assertThat(stdList.get(0), is(equalTo(4)));
        assertThat(stdList.get(1), is(equalTo(5)));
        assertThat(stdList.get(2), is(equalTo(6)));
    }

    @Test
    public void testRemoveAllContainsAll() throws Exception {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(6);
        arrayList.add(4);
        arrayList.add(5);
        assertThat(stdList.removeAll(arrayList), is(true));
        assertThat(stdList.size(), is(equalTo(0)));
    }

    @Test
    public void testRemoveAllContainsOne() throws Exception {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(8);
        arrayList.add(9);
        arrayList.add(5);
        assertThat(stdList.removeAll(arrayList), is(true));
        assertThat(stdList.size(), is(equalTo(2)));
        assertThat(stdList.get(0), is(equalTo(4)));
        assertThat(stdList.get(1), is(equalTo(6)));
    }

    @Test
    public void testRemoveAllContainsSome() throws Exception {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(6);
        arrayList.add(9);
        arrayList.add(4);
        assertThat(stdList.removeAll(arrayList), is(true));
        assertThat(stdList.size(), is(equalTo(1)));
        assertThat(stdList.get(0), is(equalTo(5)));
    }

    @Test
    public void testConainsAllContainsNone() throws Exception {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(7);
        arrayList.add(9);
        arrayList.add(10);
        assertThat(stdList.containsAll(arrayList), is(false));
    }

    @Test
    public void testConainsAllContainsAll() throws Exception {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(5);
        arrayList.add(6);
        arrayList.add(4);
        assertThat(stdList.containsAll(arrayList), is(true));
    }

    @Test
    public void testConainsAllContainsAllShort() throws Exception {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(6);
        arrayList.add(4);
        assertThat(stdList.containsAll(arrayList), is(true));
    }

    @Test
    public void testConainsAllContainsOne() throws Exception {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(8);
        arrayList.add(5);
        arrayList.add(7);
        assertThat(stdList.containsAll(arrayList), is(false));
    }

    @Test
    public void testConainsAllContainsSome() throws Exception {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(8);
        arrayList.add(6);
        arrayList.add(4);
        assertThat(stdList.containsAll(arrayList), is(false));
    }

    @Test
    public void testDescendingIteratort() throws Exception {
        Iterator<Integer> iterator = stdDeque.descendingIterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(equalTo(6)));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(equalTo(5)));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(equalTo(4)));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void testDescendingIteratorRemove() throws Exception {
        Iterator<Integer> iterator = stdDeque.descendingIterator();
        iterator.next();
        iterator.next();
        iterator.remove();
        assertThat(stdDeque.size(), is(equalTo(2)));
        assertThat(stdDeque.peekFirst(), is(equalTo(4)));
        assertThat(stdDeque.peekLast(), is(equalTo(6)));
    }

    @Test
    public void testAddFirst() throws Exception {
        stdDeque.addFirst(1);
        assertThat(stdDeque.size(), is(equalTo(4)));
        assertThat(stdDeque.peekFirst(), is(equalTo(1)));
    }

    @Test
    public void testAddLast() throws Exception {
        stdDeque.addLast(1);
        assertThat(stdDeque.size(), is(equalTo(4)));
        assertThat(stdDeque.peekLast(), is(equalTo(1)));
    }

    @Test
    public void testOfferFirst() throws Exception {
        assertThat(stdDeque.offerFirst(12), is(true));
    }

    @Test
    public void testOfferLast() throws Exception {
        assertThat(stdDeque.offerLast(11), is(true));
    }

    @Test
    public void testRemoveFirst() throws Exception {
        assertThat(stdDeque.removeFirst(), is(equalTo(4)));
        assertThat(stdDeque.size(), is(equalTo(2)));
        assertThat(stdDeque.peekFirst(), is(equalTo(5)));
    }

    @Test
    public void testRemoveFirstNoDeque() throws Exception {
        assertThat((new DoubleLinkedList<Integer>()).removeFirst(), is(nullValue()));
    }

    @Test
    public void testRemoveLast() throws Exception {
        assertThat(stdDeque.removeLast(), is(equalTo(6)));
        assertThat(stdDeque.size(), is(equalTo(2)));
        assertThat(stdDeque.peekLast(), is(equalTo(5)));
    }

    @Test
    public void testRemoveLastNoDeque() throws Exception {
        assertThat((new DoubleLinkedList<Integer>()).removeLast(), is(nullValue()));
    }

    @Test
    public void testPollFirst() throws Exception {
        assertThat(stdDeque.pollFirst(), is(equalTo(4)));
        assertThat(stdDeque.size(), is(equalTo(2)));
        assertThat(stdDeque.peekFirst(), is(equalTo(5)));
    }

    @Test
    public void testPollFirstEmptyDeque() throws Exception {
        Deque<Integer> deque = new DoubleLinkedList<Integer>();
        assertThat(deque.pollFirst(), is(nullValue()));
    }

    @Test
    public void testPollLast() throws Exception {
        assertThat(stdDeque.pollLast(), is(equalTo(6)));
        assertThat(stdDeque.size(), is(equalTo(2)));
        assertThat(stdDeque.peekLast(), is(equalTo(5)));
    }

    @Test
    public void testPollLastEmptyDeque() throws Exception {
        Deque<Integer> deque = new DoubleLinkedList<Integer>();
        assertThat(deque.pollLast(), is(nullValue()));
    }

    @Test
    public void testGetFirst() throws Exception {
        assertThat(stdDeque.getFirst(), is(equalTo(4)));
        assertThat(stdDeque.size(), is(equalTo(3)));
        assertThat(stdDeque.peekFirst(), is(equalTo(4)));
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetFirstEmptyDeque() throws Exception {
        Deque<Integer> deque = new DoubleLinkedList<Integer>();
        deque.getFirst();
    }

    @Test
    public void testGetLast() throws Exception {
        assertThat(stdDeque.getLast(), is(equalTo(6)));
        assertThat(stdDeque.size(), is(equalTo(3)));
        assertThat(stdDeque.peekLast(), is(equalTo(6)));
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetLastEmptyDeque() throws Exception {
        Deque<Integer> deque = new DoubleLinkedList<Integer>();
        deque.getLast();
    }

    @Test
    public void testRemoveFirstOccurrence() throws Exception {
        assertThat(stdDeque.removeFirstOccurrence(5), is(true));
        assertThat(stdDeque.size(), is(equalTo(2)));

        assertThat(stdDeque.pollFirst(), is(equalTo(4)));
        assertThat(stdDeque.pollFirst(), is(equalTo(6)));
    }

    @Test
    public void testRemoveFirstOccurrenceWithMoreThanOne() throws Exception {
        stdDeque.addLast(7);
        stdDeque.addLast(5);
        stdDeque.addLast(2);

        assertThat(stdDeque.removeFirstOccurrence(5), is(true));
        assertThat(stdDeque.size(), is(equalTo(5)));
        assertThat(stdDeque.pollFirst(), is(equalTo(4)));
        assertThat(stdDeque.pollFirst(), is(equalTo(6)));
        assertThat(stdDeque.pollFirst(), is(equalTo(7)));
        assertThat(stdDeque.pollFirst(), is(equalTo(5)));
        assertThat(stdDeque.pollFirst(), is(equalTo(2)));
    }

    @Test
    public void testRemoveFirstOccurrenceWithZero() throws Exception {
        assertThat(stdDeque.removeFirstOccurrence(2), is(false));
    }

    @Test
    public void testRemoveLastOccurrence() throws Exception {
        assertThat(stdDeque.removeLastOccurrence(4), is(true));
        assertThat(stdDeque.size(), is(equalTo(2)));

        assertThat(stdDeque.pollFirst(), is(equalTo(5)));
        assertThat(stdDeque.pollFirst(), is(equalTo(6)));
    }

    @Test
    public void testRemoveLastOccurrenceWithMoreThanOne() throws Exception {
        stdDeque.addLast(7);
        stdDeque.addLast(5);
        stdDeque.addLast(2);

        assertThat(stdDeque.removeLastOccurrence(5), is(true));
        assertThat(stdDeque.size(), is(equalTo(5)));
        assertThat(stdDeque.pollFirst(), is(equalTo(4)));
        assertThat(stdDeque.pollFirst(), is(equalTo(5)));
        assertThat(stdDeque.pollFirst(), is(equalTo(6)));
        assertThat(stdDeque.pollFirst(), is(equalTo(7)));
        assertThat(stdDeque.pollFirst(), is(equalTo(2)));
    }

    @Test
    public void testRemoveLastOccurrenceWithZero() throws Exception {
        assertThat(stdDeque.removeLastOccurrence(2), is(false));
    }

    @Test
    public void testOffer() throws Exception {
        assertThat(stdDeque.offer(12), is(true));
    }

    @Test
    public void testDequeRemove() throws Exception {
        assertThat(stdDeque.remove(), is(equalTo(4)));
        assertThat(stdDeque.size(), is(equalTo(2)));
        assertThat(stdDeque.pollFirst(), is(equalTo(5)));
        assertThat(stdDeque.pollFirst(), is(equalTo(6)));
    }

    @Test
    public void testPoll() throws Exception {
        assertThat(stdDeque.poll(), is(equalTo(4)));
        assertThat(stdDeque.size(), is(equalTo(2)));
        assertThat(stdDeque.pollFirst(), is(equalTo(5)));
        assertThat(stdDeque.pollFirst(), is(equalTo(6)));
    }

    @Test
    public void testElement() throws Exception {
        assertThat(stdDeque.element(), is(equalTo(4)));
    }

    @Test
    public void testElementEmpty() throws Exception {
        assertThat((new DoubleLinkedList<Integer>()).element(), is(nullValue()));
    }

    @Test
    public void testPeek() throws Exception {
        assertThat(stdDeque.peek(), is(equalTo(4)));
    }

    @Test
    public void testPeekEmpty() throws Exception {
        assertThat((new DoubleLinkedList<Integer>()).peek(), is(nullValue()));
    }

    @Test
    public void testPush() throws Exception {
        stdDeque.push(15);
        assertThat(stdDeque.size(), is(equalTo(4)));
        assertThat(stdDeque.pollFirst(), is(equalTo(15)));
        assertThat(stdDeque.pollFirst(), is(equalTo(4)));
        assertThat(stdDeque.pollFirst(), is(equalTo(5)));
        assertThat(stdDeque.pollFirst(), is(equalTo(6)));
    }

    @Test
    public void testPop() throws Exception {
        stdDeque.push(15);
        assertThat(stdDeque.pop(), is(equalTo(15)));
        assertThat(stdDeque.size(), is(equalTo(3)));
        assertThat(stdDeque.pollFirst(), is(equalTo(4)));
        assertThat(stdDeque.pollFirst(), is(equalTo(5)));
        assertThat(stdDeque.pollFirst(), is(equalTo(6)));
    }

    @Test
    public void testClone() throws Exception {
        DoubleLinkedList<Integer> newList = (DoubleLinkedList<Integer>)stdDblLnkLst.clone();
        assertThat(newList, is(not(sameInstance(stdDblLnkLst))));
        assertThat(true, is(newList.getClass() == stdList.getClass()));
        assertThat(newList.size(), is(equalTo(3)));
        assertThat(newList.get(0), is(equalTo(4)));
        assertThat(newList.get(1), is(equalTo(5)));
        assertThat(newList.get(2), is(equalTo(6)));
    }

    @Test
    public void testSerializationWrite() throws Exception {
        ObjectOutputStream OS = new ObjectOutputStream(new ByteArrayOutputStream());
        OS.writeObject(stdList);
    }

    @Test
    public void testSerializationRead() throws Exception {
        ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
        ObjectOutputStream OOS = new ObjectOutputStream(BAOS);
        OOS.writeObject(stdList);
        ObjectInputStream OIS = new ObjectInputStream(new ByteArrayInputStream(BAOS.toByteArray()));
        DoubleLinkedList<Integer> readList = (DoubleLinkedList<Integer>) OIS.readObject();
        assertThat(readList.size(), is(equalTo(3)));
        assertThat(readList.get(0), is(equalTo(4)));
        assertThat(readList.get(1), is(equalTo(5)));
        assertThat(readList.get(2), is(equalTo(6)));
    }
}