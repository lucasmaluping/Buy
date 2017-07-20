package com.lucas.buy.utils;

import android.util.Log;

import com.lucas.buy.contents.UserContents;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * 上传图片的类
 * Created by 111 on 2017/7/13.
 */

public class ImageUpdateUtil {
//    private static final String TAG = "ImageUpdateUtil";
    private static HttpURLConnection connection;
    private static DataOutputStream doutputStream;
    private static FileInputStream finputStream;

    private static String boundary = "-----------------------------1954231646874";

    /**
     * 到目前为止，仍未发现和下面的uploadFile有什么本质区别，但是就是不执行，服务器端解析的size就他妈的是0
     * @param filePath
     * @return
     */
    public static String upload(String filePath) {
        Log.i(TAG,"...upload");
        try {
            //获取HttpURLConnection实例，并对连接进行一系列对的设置。表单提交的请求头信息
            URL url = new URL(UserContents.imageGetUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10*1000);
            connection.setConnectTimeout(10*1000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(true);

            connection.setRequestProperty("Charset", "utf-8");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

            connection.setRequestMethod("POST");
            connection.setRequestProperty("connection","keep-alive");
            connection.setRequestProperty("Content-Type","multipart/form-data; boundary=----WebKitFormBoundaryCjkbdjmUGD4QJISL");

            //根据路径创建一个文件实例
            File file = new File(filePath);

            //创建一个DataOutputStream实例，并写入一系列数据。Http请求体有关
            doutputStream = new DataOutputStream(connection.getOutputStream());
            doutputStream.writeBytes(boundary);
            doutputStream.writeBytes("\r\n");
            doutputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=" + file.getName());
            doutputStream.writeBytes("\r\n");
            doutputStream.writeBytes("Content-Type:image/pjpeg");
            doutputStream.writeBytes("\r\n");
//            doutputStream.writeBytes("\r\n");



            //获取图片文件的字节流，并向url流中写入图片字节流
//            finputStream = new FileInputStream(filePath);
            finputStream = new FileInputStream(file);
            int bytesAvailable = finputStream.available();
            int maxBufferSize = 1 * 1024 * 512;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            int bytesRead = finputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                doutputStream.write(buffer, 0, bytesRead);
                bytesAvailable = finputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = finputStream.read(buffer, 0, bufferSize);
            }
            doutputStream.writeBytes("\r\n");
            doutputStream.writeBytes("-----------------------------1954231646874--");
            doutputStream.writeBytes("\r\n");
            doutputStream.writeBytes("\r\n");
            doutputStream.flush();

            //Service返回的信息
            int code = connection.getResponseCode();
            Log.i(TAG,"...code:" + code);
            if(code == 200) {
                InputStream inStream = connection.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int len = -1;
                while ((len = inStream.read(buf)) != -1) {
                    byteArrayOutputStream.write(buf, 0, len);
                }

                byteArrayOutputStream.close();
                inStream.close();
                return new String(byteArrayOutputStream.toByteArray());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static ImageUpdateUtil uploadUtil;
    private static final String BOUNDARY =  UUID.randomUUID().toString(); // 边界标识 随机生成
    private static final String PREFIX = "--";
    private static final String LINE_END = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型
    private static final String TAG = "UploadUtil";

    private ImageUpdateUtil() {

    }
    /**
     * 单例模式获取上传工具类
     * @return
     */
    public static ImageUpdateUtil getInstance() {
        if (null == uploadUtil) {
            uploadUtil = new ImageUpdateUtil();
        }
        return uploadUtil;
    }

    private int readTimeOut = 10 * 1000; // 读取超时
    private int connectTimeout = 10 * 1000; // 超时时间
    /***
     * 请求使用多长时间
     */
    private static int requestTime = 0;

    private static final String CHARSET = "utf-8"; // 设置编码

    /***
     * 上传成功
     */
    public static final int UPLOAD_SUCCESS_CODE = 1;
    /**
     * 文件不存在
     */
    public static final int UPLOAD_FILE_NOT_EXISTS_CODE = 2;
    /**
     * 服务器出错
     */
    public static final int UPLOAD_SERVER_ERROR_CODE = 3;
    protected static final int WHAT_TO_UPLOAD = 1;
    protected static final int WHAT_UPLOAD_DONE = 2;

    /**
     * android上传文件到服务器
     *
     * @param filePath
     *            需要上传的文件的路径
     * @param fileKey
     *            在网页上<input type=file name=xxx/> xxx就是这里的fileKey
     * @param RequestURL
     *            请求的URL
     */
    public void uploadFile(String filePath, String fileKey, String RequestURL,
                           Map<String, String> param) {
        if (filePath == null) {
            sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE,"文件不存在");
            return;
        }
        try {
            File file = new File(filePath);
            uploadFile(file, fileKey, RequestURL, param);
        } catch (Exception e) {
            sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE,"文件不存在");
            e.printStackTrace();
            return;
        }
    }

    /**
     * android上传文件到服务器
     *
     * @param file
     *            需要上传的文件
     * @param fileKey
     *            在网页上<input type=file name=xxx/> xxx就是这里的fileKey
     * @param RequestURL
     *            请求的URL
     * @param param
     *         post方式上传的参数
     */
    public void uploadFile(final File file, final String fileKey,
                           final String RequestURL, final Map<String, String> param) {
        if (file == null || (!file.exists())) {
            sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE,"文件不存在");
            return;
        }

        Log.i(TAG, "请求的URL=" + RequestURL);
        Log.i(TAG, "请求的fileName=" + file.getName());
        Log.i(TAG, "请求的fileKey=" + fileKey);
        new Thread(new Runnable() {  //开启线程上传文件
            @Override
            public void run() {
                toUploadFile(file, fileKey, RequestURL, param);
            }
        }).start();

    }

