package com.project.CyShare.Tools;

import java.util.HashMap;
import java.util.Map;

public class TestTrie {
    static Trie trie = new Trie();
    public  static void dosomething()
    { 

        trie.insert("BHuwan",1);
        trie.insert("Joshi",2);
     //   trie.insert("BHuwan",3);
        trie.search("BHuwan");
    }
    public static void main(String[] args)
    {  dosomething();
        System.out.println(trie.search("BHuwan"));
    }
    }
