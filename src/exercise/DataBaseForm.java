package exercise;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DataBaseForm implements MouseListener{
    private JPanel Main;
    private JPanel Buttons;
    private JButton Define;
    private JButton Enter;
    private JButton Search;
    private JButton Modify;
    private JButton Delete;
    private JButton Print;
    private JButton Load;
    private JButton Save;
    private JPanel Menu;
    private JPanel Board;
    private JPanel Title;
    private JLabel TitleLabel;
    private JPanel BoardButton;
    private JButton next;
    private JButton prior;
    private JTable table1;
    private JButton refreshButton;

    private DefaultTableModel model;

    private static int _selectField;
    private static int _fieldCount;
    private static int _sortField;
    private static Field[] _fields;
    private static DataBaseForm dataForm;
    private Record record;

    public static void main(String[] args) throws Exception
    {
        dataForm = new DataBaseForm();
    }

    public DataBaseForm()
    {
        _selectField = -1;
        _sortField = -1;
        JFrame main = new JFrame();
        record = new Record();
        record.setRecord(record);
        main.add(Main);
        main.setSize(800,600);
        main.setVisible(true);
        table1.addMouseListener(this);
        Define.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Init();
                FieldCheck check = new FieldCheck(dataForm);
            }
        });
        Enter.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                record.Enter();
            }
        });
        Search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(_selectField < 0)
                {
                    DataBaseForm.ShowMessage("Field를 선택하세요!");
                    return;
                }
                else
                {
                    record.setSearchField();
                }
            }
        });
        Modify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(_selectField < 0)
                {
                    DataBaseForm.ShowMessage("Field를 선택하세요!");
                    return;
                }
                else
                {
                    record.setModifyRecord();
                }
            }
        });
        Delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(_selectField < 0)
                {
                    DataBaseForm.ShowMessage("Field를 선택하세요!");
                    return;
                }
                else
                {
                    record.setDeleteRecord();
                }
            }
        });
        Print.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        Save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        Load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                MakeModel();
            }
        });
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(record.get_currentRec() == null)
                {
                    DataBaseForm.ShowMessage("레코드가 비었습니다.");
                    return;
                }
                if(record.get_currentRec().get_next() != null)
                {
                    record.set_currentRec(record.get_currentRec().get_next());
                    MakeModel();
                    return;
                }
                else
                {
                    DataBaseForm.ShowMessage("마지막 레코드 입니다.");
                    return;
                }

            }
        });
        prior.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(record.get_currentRec() == null)
                {
                    DataBaseForm.ShowMessage("레코드가 비었습니다.");
                    return;
                }
                if(record.get_currentRec().get_prior() != null)
                {
                    record.set_currentRec(record.get_currentRec().get_prior());
                    MakeModel();
                    return;
                }
                else
                {
                    DataBaseForm.ShowMessage("첫번째 레코드 입니다.");
                    return;
                }
            }
        });
    }
    private void MakeModel()
    {
        int recordIndex = 1;
        String[] head = {"RecordNumber","Field","Record"};
        String[][] contents = new String[_fieldCount][3];

        if((record.get_currentRec() != null) && record.get_first() != null)
        {
            RecordNode p = record.get_first();
            while(p != record.get_currentRec())
            {
                recordIndex++;
                p = p.get_next();
            }
        }
        for(int i = 0; i < _fieldCount; i++)
        {

            contents[i][1] = _fields[i].getName();      //Field의 Name넣기

            if(record.get_currentRec() != null && (record.get_currentRec().get().size() >= _fieldCount))
            {
                contents[i][2] = record.get_currentRec().get_data(i).toString();   //나중에 레코드노드 만들게 되면 이런 형식으로
            }
            else
            {
                contents[i][2] = null;
            }
            contents[i][0] = Integer.toString(recordIndex);
        }
        model = new DefaultTableModel(contents,head)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;                                                       //Table 정보 수정 막아놓음
            }
        };
        table1.setModel(model);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        table1.setRowHeight(30);                                                    //Cell의 높이 지정
        table1.getColumn("Field").setCellRenderer(renderer);              //가운데 정렬
        table1.getColumn("Record").setCellRenderer(renderer);
        table1.getColumn("RecordNumber").setCellRenderer(renderer);
    }

    private void Init()
    {
        RecordNode temp;
        while(record.get_last() != null)
        {
            temp = record.get_last().get_prior();
            if(temp != null)
            {
                record.get_last().set_data(null);
                record.set_last(null);
            }
            record.set_last(temp);
        }
        if(record.get_first() != null)
        {
            record.get_first().set_data(null);
            record.set_first(null);
        }
        if(record.get_currentRec() != null)
        {
            record.get_currentRec().set_data(null);
            record.set_currentRec(null);
        }
    }
    public static void ShowMessage(String message)
    {
        JOptionPane.showMessageDialog(null,message);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        _selectField = table1.getSelectedRow();
        System.out.println(_selectField);
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    public static Field[] getFields()
    {
        return _fields;
    }
    public static void setFields(Field[] fields){ _fields = fields;}
    public static int get_fieldCount() {return _fieldCount;}
    public static void  set_fieldCount(int value){_fieldCount = value;}
    public DefaultTableModel getModel() {return model;}
    public void setModel(){MakeModel();}
    public static int get_sortField() {return _sortField;}
    public static void set_sortField(int value) {_sortField = value;}
    public static int get_selectField() {return _selectField; }
    public static void set_selectField(int value) {_selectField = value;}
    public static DataBaseForm getDataForm() {return dataForm;}
}
