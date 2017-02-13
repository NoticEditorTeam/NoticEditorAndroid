package com.noticeditorteam.noticeditorandroid.io;

import com.noticeditorteam.noticeditorandroid.model.NoticeItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import static com.noticeditorteam.noticeditorandroid.io.JsonFields.KEY_CHILDREN;
import static com.noticeditorteam.noticeditorandroid.io.JsonFields.KEY_CONTENT;
import static com.noticeditorteam.noticeditorandroid.io.JsonFields.KEY_TITLE;

public class JsonFormat {

    public static JsonFormat with(File file) { return new JsonFormat(file); }

    private final File file;

    private JsonFormat(File file) { this.file = file; }

    public NoticeItem importDocument() throws IOException, JSONException {
        JSONObject json = new JSONObject(IOUtil.readContent(file));

        return jsonToTree(json);
    }

    private NoticeItem jsonToTree(JSONObject json) throws JSONException {
        NoticeItem item = new NoticeItem(json.getString(KEY_TITLE), json.optString(KEY_CONTENT, null));
        JSONArray arr = json.getJSONArray(KEY_CHILDREN);
        for(int i = 0; i < arr.length(); ++i) {
            item.addChild(jsonToTree(arr.getJSONObject(i)));
        }
        return item;
    }
}
