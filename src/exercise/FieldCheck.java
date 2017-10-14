package exercise;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class FieldCheck implements ActionListener, KeyListener, MouseListener
{
    private JFrame frame;
    private JTextField EnterNumber;
    private JButton insertValueButton;
    private JPanel panel1;
    private JPanel panel2;
    private JButton insertValueButton1;
    private JTextField EnterName;
    private JTextField EnterType;
    private JTextField EnterRecordSize;
    private JLabel Counter;
    private JPanel panel3;
    private JTable table1;
    private CardLayout cards;

    private int _fieldCounter;
    private int _index;
    private Field[] fields;
    private DataBaseForm mainForm;

    public FieldCheck(DataBaseForm database)
    {
        mainForm = database;
        cards = new CardLayout();
        frame = new JFrame();
        frame.setLayout(cards);
        frame.getContentPane().add("Count",panel1);
        frame.getContentPane().add("Value",panel2);
        frame.getContentPane().add("Sort",panel3);
        frame.setVisible(true);
        frame.setSize(500,300);
        insertValueButton.addActionListener(this::actionPerformed);
        insertValueButton1.addActionListener(this::actionPerformed);
        EnterNumber.addKeyListener(this);
        EnterRecordSize.addKeyListener(this);
        _index = 0;
    }

    private void numField()
    {
        String value = EnterNumber.getText();
        _fieldCounter = Integer.parseInt(value);
        mainForm.set_fieldCount(_fieldCounter);
        mainForm.setFields(new Field[_fieldCounter]);
        System.out.println("count :"+mainForm.getFields().length);
        cards.next(frame.getContentPane());
        Counter.setText("Field["+Integer.toString(_index)+"]");
    }
    private void InsertValue()
    {
        fields = mainForm.getFields();
        fields[_index] = new Field();
        fields[_index].setName(EnterName.getText());

        String type;

        type = EnterType.getText();
        type = type.toLowerCase();
        switch(type.charAt(0))
        {
            case 's':
                fields[_index].setType(Type.STRING);
                break;
            case 'd':
                fields[_index].setType(Type.DOUBLE);
                break;
            case 'i':
                fields[_index].setType(Type.INT);
                break;
            case 'c':
                fields[_index].setType(Type.CHAR);
                break;
            default:
                JOptionPane.showMessageDialog(frame,"String, Double, Int, Char 중 선택하십시오");
                return;
        }
        String size = EnterRecordSize.getText();
        if(fields[_index].getType() == Type.CHAR)
        {
            size = "1";
        }
        fields[_index].setSize(Integer.parseInt(size));

        EnterRecordSize.setText("");
        EnterType.setText("");
        EnterName.setText("");
        _index++;
        Counter.setText("Field["+Integer.toString(_index)+"]");
        if(_index == _fieldCounter)
        {
            mainForm.setModel();
            cards.next(frame.getContentPane());
            String[] columns = {"RecordNumber","Field", "Record"};
            String[][] data = new String[_fieldCounter][3];
            for(int i = 0; i < _fieldCounter; i++)
            {
                data[i][0] = Integer.toString(1);
                data[i][1] = fields[i].getName();
                data[i][2] = null;

            }
            DefaultTableModel model = new DefaultTableModel(data,columns)
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
            table1.getColumn("RecordNumber").setCellRenderer(renderer);
            table1.getColumn("Field").setCellRenderer(renderer);              //가운데 정렬
            table1.getColumn("Record").setCellRenderer(renderer);
            table1.addMouseListener(this);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if((e.getSource() == insertValueButton) && EnterNumber.getText().length() != 0)
        {
            numField();
        }
        else if((e.getSource() == insertValueButton1))
        {
            if(!EnterName.getText().isEmpty() && !EnterType.getText().isEmpty() && !EnterRecordSize.getText().isEmpty())
            {
                InsertValue();
                System.out.println("name : "+fields[_index-1].getName()+
                        " Type : "+fields[_index-1].getType()+
                        " size : "+fields[_index-1].getSize());
            }
            else
                JOptionPane.showMessageDialog(frame,"값을 입력하고 버튼을 누르세요");
        }
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        char c = e.getKeyChar();
        if(!Character.isDigit(c))
        {
            e.consume();
            return;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        int row = table1.getSelectedRow();
        System.out.println(row);
        DataBaseForm.set_sortField(row);
        frame.dispose();
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }
}
