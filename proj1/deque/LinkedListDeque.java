package deque;

import java.util.Iterator;
import java.util.Objects;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    /**
     * subclass, which is used to represent the Node concept
     */
    private class TNode {
        private T item;
        private TNode next;
        private TNode prev;


        public TNode(T i, TNode p, TNode n) {
            prev = p;
            next = n;
            item = i;
        }
    }
    private TNode recursionPos;
    /* size to take some methods "constant time"  */
    private int size;
    private TNode sentinel;

    public LinkedListDeque() {
        size = 0;
        sentinel = new TNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        recursionPos = sentinel;
    }


    @Override
    public void addFirst(T x) {
        size = size + 1;
        /* TNode 前后连接 */
        TNode newFirst = new TNode(x, sentinel, sentinel.next);

        sentinel.next.prev = newFirst;
        sentinel.next = newFirst;
    }

    @Override
    public void addLast(T x) {
        size = size + 1;

        /* 获取现有的last */
        TNode oldLast = sentinel.prev;
        TNode newLast = new TNode(x, oldLast, sentinel);

        oldLast.next = newLast;
        sentinel.prev = newLast;

    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        TNode p = sentinel.next;
        while (p.next != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        /* 打印最后一个item以及换行符*/
        System.out.println(p.item);
    }

    @Override
    public T removeFirst() {
        if (this.isEmpty()) {
            return null;
        }
        size = size - 1;
        TNode victim = sentinel.next;

        sentinel.next = victim.next;
        victim.next.prev = sentinel;


        return victim.item;
    }

    @Override
    public T removeLast() {
        if (this.isEmpty()) {
            return null;
        }
        size = size - 1;
        TNode victim = sentinel.prev;

        victim.prev.next = sentinel;
        sentinel.prev = victim.prev;

        return victim.item;
    }

    @Override
    public T get(int index) {
        if (this.isEmpty()) {
            return null;
        }

        TNode p = sentinel.next;

        while (index != 0) {
            p = p.next;
            index -= 1;
        }

        return p.item;
    }

    public T getRecursive(int index) {
        if (this.isEmpty()) {
            return null;
        }
        if (index == 0) {
            T res  = recursionPos.next.item;
            recursionPos = sentinel;
            return res;
        } else {
            recursionPos = recursionPos.next;
            return getRecursive(index - 1);
        }
    }


    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;


        if (other instanceof Deque) {
            Deque<T> otherList = (Deque) other;
            if (size != otherList.size())
                return false;

            for (int i = 0; i < size; i++) {
                T item = get(i);
                T itemOther = otherList.get(i);
                if (!item.equals(itemOther))
                    return false;
            }
        } else {
            return false;
        }

        return true;
    }

    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator {
        private TNode wizPos;

        public LinkedListIterator() {
            wizPos = sentinel.next;
        }

        @Override
        public boolean hasNext() {
            return wizPos != sentinel;
        }

        @Override
        public T next() {
            T returnT = wizPos.item;

            wizPos = wizPos.next;

            return returnT;
        }
    }
}
