package com.project.CyShare.Tools;

import java.util.HashMap;
import java.util.Map;

public class Trie {
    public TrieMapNode root;
    public long size;


    public Trie() {
        root=new TrieMapNode();

    }


    public void insert(String word,long userid) {
        TrieMapNode curr=root;
        for(int i=0;i<word.length();i++)
        {
            if(curr.map.get(word.charAt(i))!=null)
            {
                curr=curr.map.get(word.charAt(i));
            }
            else{
                TrieMapNode temp=new TrieMapNode();
                curr.map.put(word.charAt(i),temp);
                curr=temp;
            }


        }

        curr.end=true;
        size++;
        curr.userid = userid;
    }


    public long search(String word) {
        TrieMapNode curr=root;
        for(int i=0;i<word.length();i++)
        {
            if(curr.map.get(word.charAt(i))==null)
                return -1;
            curr=curr.map.get(word.charAt(i));

        }
        if(curr.end==true)
            return curr.userid;
        return -1
                ;
    }
    public long getSize()
    {
        return size;
    }

    public boolean startsWith(String prefix) {
        TrieMapNode curr=root;
        for(int i=0;i<prefix.length();i++)
        {
            if(curr.map.get(prefix.charAt(i))==null)
                return false;
            curr=curr.map.get(prefix.charAt(i));

        }
        return true;

    }

    static class TrieMapNode{
        public Map<Character,TrieMapNode>map;
        public boolean end;
        long userid;
        public TrieMapNode(){
            map=new HashMap<>();
            end=false;
        }

    }


}
