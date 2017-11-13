import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DataBaseForm implements MouseListener
{
    private JButton next;               //다음 레코드 보이기
    private JButton prior;              //이전 레코드 보이기
    private JButton refreshButton;      //새로고침
    private JTable table1;              //레코드를 보이는 장소
    private JButton DefineBtn;
    private JButton EnterBtn;
    private JButton SearchBtn;
    private JButton ModifyBtn;
    private JButton DeleteBtn;
    private JButton LoadBtn;
    private JButton SaveBtn;
    private JPanel Main;

    private Record record;
    FieldCheck check;

    public DataBaseForm()
    {
        JFrame frame = new JFrame();
        frame.add(Main);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setVisible(true);
        table1.addMouseListener(this);
        record = DataBase.get_record();

        //필드 정의
        DefineBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Define을 두번이상 누를 경우 대비/앞의 내용 다 지움
                Record.getInstance().set_Init();
                check = new FieldCheck();
            }
        });

        //레코드 입력
        EnterBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Enter.getInstance(0);
            }
        });

        //레코드 검색
        SearchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (DataBase.get_selectField() < 0) {
                    DataBaseForm.ShowMessage("Field를 선택하세요!");
                    return;
                } else {
                    Search.getInstance();
                }
            }
        });

        //레코드 수정
        ModifyBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (DataBase.get_selectField() < 0)
                {
                    DataBaseForm.ShowMessage("Field를 선택하세요!");
                    return;
                }
                else
                {
                    Modify.getInstance();
                }
            }
        });

        //레코드 삭제
        DeleteBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (DataBase.get_selectField() < 0)
                {
                    DataBaseForm.ShowMessage("Field를 선택하세요!");
                    return;
                }
                else
                {
                    Delete.getInstance();
                }
            }
        });

        //파일 저장
        SaveBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SaveLoad sl = new SaveLoad(true);
            }
        });

        //파일 로드
        LoadBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SaveLoad sl = new SaveLoad(false);
            }
        });

        //새로고침
        refreshButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(DataBase.IsFullField())
                    setModel();
            }
        });

        //다음 레코드로 이동
        next.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (record.get_currentRec() == null)
                {
                    DataBaseForm.ShowMessage("레코드가 비었습니다.");
                    return;
                }
                if (record.get_currentRec().get_next() != null)
                {
                    record.set_currentRec(record.get_currentRec().get_next());
                    setModel();
                    return;
                }
                else    //마지막 레코드에서 누르면 실행
                {
                    DataBaseForm.ShowMessage("마지막 레코드 입니다.");
                    return;
                }
            }
        });

        //이전 레코드로 이동
        prior.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //레코드에 값이 없을 때
                if (record.get_currentRec() == null)
                {
                    DataBaseForm.ShowMessage("레코드가 비었습니다.");
                    return;
                }
                if (record.get_currentRec().get_prior() != null)
                {
                    record.set_currentRec(record.get_currentRec().get_prior());
                    setModel();
                    return;
                }
                else    //첫번째 레코드에서 누르면 실행
                {
                    DataBaseForm.ShowMessage("첫번째 레코드 입니다.");
                    return;
                }
            }
        });
    }

    //테이블 가운데 정렬, 높이 지정
    public void setModel()
    {
        table1.setModel(record.getMakeModel());

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        table1.setRowHeight(30);                                                    //Cell의 높이 지정
        table1.getColumn("Field").setCellRenderer(renderer);              //가운데 정렬
        table1.getColumn("Record").setCellRenderer(renderer);
        table1.getColumn("RecordNumber").setCellRenderer(renderer);
    }

    //알림창 생성
    public static void ShowMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        DataBase.set_selectField(table1.getSelectedRow());
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
