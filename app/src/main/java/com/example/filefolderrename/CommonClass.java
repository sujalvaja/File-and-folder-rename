package com.example.filefolderrename;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;

public class CommonClass {
    public static String videoFile_path = "";
    public static Uri uriVideo;

    public static String getInternalFolder(Context context) {
        File file1 = context.getExternalFilesDir("VideoToPDF");
        String relativePath = file1.getAbsolutePath() + File.separator + "PDF";
        if(!new File(relativePath).exists()){
            new File(relativePath).mkdir();
        }
        return relativePath;
    }

    public static File from(Context context, Uri uri) throws IOException {
        File rename = null;
        try {
            File file = null;
            InputStream openInputStream = context.getContentResolver().openInputStream(uri);
            String fileName = getFileName(uri, context);
            String[] splitFileName = splitFileName(fileName);
            try {
                file = File.createTempFile(splitFileName[0], splitFileName[1]);
            } catch (Exception unused) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                file = File.createTempFile(timestamp.getTime() + "", splitFileName[1]);
            }
            rename = rename(file, fileName);
            rename.deleteOnExit();
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(rename);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (openInputStream != null) {
                copy(openInputStream, fileOutputStream);
                openInputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rename;
    }

    public static File rename(File file, String str) {

        File file2 = new File(file.getParent(), str);
        if (!file2.equals(file)) {
            if (file2.exists() && file2.delete()) {
                Log.d("FileUtil", "Delete old " + str + " file");
            }
            if (file.renameTo(file2)) {
                Log.d("FileUtil", "Rename file to " + str);
            }
        }
        return file2;
    }

    @SuppressLint("Range")
    public static String getFileName(Uri uri, Context context) {
        String result = null;
        try {
            if (uri.getScheme().equals("content")) {
                Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    assert cursor != null;
                    cursor.close();
                }

            }
            if (result == null) {
                result = uri.getPath();
                int cut = result.lastIndexOf('/');
                if (cut != -1) {
                    result = result.substring(cut + 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String[] splitFileName(String str) {
        String str2;
        int lastIndexOf = str.lastIndexOf(".");
        if (lastIndexOf != -1) {
            String substring = str.substring(0, lastIndexOf);
            str2 = str.substring(lastIndexOf);
            str = substring;
        } else {
            str2 = "";
        }
        return new String[]{str, str2};
    }

    private static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[4096];
        long j = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (-1 == read) {
                return;
            }
            outputStream.write(bArr, 0, read);
            j += (long) read;
        }
    }


    public static String toBinary(String str, int bits) {
        String result = "";
        String tmpStr;
        int tmpInt;
        char[] messChar = str.toCharArray();

        for (int i = 0; i < messChar.length; i++) {
            tmpStr = Integer.toBinaryString(messChar[i]);
            tmpInt = tmpStr.length();
            if (tmpInt != bits) {
                tmpInt = bits - tmpInt;
                if (tmpInt == bits) {
                    result += tmpStr;
                } else if (tmpInt > 0) {
                    for (int j = 0; j < tmpInt; j++) {
                        result += "0";
                    }
                    result += tmpStr;
                } else {
                    System.err.println("argument 'bits' is too small");
                }
            } else {
                result += tmpStr;
            }
            result += ""; // separator
        }

        return result;
    }
    public static ArrayList<File>  datalist = new ArrayList<>();
    public static ArrayList<File> Search_Dir(File dir) {
        File[] filelist = dir.listFiles();
        if (filelist != null) {
            for (int i = 0; i < filelist.length; i++) {
                if (filelist[i].isDirectory()) {
//                    Log.e("Tag", "name --- " + FileList[i].name)
                    Search_Dir(filelist[i]);
                } else {
                    if (filelist[i].getName().endsWith(".pdf")) {
//                            Log.e("Tag", "name --- " + FileList[i].path)
                        datalist.add(filelist[i]);
                    }
                }
            }
        }
        return datalist;
    }
}
