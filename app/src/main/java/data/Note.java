package data;

import java.io.Serializable;

/**
 * Created by Song on 2015/4/27.
 */
public class Note  implements Serializable {

    private static final long serialVersionUID = -1271473775017274050L;
    private int _id;
    private String content;
    private String _time;


    public int get_id() {
        return _id;
    }
    public void set_id(int _id) {
        this._id = _id;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String get_time() {
        return _time;
    }
    public void set_time(String _time) {
        this._time = _time;
    }
    @Override
    public String toString() {
        return content;
    }


}

