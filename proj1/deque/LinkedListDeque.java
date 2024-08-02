package deque;

import java.util.Iterator;
import java.util.Objects;

public class LinkedListDeque<Item> implements Deque<Item>,Iterable<Item> {
    /** subclass, which is used to represent the Node concept */
    private class ItemNode {
        public Item item;
        public ItemNode next;
        public ItemNode prev;

        public ItemNode(Item i, ItemNode p,ItemNode n){
            prev = p;
            next = n;
            item = i;
        }
    }

    /* size to take some methods "constant time"  */
    private int size;
    private ItemNode sentinel;

    public LinkedListDeque(){
        size = 0;
        sentinel = new ItemNode(null,null,null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }

    public LinkedListDeque(Item x){
        size = 1;
        sentinel = new ItemNode(null,null,null);
        sentinel.next = new ItemNode(x,sentinel,sentinel);
        sentinel.prev = sentinel.next;
    }
    @Override
    public void addFirst(Item x){
        size = size + 1;
        /* ItemNode 前后连接 */
        ItemNode newFirst = new ItemNode(x,sentinel,sentinel.next);

        sentinel.next.prev = newFirst;
        sentinel.next = newFirst;
    }
    @Override
    public void addLast(Item x){
        size = size + 1;

        /* 获取现有的last */
        ItemNode oldLast = sentinel.prev;
        ItemNode newLast = new ItemNode(x, oldLast, sentinel);

        oldLast.next = newLast;
        sentinel.prev = newLast;

    }

    @Override
    public int size(){
        return size;
    }
    @Override
    public void printDeque(){
        ItemNode p = sentinel.next;
        while(p.next != sentinel){
            System.out.print(p.item + " ");
            p = p.next;
        }
        /* 打印最后一个item以及换行符*/
        System.out.println(p.item);
    }
    @Override
    public Item removeFirst(){
        if(this.isEmpty()){
            return null;
        }
        size = size -1;
        ItemNode victim = sentinel.next;

        sentinel.next = victim.next;
        victim.next.prev = sentinel ;


        return victim.item;
    }
    @Override
    public Item removeLast(){
        if(this.isEmpty()){
            return null;
        }
        size = size -1;
        ItemNode victim = sentinel.prev;

        victim.prev.next = sentinel;
        sentinel.prev = victim.prev;

        return victim.item;
    }
    @Override
    public Item get(int index){
        if(this.isEmpty()){
            return null;
        }

        ItemNode p = sentinel.next;

        while (index != 0){
            p = p.next;
            index -= 1;
        }

        return p.item;
    }

    public Item getRecursive(int index){
        if(this.isEmpty()){
            return null;
        }
        if (index == 0){
            return sentinel.next.item;
        }else {
            this.removeFirst();
            return getRecursive(index - 1);
        }
    }


    public boolean contains(Item x){
        if (size == 0)
            return false;

        ItemNode node = sentinel.next;

        while(node != sentinel){
            if (node.item == x)
                return true;
            node = node.next;
        }
        return false;
    }
    @Override
    public boolean equals(Object other){
        if(this == other)
            return true;


        if( other instanceof LinkedListDeque otherList){
            if (size != otherList.size)
                return false;

            for(Item x : this){
                if(!otherList.contains(x))
                    return false;
            }
        }
        return true;
    }

    public Iterator<Item> iterator(){
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator{
        private ItemNode wizPos ;
        public LinkedListIterator(){
            wizPos = sentinel.next;
        }
        @Override
        public boolean hasNext(){
            return wizPos != sentinel;
        }

        @Override
        public Item next() {
            Item returnItem =  wizPos.item;

            wizPos = wizPos.next;

            return returnItem;
        }
    }
}
