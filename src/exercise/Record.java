package exercise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Record implements KeyListener{

    private RecordNode _first;
    private RecordNode _last;
    private RecordNode _currentRec;
    private Field[] _fields;
    private int _index;
    private Record record;
    private RecordNode p;

    private JPanel panel1;
    private JTextField textField1;
    private JButton enterButton;
    private  JLabel value;
    private JLabel Title;
    private  JFrame frame;

    private ActionListener InsertRecord;
    private ActionListener InsertValue;

    public void Enter()
    {
        if((InsertValue != null) && (enterButton.getActionListeners().length > 0))
        {
            enterButton.removeActionListener(InsertValue);
            System.out.println("Action Change");
        }
        frame = new JFrame();
        frame.add(panel1);
        frame.setSize(400,300);
        frame.setVisible(true);
        Title.setText("Enter Record");

        InsertRecord = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if((_fields[_index].getType() == Type.INT) && (textField1.getText() == ""))
                {
                    p.set_data(0);
                }
                else if((_fields[_index].getType() == Type.DOUBLE) && (textField1.getText() == ""))
                {
                    p.set_data(0);
                }
                else
                {
                    p.set_data(textField1.getText());
                }
                _index++;
                if(_index < DataBaseForm.get_fieldCount())
                {
                    record.Initialize();
                    textField1.setText("");
                }
                else
                {
                    textField1.setText("");
                    DlsStore(p);
                    frame.dispose();
                    _currentRec = _last;
                    enterButton.removeActionListener(InsertRecord);
                    DataBaseForm.getDataForm().setModel();
                }
            }
        };

        enterButton.addActionListener(InsertRecord);
        _index = 0;
        p = new RecordNode();
        textField1.addKeyListener(this);
        record.Initialize();
    }

    public void Initialize()
    {
        if(DataBaseForm.getFields() == null)
        {
            DataBaseForm.ShowMessage("Define 또는 Load를 먼저 실행하십시오");
            return;
        }

        _fields = DataBaseForm.getFields();

        if(_fields[_index] == null)                                  //하나라도 값이 비었다면 전체가 비었기 때문에
        {
            DataBaseForm.ShowMessage("Field의 값을 정확히 입력 후 실행하십시오");
            return;
        }

        value.setText(_fields[_index].getName());
    }

    private void EnterInformation()
    {
        enterButton.removeActionListener(InsertValue);

        InsertRecord = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if((_fields[_index].getType() == Type.INT) && (textField1.getText() == ""))
                {
                    p.set_data(0);
                }
                else if((_fields[_index].getType() == Type.DOUBLE) && (textField1.getText() == ""))
                {
                    p.set_data(0);
                }
                else
                {
                    p.set_data(textField1.getText());
                }
                _index++;
                if(_index < DataBaseForm.get_fieldCount())
                {
                    record.Initialize();
                    textField1.setText("");
                }
                else
                {
                    textField1.setText("");
                    DlsStore(p);
                    frame.dispose();
                    _currentRec = _last;
                    enterButton.removeActionListener(InsertRecord);
                    DataBaseForm.getDataForm().setModel();
                }
            }
        };
        enterButton.addActionListener(InsertRecord);
    }
    private void EnterKey()
    {
        frame = new JFrame();
        frame.add(panel1);
        frame.setSize(400,300);
        Title.setText("Enter Key");
        value.setText("Key");
        frame.setVisible(true);

        if((InsertRecord != null) && (enterButton.getActionListeners().length > 0))
        {
            enterButton.removeActionListener(InsertRecord);
            System.out.println("Action Change");
        }
        _currentRec = _first;
        _index = DataBaseForm.get_selectField();
    }
    private void Modify()
    {
        EnterKey();
        InsertValue = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String key = textField1.getText();

                if(Find(key, _index) != null)
                {
                    int result = JOptionPane.showConfirmDialog(null,"해당 레코드를 변경하시겠습니까?",null,JOptionPane.YES_NO_OPTION);
                    if(result == JOptionPane.YES_OPTION)
                    {
                        DeleteRecord();
                        p = new RecordNode();
                        textField1.setText("");
                        Title.setText("Enter Record");
                        Initialize();
                        EnterInformation();
                    }
                }
            }
        };
        enterButton.addActionListener(InsertValue);
        DataBaseForm.set_selectField(-1);
    }

    private void DeleteRecord()
    {
        if(_currentRec == _first)
        {
            p = _first.get_next();
            _first = p;
            _first.set_prior(null);
            _currentRec = _first;
        }   //제거 레코드가 첫 레코드
        else
        {
            if(_currentRec != _last)
            {
                p = _currentRec.get_next();
                p.set_prior(_currentRec.get_prior());
                _currentRec.get_prior().set_next(p);
                _currentRec = p;
            }   //제거 레코드는 처음이나 끝은 아님
            else
            {
                p = _currentRec.get_prior();
                p.set_next(null);
                _last = p;
                _currentRec = p;
            }   //제거 레코드가 마지막
        }
    }

    private void Delete()
    {
        EnterKey();
        InsertValue = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String key = textField1.getText();

                if(Find(key, _index) != null)
                {
                    int result = JOptionPane.showConfirmDialog(null,"해당 레코드를 제거하시겠습니까?",null,JOptionPane.YES_NO_OPTION);
                    if(result == JOptionPane.YES_OPTION)
                    {
                        DeleteRecord();
                    }   //레코드 제거ㅇㅇ
                }   //입력 한 값 존재함
                else
                {
                    JOptionPane.showMessageDialog(null,"해당 값은 없습니다.");
                }
                enterButton.removeActionListener(InsertValue);
                textField1.setText("");
                frame.dispose();
                DataBaseForm.getDataForm().setModel();
            }
        };
        enterButton.addActionListener(InsertValue);
        DataBaseForm.set_selectField(-1);
    }

    private void Search()
    {
        EnterKey();
        InsertValue = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String key = textField1.getText();

                if(Find(key, _index) != null)
                {
                    DataBaseForm.getDataForm().setModel();
                }
                else
                {
                    DataBaseForm.ShowMessage("해당 값이 없습니다.");
                }

                enterButton.removeActionListener(InsertValue);
                textField1.setText("");
                frame.dispose();
            }   //End actionPerformed
        };
        enterButton.addActionListener(InsertValue);
        DataBaseForm.set_selectField(-1);
    }

    private RecordNode Find(String Key, int index)      //current Record부터 시작해서 last Record까지 같은 값을 가지는 항목이 있는지 조사 후 currentRecord 리턴
    {
        for(int i = 0; _currentRec != _last;i++)
        {
            if(Key.matches(_currentRec.get_data(index).toString()))
            {
                int result = JOptionPane.showConfirmDialog(null,(i+1)+"번 레코드에서 찾았습니다. 계속해서 찾으시겠습니까?",null,JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION)
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
        if(_currentRec == _last)
        {
            if(Key.matches(_currentRec.get_data(index).toString()))
            {
                JOptionPane.showMessageDialog(null,"마지막 레코드에서 찾았습니다.");
                return _currentRec;
            }
        }
        return null;
    }

    private void DlsStore(RecordNode newNode)
    {
        Type type = DataBaseForm.getFields()[DataBaseForm.get_sortField()].getType();
        String pString;
        String newString;
        RecordNode old, p;
        if(_last == null)
        {
            System.out.println("First");
            newNode.set_next(null);
            newNode.set_prior(null);
            _last = newNode;
            _first = newNode;
            return;
        }
        p = _first;
        old = null;

        while(p != null)
        {
            pString = p.get_data(DataBaseForm.get_sortField()).toString();
            newString = newNode.get_data(DataBaseForm.get_sortField()).toString();

            if(pString.isEmpty() && (type == Type.INT))
            {
                pString = "0";
            }
            else if(type == Type.DOUBLE)
            {
                pString = new String("0");
            }
            if(newString.isEmpty() && ((type == Type.INT)|(type == Type.DOUBLE)))
            {
                newString = new String("0");
            }
            else if(type == Type.DOUBLE)
            {
                newString = new String("0");
            }
            if(((type == Type.STRING) || (type == Type.CHAR)) && (pString.compareTo(
                    newString) < 0))
            {
                System.out.println("첫번째 if문");
                old = p;
                p = p.get_next();
            }
            else if(((type == Type.INT) && (Integer.parseInt(pString) < Integer.parseInt(newString))))
            {
                old = p;
                p = p.get_next();
            }
            else if(((type == Type.DOUBLE) && (Double.parseDouble(pString) < Double.parseDouble(newString))))
            {
                old = p;
                p = p.get_next();
            }
            else
            {
                if(p.get_prior() != null)
                {
                    System.out.println("두번째 if문");
                    System.out.println("p?"+p.get_prior() == null);
                    p.get_prior().set_next(newNode);
                    newNode.set_next(p);
                    newNode.set_prior(p.get_prior());
                    p.set_prior(newNode);
                    return;
                }
                System.out.println("두번째 if문 아님");
                newNode.set_next(p);
                newNode.set_prior(null);
                p.set_prior(newNode);
                _first = newNode;
                return;
            }
        }
        System.out.println("p는 마지막!!");
        old.set_next(newNode);
        newNode.set_next(null);
        newNode.set_prior(old);
        _last = newNode;
    }


    @Override
    public void keyTyped(KeyEvent e)
    {
        char c;
        if(textField1.getText().length() < _fields[_index].getSize())
        {
            switch (_fields[_index].getType())
            {
                case INT:
                    c = e.getKeyChar();
                    if (!Character.isDigit(c))
                    {
                        e.consume();
                        return;
                    }
                    break;
                case DOUBLE:
                    c = e.getKeyChar();
                    if (!Character.isDigit(c))
                    {
                        if(!((c == '.') && !textField1.getText().contains(".")))
                        {
                            e.consume();
                            return;
                        }
                    }
                    break;
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

    public RecordNode get_first()
    {
        return _first;
    }
    public void set_first(RecordNode p) {_first = p;}
    public RecordNode get_last()
    {
        return _last;
    }
    public void set_last(RecordNode p) {_last = p;}
    public RecordNode get_currentRec() {return _currentRec; }
    public void set_currentRec(RecordNode p) {_currentRec = p; }
    public void setRecord(Record record) { this.record = record; }
    public void setSearchField() {Search();}
    public void setDeleteRecord() {Delete();}
    public void setModifyRecord() {Modify();}
}
