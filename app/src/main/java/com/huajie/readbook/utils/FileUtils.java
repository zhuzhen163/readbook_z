package com.huajie.readbook.utils;

import android.os.Environment;


import com.huajie.readbook.ZApplication;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;


/**
 * Created by newbiechen on 17-5-11.
 */

public class FileUtils {
    //采用自己的格式去设置文件，防止文件被系统文件查询到
    public static final String SUFFIX_WY = ".wy";
    public static final String SUFFIX_TXT = ".txt";
    public static final String SUFFIX_EPUB = ".epub";
    public static final String SUFFIX_PDF = ".pdf";

    //获取文件夹
    public static File getFolder(String filePath){
        File file = new File(filePath);
        //如果文件夹不存在，就创建它
        if (!file.exists()){
            file.mkdirs();
        }
        return file;
    }

    //获取文件
    public static synchronized File getFile(String filePath){
        File file = new File(filePath);
        try {
            if (!file.exists()){
                //创建父类文件夹
                getFolder(file.getParent());
                //创建文件
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    //获取Cache文件夹
    public static String getCachePath(){
        if (isSdCardExist()){
            try {
                return ZApplication.getAppContext()
                        .getExternalCacheDir()
                        .getAbsolutePath();
            }catch (Exception e){
                return ZApplication.getAppContext()
                        .getCacheDir()
                        .getAbsolutePath();
            }
        }else{
            return ZApplication.getAppContext()
                    .getCacheDir()
                    .getAbsolutePath();
        }
    }

    public static long getDirSize(File file){
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                long size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {
                return file.length();
            }
        } else {
            return 0;
        }
    }

    public static String getFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"b", "kb", "M", "G", "T"};
        //计算单位的，原理是利用lg,公式是 lg(1024^n) = nlg(1024)，最后 nlg(1024)/lg(1024) = n。
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        //计算原理是，size/单位值。单位值指的是:比如说b = 1024,KB = 1024^2
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static int getFileSizeInt(long size) {
        if (size <= 0) return 0;
        //计算单位的，原理是利用lg,公式是 lg(1024^n) = nlg(1024)，最后 nlg(1024)/lg(1024) = n。
       int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
       return digitGroups;
    }

    /**
     * 本来是获取File的内容的。但是为了解决文本缩进、换行的问题
     * 这个方法就是专门用来获取书籍的...
     *
     * 应该放在BookRepository中。。。
     * @param file
     * @return
     */
    public static String getFileContent(File file){
        Reader reader = null;
        String str = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            while ((str = br.readLine()) != null){
                //过滤空语句
                if (!str.equals("")){
                    //由于sb会自动过滤\n,所以需要加上去
                    sb.append("    "+str+"\n");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.close(reader);
        }
        return sb.toString();
    }

    //判断是否挂载了SD卡
    public static boolean isSdCardExist(){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            return true;
        }
        return false;
    }

    //递归删除文件夹下的数据
    public static synchronized void deleteFile(String filePath){
        File file = new File(filePath);
        if (!file.exists()) return;

        if (file.isDirectory()){
            File[] files = file.listFiles();
            for (File subFile : files){
                String path = subFile.getPath();
                deleteFile(path);
            }
        }
        //删除文件
        file.delete();
    }

    //获取txt文件
    public static List<File> getTxtFiles(String filePath,int layer){
        List txtFiles = new ArrayList();
        File file = new File(filePath);

        //如果层级为 3，则直接返回
        if (layer == 3){
            return txtFiles;
        }

        //获取文件夹
        File[] dirs = file.listFiles(
                pathname -> {
                    if (pathname.isDirectory() && !pathname.getName().startsWith(".")) {
                        return true;
                    }
                    //获取txt文件
                    else if(pathname.getName().endsWith(".txt")){
                        txtFiles.add(pathname);
                        return false;
                    }
                    else{
                        return false;
                    }
                }
        );
        //遍历文件夹
        for (File dir : dirs){
            //递归遍历txt文件
            txtFiles.addAll(getTxtFiles(dir.getPath(),layer+1));
        }
        return txtFiles;
    }

    //由于遍历比较耗时
    public static Single<List<File>> getSDTxtFile(){
        //外部存储卡路径
        String rootPath = Environment.getExternalStorageDirectory().getPath();
        return Single.create(new SingleOnSubscribe<List<File>>() {
            @Override
            public void subscribe(SingleEmitter<List<File>> e) throws Exception {
                List<File> files = getTxtFiles(rootPath,0);
                e.onSuccess(files);
            }
        });
//       return Single.create(new Single.OnSubscribe<List<File>>() {
//            @Override
//            public void call(SingleSubscriber<? super List<File>> singleSubscriber) {
//                List<File> files = getTxtFiles(rootPath);
//                singleSubscriber.onSuccess(files);
//            }
//        });
    }

    //获取文件的编码格式
    public static Charset getCharset(String fileName) {
        BufferedInputStream bis = null;
        Charset charset = Charset.GBK;
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            bis = new BufferedInputStream(new FileInputStream(fileName));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1)
                return charset;
            if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = Charset.UTF8;
                checked = true;
            }else if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = Charset.UTF16LE;
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = Charset.UTF16BE;
                checked = true;
            }else if (first3Bytes[0] == (byte) 0x00 && first3Bytes[1] == (byte) 0x00 && first3Bytes[2] == (byte) 0xFE){
                charset = Charset.UTF32BE;
                checked = true;
            }else if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE && first3Bytes[2] == (byte) 0x00){
                charset = Charset.UTF32LE;
                checked = true;
            }else if (first3Bytes[0] == -17 && first3Bytes[1] == -69 && first3Bytes[2] == -65){
                charset = Charset.UTF8;
                checked = true;
            }

            bis.mark(0);
            if (!checked) {
                while ((read = bis.read()) != -1) {
                    if (read >= 0xF0)
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
                            // (0x80 - 0xBF),也可能在GB编码内
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                    if (isUTF8(bis)){
                                        charset = Charset.UTF8;
                                    }else {
                                        charset = Charset.GBK;
                                    }
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(bis);
        }
        return charset;
    }

    /**
     * 是否是无BOM的UTF8格式，不判断常规场景，只区分无BOM UTF8和GBK
     *
     * @param bis
     * @return
     */
    private static boolean isUTF8(BufferedInputStream bis) throws Exception {
        bis.reset();

        //读取第一个字节
        int code = bis.read();
        do {
            BitSet bitSet = convert2BitSet(code);
            //判断是否为单字节
            if (bitSet.get(0)) {//多字节时，再读取N个字节
                if (!checkMultiByte(bis, bitSet)) {//未检测通过,直接返回
                    return false;
                }
            } else {
                //单字节时什么都不用做，再次读取字节
            }
            code = bis.read();
        } while (code != -1);
        return true;
    }

    /**
     * 将整形转为BitSet
     *
     * @param code
     * @return
     */
    private static int BYTE_SIZE = 8;
    private static BitSet convert2BitSet(int code) {
        BitSet bitSet = new BitSet(BYTE_SIZE);

        for (int i = 0; i < BYTE_SIZE; i++) {
            int tmp3 = code >> (BYTE_SIZE - i - 1);
            int tmp2 = 0x1 & tmp3;
            if (tmp2 == 1) {
                bitSet.set(i);
            }
        }
        return bitSet;
    }
    /**
     * 检测多字节，判断是否为utf8，已经读取了一个字节
     *
     * @param bis
     * @param bitSet
     * @return
     */
    private static boolean checkMultiByte( BufferedInputStream bis,BitSet bitSet) throws Exception {
        int count = getCountOfSequential(bitSet);
        byte[] bytes = new byte[count - 1];//已经读取了一个字节，不能再读取
        bis.read(bytes);
        for (byte b : bytes) {
            if (!checkUtf8Byte(b)) {
                return false;
            }
        }
        return true;
    }
    /**
     * 检测bitSet中从开始有多少个连续的1
     *
     * @param bitSet
     * @return
     */
    private static int getCountOfSequential(BitSet bitSet) {
        int count = 0;
        for (int i = 0; i < BYTE_SIZE; i++) {
            if (bitSet.get(i)) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }
    /**
     * 检测单字节，判断是否为utf8
     *
     * @param b
     * @return
     */
    private static boolean checkUtf8Byte(byte b) throws Exception {
        BitSet bitSet = convert2BitSet(b);
        return bitSet.get(0) && !bitSet.get(1);
    }

    /**
     * 判断文件的编码格式
     * @param fileName :file
     * @return 文件编码格式
     * @throws Exception
     */
    public static Charset getEncoding(String fileName){
        BufferedInputStream bis = null;
        Charset charset = Charset.GBK;
        try {
            bis = new BufferedInputStream(new FileInputStream(fileName));
            int p = (bis.read() << 8) + bis.read();
            switch (p) {
                case 0xefbb://61371
                    charset = Charset.UTF8;
                    break;
                case 0xfffe://65543
                    charset = Charset.UTF16LE;
                    break;
                case 0xfeff://65279
                    charset = Charset.UTF16BE;
                    break;
                case 0x0000FE:
                    charset = Charset.UTF32BE;
                    break;
                case 0xFFFE00:
                    charset = Charset.UTF32LE;
                    break;
                case 12668:
                    charset = Charset.UTF8;
                    break;

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            IOUtils.close(bis);
        }

        return charset;
    }
}
