package com.zefu.common.core.utils.bus;


public class ArrayUtil {

    public static String[] copyString(String[] source){
        if(isEmpty(source)){
            return null;
        }
        int len = source.length;
        String[] arr = new String[len];
        for(int i=0; i < len; i ++){
            arr[i] = source[i];
        }
        return arr;
    }


    public static String concat(String[] source, String split){
        if(isEmpty(source)){
            return null;
        }
        String result = "";
        for(int i=0; i < source.length; i ++){
            result = result.concat(source[i]);
             if(i != source.length - 1){
                 result = result.concat(split);
             }
        }
        return result;
    }

    public static boolean isEmpty(String[] source){
        if(null == source){
            return true;
        }
        if(0 == source.length){
            return true;
        }
        return false;
    }
}
