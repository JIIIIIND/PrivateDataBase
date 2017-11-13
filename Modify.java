import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Modify{

    private ActionListener InsertValue;
    private static Modify instance;
    private static Record record;

    private Modify() { }

    public static void getInstance()
    {
        if(instance == null)
            instance = new Modify();
        record = Record.getInstance();
        instance.RecordModify();
    }

    private void RecordModify()
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
                    int result = JOptionPane.showConfirmDialog(null, "해당 레코드를 변경하시겠습니까?", null, JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION)
                    {
                        //해당 레코드를 삭제하고 새로운 레코드를 입력 받는다.
                        record.getDeleteRecord();
                        record.getTextField1().setText("");
                        record.getFrame().dispose();
                        Enter.getInstance(record.get_index());
                    }
                }
                record.getTextField1().setText("");
                record.getFrame().dispose();
                DataBaseForm.ShowMessage("해당 값은 없습니다.");
                record.getEnterButton().removeActionListener(InsertValue);
            }
            //End ActionListener
        };
        record.getEnterButton().addActionListener(InsertValue);
        DataBase.set_selectField(-1);
    }
}
