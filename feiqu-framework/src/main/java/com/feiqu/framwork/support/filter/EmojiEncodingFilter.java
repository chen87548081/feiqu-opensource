package com.feiqu.framwork.support.filter;

import com.feiqu.common.utils.EmojiUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by chenweidong on 2018/1/10.
 */
public class EmojiEncodingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        request = new HttpServletRequestWrapper(request) {

            @Override
            public String getParameter(String name) {
                // 参数名
                // 返回值之前 先进行 Emoji 转化
                return EmojiUtils.toAliases(super.getParameter(name));
            }

            @Override
            public String[] getParameterValues(String name) {
                // 参数值
                // 返回值之前 先进行 Emoji 转化
                String[] values = super.getParameterValues(name);
                if (values != null) {
                    for (int i = 0; i < values.length; i++) {
                        values[i] = EmojiUtils.toAliases(values[i]);
                    }
                }
                return values;
            }

        };

        filterChain.doFilter(request, response);
    }

}
