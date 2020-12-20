package com.hyhello.priceless.block;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalListAccounts;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.ipc.WindowsIpcService;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 *
 */
public class ETHAccount {

    //0xd4278788d4f730ce42a0448c7d7a50b5ca9b17bd
    private static Admin admin;
    private static Web3j web3j;


    public static void main(String[] args) throws IOException {
        admin = Admin.build(new HttpService("https://mainnet.infura.io/v3/1f0898be26f54849b38479f6c9ce76ff"));
        //admin = Admin.build(new HttpService(ETH.RPC_URL));

        //createNewAccount();
        //getAccountList();
        //unlockAccount();

        System.out.println(getEthBanlance(admin, "0x88078d0F3A25bcBA6757d6045E43F1A64fD2783a"));
//		admin.personalSendTransaction(); 该方法与web3j.sendTransaction相同 不在此写例子。
    }

    public static String getEthBanlance(Web3j web3j,String userAddress) throws IOException {
        //获取指定钱包的以太币余额
        BigInteger integer=web3j.ethGetBalance(userAddress,DefaultBlockParameterName.LATEST).send().getBalance();


        //eth默认会部18个0这里处理比较随意
        String decimal = toDecimal(18,integer);
        System.out.println(decimal);


        //因为是按18位来算的,所有在倒数18位前面加 小数点
		/*String str = integer.toString();
		String decimal = toDecimal(18,str);
		System.out.println(decimal);*/
        return decimal;
    }

    public static String toDecimal(int decimal,BigInteger integer){
//		String substring = str.substring(str.length() - decimal);
        StringBuffer sbf = new StringBuffer("1");
        for (int i = 0; i < decimal; i++) {
            sbf.append("0");
        }
        String balance = new BigDecimal(integer).divide(new BigDecimal(sbf.toString()), 18, BigDecimal.ROUND_DOWN).toPlainString();
        return balance;
    }

    /**
     * 创建账号
     */
    private static void createNewAccount() {
        try {
            NewAccountIdentifier newAccountIdentifier = admin.personalNewAccount(ETH.password).send();
            String address = newAccountIdentifier.getAccountId();
            System.out.println("new account address " + address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BigDecimal getBalance(Web3j web3j, String address) {
        try {
            EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            return Convert.fromWei(new BigDecimal(ethGetBalance.getBalance()),Convert.Unit.ETHER);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取账号列表
     */
    private static void getAccountList() {
        try {
            PersonalListAccounts personalListAccounts = admin.personalListAccounts().send();
            List<String> addressList;
            addressList = personalListAccounts.getAccountIds();
            System.out.println("account size " + addressList.size());
            for (String address : addressList) {
                System.out.println(address);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 账号解锁
     */
    private static void unlockAccount() {
        String address = "0xd4278788d4f730ce42a0448c7d7a50b5ca9b17bd";
        //账号解锁持续时间 单位秒 缺省值300秒
        BigInteger unlockDuration = BigInteger.valueOf(60L);
        try {
            PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(address, ETH.password, unlockDuration).send();
            System.out.println(personalUnlockAccount.toString());
            Boolean isUnlocked = personalUnlockAccount.accountUnlocked();
            System.out.println("account unlock " + isUnlocked);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
