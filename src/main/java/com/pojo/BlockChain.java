package com.pojo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

public class BlockChain {
    private final static Logger LOGGER = Logger.getLogger(BlockChain.class.getName());

    private ArrayList<Block> chain = new ArrayList<Block>();

    public BlockChain() {

    }

    public boolean addBlock(Block block) {
        chain.add(block);
        return true;
    }

    public Block getBlock(Integer depth) {
        if (depth >= chain.size() || depth < 0) {
            throw new NoSuchElementException();
        }
        return chain.get(depth);
    }
    //迭代器
    public Iterator<Block> iterator() {
        return new Iterator<Block>() {
            int currentIdx = chain.size() - 1;

            @Override
            public boolean hasNext() {
                return (currentIdx >= 0);
            }

            @Override
            public Block next() {
                return chain.get(currentIdx--);
            }

            @Override
            public void remove() {}


        };
    }
    //验证链
    public boolean verifyChain() {
        Iterator<Block> iter = chain.iterator();
        while (iter.hasNext()) {
            Block b = iter.next();
            if (b.verifyBlock() != true) return false;
        }
        return true;
    }
}
