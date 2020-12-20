package com.hyhello.priceless.block;

import okhttp3.RequestBody;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalListAccounts;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 *
 */
public class ETH {

    private static Web3j web3j;
    private static Admin admin;
    public static String RPC_URL = "http://127.0.0.1:8545";

    public static String fromAddress = "0xd4278788d4f730ce42a0448c7d7a50b5ca9b17bd";
    public static String password= "";
    private static String toAddress = "0x05f50cd5a97d9b3fec35df3d0c6c8234e6793bdf";
    private static BigDecimal defaultGasPrice = BigDecimal.valueOf(5);

    public static void main(String[] args) throws Exception {
        //web3j = Web3j.build(new HttpService(RPC_URL));
        //admin = Admin.build(new HttpService(RPC_URL));

        web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/1f0898be26f54849b38479f6c9ce76ff"));
        admin = Admin.build(new HttpService("https://mainnet.infura.io/v3/1f0898be26f54849b38479f6c9ce76ff"));


        //getBalance(fromAddress);
        //getTransactionNonce(fromAddress);
        //sendTransaction();

        tran();
    }

    public static  void tran() throws Exception {
        Credentials credentials = WalletUtils.loadCredentials("", "C:\\Users\\wangshu\\Desktop\\UTC--2020-12-18T07-04-48.445177000Z--d4278788d4f730ce42a0448c7d7a50b5ca9b17bd");
        TransactionReceipt transactionReceipt = Transfer.sendFunds(
                web3j, credentials, "0x88078d0F3A25bcBA6757d6045E43F1A64fD2783a",
                BigDecimal.valueOf(0.03), Convert.Unit.ETHER)
                .send();

        BigInteger bigInteger = transactionReceipt.getGasUsed();
        System.out.println(bigInteger.intValue());
    }

    /**
     * 获取余额
     *
     * @param address 钱包地址
     * @return 余额
     */
    private static BigInteger getBalance(String address) {
        BigInteger balance = null;
        try {
            EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            balance = ethGetBalance.getBalance();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("address " + address + " balance " + balance + "wei");
        return balance;
    }

    /**
     * 生成一个普通交易对象
     *
     * @param fromAddress 放款方
     * @param toAddress   收款方
     * @param nonce       交易序号
     * @param gasPrice    gas 价格
     * @param gasLimit    gas 数量
     * @param value       金额
     * @return 交易对象
     */
    private static Transaction makeTransaction(String fromAddress, String toAddress,
                                               BigInteger nonce, BigInteger gasPrice,
                                               BigInteger gasLimit, BigInteger value) {
        Transaction transaction;
        transaction = Transaction.createEtherTransaction(fromAddress, nonce, gasPrice, gasLimit, toAddress, value);
        return transaction;
    }

    /**
     * 获取普通交易的gas上限
     *
     * @param transaction 交易对象
     * @return gas 上限
     */
    private static BigInteger getTransactionGasLimit(Transaction transaction) {
        BigInteger gasLimit = BigInteger.ZERO;
        try {
            EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(transaction).send();
            //web3j.ethBlockNumber().send().getBlockNumber();

            gasLimit = ethEstimateGas.getAmountUsed();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gasLimit;
    }

    /**
     * 获取账号交易次数 nonce
     *
     * @param address 钱包地址
     * @return nonce
     */
    private static BigInteger getTransactionNonce(String address) {
        BigInteger nonce = BigInteger.ZERO;
        try {
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
            nonce = ethGetTransactionCount.getTransactionCount();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("TransactionNonce = " + nonce);
        return nonce;
    }


    public static BigInteger getTransactionGasLimit(Web3j web3j, Transaction transaction) {
        try {
            EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(transaction).send();
            if (ethEstimateGas.hasError()){
                throw new RuntimeException(ethEstimateGas.getError().getMessage());
            }
            return ethEstimateGas.getAmountUsed();
        } catch (IOException e) {
            throw new RuntimeException("net error");
        }
    }

    /**
     * 发送一个普通交易
     *
     * @return 交易 Hash
     */
    private static String sendTransaction() {
        BigInteger unlockDuration = BigInteger.valueOf(60L);
        BigDecimal amount = new BigDecimal("0.0001");
        String txHash = null;
        try {
            PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(fromAddress, password, unlockDuration).send();
            if (personalUnlockAccount.accountUnlocked()) {
                BigInteger value = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
                //Transaction transaction = makeTransaction(fromAddress, toAddress,
                //        null, null, BigInteger.valueOf(25000), value);
                //不是必须的 可以使用默认值
                BigInteger gasLimit = null;
                //不是必须的 缺省值就是正确的值
                BigInteger nonce = getTransactionNonce(fromAddress);
                //该值为大部分矿工可接受的gasPrice
                BigInteger gasPrice = Convert.toWei(defaultGasPrice, Convert.Unit.GWEI).toBigInteger();
                Transaction transaction = makeTransaction(fromAddress, toAddress,
                        nonce, gasPrice,
                        gasLimit, value);
                EthSendTransaction ethSendTransaction = web3j.ethSendTransaction(transaction).send();
                txHash = ethSendTransaction.getTransactionHash();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("tx hash " + txHash);
        return txHash;
    }

    //使用 web3j.ethSendRawTransaction() 发送交易 需要用私钥自签名交易 详见ColdWallet.java

}
