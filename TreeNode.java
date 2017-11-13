import java.util.ArrayList;

public class TreeNode
{
    private TreeNode _left;
    private TreeNode _right;
    private int balance;
    private ArrayList<Object> _data;

    public TreeNode()
    {
        _data = new ArrayList();
        balance = 0;
    }

    public void set_data(Object data) { this._data.add(data); }

    public Object get_data(int index) { return _data.get(index); }

    public TreeNode get_left() {return _left;}

    public void set_left(TreeNode p) {_left = p;}

    public TreeNode get_right() {return _right; }

    public void set_right(TreeNode p) {_right = p;}

    public int getBalance() {return balance;}

    public void setBalance(int value) {balance = value;}

    public int hasChild()
    {
        if((this._left != null) && (this._right != null))
        {
            return 0;
        }
        else if((this._left == null) && (this._right != null))
        {
            return 1;
        }
        else if((this._left != null) && (this._right == null))
        {
            return 2;
        }
        else
        {
            return 3;
        }
    }
}
