package org.sse.http;

import java.util.HashMap;

/**
 * @author tcowan
 */
public class MimeTypes {
    public static HashMap mimeMap = new HashMap();
    static {
        mimeMap.put("", "content/unknown");
        mimeMap.put("uu", "application/octet-stream");
        mimeMap.put("exe", "application/octet-stream");
        mimeMap.put("ps", "application/postscript");
        mimeMap.put("zip", "application/zip");
        mimeMap.put("sh", "application/x-shar");
        mimeMap.put("tar", "application/x-tar");
        mimeMap.put("snd", "audio/basic");
        mimeMap.put("au", "audio/basic");
        mimeMap.put("wav", "audio/x-wav");
        mimeMap.put("gif", "image/gif");
        mimeMap.put("jpg", "image/jpeg");
        mimeMap.put("jpeg", "image/jpeg");
        mimeMap.put("htm", "text/html");
        mimeMap.put("html", "text/html");
        mimeMap.put("text", "text/plain");
        mimeMap.put("c", "text/plain");
        mimeMap.put("cc", "text/plain");
        mimeMap.put("c++", "text/plain");
        mimeMap.put("h", "text/plain");
        mimeMap.put("pl", "text/plain");
        mimeMap.put("txt", "text/plain");
        mimeMap.put("java", "text/plain");
        mimeMap.put("css", "text/css");
        mimeMap.put("xml", "text/xml");
    };

    public static String getContentType(String extension) {
        String contentType = (String) mimeMap.get(extension);
        if (contentType == null)
            contentType = "unkown/unkown";
        return contentType;
    }

}