import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Enter{

    private static Enter instance;
    private RecordNode p;
    private static Record record;
    ActionListener InsertRecord;

    private Enter() { }

    public static void getInstance(int index)
    {
        if(instance == null)
            instance = new Enter();
        record = DataBase.get_record();
        instance.EnterRecord(index);
    }
    private void EnterRecord(int index)
    {
        record.getMakeForm("Enter Record", "Field");
        EnterInformation();
        record.set_index(index);
        p = new RecordNode();
        record.getTextField1().addKeyListener(record);
        EnterInitialize();
    }

    //Form의 내용 변경 및 NullReference 오류 예방
    private void EnterInitialize()
    {
        if (DataBase.get_fields() == null) {
            DataBaseForm.ShowMessage("Define 또는 Load를 먼저 실행하십시오");
            record.getFrame().dispose();
            return;
        }

        record.set_fields();

        //하나라도 값이 비었다면 전체가 비었기 때문에 하나만 검사
        if (record.get_fields()[record.get_index()] == null)
        {
            DataBaseForm.ShowMessage("Field의 값을 정확히 입력 후 실행하십시오");
            record.getFrame().dispose();
            return;
        }

        record.getValue().setText(record.get_fields()[record.get_index()].getName());
    }

    //레코드 정보 입력
    private void EnterInformation() {
        InsertRecord = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((record.get_fields()[record.get_index()].getType() == Type.INT) && (record.getTextField1().getText() == "")) {
                    p.set_data(0);
                } else if ((record.get_fields()[record.get_index()].getType() == Type.DOUBLE) && (record.getTextField1().getText() == "")) {
                    p.set_data(0);
                } else {
                    p.set_data(record.getTextField1().getText());
                }
                record.set_index(record.get_index()+1);
                if (record.get_index() < DataBase.get_fieldCount()) {
                    EnterInitialize();
                    record.getTextField1().setText("");
                } else {
                    record.getTextField1().setText("");
                    record.setDlsStore(p);
                    record.set_currentRec(record.get_last());
                    record.getEnterButton().removeActionListener(InsertRecord);
                    DataBase.get_form().setModel();
                    record.getFrame().dispose();
                }
            }
        };
        record.getEnterButton().addActionListener(InsertRecord);
    }
}
