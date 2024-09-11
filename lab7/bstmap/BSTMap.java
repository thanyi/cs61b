package bstmap;


import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private BSTNode root;
    private int size;

    private class BSTNode {
        private K key;
        private V value;
        private BSTNode left, right;

        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;

        }
    }

    public BSTMap() {
    }

    @Override
    public void clear() {
        root =null;

        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
       return containsKey(root,key);

    }

    private boolean containsKey(BSTNode x, K key) {

        if (x == null){
            return false;
        }
        boolean res;
        int cmp = key.compareTo(x.key);

        if (cmp < 0){
            res = containsKey(x.left,key);
        }else if (cmp > 0){
            res = containsKey(x.right,key);
        }else {
            return true;
        }

        return res;
    }

    @Override
    public V get(K key) {
        /* 进行put，获取返回值，这也可作为root的初始化 */
        V resValue = get(root, key);
        return resValue;

    }
    private V get(BSTNode x, K key){
        /* key和value如果为空 */
        if (key == null) {
            throw new UnsupportedOperationException("calls get() with a null key");
        }

        if (x == null){
            return null;
        }

        int cmp = key.compareTo(x.key);
        V res;
        if(cmp < 0){
            res = get(x.left,key);
        } else if (cmp > 0) {
            res = get(x.right,key);
        }else {
            return x.value;
        }
        return res;
    }
    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        /* key和value如果为空 */
        if (key == null) {
            throw new UnsupportedOperationException("calls put() with a null key");
        }

        /* 进行put，获取返回值，这也可作为root的初始化 */
        root = put(root, key, value);

    }

    private BSTNode put(BSTNode x, K key, V value) {

        if (x == null) {
            size = size + 1;
            return new BSTNode(key,value);
        }
        int cmp = key.compareTo(x.key);

        if (cmp < 0) {  // key < root.key
            x.left = put(x.left, key, value);   // 将递归中的值进行返回，完成由子到父的链接
        }else if (cmp > 0){
            x.right = put(x.right, key, value);
        }else {
            x.value = value;
        }

        /* 不只关注递归最后一项可以获取到的值，还要返回状态 */
        return x;   // 将递归中的值进行返回，完成由子到父的链接
    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public V remove(K key) {
        return null;
    }

    @Override
    public V remove(K key, V value) {
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return null;
    }

    private class KeyIterator implements Iterator {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            return null;
        }
    }

    /**
     * 可以将BST以从小到大的顺序将每一项进行输出
     * @return
     */
    public void printInOrder(){
        printInOrder(root);
    }

    public void printInOrder(BSTNode node){
        // 检查是否BST直接为空
        if (node == null){
           System.out.println("BST is Empty!");
        }
        // 查看左孩子是否存在，存在则进入
        if (node.left != null){
            printInOrder(node.left);
        }

        System.out.println(node.value);
        // 查看右孩子是否存在，存在则进入
        if (node.right != null) {
            printInOrder(node.right);
        }
    }
}
