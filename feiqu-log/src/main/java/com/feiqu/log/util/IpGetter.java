package com.feiqu.log.util;


import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * IP工具类
 *
 * @author chenlongfei
 * @version 2017-03-16
 * @see IpGetter
 */
public class IpGetter {

    /**
     * 单网卡名称
     */
    private static final String NETWORK_CARD = "eth0";
    /**
     * 绑定网卡名称
     */
    private static final String NETWORK_CARD_BAND = "bond0";

    /**
     * 获取本机的局域网ip地址，兼容Linux
     * @return String
     * @throws Exception
     */
    public static String getIp(){
        String localHostAddress = "";
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();

            while(allNetInterfaces.hasMoreElements()){
                NetworkInterface networkInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> address = networkInterface.getInetAddresses();
                while(address.hasMoreElements()){
                    InetAddress inetAddress = address.nextElement();
                    if(inetAddress != null && inetAddress instanceof Inet4Address){
                        localHostAddress = inetAddress.getHostAddress();
                    }
                }
            }
        }catch (Exception e){

        }
        return localHostAddress;
    }
}