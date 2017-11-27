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

    private void addNode(TreeNode newNode, TreeNode current)
    {
        String newString = (String)newNode.get_data(DataBase.get_sortField());
        String curString = (String)current.get_data(DataBase.get_sortField());
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
        //밸런스 깨진 부분이 완전히 없어질때까지 반복시키는 함수 추가 바람
        BalanceCal(_root);
        //밸런스 깨진 부분 찾아서 수정하는 메소드 추가 바람
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

    //해당 노드의 자식 노드 중 가장 높은 degree를 찾아서 반환한다.
    private int MaxLevel(TreeNode root, int level, int Max)
    {
        TreeNode p = root;
        while(p != null)
        {
            switch(p.hasChild())
            {
                case 0:
                    MaxLevel(p.get_left(), level+1, Max);
                    MaxLevel(p.get_right(), level+1, Max);
                    p = null;
                    break;
                case 1:
                    p = p.get_left();
                    level++;
                    break;
                case 2:
                    p = p.get_right();
                    level++;
                    break;
                case 3:
                    if(Max < level)
                        Max = level;
                    p = null;
                    break;
            }
        }
        return Max;
    }

    //root노드의 밸런스를 넣어준다.
    private void BalanceOperation(TreeNode root)
    {
        int leftLevel = 0;
        try {
            leftLevel = MaxLevel(root.get_left(), 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int rightLevel = MaxLevel(root.get_right(),0,0);

        root.setBalance(leftLevel-rightLevel);
    }

    //각각의 노드에 대해서 현재의 밸런스 값을 넣어준다.
    private void BalanceCal(TreeNode root)
    {
        TreeNode p = root;
        while(p != null)
        {
            switch (p.hasChild())
            {
                case 0:
                    BalanceOperation(p);
                    BalanceCal(p.get_left());
                    BalanceCal(p.get_right());
                    p = null;
                    break;
                case 1:
                    BalanceOperation(p);
                    p = p.get_left();
                    break;
                case 2:
                    BalanceOperation(p);
                    p = p.get_right();
                    break;
                case 3:
                    p.setBalance(0);
                    p = null;
                    break;
            }
        }
    }

    private TreeNode Rotate(TreeNode root)
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
        return root;
    }

    private void CheckBalance(TreeNode root)
    {
        TreeNode p = root;
        if(p == _root)
        {
            if(p.getBalance() > 1)
            {
                //LL회전 수행
                if(p.get_left().getBalance() != -1)
                {
                    _root = RightRotate(root);
                }
                //LR회전 수행
                else
                {
                    p.set_left(LeftRotate(p.get_left()));
                    _root = RightRotate(p);
                }
            }
            else if(p.getBalance() < -1)
            {
                if(p.get_right().getBalance() != 1)
                {
                    _root = LeftRotate(p);
                }
                else
                {
                    p.set_right(RightRotate(p.get_right()));
                    _root = LeftRotate(p);
                }
            }
            else return;
        }
        else
        {
            while (p != null)
            {
                switch (p.hasChild())
                {
                    case 0:
                        p.set_left(Rotate(p.get_left()));
                        p.set_right(Rotate(p.get_right()));

                        CheckBalance(p.get_left());
                        CheckBalance(p.get_right());
                        break;
                    case 1:
                        p.set_left(Rotate(p.get_left()));
                        p = p.get_left();
                        break;
                    case 2:
                        p.set_right(Rotate(p.get_right()));
                        p = p.get_right();
                        break;
                    case 3:
                        p = null;
                        break;
                }
            }
        }
    }
}
