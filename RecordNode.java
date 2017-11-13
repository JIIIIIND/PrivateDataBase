import java.util.ArrayList;

public class RecordNode {
    private ArrayList<Object> _data;
    private RecordNode _next;
    private RecordNode _prior;


    public RecordNode()
    {
        _data = new ArrayList();
    }

    public void set_data(Object data) {
        this._data.add(data);
    }

    public Object get_data(int index) { return _data.get(index); }

    public ArrayList get() {
        return _data;
    }

    public RecordNode get_next() {
        return _next;
    }

    public void set_next(RecordNode p) {
        _next = p;
    }

    public RecordNode get_prior() {
        return _prior;
    }

    public void set_prior(RecordNode p) {
        _prior = p;
    }


}
