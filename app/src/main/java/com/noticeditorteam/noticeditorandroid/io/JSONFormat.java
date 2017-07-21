package com.noticeditorteam.noticeditorandroid.io;

import com.noticeditorteam.noticeditorandroid.model.NoticeItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.noticeditorteam.noticeditorandroid.io.JSONFields.KEY_CHILDREN;
import static com.noticeditorteam.noticeditorandroid.io.JSONFields.KEY_CONTENT;
import static com.noticeditorteam.noticeditorandroid.io.JSONFields.KEY_TITLE;

public class JSONFormat {

    public static JSONFormat with(File file) { return new JSONFormat(file); }

    private final File file;

    private JSONFormat(File file) { this.file = file; }

    @SuppressWarnings("WeakerAccess")
    public NoticeItem importDocument() throws IOException, JSONException {
        JSONObject json = new JSONObject(IOUtil.readContent(file));

        return JSONToTree(json);
    }

    private NoticeItem JSONToTree(JSONObject json) throws JSONException {
        NoticeItem item = new NoticeItem(json.getString(KEY_TITLE), json.optString(KEY_CONTENT, null));
        JSONArray arr = json.getJSONArray(KEY_CHILDREN);
        for(int i = 0; i < arr.length(); ++i) {
            item.addChild(JSONToTree(arr.getJSONObject(i)));
        }
        return item;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void export(NoticeItem tree) throws JSONException, IOException{
        if(file.exists()) {
            file.delete();
        }
        JSONObject json = new JSONObject();
        treeToJSON(tree, json);

        IOUtil.writeJson(file, json);
    }

    private void treeToJSON(NoticeItem tree, JSONObject json) throws JSONException {
        json.put(KEY_TITLE, tree.getTitle());
        JSONArray childrenJSON = new JSONArray();
        if(tree.isBranch()) {
            for(NoticeItem child : tree.getChildren()) {
                JSONObject childJSON = new JSONObject();
                treeToJSON(child, childJSON);
                childrenJSON.put(childJSON);
            }
        }
        else {
            json.put(KEY_CONTENT, tree.getContent());
        }
        json.put(KEY_CHILDREN, childrenJSON);
    }
}
