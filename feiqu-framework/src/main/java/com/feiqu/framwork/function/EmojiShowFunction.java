package com.feiqu.framwork.function;

import com.feiqu.common.utils.EmojiUtils;
import org.beetl.core.Context;
import org.beetl.core.Function;

/**
 * Created by chenweidong on 2018/1/10.
 */
public class EmojiShowFunction implements Function {
    public Object call(Object[] params, Context context) {
        if (params.length != 1){
            throw new RuntimeException("length of params must be 1 !");
        }
        if(params[0] == null){
            return null;
        }
        if (params[0].toString().length()==0){
            return null;
        }


        return EmojiUtils.toUnicode((String) params[0]);
    }
}
