package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    // 存储所有的key值
    private Set<K> keySet = new HashSet<>();
    // 用于存储初始化桶数
    private int initialSize = 16;
    private double loadFactor = 0.75;

    /** Constructors */
    public MyHashMap() {
        buckets = createTable(this.initialSize);
    }

    public MyHashMap(int initialSize) {
        this.initialSize = initialSize;
        buckets = createTable(this.initialSize);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.initialSize = initialSize;
        this.loadFactor = maxLoad;
        buckets = createTable(this.initialSize);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key,value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {

        return new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!


    @Override
    public boolean containsKey(K key) {
        return this.keySet.contains(key);
    }

    @Override
    public int size() {
        return keySet.size();
    }


    /**
     * 进行hash处理，计算将其分配进bucket的序列号
     * 使用mod运算
     * @return 第几个bucket
     */
    public int hash(K key) {
        // buckets中的序列号
        int hashNum = key.hashCode();
        return Math.floorMod(hashNum, buckets.length);
    }

    public int hash(K key, Collection[] buckets) {
        // buckets中的序列号
        int hashNum = key.hashCode();
        return Math.floorMod(hashNum, buckets.length);
    }

    @Override
    public V get(K key) {
        if (!containsKey(key))
            return null;

        int bucketIdx = hash(key);
        if (buckets[bucketIdx] != null){
            for (Node node : buckets[bucketIdx]) {
                if (node.key.equals(key)) {
                    return node.value;
                }
            }
        }

        return null;

    }


    @Override
    public Set<K> keySet() {
        return keySet;
    }

    @Override
    public void put(K key, V value) {

        Node nodeAdd = new Node(key,value);
        int bucketIdx = hash(key);
        /* 如果不存在这个bucket */
        if (buckets[bucketIdx]== null){
            buckets[bucketIdx] = createBucket();
        }
        /* 如果已经存在这个key */
        if (containsKey(key)){
            for (Node node : buckets[bucketIdx]) {
                if (node.key == key){
                    node.value = value;
                }
            }
        }else {
            // 如果不存在
            buckets[bucketIdx].add(nodeAdd);
        }
        keySet.add(key);
        /* 如果此时需要进行resize */
        if( keySet.size() >= loadFactor * this.buckets.length){
            resize(2 * this.buckets.length);
        }


    }

    /**
     * 如果此时 keySize / buckets.length > loadFactor
     * 则需要重新进行buckets的大小设计
     */
    public void resize(int size){
        Collection<Node>[] tmpTable = createTable(size);
        Collection<Node>[] oldbuckets = this.buckets;

        for (var bucket: oldbuckets) {
            if (bucket != null){
                for(var node: bucket){
                    int tmpIdx = hash(node.key,tmpTable);
                    // 如果tmpTable中这一项中的数据结构不存在
                    if(tmpTable[tmpIdx] == null)
                        tmpTable[tmpIdx] = createBucket();

                    tmpTable[tmpIdx].add(node);
                }
            }

        }

        this.buckets = tmpTable;

    }

    @Override
    public V remove(K key) {
        return remove(key,null);
    }

    @Override
    public V remove(K key, V value) {
        int bucketIdx = hash(key);
        if (!containsKey(key)){
            return null;
        }
        V valueRemove = get(key);
        buckets[bucketIdx].remove(key);

        keySet.remove(key);
        return valueRemove;
    }

    @Override
    public void clear() {
        for (var bucket :
                buckets) {
            bucket = null;
        }
        keySet.clear();
    }


    @Override
    public Iterator<K> iterator() {
        return keySet.iterator();
    }
}
