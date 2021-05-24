package com.whopuppy.listener;

import com.nhncorp.lucy.security.xss.event.ElementListener;
import com.nhncorp.lucy.security.xss.listener.WhiteUrlList;
import com.nhncorp.lucy.security.xss.markup.Element;

public class ImgListener implements ElementListener {
    public void handleElement(Element element) {

        if (element.isDisabled()) {
            return;
        }
        if((element.getAttributes()==null || element.getAttributes().size()==0 ) && (element.getContents()==null  || element.getContents().size()==0)){
            return;
        }

        String srcUrl = element.getAttributeValue("src");
        boolean isWhiteUrl = this.isWhiteUrl(srcUrl);

        if (!isWhiteUrl) {
            element.setEnabled(false);
            return;
        }
    }

    private boolean isWhiteUrl(String url) {
        WhiteUrlList list = WhiteUrlList.getInstance();
        if (list != null && list.contains(url)) {
            return true;
        }
        return false;
    }
}