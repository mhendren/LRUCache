package com.mhendren.LRUCache;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class DoubleLinkedListTest {
    List<Integer> stdList = new DoubleLinkedList<Integer>();
    @Before
    public void beforeEach() throws Exception {
        stdList.add(4);
        stdList.add(5);
        stdList.add(6);
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
    public void testLastIndexOf() throws Exception {
        stdList.add(5);
        assertThat(stdList.lastIndexOf(5), is(equalTo(3)));
        assertThat(stdList.lastIndexOf(4), is(equalTo(0)));
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

    @Test
    public void testToArray() throws Exception {
        Object[] arr = stdList.toArray();
        assertThat(arr.length, is(equalTo(3)));
        assertThat((Integer)arr[0], is(equalTo(4)));
        assertThat((Integer)arr[1], is(equalTo(5)));
        assertThat((Integer)arr[2], is(equalTo(6)));
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
        while(listIterator.hasNext() && idx < values.length) {
            values[idx++] = listIterator.next();
        }
        assertThat(listIterator.hasNext(), is(false));
        assertThat(listIterator.hasPrevious(), is(true));
        assertThat(listIterator.previous(), is(equalTo(6)));
        assertThat(values[0], is(equalTo(4)));
        assertThat(values[1], is(equalTo(5)));
        assertThat(values[2], is(equalTo(6)));
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
}