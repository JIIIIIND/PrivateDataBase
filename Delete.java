import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Delete{

    private ActionListener InsertValue;
    private static Delete instance;
    private static Record record;

    private Delete() {}

    public static void getInstance()
    {
        if(instance == null)
            instance = new Delete();
        record = Record.getInstance();
        instance.RecordDelete();
    }

    //레코드 삭제를 위한 초기 단계(키 입력, 삭제 레코드 찾기)
    private void RecordDelete()
    {
        record.getMakeForm("Enter Key","Key");
        record.set_currentRec(record.get_first());
        record.set_index(DataBase.get_selectField());

        InsertValue = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String key = record.getTextField1().getText();

                if (record.getFind(key, record.get_index()) != null)
                {
                    int result = JOptionPane.showConfirmDialog(null, "해당 레코드를 제거하시겠습니까?", null, JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION)
                    {
                        record.getDeleteRecord();
                    }   //레코드 제거
                }   //입력 한 값 존재함
                else
                {
                    JOptionPane.showMessageDialog(null, "해당 값은 없습니다.");
                }
                record.getTextField1().setText("");
                record.getFrame().dispose();
                record.getEnterButton().removeActionListener(InsertValue);
                DataBase.get_form().setModel();
            }
        };
        record.getEnterButton().addActionListener(InsertValue);
        DataBase.set_selectField(-1);
    }
}