    private void toUploadFile(File file, String fileKey, String RequestURL,
                              Map<String, String> param) {
        String result = null;
        requestTime= 0;

        long requestTime = System.currentTimeMillis();
        long responseTime = 0;

        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(readTimeOut);
            conn.setConnectTimeout(connectTimeout);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
//          conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            /**
             * 当文件不为空，把文件包装并且上传
             */
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            StringBuffer sb = null;
            String params = "";

            /***
             * 以下是用于上传参数
             */
            if (param != null && param.size() > 0) {
                Iterator<String> it = param.keySet().iterator();
                while (it.hasNext()) {
                    sb = null;
                    sb = new StringBuffer();
                    String key = it.next();
                    String value = param.get(key);
                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
                    sb.append(value).append(LINE_END);
                    params = sb.toString();
                    Log.i(TAG, key+"="+params+"##");
                    dos.write(params.getBytes());
//                  dos.flush();
                }
            }

            sb = null;
            params = null;
            sb = new StringBuffer();
            /**
             * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
             * filename是文件的名字，包含后缀名的 比如:abc.png
             */
            sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
            sb.append("Content-Disposition:form-data; name=\"" + fileKey
                    + "\"; filename=\"" + file.getName() + "\"" + LINE_END);
            sb.append("Content-Type:image/pjpeg" + LINE_END); // 这里配置的Content-type很重要的 ，用于服务器端辨别文件的类型的
            sb.append(LINE_END);
            params = sb.toString();
            sb = null;

            Log.i(TAG, file.getName()+"=" + params+"##");
            dos.write(params.getBytes());


            /**上传文件*/
            InputStream is = new FileInputStream(file);
////            onUploadProcessListener.initUpload((int)file.length());
//            byte[] bytes = new byte[1024];
//            int len = 0;
//            int curLen = 0;
//            while ((len = is.read(bytes)) != -1) {
//                curLen += len;
//                dos.write(bytes, 0, len);
////                onUploadProcessListener.onUploadProcess(curLen);
//            }
//            is.close();
//            dos.write(LINE_END.getBytes());
//            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
//            dos.write(end_data);


            int bytesAvailable = is.available();
            int maxBufferSize = 1 * 1024 * 512;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            int bytesRead = is.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer, 0, bytesRead);
                bytesAvailable = is.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = is.read(buffer, 0, bufferSize);
            }
            is.close();
            dos.writeBytes("\r\n");
            dos.writeBytes(PREFIX + BOUNDARY + PREFIX );
//            dos.writeBytes("-----------------------------1954231646874--");
            dos.writeBytes("\r\n");
            dos.writeBytes("\r\n");


//            dos.write(LINE_END.getBytes());
//            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
//            dos.write(end_data);




            dos.flush();
//
//          dos.write(tempOutputStream.toByteArray());
            /**
             * 获取响应码 200=成功 当响应成功，获取响应的流
             */
            int res = conn.getResponseCode();
            responseTime = System.currentTimeMillis();
            this.requestTime = (int) ((responseTime-requestTime)/1000);
            Log.e(TAG, "response code:" + res);
            if (res == 200) {
                Log.e(TAG, "request success");
                InputStream input = conn.getInputStream();
                StringBuffer sb1 = new StringBuffer();
                int ss;
                while ((ss = input.read()) != -1) {
                    sb1.append((char) ss);
                }
                result = sb1.toString();
                Log.e(TAG, "result : " + result);
                sendMessage(UPLOAD_SUCCESS_CODE, "上传结果："
                        + result);
                return;
            } else {
                Log.e(TAG, "request error");
                sendMessage(UPLOAD_SERVER_ERROR_CODE,"上传失败：code=" + res);
                return;
            }
        } catch (MalformedURLException e) {
            sendMessage(UPLOAD_SERVER_ERROR_CODE,"上传失败：error=" + e.getMessage());
            e.printStackTrace();
            return;
        } catch (IOException e) {
            sendMessage(UPLOAD_SERVER_ERROR_CODE,"上传失败：error=" + e.getMessage());
            e.printStackTrace();
            return;
        }
    }

    /**
     * 发送上传结果
     * @param responseCode
     * @param responseMessage
     */
    private void sendMessage(int responseCode,String responseMessage)
    {
//        onUploadProcessListener.onUploadDone(responseCode, responseMessage);
    }

    /**
     * 下面是一个自定义的回调函数，用到回调上传文件是否完成
     *
     * @author shimingzheng
     *
     */
    public static interface OnUploadProcessListener {
        /**
         * 上传响应
         * @param responseCode
         * @param message
         */
        void onUploadDone(int responseCode, String message);
        /**
         * 上传中
         * @param uploadSize
         */
        void onUploadProcess(int uploadSize);
        /**
         * 准备上传
         * @param fileSize
         */
        void initUpload(int fileSize);
    }
    private OnUploadProcessListener onUploadProcessListener;



    public void setOnUploadProcessListener(
            OnUploadProcessListener onUploadProcessListener) {
        this.onUploadProcessListener = onUploadProcessListener;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
    /**
     * 获取上传使用的时间
     * @return
     */
    public static int getRequestTime() {
        return requestTime;
    }

    public static interface uploadProcessListener{

    }
}
