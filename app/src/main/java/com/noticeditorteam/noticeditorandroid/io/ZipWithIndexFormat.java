package com.noticeditorteam.noticeditorandroid.io;

import com.noticeditorteam.noticeditorandroid.model.NoticeItem;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ZipWithIndexFormat {

    private static final String INDEX_JSON = "index.json";

    private static final String BRANCH_PREFIX = "branch_";
    private static final String NOTE_PREFIX = "note_";

    public static ZipWithIndexFormat with(File file) throws ZipException {
        return new ZipWithIndexFormat(file);
    }

    private final Set<String> paths;
    private final ZipFile zip;
    private final ZipParameters parameters;

    private ZipWithIndexFormat(File file) throws ZipException {
        paths = new HashSet<>();
        zip = new ZipFile(file);
        parameters = new ZipParameters();
    }

    public NoticeItem importDocument() throws IOException, JSONException, ZipException {
        String indexContent = readFile(INDEX_JSON);
        if (indexContent.isEmpty()) {
            throw new IOException("Invalid file format");
        }

        JSONObject index = new JSONObject(indexContent);
        return readNotices("", index);
    }

    private String readFile(String path) throws IOException, ZipException {
        FileHeader header = zip.getFileHeader(path);
        if (header == null)
            return "";
        try (InputStream is = zip.getInputStream(header)) {
            return IOUtil.stringFromStream(is);
        }
    }

    private byte[] readBytes(String path) throws IOException, ZipException {
        FileHeader header = zip.getFileHeader(path);
        if (header == null)
            return new byte[0];

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = zip.getInputStream(header)) {
            IOUtil.copy(is, baos);
        }
        baos.flush();
        return baos.toByteArray();
    }

    private NoticeItem readNotices(String dir, JSONObject index) throws IOException, JSONException, ZipException {
        final String title = index.getString(JsonFields.KEY_TITLE);
        final String filename = index.getString(JsonFields.KEY_FILENAME);
        final String dirPrefix = index.has(JsonFields.KEY_CHILDREN) ? BRANCH_PREFIX : NOTE_PREFIX;

        final String newDir = dir + dirPrefix + filename + "/";
        if (index.has(JsonFields.KEY_CHILDREN)) {
            JSONArray children = index.getJSONArray(JsonFields.KEY_CHILDREN);
            NoticeItem branch = new NoticeItem(title);
            for (int i = 0; i < children.length(); i++) {
                branch.addChild(readNotices(newDir, children.getJSONObject(i)));
            }
            return branch;
        } else {
            // ../note_filename/filename.md
            final String mdPath = newDir + filename + ".md";
            final NoticeItem item = new NoticeItem(title, readFile(mdPath));
            item.setPath(newDir);
            return item;
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Export">
    public void export(NoticeItem notice) throws IOException, JSONException, ZipException {
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        parameters.setSourceExternalStream(true);

        JSONObject index = new JSONObject();
        writeNoticesAndFillIndex("", notice, index);
        storeFile(INDEX_JSON, index.toString());
    }

    private void storeFile(String path, String content) throws IOException, ZipException {
        parameters.setFileNameInZip(path);
        try (InputStream stream = IOUtil.toStream(content)) {
            zip.addStream(stream, parameters);
        }
    }

    private void storeFile(String path, byte[] data) throws IOException, ZipException {
        parameters.setFileNameInZip(path);
        try (InputStream stream = new ByteArrayInputStream(data)) {
            zip.addStream(stream, parameters);
        }
    }

    private void writeNoticesAndFillIndex(String dir, NoticeItem item, JSONObject index) throws IOException, JSONException, ZipException {
        final String title = item.getTitle();
        final String dirPrefix = item.isBranch() ? BRANCH_PREFIX : NOTE_PREFIX;
        String filename = IOUtil.sanitizeFilename(title);

        String newDir = dir + dirPrefix + filename;
        if (paths.contains(newDir)) {
            // solve collision
            int counter = 1;
            String newFileName = filename;
            while (paths.contains(newDir)) {
                newFileName = String.format("%s_(%d)", filename, counter++);
                newDir = dir + dirPrefix + newFileName;
            }
            filename = newFileName;
        }
        paths.add(newDir);

        index.put(JsonFields.KEY_TITLE, title);
        index.put(JsonFields.KEY_FILENAME, filename);

        if (item.isBranch()) {
            // ../branch_filename
            ArrayList list = new ArrayList();
            for(NoticeItem child : item.getChildren()) {
                JSONObject indexEntry = new JSONObject();
                writeNoticesAndFillIndex(newDir + "/", child, indexEntry);
                list.add(indexEntry);
            }
            index.put(JsonFields.KEY_CHILDREN, new JSONArray(list));
        } else {
            // ../note_filename/filename.md
            storeFile(newDir + "/" + filename + ".md", item.getContent());
        }
    }
}