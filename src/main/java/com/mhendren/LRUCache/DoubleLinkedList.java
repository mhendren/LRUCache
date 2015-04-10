package com.mhendren.LRUCache;

import java.util.*;

/**
 * Created by mhendren on 4/8/2015.
 */
public class DoubleLinkedList<E> implements List<E> {
    private class DoubleLinkedListNode {
        DoubleLinkedListNode(E data) { this.data = data; }
        E data;
        DoubleLinkedListNode prev;
        DoubleLinkedListNode next;
    };

    DoubleLinkedListNode head;
    DoubleLinkedListNode tail;
    private int nodeCount = 0;
    private int adjustCount = 0;

    synchronized void adjust() {
        adjustCount++;
    }

    @Override
    public int size() {
        return this.nodeCount;
    }

    @Override
    public boolean isEmpty() {
        return this.nodeCount == 0;
    }

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

    @Override
    public Iterator iterator() {
        return new LstIter(0);
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[nodeCount];
        int idx = 0;
        for(DoubleLinkedListNode cur = head; cur != null; cur = cur.next)
            array[idx++] = cur.data;
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
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

    @Override
    public boolean add(E o) {
        DoubleLinkedListNode newNode = new DoubleLinkedListNode(o);
        if (tail != null) { tail.next = newNode; }
        newNode.next = null;
        newNode.prev = tail;
        tail = newNode;
        if (head == null) head = newNode;
        nodeCount++;
        adjust();
        return true;
    }

    private void removeNode(DoubleLinkedListNode node) {
        if (node.prev != null) node.prev.next = node.next; else head = node.next;
        if (node.next != null) node.next.prev = node.prev; else tail = node.prev;
        nodeCount--;
        adjust();
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (DoubleLinkedListNode cur = head; cur != null; cur = cur.next) {
                if (cur.data == null) {
                    removeNode(cur);
                    return true;
                }
            }
        } else {
            for (DoubleLinkedListNode cur = head; cur != null; cur = cur.next) {
                if (cur.data.equals(o)) {
                    removeNode(cur);
                    return true;
                }
            }
        }
        return false;
    }

    private DoubleLinkedListNode newListFromCollection(Collection<E> c) {
        DoubleLinkedListNode start = null;
        DoubleLinkedListNode cur = null;
        if (c != null) {
            for (E data : c) {
                DoubleLinkedListNode newNode = new DoubleLinkedListNode(data);
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

    private DoubleLinkedListNode findEndOfSubList(DoubleLinkedListNode start) {
        DoubleLinkedListNode cur = start;
        while(cur != null && cur.next != null) {
            cur = cur.next;
        }
        return cur;
    }

    @Override
    public boolean addAll(Collection c) {
        DoubleLinkedListNode newList = newListFromCollection(c);
        if(newList == null) return false;
        newList.prev = tail;
        if(tail == null) { head = newList; } else { tail.next = newList; }
        tail = findEndOfSubList(newList);
        nodeCount += c.size();
        adjust();
        return true;
    }

    @Override
    public boolean addAll(int index, Collection c) {
        checkIndex(index, 1);
        if(index == nodeCount) return addAll(c);
        DoubleLinkedListNode newList = newListFromCollection(c);
        if (newList == null) return false;
        DoubleLinkedListNode cur = findIndex(index);
        DoubleLinkedListNode end = findEndOfSubList(newList);
        end.next = cur;
        if(cur.prev != null) {cur.prev.next = newList;} else {head = newList;}
        newList.prev = cur.prev;
        cur.prev = end;
        nodeCount += c.size();
        adjust();
        return true;
    }

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

    private DoubleLinkedListNode findIndex(int index) {
        DoubleLinkedListNode cur;
        if (index >= (nodeCount >> 1)) {
            cur = tail;
            for (int i = nodeCount - 1; i > index; i--) {
                if(cur.prev != null) cur = cur.prev;
                else throw new IndexOutOfBoundsException("Index: " + index + ", No prev on Node");
            }
        } else {
            cur = head;
            for (int i = 0; i < index; i++) {
                if (cur.next != null) cur = cur.next;
                else throw new IndexOutOfBoundsException("Index: " + index + ", No next on Node");
            }
        }
        return cur;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        DoubleLinkedListNode cur = findIndex(index);
        return cur.data;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        DoubleLinkedListNode cur = findIndex(index);
        E data = cur.data;
        cur.data = element;
        adjust();
        return data;
    }

    @Override
    public void add(int index, E element) {
        // can actually add at the current nodeCount position
        checkIndex(index, 1);

        if (index == nodeCount) add(element);
        else {
            DoubleLinkedListNode newNode = new DoubleLinkedListNode(element);
            DoubleLinkedListNode cur = findIndex(index);
            if (cur.prev != null) { cur.prev.next = newNode; } else { head = newNode; }
            newNode.next = cur;
            newNode.prev = cur.prev;
            cur.prev = newNode;
            nodeCount++;
            adjust();
        }
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        DoubleLinkedListNode cur = findIndex(index);
        if (cur.next != null) { cur.next.prev = cur.prev; } else { tail = cur.prev; }
        if (cur.prev != null) { cur.prev.next = cur.next; } else { head = cur.next; }
        nodeCount--;
        adjust();
        return cur.data;
    }

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
        DoubleLinkedListNode nextNode;
        int index = nodeCount;
        int expectedAdjustCount = adjustCount;

        @SuppressWarnings("unchecked")
        LstIter(int index) {
            if (index < 0 || index > nodeCount) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + nodeCount);
            }
            DoubleLinkedListNode cur = null;
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

        @Override
        public boolean hasNext() {
            return index < nodeCount;
        }

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

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

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

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public int previousIndex() {
            return index-1;
        }

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

        public void set(Object o) {
            checkValidState();
            if (index == nodeCount) {
                throw new IndexOutOfBoundsException("Attempted to set value after last element in list");
            }
            adjust();
            expectedAdjustCount++;
            nextNode.data = (E)o;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void add(Object o) {
            checkValidState();
            DoubleLinkedListNode newNode = new DoubleLinkedListNode((E) o);
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

    @SuppressWarnings("unchecked")
    @Override
    public ListIterator listIterator() {
        return new LstIter(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ListIterator listIterator(int index) {
        return new LstIter(index);
    }

    private List subListFromTail(int start, int stop) {
        DoubleLinkedListNode cur = tail;
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

    @Override
    public List subList(int fromIndex, int toIndex) throws IndexOutOfBoundsException {
        checkIndex(fromIndex);
        checkIndex(toIndex);
        if (toIndex < fromIndex) throw new IllegalArgumentException("fromIndex: " + fromIndex + " > toIndex: " + toIndex);
        if (nodeCount - toIndex <= fromIndex) return subListFromTail(nodeCount - toIndex - 1, nodeCount - fromIndex - 1);

        DoubleLinkedListNode cur = head;
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

    @Override
    public boolean retainAll(Collection c) {
        boolean adjust = false;
        for(DoubleLinkedListNode cur = head; cur != null; cur = cur.next) {
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

    @Override
    public boolean removeAll(Collection c) {
        boolean adjust = false;
        for(DoubleLinkedListNode cur = head; cur != null; cur = cur.next) {
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

    @Override
    public boolean containsAll(Collection c) {
        for(Object data : c) {
            if(!contains(data)) return false;
        }
        return true;
    }

}
