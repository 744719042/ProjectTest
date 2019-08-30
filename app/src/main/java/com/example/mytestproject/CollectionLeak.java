package com.example.mytestproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;

public class CollectionLeak {
    static class Node implements Comparable<Node>{
        private int data;

        public Node(int data) {
            this.data = data;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return data == node.data;
        }

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }

        @Override
        public int hashCode() {
            return Objects.hash(data);
        }

        @Override
        public int compareTo(Node o) {
            if (o == null) {
                return 1;
            }
            return data - o.data;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "data=" + data +
                    '}';
        }
    }

    public static void main(String[] args) {
        Node node1 = new Node(10);
        List<Node> list = new ArrayList<>();
        list.add(node1);
        System.out.println("list = " + list.toString());
        node1.setData(15);
        list.remove(node1);
        System.out.println("list = " + list.toString());

        Node node2 = new Node(20);
        Map<Node, String> map = new HashMap<>();
        map.put(node2, "Hello");
        System.out.println(map.toString());
        node2.setData(25);
        map.remove(node2);
        System.out.println(map.toString());

        TreeSet<Node> treeSet = new TreeSet<>();
        Node node3 = new Node(30);
        treeSet.add(node3);
        System.out.println(treeSet.toString());
        node3.setData(35);
        treeSet.remove(node3);
        System.out.println(treeSet.toString());

        Node node4 = new Node(40);
        HashSet<Node> hashSet = new HashSet<>();
        hashSet.add(node4);
        System.out.println(hashSet.toString());
        node4.setData(45);
        hashSet.remove(node4);
        System.out.println(hashSet);
    }
}
