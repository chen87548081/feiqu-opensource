package com.feiqu.framwork.util;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.HtmlUtils;

import java.beans.PropertyEditorSupport;

/**
 * @author cwd
 * @create 2017-10-16:12
 **/
public class StringEscapeEditor extends PropertyEditorSupport {
    private final Logger logger = LoggerFactory.getLogger(StringEscapeEditor.class);
    private boolean escapeHTML;// 编码HTML
    private boolean escapeJavaScript;// 编码JavaScript

    public StringEscapeEditor() {
        super();
    }

    public StringEscapeEditor(boolean escapeHTML, boolean escapeJavaScript) {
        super();
        this.escapeHTML = escapeHTML;
        this.escapeJavaScript = escapeJavaScript;
    }

    @Override
    public String getAsText() {
        Object value = getValue();
        return value != null ? value.toString() : "";
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null) {
            setValue(null);
        } else {
            String value = text;
            if (escapeHTML) {
                value = HtmlUtils.htmlEscape(value);
//                logger.info("escapeHTML > value:" + value);
            }
            if (escapeJavaScript) {
                value = StringEscapeUtils.escapeJavaScript(value);
//                logger.info("escapeJavaScript > value:" + value);
            }
            setValue(value);
        }
    }
}
