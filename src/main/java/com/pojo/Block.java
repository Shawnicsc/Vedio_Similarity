package com.pojo;


import com.calculate.Hash;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Block {
    //全局日志记录
    private final static Logger LOGGER = Logger.getLogger(Block.class.getName());
    //原子操作 实现自增功能（线程非阻塞同步）
    private static final AtomicInteger count = new AtomicInteger(0);
    //置零 初始化数组
    private final static byte[] nullHash = new byte[]{0x0};
    //最大随机数
    private final static Long maxnonce = 5000000L;
    //控制生成随机数
    private final static Long SomeNumber = 567890L;

    //  区块实例变量
    private byte[] previousHash;
    //交易记录
    private String[] transactions;
    private byte[] blockHash;
    /**
     * 挖矿工作种子
     * 挖矿是为了生成新的区块，并且通过解决复杂的数学问题来获得网络上的加密货币奖励。
     * 种子可以被视为一个输入，随机生成一个初始参数或初始状态。
     * 这个参数或状态对于成功解决区块链网络的数学问题非常重要。
     * 确保竞争条件下的公平性和安全性。
     */
    private long nonce = 0;
    //区块编号
    private long ordinal = 0;

    public Block(byte[] previousHash, String[] transactions) {
        this.ordinal = count.getAndIncrement();
        this.previousHash = previousHash;
        this.transactions = transactions;
        this.nonce = (long) (Math.random() * SomeNumber);

        this.blockHash = nullHash;
    }

    /**
     * 无参构造 仅限测试
     */
    public Block() {
        this.ordinal = count.getAndIncrement();
        this.previousHash = nullHash;
        this.transactions = new String[]{};
        this.nonce = 0;

        this.blockHash = nullHash;
    }

    public long getOrdinal() {
        return this.ordinal;
    }
    public byte[] getPreviousHash() {
        return previousHash;
    }

    public String[] getTransactions() {
        return transactions;
    }

    public byte[] getBlockHash() {
        return blockHash;
    }
    public String getBlockHashString() {
        return Hash.hexString(blockHash);
    }
    //验证块
    public boolean verifyBlock() {
        byte[] transactionshash = Hash.hash(Arrays.toString(this.transactions).getBytes());
        byte[] noncebytes = longToBytes(nonce);
        byte[] hashTry = Hash.hash(noncebytes, transactionshash, this.previousHash);
        if (hashTry.length != blockHash.length) return false;
        for (int i = 0; i < hashTry.length; i++)
            if (hashTry[i] != blockHash[i])
                return false;
        return true;
    }

    /**
     *生成和验证新的区块
     */
    public Block mine() {

        Long start = System.currentTimeMillis();//时间计算
        byte[] transactionshash = Hash.hash(Arrays.toString(this.transactions).getBytes());
        long top = nonce + maxnonce;
        while (nonce < top) {
            byte[] noncebytes = longToBytes(nonce);
            //加密
            byte[] hashTry = Hash.hash(noncebytes, transactionshash, this.getPreviousHash());
            if (hashTry[0] == 0 && hashTry[1] == 0) { //&& hashTry[2] == 0) {
                System.out.println("mine: nonce: " + nonce);
                System.out.println(Hash.hexString(hashTry));
                this.blockHash = hashTry;
                break;
            }
            if (nonce == top - 1) {
                System.out.println("MaxNonced Out");
            }
            nonce++;
        }
        Long end = System.currentTimeMillis();
        System.out.printf("Mining time for Block %d : %8d ms.\n--\n", this.getOrdinal(), (end - start));
        return this;
    }
    //格式转化
    private static byte[] longToBytes(long l) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (l & 0xFF);
            l >>= 8;
        }
        return result;
    }
    // 输出格式设置
    public String blockDataString() {
        return String.format("Block[%d] (Verified? %b)\n PrevHash: %s\n Nonce: %s\n Transactions: %s\nBlockHash: %s\n--\n",
                ordinal,
                this.verifyBlock(),
                Hash.hexString(previousHash),
                nonce,
                String.join(" / ", transactions),
                Hash.hexString(blockHash));
    }

}