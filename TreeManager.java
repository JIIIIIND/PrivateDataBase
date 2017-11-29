import sun.security.jca.GetInstance;

public class TreeManager {

    private static TreeManager _instance;
    private TreeNode _root;

    private TreeManager(){}

    public static TreeManager GetInstance()
    {
        if(_instance == null)
            _instance = new TreeManager();
        return _instance;
    }

    public void PrintInorder(TreeNode root)
    {
        if(root != null)
        {
            PrintInorder(root.get_left());
            System.out.println("data: "+root.get_data(0));
            PrintInorder(root.get_right());
        }
    }

    public void add(TreeNode newNode, TreeNode root)
    {
        addNode(newNode, root);
        while( !isBalanced(_root,true))
        {
            CheckBalance(_root);
            Balance(_root);
        }
    }

    public void addNode(TreeNode newNode, TreeNode root)
    {
        TreeNode current = root;
        if(_root == null)
        {
            _root = newNode;
            return;
        }
        String newString = newNode.get_data(DataBase.get_sortField()).toString();
        String curString = current.get_data(DataBase.get_sortField()).toString();
        boolean isLeft = false;

        switch(DataBase.get_fields()[DataBase.get_sortField()].getType())
        {
            case STRING:
                if(newString.compareTo(curString) < 0)
                    isLeft = true;
                else
                    isLeft = false;
                break;
            case CHAR:
                if(newString.compareTo((curString))<0)
                    isLeft = true;
                else
                    isLeft = false;
            case INT:
                if(Integer.parseInt(newString) < Integer.parseInt(curString))
                    isLeft = true;
                else
                    isLeft = false;
                break;
            case DOUBLE:
                if(Double.parseDouble(newString) < Double.parseDouble(curString))
                    isLeft = true;
                else
                    isLeft = false;
                break;
        }
        if(isLeft)
        {
            if((current.hasChild() == 0) || (current.hasChild() == 1))
                addNode(newNode,current.get_left());
            else
                current.set_left(newNode);
        }
        else
        {
            if((current.hasChild() == 0) || (current.hasChild() == 2))
                addNode(newNode, current.get_right());
            else
                current.set_right(newNode);
        }
        Balance(_root);
    }


    private TreeNode RightRotate(TreeNode root)
    {
        TreeNode temp = root.get_left();
        root.set_left(temp.get_right());
        temp.set_right(root);
        return temp;
    }

    private TreeNode LeftRotate(TreeNode root)
    {
        TreeNode temp = root.get_right();
        root.set_right(temp.get_left());
        temp.set_left(root);
        return temp;
    }

    private int Maxlevel(TreeNode root, int level, int Max)
    {
        if(root != null)
        {
            int left = Maxlevel(root.get_left(),level+1,Max);
            int right = Maxlevel(root.get_right(),level+1,Max);
            if(left > right)
                Max = left+1;
            else
                Max = right+1;
        }
        else
        {
            if(level > Max)
                Max = level;
        }
        return Max-1;
    }

    //root노드의 밸런스를 넣어준다.
    private void BalanceOperation(TreeNode root)
    {
        int leftLevel = Maxlevel(root.get_left(), 1, 0);

        int rightLevel = Maxlevel(root.get_right(),1,0);

        root.setBalance(leftLevel-rightLevel);
    }

    public void Balance(TreeNode root)
    {
        if(root != null)
        {
            Balance(root.get_left());
            BalanceOperation(root);
            Balance(root.get_right());
        }
    }

    public TreeNode Rotate(TreeNode root)
    {
        if(root.getBalance() > 1)
        {
            if(root.get_left().getBalance() != -1)
            {
                root = (RightRotate(root));
            }
            else
            {
                root.set_left(LeftRotate(root.get_left()));
                root = (RightRotate(root));
            }
        }
        else if(root.getBalance() < -1)
        {
            if(root.get_right().getBalance() != 1)
            {
                root = (LeftRotate(root));
            }
            else
            {
                root.set_right(RightRotate(root.get_right()));
                root = (LeftRotate(root));
            }
        }
        Balance(_root);
        return root;
    }

    private boolean isBalanced(TreeNode root,boolean escape)
    {
        if(root != null)
        {
            if(root.getBalance() < -1 || root.getBalance() > 1)
            {
                escape = false;
            }
            isBalanced(root.get_left(),escape);
            isBalanced(root.get_right(),escape);
        }
        return escape;
    }

    public TreeNode CheckBalance(TreeNode root)
    {
        switch(root.hasChild())
        {
            case 0:
                root.set_left(CheckBalance(root.get_left()));
                root.set_right(CheckBalance(root.get_right()));
                if(root == _root)
                    _root = Rotate(root);
                else
                    root = Rotate(root);
                break;
            case 1:
                root.set_left(CheckBalance(root.get_left()));
                if(root == _root)
                    _root = Rotate(root);
                else
                    root = Rotate(root);
                break;
            case 2:
                root.set_right(CheckBalance(root.get_right()));
                if(root == _root)
                    _root = Rotate(root);
                else
                    root = Rotate(root);
                break;
            case 3:
                return root;
        }
        return root;
    }

    public TreeNode getRoot() {return _root;}
}
