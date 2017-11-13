import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Record implements KeyListener
{
    private RecordNode _first;
    private RecordNode _last;
    private RecordNode _currentRec;
    private Field[] _fields;
    private int _index;
    private RecordNode p;
    private static Record _record;

    private JLabel Title;
    private JLabel value;
    private JTextField textField1;
    private JButton enterButton;
    private JPanel panel1;
    private JFrame frame;

    private Record() {}
    public static Record getInstance()
    {
        if(_record == null)
        {
            _record = new Record();
        }
        return _record;
    }
    //레코드의 내용을 전부 초기화 한다
    private void Init()
    {
        RecordNode temp;

        //마지막 레코드부터 초기화 실시
        while (_record._last != null)
        {
            temp = _record._last.get_prior();
            if (temp != null)
            {
                _record._last.set_data(null);
                _record.set_last(null);
            }
            _record.set_last(temp);
        }

        //first의 값이 있다면 초기화
        if (_record.get_first() != null)
        {
            _record.get_first().set_data(null);
            _record.set_first(null);
        }
        //현재 레코드의 값이 있다면 초기화
        if (_record.get_currentRec() != null)
        {
            _record.get_currentRec().set_data(null);
            _record.set_currentRec(null);
        }
    }

    private void MakeForm(String title, String val)
    {
        set_fields();
        ActionListenerInit();
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.add(panel1);
        frame.setSize(400, 300);
        frame.setVisible(true);
        Title.setText(title);
        value.setText(val);
    }

    //테이블 생성
    private DefaultTableModel MakeModel()
    {
        set_fields();
        int fieldCount = DataBase.get_fieldCount();
        DefaultTableModel model;
        int recordIndex = 1;
        String[] head = {"RecordNumber", "Field", "Record"};
        String[][] contents = new String[fieldCount][3];

        if ((_currentRec != null) && _first != null)
        {
            RecordNode p = _first;
            while (p != _currentRec)
            {
                recordIndex++;
                p = p.get_next();
            }

        }
        for (int i = 0; i < fieldCount; i++)
        {
            contents[i][1] = _fields[i].getName();      //Field의 Name넣기

            if (_currentRec != null && (_currentRec.get().size() >= fieldCount))
            {
                contents[i][2] = _currentRec.get_data(i).toString();
            }
            else
            {
                contents[i][2] = null;
            }
            contents[i][0] = Integer.toString(recordIndex);
        }
        model = new DefaultTableModel(contents, head)
        {
            //앨범 수정 막음
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        return model;
    }

    //기존 C언어에서 쓰인 방식대로 정렬, 문자는 처음 값부터 비교해서 더 작은 값이 앞에, 숫자는 더 작은 값이 앞으로
    private void DlsStore(RecordNode newNode)
    {
        Type type = DataBase.get_fields()[DataBase.get_sortField()].getType();
        String pString;
        String newString;
        RecordNode old, p;
        if (_last == null)
        {
            newNode.set_next(null);
            newNode.set_prior(null);
            _last = newNode;
            _first = newNode;
            return;
        }
        p = _first;
        old = null;

        while (p != null)
        {
            pString = p.get_data(DataBase.get_sortField()).toString();
            newString = newNode.get_data(DataBase.get_sortField()).toString();

            if (pString.isEmpty() && (type == Type.INT))
            {
                pString = "0";
            }
            else if (type == Type.DOUBLE)
            {
                pString = new String("0");
            }
            if (newString.isEmpty() && ((type == Type.INT) | (type == Type.DOUBLE)))
            {
                newString = new String("0");
            } else if (type == Type.DOUBLE)
            {
                newString = new String("0");
            }
            if (((type == Type.STRING) || (type == Type.CHAR)) && (pString.compareTo(
                    newString) < 0))
            {
                old = p;
                p = p.get_next();
            }
            else if (((type == Type.INT) && (Integer.parseInt(pString) < Integer.parseInt(newString))))
            {
                old = p;
                p = p.get_next();
            }
            else if (((type == Type.DOUBLE) && (Double.parseDouble(pString) < Double.parseDouble(newString))))
            {
                old = p;
                p = p.get_next();
            }
            else
            {
                if (p.get_prior() != null)
                {
                    p.get_prior().set_next(newNode);
                    newNode.set_next(p);
                    newNode.set_prior(p.get_prior());
                    p.set_prior(newNode);
                    return;
                }
                newNode.set_next(p);
                newNode.set_prior(null);
                p.set_prior(newNode);
                _first = newNode;
                return;
            }
        }
        old.set_next(newNode);
        newNode.set_next(null);
        newNode.set_prior(old);
        _last = newNode;
    }

    //Record 삭제를 수행
    private void DeleteRecord()
    {
        //제거 레코드가 첫 레코드이고 동시에 마지막은 아님
        if (_currentRec == _first)
        {
            p = _first.get_next();
            _first = p;
            if(_first != null)
                _first.set_prior(null);
            _currentRec = _first;
        }
        else
        {
            //제거 레코드는 처음이나 끝은 아님
            if (_currentRec != _last)
            {
                p = _currentRec.get_next();
                p.set_prior(_currentRec.get_prior());
                _currentRec.get_prior().set_next(p);
                _currentRec = p;
            }
            //제거 레코드가 마지막
            else
                {
                p = _currentRec.get_prior();
                p.set_next(null);
                _last = p;
                _currentRec = p;
            }
        }
    }

    //current Record부터 시작해서 last Record까지 같은 값을 가지는 항목이 있는지 조사 후 currentRecord 리턴
    private RecordNode Find(String Key, int index)
    {
        _currentRec = _first;
        for (int i = 0; _currentRec != _last; i++)
        {
            if (Key.matches(_currentRec.get_data(index).toString()))
            {
                int result = JOptionPane.showConfirmDialog(null, (i + 1) + "번 레코드에서 찾았습니다. 계속해서 찾으시겠습니까?", null, JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION)
                {
                    _currentRec = _currentRec.get_next();
                }
                else
                {
                    return _currentRec;
                }
            }
            else
            {
                _currentRec = _currentRec.get_next();
            }
        }
        if ((_currentRec == _last) && (_currentRec != null))
        {
            if (Key.matches(_currentRec.get_data(index).toString()))
            {
                JOptionPane.showMessageDialog(null, "마지막 레코드에서 찾았습니다.");
                return _currentRec;
            }
        }

        return null;
    }

    //버튼에 더해진 ActionListener를 제거함
    public void ActionListenerInit()
    {
        int listenerCount = enterButton.getActionListeners().length;
        if (listenerCount > 0)
        {
            //enterButton.removeActionListener(InsertValue);
            for(int i = 0; i < listenerCount; i++)
            {
                enterButton.removeActionListener(enterButton.getActionListeners()[i]);
            }
        }
    }

    //필드의 타입에 따라 입력 할 수 있는 키 조정
    @Override
    public void keyTyped(KeyEvent e)
    {
        char c;
        //입력되는 키와 입력 가능한 사이즈를 비교
        if (textField1.getText().length() < _fields[_index].getSize())
        {
            switch (_fields[_index].getType())
            {
                case INT:
                    c = e.getKeyChar();
                    //숫자가 아니라면 입력 안받음
                    if (!Character.isDigit(c))
                    {
                        e.consume();
                        return;
                    }
                    break;
                case DOUBLE:
                    c = e.getKeyChar();
                    //숫자거나 .만 입력 받음
                    if (!Character.isDigit(c))
                    {
                        //.이 이미 포함되어 있으면 .은 입력받지 않음
                        if (!((c == '.') && !textField1.getText().contains(".")))
                        {
                            e.consume();
                            return;
                        }
                    }
                    break;
            //switch 종료
            }
        }
        else
        {
            e.consume();
            return;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) { }

    //여기서부터 접근자, 설정자
    public void set_Init() { Init(); }
    
    public void setDlsStore(RecordNode p) { DlsStore(p); }

    public DefaultTableModel getMakeModel() { return MakeModel(); }

    public RecordNode get_first() {
        return _first;
    }

    public void set_first(RecordNode p) {
        _first = p;
    }

    public RecordNode get_last() {
        return _last;
    }

    public void set_last(RecordNode p) {
        _last = p;
    }

    public RecordNode get_currentRec() {
        return _currentRec;
    }

    public void set_currentRec(RecordNode p) {
        _currentRec = p;
    }

    public Field[] get_fields() {return _fields; }

    public void set_fields() { _fields = DataBase.get_fields(); }
    
    public int get_index() {return _index;}
    public void set_index(int i) {_index = i;}

    public RecordNode getFind(String key, int index) { return Find(key, index); }
    
    public void getMakeForm(String title, String val) {_record.MakeForm(title, val);}

    public void getDeleteRecord() { DeleteRecord(); }

    public JFrame getFrame() {return frame;}

    public JButton getEnterButton() { return enterButton; }

    public JLabel getValue() { return value; }

    public JTextField getTextField1() { return textField1; }
}
