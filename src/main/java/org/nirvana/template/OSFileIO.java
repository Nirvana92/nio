package org.nirvana.template;

import java.io.*;

public class OSFileIO {
    File file = new File("~/test.txt");
    byte[] bytes = "123456789".getBytes();

    /**
     * 原始的file输入流输入内容到磁盘
     */
    public void fileInput() throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        while (true) {
            // 这里不使用flush. flush 是强制脏页刷盘. 是以一页[4k]写到磁盘
            fos.write(bytes);
        }
    }

    /**
     * buffer 输入流输入内容到磁盘
     *
     * buffer 快还是file output 快
     *
     * buffer 减少减少系统调用
     */
    public void bufferInput() throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        while (true) {
            bos.write(bytes);
        }
    }

    public void accessRandomFile() {
        // filechannel 
    }
}
